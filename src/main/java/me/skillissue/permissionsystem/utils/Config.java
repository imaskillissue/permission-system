package me.skillissue.permissionsystem.utils;

import java.io.File;
import java.io.IOException;

public class Config {
  private static Config instance;
  @FileConfigField public String prefix = "§cPermissionSystem §8»§7";

  public Config() {
    instance = this;
    File file = new File("plugins/PermissionSystem", "config.yml");
    if (!file.exists()) {
      try {
        file.getParentFile().mkdirs();
        file.createNewFile();
        FileConfigUtil.saveObjectToConfig(this, file);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return;
    }
    FileConfigUtil.loadConfig(this, file);
  }

  public static Config getInstance() {
    return instance;
  }
}
