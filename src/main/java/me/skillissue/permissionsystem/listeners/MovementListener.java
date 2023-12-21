package me.skillissue.permissionsystem.listeners;

import java.util.HashMap;
import me.skillissue.permissionsystem.structures.PermissionPlayer;
import me.skillissue.permissionsystem.utils.PlayerStorage;
import me.skillissue.permissionsystem.utils.StringUtils;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MovementListener implements Listener {
  private final HashMap<Player, Long> lastMove = new HashMap<>();

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    if (!lastMove.containsKey(event.getPlayer())
        || System.currentTimeMillis() - lastMove.get(event.getPlayer()) > 3000) {
      lastMove.put(event.getPlayer(), System.currentTimeMillis());
      for (int x = 0; x < 10; x++) {
        for (int y = 0; y < 10; y++) {
          for (int z = 0; z < 10; z++) {
            if (event.getPlayer().getLocation().add(x, y, z).getBlock().getState()
                instanceof Sign) {
              Sign sign = (Sign) event.getPlayer().getLocation().add(x, y, z).getBlock().getState();
              if (sign.getLine(0).equalsIgnoreCase("[Permissions]")) {
                PermissionPlayer permissionPlayer =
                    PlayerStorage.getPermissionData(event.getPlayer());
                event
                    .getPlayer()
                    .sendSignChange(
                        event.getPlayer().getLocation().add(x, y, z),
                        new String[] {
                          StringUtils.formatMessage("%sign_line1")
                              .replace("%group%", permissionPlayer.getGroup().getName())
                              .replace(
                                  "%expire",
                                  StringUtils.formatTime(permissionPlayer.getRankExpire(), 3)),
                          StringUtils.formatMessage("%sign_line2")
                              .replace("%group%", permissionPlayer.getGroup().getName())
                              .replace(
                                  "%expire",
                                  StringUtils.formatTime(permissionPlayer.getRankExpire(), 3)),
                          StringUtils.formatMessage("%sign_line3")
                              .replace("%group%", permissionPlayer.getGroup().getName())
                              .replace(
                                  "%expire",
                                  StringUtils.formatTime(permissionPlayer.getRankExpire(), 3)),
                          StringUtils.formatMessage("%sign_line4")
                              .replace("%group%", permissionPlayer.getGroup().getName())
                              .replace(
                                  "%expire",
                                  StringUtils.formatTime(permissionPlayer.getRankExpire(), 3))
                        });
              }
            }
          }
        }
      }
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onPlayerQuit(PlayerQuitEvent event) {
    lastMove.remove(event.getPlayer());
  }
}
