package net.enderbyteprograms.KeepChoice;

import org.bukkit.Bukkit;

public class Utils {
    public static boolean isPlayerOnline(String uuid) {
        return Bukkit.getPlayer(uuid).isOnline();
    }
}
