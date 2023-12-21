package me.skillissue.permissionsystem.listeners;

import me.skillissue.permissionsystem.structures.PermissionPlayer;
import me.skillissue.permissionsystem.utils.PlayerStorage;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class MovementListener implements Listener {
  private final HashMap<Player, Long> lastMove = new HashMap<>();

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    if (!lastMove.containsKey(event.getPlayer())
        || System.currentTimeMillis() - lastMove.get(event.getPlayer()) > 1000) {
      lastMove.put(event.getPlayer(), System.currentTimeMillis());
      for (int x = 0; x < 10; x++) {
        for (int y = 0; y < 10; y++) {
          for (int z = 0; z < 10; z++) {
            if (event.getPlayer().getLocation().add(x, y, z).getBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getPlayer().getLocation().add(x, y, z).getBlock().getState();
                if (sign.getLine(0).equalsIgnoreCase("[Permissions]")) {
                  PermissionPlayer permissionPlayer = PlayerStorage.getPermissionData(event.getPlayer());
                  event.getPlayer().sendSignChange(event.getPlayer().getLocation().add(x, y, z), new String[]{"[Permissions]", "Group: "+permissionPlayer.getGroup().getName(), "Until: ", "Suffix: "+sign.getLine(3)});
                }
            }
          }
        }
      }
    }
  }
}
