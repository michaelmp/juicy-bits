package team133

import team133.tasks._

trait SenseTrait extends Bot {

  val sense = new Senses(this)

  private val foo = new SenseTask(this, Core.SENSE_PRIORITY, sense)

}
