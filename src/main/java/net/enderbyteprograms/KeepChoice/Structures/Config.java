package net.enderbyteprograms.KeepChoice.Structures;

import net.enderbyteprograms.KeepChoice.Static;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
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

    public boolean WasWildcardWorld = false;

    public Config(FileConfiguration ingress) {
        RunInWorlds = ingress.getStringList("runinworlds");
        RunInWorlds.remove("__placeholder");
        if (RunInWorlds.contains("*")) {
            WasWildcardWorld = true;
            //Wildcard detected
            RunInWorlds.clear();
            for (World w: Bukkit.getWorlds()) {
                if (w.getGameRuleValue(GameRule.KEEP_INVENTORY)) {
                    RunInWorlds.add(w.getName());
                }
                else {
                    Static.Logger.info("Skipping world "+w.getName()+" as keepinventory is turned off.");
                }
            }
        }
        RunInWorlds.remove("*");

        List<String> configkeys = new ArrayList<String>(ingress.getKeys(false));
        for (String configkey:configkeys) {
            //Static.Logger.info(configkey);
            if (!configkey.equals("runinworlds") && !configkey.equals("data")) {
                //Avoid accidentally grabbing the runinworlds
                if (!configkey.equals("default")) {
                    try {
                        World w = Bukkit.getWorld(configkey);
                        if (!w.getGameRuleValue(GameRule.KEEP_INVENTORY)) {
                            throw new Exception();
                        }
                    } catch (Exception e){
                        Static.Logger.warning("Skipped world "+configkey+" as it doesn't exist or keepinventory isn't enabled!");
                    }
                }
                ConfigDefaultBehaviour pv = new ConfigDefaultBehaviour(ingress,configkey);
                //Static.Logger.info(String.valueOf(pv.KeepItems));
                WorldSettings.put(configkey,pv);
            }
        }
    }

    public boolean IsWorldKnown(String name) {
        return WorldSettings.containsKey(name);
    }
    public boolean IsWorldAllowed(String name) {
        return RunInWorlds.contains(name);
    }

}
