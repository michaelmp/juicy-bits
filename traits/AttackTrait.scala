package team133

import team133.tasks._

trait AttackTrait extends Bot
  with MoveTrait
  with SenseTrait
  {

  val attacker = new Weapon(this, new EnemyEvaluator(this, sense))

  new AttackTask(this, Core.ATTACK_PRIORITY, attacker, motor, sense)

}
