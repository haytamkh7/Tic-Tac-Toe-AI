import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Player 1: Enter your name please");
        String player1 = scanner.nextLine();
        System.out.println("Player 2: Enter your name please");
        String player2 = scanner.nextLine();

        // Initialize the game board
        char[][] board = create_board();

        // Start the game loop
        for (int i = 0; i < 9; i++) {
            boolean validMove = false;
            while (!validMove) {
                try {
                    char currentPlayer = (i % 2 == 0) ? 'X' : 'O'; // Determine the current player
                    String playerName = (i % 2 == 0) ? player1 : player2;

                    System.out.println(playerName + " move:");

                    int[] playerMove = findWinningMovesAI(board, currentPlayer);
                    board = make_move(board, playerMove[0], playerMove[1], currentPlayer);

                    // If the move was successful, mark it as valid to exit the loop
                    validMove = true;
                    print_board(board);

                    // Check if there is a winner or the game is a draw (i == 8 signifies all spots on the board have been filled)
                    char winner = getWinner(board);
                    if (winner != '0' || i == 8) {
                        if (winner == 'X') {
                            System.out.println("Congratulations " + player1 + " You Won!");
                            return;
                        } else if (winner == 'O') {
                            System.out.println("Congratulations " + player2 + " You Won!");
                            return;
                        } else if (i == 8) {
                            System.out.println("Game Over. No winner.");
                            return;
                        }
                    }
                } catch (IllegalArgumentException e) {
                    // If the move was invalid, print the error message and repeat the loop
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static int[] findWinningMovesAI(char[][] board, char playerSign) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == '\u0000') {
                    char[][] tempBoard = new char[3][3];
                    for (int m = 0; m < board.length; m++) {
                        for (int n = 0; n < board[m].length; n++) {
                            tempBoard[m][n] = board[m][n];
                        }
                    }
                    tempBoard[i][j] = playerSign;
                    if (getWinner(tempBoard) == playerSign) {
                        return new int[]{i, j};
                    }
                }
            }
        }
        
        int[] randomMove = randomAI(board);
        if (randomMove != null) {
            return randomMove;
        } else {
            throw new IllegalArgumentException("No valid moves left!");
        }
    }

    public static int[] randomAI(char[][] board) {
        ArrayList<int[]> emptySpots = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == '\u0000') {
                    emptySpots.add(new int[]{i, j});
                }
            }
        }
        if(!emptySpots.isEmpty()) {
            Random rand = new Random();
            int[] randomSpot = emptySpots.get(rand.nextInt(emptySpots.size()));
            return randomSpot;
        }
        return null;
    }

    public static char[][] create_board() {
        char[][] board = new char[3][3];
        return board;
    }

    public static char getWinner(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            String horizontalLine = "";
            for (int j = 0; j < board[i].length; j++)
                horizontalLine += board[i][j];
            if (horizontalLine.equals("OOO") || horizontalLine.equals("XXX"))
                return horizontalLine.charAt(0);
        }
        
        for (int i = 0; i < board.length; i++) {
            String verticalLine = "";
            for (int j = 0; j < board.length; j++)
                verticalLine += board[j][i];
            if (verticalLine.equals("OOO") || verticalLine.equals("XXX"))
                return verticalLine.charAt(0);
        }
        
        String topLeftBottomRightDiagonalLine = "";
        String topRightBottomLeftDiagonalLine = "";
        for (int i = 0; i < board.length; i++) {
            topLeftBottomRightDiagonalLine += board[i][i];
            topRightBottomLeftDiagonalLine += board[i][board[i].length - 1 - i];
        }
        if (topLeftBottomRightDiagonalLine.equals("OOO") || topLeftBottomRightDiagonalLine.equals("XXX"))
            return topLeftBottomRightDiagonalLine.charAt(0);
        if (topRightBottomLeftDiagonalLine.equals("OOO") || topRightBottomLeftDiagonalLine.equals("XXX"))
            return topRightBottomLeftDiagonalLine.charAt(0);
        return '0';
    }

    public static char[][] make_move(char[][] board, int X, int Y, char playerSign) {
        if (board[X][Y] != '\u0000')
            throw new IllegalArgumentException("The cell at [" + X + ", " + Y + "]" + " is already taken!");
        board[X][Y] = playerSign;
        return board;
    }

    public static void print_board(char[][] board) {
        int j;
        System.out.println("   0 1 2");
        System.out.println("   - - -");
        for (int i = 0; i < board.length; i++) {
            System.out.print(i + " ");
            System.out.print("|");
            for (j = 0; j < board[i].length - 1; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.print(board[i][j]);
            System.out.print("|");
            System.out.println();
        }
        System.out.println("   - - -");
    }
}
