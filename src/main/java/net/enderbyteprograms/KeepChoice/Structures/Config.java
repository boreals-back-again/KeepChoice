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

        List<String> configkeys = new ArrayList<String>(ingress.getKeys(false));
        for (String configkey:configkeys) {
            //Static.Logger.info(configkey);
            if (!configkey.equals("runinworlds") && !configkey.equals("data")) {
                //Avoid accidentally grabbing the runinworlds
                ConfigDefaultBehaviour pv = new ConfigDefaultBehaviour(ingress,configkey);
                //Static.Logger.info(String.valueOf(pv.KeepItems));
                WorldSettings.put(configkey,pv);
            }
        }
    }
}
