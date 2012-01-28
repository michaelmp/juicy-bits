package team133

import battlecode.common._
import team133.tasks._

trait RegenTrait extends Bot
  with MoveTrait
  with SenseTrait
  {

  val regen = new Regens(this, sense)

  new RegenTask(this, Core.REGEN_PRIORITY, regen, motor, sense)

}
