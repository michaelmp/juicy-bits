package team133.tasks;

import team133.*;
import battlecode.common.*;

public class SpawnTask extends Task {

  private Spawns spawner;
  private RobotController rc;
  public SpawnTask(Bot bot, int priority, Spawns spawner)  {
    super(bot, priority);
    this.spawner = spawner;
    this.rc = bot.rc;
  }

  public void go() {
    spawner.go();
  }

}
