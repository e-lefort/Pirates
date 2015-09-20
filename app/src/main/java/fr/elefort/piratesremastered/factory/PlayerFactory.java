package fr.elefort.piratesremastered.factory;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import fr.elefort.piratesremastered.model.Animation;
import fr.elefort.piratesremastered.model.Level;
import fr.elefort.piratesremastered.model.Player;
import fr.elefort.piratesremastered.model.gravity.Gravity;
import fr.elefort.piratesremastered.model.inputs.PlayerInputListener;

/**
 * Created by Eric on 14/09/2015.
 */
public class PlayerFactory {

    public static String[] loadSkins(Context context) {
        String[] skins = new String[0];
        try {
            skins = context.getAssets().list("skins");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return skins;
    }

    public static Player createPlayer(Context context, String skin, Level level, int nb, int count) {
        AssetManager assetManager = context.getAssets();
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        String filePath = "skins"  + File.separator + skin;
        try {
            for (String img : context.getAssets().list(filePath)) {
                bitmaps.add(BitmapFactory.decodeStream(assetManager.open(filePath + File.separator + img)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Animation animation = new Animation(bitmaps);

        Bitmap bitmap = animation.getBitmap();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scale = Math.min((float) level.MOVE_STEP / width, (float) level.MOVE_STEP / height);

        PlayerInputListener.InputListenerUpdater updater;

        if(nb == 1){
            updater = new PlayerInputListener.InputListenerUpdater() {

                @Override
                public void update(View v, Player player, RectF zone) {
                    zone.set(0, 0, v.getWidth() * 0.5f, v.getHeight());
                }
            };
        }else{
            updater = new PlayerInputListener.InputListenerUpdater() {
                @Override
                public void update(View v, Player player, RectF zone) {
                    zone.set(v.getWidth() * 0.5f, 0, v.getWidth(), v.getHeight());
                }
            };
        }

        Player player = new Player(new RectF(0, 0, (width * scale), (height * scale)), Gravity.UP);
        player.setName(skin);
        player.setAnimation(animation);
        player.switchDirection();
        player.setInputListener(new PlayerInputListener(updater));
        return  player;
    }
}
