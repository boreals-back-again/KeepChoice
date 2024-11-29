package net.enderbyteprograms.KeepChoice.listeners;

import net.enderbyteprograms.KeepChoice.Static;
import net.enderbyteprograms.KeepChoice.Structures.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String puuid = event.getPlayer().getUniqueId().toString();//Java be like
        //Static.Logger.warning(puuid);
        if (!Static.Data.containsKey(puuid)) {
            Static.Data.put(puuid,new PlayerData(puuid));
        }
    }
}
