juicy-bits
==========

A robot player for the MIT Battlecode game.

Some notable features:

(1) Scala + Java. A typical robot definition looks something like this:

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

Java for the performance-intensive parts (most), and Scala for the creative/experimental parts.
In the above example, adding a behavior to a robot is as simple as adding a Scala trait.

(2) Task manager. All robot behavior is governed by a priority queue,
with semantics for eager yielding, redundancy filtering, and decay.

(3) Mutual exclusion semantics. Arbitrary traits, and therefore arbitrary tasks, are capable of working
together by respecting a set of mutices controlling critical robot resources (action, flux, energon).