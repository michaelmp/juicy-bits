package team133.tasks;

import team133.*;
import battlecode.common.*;

public class SpreadTask extends Task {

  private final Builds builder;
  private final Moves motor;
  private final Senses sense;
  private final RobotController rc;
  public SpreadTask(Bot bot, int priority, Builds builder, Moves motor, Senses sense) {
    super(bot, priority);
    this.rc = bot.rc;
    this.builder = builder;
    this.motor = motor;
    this.sense = sense;
  }

  public void go() {
    if (builder.target_location != null && builder.target_location.distanceSquaredTo(bot.loc) < builder.target_lock_distance_squared) {
      motor.avoid_location = null;
    } else {
      if (sense.archon_locations_count > 1) {
        for (int i = 0; i < sense.archon_locations_count; i++) {
          if (sense.archon_locations[i].equals(bot.loc)) continue;
          if (bot.distanceSquaredTo(sense.archon_locations[i]) < GameConstants.PRODUCTION_PENALTY_R2 + 1) {
            //bot.acquireProductionMutex();
            motor.avoid_location = sense.archon_locations[i];
            motor.avoid_location_cushion2 = GameConstants.PRODUCTION_PENALTY_R2 + 1;
            break;
          }
        }
      }
    }
  }

}
