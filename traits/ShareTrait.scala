package team133

import team133.tasks._

trait ShareTrait
  extends Bot
  with SenseTrait
  {

  val share = new Shares(this, sense)

  private val foo = new ShareTask(this, Core.SHARE_PRIORITY, share, sense)

}
