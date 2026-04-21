Arkanoid clone in Java 17 — focused on clean OOP design patterns and a custom geometry engine.

## Architecture

Three design patterns structure the codebase:

**Observer** — `HitNotifier` / `HitListener` interfaces
`Block` implements `HitNotifier` and maintains a list of `HitListener` subscribers. On each hit it calls `hitEvent(Block, Ball)` on every registered listener. Concrete listeners: `BlockRemover` (removes the block and decrements a counter), `BallRemover` (removes the ball when it reaches the death region), `ScoreTrackingListener` (adds 5 points per destroyed block).

**Composite** — `SpriteCollection`, `GameEnvironment`
`SpriteCollection` holds all `Sprite` objects and fans out `drawAllOn()` and `notifyAllTimePassed()` calls. `GameEnvironment` holds all `Collidable` objects and exposes `getClosestCollision(Line trajectory)`, which iterates every collidable and returns the closest intersection point.

**Factory Method** — `Velocity.fromAngleAndSpeed`
`Velocity.fromAngleAndSpeed(double angle, double speed)` is a static factory that converts a polar (angle, speed) pair into Cartesian (dx, dy) components. Angle 0° points right; 90° points upward (y-axis inverted). Used everywhere a velocity is created from game logic.

---

## Geometry Engine

All geometry lives in the `geometry/` package and is used by the collision system.

**Line-segment intersection (`Line.intersectionWith`)** — 4-case dispatch:
1. Both segments vertical → `handleParallelIntersection(..., vertical=true)`: checks they share the same x, then returns a touching endpoint or null.
2. Both segments horizontal → `handleParallelIntersection(..., vertical=false)`: same logic on y.
3. Same finite slope → `handleCollinearIntersection()`: checks they lie on the same line (matching y-intercept), returns a shared endpoint or null.
4. General case → `calculateIntersection()`: solves with slope-intercept algebra, then verifies the candidate point lies within both segments using `isBetween()`.

**Epsilon floating-point comparison**
`EPSILON = 1e-5`. Every equality test uses `doubleEquals(a, b)` → `Math.abs(a - b) < EPSILON`. Applied consistently to slope comparisons, boundary checks, and point equality in `Point.equals()`.

**AABB collision detection**
`Rectangle` exposes four `Line` border segments (top, left, bottom, right). `Line.closestIntersectionToStartOfLine(Rectangle)` collects all border intersections and returns the one nearest the trajectory start. `GameEnvironment.getClosestCollision(Line trajectory)` runs this over every registered `Collidable` and returns the globally closest hit.

**5-zone paddle deflection (`Paddle.hit`)**
The paddle surface is divided into five equal regions. Region is computed as `(int)((x - paddleLeft) / (paddleWidth / 5)) + 1`, clamped to [1, 5]. Each region returns a fixed-angle velocity at the current ball speed:
- Region 1: 120° (sharp left-up)
- Region 2: 150° (soft left-up)
- Region 3: straight up — `new Velocity(dx, -|dy|)` preserving horizontal component
- Region 4: 30° (soft right-up)
- Region 5: 60° (sharp right-up)

---

## Class Structure

```
src/
├── game/
│   ├── Game.java            — initializes 800×600 window, 60 FPS loop, pause (P/Space), win/lose screens
│   ├── GameEnvironment.java — registry of Collidables; closest-collision queries
│   ├── SpriteCollection.java— registry of Sprites; fans out draw and time-step calls
│   ├── Collidable.java      — interface: getCollisionRectangle(), hit(Ball, Point, Velocity)
│   ├── Sprite.java          — interface: drawOn(DrawSurface), timePassed()
│   └── CollisionInfo.java   — value object: collision point + collidable reference
│
├── geometry/
│   ├── Point.java           — 2D point; distance(); epsilon-based equals()
│   ├── Line.java            — segment; 4-case intersectionWith(); closestIntersectionToStartOfLine()
│   ├── Rectangle.java       — AABB; four border Lines; intersectionPoints(Line)
│   └── Velocity.java        — (dx,dy) vector; fromAngleAndSpeed() factory; applyToPoint()
│
├── sprites/
│   ├── Ball.java            — moves via moveOneStep(); queries GameEnvironment; handles stuck-ball ejection
│   ├── Paddle.java          — keyboard-driven; 5-zone hit() deflection; bounded by x∈[20,780]
│   ├── Block.java           — Collidable + HitNotifier; isRemovable and isDeathRegion flags
│   ├── Background.java      — full-screen background image sprite
│   └── ScoreIndicator.java  — renders current score at top of screen
│
└── listeners/
    ├── HitListener.java         — interface: hitEvent(Block, Ball)
    ├── HitNotifier.java         — interface: addHitListener(), removeHitListener()
    ├── BlockRemover.java        — removes block from game; decrements remainingBlocks counter
    ├── BallRemover.java         — removes ball from game; decrements remainingBalls counter
    ├── ScoreTrackingListener.java — adds 5 points on each block hit
    └── Counter.java             — simple int counter with increase/decrease/getValue
```

---

## Controls

| Key | Action |
|-----|--------|
| Left Arrow | Move paddle left |
| Right Arrow | Move paddle right |
| P | Pause |
| Space | Resume (from pause) |

---

## Run

**Prerequisites:** JDK 17+

**Quick start (PowerShell script handles the classpath):**
```powershell
./run_game.ps1
```

**Manual:**
```bash
# Compile
javac -cp "biuoop-1.4.jar;src" src/Ass5Game.java -d bin

# Run
java -cp "biuoop-1.4.jar;bin" Ass5Game
```
