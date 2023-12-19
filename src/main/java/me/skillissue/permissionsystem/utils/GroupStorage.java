package me.skillissue.permissionsystem.utils;

import me.skillissue.permissionsystem.structures.Group;

import java.util.ArrayList;

public class GroupStorage {
  private static final ArrayList<Group> GROUPS = new ArrayList<>();

  public static Group getGroupByName(String name) {
    try {
      return (Group) GROUPS.stream().filter(group -> group.getName().equalsIgnoreCase(name)).toArray()[0];
    } catch (ClassCastException ex) {
      return null;
    }
  }

  public static Group getGroupById(int id) {
    return GROUPS.get(id);
  }
}
