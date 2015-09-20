package fr.elefort.piratesremastered.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.SparseArray;

import java.io.IOException;

/**
 * Created by Eric on 13/09/2015.
 */
public class Texture {

    public enum Drawable {
        NONE,
        HORIZONTAL,
        VERTICAL,

        HORIZONTAL_TOP,
        HORIZONTAL_BOTTOM,
        VERTICAL_LEFT,
        VERTICAL_RIGHT,

        FULL_BODER,
        FULL_NO_BORDER,

        INTERSECTION,

        CORNER_LEFT,
        CORNER_RIGHT,
        CORNER_TOP,
        CORNER_BOTTOM,

        CORNER_TOP_LEFT,
        CORNER_TOP_RIGHT,
        CORNER_BOTTOM_LEFT,
        CORNER_BOTTOM_RIGHT,
    }

    public enum Orientation {
        ROTATE_45 (45),
        ROTATE_90 (90),
        ROTATE_180 (180),
        ROTATE_270 (270);

        private final int value;
        Orientation(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    private final SparseArray<Bitmap> map = new SparseArray<Bitmap>();
    private final Matrix matrix = new Matrix();

    private void addFrame(Drawable id, Bitmap bitmap) {
        map.put(id.ordinal(), bitmap);
    }

    private void addFrame(Drawable id, Bitmap bitmap, Orientation orientation) {
        map.put(id.ordinal(), flip(bitmap, orientation.getValue()));
    }

    private Bitmap flip(Bitmap bitmap, int orientation) {
        matrix.reset();
        matrix.postRotate(orientation);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public void build(Context context, String prefix) throws IOException {
        loadBaseAssets(context, prefix);
        loadHalfAssets(context, prefix);
        loadFullAssets(context, prefix);
        loadAssets(context, prefix);
        loadLeftAssets(context, prefix);
        loadTopLeftAssets(context, prefix);
    }

    public Bitmap getBitmap(int orientation) {
        return map.get(orientation);
    }

    private void loadBaseAssets(Context context, String prefix) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeStream(context.getAssets().open(prefix + "_base.png"));
        addFrame(Drawable.HORIZONTAL, bitmap);
        addFrame(Drawable.VERTICAL, bitmap, Orientation.ROTATE_90);
    }

    private void loadHalfAssets(Context context, String prefix) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeStream(context.getAssets().open(prefix + "_half.png"));
        addFrame(Drawable.HORIZONTAL_TOP, bitmap);
        addFrame(Drawable.VERTICAL_RIGHT, bitmap, Orientation.ROTATE_90);
        addFrame(Drawable.HORIZONTAL_BOTTOM, bitmap, Orientation.ROTATE_180);
        addFrame(Drawable.VERTICAL_LEFT, bitmap, Orientation.ROTATE_270);
    }

    private void loadFullAssets(Context context, String prefix) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeStream(context.getAssets().open(prefix + "_full.png"));
        addFrame(Drawable.FULL_BODER, bitmap);
    }

    private void loadAssets(Context context, String prefix) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeStream(context.getAssets().open(prefix + ".png"));
        addFrame(Drawable.INTERSECTION, bitmap);
    }

    private void loadLeftAssets(Context context, String prefix) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeStream(context.getAssets().open(prefix + "_left.png"));
        addFrame(Drawable.CORNER_LEFT, bitmap);
        addFrame(Drawable.CORNER_TOP, bitmap, Orientation.ROTATE_90);
        addFrame(Drawable.CORNER_RIGHT, bitmap, Orientation.ROTATE_180);
        addFrame(Drawable.CORNER_BOTTOM, bitmap, Orientation.ROTATE_270);
    }

    private void loadTopLeftAssets(Context context, String prefix) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeStream(context.getAssets().open(prefix + "_top_left.png"));
        addFrame(Drawable.CORNER_TOP_LEFT, bitmap);
        addFrame(Drawable.CORNER_TOP_RIGHT, bitmap, Orientation.ROTATE_90);
        addFrame(Drawable.CORNER_BOTTOM_RIGHT, bitmap, Orientation.ROTATE_180);
        addFrame(Drawable.CORNER_BOTTOM_LEFT, bitmap, Orientation.ROTATE_270);
    }

}
