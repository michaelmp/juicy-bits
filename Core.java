package team133;

import battlecode.common.*;
import java.util.Comparator;

public class Core {

  /** Tasks are executed in order from low to high priority. */

  public static int PRIORITY_LOW = 0;
  // 0 - 999 reserved for core start-of-round tasks
  public static int RECV_PRIORITY = 0;
  public static int SENSE_PRIORITY = 1;
  public static int DECIDE_PRIORITY = 2;
  public static int SPREAD_PRIORITY = 3;
  public static int ANNOUNCE_PRIORITY = 4;
  public static int LISTEN_TO_INFORMATION = 998;
  public static int TAKE_ORDERS_PRIORITY = 999;

  public static int PRIORITY_MID = 1000;
  // 1000 - 9999 reserved for dynamic tasks (messaged orders)
  // ...

  public static int PRIORITY_HIGH = 10000;
  // 10000+ reserved for end-of-round tasks
  public static int BUILD_PRIORITY = 10000;
  public static int SHARE_PRIORITY = 10001;
  public static int SPAWN_PRIORITY = 10002;
  public static int ATTACK_PRIORITY = 10003;
  public static int FLEE_PRIORITY = 10004;
  public static int REGEN_PRIORITY = 10005;
  public static int MOVE_PRIORITY = 10006;
  public static int SEND_PRIORITY = 10100;

  private static int debug_profile_bytecode = 0;
  public static void debug_beginProfile() {
    debug_profile_bytecode = Clock.getBytecodesLeft();
  }
  public static void debug_endProfile() {
    System.out.println((debug_profile_bytecode - Clock.getBytecodesLeft()) + " bytecodes");
  }
  public static void debug_endProfile(String msg) {
    System.out.print(msg + ": ");
    debug_endProfile();
  }

  public static class DistanceComparator implements Comparator<MapLocation> {
    private MapLocation source;
    public DistanceComparator(MapLocation source) {
      this.source = source;
    }

    @Override
    public int compare(MapLocation a, MapLocation b) {
      return a.distanceSquaredTo(source) - b.distanceSquaredTo(source);
    }
  }

  private static final int[][] dir_ord_diff = {
    {0, 1, 2, 3, 4, 3, 2, 1, 0, 0},
    {1, 0, 1, 2, 3, 4, 3, 2, 0, 0},
    {2, 1, 0, 1, 2, 3, 4, 3, 0, 0},
    {3, 2, 1, 0, 1, 2, 3, 4, 0, 0},
    {4, 3, 2, 1, 0, 1, 2, 3, 0, 0},
    {3, 4, 3, 2, 1, 0, 1, 2, 0, 0},
    {2, 3, 4, 3, 2, 1, 0, 1, 0, 0},
    {1, 2, 3, 4, 3, 2, 1, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
  };

  public static int dirDiff(Direction a, Direction b) {
    return dir_ord_diff[a.ordinal()][b.ordinal()];
  }

}
