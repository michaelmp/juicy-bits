package team133

import team133.tasks._

trait ScoutTrait extends Bot
  with MoveTrait
  with SenseTrait
  {

  private val foo = new ScoutTask(this, Core.DECIDE_PRIORITY, motor, sense)

}
