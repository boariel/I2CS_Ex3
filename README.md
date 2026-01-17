# I2CS_Ex3
This repository is my implementation of Ex3 in the intro to computer science course.
The goal in this assignment is to develop an algorithm capable of winning the pac man game
and also to design and develop the pacman game.
In pacman you move through a 2d maze trying to collect all the orbs of the map to win.
In the maze there are ghosts that if you bump into them you lose, but if you eat a green power-up orb
you can eat the ghosts for a period of time to make them not dangerous for a certain amount of time
## My pacman algorithm
my algorithm is a simple algorithm that is focused on three states:
1. when close ghost is eadible.
2. when close to a ghost.
3. when not close to any ghosts.
When in the first state the algorithm tries to eat the closest ghost.
When in the second state the algorithm looks for the tiles furthest from as many ghosts as possible
and goes there.
When in the third state the algorithm goes to the closet orb.
