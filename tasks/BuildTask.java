package team133.tasks;

import team133.*;
import battlecode.common.*;

public class BuildTask extends Task {

  private final Builds builder;
  private final Senses sense;
  private final RobotController rc;
  public BuildTask(Bot bot, int priority, Builds builder, Senses sense) {
    super(bot, priority);
    this.rc = bot.rc;
    this.builder = builder;
    this.sense = sense;
  }

  public void go() {
    if (sense.combat_enemies_count > 0) return;
    builder.go();
  }

}
