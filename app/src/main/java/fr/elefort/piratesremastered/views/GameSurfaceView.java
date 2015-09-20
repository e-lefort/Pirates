package fr.elefort.piratesremastered.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import fr.elefort.piratesremastered.util.Benchmark;
import fr.elefort.piratesremastered.Settings;
import fr.elefort.piratesremastered.model.GameModel;
import fr.elefort.piratesremastered.model.Movable;
import fr.elefort.piratesremastered.model.Obstacle;

/**
 * Created by Eric on 14/09/2015.
 */
public class GameSurfaceView extends SurfaceView implements GameSurfaceViewInterface{

    private final Benchmark benchmark = new Benchmark();
    private final Paint paint = new Paint();
    private final Object lock = new Object();
    private volatile boolean pause;

    private Bitmap bitmap;
    private GameModel model;

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setGameModel(final GameModel model) {
        this.model = model;
        this.model.setView(this);

        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (pause)
                    return false;

                for (OnTouchListener listener : model.touchListeners) {
                    listener.onTouch(v, event);
                }
                return true;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        SurfaceHolder holder = getHolder();
        synchronized (holder) {
            float scale = model.getLevel().getScale(getWidth(), getHeight());

            canvas.save();

            if (bitmap != null)
                canvas.drawBitmap(bitmap, 0, 0, paint);

            for (Movable movable : model.movables) {
                movable.draw(canvas, model.getLevel().getOffsetX(getWidth(), scale), model.getLevel().getOffsetY(getHeight(), scale), scale);
            }

            for (Obstacle obstacle : model.obstacles) {
                obstacle.draw(canvas, model.getLevel().getOffsetX(getWidth(), scale), model.getLevel().getOffsetY(getHeight(), scale), scale);
            }

            canvas.restore();

            if (Settings.getInstance().showPerformance()) {
                double fps = benchmark.fps();
                double ram = benchmark.ram();
                double ramAllocate = benchmark.ramAllocate();

                paint.setColor(Color.WHITE);
                paint.setTextSize(15);

                int count = 0;
                for (Movable movable : model.movables) {
                    count++;
                    canvas.drawText(count + " - " + movable + "\tVelocity : " + movable.velocity + "\tImpulsion : " + movable.impulsion
                            + "\tLife : " + movable.getStats().lifes + "\tWounded : " + movable.getStats().wounded, 10, count * 25, paint);
                }
                canvas.drawText("FPS : " + String.format("%.2f", fps) + "\tRAM : " + String.format("%.2f", ram) + "/" + String.format("%.2f", ramAllocate) + "mb", 10, getHeight() - 25, paint);
            }

            super.onDraw(canvas);
        }
    }

    @Override
    public void run(){
        SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
            private Canvas c;
            private Thread render;
            private long lastTime;

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                setWillNotDraw(false);
                render = new Thread(new Runnable() {

                    private void onPause() {
                        synchronized(lock) {
                            while (pause) {
                                try {
                                    lock.wait();
                                } catch (InterruptedException e) {
                                    pause = false;
                                    Thread.currentThread().interrupt();
                                }
                            }
                        }
                    }

                    @Override
                    public void run() {
                        while(!Thread.currentThread().isInterrupted()) {

                            final long time = SystemClock.uptimeMillis();
                            final long timeDelta = time - lastTime;

                            if(timeDelta >= 1000) {
                                lastTime = time;
                            }

                            SurfaceHolder holder = getHolder();
                            c = null;
                            try {
                                c = holder.lockCanvas(null);
                                synchronized (holder) {
                                    model.update(timeDelta);
                                    postInvalidate();
                                }
                            }
                            finally {
                                if (c != null) {
                                    holder.unlockCanvasAndPost(c);
                                }
                            }
                            onPause();
                        }
                    }
                });
                render.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                bitmap = model.getLevel().getBitmap(model.getTexture(), getWidth(), getHeight());
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (render == null)
                    return;
                render.interrupt();
                render = null;
            }
        };

        getHolder().addCallback(callback);
    }

    @Override
    public void pause() {
        synchronized (lock) {
            pause = true;
        }
    }

    @Override
    public void stop() {
        synchronized (lock) {
        }
    }

    @Override
    public void resume() {
        synchronized (lock) {
            pause = false;
            lock.notifyAll();
        }
    }
}
