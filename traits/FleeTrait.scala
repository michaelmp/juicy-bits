package team133

import team133.tasks._

trait FleeTrait extends Bot
  with MoveTrait
  with SenseTrait
  {

  new FleeTask(this, Core.FLEE_PRIORITY, motor, sense)

}
