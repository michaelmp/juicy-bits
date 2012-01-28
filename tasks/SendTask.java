package team133.tasks;

import team133.*;
import battlecode.common.*;

public class SendTask extends Task {

  private final Broadcasts radio;
  private final RobotController rc;
  public SendTask(Bot bot, int priority, Broadcasts radio)  {
    super(bot, priority);
    this.radio = radio;
    this.rc = bot.rc;
  }

  public void go() {
    //rc.setIndicatorString(2, "sent: " + radio.packets_out_count + "/" + Broadcasts.MAX_OUT + " received: " + radio.packets_in_count + "/" + Broadcasts.MAX_IN);
    radio.packets_in_count = 0;
    radio.send();
  }

}
