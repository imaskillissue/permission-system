package me.skillissue.permissionsystem;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import me.skillissue.permissionsystem.structures.Group;
import me.skillissue.permissionsystem.utils.GroupStorage;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PermissionSystemTest {
  ServerMock server;

  @BeforeEach
  void setUp() {
    server = MockBukkit.mock();
    MockBukkit.load(PermissionSystem.class);
  }

  @AfterEach
  void tearDown() {
    PermissionSystem.getInstance().sql.dropTable("groups");
    PermissionSystem.getInstance().sql.dropTable("users");
    MockBukkit.unmock();
  }

  @Test
  void playerGotNameUpdate() {
    Player player = server.addPlayer("Test");
    // Check if the player displayName is group.prefix + player.name
    assertEquals(GroupStorage.getGroupById(1).getPrefix()+"Test§r", player.getDisplayName());
  }
}
