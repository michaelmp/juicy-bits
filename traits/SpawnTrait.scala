package team133

import team133.tasks._

trait SpawnTrait extends Bot
  with MoveTrait
  {

  val spawner = new Spawns(this, motor)

  private val foo = new SpawnTask(this, Core.SPAWN_PRIORITY, spawner)

}
