package team133;

import battlecode.common.*;

public class ScorchEvaluator implements TargetEvaluator {

  private final RobotController rc;
  private final Bot bot;
  private final Senses sense;
  public ScorchEvaluator(Bot bot, Senses sense) {
    this.rc = bot.rc;
    this.bot = bot;
    this.sense = sense;
  }

  private RobotInfo target = null;
  private double collateral = 0;
  private double damage = 0;
  public boolean scan(RobotInfo[] objects, int limit) {
    RobotInfo info = null;
    collateral = 0;
    damage = 0;
    double amount;

    for (int i = 0; i < limit; i++) {
      info = objects[i];
      if (info.type.level != RobotLevel.ON_GROUND) continue;
      /*
      if (info.type == RobotType.TOWER) {
        if (!sense.isVulnerableEnemyTower(info)) {
          continue;
        }
      }
      */
      amount = Math.min(info.energon, rc.getType().attackPower);
      if (info.team == rc.getTeam()) {
        collateral += amount;
      } else if (info.team == rc.getTeam().opponent()) {
        damage += amount;
      }
    }
    return collateral < damage;
  }

  public RobotInfo getTargetInfo() {
    return null;
  }

  public boolean fire() {
    if (rc.isAttackActive()) return false;
    if (collateral > damage) return false;
    try {
      rc.attackSquare(rc.getLocation().add(rc.getDirection()), RobotLevel.ON_GROUND);
    } catch (GameActionException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

}
