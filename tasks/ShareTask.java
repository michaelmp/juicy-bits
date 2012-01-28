package team133.tasks;

import team133.*;
import battlecode.common.*;

public class ShareTask extends Task {

  private Senses sense;
  private Shares share;
  private RobotController rc;
  public ShareTask(Bot bot, int priority, Shares share, Senses sense)  {
    super(bot, priority);
    this.share = share;
    this.sense = sense;
    this.rc = bot.rc;
  }

  public void go() {
    share.go();
  }

}
