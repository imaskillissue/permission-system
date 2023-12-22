package me.skillissue.permissionsystem.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.security.InvalidParameterException;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;
import me.skillissue.permissionsystem.structures.Group;
import me.skillissue.permissionsystem.structures.PermissionPlayer;
import me.skillissue.permissionsystem.utils.FileConfigField;
import me.skillissue.permissionsystem.utils.FileConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SqlConnection {
  @FileConfigField private final String host;
  @FileConfigField private final String port;
  @FileConfigField private final String database;
  @FileConfigField private final String username;
  @FileConfigField private final String password;
  private Connection connection;
  private PreparedStatement addUser;
  private PreparedStatement getUser;
  private PreparedStatement dropTables;
  private PreparedStatement addGroup;
  private PreparedStatement addSign;
  private PreparedStatement getSigns;
  private PreparedStatement removeSign;

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
    config.setUsername(username);
    config.setPassword(password);
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
              "CREATE TABLE IF NOT EXISTS `groups` ("
                  + "id INT AUTO_INCREMENT primary key NOT NULL, "
                  + "name VARCHAR(64) NOT NULL ,"
                  + "prefix VARCHAR(16),"
                  + "permissions TEXT NOT NULL);");
      preparedStatement.execute();
      preparedStatement.close();
      preparedStatement =
          connection.prepareStatement(
              "CREATE TABLE IF NOT EXISTS signs ("
                  + "x FLOAT,"
                  + "y FLOAT,"
                  + "z FLOAT,"
                  + "world VARCHAR(64)"
                  + ");");
      preparedStatement.execute();
      preparedStatement.close();

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
    if (player == null) {
      throw new InvalidParameterException("Player can't be null");
    }
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

  public void addSign(Location location) {
    try {
      if (addSign == null) {
        addSign =
            connection.prepareStatement("INSERT INTO signs (x, y, z, world) VALUES (?, ?, ?, ?);");
      }
      addSign.setFloat(1, (float) location.getX());
      addSign.setFloat(2, (float) location.getY());
      addSign.setFloat(3, (float) location.getZ());
      addSign.setString(4, location.getWorld().getName());
      addSign.execute();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Location[] getSigns() {
    try {
      if (getSigns == null) {
        getSigns = connection.prepareStatement("SELECT * FROM signs;");
      }
      ResultSet resultSet = getSigns.executeQuery();
      ArrayList<Location> locations = new ArrayList<>();
      while (resultSet.next()) {
        locations.add(
            new Location(
                Bukkit.getWorld(resultSet.getString("world")),
                resultSet.getFloat("x"),
                resultSet.getFloat("y"),
                resultSet.getFloat("z")));
      }
      return locations.toArray(new Location[0]);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void removeSign(Location location) {
    try {
      if (removeSign == null) {
        removeSign =
            connection.prepareStatement(
                "DELETE FROM signs WHERE x = ? AND y = ? AND z = ? AND world = ?;");
      }
      removeSign.setFloat(1, (float) location.getX());
      removeSign.setFloat(2, (float) location.getY());
      removeSign.setFloat(3, (float) location.getZ());
      removeSign.setString(4, location.getWorld().getName());
      removeSign.execute();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void close() {
    try {
      connection.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
