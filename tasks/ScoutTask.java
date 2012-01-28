package team133.tasks;

import team133.*;
import battlecode.common.*;

public class ScoutTask extends Task {

  private final Moves motor;
  private final Senses sense;
  private final RobotController rc;
  private final Bot bot;
  public ScoutTask(Bot bot, int priority, Moves motor, Senses sense) {
    super(bot, priority);
    this.rc = bot.rc;
    this.bot = bot;
    this.motor = motor;
    this.sense = sense;
  }

  public void go() {
    MapLocation archon = sense.computeClosestArchon();

    if (sense.needFlux() || sense.combat_enemies_count > 0) {
      bot.acquireObjectiveMutex();
      if (sense.archon_locations_count > 0) {
        motor.location = archon;
        motor.location_cushion2 = 2;
      }
    } else {
      int x = bot.loc.x;
      int y = bot.loc.y;
      if (sense.bound_up == null) {
        motor.location = new MapLocation(x, y - 10);
      } else if (sense.bound_down == null) {
        motor.location = new MapLocation(x, y + 10);
      } else if (sense.bound_left == null) {
        motor.location = new MapLocation(x - 10, y);
      } else if (sense.bound_right == null) {
        motor.location = new MapLocation(x + 10, y);
      } else {
        motor.location = archon;
      }
      motor.location_cushion2 = 2;
    }

    if (sense.combat_enemies_count == 0) {
      if (rc.roundsUntilMovementIdle() > 1) bot.sleep = true;
      if (motor.location == null) {
        bot.sleep = true;
      } else if (bot.distanceSquaredTo(motor.location) <= motor.location_cushion2) {
        bot.sleep = true;
      }
    }
  }

}
