package net.enderbyteprograms.KeepChoice;

import net.enderbyteprograms.KeepChoice.Language.EPLanguage;
import net.enderbyteprograms.KeepChoice.Language.Languages;
import net.enderbyteprograms.KeepChoice.Structures.Config;
import net.enderbyteprograms.KeepChoice.Structures.PlayerData;
import net.enderbyteprograms.KeepChoice.bstats.Metrics;
import net.enderbyteprograms.KeepChoice.commands.KeepInventoryCommand;
import net.enderbyteprograms.KeepChoice.commands.KeepInventoryTabCompleter;
import net.enderbyteprograms.KeepChoice.listeners.OnDeath;
import net.enderbyteprograms.KeepChoice.listeners.OnJoin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class KeepChoiceMain extends JavaPlugin {
    @Override
    public void onEnable() {
        Static.Plugin = this;
        this.saveDefaultConfig();
        Static.RawConfig = this.getConfig();

        Static.Logger = this.getLogger();
        Static.Config = new Config(Static.RawConfig);

        this.getLogger().info("Found "+String.valueOf(Static.Config.WorldSettings.size())+" Configurations");

        int pluginID = 24015;
        Metrics metrics = new Metrics(this, pluginID);

        ReadPlayerData();

        getServer().getPluginManager().registerEvents(new OnJoin(), this);
        getServer().getPluginManager().registerEvents(new OnDeath(),this);
        getCommand("keepinventory").setExecutor(new KeepInventoryCommand());
        getCommand("keepinventory").setTabCompleter(new KeepInventoryTabCompleter());

        //Create language file if it doesn't exist
        File datafilelocation = new File(this.getDataFolder(),"language.yml");
        boolean langloadfailed = false;
        try {
            EPLanguage.LoadConfigurationFile(datafilelocation);
        } catch (Exception e) {
            getLogger().warning("Writing new language file - "+e.getMessage());
            langloadfailed = true;
        }

        if (!datafilelocation.exists() || langloadfailed) {
            Languages stl = null;
            if (EPLanguage.SelectedLanguage != null) {
                stl = EPLanguage.SelectedLanguage;
            }
            saveResource("language.yml",true);
            try {
                EPLanguage.LoadConfigurationFile(datafilelocation);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }
            if (stl != null) {
                EPLanguage.ChooseLanguage(stl.value());
            }
        }

        getLogger().info(EPLanguage.get("welcome.startup")+" (c) 2024-2025 Enderbyte Programs, some rights reserved");
    }
    @Override
    public void onDisable() {
        WritePlayerData();
        getLogger().info(EPLanguage.get("welcome.goodbye"));
    }

    public void WritePlayerData() {
        for(PlayerData datablocks:Static.Data.values()) {
            for (String worldkey:datablocks.WorldData.keySet()) {
                this.getConfig().set(String.format("data.%s.%s",datablocks.UUID,worldkey),datablocks.WorldData.get(worldkey));
            }
        }
        this.saveConfig();
    }

    public void ReadPlayerData() {
        for(String data:this.getConfig().getConfigurationSection("data").getKeys(false)) {
            if (data.equals("__placeholder")) {
                continue;
            }
            PlayerData pd = new PlayerData(data);
            ConfigurationSection thisdata = this.getConfig().getConfigurationSection("data."+data);
            for (String key:thisdata.getKeys(false)) {
                Boolean vv = thisdata.getBoolean(key);
                //getLogger().info(vv.toString());
                pd.WorldData.put(key, vv);
            }
            Static.Data.put(data,pd);
        }
    }

}
