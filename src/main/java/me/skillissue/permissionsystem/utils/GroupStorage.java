package me.skillissue.permissionsystem.utils;

import me.skillissue.permissionsystem.PermissionSystem;
import me.skillissue.permissionsystem.structures.Group;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupStorage {
  private static final ArrayList<Group> GROUPS = new ArrayList<>();

  static {
    GROUPS.addAll(Arrays.stream(PermissionSystem.getInstance().sql.getGroups()).toList());
    if (GROUPS.isEmpty()) {
      GROUPS.add(new Group("default", "§7"));
    }
  }

  public static Group getGroupByName(String name) {
    try {
      return (Group)
          GROUPS.stream().filter(group -> group.getName().equalsIgnoreCase(name)).toArray()[0];
    } catch (ClassCastException ex) {
      return null;
    }
  }

  public static Group getGroupById(int id) {
    return GROUPS.get(id);
  }
}
