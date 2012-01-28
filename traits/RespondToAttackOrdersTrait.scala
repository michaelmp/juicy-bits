package team133

import team133.tasks._

trait RespondToAttackOrdersTrait extends Bot
  with BroadcastTrait
  with MoveTrait
  {

  new RespondToAttackOrdersTask(this, Core.TAKE_ORDERS_PRIORITY, radio, motor)

}
