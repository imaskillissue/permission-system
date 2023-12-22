package me.skillissue.permissionsystem.injection;

import org.bukkit.Bukkit;

public class CraftBukkitInfo {
  private static final String PACKAGE_NAME;

  static {
    PACKAGE_NAME = Bukkit.getOfflinePlayer("test").getClass().getPackageName();
  }

  public static String toClassName(String className) {
    return PACKAGE_NAME + '.' + className;
  }
}
