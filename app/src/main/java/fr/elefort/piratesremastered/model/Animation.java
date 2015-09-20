package fr.elefort.piratesremastered.model;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;

import fr.elefort.piratesremastered.model.gravity.Gravity;
import fr.elefort.piratesremastered.model.gravity.GravityEngine;

/**
 * Created by Eric on 15/09/2015.
 */
public class Animation {

    private class Direction {
        static final int FORWARD = 10;
        static final int BACKWARD = 20;
        static final int JUMPING = 30;
        static final int FALLING = 40;
    }

    private class AnimationFrame {
        Bitmap bitmap;
        long endTime;

        public AnimationFrame(Bitmap bitmap, long endTime) {
            this.bitmap = bitmap;
            this.endTime = endTime;
        }
    }

    private static final int DURATION = 50;
    private static final int REFRESH_RATE = 10;

    private final SparseArray<ArrayList<AnimationFrame>> animations = new SparseArray<>();
    private ArrayList<AnimationFrame> currentFramesList;
    private final ArrayList<Bitmap> bitmaps;
    private int currentFrame;
    private long animTime;
    private long totalDuration;

    public Animation(ArrayList<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;

        for (GravityEngine gravity : Gravity.values()) {
            animations.put(gravity.ordinal() << Direction.FORWARD, new ArrayList<AnimationFrame>());
            animations.put(gravity.ordinal() << Direction.BACKWARD, new ArrayList<AnimationFrame>());
        }
        currentFramesList = animations.get(animations.keyAt(0));

        for (Bitmap b : bitmaps) {
            totalDuration += DURATION;
            for (GravityEngine gravity : Gravity.values()) {
                animations.get(gravity.ordinal() << Direction.FORWARD).add(new AnimationFrame(flip(b, gravity, true), totalDuration));
                animations.get(gravity.ordinal() << Direction.BACKWARD).add(new AnimationFrame(flip(b, gravity, false), totalDuration));
            }
        }
    }

    public void update(GravityEngine gravity, int velocity) {
        Log.e("Animation update", Integer.toString((int) Math.signum(velocity)));
        currentFramesList = animations.get(gravity.ordinal() << getOrientation(velocity));
        animTime += REFRESH_RATE;
        if(animTime >= totalDuration) {
            animTime = animTime % totalDuration;
            currentFrame = 0;
        }

        while(animTime > getFrame(currentFrame).endTime) {
            currentFrame++;
        }
    }

    public Bitmap getBitmap() {
        return getFrame(currentFrame).bitmap;
    }

    private AnimationFrame getFrame(int i) {
        return currentFramesList.get(i);
    }

    private Bitmap flip(Bitmap bitmap, GravityEngine gravity, boolean forward) {
        Matrix matrix = new Matrix();
        if (!forward)
            matrix.preScale(-1.0f, 1.0f);
        matrix.postRotate(gravity.getOrientation());
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private int getOrientation(int velocity) {
        return (int)Math.signum(velocity) > 0 ? Direction.FORWARD : Direction.BACKWARD;
    }
}
