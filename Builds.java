package team133;

import battlecode.common.*;

public class Builds {

  private final RobotController rc;
  private final Bot bot;
  private final Moves motor;
  public Builds(Bot bot, Moves motor) {
    this.rc = bot.rc;
    this.bot = bot;
    this.motor = motor;
  }

  public MapLocation target_location = null;
  public int target_lock_distance_squared = 2;

  public boolean canBuild() {
    if (rc.isMovementActive() || RobotType.TOWER.spawnCost >= rc.getFlux()) {
      return false;
    }
    if (!bot.loc.isAdjacentTo(target_location)) return false;
    if (bot.dir != bot.loc.directionTo(target_location)) return false;
    return true;
  }

  public boolean mustBuild() {
    if (target_location == null) return false;
    /*
    if (rc.canSenseSquare(target_location)) {
      try {
        GameObject o = rc.senseObjectAtLocation(target_location, RobotType.TOWER.level);
        if (o != null) return false;
      } catch (GameActionException e) {
        e.printStackTrace();
        return false;
      }
    }
    */
    return true;
  }

  /**
   * Holds the production lock and flux lock.
   */
  public void go() {
    if (!mustBuild()) return;


    if (bot.loc.equals(target_location)) {
      motor.avoid_location = bot.loc;
      motor.avoid_location_cushion2 = 1;
    }

    if (bot.productionMutexAvailable() && bot.fluxMutexAvailable(200)) {
      bot.acquireProductionMutex();
      bot.acquireFluxMutex(200);
    } else {
      return;
    }

    critical: {
      if (!canBuild()) {
        if (target_location.distanceSquaredTo(bot.loc) <= target_lock_distance_squared) {
          if (bot.flux > 250) break critical;
          return;
        } else {
          break critical;
        }
      }

      try {
        GameObject o = rc.senseObjectAtLocation(target_location, RobotType.TOWER.level);
        if (o != null) {
          if (o.getTeam() == bot.team) {
            return;
          } else {
            break critical;
          }
        }
      } catch (GameActionException e) {
        e.printStackTrace();
        return;
      }

      try {
        rc.spawn(RobotType.TOWER);
      } catch (GameActionException e) {
        e.printStackTrace();
      }

      return;
    }

    bot.releaseProductionMutex();
    bot.releaseFluxMutex();
  }

}
