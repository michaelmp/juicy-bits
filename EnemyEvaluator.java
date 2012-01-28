package team133;

import battlecode.common.*;

public class EnemyEvaluator implements TargetEvaluator {

  private final RobotController rc;
  private final Bot bot;
  private final Senses sense;
  public EnemyEvaluator(Bot bot, Senses sense) {
    this.rc = bot.rc;
    this.bot = bot;
    this.sense = sense;
  }

  private double evaluate(RobotInfo info) {
    return bot.distanceSquaredTo(info.location);
  }

  private RobotInfo target = null;
  public boolean scan(RobotInfo[] objects, int limit) {
    RobotInfo info = null;
    RobotInfo mediocre = null;
    RobotInfo worst = null;
    target = null;
    double target_evaluation = 0;
    double testval = 0;

    for (int i = 0; i < limit; i++) {
      info = objects[i];
      if (info.type == RobotType.TOWER) {
        if (bot.type == RobotType.SCOUT) continue;
        /*
        if (!sense.isVulnerableEnemyTower(info)) {
          continue;
        }*/
      }
      if (bot.type == RobotType.SCOUT) {
        if (info.flux < GameConstants.UNIT_UPKEEP) continue;
      }
      if (rc.canAttackSquare(info.location)) {
        if (info.type == RobotType.TOWER) {
          worst = info;
          continue;
        } if (info.type == RobotType.SCOUT) {
          mediocre = info;
          continue;
        } else {
          target = info;
          return true;
        }
      }
      if (target == null || (testval = evaluate(info)) > target_evaluation) {
        target = info;
        target_evaluation = testval;
      }
    }
    if (target == null) {
      if (mediocre == null) {
        if (worst != null) {
          try {
            GameObject node = rc.senseObjectAtLocation(worst.location, RobotLevel.POWER_NODE);
            if (node != null) {
              for (MapLocation n : ((PowerNode) node).neighbors()) {
                //TODO: use hash map
                for (int i = 0; i < sense.allied_nodes_count; i++) {
                  if (n.equals(sense.allied_nodes[i].getLocation())) {
                    target = worst;
                    break;
                  }
                }
              }
            }
          } catch (GameActionException e) {
            e.printStackTrace();
          }
        }
      } else {
        target = mediocre;
      }
    }
    return target != null;
  }

  public RobotInfo getTargetInfo() {
    return target;
  }

  public boolean fire() {
    if (target == null) return false;
    if (rc.isAttackActive()) return false;
    if (!rc.canAttackSquare(target.location)) return false;
    try {
      rc.attackSquare(target.location, target.type.level);
    } catch (GameActionException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

}
