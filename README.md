<div align="center">

# Arkanoid

Arkanoid clone in Java 17 — focused on clean OOP design patterns and a custom geometry engine.

<br>

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=flat-square&logo=openjdk)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)
![Pattern](https://img.shields.io/badge/Patterns-Observer%20%7C%20Composite%20%7C%20Factory-green?style=flat-square)

</div>

---

## Table of Contents

- [Architecture](#architecture)
- [Geometry Engine](#geometry-engine)
- [Class Structure](#class-structure)
- [Controls](#controls)
- [Run](#run)

---

## Architecture

Three design patterns structure the codebase.

### Observer — `HitNotifier` / `HitListener`

`Block` implements `HitNotifier` and maintains a list of `HitListener` subscribers. On each hit it calls `hitEvent(Block, Ball)` on every registered listener.

| Listener | Behaviour |
|:---|:---|
| `BlockRemover` | Removes the block and decrements a counter |
| `BallRemover` | Removes the ball when it reaches the death region |
| `ScoreTrackingListener` | Adds 5 points per destroyed block |

### Composite — `SpriteCollection`, `GameEnvironment`

`SpriteCollection` holds all `Sprite` objects and fans out `drawAllOn()` and `notifyAllTimePassed()` calls. `GameEnvironment` holds all `Collidable` objects and exposes `getClosestCollision(Line trajectory)`, which iterates every collidable and returns the closest intersection point.

### Factory Method — `Velocity.fromAngleAndSpeed`

`Velocity.fromAngleAndSpeed(double angle, double speed)` converts a polar (angle, speed) pair into Cartesian (dx, dy) components. Angle 0° points right; 90° points upward (y-axis inverted). Used everywhere a velocity is created from game logic.

---

## Geometry Engine

All geometry lives in the `geometry/` package and drives the collision system.

### Line-segment intersection — `Line.intersectionWith`

Four-case dispatch:

| Case | Handler | Logic |
|:---|:---|:---|
| Both vertical | `handleParallelIntersection(..., true)` | Same x? Return touching endpoint or `null` |
| Both horizontal | `handleParallelIntersection(..., false)` | Same y? Return touching endpoint or `null` |
| Same finite slope | `handleCollinearIntersection()` | Same y-intercept? Return shared endpoint or `null` |
| General | `calculateIntersection()` | Slope-intercept algebra; verify point inside both segments via `isBetween()` |

### Epsilon comparison

> **`EPSILON = 1e-5`** — every equality test uses `doubleEquals(a, b)` → `Math.abs(a - b) < EPSILON`

Applied to slope comparisons, boundary checks, and `Point.equals()`.

### AABB collision detection

`Rectangle` exposes four `Line` border segments (top, left, bottom, right). `Line.closestIntersectionToStartOfLine(Rectangle)` collects all border intersections and returns the nearest one. `GameEnvironment.getClosestCollision(Line)` runs this over every registered `Collidable` and returns the globally closest hit.

### 5-zone paddle deflection — `Paddle.hit`

Region formula: `(int)((x - paddleLeft) / (paddleWidth / 5)) + 1`, clamped to `[1, 5]`.

```
←───────────────────── paddle ──────────────────────→
┌──────────┬──────────┬──────────┬──────────┬──────────┐
│    1     │    2     │    3     │    4     │    5     │
│   120°   │   150°   │   up     │   30°    │   60°    │
└──────────┴──────────┴──────────┴──────────┴──────────┘
```

| Region | Angle | Direction |
|:---:|:---:|:---|
| 1 | 120° | Sharp left-up |
| 2 | 150° | Soft left-up |
| 3 | — | Straight up — `new Velocity(dx, -\|dy\|)` preserves horizontal |
| 4 | 30° | Soft right-up |
| 5 | 60° | Sharp right-up |

---

## Class Structure

<details>
<summary><strong>Expand class tree</strong></summary>

```
src/
├── game/
│   ├── Game.java             — 800×600 window, 60 FPS loop, pause (P/Space), win/lose screens
│   ├── GameEnvironment.java  — registry of Collidables; closest-collision queries
│   ├── SpriteCollection.java — registry of Sprites; fans out draw and time-step calls
│   ├── Collidable.java       — interface: getCollisionRectangle(), hit(Ball, Point, Velocity)
│   ├── Sprite.java           — interface: drawOn(DrawSurface), timePassed()
│   └── CollisionInfo.java    — value object: collision point + collidable reference
│
├── geometry/
│   ├── Point.java            — 2D point; distance(); epsilon-based equals()
│   ├── Line.java             — segment; 4-case intersectionWith(); closestIntersectionToStartOfLine()
│   ├── Rectangle.java        — AABB; four border Lines; intersectionPoints(Line)
│   └── Velocity.java         — (dx,dy) vector; fromAngleAndSpeed() factory; applyToPoint()
│
├── sprites/
│   ├── Ball.java             — moveOneStep(); queries GameEnvironment; stuck-ball ejection
│   ├── Paddle.java           — keyboard-driven; 5-zone hit() deflection; x ∈ [20, 780]
│   ├── Block.java            — Collidable + HitNotifier; isRemovable and isDeathRegion flags
│   ├── Background.java       — full-screen background image sprite
│   └── ScoreIndicator.java   — renders current score at top of screen
│
└── listeners/
    ├── HitListener.java           — interface: hitEvent(Block, Ball)
    ├── HitNotifier.java           — interface: addHitListener(), removeHitListener()
    ├── BlockRemover.java          — removes block; decrements remainingBlocks counter
    ├── BallRemover.java           — removes ball; decrements remainingBalls counter
    ├── ScoreTrackingListener.java — adds 5 points on each block hit
    └── Counter.java               — int counter with increase/decrease/getValue
```

</details>

---

## Controls

<div align="center">

| Key | Action |
|:---:|:---|
| `←` | Move paddle left |
| `→` | Move paddle right |
| `P` | Pause |
| `Space` | Resume from pause |

</div>

---

## Run

**Prerequisites:** JDK 17+

**Quick start** — PowerShell script handles the classpath:

```powershell
./run_game.ps1
```

<details>
<summary><strong>Manual</strong></summary>

```bash
# Compile
javac -cp "biuoop-1.4.jar;src" src/Ass5Game.java -d bin

# Run
java -cp "biuoop-1.4.jar;bin" Ass5Game
```

</details>
