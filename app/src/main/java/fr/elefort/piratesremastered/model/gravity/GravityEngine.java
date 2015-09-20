package fr.elefort.piratesremastered.model.gravity;

import fr.elefort.piratesremastered.model.BaseObject;
import fr.elefort.piratesremastered.model.Movable;

/**
 * Created by Eric on 15/09/2015.
 */
public interface GravityEngine {
    int getOrientation();
    void update(Movable sender, long timeDelta);
    boolean intersect(Movable sender, BaseObject obj);
    void checkBottom(Movable sender);
    int ordinal();
}

