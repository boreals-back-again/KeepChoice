package net.enderbyteprograms.KeepChoice.Structures;

import net.enderbyteprograms.KeepChoice.Static;
import net.enderbyteprograms.KeepChoice.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData {
    public String UUID;
    public OfflinePlayer Player;
    public HashMap<String, Boolean> WorldData = new HashMap<>();

    public PlayerData(String uuid) {
        UUID = uuid;
        Player = Bukkit.getOfflinePlayer(java.util.UUID.fromString(uuid));
        if (Player == null) {
            //Static.Logger.warning(uuid);
            Player = Bukkit.getOfflinePlayer(java.util.UUID.fromString(uuid));
        }

        if (Player != null && Player.isOnline()) {
            //Discover and load worlds
            for (World world : Bukkit.getWorlds()) {
                String currentworld = world.getName();
                if (Static.Config.IsWorldKnown(currentworld)) {
                    WorldData.put(currentworld, Static.Config.WorldSettings.get(currentworld).KeepItems);
                } else {
                    WorldData.put(currentworld, Static.Config.getDefault().KeepItems);
                }
            }

        }
    }

    public static PlayerData fromConfigurationSet(FileConfiguration ingress,String uuid) {
        PlayerData ffinal = new PlayerData(uuid);
        //If we call this, it is being loaded from the file
        for (String key: ingress.getConfigurationSection("data."+uuid).getKeys(false)) {
            ffinal.WorldData.put(key,ingress.getBoolean("data."+uuid+"."+key));
        }

        return ffinal;
    }
}
