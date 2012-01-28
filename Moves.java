package team133;

import battlecode.common.*;

public class Moves {

  private final RobotController rc;
  private final Bot bot;
  private final Senses sense;
  public Moves(Bot bot, Senses sense) {
    this.rc = bot.rc;
    this.bot = bot;
    this.sense = sense;
  }

  public MapLocation location = null;
  public int location_cushion2 = 2;
  public MapLocation rescue_location = null;
  public int rescue_location_cushion2 = 2;
  public MapLocation avoid_location = null;
  public int avoid_location_cushion2 = 5;
  public Direction direction = null;
  public int direction_cushion = 0;
  public boolean backstep = false;

  int onedge = 0;
  int tracing_duration = 0;

  public boolean canMove() {
    if (rc.getFlux() < 2 * rc.getType().moveCost) return false;
    return !rc.isMovementActive();
  }

  /**
   * Get to a safe distance, defined by avoid_location_cushion2.
   */
  public boolean mustAvoid() {
    if (avoid_location == null) return false;
    if (bot.distanceSquaredTo(avoid_location) >= avoid_location_cushion2) return false;
    return true;
  }

  /**
   * Get close to the target, as close as location_cushion2.
   */
  public boolean mustMove() {
    if (location == null) return false;
    if (location.distanceSquaredTo(bot.loc) <= location_cushion2) return false;
    return true;
  }

  /**
   * When not moving, face this direction or as close to this direction as
   * direction_cushion.
   */
  public boolean mustTurn() {
    if (direction == null) return false;
    if (Core.dirDiff(direction, bot.dir) <= direction_cushion) return false;
    return true;
  }

  /**
   * Holds movement lock if an unsatisfied objective remains.
   */
  public void go() {
    if (!(mustAvoid() || mustMove() || mustTurn())) return;
    if (!bot.acquireMovementMutex()) return;

    sense.checkBounds();

    /*
    if (sense.bound_left != null && bot.loc.x <= sense.bound_left.x + 2) {
      onedge++;
    } else if (sense.bound_right != null && bot.loc.x >= sense.bound_right.x - 2) {
      onedge++;
    } else if (sense.bound_up != null && bot.loc.y <= sense.bound_up.y + 2) {
      onedge++;
    } else if (sense.bound_down != null && bot.loc.y >= sense.bound_down.y - 2) {
      onedge++;
    } else {
      onedge = 0;
    }

    if (onedge > 50) {
      tracing = false;
      tracing_duration = 0;
      cw = !cw;
    }*/

    /*
    if (tracing_duration > 150) {
      tracing = false;
      tracing_duration = 0;
    }
    */

    critical: {
      if (!canMove()) return;

      if (mustAvoid()) {
        if (flee(avoid_location)) {
          if (bot.type == RobotType.ARCHON) tracing = false;
          backstep = false;
          return;
        }
      }

      if (mustMove()) {
        debug_displayDestination();
        if (path(location)) {
          backstep = false;
          return;
        }
      } else if (mustTurn()) {
        if (face(direction)) return;
      }

      return;
    }
  }

  private void debug_displayDestination() {
    rc.setIndicatorString(1, "destination: " + (location.x - bot.loc.x) + "," + (location.y - bot.loc.y));
  }

  /**
   * @return true iff robot moves this turn
   */
  public boolean face(MapLocation loc) {
    return face(bot.loc.directionTo(loc));
  }

  /**
   * @return true iff robot moves this turn
   */
  public boolean face(Direction dir) {
    if (!canMove())  return false;
    if (dir == Direction.OMNI || dir == Direction.NONE) return false;
    if (dir == bot.dir) return false;
    try {
      rc.setDirection(dir);
    } catch (GameActionException e) {
      e.printStackTrace();
    }
    return true;
  }

  /**
   * @return true iff robot moves this turn
   */
  public boolean step() {
    if (!canMove()) return false;
    if (!rc.canMove(bot.dir)) return false;
    try {
      rc.moveForward();
    } catch (GameActionException e) {
      e.printStackTrace();
    }
    return true;
  }

  /**
   * @return true iff robot moves this turn
   */
  public boolean backStep() {
    if (!canMove()) return false;
    if (!rc.canMove(bot.dir.opposite())) return false;
    try {
      rc.moveBackward();
    } catch (GameActionException e) {
      e.printStackTrace();
    }
    return true;
  }

  /**
   * @return true iff robot moves this turn
   */
  public boolean step(Direction dir) {
    if (!canMove()) return false;
    Direction[] dirs = {dir, dir.rotateLeft(), dir.rotateRight()};
    choose: {
      for (Direction d : dirs) {
        if (rc.canMove(d)) {
          dir = d;
          break choose;
        }
      }
      return false;
    }
    if (backstep) {
      if (bot.dir.opposite() != dir) {
        return face(dir.opposite());
      } else {
        return backStep();
      }
    } else {
      if (bot.dir != dir) {
        return face(dir);
      } else {
        return step();
      }
    }
  }

  // lower rank must trace around higher rank
  private int move_rank(RobotType type) {
    switch (type) {
      case TOWER: return 5;
      case ARCHON: return 4;
      case SCORCHER: return 3;
      case DISRUPTER: return 2;
      case SOLDIER: return 1;
      case SCOUT: return 0;
    }
    return 0;
  }

