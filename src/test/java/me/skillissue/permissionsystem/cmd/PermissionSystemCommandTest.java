package me.skillissue.permissionsystem.cmd;

import static org.junit.jupiter.api.Assertions.*;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.skillissue.permissionsystem.PermissionSystem;
import me.skillissue.permissionsystem.utils.StringUtils;
import org.junit.jupiter.api.*;

class PermissionSystemCommandTest {
  static ServerMock server;

  @BeforeAll
  static void setUp() {
    server = MockBukkit.mock();
    MockBukkit.load(PermissionSystem.class);
  }

  @AfterAll
  static void tearDown() {
    PermissionSystem.getInstance().sql.dropTables();
    MockBukkit.unmock();
  }

  // TODO: More tests

  @Test
  void runCommand_NoPermission() {
    PlayerMock player = server.addPlayer("ImaSkillissue");
    server.execute("ps", player, "user", "ImaSkillissue");
    assertEquals(
        (StringUtils.formatMessage("%no_permission").replaceAll("§[0-9a-fA-F]", "")),
        player.nextMessage().replaceAll("§[0-9a-fA-F]", ""));
  }
}
