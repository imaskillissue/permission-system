package me.skillissue.permissionsystem;

import me.skillissue.permissionsystem.cmd.PermissionSystemCommand;
import me.skillissue.permissionsystem.listeners.ServerMemberHandler;
import me.skillissue.permissionsystem.sql.SqlConnection;
import me.skillissue.permissionsystem.utils.Config;
import me.skillissue.permissionsystem.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class PermissionSystem extends JavaPlugin {
  private static PermissionSystem instance;
  public SqlConnection sql;

  @Override
  public void onEnable() {
    new Config();
    instance = this;
    sql = new SqlConnection("127.0.0.1", 3306, "permissions", "valo", "pw");
    Bukkit.getConsoleSender().sendMessage("Cmd: " + this.getCommand("permissionsystem"));
    CommandExecutor cmd = new PermissionSystemCommand();
    this.getCommand("permissionsystem").setExecutor(cmd);
    this.getCommand("ps").setExecutor(cmd);
    this.getCommand("perms").setExecutor(cmd);
    Bukkit.getConsoleSender().sendMessage(StringUtils.formatMessage(StringUtils.formatMessage("%plugin_loaded")));
    Bukkit.getPluginManager().registerEvents(new ServerMemberHandler(), this);
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }

  public static PermissionSystem getInstance() {
    return instance;
  }
}
