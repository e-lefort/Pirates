package fr.elefort.piratesremastered.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Eric on 14/09/2015.
 */
public abstract class BaseObject {
    public Drawing getDrawing() {
        return drawing;
    }

    class Drawing {
        final Rect textureRect = new Rect();
        final RectF drawRect = new RectF();
        final Paint paint = new Paint();
    }

    private Animation animation;
    private final RectF hitBox;
    private final Point position = new Point();
    private final Drawing drawing = new Drawing();

    BaseObject(RectF hitBox){
        this.hitBox = hitBox;
    }

    public RectF getHitBox(){
        return hitBox;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public void draw(Canvas canvas, float offsetX, float offsetY, float scale) {
        float x = position.x + offsetX;
        float y = position.y + offsetY;

        //drawing.paint.setColor(Color.CYAN);
        //canvas.drawRect(hitBox.left * scale, hitBox.top * scale, hitBox.right * scale, hitBox.bottom  * scale, drawing.paint);
        //canvas.drawRect((hitBox.left + offsetX) * scale, (hitBox.top + offsetY) * scale, (hitBox.right + offsetX) * scale, (hitBox.bottom + offsetY) * scale, drawing.paint);

        if (animation != null)
        {
            Bitmap bitmap = animation.getBitmap();
            int bWidth = bitmap.getWidth();
            int bHeight = bitmap.getHeight();

            drawing.textureRect.set(0, 0, bWidth, bHeight);
            drawing.drawRect.set((hitBox.left + offsetX) * scale, (hitBox.top + offsetY) * scale, (hitBox.right + offsetX) * scale, (hitBox.bottom + offsetY) * scale);
            canvas.drawBitmap(bitmap, drawing.textureRect, drawing.drawRect, drawing.paint);
        }

        //drawing.paint.setColor(Color.BLUE);
        //canvas.drawLine(x * scale, y * scale, (hitBox.right + offsetX) * scale, (hitBox.bottom + offsetY) * scale, drawing.paint);
        //canvas.drawPoint(x * scale, y * scale, drawing.paint);
    }

    public void update(int x, int y) {
        setPosition(x, y);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(int x, int y){
        this.position.set(x, y);
        this.hitBox.offset(x - this.hitBox.centerX(), y - this.hitBox.centerY());
    }

    public Animation getAnimation() { return animation; }

    public abstract void update(long timeDelta);

    @Override
    public String toString() {
        return hitBox.toString();
    }
}
