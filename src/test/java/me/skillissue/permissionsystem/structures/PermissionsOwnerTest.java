package me.skillissue.permissionsystem.structures;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import me.skillissue.permissionsystem.PermissionSystem;
import me.skillissue.permissionsystem.tests.MockPermissionsOwner;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PermissionsOwnerTest {
  ServerMock server;

  @BeforeEach
  void setUp() {
    server = MockBukkit.mock();
    MockBukkit.load(PermissionSystem.class);
  }

  @AfterEach
  void tearDown() {
    PermissionSystem.getInstance().sql.dropTables();
    MockBukkit.unmock();
  }

  @Test
  void giveUserPermissions() {
    MockPermissionsOwner mockPermissionsOwner = new MockPermissionsOwner(new ArrayList<>());
    mockPermissionsOwner.addPermission("test.permission");
    mockPermissionsOwner.addPlayer(server.addPlayer());
    assertTrue(server.getPlayer(0).hasPermission("test.permission"));
  }

  @Test
  void removeUserPermissions() {
    MockPermissionsOwner mockPermissionsOwner = new MockPermissionsOwner(new ArrayList<>());
    mockPermissionsOwner.addPermission("test.permission");
    mockPermissionsOwner.addPlayer(server.addPlayer());
    mockPermissionsOwner.removePlayer(server.getPlayer(0));
    assertFalse(server.getPlayer(0).hasPermission("test.permission"));
  }

  @Test
  void addPermission_doesNotOwnPermission() {
    MockPermissionsOwner mockPermissionsOwner = new MockPermissionsOwner(new ArrayList<>());
    mockPermissionsOwner.addPermission("test.permission");
    assertTrue(Arrays.asList(mockPermissionsOwner.getPermissions()).contains("test.permission"));
  }

  @Test
  void addPermission_ownsPermission() {
    MockPermissionsOwner mockPermissionsOwner = new MockPermissionsOwner(new ArrayList<>());
    mockPermissionsOwner.addPermission("test.permission");
    mockPermissionsOwner.addPermission("test.permission");
    assertEquals(1, mockPermissionsOwner.getPermissions().length);
  }

  @Test
  void givePlayerPermissions_allPermissionsGiven() {
    MockPermissionsOwner mockPermissionsOwner = new MockPermissionsOwner(new ArrayList<>());
    Player player = server.addPlayer();
    mockPermissionsOwner.addPermission("test.permission1");
    mockPermissionsOwner.addPermission("test.permission2");
    mockPermissionsOwner.givePlayerPermissions(player);
    assertTrue(player.hasPermission("test.permission1"));
    assertTrue(player.hasPermission("test.permission2"));
  }

  @Test
  void givePlayerPermissions_noPermissionsGiven() {
    MockPermissionsOwner mockPermissionsOwner = new MockPermissionsOwner(new ArrayList<>());
    Player player = server.addPlayer();
    mockPermissionsOwner.givePlayerPermissions(player);
    assertFalse(player.hasPermission("test.permission1"));
  }

  @Test
  void givePlayerPermissions_somePermissionsGiven() {
    MockPermissionsOwner mockPermissionsOwner = new MockPermissionsOwner(new ArrayList<>());
    Player player = server.addPlayer();
    mockPermissionsOwner.addPermission("test.permission1");
    mockPermissionsOwner.givePlayerPermissions(player);
    assertTrue(player.hasPermission("test.permission1"));
    assertFalse(player.hasPermission("test.permission2"));
  }

  @Test
  void removePermissionsGivenByGroup_removesAllPermissionsOfPlayer() {
    MockPermissionsOwner mockPermissionsOwner = new MockPermissionsOwner(new ArrayList<>());
    Player player = server.addPlayer();
    mockPermissionsOwner.addPermission("test.permission1");
    mockPermissionsOwner.addPermission("test.permission2");
    mockPermissionsOwner.givePlayerPermissions(player);
    mockPermissionsOwner.removePermissionsGivenByGroup(player);
    assertFalse(player.hasPermission("test.permission1"));
    assertFalse(player.hasPermission("test.permission2"));
  }

  @Test
  void removePermissionsGivenByGroup_doesNotRemovePermissionsOfOtherPlayers() {
    MockPermissionsOwner mockPermissionsOwner = new MockPermissionsOwner(new ArrayList<>());
    Player player1 = server.addPlayer();
    Player player2 = server.addPlayer();
    mockPermissionsOwner.addPermission("test.permission");
    mockPermissionsOwner.givePlayerPermissions(player1);
    mockPermissionsOwner.givePlayerPermissions(player2);
    mockPermissionsOwner.removePermissionsGivenByGroup(player1);
    assertTrue(player2.hasPermission("test.permission"));
  }

  @Test
  void removePermissionsGivenByGroup_doesNothingIfPlayerHasNoPermissions() {
    MockPermissionsOwner mockPermissionsOwner = new MockPermissionsOwner(new ArrayList<>());
    Player player = server.addPlayer();
    mockPermissionsOwner.removePermissionsGivenByGroup(player);
    assertFalse(player.hasPermission("test.permission"));
  }

  @Test
  void removePermissionsGivenByGroup_doesNotRemovePermissionsGivenByOtherSources() {
    MockPermissionsOwner mockPermissionsOwner1 = new MockPermissionsOwner(new ArrayList<>());
    MockPermissionsOwner mockPermissionsOwner2 = new MockPermissionsOwner(new ArrayList<>());
    Player player = server.addPlayer();
    mockPermissionsOwner1.addPermission("test.permission");
    mockPermissionsOwner2.addPermission("test.permission");
    mockPermissionsOwner1.givePlayerPermissions(player);
    mockPermissionsOwner2.givePlayerPermissions(player);
    mockPermissionsOwner1.removePermissionsGivenByGroup(player);
    assertTrue(player.hasPermission("test.permission"));
  }
}
