package net.enderbyteprograms.KeepChoice.commands;

import net.enderbyteprograms.KeepChoice.KeepChoiceMain;
import net.enderbyteprograms.KeepChoice.Language.EPLanguage;
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

import java.io.File;
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
                        Utils.SendError(sender, EPLanguage.get("error.notenabled"));
                        return false;
                    }

                    if (!currentdata.WorldData.containsKey(currentworld)) {
                        currentdata.WorldData.put(currentworld, Static.Config.getDefault().KeepItems);
                    }

                    currentdata.WorldData.put(currentworld, !currentdata.WorldData.get(currentworld));

                    sender.sendMessage(EPLanguage.get("kihead") + currentworld + "\" : " + Utils.FriendlyBool(currentdata.WorldData.get(currentworld)));

                    Static.Plugin.WritePlayerData();
                } else {
                    Utils.SendError(sender, EPLanguage.get("error.unknown"));

                    return false;
                }
            } else {
                String action = args[0];
                if (Objects.equals(action, "help")) {
                    sender.sendMessage(ChatColor.BLUE + "KeepChoice v" + Constants.Version + ChatColor.RESET + " Help Page");
                    sender.sendMessage(EPLanguage.get("help.lineinfo"));
                    if (Utils.isAdmin(sender)) {
                        sender.sendMessage(
                                EPLanguage.get("help.adminhelp")
                        );
                    } else {
                        sender.sendMessage(
                                EPLanguage.get("help.userhelp")
                        );
                    }

                } else if (action.equals("worldinfo")) {
                    sender.sendMessage(EPLanguage.get("info.head"));
                    for (String k : Static.Config.WorldSettings.keySet()) {
                        World w = Bukkit.getWorld(k);
                        if (w == null && !k.equals("default")) {
                            //Exemption for default
                        } else {
                            try {
                                sender.sendMessage(EPLanguage.get("info.world")+" " + k + ": " + Utils.FriendlyBool(Static.Config.WorldSettings.get(k).KeepItems));
                            } catch (Exception e) {
                                //Prevent default from being ignored
                                sender.sendMessage(EPLanguage.get("info.world")+ " " + k + ": " + Utils.FriendlyBool(Static.Config.WorldSettings.get(k).KeepItems));
                            }
                        }

                    }
                } else if (action.equals("playerinfo")) {
                    sender.sendMessage(EPLanguage.get("list.head"));
                    String pl = args[1];
                    PlayerData p = null;
                    if (Bukkit.getPlayer(pl) == null) {
                        p = Static.Data.get(Bukkit.getOfflinePlayer(pl).getUniqueId().toString());
                    } else {
                        p = Static.Data.get(Bukkit.getPlayer(pl).getUniqueId().toString());
                    }
                        sender.sendMessage(Utils.SafeGetNameOrUUID(p.Player) + ":");
                        for (String worldkey : p.WorldData.keySet()) {
                            sender.sendMessage("    " + worldkey + ": " + Utils.FriendlyBool(p.WorldData.get(worldkey)));
                        }
                } else if (action.equals("setrunson")) {
                    if (args.length < 3) {
                        Utils.SendError(sender,EPLanguage.get("error.args"));
                        return false;
                    }

                    if (!Utils.isAdmin(sender)) {
                        Utils.SendError(sender,EPLanguage.get("error.perms"));
                        return false;
                    }

                    String forworld = args[1];
                    boolean newstate = Utils.BoolFromS(args[2]);

                    if (forworld.equals("#all")) {
                        //Disable all of none
                        if (newstate) {
                            Static.Config.RunInWorlds.clear();
                            for (World w:Bukkit.getWorlds()) {

                                Static.Config.RunInWorlds.add(w.getName());
                            }
                            Static.RawConfig.set("runinworlds",new String[]{"*"});
                            Static.Plugin.saveConfig();
                            Utils.SendSuccess(sender,EPLanguage.get("run.allenabled"));

                        } else {
                            Static.Config.RunInWorlds.clear();
                            Static.RawConfig.set("runinworlds",new String[]{"__placeholder"});
                            Static.Plugin.saveConfig();
                            Utils.SendSuccess(sender,EPLanguage.get("run.alldisabled"));
                        }


                    } else {
                        if (newstate) {
                            Static.Config.RunInWorlds.add(forworld);
                        } else {
                            Static.Config.RunInWorlds.remove(forworld);

                        }
                        Static.RawConfig.set("runinworlds",Static.Config.RunInWorlds.toArray());

                        Utils.SendSuccess(sender,EPLanguage.get("stateupdated") + forworld);

                    }
                } else if (action.equals("setdefaultkion")) {

                    if (args.length < 3) {
                        Utils.SendError(sender,EPLanguage.get("error.args"));
                        return false;
                    }

                    if (!Utils.isAdmin(sender)) {
                        Utils.SendError(sender,EPLanguage.get("error.perms"));
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
                        Utils.SendError(sender,EPLanguage.get("error.invalidworld"));
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
                        Utils.SendSuccess(sender,EPLanguage.get("update.default"));

                    } else {
                        ConfigDefaultBehaviour olddata = Static.Config.WorldSettings.get(forworld);
                        if (olddata == null) {
                            olddata = new ConfigDefaultBehaviour(newstate);
                        }
                        olddata.KeepItems = newstate;
                        Static.Config.WorldSettings.put(forworld,olddata);
                        Static.RawConfig.set(forworld+".Enabled",newstate);
                        if (force) {
                            for (PlayerData p:Static.Data.values()) {
                                p.WorldData.put(forworld,newstate);
                            }
                        }
                        Static.Plugin.saveConfig();
                        Utils.SendSuccess(sender,EPLanguage.get("update.world")+forworld);
                    }

                } else if (action.equals("reload")) {
                    if (Utils.isAdmin(sender)) {
                        Static.Config = new Config(Static.Plugin.getConfig());
                        EPLanguage.LoadConfigurationFile(new File(Static.Plugin.getDataFolder(),"language.yml"));
                        Utils.SendSuccess(sender, "Reloaded configuration and language");
                    } else {
                        Utils.SendError(sender,EPLanguage.get("error.perms"));
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
                                Utils.SendError(sender, EPLanguage.get("error.notenabled"));
                                return false;
                            }

                            if (!currentdata.WorldData.containsKey(currentworld)) {
                                currentdata.WorldData.put(currentworld, Static.Config.getDefault().KeepItems);
                            }

                            currentdata.WorldData.put(currentworld, !currentdata.WorldData.get(currentworld));

                            sender.sendMessage(EPLanguage.get("kihead") + currentworld + "\" : " + Utils.FriendlyBool(currentdata.WorldData.get(currentworld)));

                            Static.Plugin.WritePlayerData();
                        } else {
                            Utils.SendError(sender, EPLanguage.get("error.unknown"));

                            return false;
                        }
                    } else {
                        if (!Utils.isAdmin(sender)) {
                            Utils.SendError(sender, EPLanguage.get("error.perms"));
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
                            Utils.SendError(sender, EPLanguage.get("error.notenabled"));
                            return false;
                        }

                        if (!currentdata.WorldData.containsKey(currentworld)) {
                            currentdata.WorldData.put(currentworld, Static.Config.getDefault().KeepItems);
                        }

                        currentdata.WorldData.put(currentworld, !currentdata.WorldData.get(currentworld));

                        sender.sendMessage(EPLanguage.get("kihead") + currentworld + "\" : " + Utils.FriendlyBool(currentdata.WorldData.get(currentworld)) + " for " + Utils.SafeGetNameOrUUID(currentdata.Player));

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
