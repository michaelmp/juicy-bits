juicy-bits
==========

A robot player for the MIT Battlecode game.

Some notable features:

(1) Scala + Java. Java for performance-intensive code, Scala for experimental code. A typical robot definition looks something like this:

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

(2) Task manager. All robot behavior is governed by a priority queue,
with semantics for eager yielding, redundancy filtering, and decay.

(3) Mutual exclusion semantics. All tasks use mutices to aquire critical robot resources (action, flux, energon).
The result is freedom to compose different traits and differing task priorities.

In the above example, adding a behavior to the Soldier robot is as simple as adding a Scala trait.