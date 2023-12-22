package me.skillissue.permissionsystem.utils;

import java.io.File;
import java.util.HashMap;

public class Config {
  private static Config instance;
  @FileConfigField public int sql_sync_interval = 30;
  @FileConfigField public HashMap<String, String> messages = new HashMap<>();

  public Config() {
    instance = this;
    messages.put(
        "command_help",
        """
        %prefix §6/%command Help
        §7/%command help §8- §7Shows this help
        §7/%command user <username> §8- §7Shows user info
        §7/%command user <username> group <group> §8- §7Sets user group
        §7/%command user <username> group <group> <time> §8- §7Sets user group for a specific time
        §7/%command user <username> add <permission> §8- §7Adds permission to user
        §7/%command user <username> remove <permission> §8- §7Removes permission from user
        §7/%command group <group> §8- §7Shows group info
        §7/%command group <group> create §8- §7Creates group
        §7/%command group <group> delete §8- §7Deletes group
        §7/%command group <group> add <permission> §8- §7Adds permission to group
        §7/%command group <group> remove <permission> §8- §7Removes permission from group
        §7/%command group <group> prefix <prefix> §8- §7Sets group prefix
        §cTime Format: §4CASE SENSITIVE
        §a1s §7= §a1 Second
        §a1m §7= §a1 Minute
        §a1h §7= §a1 Hour
        §a1d §7= §a1 Day
        §a1M §7= §a1 Month
        §a1y §7= §a1 Year""");
    messages.put("command_invalid_usage", "%prefix §cInvalid usage! Use /%command help");
    messages.put("group_added_permission", "%prefix §7added permission!");
    messages.put("group_already_exists", "%prefix §cGroup already exists!");
    messages.put("group_already_has_permission", "%prefix The target already has permission!");
    messages.put("group_created", "%prefix §aGroup created!");
    messages.put("group_deleted", "%prefix §aGroup deleted!");
    messages.put("group_doesnt_have_permission", "%prefix §7doesn't have permission!");
    messages.put(
        "group_info",
        """
        %prefix §6Group Info
        §7Name: §a%name
        §7Prefix: §a%gprefix
        §7Permissions: §a%permissions""");
    messages.put("group_not_found", "%prefix §cGroup not found!");
    messages.put("group_prefix_set", "%prefix §7The prefix is now %updated!");
    messages.put("group_removed_permission", "%prefix §7removed permission!");
    messages.put("group_set", "%prefix group is now %group for %expire!");
    messages.put("no_permission", "%prefix §cYou don't have permission to do that!");
    messages.put("no_player", "%prefix §cYou must be a player to do that!");
    messages.put("player_not_found", "%prefix §cPlayer not found!");
    messages.put("plugin_loaded", "%prefix §aPlugin loaded!");
    messages.put("prefix", "§cPermissionSystem §8»§7");
    messages.put("sign_line1", "§3Rank Info:");
    messages.put("sign_line2", "§3Group: §a%group");
    messages.put("sign_line3", "§3Expire: §a%expire");
    messages.put("sign_line4", "§eHave Fun!");
    messages.put("sql_connected", "%prefix §aConnected to MySQL!");
    messages.put("sql_sync", "%prefix §aSynced with MySQL Database!");
    messages.put("user_added_permission", "%prefix §7added permission!");
    messages.put("user_already_has_permission", "%prefix §7already has permission!");
    messages.put("user_doesnt_have_permission", "%prefix §7doesn't have permission!");
    messages.put("user_group_set", "%prefix §7%target's group is now %group for %expire!");
    messages.put(
        "user_info",
        """
        %prefix §6User Info
        §7Username: §a%username
        §7Group: §a%group
        §7Expire: §a%expire
        §7Permissions: §a%permissions""");
    messages.put("user_removed_permission", "%prefix §7removed permission!");
    File file = new File("plugins/PermissionSystem", "config.yml");
    if (!file.exists()) {
      FileConfigUtil.saveObjectToConfig(this, file);
      return;
    }
    FileConfigUtil.loadConfig(this, file);
  }

  public static Config getInstance() {
    return instance;
  }
}
