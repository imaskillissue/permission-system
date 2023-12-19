package me.skillissue.permissionsystem.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

public class FileConfigUtil {
  public static void saveObjectToConfig(Object o, File file) {
    HashMap<String, Object> map = new HashMap<>();
    for (Field field : o.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      if (!field.isAnnotationPresent(FileConfigField.class) || !field.canAccess(o)) {
        continue;
      }
      try {
        map.put(field.getName(), field.get(o));
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
    for (String key : map.keySet()) {
      configuration.set(key, map.get(key));
    }
    try {
      configuration.save(file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void loadConfig(Object o, File file) {
    if (!file.exists()) {
      return;
    }
    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
    for (Field field : o.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      if (!field.isAnnotationPresent(FileConfigField.class)
          || !field.canAccess(o)
          || !configuration.contains(field.getName())) {
        continue;
      }
      try {
        field.set(o, configuration.get(field.getName()));
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
