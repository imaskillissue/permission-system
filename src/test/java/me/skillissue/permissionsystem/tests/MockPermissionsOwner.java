package me.skillissue.permissionsystem.tests;

import me.skillissue.permissionsystem.structures.PermissionsOwner;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MockPermissionsOwner extends PermissionsOwner {
  public MockPermissionsOwner(ArrayList<OfflinePlayer> players) {
    super(players);
  }

  public void addPlayer(Player player) {
    givePlayerPermissions(player);
    players.add(player);
  }

  public void removePlayer(Player player) {
    removePermissionsGivenByGroup(player);
    players.remove(player);
  }
}
