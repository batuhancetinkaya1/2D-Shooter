# 2D-Shooter

Welcome to the **2D-Shooter** project! This repository contains a Java-based 2D shooter game developed using the libGDX framework. The game features multiple enemy types, a boost system, and includes menus for leaderboard and pause functionalities.

## Table of Contents
- [Features](#features)
- [Installation](#installation)
- [Gameplay](#gameplay)
- [Boost System](#boost-system)
- [Project Structure](#project-structure)
- [Known Issues](#known-issues)
- [Future Improvements](#future-improvements)
- [License](#license)

---

## Features
- **Three Distinct Enemy Types**: Each with unique behaviors and challenges.
- **Boost Mechanic**: Allows players to temporarily enhance their abilities.
- **Leaderboard Menu**: Displays high scores to encourage replayability.
- **Pause Functionality**: Players can pause and resume the game at their convenience.
- **Wave Timer**: Each wave has a time limit to increase the challenge.
- **Object-Oriented Programming Principles**: The game is structured to demonstrate OOP basics.
- **Cross-Platform Support**: Playable on both desktop and web platforms.

## Installation
1. **Clone the repository**:
   ```bash
   git clone https://github.com/batuhancetinkaya1/2D-Shooter.git
   ```
2. **Open the project** in your preferred Java IDE.
3. **Build and run the project** using Gradle:
   ```bash
   ./gradlew desktop:run
   ```

## Gameplay
The objective is to survive against waves of enemies by shooting them down. Utilize the boost mechanic strategically to gain an advantage.

### Controls
- **Movement**: [Define movement controls, e.g., Arrow keys or WASD]
- **Shoot**: [Define shooting control, e.g., Spacebar or Mouse click]
- **Boost**: [Define boost control, e.g., Shift key]
- **Pause/Resume**: [Define pause control, e.g., P key or Esc]

## Boost System
The game includes various boosts to assist the player during gameplay. Below is a list of available boosts and their effects:

1. **Speed Boost**:
   - Increases the player's movement speed for a limited time.
   - Useful for dodging enemies or repositioning quickly.

2. **Bullet Speed Boost**:
   - Increases the speed of the bullets fired by the player.
   - Ideal for hitting fast-moving enemies or targets at a distance.

3. **Bullet Rate Boost**:
   - Reduces the time between consecutive shots.
   - Enhances the player's firepower in intense situations.

4. **Health Recovery**:
   - Restores a portion of the player's health.
   - Useful for surviving longer in challenging waves.

5. **Score Boost**:
   - Grants additional points to the player's score.
   - Encourages strategic gameplay to maximize scoring potential.

6. **Time Boost**:
   - Adds extra time to the current wave.
   - Helps players manage tight time limits during challenging waves.

Boosts are scattered throughout the game and can be collected by the player. Use them wisely to maximize their effectiveness.

## Project Structure
- **core**: Contains the main game logic and shared assets.
  - **src/main/java/com/mygdx/game**: Java source files implementing game mechanics.
- **assets**: Game assets including images, sounds, and fonts.
- **desktop**: Desktop-specific launcher and configurations.
- **html**: Web platform using GWT and WebGL.
- **lwjgl3**: LWJGL3 backend for desktop applications.
- **gradle**: Gradle wrapper and build configurations.

## Future Improvements
- **Enhanced Graphics**: Improve visual elements and animations.
- **Additional Power-ups**: Introduce new power-ups to diversify gameplay.
- **More Enemy Types**: Add enemies with varied behaviors and difficulty levels.
- **Online Leaderboard**: Implement a global leaderboard for competitive play.

## License
This project is open source and available under the [MIT License](LICENSE). Feel free to use, modify, and distribute the code as per the license terms.

---

This project was created as part of my undergraduate Java course at **Istanbul Technical University (ITU)** and serves as my first game development project. Happy coding! If you encounter any issues or have suggestions, please open an issue in the repository or reach out. 🚀
