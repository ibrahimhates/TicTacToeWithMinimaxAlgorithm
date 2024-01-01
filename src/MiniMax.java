import java.util.Objects;

public class MiniMax {
    private static String ai = "X";
    private static String human = "O";
    public static void playAI(String[][] board){
        bestMoveAI(board);
    }

    private static void bestMoveAI(String[][] board){
        int[] bestMove = new int[0];
        int bestScore = Integer.MIN_VALUE;
        String[][] tempBoard = board.clone();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(board[i][j] == " "){
                    tempBoard[i][j] = ai;
                    int score = minimax(tempBoard,0,false);
                    tempBoard[i][j] = " ";
                    if(score > bestScore){
                        bestScore = score;
                        bestMove = new int[]{i, j};
                    }
                }
            }
        }
        board[bestMove[0]][bestMove[1]] = ai;
    }

    private static int minimax(String[][] board,int depth,boolean isMaximizing) {
        String result = checkWinner(board);
        if(result != null) {
            return TicTacToe.scores.get(result);
        }

        if(isMaximizing){
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if(Objects.equals(board[i][j], " ")){
                        board[i][j] = ai;
                        int score = minimax(board,depth+1,false);
                        board[i][j] = " ";
                        bestScore = Integer.max(score,bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if(Objects.equals(board[i][j], " ")){
                        board[i][j] = human;
                        int score = minimax(board,depth+1,true);
                        board[i][j] = " ";
                        bestScore = Integer.min(score,bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    public static String checkWinner(String[][] board) {
        String winner = null;

        // horizontal
        for (int i = 0; i < 3; i++) {
            if (equals3(board[i][0], board[i][1], board[i][2])) {
                winner = board[i][0];
            }
        }

        // Vertical
        for (int i = 0; i < 3; i++) {
            if (equals3(board[0][i], board[1][i], board[2][i])) {
                winner = board[0][i];
            }
        }

        // Diagonal
        if (equals3(board[0][0], board[1][1], board[2][2])) {
            winner = board[0][0];
        }
        if (equals3(board[2][0], board[1][1], board[0][2])) {
            winner = board[2][0];
        }

        int openSpots = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (Objects.equals(board[i][j], " ")) {
                    openSpots++;
                }
            }
        }

        if (winner == null && openSpots == 0) {
            return "tie";
        } else {
            return winner;
        }
    }

    private static boolean equals3(String x1,String x2,String x3){
        return !Objects.equals(x1, " ")
                && Objects.equals(x1, x2)
                && Objects.equals(x2, x3);
    }
}
