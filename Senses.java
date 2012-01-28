package team133;

import battlecode.common.*;

public class Senses {

  private final RobotController rc;
  private final Bot bot;
  public Senses(Bot bot) {
    this.rc = bot.rc;
    this.bot = bot;
    this.move_cost = rc.getType().moveCost + GameConstants.UNIT_UPKEEP * rc.getType().moveDelayOrthogonal;
    this.core = rc.sensePowerCore();
  }

  public final PowerNode core;

  public Robot leader = null;
  public MapLocation leader_location = null;
  
  public MapLocation bound_left = null;
  public MapLocation bound_up = null;
  public MapLocation bound_right = null;
  public MapLocation bound_down = null;

  private final int MAX = 30;

  public int robots_count = 0;
  public Robot[] robots = null;

  public int nodes_count = 0;
  public PowerNode[] nodes = null;

  public int all_info_count = 0;
  public final RobotInfo[] all_info = new RobotInfo[MAX];

  public int allied_info_count = 0;
  public final RobotInfo[] allied_info = new RobotInfo[MAX];

  public int allied_soldiers_count = 0;
  public final RobotInfo[] allied_soldiers = new RobotInfo[MAX];

  public int allied_scouts_count = 0;
  public final RobotInfo[] allied_scouts = new RobotInfo[MAX];

  public int allied_scorchers_count = 0;
  public final RobotInfo[] allied_scorchers = new RobotInfo[MAX];

  public int allied_archons_count = 0;
  public final RobotInfo[] allied_archons = new RobotInfo[MAX];

  public int combat_allies_count = 0;
  public final RobotInfo[] combat_allies = new RobotInfo[MAX];

  public int allied_nodes_count = 0;
  public PowerNode[] allied_nodes = null;

  public int archon_locations_count = 0;
  public MapLocation[] archon_locations = null;

  public int enemy_info_count = 0;
  public final RobotInfo[] enemy_info = new RobotInfo[MAX];

  public int enemy_soldiers_count = 0;
  public final RobotInfo[] enemy_soldiers = new RobotInfo[MAX];

  public int enemy_scorchers_count = 0;
  public final RobotInfo[] enemy_scorchers = new RobotInfo[MAX];

  public int enemy_archons_count = 0;
  public final RobotInfo[] enemy_archons = new RobotInfo[MAX];

  public int enemy_towers_count = 0;
  public final RobotInfo[] enemy_towers = new RobotInfo[MAX];

  public int enemy_disrupters_count = 0;
  public final RobotInfo[] enemy_disrupters = new RobotInfo[MAX];

  public int combat_enemies_count = 0;
  public final RobotInfo[] combat_enemies = new RobotInfo[MAX];

  public boolean isNodeThere(MapLocation loc) {
    for (MapLocation node : rc.senseCapturablePowerNodes()) {
      if (loc.equals(node)) return true; 
    }
    return false;
  }

  public MapLocation computeClosestArchon() {
    if (archon_locations_count > 1) {
      MapLocation closest = archon_locations[0];
      int dist = bot.distanceSquaredTo(closest);
      for (int i = 1; i < archon_locations_count; i++) {
        int better = bot.distanceSquaredTo(archon_locations[i]);
        if (better < dist) {
          closest = archon_locations[i];
          dist = better;
        }
      }
      return closest;
    }
    return null;
  }

  public MapLocation computeClosestCapturableNode() {
    MapLocation closest = null;
    int dist = 10000;
    for (MapLocation l : rc.senseCapturablePowerNodes()){
      int better = bot.distanceSquaredTo(l);
      if (better < dist) {
        closest = l;
        dist = better;
      }
    }
    return closest;
  }

  public void test_right_bound(MapLocation loc) {
    TerrainTile t = rc.senseTerrainTile(loc);
    if (t == TerrainTile.OFF_MAP || t == null) {
      if (bound_right == null) {
        bound_right = loc;
      } else {
        if (bound_right.x > loc.x) {
          bound_right = loc;
        }
      }
    }
  }

  public void test_left_bound(MapLocation loc) {
    TerrainTile t = rc.senseTerrainTile(loc);
    if (t == TerrainTile.OFF_MAP || t == null) {
      if (bound_left == null) {
        bound_left = loc;
      } else {
        if (bound_left.x < loc.x) {
          bound_left = loc;
        }
      }
    }
  }

  public void test_up_bound(MapLocation loc) {
    TerrainTile t = rc.senseTerrainTile(loc);
    if (t == TerrainTile.OFF_MAP || t == null) {
      if (bound_up == null) {
        bound_up = loc;
      } else {
        if (bound_up.y < loc.y) {
          bound_up = loc;
        }
      }
    }
  }
  
  public void test_down_bound(MapLocation loc) {
    TerrainTile t = rc.senseTerrainTile(loc);
    if (t == TerrainTile.OFF_MAP || t == null) {
      if (bound_down == null) {
        bound_down = loc;
      } else {
        if (bound_down.y > loc.y) {
          bound_down = loc;
        }
      }
    }
  }
  
