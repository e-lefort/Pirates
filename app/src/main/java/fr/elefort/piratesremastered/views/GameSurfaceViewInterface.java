package fr.elefort.piratesremastered.views;

import fr.elefort.piratesremastered.model.GameModel;

/**
 * Created by Eric on 16/09/2015.
 */
public interface GameSurfaceViewInterface {
    void setGameModel(GameModel model);
    void run();
    void pause();
    void stop();
    void resume();
}
