package fr.elefort.piratesremastered.model;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.elefort.piratesremastered.model.bonus.BonusPool;
import fr.elefort.piratesremastered.views.GameSurfaceViewInterface;

/**
 * Created by Eric on 13/09/2015.
 */
public class GameModel {
    private final Random generator = new Random(System.currentTimeMillis());

    public final ArrayList<Player> players = new ArrayList<>();
    public final ArrayList<Obstacle> obstacles = new ArrayList<>();
    public final ArrayList<Bonus> bonusItems = new ArrayList<>();
    public final ArrayList<Movable> movables = new ArrayList<>();
    public final ArrayList<View.OnTouchListener> touchListeners = new ArrayList<>();

    private BonusPool bonusPool;
    private final Context context;

    private int countDown;
    private GameSurfaceViewInterface view;
    private Level level;
    private Texture texture;

    public GameModel(Context context) {
        this.context = context;
    }

    public int getTimer() {
        return countDown;
    }

    private void deregisterAllPlayers(List<Player> items) {
        this.players.removeAll(items);
        this.movables.removeAll(items);
    }

    private void deregisterAllBonus(List<Bonus> items) {
        this.bonusItems.removeAll(items);
        this.movables.removeAll(items);
        this.bonusPool.recycleAll(items);
    }

    private void registerAllPlayers(List<Player> items) {
        for(Player player : items) {
            players.add(player);
            movables.add(player);
            touchListeners.add(player);
        }
    }

    private void registerBonus(Bonus bonus) {
        bonusItems.add(bonus);
        movables.add(bonus);
    }

    private void registerAllBonus(List<Bonus> items) {
        for(Bonus bonus : items) {
            registerBonus(bonus);
        }
    }

    private void registerAllPlatforms(List<Platform> items) {
        for(Platform platform : items) {
            obstacles.add(platform);
        }
    }

    public boolean endOfTheGame() {
        return players.size() <= 1 || countDown <= 0;
    }

    public void update(long timeDelta) {
        if(timeDelta > 1000){
            offerBonus();
        }

        for (Movable movable : movables) {
            movable.update(timeDelta);
            for (Obstacle obstacle : obstacles){
                movable.onContact(obstacle);
            }
            for (Movable others : movables){
                movable.onContact(others);
            }
        }
        updatePlayers(timeDelta);
        updateBonus(timeDelta);
    }

    private void offerBonus() {
        Bonus bonus = bonusPool.peek();
        if(bonus != null) {
            Obstacle obstacle = obstacles.get(generator.nextInt(obstacles.size() - 1));
            bonus.setPosition((int)obstacle.getHitBox().right, (int)obstacle.getHitBox().bottom);
            registerBonus(bonus);
        }
    }

    private void updatePlayers(long timeDelta) {
        ArrayList<Player> playersNotAlive = new ArrayList<>();
        for (Player player : players) {
            if (!player.isAlive()) {
                playersNotAlive.add(player);
            }
        }
        deregisterAllPlayers(playersNotAlive);
    }

    private void updateBonus(long timeDelta) {
        ArrayList<Bonus> bonusConsumed = new ArrayList<>();
        for (Bonus bonus : bonusItems) {
            if (!bonus.isAlive() || !level.getRectF().contains(bonus.getPosition().x, bonus.getPosition().y)) { //!Geometry.intersect(level.getRectF(), bonus.getHitBox())) {
                bonusConsumed.add(bonus);
            }
        }
        deregisterAllBonus(bonusConsumed);
    }

    public void setView(GameSurfaceViewInterface view) {
        this.view = view;
    }

    public void setLevel(Level level) {
        this.level = level;
        this.bonusPool = new BonusPool(context, level, 5);

        this.players.clear();
        this.bonusItems.clear();
        this.movables.clear();
        this.obstacles.clear();
        this.touchListeners.clear();

        registerAllPlayers(level.players);
        registerAllBonus(level.bonusItems);
        registerAllPlatforms(level.platforms);
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Level getLevel() {
        return level;
    }

    public Texture getTexture() {
        return texture;
    }
}