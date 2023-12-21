package me.skillissue.permissionsystem.structures;

import java.lang.ref.PhantomReference;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.skillissue.permissionsystem.PermissionSystem;
import me.skillissue.permissionsystem.sql.SqlConnection;
import me.skillissue.permissionsystem.utils.GroupStorage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class PermissionPlayer extends PermissionsOwner {
  private static PreparedStatement updateStatement;
  private static PreparedStatement selectStatement;
  private final OfflinePlayer PLAYER;
  private Group group = GroupStorage.getGroupById(1);
  private long rankExpire = 0L;

  public PermissionPlayer(OfflinePlayer player) {
    super(new ArrayList<>(List.of(player)));
    this.PLAYER = player;
  }

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    setGroup(group, 0L);
  }

  public void setGroup(Group group, long rankExpire) {
    if (this.group != null && Bukkit.getPlayer(this.getPlayer().getUniqueId()) != null) {
      this.group.removePermissionsGivenByGroup(Bukkit.getPlayer(this.getPlayer().getUniqueId()));
    }
    this.group = group;
    this.rankExpire = rankExpire;
    group.addPlayer(this);
    try {
      if (updateStatement == null) {
        updateStatement =
            PermissionSystem.getInstance()
                .sql
                .createStatement("UPDATE users SET usergroup = ?, rankexpire = ? WHERE uuid = ?;");
      }

      updateStatement.setInt(1, group.getId());
      updateStatement.setLong(2, rankExpire);
      updateStatement.setString(3, getPlayer().getUniqueId().toString());
      updateStatement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public long getRankExpire() {
    return rankExpire;
  }

  public void setRankExpire(long rankExpire) {
    this.rankExpire = rankExpire;
  }

  public OfflinePlayer getPlayer() {
    return PLAYER;
  }

  public void reload() {
    try {
      if (PermissionPlayer.selectStatement == null) {
        SqlConnection sql = PermissionSystem.getInstance().sql;
        PermissionPlayer.selectStatement =
            sql.createStatement("SELECT * FROM users WHERE uuid = ?;");
      }
      PermissionPlayer.selectStatement.setString(1, getPlayer().getUniqueId().toString());
      ResultSet resultSet = PermissionPlayer.selectStatement.executeQuery();
      if (resultSet.next()) {
        setGroup(
            GroupStorage.getGroupById(resultSet.getInt("group_id")),
            resultSet.getLong("rankexpire"));
        updateList(resultSet.getString("permissions"));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
