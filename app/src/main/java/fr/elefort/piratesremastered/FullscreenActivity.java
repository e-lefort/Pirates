package fr.elefort.piratesremastered;

import fr.elefort.piratesremastered.factory.LevelFactory;
import fr.elefort.piratesremastered.model.GameModel;
import fr.elefort.piratesremastered.model.Level;
import fr.elefort.piratesremastered.model.Player;
import fr.elefort.piratesremastered.model.Texture;
import fr.elefort.piratesremastered.util.SystemUiHider;
import fr.elefort.piratesremastered.views.GameSurfaceViewInterface;
import fr.elefort.priratesremastered.R;

import android.app.Activity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        LevelFactory levelFactory = new LevelFactory(getApplicationContext());
        Level level = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getApplicationContext().getAssets().open("level" + File.separator + "level-08 1vs1"), "ASCII"));
            level = levelFactory.createLevel(reader);

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        Texture gameAssets = new Texture();

        try {
            gameAssets.build(getApplicationContext(), "img/grass");
        } catch (IOException e) {
            e.printStackTrace();
        }

        GameModel model = new GameModel(getApplicationContext());
        model.setLevel(level);
        model.setTexture(gameAssets);

        final GameSurfaceViewInterface surfaceView = (GameSurfaceViewInterface) findViewById(R.id.game_surface_view);
        surfaceView.setGameModel(model);
        surfaceView.run();
    }

    @Override
    protected void onPause() {
        final GameSurfaceViewInterface surfaceView = (GameSurfaceViewInterface) findViewById(R.id.game_surface_view);
        surfaceView.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        final GameSurfaceViewInterface surfaceView = (GameSurfaceViewInterface) findViewById(R.id.game_surface_view);
        surfaceView.stop();
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        final GameSurfaceViewInterface surfaceView = (GameSurfaceViewInterface) findViewById(R.id.game_surface_view);
        surfaceView.resume();
        super.onResume();
    }
}
