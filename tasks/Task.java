package team133;

public abstract class Task {

  protected Bot bot;
  public int priority;
  public Task(Bot bot, int priority) {
    this.bot = bot;
    this.priority = priority;
    this.bot.addTask(this);
  }

  public abstract void go();

}
