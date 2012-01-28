package team133

import battlecode.common._

class Scout(rc : RobotController) extends Bot(rc)
  with AnnounceBoundsTrait
  with AnnounceEnemyTrait
  with AttackTrait
  with ListenForBoundsTrait
  with RegenTrait
  with ScoutTrait
  with ShareTrait
