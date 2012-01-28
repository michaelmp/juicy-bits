package team133.tasks;

import team133.*;
import battlecode.common.*;

public class ReceiveTask extends Task {

  private final Broadcasts radio;
  private final RobotController rc;
  public ReceiveTask(Bot bot, int priority, Broadcasts radio)  {
    super(bot, priority);
    this.radio = radio;
    this.rc = bot.rc;
  }

  public void go() {
    radio.recv();
  }

}
