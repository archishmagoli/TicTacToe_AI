package tictactoe;
import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] controlSequence;
        String[] inputs = new String[]{"easy", "medium", "hard", "user"};

        while (true) {
            System.out.print("Input command: ");
            String controls = scanner.nextLine();
            controlSequence = controls.split(" ");

            if (controlSequence[0].equals("exit")) {
                break;
            }


            if (controlSequence.length == 3) {
                if (controlSequence[0].equals("start")) {
                    if (Arrays.asList(inputs).contains(controlSequence[1])) {
                        if (Arrays.asList(inputs).contains(controlSequence[2])) {
                            char[][] board = new char[3][3];
                            int numSpaces = board.length * board[0].length;

                            // Fill board with empty spaces
                            for (int i = 0; i < board.length; i++) {
                                for (int j = 0; j < board[0].length; j++) {
                                    board[i][j] = '_';
                                }
                            }

                            printBoard(board);

                            String finalStatus = "Game not finished";

                            while (finalStatus.equals("Game not finished")) {
                                if (controlSequence[1].equals("user")) {
                                    board = userMove(board, 'X', scanner);
                                } else {
                                    board = computerMove(board, 'X', controlSequence[1], numSpaces);
                                }

                                numSpaces -= 1;
                                finalStatus = analyzeBoard(board, numSpaces);

                                if (!finalStatus.equals("Game not finished")) {
                                    break;
                                } else {
                                    if (controlSequence[2].equals("user")) {
                                        board = userMove(board, 'O', scanner);
                                    } else {
                                        board = computerMove(board, 'O', controlSequence[2], numSpaces);
                                    }

                                    finalStatus = analyzeBoard(board, numSpaces);
                                    if (!finalStatus.equals("Game not finished")) {
                                        break;
                                    }
                                }
                                numSpaces -= 1;
                            }

                            System.out.println(finalStatus);
                        }
                    }
                }
            } else {
                System.out.println("Bad parameters!");
            }
        }
    }

    private static char[][] computerMove(char[][] board, char player, String difficulty, int numSpaces) {
        if (difficulty.equals("easy")) {
            System.out.println("Making move level \"easy\"");
            computerMoveRandom(board, player);
        } else if (difficulty.equals("medium")) {
            System.out.println("Making move level \"medium\"");

            Object[] verticalStatus = checkVerticalStatus(board, 2);
            Object[] horizontalStatus = checkHorizontalStatus(board, 2);
            Object[] diagonalStatus = checkDiagonalStatus(board); // Always threshold 2

            if (verticalStatus.length > 1) {
                // System.out.println(Arrays.toString(verticalStatus));
                board[(int) verticalStatus[1]][(int) verticalStatus[2]] = player;
                printBoard(board);
                return board;
            }

            if (horizontalStatus.length > 1) {
                // System.out.println(Arrays.toString(horizontalStatus));
                board[(int) horizontalStatus[1]][(int) horizontalStatus[2]] = player;
                printBoard(board);
                return board;
            }

            if (diagonalStatus.length > 1) {
                // System.out.println(Arrays.toString(diagonalStatus));
                board[(int) diagonalStatus[1]][(int) diagonalStatus[2]] = player;
                printBoard(board);
                return board;
            }

            return computerMoveRandom(board, player);
        } else if (difficulty.equals("hard")) {
            System.out.println("Making move level \"hard\"");
            board = hardMoveController(board, player, numSpaces);
            printBoard(board);
            return board;
        }

        return board;
    }

    private static char[][] hardMoveController(char[][] board, char player, int numSpaces) {
        int bestScore = player == 'X' ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int[] bestMove = new int[2];

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (board[x][y] == '_') {
                    board[x][y] = player;
                    char nextPlayer = player == 'X' ? 'O' : 'X';
                    int score = minimax(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, nextPlayer == 'X', numSpaces);
                    board[x][y] = '_';
                    if (player == 'X') {
                        if (score > bestScore) { // Meaning result has been maximized
                            bestScore = score;
                            bestMove = new int[]{x, y};
                        }
                    } else if (player == 'O') {
                        if (score < bestScore) {
                            bestScore = score;
                            bestMove = new int[]{x, y};
                        }
                    }
                }
            }
        }
        
        board[bestMove[0]][bestMove[1]] = player;
        return board;
    }

    private static int minimax(char[][] board, int depth, int alpha, int beta, boolean isMaximizingPlayer, int numSpaces) {
        int result = gameState(board, numSpaces);

        // Terminating condition - game has ended (either in a win or draw)
        if (result > -2) {
            return result;
        }

        int bestScore = isMaximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        char player = isMaximizingPlayer ? 'X' : 'O';

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                // Check if spot is empty
                if (board[x][y] == '_') {
                    board[x][y] = player;
                    int score = minimax(board, depth + 1, alpha, beta, !isMaximizingPlayer, numSpaces - 1); // Recursive call
                    board[x][y] = '_';

                    if (isMaximizingPlayer) {
                        bestScore = Math.max(score, bestScore);
                        alpha = Math.max(score, alpha);
                        if (beta <= alpha) {
                            break;
                        }
                    } else {
                        bestScore = Math.min(score, bestScore);
                        beta = Math.min(score, beta);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
        }

        return bestScore;
    }

    private static char[][] userMove(char[][] board, char player, Scanner scanner) {
        while (true) {
            System.out.print("Enter the coordinates: ");
            String coordinateString = scanner.nextLine();
            coordinateString = coordinateString.replace(" ", "");

            if (!coordinateString.matches("[0-9]+\\s*")) {
                System.out.println("You should enter numbers!");
                continue;
            } else if (coordinateString.length() > 2) {
                System.out.println("Invalid coordinates! Try again!");
                continue;
            }

            int row = Character.getNumericValue(coordinateString.charAt(0));
            int column = Character.getNumericValue(coordinateString.charAt(1));

            if (row > board.length || column > board[0].length) {
                System.out.println("Coordinates should be from 1 to 3!");
            } else if (board[row - 1][column - 1] == 'X' || board[row - 1][column - 1] == 'O') {
                System.out.println("This cell is occupied! Choose another one!");
            } else {
                board[row - 1][column - 1] = player;
                printBoard(board);
                break;
            }
        }

        return board;
    }

    private static char[][] computerMoveRandom(char[][] board, char player) {
        int row = new Random().nextInt(3);
        int column = new Random().nextInt(3);

        while (board[row][column] == 'X' || board[row][column] == 'O') {
            row = new Random().nextInt(3);
            column = new Random().nextInt(3);
        }

        board[row][column] = player;
        printBoard(board);
        return board;
    }

    private static void printBoard(char[][] board) {
        StringBuilder boardTemplate = new StringBuilder("---------");

        for (char[] chars : board) {
            boardTemplate.append("\n| ");
            for (char aChar : chars) {
                if (aChar == 'X' || aChar == 'O') {
                    boardTemplate.append(aChar).append(" ");
                } else {
                    boardTemplate.append("  ");
                }
            }
            boardTemplate.append("|");
        }

        boardTemplate.append("\n---------");
        System.out.println(boardTemplate);
    }

    private static String analyzeBoard(char[][] board, int numSpaces) {
        if (!isDiagonalVictory(board)[0].equals("")) {
            return (String) isDiagonalVictory(board)[0];
        } else if (!isVerticalVictory(board).equals("")) {
            return isVerticalVictory(board);
        } else if (!isHorizontalVictory(board).equals("")) {
            return isHorizontalVictory(board);
        } else if (numSpaces - 1 > 0) {
            return "Game not finished";
        } else {
            return "Draw";
        }
    }

    private static int gameState(char[][] board, int numSpaces) {
        if (isDiagonalVictory(board).length > 1) {
            return (int) isDiagonalVictory(board)[1];
        } else if (checkHorizontalStatus(board, 3).length > 1) {
            return (int) checkHorizontalStatus(board, 3)[1];
        } else if (checkVerticalStatus(board, 3).length > 1) {
            return (int) checkVerticalStatus(board, 3)[1];
        } else if (numSpaces - 1 == 0) {
            return 0;
        } else {
            return -2;
        }
    }

    private static String isHorizontalVictory(char[][] board) {
        return (String) checkHorizontalStatus(board, 3)[0];
    }

    private static Object[] checkHorizontalStatus(char[][] board, int sameThreshold) {
        for (int x = 0; x < board.length; x++) {
            int xCount = 0;
            int oCount = 0;
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y] == 'X') {
                    xCount++;
                } else if (board[x][y] == 'O') {
                    oCount++;
                }

                if (xCount == sameThreshold || oCount == sameThreshold) {
                    if (sameThreshold == 2) {
                        for (int yCoor = 0; yCoor < board[x].length; yCoor++) {
                            if (board[x][yCoor] == '_') {
                                return new Object[]{board[x][y], x, yCoor};
                            }
                        }
                        break;
                    } else if (sameThreshold == 3) {
                        return new Object[]{String.format("%c wins", board[x][y]), board[x][y] == 'X' ? 1 : -1};
                    }
                }
            }
        }

        return new Object[]{""};
    }

    private static String isVerticalVictory(char[][] board) {
        return (String) checkVerticalStatus(board, 3)[0];
    }

    private static Object[] checkVerticalStatus(char[][] board, int sameThreshold) {
        for (int y = 0; y < board[0].length; y++) {
            int xCount = 0;
            int oCount = 0;
            for (int x = 0; x < board.length; x++) {
                if (board[x][y] == 'X') {
                    xCount++;
                } else if (board[x][y] == 'O') {
                    oCount++;
                }

                if (xCount == sameThreshold || oCount == sameThreshold) {
                    if (sameThreshold == 2) {
                        for (int xCoor = 0; xCoor < board.length; xCoor++) {
                            if (board[xCoor][y] == '_') {
                                return new Object[]{board[x][y], xCoor, y};
                            }
                        }
                        break;
                    } else if (sameThreshold == 3) {
                        return new Object[]{String.format("%c wins", board[x][y]), board[x][y] == 'X' ? 1 : -1};
                    }
                }
            }
        }
        return new Object[]{""};
    }


    private static Object[] checkDiagonalStatus(char[][] board) {
        if (board[0][0] != '_') {
            if (board[1][1] == board[0][0] && board[2][2] == '_') {
                return new Object[]{board[0][0], 2, 2};
            } else if (board[2][2] == board[0][0] && board[1][1] == '_') {
                return new Object[]{board[0][0], 1, 1};
            }
        } else if (board[1][1] != '_') {
            if (board[2][2] == board[1][1]) {
                return new Object[]{board[1][1], 0, 0};
            }
        }

        if (board[0][2] != '_') {
            if (board[1][1] == board[0][2] && board[2][0] == '_') {
                return new Object[]{board[1][1], 2, 0};
            } else if (board[2][0] == board[0][2] && board[1][1] == '_') {
                return new Object[]{board[2][0], 1, 1};
            }
        } else if (board[1][1] != '_') {
            if (board[2][0] == board[1][1]) {
                return new Object[]{board[1][1], 0, 2};
            }
        }

        return new Object[]{""};
    }

    private static Object[] isDiagonalVictory(char[][] board) {
        if (board[0][0] != '_' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return new Object[]{String.format("%c wins", board[1][1]), board[1][1] == 'X' ? 1 : -1};
        } else if (board[1][1] != '_' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return new Object[]{String.format("%c wins", board[1][1]), board[1][1] == 'X' ? 1 : -1};
        } else {
            return new Object[]{""};
        }
    }
}
