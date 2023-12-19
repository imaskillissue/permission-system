package me.skillissue.permissionsystem.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.skillissue.permissionsystem.structures.Group;
import me.skillissue.permissionsystem.structures.PermissionPlayer;
import me.skillissue.permissionsystem.utils.FileConfigField;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;

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

  public Group getGroup(int id) {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement preparedStatement =
          connection.prepareStatement("SELECT * FROM groups WHERE id = ?");
      preparedStatement.setInt(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (!resultSet.next()) {
        return null;
      }
      Group group = new Group();
      group.setName(resultSet.getString("name"));
      group.setPrefix(resultSet.getString("prefix"));
      group.__pls_Dont_Use_It__(resultSet.getInt("id"));
      group.addPermissions(resultSet.getString("permissions").split(";"));
      return group;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean existGroup(int id) {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement preparedStatement =
          connection.prepareStatement("SELECT * FROM groups WHERE id = ?");
      preparedStatement.setInt(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      return resultSet.next();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public int addGroup(String name, String prefix, String[] permissions) {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement preparedStatement =
          connection.prepareStatement(
              "INSERT INTO groups (name, prefix, permissions) VALUES (?, ?, ?);",
              Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setString(1, name);
      preparedStatement.setString(2, prefix);
      preparedStatement.setString(3, String.join(";", permissions));
      preparedStatement.execute();
      ResultSet resultSet = preparedStatement.getGeneratedKeys();
      if (!resultSet.next()) {
        return -1;
      }
      return resultSet.getInt(1);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return -1;
  }

  public Group[] getGroups() {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM groups");
      ResultSet resultSet = preparedStatement.executeQuery();
      ArrayList<Group> groups = new ArrayList<>();
      while (resultSet.next()) {
        Group group = new Group();
        group.setName(resultSet.getString("name"));
        group.setPrefix(resultSet.getString("prefix"));
        group.__pls_Dont_Use_It__(resultSet.getInt("id"));
        group.addPermissions(resultSet.getString("permissions").split(";"));
        groups.add(group);
      }
      return groups.toArray(new Group[0]);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
