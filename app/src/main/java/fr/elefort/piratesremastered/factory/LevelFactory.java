package fr.elefort.piratesremastered.factory;

import android.content.Context;
import android.graphics.RectF;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import fr.elefort.piratesremastered.model.Bonus;
import fr.elefort.piratesremastered.model.Platform;
import fr.elefort.piratesremastered.model.Player;
import fr.elefort.piratesremastered.model.gravity.Gravity;
import fr.elefort.piratesremastered.model.Level;

/**
 * Created by Eric on 18/09/2015.
 */
public class LevelFactory {
    private final static char OBSTACLE = 'x';

    private final Context context;

    private final ArrayList<String> cached = new ArrayList<>();
    private boolean[][] obstacles;
    private Level currentLevel;

    private final BonusFactory bonusFactory = new BonusFactory();

    public LevelFactory(Context context) {
        this.context = context;
    }

    public Level createLevel(BufferedReader reader) {
        try {
            build(reader);
        } catch (IOException e) {
            return null;
        }
        return currentLevel;
    }

    private void build(BufferedReader reader) throws IOException {
        initLevel(reader);

        int width = currentLevel.getAbsWidth() + 2;
        int height = currentLevel.getHeight() + 2;

        obstacles = new boolean[width][];
        for(int i = 0; i < obstacles.length; i++) {
            obstacles[i] = new boolean[height];
        }

        for(int y = 0; y < cached.size(); y++) {
            String line = cached.get(y);

            for(int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);

                if (isObstacle(c)){
                    createObstacle(x, y);
                    continue;
                }
                if(isPlayer(c)){
                    createPlayer(x, y, c);
                    continue;
                }
                if(isBonus(c)){
                    createBonus(x, y, c);
                    continue;
                }
            }
        }
        currentLevel.setObstacles(obstacles);
        createPlatforms();
    }

    private void createPlatforms() {
        boolean[][] obstacles = new boolean[this.obstacles.length][];
        for(int i = 0; i < this.obstacles.length; i++)
        {
            boolean[] aMatrix = this.obstacles[i];
            int   aLength = aMatrix.length;
            obstacles[i] = new boolean[aLength];
            System.arraycopy(aMatrix, 0, obstacles[i], 0, aLength);
        }

        createHorizontalPlatforms(obstacles);
        createVerticalPlatforms(obstacles);
    }

    private void createHorizontalPlatforms(boolean[][] obstacles) {
        boolean draw = false;
        int fromX = 0;
        int fromY = 0;
        int width = currentLevel.getAbsWidth() + 2;
        int height = currentLevel.getAbsHeight() + 2;

        for(int x = 1; x < width - 1; x++){
            for(int y = 1; y < height - 1; y++){
                if(obstacles[x][y]){
                    if(!draw){
                        fromX = x-1;
                        fromY = y-1;
                    }
                    draw = true;
                }else{
                    if(draw){
                        if(fromY + 1 < y - 1){
                            for(int i = fromY + 1; i < y; i++){
                                obstacles[x][i] = false;
                            }
                            float left = fromX;
                            float top = fromY;
                            float right = left + 1;
                            float bottom = top + y - 1 - fromY;
                            currentLevel.platforms.add(new Platform(new RectF(left * currentLevel.MOVE_STEP, top * currentLevel.MOVE_STEP, right * currentLevel.MOVE_STEP, bottom * currentLevel.MOVE_STEP), Gravity.LEFT));
                        }
                        draw = false;
                    }
                }
            }
            if(draw){
                if(fromY + 1 < height - 2){
                    for(int i = fromY + 1; i < height; i++){
                        obstacles[x][i] = false;
                    }
                    float left = fromX;
                    float top = fromY;
                    float right = left + 1;
                    float bottom = top + height - 2;
                    currentLevel.platforms.add(new Platform(new RectF(left * currentLevel.MOVE_STEP, top * currentLevel.MOVE_STEP, right * currentLevel.MOVE_STEP, bottom * currentLevel.MOVE_STEP), Gravity.RIGHT));
                }
            }
            draw = false;
        }
    }

    private void createVerticalPlatforms(boolean[][] obstacles) {
        boolean draw = false;
        int fromX = 0;
        int fromY = 0;
        int width = currentLevel.getAbsWidth() + 2;
        int height = currentLevel.getAbsHeight() + 2;

        for(int y = 1; y < height - 1; y++){
            for(int x = 1; x < width - 1; x++){
                if(obstacles[x][y]){
                    if(!draw){
                        fromX = x-1;
                        fromY = y-1;
                    }
                    draw = true;
                }else{
                    if(draw){
                        if(fromX + 1 <= x - 1){
                            float left = fromX;
                            float top = fromY;
                            float right = left + x - 1 - fromX;
                            float bottom = top + 1;
                            currentLevel.platforms.add(new Platform(new RectF(left * currentLevel.MOVE_STEP, top * currentLevel.MOVE_STEP,right * currentLevel.MOVE_STEP, bottom * currentLevel.MOVE_STEP), Gravity.UP));
                        }
                        draw = false;
                    }
                }
            }
            if(draw){
                if(fromX + 1 <= width - 2){
                    float left = fromX;
                    float top = fromY;
                    float right = left + width - 2;
                    float bottom = top + 1;
                    currentLevel.platforms.add(new Platform(new RectF(left * currentLevel.MOVE_STEP, top * currentLevel.MOVE_STEP, right * currentLevel.MOVE_STEP, bottom * currentLevel.MOVE_STEP), Gravity.DOWN));
                }
            }
            draw = false;
        }
    }

    private boolean isPlayer(char c) {
        int player = Character.getNumericValue(c);
        return 0 < player && player <= 4;
    }

    private void createPlayer(int x, int y, char c) {
        int playerId = Character.getNumericValue(c);
        Player player = PlayerFactory.createPlayer(context, PlayerFactory.loadSkins(context.getApplicationContext())[playerId - 1], currentLevel, playerId, 2);
        if(player == null)
            return;
        player.setPosition(x * currentLevel.MOVE_STEP, y * currentLevel.MOVE_STEP);
        currentLevel.players.add(player);
    }

    private boolean isBonus(char c) {
        return true;
    }

    private void createBonus(int x, int y, char c) {
        Bonus bonus = bonusFactory.createBonus(context, currentLevel, c);
        if (bonus == null)
            return;
        bonus.setPosition(x * currentLevel.MOVE_STEP, y * currentLevel.MOVE_STEP);
        currentLevel.bonusItems.add(bonus);
    }

    private boolean isObstacle(char c) {
        return c == OBSTACLE;
    }

    private void createObstacle(int x, int y) {
        obstacles[x+1][y+1] = true;
    }

    private void initLevel(BufferedReader reader) throws IOException {
        int width = 0;
        int height = 0;

        String mLine = reader.readLine();
        while (mLine != null) {
            cached.add(mLine);
            width = Math.max(mLine.length(), width);
            height++;
            mLine = reader.readLine();
        }

        currentLevel = new Level(width, height);
    }
}
