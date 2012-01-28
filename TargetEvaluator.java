package team133;

import battlecode.common.*;

public interface TargetEvaluator {

  public boolean scan(RobotInfo[] robots, int limit);
  public RobotInfo getTargetInfo();
  public boolean fire();

}
