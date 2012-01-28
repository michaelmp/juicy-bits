package team133

import team133.tasks._

trait BroadcastTrait extends Bot
  {

  val radio = new Broadcasts(this)

  new ReceiveTask(this, Core.RECV_PRIORITY, radio)
  new SendTask(this, Core.SEND_PRIORITY, radio)

}
