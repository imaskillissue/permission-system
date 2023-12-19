package me.skillissue.permissionsystem.structures;

import me.skillissue.permissionsystem.PermissionSystem;
import me.skillissue.permissionsystem.utils.PermissionUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class Group {
  private int id;
  private String name;
  private String prefix;
  private ArrayList<String> permissions;
  private ArrayList<Group> parents;

  public Group(String name, String prefix) {
    this.name = name;
    this.prefix = prefix;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public String[] getPermissions() {
    ArrayList<String> perms = new ArrayList<>(permissions);
    for (Group group : parents) {
      perms.addAll(Arrays.stream(group.getPermissions()).toList());
    }
    return perms.toArray(new String[0]);
  }

  public int getId() {
    return id;
  }

  public void addPermission(String permission) {
    if (!permissions.contains(permission)) permissions.add(permission);
  }

  public void removePermission(String permission) {
    permissions.remove(permission);
  }

  public void givePlayerPermissions(Player player) {
    for (int i = 0; i < permissions.size(); i++) {
      PermissionUtil.addPermission(player, permissions.get(i));
    }
    for (int i = 0; i < parents.size(); i++) {
      parents.get(i).givePlayerPermissions(player);
    }
  }
}
