package team133.tasks;

import team133.*;
import battlecode.common.*;

public class RegenTask extends Task {

  private final Regens regen;
  private final Moves motor;
  private final Senses sense;
  private final RobotController rc;
  public RegenTask(Bot bot, int priority, Regens regen, Moves motor, Senses sense) {
    super(bot, priority);
    this.rc = bot.rc;
    this.regen = regen;
    this.motor = motor;
    this.sense = sense;
  }

  private RobotInfo needyBot() {
    for (int i = 0; i < sense.allied_info_count; i++) {
      if (sense.allied_info[i].regen) continue;
      if (sense.allied_info[i].type.maxEnergon - sense.allied_info[i].energon > 1) {
        return sense.allied_info[i];
      }
    }
    return null;
  }

  public void go() {
    RobotInfo info = needyBot();
    if (info != null) {
      if (bot.distanceSquaredTo(info.location) <= bot.type.attackRadiusMaxSquared) {
        regen.go();
      }
      if (bot.acquireObjectiveMutex()) {
        motor.location = info.location;
        motor.location_cushion2 = 0;
      }
    }
  }


}
