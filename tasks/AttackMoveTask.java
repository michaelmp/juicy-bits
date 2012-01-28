package team133.tasks;

import team133.*;
import battlecode.common.*;

public class AttackMoveTask extends ObjectiveTask {

  private final RobotController rc;
  public final MapLocation target;
  private final Moves motor;
  public AttackMoveTask(Bot bot, int priority, MapLocation target, Moves motor) {
    super(bot, priority);
    this.rc = bot.rc;
    this.target = target;
    this.motor = motor;
  }

  public void go() {
    if (isComplete()) return;
    if (bot.acquireObjectiveMutex()) {
      motor.location = target;
      motor.location_cushion2 = bot.type.attackRadiusMaxSquared;
    }
  }

  public boolean isComplete() {
    return bot.distanceSquaredTo(target) <= bot.type.attackRadiusMaxSquared;
  }

}
