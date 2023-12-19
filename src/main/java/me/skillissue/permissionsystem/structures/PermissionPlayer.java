package me.skillissue.permissionsystem.structures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.skillissue.permissionsystem.utils.GroupStorage;
import org.bukkit.OfflinePlayer;

public class PermissionPlayer extends PermissionsOwner {
  private final OfflinePlayer PLAYER;
  private Group group = GroupStorage.getGroupById(0);
  private long rankExpire = 0L;

  public PermissionPlayer(OfflinePlayer player) {
    super(new ArrayList<>(List.of(player)));
    this.PLAYER = player;
  }

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
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
}