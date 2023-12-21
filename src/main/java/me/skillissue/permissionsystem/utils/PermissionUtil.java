package me.skillissue.permissionsystem.utils;

import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

public class PermissionUtil {

  public static PermissionAttachment setPermission(
      Player player, String permission, boolean value) {
    return player.addAttachment(
        Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("PermissionSystem")),
        permission,
        value);
  }

  public static PermissionAttachment addPermission(Player player, String permission) {
    return setPermission(player, permission, true);
  }

  public static PermissionAttachment removePermission(Player player, String permission) {
    return setPermission(player, permission, false);
  }
}
