# I2CS_Ex3
This repository is my implementation of Ex3 in the intro to computer science course.
The goal in this assignment is to develop an algorithm capable of winning the pac man game
and also to design and develop the pacman game.
In pacman you move through a 2d maze trying to collect all the orbs of the map to win.
In the maze there are ghosts that if you bump into them you lose, but if you eat a green power-up orb
you can eat the ghosts for a period of time to make them not dangerous for a certain amount of time.

## My pacman algorithm
my algorithm is a simple algorithm that is focused on three states:
1. when close ghost is eadible.
2. when close to a ghost.
3. when not close to any ghosts.

When in the first state the algorithm tries to eat the closest ghost.
When in the second state the algorithm looks for the tiles furthest from as many ghosts as possible
and goes there.
When in the third state the algorithm goes to the closet orb.

## algorithm files
the files for the algorithm found in src outside of the server folder.
| File / Package | Description |
|----------------|-------------|
| `Ex3Algo.java` | A class implementing my algorithm. |
| `Ex3Main.java` | A class running the game. |
| `GameInfo.java` | A class that keeps the game settings. |
| `ManualAlgo.java` | A class used to control pacman yourself instad of the algorithm. |
| `Pixel2D.java` | An interface that represents an integer based coordinate of a 2D raster. |
| `Index2D.java` | An instance class of Pixel2D interface. |
| `Map2D.java` | An interface that represents a 2D map as a raster matrix, image or maze. |
| `Map.java` | An instance class of Map2D interface. |
| `*Test.java` | Unit tests for core functionality. |

## My pacman server-side implementation
The src file includes a folder called 'server' in which resides my implementation of the server-side for pacman.
My implementation uses the class 'StdDraw' to show a pacman game on screen including a menu for the the and the game itself.
Here is a short video of me playing pacman through my implementatio:
https://drive.google.com/file/d/1G3u2T0amEfFofAQpEl1hpaR09AN9vUcC/view?usp=sharing

## Server-side files
| File / Package | Description |
|----------------|-------------|
| `Game.java` | A class keeping base values for different elements in the game. |
| `GameRenderer.java` | A class displaying elements of the game using 'StdDraw'. |
| `GhostCL.java` | Interface representing a ghost enemy in the game. |
| `MenuScreen.java` | A class used to display the game menu usind 'StdDraw'. |
| `MyGame.java` | A class representing the elements of the game like board or position. Implements Pacmangame interface. |
| `MyGhost.java` | A class representing a ghost enemy in the game. Implements GhostCL interface. |
| `PacManAlgo.java` | An interface for algorithms. |
| `PacmanGame.java` | An interface for game elements. |
| `SmartAlgo.java` | A class implementing my algorithm. |
| `ServerMain.java` | A class running the game. |
| `ServerConfig.java` | A class that keeps the game settings. |
| `ManualAlgo.java` | A class used to control pacman yourself instad of the algorithm. |
| `Pixel2D.java` | An interface that represents an integer based coordinate of a 2D raster. |
| `Index2D.java` | An instance class of Pixel2D interface. |
| `Map2D.java` | An interface that represents a 2D map as a raster matrix, image or maze. |
| `Map.java` | An instance class of Map2D interface. |
| `StdDraw.java` | A class used to help display the GUI. |

## Executables 
This repository also includes 'Ex3_2','Ex3_3' which have exacutable jar files so you can run the algorithm ('Ex3_2')
or my implementation of the game ('Ex3_3') without puting all the files in a development environment.
The jar files can be found in the release.
