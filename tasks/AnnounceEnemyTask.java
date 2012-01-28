package team133.tasks;

import team133.*;
import battlecode.common.*;

public class AnnounceEnemyTask extends Task {

  private final Broadcasts radio;
  private final Senses sense;
  private final RobotController rc;
  private final Bot bot;
  public AnnounceEnemyTask(Bot bot, int priority, Senses sense, Broadcasts radio) {
    super(bot, priority);
    this.sense = sense;
    this.radio = radio;
    this.rc = bot.rc;
    this.bot = bot;
  }

  public void go() {
    if (sense.enemy_archons_count > 0) {
      radio.addMessage(new Broadcasts.Packet(Broadcasts.ATTACK_TO_LOC, sense.enemy_archons[0].location, bot.loc, null));
    } else if (sense.combat_enemies_count > 0) {
      radio.addMessage(new Broadcasts.Packet(Broadcasts.ATTACK_TO_LOC, sense.combat_enemies[0].location, bot.loc, null));
    } else if (sense.enemy_towers_count > 0) {
      radio.addMessage(new Broadcasts.Packet(Broadcasts.ATTACK_TO_LOC, sense.enemy_towers[0].location, bot.loc, null));
    }
  }

}
