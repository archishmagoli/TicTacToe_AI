# Tic-Tac-Toe with AI
This project is the classic Tic-Tac-Toe game but with a twist: you can play against an AI agent! It's currently playable on the command line. 

## Game Logic
There are three difficulty levels the user can choose from

### Easy
The AI agent makes random moves at this level.

### Medium
The AI agent follows the logic below ~
- If it already has two in a row and can win in one more move, it does so.
- If its opponent can win in one more move, it makes the move necessary to *block* its opponent.
- Otherwise, it makes a random move.

### Hard
In this level, the AI agent uses the **minimax algorithm** with **alpha-beta pruning** to determine the most optimal move, regardless of whether it plays 'X' or 'O'.

## Game Parameters
The initial ```Input command``` takes three parameters:
- ```start```: starts the game, obviously
- The first player: if you (the user) would like to start, enter ```user```. Otherwise, specify the difficulty level of the (first) AI agent.
- The second player: if you (the user) would like to play against yourself, enter ```user```. Otherwise, specify the difficulty level of the (second) AI agent.

After you play a game, you have the option to play another game with different parameters or exit the program with the ```exit``` command.

## Example
Here's an example of the game being played!
```
Input command: > start hard user
Making move level "hard"
---------
|       |
| X     |
|       |
---------
Enter the coordinates: > 2 2
---------
|       |
| X O   |
|       |
---------
Making move level "hard"
---------
|   X   |
| X O   |
|       |
---------
Enter the coordinates: > 3 2
---------
|   X   |
| X O   |
|   O   |
---------
Making move level "hard"
---------
| X X   |
| X O   |
|   O   |
---------
Enter the coordinates: > 3 1
---------
| X X   |
| X O   |
| O O   |
---------
Making move level "hard"
---------
| X X X |
| X O   |
| O O   |
---------
X wins

Input command: > exit
```
