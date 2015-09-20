package fr.elefort.piratesremastered.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import fr.elefort.piratesremastered.model.gravity.Gravity;
import fr.elefort.piratesremastered.model.gravity.GravityEngine;
import fr.elefort.piratesremastered.Settings;
import fr.elefort.piratesremastered.model.inputs.InputListener;

/**
 * Created by Eric on 14/09/2015.
 */
public abstract class Movable extends BaseObject implements View.OnTouchListener {

    private Obstacle obstacle;

    public void setObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
    }

    public Obstacle getObstacle() {
        return this.obstacle;
    }

    public void setInputListener(InputListener inputListener) {
        this.inputListener = inputListener;
    }

    public PlayerStats getStats() {
        return stats;
    }

    public void setGravity(GravityEngine gravity) {
        onGravityChange(gravity);
        this.gravity = gravity;
    }

    public void onGravityChange(GravityEngine newGravity) {
        int rotate = this.gravity.getOrientation() - newGravity.getOrientation();

        Matrix m = new Matrix();
        m.setRotate(rotate, this.getHitBox().centerX(), this.getHitBox().centerY());
        m.mapRect(this.getHitBox());
    }

    public boolean isJumping() {
        return impulsion != 0;
    }

    public boolean attachToPlatform() {
        if (obstacle == null)
            return false;
        return onContact((BaseObject) obstacle);
    }

    public class PlayerStats {
        public int lifes;
        public boolean wounded;

        public void hurt() {
            lifes -= 1;
            wounded = true;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    private final PlayerStats stats = new PlayerStats();
    private InputListener inputListener;
    private GravityEngine gravity = Gravity.DOWN;
    public int impulsion;
    public int velocity;
    private String name;

    public GravityEngine getGravity() {
        return gravity;
    }

    public Movable(RectF rect, GravityEngine gravity) {
        super(rect);
        setGravity(gravity);
        this.velocity = 1;
    }

    @Override
    public void update(long timeDelta) {
        if(timeDelta >= 1000){
            decreaseVelocity();
            stats.wounded = false;
        }
        gravity.update(this, timeDelta);
        getAnimation().update(gravity, velocity);
    }

    public void switchDirection() {
        this.velocity *= -1;
    }

    public void increaseVelocity() {
        if (Math.abs(velocity) <= 10)
            velocity += 1 * Math.signum(velocity);
    }

    public void decreaseVelocity() {
        if (Math.abs(velocity) > 1)
            velocity -= 1 * Math.signum(velocity);
    }

    public void jump() {
        impulsion += 5;
    }

    public boolean onContact(BaseObject obj) {
        if (gravity == null)
            return false;
        if (this.equals(obj))
            return false;
        return gravity.intersect(this, obj);
    }

    public void onContact(Obstacle obstacle) {
        if (obstacle.equals(this.obstacle) || this.equals(obstacle))
            return;

        if (onContact((BaseObject) obstacle)) {
            obstacle.notifyContact(this);
        }
    }

    public void onContact(Movable movable) {
        if (this.equals(movable))
            return;

        if (onContact((BaseObject) movable)) {
            movable.notifyContact(this);
        }
    }

    public abstract void notifyContact(Movable movable);

    public boolean isAlive() {
        return stats.lifes > 0;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (inputListener == null)
            return false;
        return inputListener.listen(v, event, this);
    }

    public abstract boolean canAttachToPlatform(Obstacle obstacle, GravityEngine gravity);

    @Override
    public void draw(Canvas canvas, float offsetX, float offsetY, float scale) {

        if (Settings.getInstance().isDebugMode())
        {
            Paint paint = super.getDrawing().paint;
            if(getGravity() == Gravity.UP)
                paint.setColor(Color.RED);
            if(getGravity() == Gravity.DOWN)
                paint.setColor(Color.BLUE);
            if(getGravity() == Gravity.RIGHT)
                paint.setColor(Color.GREEN);
            if(getGravity() == Gravity.LEFT)
                paint.setColor(Color.YELLOW);
            canvas.drawRect(getHitBox().left * scale, getHitBox().top * scale, getHitBox().right * scale, getHitBox().bottom  * scale, paint);
        }

        super.draw(canvas, offsetX, offsetY, scale);
    }

    @Override
    public String toString() {
        if (name != "")
            return name;
        return super.toString();
    }
}
