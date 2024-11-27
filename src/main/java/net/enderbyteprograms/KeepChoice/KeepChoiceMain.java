package net.enderbyteprograms.KeepChoice;

import net.enderbyteprograms.KeepChoice.Structures.Config;
import org.bukkit.plugin.java.JavaPlugin;

public class KeepChoiceMain extends JavaPlugin {
    @Override
    public void onEnable() {
        this.saveConfig();
        Static.RawConfig = this.getConfig();

        Static.Logger = this.getLogger();
        Static.Config = new Config(Static.RawConfig);
        this.getLogger().info("Found "+String.valueOf(Static.Config.WorldSettings.size())+" Configurations");
        getLogger().info("KeepChoice is ready. (c) 2024 Enderbyte Programs, some rights reserved");
    }
    @Override
    public void onDisable() {
        getLogger().info("ByeBye Everybody!");
    }
}
