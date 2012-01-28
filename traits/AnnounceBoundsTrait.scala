package team133

import team133.tasks._

trait AnnounceBoundsTrait extends Bot
  with BroadcastTrait
  with SenseTrait
  {

  new AnnounceBoundsTask(this, Core.ANNOUNCE_PRIORITY, sense, radio)

}
