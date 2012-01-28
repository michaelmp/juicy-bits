package team133.tasks;

import team133.*;
import battlecode.common.*;

public class FleeTask extends Task {

  private final Moves motor;
  private final Senses sense;
  private final RobotController rc;
  private final Bot bot;
  public FleeTask(Bot bot, int priority, Moves motor, Senses sense) {
    super(bot, priority);
    this.rc = bot.rc;
    this.bot = bot;
    this.motor = motor;
    this.sense = sense;
  }

  private boolean mustFlee() {
    if (sense.combat_enemies_count == 0) return false;
    if (bot.type == RobotType.ARCHON) {
      if (sense.combat_enemies_count > sense.combat_allies_count - 3) return true;
      if (bot.distanceSquaredTo(sense.combat_enemies[0].location) < 25) return true;
    } else if (bot.type == RobotType.SOLDIER) {
      if (sense.enemy_scorchers_count > 0) return false;
      if (sense.combat_allies_count == 0) {
        if (sense.combat_enemies_count > 1) return true;
        //return sense.combat_enemies[0].energon / bot.type.attackPower > bot.energon / sense.combat_enemies[0].type.attackPower;
      }
    }
    return false;
  }

  public void go() {
    if (!mustFlee()) return;

    // Determine the threat.
    MapLocation threat = null;

    int i = 0;
    RobotInfo info = sense.combat_enemies[0]; //TODO: this may need to be prioritized

    if (info == null) {
      return;
    } else {
      threat = info.location;
    }

    MapLocation rescue = null;

    if (bot.type == RobotType.ARCHON) {
      if (sense.combat_allies_count > 1) {
        rescue = sense.combat_allies[0].location;
      } else {
        MapLocation archon = sense.computeClosestArchon();
        if (archon != null) {
          if (archon.equals(bot.loc) || bot.distanceSquaredTo(archon) < 25) {
            rescue = rc.sensePowerCore().getLocation();
          } else {
            rescue = archon;
          }
        }
      }
    } else {
      rescue = sense.computeClosestArchon();
    }

    //TODO: a nearby soldier may be safer, or broadcast distress
  
    if (rescue == null) rescue = rc.sensePowerCore().getLocation();

    if (motor.rescue_location != rescue) {
      motor.tracing = false;
    }

    motor.rescue_location = rescue;
    motor.rescue_location_cushion2 = 0;

    if (rc.canMove(bot.dir.opposite())) motor.backstep = true;

    motor.avoid_location = threat;
    motor.avoid_location_cushion2 = 25;
  }

}
