package me.skillissue.permissionsystem.cmd;

import me.skillissue.permissionsystem.structures.PermissionPlayer;
import me.skillissue.permissionsystem.utils.GroupStorage;
import me.skillissue.permissionsystem.utils.PlayerStorage;
import me.skillissue.permissionsystem.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PermissionSystemCommand implements CommandExecutor {

  @Override
  public boolean onCommand(
      @NotNull CommandSender commandSender,
      @NotNull Command command,
      @NotNull String s,
      @NotNull String[] args) {
    if (!commandSender.hasPermission("permissionsystem")) {
      commandSender.sendMessage(StringUtils.formatMessage("%no_permission"));
      return true;
    }
    if (args.length == 0) {
      commandSender.sendMessage(
          StringUtils.formatMessage("%command_invalid_usage")
              .replace("%command", command.getName()));
      return true;
    }
    switch (args[0].toLowerCase()) {
      case "user":
        if (!commandSender.hasPermission("permissionsystem.user")) {
          commandSender.sendMessage(StringUtils.formatMessage("%no_permission"));
          return true;
        }
        handleUser(commandSender, args);
        break;
      case "group":
        if (!commandSender.hasPermission("permissionsystem.group")) {
          commandSender.sendMessage(StringUtils.formatMessage("%no_permission"));
          return true;
        }
        handleGroup(commandSender, args);
        break;
      case "help":
        printHelp(commandSender, command.getName());
        break;
      default:
        commandSender.sendMessage(
            StringUtils.formatMessage("%command_invalid_usage")
                .replace("%command", command.getName()));
        break;
    }
    return true;
  }

  private void handleGroup(CommandSender commandSender, String[] args) {
    if (args.length <= 1) {
      commandSender.sendMessage(
          StringUtils.formatMessage("%command_invalid_usage")
              .replace("%command", "permissionsystem"));
      return;
    }

    if (args.length == 2) {
      if (!commandSender.hasPermission("permissionsystem.group.info")) {
        commandSender.sendMessage(StringUtils.formatMessage("%no_permission"));
        return;
      }
      if (GroupStorage.getGroupByName(args[1]) == null) {
        commandSender.sendMessage(StringUtils.formatMessage("%group_not_found"));
        return;
      }
      commandSender.sendMessage(
          StringUtils.formatMessage("%group_info")
              .replace("%group", args[1])
              .replace(
                  "%prefix", GroupStorage.getGroupByName(args[1])
                              .getPrefix().replace("§", "&"))
              .replace(
                  "%permissions",
                  String.join(", ", GroupStorage.getGroupByName(args[1]).getPermissions())));
      return;
    }

    switch (args[1].toLowerCase()) {
      case "create":
        if (!commandSender.hasPermission("permissionsystem.group.create")) {
          commandSender.sendMessage(StringUtils.formatMessage("%no_permission"));
          return;
        }
        groupCreate(commandSender, args);
        break;
      case "delete":
        if (!commandSender.hasPermission("permissionsystem.group.delete")) {
          commandSender.sendMessage(StringUtils.formatMessage("%no_permission"));
          return;
        }
        groupDelete(commandSender, args);
        break;
      case "add":
        if (!commandSender.hasPermission("permissionsystem.group.add")) {
          commandSender.sendMessage(StringUtils.formatMessage("%no_permission"));
          return;
        }
        groupAdd(commandSender, args);
        break;
      case "remove":
        if (!commandSender.hasPermission("permissionsystem.group.remove")) {
          commandSender.sendMessage(StringUtils.formatMessage("%no_permission"));
          return;
        }
        groupRemove(commandSender, args);
        break;
      case "prefix":
        if (!commandSender.hasPermission("permissionsystem.group.prefix")) {
          commandSender.sendMessage(StringUtils.formatMessage("%no_permission"));
          return;
        }
        groupPrefix(commandSender, args);
        break;
      default:
        commandSender.sendMessage(
            StringUtils.formatMessage("%command_invalid_usage")
                .replace("%command", "permissionsystem group"));
        break;
    }
  }

  private void groupPrefix(CommandSender commandSender, String[] args) {
    if (args.length <= 4) {
      commandSender.sendMessage(
          StringUtils.formatMessage("%command_invalid_usage")
              .replace("%command", "permissionsystem"));
      return;
    }
    if (GroupStorage.getGroupByName(args[3]) == null) {
      commandSender.sendMessage(StringUtils.formatMessage("%group_not_found"));
      return;
    }
    GroupStorage.getGroupByName(args[3]).setPrefix(args[4].replace("&", "§"));
    commandSender.sendMessage(StringUtils.formatMessage("%group_prefix_set").replace("%updated", args[4]));
  }

  private void groupRemove(CommandSender commandSender, String[] args) {
    if (args.length <= 4) {
      commandSender.sendMessage(
          StringUtils.formatMessage("%command_invalid_usage")
              .replace("%command", "permissionsystem"));
      return;
    }
    if (GroupStorage.getGroupByName(args[3]) == null) {
      commandSender.sendMessage(StringUtils.formatMessage("%group_not_found"));
      return;
    }
    if (!GroupStorage.getGroupByName(args[3]).hasPermission(args[4])) {
      commandSender.sendMessage(StringUtils.formatMessage("%group_doesnt_have_permission"));
      return;
    }
    GroupStorage.getGroupByName(args[3]).removePermission(args[4]);
    commandSender.sendMessage(
        StringUtils.formatMessage("%group_removed_permission")
            .replace("%permission", args[4])
            .replace("%group", args[3]));
  }

  private void groupAdd(CommandSender commandSender, String[] args) {
    if (args.length <= 4) {
      commandSender.sendMessage(
          StringUtils.formatMessage("%command_invalid_usage")
              .replace("%command", "permissionsystem"));
      return;
    }
    if (GroupStorage.getGroupByName(args[3]) == null) {
      commandSender.sendMessage(StringUtils.formatMessage("%group_not_found"));
      return;
    }
    if (GroupStorage.getGroupByName(args[3]).hasPermission(args[4])) {
      commandSender.sendMessage(StringUtils.formatMessage("%group_already_has_permission"));
      return;
    }
    GroupStorage.getGroupByName(args[3]).addPermission(args[4]);
    commandSender.sendMessage(
        StringUtils.formatMessage("%group_added_permission")
            .replace("%permission", args[4])
            .replace("%group", args[3]));
  }

  private void groupDelete(CommandSender commandSender, String[] args) {
    if (args.length <= 5) {
      commandSender.sendMessage(
          StringUtils.formatMessage("%command_invalid_usage")
              .replace("%command", "permissionsystem"));
      return;
    }
    if (GroupStorage.getGroupByName(args[2]) == null) {
      commandSender.sendMessage(StringUtils.formatMessage("%group_not_found"));
      return;
    }
    GroupStorage.deleteGroup(GroupStorage.getGroupByName(args[2]));
    commandSender.sendMessage(
        StringUtils.formatMessage("%group_deleted").replace("%group", args[2]));
  }

  private void groupCreate(CommandSender commandSender, String[] args) {
    if (args.length <= 5) {
      commandSender.sendMessage(
          StringUtils.formatMessage("%command_invalid_usage")
              .replace("%command", "permissionsystem"));
      return;
    }
    if (GroupStorage.getGroupByName(args[2]) != null) {
      commandSender.sendMessage(StringUtils.formatMessage("%group_already_exists"));
      return;
    }
    if (!GroupStorage.addGroup(args[2])) {
      commandSender.sendMessage(StringUtils.formatMessage("%error"));
      return;
    }
    commandSender.sendMessage(
        StringUtils.formatMessage("%group_created").replace("%group", args[2]));
  }

  private void handleUser(CommandSender commandSender, String[] args) {
    if (args.length <= 1) {
      commandSender.sendMessage(
          StringUtils.formatMessage("%command_invalid_usage")
              .replace("%command", "permissionsystem"));
      return;
    }
    OfflinePlayer player = Bukkit.getPlayer(args[1]);
    if (player == null) {
      player = Bukkit.getOfflinePlayer(args[1]);
    }
    if (args.length == 2) {
      if (!commandSender.hasPermission("permissionsystem.user.info")) {
        commandSender.sendMessage(StringUtils.formatMessage("%no_permission"));
        return;
      }
      commandSender.sendMessage(
          StringUtils.formatMessage("%user_info")
              .replace("%username", player.getName())
              .replace("%group", PlayerStorage.getPermissionData(player).getGroup().getName())
              .replace(
                  "%expire",
                  StringUtils.formatTime(
                      PlayerStorage.getPermissionData(player).getRankExpire(), 2))
              .replace(
                  "%permissions",
                  String.join(", ", PlayerStorage.getPermissionData(player).getPermissions())));
      return;
    }
    switch (args[2].toLowerCase()) {
      case "group":
        if (!commandSender.hasPermission("permissionsystem.user.group")) {
          commandSender.sendMessage(StringUtils.formatMessage("%no_permission"));
          return;
        }
        userGroup(commandSender, args);
        break;
      case "add":
        if (!commandSender.hasPermission("permissionsystem.user.add")) {
          commandSender.sendMessage(StringUtils.formatMessage("%no_permission"));
          return;
        }
        userAdd(commandSender, args);
        break;
      case "remove":
        if (!commandSender.hasPermission("permissionsystem.user.remove")) {
          commandSender.sendMessage(StringUtils.formatMessage("%no_permission"));
          return;
        }
        userRemove(commandSender, args);
        break;
      default:
        commandSender.sendMessage(
            StringUtils.formatMessage("%command_invalid_usage")
                .replace("%command", "permissionsystem"));
        break;
    }
  }

  private void userRemove(CommandSender commandSender, String[] args) {
    if (args.length <= 4) {
      commandSender.sendMessage(
          StringUtils.formatMessage("%command_invalid_usage")
              .replace("%command", "permissionsystem"));
      return;
    }
    Player player = (Player) commandSender;
    PermissionPlayer permissionPlayer = PlayerStorage.getPermissionData(player);
    if (!permissionPlayer.hasPermission(args[3])) {
      commandSender.sendMessage(StringUtils.formatMessage("%user_doesnt_have_permission"));
      return;
    }
    permissionPlayer.removePermission(args[3]);
    commandSender.sendMessage(
        StringUtils.formatMessage("%user_removed_permission")
            .replace("%permission", args[3])
            .replace("%target", player.getName()));
  }

  private void userAdd(CommandSender commandSender, String[] args) {
    if (args.length <= 4) {
      commandSender.sendMessage(
          StringUtils.formatMessage("%command_invalid_usage")
              .replace("%command", "permissionsystem"));
      return;
    }
    Player player = (Player) commandSender;
    PermissionPlayer permissionPlayer = PlayerStorage.getPermissionData(player);
    if (permissionPlayer.hasPermission(args[3])) {
      commandSender.sendMessage(StringUtils.formatMessage("%user_already_has_permission"));
      return;
    }
    permissionPlayer.addPermission(args[3]);
    commandSender.sendMessage(
        StringUtils.formatMessage("%user_added_permission")
            .replace("%permission", args[3])
            .replace("%target", player.getName()));
  }

  private void userGroup(CommandSender commandSender, String[] args) {
    if (args.length <= 6) {
      commandSender.sendMessage(
          StringUtils.formatMessage("%command_invalid_usage")
              .replace("%command", "permissionsystem"));
      return;
    }
    Player player = (Player) commandSender;
    PermissionPlayer permissionPlayer = PlayerStorage.getPermissionData(player);
    if (GroupStorage.getGroupByName(args[3]) == null) {
      commandSender.sendMessage(StringUtils.formatMessage("%group_not_found"));
      return;
    }
    long expire = 0;
    if (args.length == 4) {
      expire = StringUtils.parseTime(args[4]);
      if (expire == 0) {
        commandSender.sendMessage(
            StringUtils.formatMessage("%command_invalid_usage")
                .replace("%command", "permissionsystem"));
        return;
      }
    }
    permissionPlayer.setGroup(GroupStorage.getGroupByName(args[3]), expire);
    commandSender.sendMessage(
        StringUtils.formatMessage("%user_group_set")
            .replace("%group", args[3])
            .replace("%target", player.getName())
            .replace("%expire", StringUtils.formatTime(expire, 6)));
  }

  private void printHelp(CommandSender commandSender, String commandName) {
    commandSender.sendMessage(
        StringUtils.formatMessage("%command_help").replace("%command", commandName));
  }
}
