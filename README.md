# Arkanoid Game (Assignment 5)

A Java implementation of the classic Arkanoid game, developed as part of a programming assignment.

## Description
This project recreates the gameplay of Arkanoid/Breakout. The player controls a paddle to bounce a ball and destroy blocks. The game features:
-   Sprite-based animation system
-   Collision detection
-   Score tracking
-   Win/Loss conditions

## Project Structure
-   `src/`: Source code files.
-   `biuoop-1.4.jar`: Required library for GUI and drawing.
-   `run_game.ps1`: PowerShell script to verify and run the game easily.

## How to Run

### Prerequisites
-   Java Development Kit (JDK) installed (Java 17+ recommended).

### Using the Script (Recommended)
Run the included PowerShell script from the project root:
```powershell
./run_game.ps1
```

### Manual Compilation & Execution
If you prefer running manual commands:

**Compile:**
```bash
javac -cp "biuoop-1.4.jar;src" src/Ass5Game.java -d bin
```

**Run:**
```bash
java -cp "biuoop-1.4.jar;bin" Ass5Game
```

## Game Controls
-   **Left Arrow**: Move Paddle Left
-   **Right Arrow**: Move Paddle Right
-   **P**: Pause Game (if implemented)
-   **Space**: Resume (if implemented)

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author
Roy Yanai Carmelli
