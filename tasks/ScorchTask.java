package team133.tasks;

import team133.*;
import battlecode.common.*;

public class ScorchTask extends Task {

  private final Weapon attacker;
  private final Moves motor;
  private final Senses sense;
  private final RobotController rc;
  public ScorchTask(Bot bot, int priority, Weapon attacker, Moves motor, Senses sense) {
    super(bot, priority);
    this.rc = bot.rc;
    this.attacker = attacker;
    this.motor = motor;
    this.sense = sense;
  }

  public void go() {
    attacker.scan(sense.all_info, sense.all_info_count);
    attacker.fire();
  }

}
