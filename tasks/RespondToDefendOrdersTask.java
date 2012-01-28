package team133.tasks;

import team133.*;
import battlecode.common.*;

/**
 * Reads message packets and creates new tasks based on the orders.
 * This task has low priority, and must only create new tasks with priority
 * greater/equal than Core.PRIORITY_MID and lower than Core.PRIORITY_HIGH. 
 */
public class RespondToDefendOrdersTask extends Task {

  private final Broadcasts radio;
  private final Moves motor;
  private final RobotController rc;
  public RespondToDefendOrdersTask(Bot bot, int priority, Broadcasts radio, Moves motor)  {
    super(bot, priority);
    this.radio = radio;
    this.rc = bot.rc;
    this.motor = motor;
  }

  public void go() {
    for (int i = 0; i < radio.packets_in_count; i++) {
      Broadcasts.Packet p = radio.packets_in[i];
      switch (p.opcode) {
        case Broadcasts.DEFEND_AT_LOC:
          new AttackMoveTask(bot, Core.PRIORITY_MID, p.loc, motor);
          break;
      }
    }
  }

}
