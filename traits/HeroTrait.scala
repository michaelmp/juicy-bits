package team133

import team133.tasks._

trait HeroTrait extends Bot
  with MoveTrait
  with SenseTrait
  {

  new HeroTask(this, Core.DECIDE_PRIORITY, motor, sense)

}
