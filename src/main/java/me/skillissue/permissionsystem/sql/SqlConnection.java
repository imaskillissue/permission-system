package me.skillissue.permissionsystem.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.skillissue.permissionsystem.structures.PermissionPlayer;
import me.skillissue.permissionsystem.utils.FileConfigField;
import me.skillissue.permissionsystem.utils.PermissionUtil;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.Arrays;

public class SqlConnection {
  @FileConfigField private String host;
  @FileConfigField private String port;
  @FileConfigField private String database;
  @FileConfigField private String username;
  @FileConfigField private String password;
  private HikariDataSource dataSource;

  private PreparedStatement addUser;
  private PreparedStatement getUser;

  public SqlConnection() {
    this.host = "localhost";
    this.port = "3306";
    this.database = "permissionsystem";
    this.username = "root";
    this.password = "password";
  }

  public SqlConnection(String host, int port, String database, String username, String password) {
    this.host = host;
    this.port = Integer.toString(port);
    this.database = database;
    this.username = username;
    this.password = password;
    connect();
  }

  public void connect() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
    config.setUsername("valo");
    config.setPassword("pw");
    dataSource = new HikariDataSource(config);
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement preparedStatement =
          connection.prepareStatement(
              "CREATE TABLE IF NOT EXISTS users ("
                  + "uuid VARCHAR(36) NOT NULL, "
                  + "usergroup INT NOT NULL , "
                  + "rankexpire BIGINT NOT NULL , "
                  + "permissions TEXT NOT NULL);");
      preparedStatement.execute();
      preparedStatement.close();
      preparedStatement =
          connection.prepareStatement(
              "CREATE TABLE IF NOT EXISTS groups ("
                  + "id INT AUTO_INCREMENT primary key NOT NULL, "
                  + "name VARCHAR(64) NOT NULL ,"
                  + "prefix VARCHAR(16),"
                  + "permissions TEXT NOT NULL);");
      preparedStatement.execute();

      addUser =
          connection.prepareStatement(
              "INSERT INTO users (uuid, usergroup, rankexpire, permissions) VALUES (?, ?, ?, ?);");
      getUser = connection.prepareStatement("SELECT * FROM users WHERE uuid = ?");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void addUser(PermissionPlayer player) {
    try {
      addUser.clearParameters();
      addUser.setString(1, player.getPlayer().getUniqueId().toString());
      addUser.setInt(2, player.getGroup().getId());
      addUser.setLong(3, player.getRankExpire());
      addUser.setString(4, String.join(";", player.getPermissions()));
      addUser.execute();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public PermissionPlayer getPlayer(String uuid) {
    PermissionPlayer permissionPlayer = new PermissionPlayer(Bukkit.getOfflinePlayer(uuid));
    try {
      getUser.setString(1, uuid);
      ResultSet resultSet = getUser.executeQuery();
      if (!resultSet.next()) {
        addUser(permissionPlayer);
        return permissionPlayer;
      }
      // TODO CONTINUE HERE
      resultSet.getString("permission");
      resultSet.getLong("rankexpire");

    } catch (Exception e) {
      e.printStackTrace();
    }
    return permissionPlayer;
  }
}
