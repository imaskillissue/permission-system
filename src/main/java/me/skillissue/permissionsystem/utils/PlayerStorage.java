package me.skillissue.permissionsystem.utils;

import me.skillissue.permissionsystem.PermissionSystem;
import me.skillissue.permissionsystem.structures.PermissionPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerStorage {
  private static final HashMap<OfflinePlayer, PermissionPlayer> permissionPlayerHashMap =
      new HashMap<>();

  private PlayerStorage() {}

  public static PermissionPlayer getPermissionData(OfflinePlayer player) {
    if (permissionPlayerHashMap.containsKey(player)) {
      return permissionPlayerHashMap.get(player);
    }
    PermissionPlayer permissionPlayer = PermissionSystem.getInstance().sql.getPlayer(player.getUniqueId().toString());
    return permissionPlayer;
  }

  public static void loadPlayer(Player player) {
    permissionPlayerHashMap.put(player, getPermissionData(player));
  }

  public static void unloadPlayer(Player player) {
    permissionPlayerHashMap.remove(player);
  }

  public static void reload() {
    for (OfflinePlayer player : permissionPlayerHashMap.keySet()) {
      permissionPlayerHashMap.get(player).reload();
    }
  }
}
