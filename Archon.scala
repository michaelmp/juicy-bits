package team133

import battlecode.common._

class Archon(rc : RobotController) extends Bot(rc)
  with AnnounceBoundsTrait
  with AnnounceEnemyTrait
  with DecideTrait
  with FleeTrait
  with ListenForBoundsTrait
  with ShareTrait
  with SpreadTrait
