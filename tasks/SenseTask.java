package team133.tasks;

import team133.*;

public class SenseTask extends Task {

  private Senses sense;
  public SenseTask(Bot bot, int priority, Senses sense) {
    super(bot, priority);
    this.sense = sense;
  }

  public void go() {
    sense.refresh();
  }


}
