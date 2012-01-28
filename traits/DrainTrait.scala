package team133

import battlecode.common._

trait DrainTrait extends Bot 
  with SenseTrait
  {

  val drainer = new Weapon(this, new EnemyEvaluator(this, sense))

}
