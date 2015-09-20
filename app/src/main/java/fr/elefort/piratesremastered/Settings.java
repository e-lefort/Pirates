package fr.elefort.piratesremastered;

/**
 * Created by Eric on 19/09/2015.
 */
public class Settings {
    private static final Settings settings = new Settings();

    private Settings() {}

    public static Settings getInstance() {
        return settings;
    }

    public boolean isDebugMode(){
        return false;
    }

    public boolean showPerformance(){
        return true;
    }
}
