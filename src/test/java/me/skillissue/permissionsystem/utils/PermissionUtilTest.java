package me.skillissue.permissionsystem.utils;

import static org.junit.jupiter.api.Assertions.*;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import me.skillissue.permissionsystem.PermissionSystem;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.*;

class PermissionUtilTest {
  static ServerMock server;

  @BeforeAll
  static void setUp() {
    server = MockBukkit.mock();
    MockBukkit.load(PermissionSystem.class);
  }

  @AfterAll
  static void cleanUp() {
    PermissionSystem.getInstance().sql.dropTables();
    MockBukkit.unmock();
  }

  @Test
  void SetPermissionAdding() {
    Player p = server.addPlayer();
    boolean before = p.hasPermission("for.testing");
    PermissionUtil.setPermission(p, "for.testing", true);
    boolean after = p.hasPermission("for.testing");
    assertTrue(!before && after);
  }

  @Test
  void SetPermissionRemoving() {
    Player p = server.addPlayer();
    PermissionUtil.setPermission(p, "for.testing", true);
    boolean before = p.hasPermission("for.testing");
    PermissionUtil.setPermission(p, "for.testing", false);
    boolean after = p.hasPermission("for.testing");
  }
}
