package net.enderbyteprograms.KeepChoice.Structures;

import net.enderbyteprograms.KeepChoice.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerData {
    public String UUID;
    public Player Player;
    public HashMap<String, Boolean> WorldData = new HashMap<>();

    public PlayerData(String uuid) {
        UUID = uuid;
        Player = Bukkit.getPlayer(uuid);

    }
}
