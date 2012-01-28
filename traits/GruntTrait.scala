package team133

import team133.tasks._

trait GruntTrait extends Bot
  with MoveTrait
  with SenseTrait
  {

  new GruntTask(this, Core.DECIDE_PRIORITY, motor, sense)

}
