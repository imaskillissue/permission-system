package me.skillissue.permissionsystem.utils;

import me.skillissue.permissionsystem.structures.PermissionPlayer;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;

public class PlayerStorage {
 private static PlayerStorage instance;
 private PlayerStorage() {}
 public static PermissionPlayer getPermissionData(OfflinePlayer player) {
  PermissionPlayer permissionPlayer = new PermissionPlayer(player);
  permissionPlayer.setGroup(GroupStorage.getGroupByName("default"));
  return permissionPlayer;
 }

 private static void prepare() {
  if (instance == null) {
   instance = new PlayerStorage();

  }
 }
}
