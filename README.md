# 🧱 Arkanoid Game (Assignment 5)

> A fast-paced, object-oriented implementation of the classic arcade game Arkanoid.

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![License](https://img.shields.io/badge/License-MIT-blue)
![Course](https://img.shields.io/badge/Course-OOP-green)

## 🎮 Overview

Welcome to my Java implementation of Arkanoid! This project was built as part of an extensive Object-Oriented Programming (OOP) assignment. It features a custom game engine, sprite management, collision detection, and a scoring system.

**Key Features:**
*   **Smooth Animation Loop**: Custom-built game loop running at 60 FPS.
*   **Collision Detection**: Precise physics for ball-block and ball-paddle interactions.
*   **Dynamic Gameplay**: Colored blocks to destroy, with "Death Regions" to avoid.
*   **Pause Feature**: Need a break? Just hit `P`.

## 👾 The Lore (Fun Fact)
You might notice something special about the game's background...

> 🎨 **Behind the Scenes**: The background image is an AI-generated masterpiece featuring the **Mighty Hero of OOP, Zvika Berger**, in an epic attempt to explain complex software design patterns to our lecturer, **Dr. Marina Kogan**. Does he succeed? Only by clearing all the blocks can you find out!

## 🕹️ Game Controls

| Key | Action |
| :---: | :--- |
| **⬅️ Left Arrow** | Move Paddle Left |
| **➡️ Right Arrow** | Move Paddle Right |
| **P** | Pause Game |
| **Space** | Resume Game (from Pause) |

## 🚀 How to Run

### Prerequisites
*   **Java Development Kit (JDK)**: Version 17 or higher.

### Quick Start (Recommended)
We've included a handy script to handle the classpath for you. Just run:

```powershell
./run_game.ps1
```

### Manual Run
If you prefer the command line:

1.  **Compile:**
    ```bash
    javac -cp "biuoop-1.4.jar;src" src/Ass5Game.java -d bin
    ```
2.  **Run:**
    ```bash
    java -cp "biuoop-1.4.jar;bin" Ass5Game
    ```

## 📂 Project Structure
*   `src/`: Main source code (Game logic, Sprites, Geometry, Listeners).
*   `biuoop-1.4.jar`: The graphics library used for the GUI.
*   `run_game.ps1`: Automation script for easy execution.

## 📜 License
This project is open-source and available under the [MIT License](LICENSE).

---
*Created by Roy Yanai Carmelli*