  public boolean canTrace(Direction dir) {
    if (rc.canMove(dir)) return true;
    if (!rc.canSenseSquare(bot.loc.add(dir))) return true;
    if (rc.senseTerrainTile(bot.loc.add(dir)) != TerrainTile.LAND) return false;
    try {
      GameObject obstruction = rc.senseObjectAtLocation(bot.loc.add(dir), RobotLevel.ON_GROUND);
      if (obstruction.getTeam() != rc.getRobot().getTeam()) return true;
      if (obstruction instanceof Robot) {
        RobotInfo info = rc.senseRobotInfo((Robot) obstruction);
        if (Core.dirDiff(info.direction, info.location.directionTo(bot.loc)) <= 1) {
          return move_rank(info.type) > move_rank(rc.getType());
        }
      } else {
        // ???
      }
    } catch (GameActionException e) {
    }
    return false;
  }

  private int heuristic(MapLocation loc) {
    if (location == null) return 0;
    return loc.distanceSquaredTo(location);
  }

  /**
   * TODO
   * @return true iff robot moves this turn
   */
  public boolean tracing = false;
  private int winding = 0;
  private boolean cw = true;
  public boolean bug(Direction dir) {
    if (dir == Direction.OMNI || dir == Direction.NONE) return false;
    if (tracing) {
      tracing_duration++;

      // Wall follow.
      Direction actual = bot.dir;
      
      if (!canTrace(actual) && !(cw ? (canTrace(actual.rotateLeft())) : (canTrace(actual.rotateRight())))) {
        if (cw) {
          for (int tries = 0; tries < 8 && !canTrace(actual); tries++) {
            actual = actual.rotateRight();
            winding++;
          }
        } else {
          for (int tries = 0; tries < 8 && !canTrace(actual); tries++) {
            actual = actual.rotateLeft();
            winding++;
          }
        }
      } else {
        if (cw) {
          for (int tries = 0; tries < 8 && canTrace(actual.rotateLeft()); tries++) {
            actual = actual.rotateLeft();
            winding--;
          }
        } else {
          for (int tries = 0; tries < 8 && canTrace(actual.rotateRight()); tries++) {
            actual = actual.rotateRight();
            winding--;
          }
        }
      }   

      //rc.setIndicatorString(2, Clock.getRoundNum() + ": " + winding + " d:" + dir.toString() + " a:" + actual.toString());

      if (bot.dir != actual) {
        return face(actual);
      } else {
        if (winding <= 0) {
          tracing = false;
          tracing_duration = 0;
        }
        return step(actual);
      }
    } else {
      // Dead reckon.
      
      if (rc.canMove(dir) && rc.canMove(dir)) return step(dir);
      if (rc.getType().isAirborne()) return step(dir);

      if (dir == Direction.OMNI || dir == Direction.NONE) return false;
      Direction[] dirs = {dir, dir.rotateLeft(), dir.rotateRight()};

      boolean ok = false;
      for (int i = 0; i < 3; i++) {
        if (dirs[i] == dir) ok = true;
      }
      if (!ok) {
        tracing = true;
        tracing_duration = 0;
        winding = Core.dirDiff(bot.dir, dir);
        //int a = heuristic(bot.loc.add(dir.rotateLeft().rotateLeft()));
        //int b = heuristic(bot.loc.add(dir.rotateRight().rotateRight()));
        //if (a == b) {
          cw = !cw;
        //} else {
        //  cw = a > b;
        //}
        return bug(dir);
      }

      trace: {
        for (int i = 0; i < 3; i++) {
          if (rc.canMove(dirs[i])) {
            dir = dirs[i];
            break trace;
          }
        }
        tracing = true;
        tracing_duration = 0;
        winding = Core.dirDiff(bot.dir, dir);
        //int a = heuristic(bot.loc.add(dir.rotateLeft().rotateLeft()));
        //int b = heuristic(bot.loc.add(dir.rotateRight().rotateRight()));
        //if (a == b) {
          cw = !cw;
        //} else {
        //  cw = a > b;
        //}
        return bug(dir);
      }

      //rc.setIndicatorString(2, "coastin'");

      /*if (bot.dir != dir) {
        return face(dir);
      } else {*/
        return step(dir);
      //}
    }
  }

  /**
   * @return true iff robot moves this turn
   */
  public boolean path(MapLocation loc) {
    return bug(pathNextDir(loc));
  }

  /**
   * TODO
   */
  public Direction pathNextDir(MapLocation loc) {
    return bot.loc.directionTo(loc);
  }

  /**
   * @return true iff robot moves this turn
   */
  public boolean flee(MapLocation loc) {
    if (rescue_location != null && rescue_location.distanceSquaredTo(loc) >= avoid_location_cushion2) {
      return path(rescue_location);
    } else {
      Direction dir = loc.directionTo(bot.loc);
      if (dir == Direction.NONE || dir == Direction.OMNI) {
        return bug(Direction.NORTH);
      } else {
        return bug(loc.directionTo(bot.loc));
      }
    }
  }

}
