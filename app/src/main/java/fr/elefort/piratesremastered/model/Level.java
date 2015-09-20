package fr.elefort.piratesremastered.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;

/**
 * Created by Eric on 13/09/2015.
 */
public class Level {
    public static final int MOVE_STEP = 20;

    private int width = 0;
    private int height = 0;

    private boolean[][] obstacles;

    public final ArrayList<Player> players = new ArrayList<>();
    public final ArrayList<Bonus> bonusItems = new ArrayList<>();
    public final ArrayList<Platform> platforms = new ArrayList<>();

    public Level(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setObstacles(boolean[][] obstacles) {
        this.obstacles = obstacles;
    }

    public int getWidth() {
        return width * MOVE_STEP;
    }

    public int getHeight() {
        return height * MOVE_STEP;
    }

    public int getAbsWidth() {
        return width;
    }

    public int getAbsHeight() {
        return height;
    }

    public float getScale(int screenWidth, int screenHeight) {
        return Math.min(screenWidth/(width*MOVE_STEP), screenHeight/(height*MOVE_STEP));
    }

    public float getOffsetX(int screenWidth, float scale) {
        return ((screenWidth/scale)/2) - ((width*MOVE_STEP)/2);
    }

    public float getOffsetY(int screenHeight, float scale) {
        return ((screenHeight/scale)/2) - ((height*MOVE_STEP)/2);
    }

    public Bitmap getBitmap(Texture texture, int screenWidth, int screenHeight) {
        float scale = getScale(screenWidth, screenHeight);
        float imageStartX = getOffsetX(screenWidth, scale);
        float imageStartY = getOffsetY(screenHeight, scale);

        RectF drawRect = new RectF();
        Rect textureRect = new Rect();
        Bitmap bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888) ;
        int[][] mapping = getTextureMapping();

        Canvas tmp = new Canvas(bitmap);
        tmp.drawColor(0, PorterDuff.Mode.CLEAR);

        for(int x = 0; x < mapping.length; x++){
            for(int y = 0; y < mapping[0].length; y++){
                Bitmap b = texture.getBitmap(mapping[x][y]);
                if(b == null)
                    continue;
                int bWidth = bitmap.getWidth();
                int bHeight = bitmap.getHeight();
                textureRect.set(0, 0, bWidth, bHeight);

                drawRect.set(
                        (imageStartX + (x*MOVE_STEP)) * scale,
                        (imageStartY + (y*MOVE_STEP)) * scale,
                        (imageStartX + ((x+1)*MOVE_STEP)) * scale,
                        (imageStartY + ((y+1)*MOVE_STEP)) * scale);
                tmp.drawBitmap(b, null, drawRect, null);
            }
        }

        return bitmap;
    }

    public int[][] getTextureMapping() {
        int width = obstacles.length;
        int height = obstacles.length > 0 ? obstacles[0].length : 0;

        int[][] mapping = new int[width-2][];

        for(int i=0; i<mapping.length; i++){
            mapping[i] = new int[height-2];
        }

        //vertical
        for(int x=1; x<width-1; x++){
            for(int y=1; y<height-1; y++){
                if(obstacles[x][y]){
                    if(obstacles[x-1][y] && obstacles[x][y-1] && obstacles[x+1][y] && obstacles[x][y+1]){
                        mapping[x-1][y-1] = Texture.Drawable.INTERSECTION.ordinal();
                    }

                    if(obstacles[x-1][y] && obstacles[x][y-1]&& !obstacles[x+1][y]  && obstacles[x][y+1]){
                        mapping[x-1][y-1] = Texture.Drawable.VERTICAL_RIGHT.ordinal(); //right
                    }

                    if(!obstacles[x-1][y] && obstacles[x][y-1] && obstacles[x+1][y] && obstacles[x][y+1]){
                        mapping[x-1][y-1] = Texture.Drawable.VERTICAL_LEFT.ordinal(); //left
                    }

                    if(!obstacles[x-1][y] && obstacles[x][y-1]&& !obstacles[x+1][y]  && obstacles[x][y+1]){
                        mapping[x-1][y-1] = Texture.Drawable.VERTICAL.ordinal();
                    }

                    if(!obstacles[x-1][y] && !obstacles[x][y-1] && obstacles[x+1][y] && obstacles[x][y+1]){
                        mapping[x-1][y-1] = Texture.Drawable.CORNER_TOP_LEFT.ordinal();
                    }

                    if(obstacles[x-1][y] && obstacles[x][y-1] && !obstacles[x+1][y] && !obstacles[x][y+1]){
                        mapping[x-1][y-1] = Texture.Drawable.CORNER_BOTTOM_RIGHT.ordinal();
                    }


                    if(!obstacles[x-1][y] && obstacles[x][y-1] && obstacles[x+1][y] && !obstacles[x][y+1]){
                        mapping[x-1][y-1] = Texture.Drawable.CORNER_BOTTOM_LEFT.ordinal();
                    }

                    if(obstacles[x-1][y] && !obstacles[x][y-1] && !obstacles[x+1][y] && obstacles[x][y+1]){
                        mapping[x-1][y-1] = Texture.Drawable.CORNER_TOP_RIGHT.ordinal();
                    }

                    if(!obstacles[x-1][y] && !obstacles[x][y-1] && !obstacles[x+1][y] && obstacles[x][y+1]){
                        mapping[x-1][y-1] = Texture.Drawable.CORNER_TOP.ordinal();
                    }

                    if(!obstacles[x-1][y] && obstacles[x][y-1] && !obstacles[x+1][y] && !obstacles[x][y+1]){
                        mapping[x-1][y-1] = Texture.Drawable.CORNER_BOTTOM.ordinal();
                    }
                }
            }
        }

        //horizontal
        for(int y=1; y<height-1; y++){
            for(int x=1; x<width-1; x++){
                if(obstacles[x][y]){
                    if(obstacles[x-1][y] && !obstacles[x][y-1]&& obstacles[x+1][y]  && obstacles[x][y+1]){
                        mapping[x-1][y-1] = Texture.Drawable.HORIZONTAL_TOP.ordinal(); //top
                    }

                    if(obstacles[x-1][y] && obstacles[x][y-1] && obstacles[x+1][y] && !obstacles[x][y+1]){
                        mapping[x-1][y-1] = Texture.Drawable.HORIZONTAL_BOTTOM.ordinal(); //bottom
                    }

                    if(obstacles[x-1][y] && !obstacles[x][y-1] && obstacles[x+1][y]  && !obstacles[x][y+1]){
                        mapping[x-1][y-1] = Texture.Drawable.HORIZONTAL.ordinal();
                    }

                    if(!obstacles[x-1][y] && !obstacles[x][y-1] && !obstacles[x+1][y]  && !obstacles[x][y+1]){
                        mapping[x-1][y-1] = Texture.Drawable.FULL_BODER.ordinal();
                    }

                    if(!obstacles[x-1][y] && !obstacles[x][y-1] && obstacles[x+1][y] && !obstacles[x][y+1]){
                        mapping[x-1][y-1] = Texture.Drawable.CORNER_LEFT.ordinal();
                    }

                    if(obstacles[x-1][y] && !obstacles[x][y-1] && !obstacles[x+1][y] && !obstacles[x][y+1]){
                        mapping[x-1][y-1] = Texture.Drawable.CORNER_RIGHT.ordinal();
                    }
                }
            }
        }

        return mapping;
    }

    public RectF getRectF() {
        return new RectF(0, 0, getWidth(), getHeight());
    }
}
