package team133;

import battlecode.common.*;
import java.util.*;

public abstract class Bot {

  public static int TASK_TIMEOUT = 50;

  public static int MAX_LOW_TASKS = 20;
  public static int MAX_MID_TASKS = 3;
  public static int MAX_HIGH_TASKS = 20;

  public RobotController rc;
  private final Comparator<TaskWrapper> comparator;
  public Bot(RobotController rc) {
    this.rc = rc;
    this.comparator = new PriorityComparator();
    this.loc = rc.getLocation();
    this.dir = rc.getDirection();
    this.type = rc.getType();
    this.flux = rc.getFlux();
    this.team = rc.getTeam();
    this.robot = rc.getRobot();
    this.energon = rc.getEnergon();
    this.id = this.robot.getID();
    this.level = this.robot.getRobotLevel();
  }

  public boolean sleep = false;

  public MapLocation loc;
  public Direction dir;
  public double flux;
  public final RobotType type;
  public final Team team;
  public final Robot robot;
  public double energon;
  public final int id;
  public final RobotLevel level;

  public static class PriorityComparator implements Comparator<TaskWrapper> {
    @Override
    public int compare(TaskWrapper a, TaskWrapper b) {
      return a.task.priority - b.task.priority;
    }
  }

  public static class TaskWrapper {
    public final Task task;
    public final int timeout;
    public TaskWrapper(Task t) {
      this.task = t;
      if (isEphemeralTask(t)) {
        this.timeout = Clock.getRoundNum() + TASK_TIMEOUT;
      } else {
        this.timeout = Clock.getRoundNum() + 99999; //TODO: get devs to publish a constant
      }
    }
  }

  private List<TaskWrapper> low_tasks = new ArrayList<TaskWrapper>();
  private List<TaskWrapper> mid_tasks = new ArrayList<TaskWrapper>();
  private List<TaskWrapper> high_tasks = new ArrayList<TaskWrapper>();

  public boolean addTask(Task t) {
    if (t.priority < Core.PRIORITY_MID) {
      if (low_tasks.size() >= MAX_LOW_TASKS) return false;
      low_tasks.add(new TaskWrapper(t));
    } else if (t.priority < Core.PRIORITY_HIGH) {
      if (mid_tasks.size() >= MAX_MID_TASKS) return false;
      mid_tasks.add(new TaskWrapper(t));
    } else {
      if (high_tasks.size() >= MAX_HIGH_TASKS) return false;
      high_tasks.add(new TaskWrapper(t));
    }
    return true;
  }

  public void sortTasks() {
    Collections.sort(low_tasks, comparator);
    Collections.sort(mid_tasks, comparator);
    Collections.sort(high_tasks, comparator);
    //debug_listTasks();
  }

  public void resortMidTasks() {
    Collections.sort(mid_tasks, comparator);
  }

  public static boolean isEphemeralTask(Task t) {
    return t.priority >= Core.PRIORITY_MID && t.priority < Core.PRIORITY_HIGH;
  }

  public void killCompletedMidTasks() {
    TaskWrapper tw;

    loop: for (int i = 0; i < mid_tasks.size(); i++) {
      tw = mid_tasks.get(i);

      nokill: {
        if (tw.timeout <= Clock.getRoundNum()) {
          break nokill;
        }
        if (tw.task instanceof ObjectiveTask) {
          if (((ObjectiveTask) tw.task).isComplete()) {
            break nokill;
          }
        }
        continue loop;
      }

      mid_tasks.remove(tw);
      i--;
    }
  }

  private void debug_listTasks() {
    for (TaskWrapper t : low_tasks) {
      System.out.println(t.task.toString());
    }
    for (TaskWrapper t : mid_tasks) {
      System.out.println(t.task.toString());
    }
    for (TaskWrapper t : high_tasks) {
      System.out.println(t.task.toString());
    }
  }

