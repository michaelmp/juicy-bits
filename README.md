juicy-bits
==========

A robot player for the MIT Battlecode game.

Some notable features:

(1) Scala + Java hybrid for speed and flexibility. A typical robot definition looks something like this:

```scala
package team133

import battlecode.common._

class Soldier(rc : RobotController) extends Bot(rc)
  with AttackTrait
  with FleeTrait
  with GruntTrait
  with ListenForBoundsTrait
  with RespondToAttackOrdersTrait
  with RespondToDefendOrdersTrait
```