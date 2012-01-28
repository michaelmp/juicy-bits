package team133

import battlecode.common._
import team133.tasks._

trait ScorchTrait extends Bot
  with MoveTrait
  with SenseTrait
  {

  val scorcher = new Weapon(this, new ScorchEvaluator(this, sense))

  new ScorchTask(this, Core.ATTACK_PRIORITY, scorcher, motor, sense)

}
