package me.skillissue.permissionsystem.listeners;

import me.skillissue.permissionsystem.structures.PermissionPlayer;
import me.skillissue.permissionsystem.utils.PlayerStorage;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ServerMemberHandler implements Listener {
 @EventHandler(ignoreCancelled = true)
 public void onPlayerJoin(PlayerJoinEvent event) {
  PermissionPlayer permissionPlayer = PlayerStorage.getPermissionData(event.getPlayer());
  event.getPlayer().displayName(Component.text(permissionPlayer.getGroup().getPrefix()+event.getPlayer().getName()));

 }

}
