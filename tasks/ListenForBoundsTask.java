package team133.tasks;

import team133.*;
import battlecode.common.*;

/**
 * Reads message packets and creates new tasks based on the orders.
 * This task has low priority, and must only create new tasks with priority
 * greater/equal than Core.PRIORITY_MID and lower than Core.PRIORITY_HIGH. 
 */
public class ListenForBoundsTask extends Task {

  private final Broadcasts radio;
  private final Senses sense;
  private final RobotController rc;
  public ListenForBoundsTask(Bot bot, int priority, Broadcasts radio, Senses sense)  {
    super(bot, priority);
    this.radio = radio;
    this.rc = bot.rc;
    this.sense = sense;
  }

  public void go() {
    for (int i = 0; i < radio.packets_in_count; i++) {
      Broadcasts.Packet p = radio.packets_in[i];
      switch (p.opcode) {
        case Broadcasts.BOUND_UP:
          sense.test_up_bound(p.loc);
          break;
        case Broadcasts.BOUND_DOWN:
          sense.test_down_bound(p.loc);
          break;
        case Broadcasts.BOUND_LEFT:
          sense.test_left_bound(p.loc);
          break;
        case Broadcasts.BOUND_RIGHT:
          sense.test_right_bound(p.loc);
          break;
      }
    }
  }

}
