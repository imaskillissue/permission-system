package me.skillissue.permissionsystem.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import me.skillissue.permissionsystem.tests.FooConfig;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

class FileConfigUtilTest {

  @Test
  void saveObjectToConfig() {
    FooConfig config = new FooConfig();
    File file = new File("john/doe", "FooSave");
    if (file.exists()) file.getParentFile().delete();
    FileConfigUtil.saveObjectToConfig(config, file);
    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    assertEquals(yamlConfiguration.get("text"), config.text);
    assertEquals(yamlConfiguration.get("wha"), config.getWha());
    assertEquals(yamlConfiguration.get("number"), config.getNumber());
    assertNull(yamlConfiguration.get("not_savable"));
  }

  @Test
  void loadConfig() {
    FooConfig config = new FooConfig();
    File file = new File("john/doe", "FooRead");
    file.delete();
    FileConfigUtil.saveObjectToConfig(config, file);
    config.text = "NO";
    config.setNumber(42);
    config.setWha(Math.random());
    FileConfigUtil.loadConfig(config, file);
    assertEquals("HALLO HALLO WELT", config.text);
    assertEquals(Integer.MIN_VALUE, config.getNumber());
    assertEquals(Math.PI, config.getWha());
  }
}
