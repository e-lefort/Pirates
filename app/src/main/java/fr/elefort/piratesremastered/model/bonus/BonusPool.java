package fr.elefort.piratesremastered.model.bonus;

import android.content.Context;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import fr.elefort.piratesremastered.factory.BonusFactory;
import fr.elefort.piratesremastered.model.Bonus;
import fr.elefort.piratesremastered.model.Level;

/**
 * Created by Eric on 20/09/2015.
 */
public class BonusPool {
    private final Context context;
    private final Level level;
    private final int capacity;

    private final Random generator = new Random(System.currentTimeMillis());
    private final BonusFactory factory = new BonusFactory();
    private final ArrayBlockingQueue<Bonus> pool;
    private final BonusItem[] bonusList;

    public BonusPool(Context context, Level level, int capacity) {
        this.context = context;
        this.level = level;
        this.capacity = capacity;
        this.pool = new ArrayBlockingQueue<>(capacity);
        this.bonusList = BonusItem.values();
        createRandom();
    }

    private void createRandom() {
        for(int i = 0; i < capacity; i++) {
            pool.add(getRandomBonus());
        }
    }

    public Bonus peek() {
        Bonus bonus = pool.poll();
        if (bonus == null)
            return null;

        if (pool.size() % 2 == 0)
            bonus.switchDirection();
        return bonus;
    }

    public void recycle(Bonus bonus) {
        if (pool.size() >= capacity)
            return;
        bonus.getStats().lifes = 1;
        bonus.getStats().wounded = false;
        BonusAction action = getRandomBonusAction();
        bonus.setAnimation(action.getAnimation(context));
        bonus.setName(action.getName());
        bonus.setBonusAction(action);
        pool.offer(bonus);
    }

    private BonusAction getRandomBonusAction() {
        int i = Math.abs(generator.nextInt(bonusList.length));
        return bonusList[i];
    }

    private Bonus getRandomBonus() {
        Bonus bonus = factory.createBonus(context, level, getRandomBonusAction().getValue());
        return bonus;
    }

    public void recycleAll(List<Bonus> items) {
        for (Bonus item : items) {
            recycle(item);
        }
    }
}
