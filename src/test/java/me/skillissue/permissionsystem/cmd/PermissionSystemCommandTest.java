package me.skillissue.permissionsystem.cmd;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import me.skillissue.permissionsystem.PermissionSystem;
import me.skillissue.permissionsystem.utils.PermissionUtil;
import me.skillissue.permissionsystem.utils.PlayerStorage;
import me.skillissue.permissionsystem.utils.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PermissionSystemCommandTest {
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
  void runCommand_UserInfo() {
    PlayerMock player = server.addPlayer("ImaSkillissue");
    PermissionUtil.addPermission(player, "permissionsystem");
    server.execute("ps", player, "user", "ImaSkillissue");
    assertEquals(
        (StringUtils.formatMessage("%user_info")
            .replace("%username", player.getName())
            .replace("%group", PlayerStorage.getPermissionData(player).getGroup().getName())
            .replace(
                "%expire",
                StringUtils.formatTime(PlayerStorage.getPermissionData(player).getRankExpire(), 2))
            .replace(
                "%permissions",
                String.join(", ", PlayerStorage.getPermissionData(player).getPermissions()))
            .replaceAll("§[0-9a-fA-F]", "")),
        player.nextMessage().replaceAll("§[0-9a-fA-F]", ""));
  }

  @Test
  void runCommand_WrongArgs() {
    PlayerMock player = server.addPlayer("ImaSkillissue");
    PermissionUtil.addPermission(player, "permissionsystem");
    server.execute("ps", player, "user", "ImaSkillissue", "test", "test");
    assertEquals(
        (StringUtils.formatMessage("%command_invalid_usage")
            .replace("%command", "ps")
            .replaceAll("§[0-9a-fA-F]", "")),
        player.nextMessage().replaceAll("§[0-9a-fA-F]", ""));
  }

  @Test
  void runCommand_NoArgs() {
    PlayerMock player = server.addPlayer("ImaSkillissue");
    PermissionUtil.addPermission(player, "permissionsystem");
    server.execute("ps", player);
    assertEquals(
        (StringUtils.formatMessage("%command_invalid_usage")
            .replace("%command", "ps")
            .replaceAll("§[0-9a-fA-F]", "")),
        player.nextMessage().replaceAll("§[0-9a-fA-F]", ""));
  }

    @Test
    void runCommand_NoPermission() {
        PlayerMock player = server.addPlayer("ImaSkillissue");
        server.execute("ps", player, "user", "ImaSkillissue");
        assertEquals(
                (StringUtils.formatMessage("%no_permission")
                        .replaceAll("§[0-9a-fA-F]", "")),
                player.nextMessage().replaceAll("§[0-9a-fA-F]", ""));
    }


}
