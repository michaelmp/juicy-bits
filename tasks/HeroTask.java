package team133.tasks;

import team133.*;
import battlecode.common.*;

public class HeroTask extends Task {

  private Moves motor;
  private Senses sense;
  private RobotController rc;
  public HeroTask(Bot bot, int priority, Moves motor, Senses sense) {
    super(bot, priority);
    this.rc = bot.rc;
    this.motor = motor;
    this.sense = sense;
  }

  public void go() {
    RobotInfo info = sense.combat_enemies[0];
    
    if (info != null) {
      motor.location = info.location;
      motor.location_cushion2 = 1;
      motor.direction = bot.directionTo(info.location);
      motor.direction_cushion = 0;
    } else if (bot.underAttack()) {
      motor.location = null;
      motor.direction = bot.dir.opposite();
      motor.direction_cushion = 0;
    } else if (sense.allied_soldiers_count == 0) {
      motor.location = sense.archon_locations[0];
    }
  }

}
