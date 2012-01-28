package team133.tasks;

import team133.*;
import battlecode.common.*;

public class DecideTask extends Task {

  private final Broadcasts radio;
  private final Moves motor;
  private final Senses sense;
  private final Builds builder;
  private final Spawns spawner;
  private final RobotController rc;
  public DecideTask(Bot bot, int priority, Moves motor, Senses sense, Builds builder, Spawns spawner, Broadcasts radio) {
    super(bot, priority);
    this.motor = motor;
    this.sense = sense;
    this.builder = builder;
    this.radio = radio;
    this.spawner = spawner;
    this.rc = bot.rc;
  }

  public void go() {
    MapLocation node = sense.computeClosestCapturableNode();
    /*
    if (bot.loc == sense.closest_archon_to_core) {
      // closest archon to home gets closest node to home
      node = sense.closest_node_to_core;
    } else {
      // half go to nearest, other half go random
      if (bot.id % 2 == 1) {
        node = sense.unclaimed_node_locations[0];
      } else {
        node = sense.unclaimed_node_locations[(bot.id >> 1) % sense.unclaimed_node_locations_count];
      }
    }*/

    if (sense.combat_enemies_count > 0) {
      builder.target_location = null;
    } else if (node != null && sense.allied_nodes_count < (Clock.getRoundNum() / 200)) {
      builder.target_location = node;

      if ((Clock.getRoundNum() + bot.id) % 20 == 0) {
        radio.addMessage(new Broadcasts.Packet(Broadcasts.DEFEND_AT_LOC, node, bot.loc, null));
      }

      motor.avoid_location = builder.target_location;
      motor.avoid_location_cushion2 = 1;

      motor.location = node;
      motor.location_cushion2 = 2;

      try {
        if (rc.canSenseSquare(motor.location)) {
          GameObject o = rc.senseObjectAtLocation(motor.location, RobotType.TOWER.level);
          if (o != null) {
            if (o.getTeam() == bot.team) {
              motor.direction = bot.directionTo(motor.location);
              motor.direction_cushion = 0;
            }
          } else {
            motor.direction = bot.directionTo(motor.location);
            motor.direction_cushion = 0;
          }
        }
      } catch (GameActionException e) {
        e.printStackTrace();
        return;
      }
    } else {
      builder.target_location = null;
    }

    /*
    if (sense.enemy_soldiers_count > sense.combat_allies_count && sense.allied_scorchers_count < 1) {
      spawner.target = RobotType.SCORCHER;
    } else*/
   
    if (Clock.getRoundNum() < 200 && rc.senseAlliedArchons()[0].equals(bot.loc)) {
      spawner.target = RobotType.SCOUT;
    } else if (sense.allied_soldiers_count < (sense.allied_archons_count + 1) / 0.5) {
      spawner.target = RobotType.SOLDIER;
    } else if (sense.allied_scouts_count == 0 && Clock.getRoundNum() - spawner.last_scout > 300) {
      spawner.target = RobotType.SCOUT;
    } else if (bot.flux > 250) {
      spawner.target = RobotType.SOLDIER;
    } else {
      spawner.target = null;
    }

    if (sense.isNodeThere(bot.loc)) {
      motor.avoid_location = bot.loc;
      motor.avoid_location_cushion2 = 1;
    }
  }

}
