package team133;

public abstract class ObjectiveTask extends Task {

  public ObjectiveTask(Bot bot, int priority) {
    super(bot, priority);
  }

  public abstract boolean isComplete();

}
