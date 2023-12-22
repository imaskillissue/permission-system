package me.skillissue.permissionsystem.listeners;

import me.skillissue.permissionsystem.structures.PermissionPlayer;
import me.skillissue.permissionsystem.utils.PlayerStorage;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ServerMemberHandler implements Listener {
  @EventHandler(ignoreCancelled = true)
  public void onPlayerJoin(PlayerJoinEvent event) {
    PlayerStorage.loadPlayer(event.getPlayer());
    PermissionPlayer permissionPlayer = PlayerStorage.getPermissionData(event.getPlayer());
    event
        .getPlayer()
        .displayName(
            Component.text(
                permissionPlayer.getGroup().getPrefix() + event.getPlayer().getName() + "§r"));
  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerQuit(PlayerQuitEvent event) {
    PlayerStorage.unloadPlayer(event.getPlayer());
  }
}
