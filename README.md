# KeepChoice
*Place Keep Inventory in the Players' hands.*

---

## Server Requirements

- Java 11 or newer
- Minecraft 1.16.5 or newer (this plugin supports all versions after this, including 1.21.3 and etc...)

### What's new?

**VERSION 1.1**

- Add internationalization support
  - Edit language.yml to customize messages
  - Has colour support
- Move /keepinventory info to /keepinventory worldinfo
- Remove /keepinventory list as it was effectively for debug
- Add /keepinventory playerinfo \<player\> that does effectively the same thing

## Setting Up

- Paste the .jar file you download into your server's plugins directory (ROOT/plugins)
- Fully restart your server
- Execute commands as necessary either by reading `COMMANDS` or by reading `COMMON SITUATIONS TO SETUP`

## Common Situations To Setup

### What happens by default?
If you make no changes to the default configuration, this will happen:
- All players will lose their inventory on all worlds, regardless of the KeepInventory Gamerule. 
- However, the player can choose to keep their inventory with the `/ki` command. 
- You should probably read this documentation to see how you can configure this plugin to suit your needs, unless you want this to happen.

### Keep Inventory on All Worlds

To set keep inventory on all worlds (giving the player a choice), run:
- `/ki setdefaultkion #default yes`

To not give the player a choice whether they keep their inventory, run:
- `/ki setrunson #all no`
- `/gamerule keepInventory true`
Note that this method effectively disables the plugin.

### Run on a few worlds only

For example, let's say I have two worlds: pvp and survival. I want the player to always keep inventory on pvp, but have a choice on survival (but default to no). We would run the following:
- `/ki setrunson #all no` (shut off plugin)
- `/gamerule keepInventory true` (run this on pvp)
- `/ki setrunson survival yes` (allow to run on survival)
- `/ki setdefaultkion survival no` (set default to no on survival.)

## Permissions

- keepchoice.use - Meant for standard players; Allows the player to change their own keep inventory status on worlds where admins have set it to be allowed.
- keepchoice.admin - Meant for admins; Allows configurations of the plugin and changes for other players.

## Commands
### /keepinventory

- /keepinventory \[world] \[player]
  - Requires `keepchoice.use` permission for world and base
  - Requires `keepchoice.admin` permission for player argument
  - If neither world nor player is specified, the player who executed this command has their Keep Inventory status toggled for their specific world.
  - If only a world is specified, the player who executed the command's keep inventory status will be changed for the precise \[world] they provided
  - If both a world and a player is specified, the \[player]'s setting for \[world] will be toggled.
- /keepinventory help
  - Requires no permissions 
  - Displays the help menu
  - Help menu will be customized depending on whether the player has permission or not.
- /keepinventory reload
  - Requires `keepchoice.admin` permission
  - Reloads configuration from the config.yml file on the server's filesystem
  - Use it when you want to update changes you have made manually.
- /keepinventory info
  - Requires `keepchoice.admin` permission
  - Lists information about per-world configuration
  - Includes Default
  - Example output: `World default: no` (shows that the default is to have no keep inventory)
- /keepinventory list
  - Requires `keepchoice.admin` permission
  - Lists information about per-player configuration
  - Shows each user's request for each world
  - **Warning: Can produce long output!**
- /keepinventory setrunson <world> <yes|no>
  - Requires `keepchoice.admin` permission
  - Sets if the plugin is allowed to run on <world> (either yes or no)
  - If disabled, Minecraft will obey the KeepInventory gamerule.
  - If enabled, Minecraft will obey either the default or per-world configuration you have set with this plugin.
- /keepinventory setdefaultkion <world> <yes|no> \[force]
  - Requires `keepchoice.admin` permission
  - Sets the default keep inventory value for new players on <world> (either yes or no)
  - if force is specified, all players, regardless of when they joined will have their setting changed to reflect the setting chosen here.