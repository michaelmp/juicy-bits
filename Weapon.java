package team133;

import battlecode.common.*;

public class Weapon {

  public final TargetEvaluator ev;
  protected final Bot bot;

  public Weapon(Bot bot, TargetEvaluator ev) {
    this.bot = bot;
    this.ev = ev;
  }

  /**
   * Acquires attack lock and only releases if a target exists, thus blocking
   * any other attackers from wasting their time scanning.
   */
  public void scan(RobotInfo[] objects, int limit) {
    if (!bot.acquireAttackMutex()) return;
    
    critical: {
      if (ev.scan(objects, limit)) break critical;
      return;
    }

    bot.releaseAttackMutex();
  }

  /**
   * Spends attack lock attacking the target.
   */
  public void fire() {
    if (!bot.acquireAttackMutex()) return;

    critical: {
      if (ev.fire()) return;
    }

    bot.releaseAttackMutex();
  }

}
