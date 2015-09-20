package fr.elefort.piratesremastered.model;

import android.graphics.RectF;
import android.util.Log;

import fr.elefort.piratesremastered.model.gravity.GravityEngine;

/**
 * Created by Eric on 14/09/2015.
 */
public class Player extends Movable {

    public Player (RectF rect, GravityEngine gravity){
        super (rect, gravity);
        getStats().lifes = 5;
    }

    @Override
    public void notifyContact(Movable movable) {
        Log.d("Player", this + " contact with " + movable);
        if(movable instanceof Player)
            notifyContact((Player)movable);
    }

    @Override
    public boolean canAttachToPlatform(Obstacle obstacle, GravityEngine gravity) {
        if (getObstacle() == null)
            return true;

        if (this.getGravity().equals(gravity))
            return true;

        return impulsion > 0;
    }

    public void notifyContact(Player player) {
        if (this.getStats().wounded || player.getStats().wounded || Math.abs(this.velocity) == Math.abs(player.velocity))
            return;

        if (Math.abs(this.velocity) > Math.abs(player.velocity)) {
            player.getStats().hurt();
        }

        if (Math.abs(this.velocity) < Math.abs(player.velocity)) {
            this.getStats().hurt();
        }
        if (Math.abs(player.velocity) != Math.abs(this.velocity)) {
            player.switchDirection();
            this.switchDirection();
        }
    }
}
