package fr.elefort.piratesremastered.model.inputs;

import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import fr.elefort.piratesremastered.model.BaseObject;
import fr.elefort.piratesremastered.model.Player;

/**
 * Created by Eric on 16/09/2015.
 */
public class PlayerInputListener implements InputListener {

    public static abstract class InputListenerUpdater {
        public abstract void update(View v, Player player, RectF zone);
    }

    private final InputListenerUpdater updater;
    private final RectF zone = new RectF();

    public PlayerInputListener(final InputListenerUpdater updater) {
        this.updater = updater;
    }

    @Override
    public boolean listen(View v, MotionEvent event, BaseObject baseObject) {
        try {
            if (!(baseObject instanceof Player)) {
                return true;
            }
            Player player = (Player) baseObject;
            updater.update(v, player, zone);
            int maskedAction = event.getActionMasked();

            switch (maskedAction) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    int pointerIndex = event.getActionIndex();

                    float x = event.getX(pointerIndex);
                    float y = event.getY(pointerIndex);

                    if (zone.contains(x, y)) {
                        if (player.isJumping() && player.attachToPlatform()){
                            player.increaseVelocity();
                            return true;
                        }
                        if (!player.isJumping() && player.attachToPlatform())
                            player.jump();
                        return true;
                    }
                    return false;
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL:
                    return false;
            }
        }
        catch (Exception ex) {
            Log.e("PlayerInputListener", ex.getMessage(), ex);
            throw ex;
        }
        return true;
    }
}
