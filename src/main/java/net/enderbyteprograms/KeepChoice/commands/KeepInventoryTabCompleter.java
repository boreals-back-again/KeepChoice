package net.enderbyteprograms.KeepChoice.commands;

import net.enderbyteprograms.KeepChoice.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeepInventoryTabCompleter implements TabCompleter {
    @Override

    public List<String> onTabComplete(CommandSender sender, Command command, String header,String[] args) {

        List<String> result = new ArrayList<>();

        //Logic
        if (args.length == 1) {
            for(World w:Bukkit.getWorlds()) {
                result.add(w.getName());
            }
            if (Utils.isAdmin(sender)) {
                result.addAll(Arrays.asList("help", "playerinfo", "worldinfo", "setrunson", "setdefaultkion","reload"));
            } else {
                result.add("help");
            }

            return Utils.CompareStartsWith(result,args[args.length-1]);
        } else {
            String action = args[0];
            if ((action.equals("setrunson") || action.equals("setdefaultkion")) && args.length == 2) {
                for(World w:Bukkit.getWorlds()) {
                    result.add(w.getName());
                }
                if (action.equals("setrunson")) {
                    result.add("#all");
                } else {
                    result.add("#default");
                }
                return Utils.CompareStartsWith(result,args[args.length-1]);
            }
            else if ((action.equals("setrunson") || action.equals("setdefaultkion")) && args.length == 3) {
                result.add("yes");
                result.add("no");
                return Utils.CompareStartsWith(result,args[args.length-1]);
            }
            else if (action.equals("setdefaultkion") && args.length == 4) {
                result.add("force");
                result.add("noforce");
                return Utils.CompareStartsWith(result,args[args.length-1]);
            } else if (args.length == 2 && Utils.isAdmin(sender)) {
                for (Player p:Bukkit.getOnlinePlayers()) {
                    result.add(p.getDisplayName());
                }
                return Utils.CompareStartsWith(result,args[args.length-1]);
            } else if (action.equals("playerinfo")) {

            }
        }


        return result;
    }
}
