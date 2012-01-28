package team133

import team133.tasks._

trait RespondToDefendOrdersTrait extends Bot
  with BroadcastTrait
  with MoveTrait
  {

  new RespondToDefendOrdersTask(this, Core.TAKE_ORDERS_PRIORITY, radio, motor)

}
