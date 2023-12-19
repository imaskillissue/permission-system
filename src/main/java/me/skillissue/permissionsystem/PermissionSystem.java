package me.skillissue.permissionsystem;

import me.skillissue.permissionsystem.cmd.PermissionSystemCommand;
import me.skillissue.permissionsystem.sql.SqlConnection;
import me.skillissue.permissionsystem.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class PermissionSystem extends JavaPlugin {

  @Override
  public void onEnable() {
    new Config();
    SqlConnection connection = new SqlConnection("127.0.0.1", 3306, "permissions", "valo", "pw");
    Bukkit.getConsoleSender().sendMessage("Cmd: " + this.getCommand("permissionsystem"));
    CommandExecutor cmd = new PermissionSystemCommand();
    this.getCommand("permissionsystem").setExecutor(cmd);
    this.getCommand("ps").setExecutor(cmd);
    this.getCommand("perms").setExecutor(cmd);
    Bukkit.getConsoleSender().sendMessage(Config.getInstance().prefix + " Plugin was loaded");
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }


}
