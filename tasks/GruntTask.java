package team133.tasks;

import team133.*;
import battlecode.common.*;

public class GruntTask extends Task {

  private final Moves motor;
  private final Senses sense;
  private final RobotController rc;
  private final Bot bot;
  public GruntTask(Bot bot, int priority, Moves motor, Senses sense) {
    super(bot, priority);
    this.rc = bot.rc;
    this.bot = bot;
    this.motor = motor;
    this.sense = sense;
  }

  public void go() {
    MapLocation archon = sense.computeClosestArchon();

    if (sense.needFlux()) {
      motor.location = archon;
      motor.location_cushion2 = 2;
    }

    if (sense.combat_enemies_count > 0) {
      motor.location = sense.combat_enemies[0].location;
      motor.location_cushion2 = bot.type.attackRadiusMaxSquared;
    } else if (sense.enemy_info_count > 0) {
      if (motor.location == null || sense.enemy_info[0].type != RobotType.TOWER) {
        motor.location = sense.enemy_info[0].location;
        motor.location_cushion2 = bot.type.attackRadiusMaxSquared;
      }
    } else {
      if (Clock.getRoundNum() % 2 == 0) bot.sleep = true;
      if (sense.archon_locations_count > 0) {
        motor.location = archon;
        motor.location_cushion2 = 2;
      }
    }

    if (sense.combat_enemies_count == 0 && sense.allied_soldiers_count == 0) {
      if (archon != null && bot.distanceSquaredTo(archon) > GameConstants.BROADCAST_RADIUS_SQUARED) {
        motor.location = archon;
        motor.location_cushion2 = GameConstants.BROADCAST_RADIUS_SQUARED;
      } else {
        motor.direction = bot.dir.opposite();
        motor.direction_cushion = 0;
      }
    }

    if (sense.isNodeThere(bot.loc)) {
      motor.avoid_location = bot.loc;
      motor.avoid_location_cushion2 = 1;
    }
  }

}
