package team133;

import battlecode.common.*;

public class Shares {

  private static final Direction[] atg_dirs = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.NONE, Direction.NORTH_EAST, Direction.SOUTH_WEST, Direction.NORTH_WEST, Direction.SOUTH_EAST};
  private static final Direction[] gta_dirs = atg_dirs;
  private static final Direction[] gtg_dirs = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.NORTH_EAST, Direction.SOUTH_WEST, Direction.NORTH_WEST, Direction.SOUTH_EAST};

  private final RobotController rc;
  private final Senses sense;
  private final Bot bot;
  public Shares(Bot bot, Senses sense) {
    this.rc = bot.rc;
    this.bot = bot;
    this.sense = sense;
  }

  private double fluxNeeded(RobotInfo info) {
    if (info.type == RobotType.TOWER) return 0;
    if (bot.type == RobotType.SCOUT && info.type == RobotType.ARCHON) return 0;
    return Math.min(info.type.maxFlux - info.flux, bot.flux - 5);
  }

  public void go() {
    RobotInfo info;
    double amount;

    if (bot.type == RobotType.ARCHON) {
      if (!bot.fluxMutexAvailable(5)) return;
      for (Direction d : gta_dirs) {
        if (bot.flux < 5) return;
        try {
          MapLocation l = bot.loc.add(d);
          GameObject o = rc.senseObjectAtLocation(l, RobotLevel.IN_AIR);
          if (o instanceof Robot) {
            if (o.getTeam() == bot.team) {
              info = rc.senseRobotInfo((Robot) o);
              amount = fluxNeeded(info);
              if (amount <= 5) continue;
              if (bot.acquireFluxMutex(amount)) {
                rc.transferFlux(info.location, RobotLevel.IN_AIR, amount);
                bot.flux = rc.getFlux();
                break;
              }
            }
          }
        } catch (GameActionException e) {
          e.printStackTrace();
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
        }
      }
      for (Direction d : gtg_dirs) {
        if (bot.flux < 5) return;
        try {
          MapLocation l = bot.loc.add(d);
          GameObject o = rc.senseObjectAtLocation(l, RobotLevel.ON_GROUND);
          if (o instanceof Robot) {
            if (o.getTeam() == bot.team) {
              info = rc.senseRobotInfo((Robot) o);
              amount = fluxNeeded(info);
              if (amount <= 5) continue;
              if (bot.acquireFluxMutex(amount)) {
                rc.transferFlux(info.location, RobotLevel.ON_GROUND, amount);
                bot.flux = rc.getFlux();
                break;
              }
            }
          }
        } catch (GameActionException e) {
          e.printStackTrace();
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
        }
      }
    } else if (bot.type == RobotType.SCOUT) {
      //Core.debug_beginProfile(); // TODO: figure out why this is so expensive
      if (!bot.fluxMutexAvailable(5)) return;
      for (Direction d : atg_dirs) {
        if (bot.flux < 5) return;
        try {
          MapLocation l = bot.loc.add(d);
          GameObject o = rc.senseObjectAtLocation(l, RobotLevel.ON_GROUND);
          if (o instanceof Robot) {
            if (o.getTeam() == bot.team) {
              info = rc.senseRobotInfo((Robot) o);
              amount = fluxNeeded(info);
              if (amount <= 5) continue;
              if (bot.acquireFluxMutex(amount)) {
                rc.transferFlux(info.location, RobotLevel.ON_GROUND, amount);
                bot.flux = rc.getFlux();
                break;
              }
            }
          }
        } catch (GameActionException e) {
          e.printStackTrace();
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
        }
      }
      //Core.debug_endProfile();
    }
  }

}
