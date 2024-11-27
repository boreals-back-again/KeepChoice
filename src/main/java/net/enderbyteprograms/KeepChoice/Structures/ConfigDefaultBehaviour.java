package net.enderbyteprograms.KeepChoice.Structures;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigDefaultBehaviour {
    public boolean KeepItems;
    public boolean KeepXP;
    public boolean DropItems;
    public boolean AllowChoice;
    public boolean DropXP;
    public int LimitXP;

    public ConfigDefaultBehaviour(FileConfiguration ingress,String header) {
        KeepItems = ingress.getBoolean(header+".KeepItems");
        KeepXP = ingress.getBoolean(header+".KeepXP");
        DropItems = ingress.getBoolean(header+".DropItems");
        AllowChoice = ingress.getBoolean(header+".AllowChoice");
        DropXP = ingress.getBoolean(header+".DropXP");
        LimitXP = ingress.getInt(header+".LimitXP");
    }
}
