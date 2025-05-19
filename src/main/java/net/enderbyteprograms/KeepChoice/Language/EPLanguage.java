package net.enderbyteprograms.KeepChoice.Language;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class EPLanguage {
    private static YamlConfiguration LanguageBase;
    public static Languages SelectedLanguage;
    public static Languages FallbackLanguage = Languages.English;
    public static final int VERSION = 7;
    private static boolean ParseColours = true;
    private static final Map<String, ChatColor> COLOURS = Map.ofEntries(
            Map.entry("#RESET",ChatColor.RESET),
            Map.entry("#RED",ChatColor.RED),
            Map.entry("#DARKRED",ChatColor.DARK_RED),
            Map.entry("#YELLOW",ChatColor.YELLOW),
            Map.entry("#GREEN",ChatColor.GREEN),
            Map.entry("#DARKGREEN",ChatColor.DARK_GREEN),
            Map.entry("#AQUA",ChatColor.AQUA),
            Map.entry("#DARKAQUA",ChatColor.DARK_AQUA),
            Map.entry("#BLUE",ChatColor.BLUE),
            Map.entry("#DARKBLUE",ChatColor.DARK_BLUE),
            Map.entry("#PURPLE",ChatColor.DARK_PURPLE),
            Map.entry("#BOLD",ChatColor.BOLD)
    );

    public static void LoadConfigurationFile(File path) throws IOException, InvalidConfigurationException {
        LanguageBase = new YamlConfiguration();
        LanguageBase.load(path);
        if (LanguageBase.getInt("version") != VERSION) {
            throw new IOException("Mismatched versions");
        }
        EPLanguage.ChooseLanguage(LanguageBase.getString("selected"));
        FallbackLanguage = Languages.GetFromString(LanguageBase.getString("fallback"));
        ParseColours = LanguageBase.getBoolean("parsecolour");
    }

    public static String get(String key) {
        ConfigurationSection basesection = LanguageBase.getConfigurationSection("translations");
        ConfigurationSection specific = basesection.getConfigurationSection(SelectedLanguage.value());
        String result;
        if (specific.contains(key)) {
            result =  specific.getString(key);
        } else {
            result =  basesection.getConfigurationSection(FallbackLanguage.value()).getString(key);
        }
        if (ParseColours) {
            for (Map.Entry<String,ChatColor> e:COLOURS.entrySet()) {
                result = result.replaceAll(e.getKey(),e.getValue().toString());
            }
            return result;
        } else {
            return result;
        }
    }

    public static void ChooseLanguage(String code) {
        SelectedLanguage = Languages.GetFromString(code);
    }

}
