package team133;

import battlecode.common.*;

public class Regens {

  private final RobotController rc;
  private final Senses sense;
  private final Bot bot;

  public Regens(Bot bot, Senses sense) {
    this.rc = bot.rc;
    this.sense = sense;
    this.bot = bot;
  }

  private boolean canRegen() {
    if (bot.flux < 5) return false;
    return true;
  }

  public void go() {
    if (!canRegen()) return;
    try {
      rc.regenerate();
      return;
    } catch (GameActionException e) {
      e.printStackTrace();
    }
    return;
  }

}