  public void runTasks() {
    if (sleep) {
      sleep = false;
      return;
    }

    // dangerous bytecode tricks
    if (type == RobotType.SOLDIER) {
      if (rc.isMovementActive() && rc.isAttackActive()) return;
      if (mid_tasks.size() == 0 && rc.isMovementActive()) return;
    }

    // low
    for (int i = 0; i < low_tasks.size(); i++) {
      //Core.debug_beginProfile();
      low_tasks.get(i).task.go();
      //Core.debug_endProfile(low_tasks.get(i).task.toString());
    }

    // mid
    for (int i = 0; i < mid_tasks.size(); i++) {
      //Core.debug_beginProfile();
      mid_tasks.get(i).task.go();
      rc.setIndicatorString(2, mid_tasks.get(i).task.toString());
      //Core.debug_endProfile(mid_tasks.get(i).task.toString());
    }
    // high
    for (int i = 0; i < high_tasks.size(); i++) {
      //Core.debug_beginProfile();
      high_tasks.get(i).task.go();
      //Core.debug_endProfile(high_tasks.get(i).task.toString());
    }
    debug_tasks();
  }

  private void debug_tasks() {
    rc.setIndicatorString(0, "orders: " + mid_tasks.size() + "/" + (low_tasks.size() + mid_tasks.size() + high_tasks.size()) + " total tasks");
  }

  private double last_energon = 0;
  public boolean underAttack() {
    return energon < last_energon;
  }

  public void yield() {
    last_energon = rc.getEnergon();
    rc.yield();
    loc = rc.getLocation();
    dir = rc.getDirection();
    flux = rc.getFlux();
    energon = rc.getEnergon();
  }

  private boolean o_mutex = false;
  public boolean acquireObjectiveMutex() {
    if (o_mutex) return false;
    return o_mutex = true;
  }
  public void releaseObjectiveMutex() {
    o_mutex = false;
  }
  public boolean objectiveMutexAvailable() {
    return !o_mutex;
  }

  private boolean m_mutex = false;
  public boolean acquireMovementMutex() {
    if (m_mutex) return false;
    return m_mutex = true;
  }
  public void releaseMovementMutex() {
    m_mutex = false;
  }
  public boolean movementMutexAvailable() {
    return !m_mutex;
  }

  private boolean a_mutex = false;
  public boolean acquireAttackMutex() {
    if (a_mutex) return false;
    return a_mutex = true;
  }
  public void releaseAttackMutex() {
    a_mutex = false;
  }
  public boolean attackMutexAvailable() {
    return !a_mutex;
  }

  private double f_mutex = 0;
  public boolean acquireFluxMutex(double amount) {
    if (f_mutex > amount) return false;
    f_mutex = amount;
    return true;
  }
  public void releaseFluxMutex() {
    f_mutex = 0.0;
  }
  public boolean fluxMutexAvailable(double amount) {
    return f_mutex <= amount;
  }

  private boolean p_mutex = false;
  public boolean acquireProductionMutex() {
    if (p_mutex) return false;
    return p_mutex = true;
  }
  public void releaseProductionMutex() {
    p_mutex = false;
  }
  public boolean productionMutexAvailable() {
    return !p_mutex;
  }

  /**
   * Recover all locks when sensible.
   */
  public void resetMutices() {
    releaseObjectiveMutex();
    releaseProductionMutex();
    releaseFluxMutex();
    if (!rc.isAttackActive()) releaseAttackMutex();
    if (!rc.isMovementActive()) releaseMovementMutex();
  }

  /**
   * @return true iff this robot should recast itself to a new class.
   */
  public boolean recast() {
    return false;
  }

  public boolean isFacing(MapLocation loc) {
    return directionTo(loc) == dir;
  }

  public boolean isAdjacentTo(MapLocation loc) {
    return this.loc.isAdjacentTo(loc);
  }

  public Direction directionTo(MapLocation loc) {
    return this.loc.directionTo(loc);
  }

  public int distanceSquaredTo(MapLocation loc) {
    return this.loc.distanceSquaredTo(loc);
  }

}
