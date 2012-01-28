package team133

import team133.tasks._

trait ListenForBoundsTrait extends Bot
  with BroadcastTrait
  with SenseTrait
  {

  new ListenForBoundsTask(this, Core.LISTEN_TO_INFORMATION, radio, sense)

}
