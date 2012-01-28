package team133.tasks;

import team133.*;
import battlecode.common.*;

public class AnnounceBoundsTask extends Task {

  private final Broadcasts radio;
  private final Senses sense;
  private final RobotController rc;
  private final Bot bot;
  public AnnounceBoundsTask(Bot bot, int priority, Senses sense, Broadcasts radio) {
    super(bot, priority);
    this.sense = sense;
    this.radio = radio;
    this.rc = bot.rc;
    this.bot = bot;
  }

  public void go() {
    if ((Clock.getRoundNum() + bot.robot.getID()) % 100 == 0) {
      if (sense.bound_up != null) {
        radio.addMessage(new Broadcasts.Packet(Broadcasts.BOUND_UP, sense.bound_up, bot.loc, null));
      }
      if (sense.bound_down != null) {
        radio.addMessage(new Broadcasts.Packet(Broadcasts.BOUND_DOWN, sense.bound_down, bot.loc, null));
      }
      if (sense.bound_right != null) {
        radio.addMessage(new Broadcasts.Packet(Broadcasts.BOUND_RIGHT, sense.bound_right, bot.loc, null));
      }
      if (sense.bound_left != null) {
        radio.addMessage(new Broadcasts.Packet(Broadcasts.BOUND_LEFT, sense.bound_left, bot.loc, null));
      }
    }
  }

}
