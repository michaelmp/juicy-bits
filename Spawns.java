package team133;

import battlecode.common.*;

public class Spawns {

  private final RobotController rc;
  private final Bot bot;
  private final Moves motor;
  public Spawns(Bot bot, Moves motor) {
    this.rc = bot.rc;
    this.bot = bot;
    this.motor = motor;
  }

  public RobotType target = null;

  public int last_scout = 0;
  public int last_soldier = 0;
  public int last_scorcher = 0;
  public int last_disrupter = 0;

  public boolean canSpawn() {
    if (target == null) return false;
    if (rc.isMovementActive()) return false;
    return true;
  }

  public boolean mustSpawn() {
    return target != null;
  }

  /**
   * Holds the production lock, flux lock, and movement lock.
   */
  public void go() {
    if (!mustSpawn()) return;

    if (bot.productionMutexAvailable() && bot.fluxMutexAvailable(target.spawnCost)) {
      bot.acquireProductionMutex();
      bot.acquireFluxMutex(target.spawnCost);
    } else {
      return;
    }

    critical: {
      if (!canSpawn()) {
        break critical;
      }

      if (target.spawnCost >= bot.flux - 5) {
        return;
      }

      if (target.level == RobotLevel.ON_GROUND) {
        if (!rc.canMove(bot.dir)) {
          motor.location = null;
          motor.direction = bot.dir.rotateRight();
          motor.direction_cushion = 0;
          return;
        }
      } else {
        try {
          if (null != rc.senseObjectAtLocation(bot.loc.add(bot.dir), RobotLevel.IN_AIR)) {
            motor.location = null;
            motor.direction = bot.dir.rotateRight();
            motor.direction_cushion = 0;
            return;
          }
        } catch (GameActionException e) {
          e.printStackTrace();
          break critical;
        }
      }

      try {
        rc.spawn(target);
      } catch (GameActionException e) {
        e.printStackTrace();
      }

      switch (target) {
        case SCOUT: last_scout = Clock.getRoundNum(); break;
        case SOLDIER: last_soldier = Clock.getRoundNum(); break;
        case SCORCHER: last_scorcher = Clock.getRoundNum(); break;
        case DISRUPTER: last_disrupter = Clock.getRoundNum(); break;
      }

      return;
    }
    
    bot.releaseProductionMutex();
  }

}
