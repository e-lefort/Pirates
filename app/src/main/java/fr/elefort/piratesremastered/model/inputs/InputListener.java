package fr.elefort.piratesremastered.model.inputs;

import android.view.MotionEvent;
import android.view.View;

import fr.elefort.piratesremastered.model.BaseObject;

/**
 * Created by Eric on 16/09/2015.
 */
public interface InputListener {
    boolean listen(View v, MotionEvent event, BaseObject baseObject);
}
