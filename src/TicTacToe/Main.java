package TicTacToe;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    //public variable
    public static char[][] board = new char[3][3];
    public static final int[][] v = {{3,2,3},
                                    {2,4,2},
                                    {3,2,3} };
    public static final int maxDepth = 8;

    public static char player, ai;

    public static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("welcome, choose side: x/o");
        char input = scanner.nextLine().charAt(0);
        if(input == 'x'){
            player = 'x';
            ai = 'o';
        }
        else if(input == 'o'){
            player = 'o';
            ai = 'x';
        }
        playGame();
    }
    public static void playGame(){
        reset();
        System.out.println("new game initialized. Player = "+ player + ", AI = "+ ai);
        printBoard(board);
        while(true){
            System.out.println("enter move: x y");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            board[x][y] = player;
            printBoard(board);
            if(isGameWon(board) == player){
                System.out.println("HUMAN WINS!");
                break;
            }
            Move move = getBestMove(cloneArray(board));
            if(move == null){
                System.out.println("TIE, GAME OVER");
                break;
            }
            board[move.x][move.y] = ai;
            printBoard(board);
            if(isGameWon(board) == ai){
                System.out.println("AI WINS!");
                break;
            }
        }
        System.out.println("Want to play again? y/n");
        scanner.nextLine();
        String input = scanner.nextLine();
        if (input.equals("y")){
            playGame();
        }
    }

    public static char[][] cloneArray(char[][] input){
        char[][] copy = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                copy[i][j] = input[i][j];
            }
        }
        return copy;
    }

    public static char isGameWon(char[][] tempBoard){
        for (int i = 0; i < 3; i++) {
            //rækker
            if(tempBoard[i][0] == tempBoard[i][1] && tempBoard[i][0] == tempBoard[i][2]){
                return  tempBoard[i][0];
            }
            //columns
            if(tempBoard[0][i] == tempBoard[1][i] && tempBoard[0][i] == tempBoard[2][i]){
                return  tempBoard[0][i];
            }
        }
        if(tempBoard[0][0] == tempBoard[1][1] && tempBoard[0][0] == tempBoard[2][2]){
            return tempBoard[0][0];
        }
        if(tempBoard[2][0] == tempBoard[1][1] && tempBoard[2][0] == tempBoard[0][2]){
            return tempBoard[2][0];
        }
        return ' ';
    }
    public static Move getBestMove(char[][] state){
        ArrayList<Move> moves = getPossibleMoves(state);
        if(moves.isEmpty()){
            return null;
        }
        Move bestMove = moves.get(0);
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            char[][] moveState = cloneArray(state);
            moveState[move.x][move.y] = ai;
            move.value = alphaBeta(moveState, Integer.MIN_VALUE, Integer.MAX_VALUE, 1, true);
            System.out.println("Move: (" + move.x+", "+move.y+") scored: "+move.value);
            if(move.value > bestMove.value){
                bestMove = move;
            }
        }
        System.out.println("Best move: ("+ bestMove.x+", "+bestMove.y+") scored: "+bestMove.value);
        return bestMove;
    }
    public static ArrayList<Move> getPossibleMoves(char[][] state){
        ArrayList<Move> moves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(state[i][j] == ' '){
                    moves.add(new Move(i, j));
                }
            }
        }
        return moves;
    }
    public static int alphaBeta(char[][] state, int a, int b, int d, boolean isMin){
        char winner = isGameWon(state);
        if (winner == ai) {
            //computer wins
            return Integer.MAX_VALUE;
        }
        else if(winner == player){
            //player wins
            return Integer.MIN_VALUE;
        }
        ArrayList<Move> possibleMoves = getPossibleMoves(state);
        //if no moves it is tie
        if(possibleMoves.size() == 0){
            return 0;
        }
        //game still has moves
        if(d == maxDepth){
            //if leafnode
            return getStaticVal(state);
        }

        int i = 0;
        Move move = possibleMoves.get(0);
        if(!isMin){
            //MAXIMIZE
            while(a < b && i < possibleMoves.size()) {
                move = possibleMoves.get(i++);
                char[][] temp = cloneArray(state);
                temp[move.x][move.y] = ai;
                int value = alphaBeta(temp, a, b, d+1, true);
                if (value > a) {
                    a = value;
                }
            }
            return a;
        }
        else{
            //MINIMIZE
            while(a < b && i < possibleMoves.size()){
                move = possibleMoves.get(i++);
                char[][] temp = cloneArray(state);
                temp[move.x][move.y] = player;
                int value = alphaBeta(temp, a, b, d+1, false);
                if (value < b) {
                    b = value;
                }
            }
            return b;
        }
    }

    public static int getStaticVal(char[][] tempBoard){
        //computer has X
        //so X value is added and O values are subtracted
        int value = 0;
        char gameWinner = isGameWon(tempBoard);
        if(gameWinner == ai){
            return Integer.MAX_VALUE;
        }
        else if(gameWinner == player){
            return Integer.MIN_VALUE;
        }
        int freeSpotCounter = 0; //count empty tiles to check for tie
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(tempBoard[i][j] == ai){
                    value += v[i][j];
                }
                else if(tempBoard[i][j] == player){
                    value -= v[i][j];
                }
                else {
                    freeSpotCounter++;
                }
            }
        }
        if(freeSpotCounter == 0){
            //tie
            value = 0;
        }
        return value;
    }
    public static void reset(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }
    public static void printBoard(char[][] tempBoard){
        for (int i = 0; i < 3; i++) {
            System.out.print("|");
            for (int j = 0; j < 3; j++) {
                System.out.print(tempBoard[i][j] + "|");
            }
            System.out.println("");
        }
    }
}
class Move{
    int x, y, value;
    public Move(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
