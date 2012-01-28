package team133.tasks;

import team133.*;

public class MoveTask extends Task {

  private Moves motor;
  public MoveTask(Bot bot, int priority, Moves motor) {
    super(bot, priority);
    this.motor = motor;
  }

  public void go() {
    motor.go();
  }


}
