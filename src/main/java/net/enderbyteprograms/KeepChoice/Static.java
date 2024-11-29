package net.enderbyteprograms.KeepChoice;

import net.enderbyteprograms.KeepChoice.Structures.Config;
import net.enderbyteprograms.KeepChoice.Structures.PlayerData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Logger;

public class Static {
    public static FileConfiguration RawConfig;
    public static Config Config;
    public static Logger Logger;
    public static HashMap<String, PlayerData> Data = new HashMap<>();
    public static KeepChoiceMain Plugin;



}
