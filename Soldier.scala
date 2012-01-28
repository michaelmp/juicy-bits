package team133

import battlecode.common._

class Soldier(rc : RobotController) extends Bot(rc)
  with AttackTrait
  with FleeTrait
  with GruntTrait
  with ListenForBoundsTrait
  with RespondToAttackOrdersTrait
  with RespondToDefendOrdersTrait
