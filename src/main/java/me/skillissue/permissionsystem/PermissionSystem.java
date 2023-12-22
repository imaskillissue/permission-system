package me.skillissue.permissionsystem;

import java.util.List;
import me.skillissue.permissionsystem.cmd.MyRankCommand;
import me.skillissue.permissionsystem.cmd.PermissionSystemCommand;
import me.skillissue.permissionsystem.injection.CraftBukkitInfo;
import me.skillissue.permissionsystem.listeners.MovementListener;
import me.skillissue.permissionsystem.listeners.ServerMemberHandler;
import me.skillissue.permissionsystem.sql.SqlConnection;
import me.skillissue.permissionsystem.structures.PermissionPlayer;
import me.skillissue.permissionsystem.utils.Config;
import me.skillissue.permissionsystem.utils.GroupStorage;
import me.skillissue.permissionsystem.utils.PlayerStorage;
import me.skillissue.permissionsystem.utils.StringUtils;
import org.bukkit.Bukkit;
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
    this.getCommand("permissionsystem").setExecutor(new PermissionSystemCommand());
    this.getCommand("permissionsystem").setAliases(List.of("ps", "perms"));
    this.getCommand("myrank").setExecutor(new MyRankCommand());
    this.getCommand("myrank").setAliases(List.of("rank"));
    Bukkit.getPluginManager().registerEvents(new ServerMemberHandler(), this);
    Bukkit.getPluginManager().registerEvents(new MovementListener(), this);
    Bukkit.getScheduler()
        .runTaskTimer(
            this,
            () -> {
              GroupStorage.reload();
              PlayerStorage.reload();
            },
            0L,
            20L * Config.getInstance().sql_sync_interval);
    Bukkit.getScheduler()
        .runTaskTimer(
            this,
            () -> {
              for (Player player : Bukkit.getOnlinePlayers()) {
                PermissionPlayer permissionPlayer = PlayerStorage.getPermissionData(player);
                if (permissionPlayer.getRankExpire() > 0
                    && permissionPlayer.getRankExpire() < System.currentTimeMillis()) {
                  permissionPlayer.setGroup(GroupStorage.getGroupById(1));
                  permissionPlayer.setRankExpire(0L);
                }
              }
            },
            0L,
            20L);
    String playerClass = CraftBukkitInfo.toClassName("entity.CraftHumanEntity");
    Bukkit.getConsoleSender().sendMessage(playerClass);
    Bukkit.getConsoleSender()
        .sendMessage(StringUtils.formatMessage(StringUtils.formatMessage("%plugin_loaded")));
  }

  @Override
  public void onDisable() {
    sql.close();
  }

  public static PermissionSystem getInstance() {
    return instance;
  }
}
