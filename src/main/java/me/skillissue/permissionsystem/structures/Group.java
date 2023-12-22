package me.skillissue.permissionsystem.structures;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import me.skillissue.permissionsystem.PermissionSystem;
import org.bukkit.Bukkit;

public class Group extends PermissionsOwner {
  private int id;
  private String name;
  private String prefix;
  private PreparedStatement updateStatement;

  public Group() {
    super(new ArrayList<>());
  }

  public Group(String name, String prefix) {
    super(new ArrayList<>());
    this.name = name;
    this.prefix = prefix;
    this.id = PermissionSystem.getInstance().sql.addGroup(name, prefix, getPermissions());
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    save();
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
    save();
  }

  // WARNING: DO NOT USE THIS METHOD!!!
  // This method is only used by the database to set the id
  // TODO: Make the code private and Invoke it via the class object
  public void __pls_Dont_Use_It__(int i) {
    this.id = i;
  }

  public int getId() {
    return id;
  }

  public void addPlayer(PermissionPlayer player) {
    if (Bukkit.getPlayer(player.getPlayer().getUniqueId()) != null) {
      player
          .getGroup()
          .removePermissionsGivenByGroup(Bukkit.getPlayer(player.getPlayer().getUniqueId()));
    }
    players.add(player.getPlayer());
    if (Bukkit.getPlayer(player.getPlayer().getUniqueId()) != null) {
      player.getGroup().givePlayerPermissions(Bukkit.getPlayer(player.getPlayer().getUniqueId()));
    }
  }

  public void load(ResultSet resultSet) {
    try {
      this.name = resultSet.getString("name");
      this.prefix = resultSet.getString("prefix");
      updateList(resultSet.getString("permissions"));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  protected void save() {
    try {
      if (updateStatement == null) {
        updateStatement =
            PermissionSystem.getInstance()
                .sql
                .createStatement(
                    "UPDATE `groups` SET name = ?, prefix = ?, permissions = ? WHERE id = ?;");
      }
      updateStatement.setString(1, name);
      updateStatement.setString(2, prefix);
      updateStatement.setString(3, String.join(";", getPermissions()));
      updateStatement.setInt(4, id);
      updateStatement.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
