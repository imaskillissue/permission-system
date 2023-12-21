package me.skillissue.permissionsystem.utils;

import me.skillissue.permissionsystem.PermissionSystem;
import me.skillissue.permissionsystem.structures.Group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GroupStorage {
  private static final HashMap<Integer, Group> GROUPS = new HashMap<>();

  static {
    for (Group group : PermissionSystem.getInstance().sql.getGroups()) {
      GROUPS.put(group.getId(), group);
    }
    if (GROUPS.isEmpty()) {
      Group group = new Group("default", "§7");
      GROUPS.put(group.getId(), group);
    }
  }

  public static Group getGroupByName(String name) {
    try {
      return (Group)
          GROUPS.values().stream().filter(group -> group.getName().equalsIgnoreCase(name)).toArray()[0];
    } catch (ClassCastException ex) {
      return null;
    }
  }

  public static Group getGroupById(int id) {
    return GROUPS.get(id);
  }
}
