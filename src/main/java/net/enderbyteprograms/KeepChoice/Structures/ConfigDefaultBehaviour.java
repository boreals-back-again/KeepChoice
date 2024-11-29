package net.enderbyteprograms.KeepChoice.Structures;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigDefaultBehaviour {
    public boolean KeepItems;

    public ConfigDefaultBehaviour(FileConfiguration ingress,String header) {
        KeepItems = ingress.getBoolean(header+".Enabled");
    }
    public ConfigDefaultBehaviour(boolean ki) {
        KeepItems = ki;
    }
}
