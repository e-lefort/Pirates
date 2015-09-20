package fr.elefort.piratesremastered.model.bonus;

import android.content.Context;

import fr.elefort.piratesremastered.model.Animation;
import fr.elefort.piratesremastered.model.Player;

/**
 * Created by Eric on 20/09/2015.
 */
public interface BonusAction{
    void perform(Player player);
    Animation getAnimation(Context context);
    String getName();
    char getValue();
}
