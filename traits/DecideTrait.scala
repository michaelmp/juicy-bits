package team133

import team133.tasks._

trait DecideTrait extends Bot
  with BroadcastTrait
  with BuildTrait
  with MoveTrait
  with SenseTrait
  with SpawnTrait
  {

  new DecideTask(this, Core.DECIDE_PRIORITY, motor, sense, builder, spawner, radio)

}
