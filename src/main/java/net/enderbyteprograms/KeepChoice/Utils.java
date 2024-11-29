package net.enderbyteprograms.KeepChoice;

import net.enderbyteprograms.KeepChoice.Structures.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static boolean isPlayerOnline(String uuid) {
        return Bukkit.getPlayer(uuid).isOnline();
    }

    public static boolean isAdmin(CommandSender s) {
        return s.hasPermission(Constants.AdminPermission);
    }

    public static void SendError(CommandSender s,String message) {
        s.sendMessage(ChatColor.DARK_RED + message + ChatColor.RESET);
    }

    public static void SendSuccess(CommandSender s,String message) {
        s.sendMessage(ChatColor.GREEN + message + ChatColor.RESET);
    }

    public static String FriendlyBool(boolean value) {
        if (value) {
            return ChatColor.GREEN + "yes" + ChatColor.RESET;
        } else {
            return ChatColor.RED + "no" + ChatColor.RESET;
        }
    }

    public static String SafeGetNameOrUUID(OfflinePlayer p) {
        String ff = p.getName();
        if (ff == null) {
            return p.getUniqueId().toString();
        }
        return ff;
    }

    public static boolean BoolFromS(String s) throws Exception {
        if (s.equals("yes")) {
            return true;
        } else if (s.equals("no")) {
            return false;
        } else {
            throw new Exception("Invalid bool!");
        }
    }

    public static List<String> GetAllWorldNames() {
        List<String> ff = new ArrayList<>();
        for (World w:Bukkit.getWorlds()) {
            ff.add(w.getName());
        }
        return ff;
    }

    public static String SafeArrayGet(String[] a,int ind) {
        try {
            return a[ind];
        } catch (Exception e) {
            return null;
        }
    }

    public static List<String> CompareStartsWith(List<String> input, String phrase) {
        List<String> result = new ArrayList<String>();
        for (String p:input) {
            if (p.startsWith(phrase) || phrase.replace(" ","").equals("")) {
                result.add(p);
            }
        }
        return result;
    }
}
