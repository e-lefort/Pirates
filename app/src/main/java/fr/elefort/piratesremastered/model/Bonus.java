package fr.elefort.piratesremastered.model;

import android.graphics.RectF;

import fr.elefort.piratesremastered.model.bonus.BonusAction;
import fr.elefort.piratesremastered.model.gravity.GravityEngine;

/**
 * Created by Eric on 20/09/2015.
 */
public class Bonus extends Movable {

    private BonusAction action;

    public Bonus(RectF rect, GravityEngine gravity, BonusAction action) {
        super(rect, gravity);
        this.action = action;
        getStats().lifes = 1;
    }

    @Override
    public void onContact(Movable movable) {

    }

    public void setBonusAction(BonusAction action) {
        this.action = action;
    }

    @Override
    public void notifyContact(Movable movable) {
        if (movable instanceof Player) {
            action.perform((Player)movable);
            getStats().hurt();
        }
    }

    @Override
    public boolean canAttachToPlatform(Obstacle obstacle, GravityEngine gravity) {
        return true;
    }
}
