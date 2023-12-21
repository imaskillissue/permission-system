package me.skillissue.permissionsystem.utils;

import me.skillissue.permissionsystem.PermissionSystem;
import me.skillissue.permissionsystem.sql.SqlConnection;
import me.skillissue.permissionsystem.structures.Group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GroupStorage {
  private static PreparedStatement selectStatement;
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
          GROUPS.values().stream()
              .filter(group -> group.getName().equalsIgnoreCase(name))
              .toArray()[0];
    } catch (ClassCastException ex) {
      return null;
    }
  }

  public static Group getGroupById(int id) {
    return GROUPS.get(id);
  }

  public static void reload() {
    try {
      if (selectStatement == null) {
        SqlConnection sql = PermissionSystem.getInstance().sql;
        selectStatement = sql.createStatement("SELECT * FROM `groups`");
      }
      ResultSet resultSet = selectStatement.executeQuery();
      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        if (GROUPS.containsKey(id)) {
          GROUPS.get(id).load(resultSet);
        } else {
          Group group = new Group();
          group.__pls_Dont_Use_It__(id);
          group.setName(resultSet.getString("name"));
          group.setPrefix(resultSet.getString("prefix"));
          group.updateList(resultSet.getString("permissions"));
          GROUPS.put(id, group);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
