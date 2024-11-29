package net.enderbyteprograms.KeepChoice.commands;

import net.enderbyteprograms.KeepChoice.KeepChoiceMain;
import net.enderbyteprograms.KeepChoice.Static;
import net.enderbyteprograms.KeepChoice.Structures.Config;
import net.enderbyteprograms.KeepChoice.Structures.ConfigDefaultBehaviour;
import net.enderbyteprograms.KeepChoice.Structures.Constants;
import net.enderbyteprograms.KeepChoice.Structures.PlayerData;
import net.enderbyteprograms.KeepChoice.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class KeepInventoryCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {


            if (args.length == 0) {
                //Bare run
                if (sender instanceof Player) {
                    if (!Static.Data.containsKey(((Player) sender).getUniqueId().toString())) {
                        Static.Data.put(((Player) sender).getUniqueId().toString(), new PlayerData(((Player) sender).getUniqueId().toString()));
                    }
                    PlayerData currentdata = Static.Data.get(((Player) sender).getUniqueId().toString());
                    String currentworld = ((Player) sender).getWorld().getName();

                    if (!Static.Config.IsWorldAllowed(currentworld)) {
                        Utils.SendError(sender, "Keep Inventory Choice is not permitted in this world.");
                        return false;
                    }

                    if (!currentdata.WorldData.containsKey(currentworld)) {
                        currentdata.WorldData.put(currentworld, Static.Config.getDefault().KeepItems);
                    }

                    currentdata.WorldData.put(currentworld, !currentdata.WorldData.get(currentworld));

                    sender.sendMessage("Keep Inventory for world \"" + currentworld + "\" is now " + Utils.FriendlyBool(currentdata.WorldData.get(currentworld)));

                    Static.Plugin.WritePlayerData();
                } else {
                    Utils.SendError(sender, "Please specify a world and a player, or run /keepinventory help for a list of options");

                    return false;
                }
            } else {
                String action = args[0];
                if (Objects.equals(action, "help")) {
                    sender.sendMessage(ChatColor.BLUE + "KeepChoice v" + Constants.Version + ChatColor.RESET + " Help Page");
                    sender.sendMessage("<> denotes mandatory argument, [] denotes optional argument.");
                    if (Utils.isAdmin(sender)) {
                        sender.sendMessage(
                                ChatColor.AQUA+"/keepinventory" + ChatColor.RESET + "[world] [player]\n" +
                                        "    Toggles keep inventory status (if the player is allowed to) for [world] if specified, and for [player] if specified (themself if not)\n" +
                                        ChatColor.AQUA+"/keepinventory" + ChatColor.RESET + " info\n" +
                                        "    Lists world configurations stored in data\n" +
                                        ChatColor.AQUA+"/keepinventory" + ChatColor.RESET + " list\n" +
                                        "    Lists all player data\n" +
                                        ChatColor.AQUA+"/keepinventory" + ChatColor.RESET + " setrunson <world> <yes|no>\n" +
                                        "    Set whether this plugin runs on <world>.\n" +
                                        ChatColor.AQUA+"/keepinventory" + ChatColor.RESET + " setdefaultkion <world> <yes|no> [force|noforce]\n" +
                                        "    Set the default keep inventory status for <world>. If force is specified, every player's settings will be updated to match this. If not, only newly joined players will experience the change. \n" +
                                        ChatColor.AQUA+"/keepinventory" + ChatColor.RESET + " reload\n" +
                                        "    Reload configuration from the disk"
                        );
                    } else {
                        sender.sendMessage(
                                ChatColor.AQUA+"/keepinventory" + ChatColor.RESET + " [world]\n" +
                                        "    Toggles keep inventory status for [world] if specified (else use current world)\n"
                        );
                    }

                } else if (action.equals("info")) {
                    sender.sendMessage("Default Keep Inventory?:");
                    for (String k : Static.Config.WorldSettings.keySet()) {
                        World w = Bukkit.getWorld(k);
                        if (w == null && !k.equals("default")) {
                            //Exemption for default
                        } else {
                            try {
                                if (!w.getGameRuleValue(GameRule.KEEP_INVENTORY)) {
                                    sender.sendMessage("World " + k + ": " + Utils.FriendlyBool(Static.Config.WorldSettings.get(k).KeepItems) + " (Disabled)");
                                } else {
                                    sender.sendMessage("World " + k + ": " + Utils.FriendlyBool(Static.Config.WorldSettings.get(k).KeepItems));
                                }
                            } catch (Exception e) {
                                //Prevent default from being ignored
                                sender.sendMessage("World " + k + ": " + Utils.FriendlyBool(Static.Config.WorldSettings.get(k).KeepItems));
                            }
                        }

                    }
                } else if (action.equals("list")) {
                    sender.sendMessage("Note that data will appear whether or not this plugin is allowed in that world.");
                    for (PlayerData p : Static.Data.values()) {
                        sender.sendMessage(Utils.SafeGetNameOrUUID(p.Player) + ":");
                        for (String worldkey : p.WorldData.keySet()) {
                            sender.sendMessage("    " + worldkey + ": " + Utils.FriendlyBool(p.WorldData.get(worldkey)));
                        }
                    }
                } else if (action.equals("setrunson")) {
                    if (args.length < 3) {
                        Utils.SendError(sender,"Not enough arguments");
                        return false;
                    }

                    if (!Utils.isAdmin(sender)) {
                        Utils.SendError(sender,"Insufficient Privilege!");
                        return false;
                    }

                    String forworld = args[1];
                    boolean newstate = Utils.BoolFromS(args[2]);

                    if (forworld.equals("#all")) {
                        //Disable all of none
                        if (newstate) {
                            Static.Config.RunInWorlds.clear();
                            for (World w:Bukkit.getWorlds()) {
                                if (!w.getGameRuleValue(GameRule.KEEP_INVENTORY)) {
                                    sender.sendMessage(ChatColor.YELLOW+"Skipping world "+w.getName());
                                    continue;
                                }

                                Static.Config.RunInWorlds.add(w.getName());
                            }
                            Static.RawConfig.set("runinworlds",new String[]{"*"});
                            Static.Plugin.saveConfig();
                            Utils.SendSuccess(sender,"Plugin enabled on all worlds where keepinventory = true.");

                        } else {
                            Static.Config.RunInWorlds.clear();
                            Static.RawConfig.set("runinworlds",new String[]{"__placeholder"});
                            Static.Plugin.saveConfig();
                            Utils.SendSuccess(sender,"Plugin disabled on all worlds.");
                        }


                    } else {
                        if (newstate) {
                            Static.Config.RunInWorlds.add(forworld);
                        } else {
                            Static.Config.RunInWorlds.remove(forworld);

                        }
                        Static.RawConfig.set("runinworlds",Static.Config.RunInWorlds.toArray());

                        Utils.SendSuccess(sender,"Updated plugin state for " + forworld);

                    }
                } else if (action.equals("setdefaultkion")) {

                    if (args.length < 3) {
                        Utils.SendError(sender,"Not enough arguments");
                        return false;
                    }

                    if (!Utils.isAdmin(sender)) {
                        Utils.SendError(sender,"Insufficient Privilege!");
                        return false;
                    }

                    String forworld = args[1];
                    boolean newstate = Utils.BoolFromS(args[2]);
                    boolean force = false;
                    if (args.length >= 4) {
                        if (args[3].equals("force")) {
                            force = true;
                        }
                    }

                    if (!forworld.equals("#default") && Bukkit.getWorld(forworld) == null) {
                        Utils.SendError(sender,"Invalid world name. Either use a world name, or #default to edit the default settings");
                        return false;
                    }

                    if (forworld.equals("#default")) {
                        ConfigDefaultBehaviour olddata = Static.Config.WorldSettings.get("default");
                        olddata.KeepItems = newstate;
                        Static.Config.WorldSettings.put("default",olddata);
                        if (force) {
                            for (PlayerData p:Static.Data.values()) {
                                for (String worldname:p.WorldData.keySet()) {
                                    if (!Static.Config.IsWorldKnown(worldname)) {
                                        //Using default
                                        p.WorldData.put(worldname,newstate);
                                    }
                                }
                            }
                        }
                        Static.RawConfig.set("default.Enabled",newstate);
                        Static.Plugin.saveConfig();

                    } else {
                        ConfigDefaultBehaviour olddata = Static.Config.WorldSettings.get(forworld);
                        if (olddata == null) {
                            olddata = new ConfigDefaultBehaviour(newstate);
                        }
                        olddata.KeepItems = newstate;
                        Static.Config.WorldSettings.put(forworld,olddata);
                        Static.Plugin.saveConfig();
                    }

                } else if (action.equals("reload")) {
                    if (Utils.isAdmin(sender)) {
                        Static.Config = new Config(Static.Plugin.getConfig());
                        Utils.SendSuccess(sender, "Reloaded");
                    } else {
                        Utils.SendError(sender,"Insufficient Privilege!");
                        return false;
                    }
                } else {
                    if (args.length == 1) {
                        //Spec woprld only
                        String currentworld = args[0];
                        if (sender instanceof Player) {
                            if (!Static.Data.containsKey(((Player) sender).getUniqueId().toString())) {
                                Static.Data.put(((Player) sender).getUniqueId().toString(), new PlayerData(((Player) sender).getUniqueId().toString()));
                            }
                            PlayerData currentdata = Static.Data.get(((Player) sender).getUniqueId().toString());

                            if (!Static.Config.IsWorldAllowed(currentworld)) {
                                Utils.SendError(sender, "Keep Inventory Choice is not permitted in this world.");
                                return false;
                            }

                            if (!currentdata.WorldData.containsKey(currentworld)) {
                                currentdata.WorldData.put(currentworld, Static.Config.getDefault().KeepItems);
                            }

                            currentdata.WorldData.put(currentworld, !currentdata.WorldData.get(currentworld));

                            sender.sendMessage("Keep Inventory for world \"" + currentworld + "\" is now " + Utils.FriendlyBool(currentdata.WorldData.get(currentworld)));

                            Static.Plugin.WritePlayerData();
                        } else {
                            Utils.SendError(sender, "Please specify a world and a player, or run /keepinventory help for a list of options");

                            return false;
                        }
                    } else {
                        if (!Utils.isAdmin(sender)) {
                            Utils.SendError(sender, "Insufficient Permission");
                            return false;
                        }
                        //World and player
                        String currentworld = args[0];
                        String sfname = args[1];


                        if (!Static.Data.containsKey(Bukkit.getOfflinePlayer(sfname).getUniqueId().toString())) {
                            Static.Data.put(Bukkit.getOfflinePlayer(sfname).getUniqueId().toString(), new PlayerData(Bukkit.getOfflinePlayer(sfname).getUniqueId().toString()));
                        }
                        PlayerData currentdata = Static.Data.get(Bukkit.getOfflinePlayer(sfname).getUniqueId().toString());

                        if (!Static.Config.IsWorldAllowed(currentworld)) {
                            Utils.SendError(sender, "Keep Inventory Choice is not permitted in this world.");
                            return false;
                        }

                        if (!currentdata.WorldData.containsKey(currentworld)) {
                            currentdata.WorldData.put(currentworld, Static.Config.getDefault().KeepItems);
                        }

                        currentdata.WorldData.put(currentworld, !currentdata.WorldData.get(currentworld));

                        sender.sendMessage("Keep Inventory for world \"" + currentworld + "\" is now " + Utils.FriendlyBool(currentdata.WorldData.get(currentworld)) + " for " + Utils.SafeGetNameOrUUID(currentdata.Player));

                        Static.Plugin.WritePlayerData();

                    }
                }
            }
            return true;
        } catch (Exception e) {
            Utils.SendError(sender,"ERROR! - "+e.getMessage());
        }

        return true;
    }

}
