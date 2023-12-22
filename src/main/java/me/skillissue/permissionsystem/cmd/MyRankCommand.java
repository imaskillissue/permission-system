package me.skillissue.permissionsystem.cmd;

import me.skillissue.permissionsystem.structures.PermissionPlayer;
import me.skillissue.permissionsystem.utils.PlayerStorage;
import me.skillissue.permissionsystem.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MyRankCommand implements CommandExecutor {
  @Override
  public boolean onCommand(
      @NotNull CommandSender commandSender,
      @NotNull Command command,
      @NotNull String s,
      @NotNull String[] strings) {
    if (!(commandSender instanceof Player player)) {
      commandSender.sendMessage(StringUtils.formatMessage("%no_player"));
      return true;
    }
    PermissionPlayer permissionPlayer = PlayerStorage.getPermissionData(player);
    player.sendMessage(
        StringUtils.formatMessage("%user_info")
            .replace("%username", player.getName())
            .replace("%group", permissionPlayer.getGroup().getName())
            .replace("%expire", StringUtils.formatTime(permissionPlayer.getRankExpire(), 2))
            .replace("%permissions", String.join(", ", permissionPlayer.getPermissions())));
    return true;
  }
}
