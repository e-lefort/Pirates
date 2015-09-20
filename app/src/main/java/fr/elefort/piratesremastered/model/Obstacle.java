package fr.elefort.piratesremastered.model;

import android.graphics.RectF;
import android.util.Log;

import fr.elefort.piratesremastered.maths.Geometry;
import fr.elefort.piratesremastered.model.gravity.Gravity;
import fr.elefort.piratesremastered.model.gravity.GravityEngine;

/**
 * Created by Eric on 14/09/2015.
 */
public abstract class Obstacle extends Movable {

    public Obstacle (RectF rect, GravityEngine gravity){
        super(rect, gravity);
    }

    @Override
    public void update(long timeDelta) {
        super.update(0, 0);
    }

    @Override
    public void jump() {

    }

    @Override
    public void notifyContact(Movable movable) {
        if (this.equals(movable.getObstacle()))
            return;

        Log.d("Obstacle", this + " contact with " + movable);

        if (Geometry.intersectTop(movable.getHitBox(), this.getHitBox())) {
            if (movable.canAttachToPlatform(this, Gravity.DOWN)) {
                movable.setObstacle(this);
                movable.setGravity(Gravity.DOWN);
                return;
            }
            else {
                movable.switchDirection();
            }
        }
        if (Geometry.intersectBottom(movable.getHitBox(), this.getHitBox())) {
            if (movable.canAttachToPlatform(this, Gravity.UP)) {
                movable.setObstacle(this);
                movable.setGravity(Gravity.UP);
                return;
            }
            else {
                movable.switchDirection();
            }

        }
        if (Geometry.intersectLeft(movable.getHitBox(), this.getHitBox())) {
            if (movable.canAttachToPlatform(this, Gravity.RIGHT)) {
                movable.setObstacle(this);
                movable.setGravity(Gravity.RIGHT);
                return;
            }
            else {
                movable.switchDirection();
            }
        }
        if (Geometry.intersectRight(movable.getHitBox(), this.getHitBox())) {
            if (movable.canAttachToPlatform(this, Gravity.LEFT)) {
                movable.setObstacle(this);
                movable.setGravity(Gravity.LEFT);
                return;
            }
            else {
                movable.switchDirection();
            }
        }
    }

    @Override
    public boolean canAttachToPlatform(Obstacle obstacle, GravityEngine gravity) {
        return false;
    }
}
