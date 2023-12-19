package me.skillissue.permissionsystem.structures;

import me.skillissue.permissionsystem.utils.PermissionUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class PermissionsOwner {
  private final HashMap<String, ArrayList<PermissionAttachment>> ATTACHMENTS = new HashMap<>();
  protected ArrayList<OfflinePlayer> players;

  protected PermissionsOwner(ArrayList<OfflinePlayer> players) {
    this.players = players;
  }

  public void addPermissions(String permission) {
    if (ATTACHMENTS.containsKey(permission)) {
      return;
    }
    ATTACHMENTS.put(permission, new ArrayList<>());
    for (OfflinePlayer player : players) {
      if (player.getPlayer() == null) {
        continue;
      }
      ATTACHMENTS.get(permission).add(PermissionUtil.addPermission(player.getPlayer(), permission));
    }
  }

  public void addPermissions(String[] permissions) {
    for (String permission : permissions) {
      addPermissions(permission);
    }
  }

  public void removePermission(String permission) {
    if (ATTACHMENTS.containsKey(permission)) {
      for (PermissionAttachment permissionAttachment : ATTACHMENTS.get(permission)) {
        permissionAttachment.remove();
      }
      ATTACHMENTS.remove(permission);
    }
  }

  public void removePermissions(String[] permissions) {
    for (String permission : permissions) {
      removePermission(permission);
    }
  }

  public String[] getPermissions() {
    return ATTACHMENTS.keySet().toArray(new String[0]);
  }

  public void updateList(String permissionsData) {
    String[] permissions = permissionsData.split(";");
    String[] toRemove =
        (String[])
            Arrays.stream(ATTACHMENTS.keySet().toArray())
                .filter(o -> !Arrays.stream(permissions).anyMatch(s -> s.equals(o)))
                .toArray();
    removePermissions(toRemove);
    addPermissions(permissions);
  }

  public void cleanPermissions() {

  }
}