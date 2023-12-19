package me.skillissue.permissionsystem.structures;

import me.skillissue.permissionsystem.PermissionSystem;
import me.skillissue.permissionsystem.sql.SqlConnection;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.Arrays;

public class Group extends PermissionsOwner {
  private int id;
  private String name;
  private String prefix;

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
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  // WARNING: DO NOT USE THIS METHOD!!!
  // This method is only used by the database to set the id
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
}
