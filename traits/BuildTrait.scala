package team133

import team133.tasks._

trait BuildTrait extends Bot
  with MoveTrait
  with SenseTrait
  {

  val builder = new Builds(this, motor)

  new BuildTask(this, Core.BUILD_PRIORITY, builder, sense)

}
