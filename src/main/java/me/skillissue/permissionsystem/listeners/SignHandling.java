package me.skillissue.permissionsystem.listeners;

import java.util.ArrayList;
import me.skillissue.permissionsystem.PermissionSystem;
import me.skillissue.permissionsystem.structures.PermissionPlayer;
import me.skillissue.permissionsystem.utils.PlayerStorage;
import me.skillissue.permissionsystem.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

public class SignHandling implements Listener {
  private static final ArrayList<Block> BLOCKS = new ArrayList<>();

  public SignHandling() {
    for (Location location : PermissionSystem.getInstance().sql.getSigns()) {
      if (location.getBlock().getState() instanceof Sign) {
        BLOCKS.add(location.getBlock());
      }
    }
  }

  public static void updateSigns() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      updatePlayersSign(player);
    }
  }

  private static void updatePlayersSign(Player player) {
    PermissionPlayer permissionPlayer = PlayerStorage.getPermissionData(player);
    for (Block block : BLOCKS) {
      if (block.getLocation().distance(player.getLocation()) < 30) {
        player.sendSignChange(
            block.getLocation(),
            new String[] {
              StringUtils.formatMessage("%sign_line1")
                  .replace("%group", permissionPlayer.getGroup().getName())
                  .replace("%expire", StringUtils.formatTime(permissionPlayer.getRankExpire(), 3)),
              StringUtils.formatMessage("%sign_line2")
                  .replace("%group", permissionPlayer.getGroup().getName())
                  .replace("%expire", StringUtils.formatTime(permissionPlayer.getRankExpire(), 3)),
              StringUtils.formatMessage("%sign_line3")
                  .replace("%group", permissionPlayer.getGroup().getName())
                  .replace("%expire", StringUtils.formatTime(permissionPlayer.getRankExpire(), 3)),
              StringUtils.formatMessage("%sign_line4")
                  .replace("%group", permissionPlayer.getGroup().getName())
                  .replace("%expire", StringUtils.formatTime(permissionPlayer.getRankExpire(), 3))
            });
      }
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onSign(SignChangeEvent event) {
    if (event.getLine(0).equalsIgnoreCase("[rank]")
        && event.getPlayer().hasPermission("permissionsystem.sign")) {
      BLOCKS.add(event.getBlock());
      PermissionSystem.getInstance().sql.addSign(event.getBlock().getLocation());
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onBlockBreak(BlockBreakEvent event) {
    if (event.getBlock().getState() instanceof Sign sign
        && sign.getLine(0).equalsIgnoreCase("[rank]")
        && event.getPlayer().hasPermission("permissionsystem.sign")) {
      BLOCKS.remove(event.getBlock());
      PermissionSystem.getInstance().sql.removeSign(event.getBlock().getLocation());
    }
  }
}
