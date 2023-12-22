package me.skillissue.permissionsystem.sql;

import static org.junit.jupiter.api.Assertions.*;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import java.security.InvalidParameterException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.skillissue.permissionsystem.PermissionSystem;
import me.skillissue.permissionsystem.structures.PermissionPlayer;
import me.skillissue.permissionsystem.utils.GroupStorage;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.*;

class SqlConnectionTest {
  static SqlConnection sqlConnection;
  static PreparedStatement recreateTableUser;
  static PreparedStatement recreateTableGroups;
  static PreparedStatement getPlayerByUUID;
  static ServerMock server;

  @BeforeAll
  static void setUp() {
    server = MockBukkit.mock();
    MockBukkit.load(PermissionSystem.class);
    sqlConnection = PermissionSystem.getInstance().sql;
    recreateTableUser =
        sqlConnection.createStatement(
            "CREATE TABLE IF NOT EXISTS users ("
                + "uuid VARCHAR(36) NOT NULL, "
                + "usergroup INT NOT NULL , "
                + "rankexpire BIGINT NOT NULL , "
                + "permissions TEXT NOT NULL);");
    recreateTableGroups =
        sqlConnection.createStatement(
            "CREATE TABLE IF NOT EXISTS groups ("
                + "id INT AUTO_INCREMENT primary key NOT NULL, "
                + "name VARCHAR(64) NOT NULL ,"
                + "prefix VARCHAR(16),"
                + "permissions TEXT NOT NULL);");
    getPlayerByUUID = sqlConnection.createStatement("SELECT * FROM users WHERE uuid = ?");
  }

  @AfterAll
  static void tearDown() {
    sqlConnection.close();
    MockBukkit.unmock();
  }

  @AfterEach
  void tearDownEach() {
    sqlConnection.dropTables();
    try {
      recreateTableUser.execute();
      recreateTableGroups.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void connect() {
    assertTrue(sqlConnection.isConnected());
  }

  @Test
  void addUser_Normal() {
    try {
      PlayerMock player = server.addPlayer();
      sqlConnection.addUser(new PermissionPlayer(player));
      getPlayerByUUID.setString(1, player.getUniqueId().toString());
      assertTrue(getPlayerByUUID.executeQuery().next());
      player.disconnect();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void addUser_NullPlayer() {
    assertThrows(InvalidParameterException.class, () -> sqlConnection.addUser(null));
  }

  @Test
  void addUser_SavesCorrectly() {
    try {
      Player player = server.addPlayer();
      PermissionPlayer permissionPlayer = new PermissionPlayer(player);
      sqlConnection.addUser(permissionPlayer);
      permissionPlayer.setGroup(
          GroupStorage.getGroupById(1), System.currentTimeMillis() + 1000 * 20);
      getPlayerByUUID.setString(1, player.getUniqueId().toString());
      ResultSet resultSet = getPlayerByUUID.executeQuery();
      resultSet.next();
      assertEquals(permissionPlayer.getRankExpire(), resultSet.getLong("rankexpire"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
