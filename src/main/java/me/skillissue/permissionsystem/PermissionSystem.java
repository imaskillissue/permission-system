package me.skillissue.permissionsystem;

import me.skillissue.permissionsystem.cmd.PermissionSystemCommand;
import me.skillissue.permissionsystem.listeners.ServerMemberHandler;
import me.skillissue.permissionsystem.sql.SqlConnection;
import me.skillissue.permissionsystem.structures.PermissionPlayer;
import me.skillissue.permissionsystem.utils.Config;
import me.skillissue.permissionsystem.utils.GroupStorage;
import me.skillissue.permissionsystem.utils.PlayerStorage;
import me.skillissue.permissionsystem.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PermissionSystem extends JavaPlugin {
  private static PermissionSystem instance;
  public SqlConnection sql;

  @Override
  public void onEnable() {
    new Config();
    instance = this;
    sql = new SqlConnection();
    if (!sql.isConnected()) {
      Bukkit.getConsoleSender().sendMessage(StringUtils.formatMessage("%sql_not_connected"));
      Bukkit.getPluginManager().disablePlugin(this);
      return;
    } else {
      Bukkit.getConsoleSender().sendMessage(StringUtils.formatMessage("%sql_connected"));
    }
    Bukkit.getConsoleSender().sendMessage("Cmd: " + this.getCommand("permissionsystem"));
    CommandExecutor cmd = new PermissionSystemCommand();
    this.getCommand("permissionsystem").setExecutor(cmd);
    this.getCommand("ps").setExecutor(cmd);
    this.getCommand("perms").setExecutor(cmd);
    Bukkit.getConsoleSender().sendMessage(StringUtils.formatMessage(StringUtils.formatMessage("%plugin_loaded")));
    Bukkit.getPluginManager().registerEvents(new ServerMemberHandler(), this);
    Bukkit.getScheduler().runTaskTimer(this, () -> {
      GroupStorage.reload();
      PlayerStorage.reload();
    }, 0L, 20L * Config.getInstance().sql_sync_interval);
    Bukkit.getScheduler().runTaskTimer(this, () -> {
      for (Player player : Bukkit.getOnlinePlayers()) {
        PermissionPlayer permissionPlayer = PlayerStorage.getPermissionData(player);
        if (permissionPlayer.getRankExpire() > 0 && permissionPlayer.getRankExpire() < System.currentTimeMillis()) {
          permissionPlayer.setGroup(GroupStorage.getGroupById(1));
          permissionPlayer.setRankExpire(0L);
        }
      }
    }, 0L, 20L);
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }

  public static PermissionSystem getInstance() {
    return instance;
  }
}
