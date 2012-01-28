package team133

import team133._
import battlecode.common._

object RobotPlayer {

  def delegate(rc : RobotController) : Bot = {
    if (rc.getType == RobotType.SOLDIER) {
      new Soldier(rc)
    } else if (rc.getType == RobotType.ARCHON) {
      new Archon(rc)
    } else if (rc.getType == RobotType.SCOUT) {
      new Scout(rc)
    } else if (rc.getType == RobotType.SCORCHER) {
      new Scorcher(rc)
    } else {
      null
    }
  }

  def run(rc : RobotController) {
    while(true) {
      try {
        var bot = delegate(rc)
        if (bot == null) {
          rc.`yield`()
        } else {
          bot.sortTasks()
          while (!bot.recast) {
            bot.resetMutices()
            bot.runTasks()
            bot.killCompletedMidTasks()
            bot.resortMidTasks()
            bot.`yield`()
          }
        }
      } catch {
        case e : Exception => {
          e.printStackTrace()
        }
      }
    }
  }

}
