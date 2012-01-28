package team133

import battlecode.common._

trait DisruptTrait extends Bot 
  with SenseTrait
  {

  val disrupter = new Weapon(this, new EnemyEvaluator(this, sense))

}
