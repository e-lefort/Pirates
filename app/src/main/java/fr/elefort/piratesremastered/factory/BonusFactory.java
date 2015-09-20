package fr.elefort.piratesremastered.factory;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.SparseArray;

import java.util.ArrayList;

import fr.elefort.piratesremastered.model.Animation;
import fr.elefort.piratesremastered.model.Bonus;
import fr.elefort.piratesremastered.model.Level;
import fr.elefort.piratesremastered.model.bonus.BonusAction;
import fr.elefort.piratesremastered.model.bonus.BonusItem;
import fr.elefort.piratesremastered.model.gravity.Gravity;

/**
 * Created by Eric on 20/09/2015.
 */
public class BonusFactory {
    private final SparseArray<BonusAction> bonusActions = new SparseArray<>();

    public BonusFactory() {
        for (BonusItem item: BonusItem.values()) {
            bonusActions.put(item.getValue(), item);
        }
    }

    public int Count() {
        return bonusActions.size();
    }

    public Bonus createBonus(Context context, Level level, char id){
        BonusAction bonusAction = bonusActions.get(id);

        if(bonusAction == null)
            return null;

        Animation animation = bonusAction.getAnimation(context);

        Bitmap bitmap = animation.getBitmap();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scale = Math.min((float) level.MOVE_STEP*0.5f / width, (float) level.MOVE_STEP*0.5f / height);

        Bonus bonus = new Bonus(new RectF(0, 0, (width * scale), (height * scale)), Gravity.UP, bonusAction);
        bonus.setName(bonusAction.getName());
        bonus.setAnimation(animation);
        return bonus;
    }
}
