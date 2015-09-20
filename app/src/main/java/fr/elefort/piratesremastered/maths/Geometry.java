package fr.elefort.piratesremastered.maths;

import android.graphics.RectF;

/**
 * Created by Eric on 16/09/2015.
 */
public class Geometry {
    /**
     * Intersection between 2 rectangles
     * @param ux left first rectangle
     * @param uy top first rectangle
     * @param vx right first rectangle
     * @param vy bottom first rectangle
     * @param ax left second rectangle
     * @param ay top second rectangle
     * @param bx right second rectangle
     * @param by bottom second rectangle
     * @return true if the first rectangle intersects the second rectangle.
     */
    public static boolean intersect(float ux, float uy, float vx, float vy, float ax, float ay, float bx, float by){
        return (ux < bx && ax < vx && uy < by && ay < vy) ? true : false;
    }

    public static boolean intersect(RectF rectA, RectF rectB){
        return intersect(rectA.left, rectA.top, rectA.right, rectA.bottom, rectB.left, rectB.top, rectB.right, rectB.bottom);
    }

    /**
     * Croissing between 2 lines
     * @param ux
     * @param uy
     * @param vx
     * @param vy
     * @param ax
     * @param ay
     * @param bx
     * @param by
     * @return
     */
    public static boolean cross(float ux, float uy, float vx, float vy, float ax, float ay, float bx, float by){
        double d = ((ux - vx) * (ay - by) - (uy - vy) * (ax - bx));
        return ( d != 0 ) ? true : false;
    }

    /**
     * Distance between 2 points
     * @param x1 coordinate <i>x</i> first point
     * @param y1 coordinate <i>y</i> first point
     * @param x2 coordinate <i>x</i> second point
     * @param y2 coordinate <i>y</i> second point
     * @return the distance between the <i>first</i> point and the <i>second</i> point
     */
    public static float distance(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }

    public static boolean intersectTop(RectF rectA, RectF rectB) {
        float ux = rectB.left;
        float uy = rectB.top;
        float vx = rectB.right;
        float vy = rectB.top;

        return Geometry.intersect(rectA.left, rectA.top, rectA.right, rectA.bottom, ux, uy, vx, vy);
    }

    public static boolean intersectBottom(RectF rectA, RectF rectB) {
        float ux = rectB.left;
        float uy = rectB.bottom;
        float vx = rectB.right;
        float vy = rectB.bottom;

        return Geometry.intersect(rectA.left, rectA.top, rectA.right, rectA.bottom, ux, uy, vx, vy);
    }

    public static boolean intersectLeft(RectF rectA, RectF rectB) {
        float ux = rectB.left;
        float uy = rectB.top;
        float vx = rectB.left;
        float vy = rectB.bottom;

        return Geometry.intersect(rectA.left, rectA.top, rectA.right, rectA.bottom, ux, uy, vx, vy);
    }

    public static boolean intersectRight(RectF rectA, RectF rectB) {
        float ux = rectB.right;
        float uy = rectB.top;
        float vx = rectB.right;
        float vy = rectB.bottom;

        return Geometry.intersect(rectA.left, rectA.top, rectA.right, rectA.bottom, ux, uy, vx, vy);
    }
}