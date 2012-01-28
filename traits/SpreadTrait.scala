package team133

import team133.tasks._

trait SpreadTrait extends Bot
  with BuildTrait
  with MoveTrait
  with SenseTrait
  {

  private val foo = new SpreadTask(this, Core.SPREAD_PRIORITY, builder, motor, sense)

}
