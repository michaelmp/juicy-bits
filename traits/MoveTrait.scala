package team133

import team133.tasks._

trait MoveTrait extends Bot
  with SenseTrait
  {

  val motor = new Moves(this, sense)

  new MoveTask(this, Core.MOVE_PRIORITY, motor)

}
