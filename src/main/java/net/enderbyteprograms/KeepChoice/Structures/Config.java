package net.enderbyteprograms.KeepChoice.Structures;

import net.enderbyteprograms.KeepChoice.Static;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Config {
    public HashMap<String,ConfigDefaultBehaviour> WorldSettings = new HashMap<String,ConfigDefaultBehaviour>();
    public List<String> RunInWorlds = new ArrayList<String>();

    public ConfigDefaultBehaviour getDefault() {
        return WorldSettings.get("default");
    }

    public Config(FileConfiguration ingress) {
        RunInWorlds = ingress.getStringList("runinworlds");

        List<String> configkeys = new ArrayList<String>(ingress.getKeys(true));
        for (String configkey:configkeys) {
            Static.Logger.info(configkey);
            if (!configkey.equals("runinworlds")) {
                //Avoid accidentally grabbing the runinworlds
                WorldSettings.put(configkey,ingress.getObject(configkey,ConfigDefaultBehaviour.class));
            }
        }
    }
}
