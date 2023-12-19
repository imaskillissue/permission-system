package me.skillissue.permissionsystem.structures;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import me.skillissue.permissionsystem.PermissionSystem;
import me.skillissue.permissionsystem.tests.MockPermissionsOwner;
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
}
