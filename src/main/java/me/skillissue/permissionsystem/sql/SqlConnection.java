package me.skillissue.permissionsystem.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;
import me.skillissue.permissionsystem.structures.Group;
import me.skillissue.permissionsystem.structures.PermissionPlayer;
import me.skillissue.permissionsystem.utils.FileConfigField;
import me.skillissue.permissionsystem.utils.FileConfigUtil;
import org.bukkit.Bukkit;

public class SqlConnection {
  private static PreparedStatement addUser;
  private static PreparedStatement getUser;
  private static PreparedStatement dropTables;
  private static PreparedStatement addGroup;
  @FileConfigField private final String host;
  @FileConfigField private final String port;
  @FileConfigField private final String database;
  @FileConfigField private final String username;
  @FileConfigField private final String password;
  private Connection connection;

  public SqlConnection() {
    this.host = "localhost";
    this.port = "3306";
    this.database = "permissionsystem";
    this.username = "root";
    this.password = "password";
    File file = new File("plugins/PermissionSystem/MySQL.yml");
    if (file.exists()) {
      FileConfigUtil.loadConfig(this, file);
    } else {
      FileConfigUtil.saveObjectToConfig(this, file);
    }
    connect();
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
    HikariDataSource dataSource = new HikariDataSource(config);
    try {
      connection = dataSource.getConnection();
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
      getUser = connection.prepareStatement("SELECT * FROM `users` where uuid = ?;");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public boolean isConnected() {
    try {
      return connection.isValid(1000);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public void dropTables() {
    try {
      if (dropTables == null) {
        dropTables = connection.prepareStatement("DROP TABLE users, `groups`;");
      }
      dropTables.execute();
    } catch (Exception e) {
      e.printStackTrace();
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

  public PermissionPlayer getPlayer(UUID uuid) {
    PermissionPlayer permissionPlayer = new PermissionPlayer(Bukkit.getOfflinePlayer(uuid));
    try {
      getUser.setString(1, uuid.toString());
      ResultSet resultSet = getUser.executeQuery();
      if (!resultSet.next()) {
        addUser(permissionPlayer);
        return permissionPlayer;
      }
      permissionPlayer.updateList(resultSet.getString("permissions"));
      permissionPlayer.setGroup(
          getGroup(resultSet.getInt("usergroup")), resultSet.getLong("rankexpire"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (permissionPlayer.getGroup() == null) {
      permissionPlayer.setGroup(getGroup(1));
    }
    return permissionPlayer;
  }

  private boolean existPlayer(String uuid) {
    try {
      getUser.setString(1, uuid);
      ResultSet resultSet = getUser.executeQuery();
      return resultSet.next();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public Group getGroup(int id) {
    try {
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
      group.updateList(resultSet.getString("permissions"));
      return group;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean existGroup(int id) {
    try {
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
    try {
      if (addGroup == null) {
        addGroup =
            connection.prepareStatement(
                "INSERT INTO groups (name, prefix, permissions) VALUES (?, ?, ?);",
                Statement.RETURN_GENERATED_KEYS);
      }
      addGroup.setString(1, name);
      addGroup.setString(2, prefix);
      addGroup.setString(3, String.join(";", permissions));
      addGroup.execute();
      ResultSet resultSet = addGroup.getGeneratedKeys();
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
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM groups");
      ResultSet resultSet = preparedStatement.executeQuery();
      ArrayList<Group> groups = new ArrayList<>();
      while (resultSet.next()) {
        Group group = new Group();
        group.setName(resultSet.getString("name"));
        group.setPrefix(resultSet.getString("prefix"));
        group.__pls_Dont_Use_It__(resultSet.getInt("id"));
        group.updateList(resultSet.getString("permissions"));
        groups.add(group);
      }
      return groups.toArray(new Group[0]);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public PreparedStatement createStatement(String sql) {
    try {
      return connection.prepareStatement(sql);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void close() {
    try {
      connection.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
