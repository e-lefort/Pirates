package fr.elefort.piratesremastered.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import fr.elefort.piratesremastered.model.gravity.Gravity;
import fr.elefort.piratesremastered.model.gravity.GravityEngine;
import fr.elefort.piratesremastered.Settings;

/**
 * Created by Eric on 16/09/2015.
 */
public class Platform extends Obstacle {
    public Platform(RectF rect, GravityEngine gravity) {
        super(rect, gravity);
    }

    @Override
    public void onGravityChange(GravityEngine oldGravity) {
        return;
    }

    @Override
    public void draw(Canvas canvas, float offsetX, float offsetY, float scale) {

        if (Settings.getInstance().isDebugMode()) {
            float x = getPosition().x;
            float y = getPosition().y;

            Paint paint = super.getDrawing().paint;
            if (getGravity() == Gravity.UP)
                paint.setColor(Color.RED);
            if (getGravity() == Gravity.DOWN)
                paint.setColor(Color.BLUE);
            if (getGravity() == Gravity.RIGHT)
                paint.setColor(Color.GREEN);
            if (getGravity() == Gravity.LEFT)
                paint.setColor(Color.YELLOW);
            canvas.drawRect((getHitBox().left + x) * scale, (getHitBox().top + y) * scale, (getHitBox().right + x) * scale, (getHitBox().bottom + y) * scale, paint);
        }
        //super.draw(canvas, offsetX, offsetY, scale);
    }
}