  public void checkBounds() {
    if (bound_up != null && bound_down != null && bound_right != null && bound_left != null) {
      return;
    }
    switch (rc.getType()) {
      case ARCHON:
        test_right_bound(new MapLocation(bot.loc.x + 6, bot.loc.y));
        test_left_bound(new MapLocation(bot.loc.x - 6, bot.loc.y));
        test_up_bound(new MapLocation(bot.loc.x, bot.loc.y - 6));
        test_down_bound(new MapLocation(bot.loc.x, bot.loc.y + 6));
        break;
      case SCOUT:
        test_right_bound(new MapLocation(bot.loc.x + 5, bot.loc.y));
        test_left_bound(new MapLocation(bot.loc.x - 5, bot.loc.y));
        test_up_bound(new MapLocation(bot.loc.x, bot.loc.y - 5));
        test_down_bound(new MapLocation(bot.loc.x, bot.loc.y + 5));
        break;
      case SOLDIER:
        switch (bot.dir) {
          case NORTH:
            test_right_bound(new MapLocation(bot.loc.x + 3, bot.loc.y));
            test_up_bound(new MapLocation(bot.loc.x, bot.loc.y - 3));
            test_left_bound(new MapLocation(bot.loc.x - 3, bot.loc.y));
            break;
          case NORTH_EAST:
            test_right_bound(new MapLocation(bot.loc.x + 3, bot.loc.y));
            test_up_bound(new MapLocation(bot.loc.x, bot.loc.y - 3));
            break;
          case EAST:
            test_right_bound(new MapLocation(bot.loc.x + 3, bot.loc.y));
            test_up_bound(new MapLocation(bot.loc.x, bot.loc.y - 3));
            test_down_bound(new MapLocation(bot.loc.x, bot.loc.y + 3));
            break;
          case SOUTH_EAST:
            test_right_bound(new MapLocation(bot.loc.x + 3, bot.loc.y));
            test_down_bound(new MapLocation(bot.loc.x, bot.loc.y + 3));
            break;
          case SOUTH:
            test_right_bound(new MapLocation(bot.loc.x + 3, bot.loc.y));
            test_down_bound(new MapLocation(bot.loc.x, bot.loc.y + 3));
            test_left_bound(new MapLocation(bot.loc.x - 3, bot.loc.y));
            break;
          case SOUTH_WEST:
            test_down_bound(new MapLocation(bot.loc.x, bot.loc.y + 3));
            test_left_bound(new MapLocation(bot.loc.x - 3, bot.loc.y));
            break;
          case WEST:
            test_down_bound(new MapLocation(bot.loc.x, bot.loc.y + 3));
            test_up_bound(new MapLocation(bot.loc.x, bot.loc.y - 3));
            test_left_bound(new MapLocation(bot.loc.x - 3, bot.loc.y));
            break;
          case NORTH_WEST:
            test_up_bound(new MapLocation(bot.loc.x, bot.loc.y - 3));
            test_left_bound(new MapLocation(bot.loc.x - 3, bot.loc.y));
            break;
        }
        break;
    }

    //rc.setIndicatorString(2, "bounds| u:" + bound_up + ", d:" + bound_down + ", l:" + bound_left + ", r:" + bound_right);
  }

  private final double move_cost;
  public boolean needFlux() {
    if (archon_locations_count == 0) return false;
    MapLocation archon = archon_locations[0];
    int dist = bot.distanceSquaredTo(archon);
    double range = rc.getFlux() / move_cost;
    return range * range < dist;
  }

  public void refresh() {
    robots = rc.senseNearbyGameObjects(Robot.class);
    nodes = rc.senseNearbyGameObjects(PowerNode.class);

    // Nodes
    allied_nodes = rc.senseAlliedPowerNodes();
    allied_nodes_count = allied_nodes.length;

    archon_locations = rc.senseAlliedArchons();
    archon_locations_count = archon_locations.length;

    // Reset counts
    robots_count = nodes_count = all_info_count = allied_info_count = enemy_info_count = allied_soldiers_count = enemy_soldiers_count = allied_scouts_count = allied_scorchers_count = enemy_scorchers_count = allied_archons_count = enemy_archons_count = enemy_towers_count = enemy_disrupters_count = combat_allies_count = combat_enemies_count = 0;

    // Robots
    int ii = Math.min(robots.length, MAX);
    RobotInfo info = null;
    for (int i = 0; i < ii; i++) {
      try {
        info = rc.senseRobotInfo(robots[i]);
      } catch (GameActionException e) {
        e.printStackTrace();
        continue;
      }

      all_info[all_info_count++] = info;

      if (robots[i].getTeam() == rc.getTeam()) {
        allied_info[allied_info_count++] = info;
        // Ally
        switch (info.type) {
          case SOLDIER:
            allied_soldiers[allied_soldiers_count++] = info;
            combat_allies[combat_allies_count++] = info;
            break;
          case SCOUT:
            allied_scouts[allied_scouts_count++] = info;
            break;
          case ARCHON:
            allied_archons[allied_archons_count++] = info;
            break;
          case SCORCHER:
            allied_scorchers[allied_scorchers_count++] = info;
            combat_allies[combat_allies_count++] = info;
            break;
          case TOWER:
            break;
          case DISRUPTER:
            break;
        }
      } else {
        enemy_info[enemy_info_count++] = info;
        // Enemy
        switch (info.type) {
          case SOLDIER:
            enemy_soldiers[enemy_soldiers_count++] = info;
            combat_enemies[combat_enemies_count++] = info;
            break;
          case ARCHON:
            enemy_archons[enemy_archons_count++] = info;
            break;
          case SCORCHER:
            enemy_scorchers[enemy_scorchers_count++] = info;
            combat_enemies[combat_enemies_count++] = info;
            break;
          case TOWER:
            enemy_towers[enemy_towers_count++] = info;
            break;
          case DISRUPTER:
            enemy_disrupters[enemy_disrupters_count++] = info;
            combat_enemies[combat_enemies_count++] = info;
            break;
        }
      }
    }
  }

}
