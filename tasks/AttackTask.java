package team133.tasks;

import team133.*;
import battlecode.common.*;

public class AttackTask extends Task {

  private final Weapon attacker;
  private final Moves motor;
  private final Senses sense;
  private final RobotController rc;
  private final Bot bot;
  public AttackTask(Bot bot, int priority, Weapon attacker, Moves motor, Senses sense) {
    super(bot, priority);
    this.rc = bot.rc;
    this.bot = bot;
    this.attacker = attacker;
    this.motor = motor;
    this.sense = sense;
  }

  public void go() {
    attacker.scan(sense.enemy_info, sense.enemy_info_count);
    attacker.fire();

    if (bot.type == RobotType.SCOUT) {
      return;
    }

    RobotInfo info = attacker.ev.getTargetInfo();

    if (info != null) {
      if (info.type != RobotType.TOWER) {
        motor.direction = bot.directionTo(info.location);
        motor.direction_cushion = 0;
        if (!rc.canAttackSquare(info.location)) {
          if (bot.distanceSquaredTo(info.location) > bot.type.attackRadiusMaxSquared) {
            motor.location = info.location;
            motor.location_cushion2 = bot.type.attackRadiusMaxSquared;
          } else {
            motor.location = null;
          }
        } else {
          motor.location = null;
        }
      }
    }
  }

}
