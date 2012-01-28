package team133

import team133.tasks._

trait AnnounceEnemyTrait extends Bot
  with BroadcastTrait
  with SenseTrait
  {

  new AnnounceEnemyTask(this, Core.ANNOUNCE_PRIORITY, sense, radio)

}
