package Maze;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        //mazeDFS(3, 3, new ArrayList<Position>());
        mazeHStart();
        //getCommand();
    }

    private static void getCommand() {
        Scanner scanner = new Scanner(System.in);

        String input[] = scanner.nextLine().split(" ");

        if (input[0].equals("gcd")) {
            System.out.println(gcd(Integer.parseInt(input[1]), Integer.parseInt(input[2])));
        }

        if (input[0].equals("factor")) {
            System.out.println(factor(Integer.parseInt(input[1])));
        }

        if (input[0].equals("simplify")) {
            System.out.println(simplify(Integer.parseInt(input[1]), Integer.parseInt(input[2])));
        }

        if (input[0].equals("power")) {
            System.out.println(power(Integer.parseInt(input[1]), Integer.parseInt(input[2])));
        }

        if (input[0].equals("fibo")) {
            System.out.println(fibo(Integer.parseInt(input[1])));
        }

    }

    private static int gcd(int a, int b) {
        if (a == b) {
            return a;
        } else if (a > b) {
            return gcd(a - b, b);
        } else {
            return gcd(a, b - a);
        }
    }

    private static int factor(int n) {
        if (n == 0) {
            return 1;
        } else return (n * factor(n - 1));
    }

    private static String simplify(int a, int b) {
        int gcd = gcd(a, b);
        return a / gcd + "/" + b / gcd;
    }

    private static int power(int a, int b) {
        if (b == 1) {
            return a;
        } else {
            b--;
            return a * power(a, b);
        }
    }

    private static int fibo(int n) {
        if (n == 1 || n == 2) {
            return 1;
        } else return fibo(n - 1) + fibo(n - 2);
    }

    public static int[][] mazeMap = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0},
            {0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0},
            {0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0},
            {0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0},
            {0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0},
            {0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1, 0},
            {0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 3, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    private static boolean mazeDFS(int a, int b, ArrayList<Position> moves) {
        // 0 = wall
        // 1 = free
        // start = [3][3]
        // 3 = goal
        System.out.println("maze(" + a + ", " + b + ")");

        Position thisPos = new Position(a, b);
        moves.add(thisPos);
        //if goal return true
        if (mazeMap[a][b] == 3) {
            System.out.println("done path:");
            for (Position pos : moves) {
                System.out.println("(" + pos.x + ", " + pos.y + ")");
            }
            for (int i = 0; i < mazeMap.length; i++) {
                for (int j = 0; j < mazeMap[0].length; j++) {
                    System.out.print(mazeMap[i][j]);
                }
                System.out.print("\n");
            }
            return true;
        }
        boolean ok;
        //for each vertice maze(vertice)

        if (b < mazeMap[0].length && mazeMap[a][b + 1] != 0 && !mazeIsVisited(a, b + 1, moves)) {
            ok = mazeDFS(a, b + 1, moves);
            if (ok) {
                return true;
            }
        }
        if (a < mazeMap.length && mazeMap[a + 1][b] != 0 && !mazeIsVisited(a + 1, b, moves)) {
            ok = mazeDFS(a + 1, b, moves);
            if (ok) {
                return true;
            }
            ;
        }
        if (a > 0 && mazeMap[a - 1][b] != 0 && !mazeIsVisited(a - 1, b, moves)) {
            ok = mazeDFS(a - 1, b, moves);
            if (ok) {
                return true;
            }
        }
        if (b > 0 && mazeMap[a][b - 1] != 0 && !mazeIsVisited(a, b - 1, moves)) {
            ok = mazeDFS(a, b - 1, moves);
            if (ok) {
                return true;
            }
        }
        //if no other moves
        moves.remove(thisPos);
        System.out.println("backtrack");
        return false;
    }

    private static boolean mazeIsVisited(int a, int b, ArrayList<Position> moves) {
        for (Position pos : moves) {
            if (pos.x == a && pos.y == b) {
                return true;
            }
        }
        return false;
    }

    public static Node[][] mazeNodes = new Node[mazeMap.length][mazeMap[0].length];
    public static ArrayList<Node> pQueue = new ArrayList<Node>();
    private static void mazeHStart(){
        //build maze
        for (int i = 0; i < mazeMap.length; i++) {
            for (int j = 0; j < mazeMap[0].length; j++) {
                mazeNodes[i][j] = new Node(i, j, mazeMap[i][j]);
            }
        }
        mazeAStar();
        //mazeHeuristic(4, 4, 0);
    }

    private static void mazeAStar(){
        //choose and setup start node
        Node current = mazeNodes[3][3];
        current.visited = true;
        current.weight = 0;
        current.h = calcHeuristic(3,3);
        //create offsets for the loop
        int[][] offsets = {{0, 1},{1,0},{0,-1},{-1, 0}};
        Node temp;
        //while not goal
        while(current.type != 3){
            //for each vertice (up,down,left,right)
            for (int i = 0; i < 4; i++) {
                //calc coordinates of temp to test
                int a = current.x + offsets[i][0];
                int b = current.y + offsets[i][1];
                System.out.println("offsets:("+a+", "+b+")");
                if(a>=0 && a < mazeNodes.length && b>=0 && b < mazeNodes[a].length){
                    //if coordinates are "doable"
                    temp = mazeNodes[a][b];
                    if(temp.type != 0){
                        //if not a wall
                        if(!temp.visited){
                            pQueue.add(temp);
                            temp.setH(calcHeuristic(a,b));
                            temp.visited = true;
                        }
                        if(temp.weight > current.weight + 1){
                            temp.weight = current.weight + 1;
                            temp.connect = current;
                        }
                    }

                }
            }
            Node lowestNode = pQueue.get(0);
            for (Node x : pQueue) {
                if(x.priority < lowestNode.priority){
                    lowestNode = x;
                }
            }
            current = lowestNode;
        }

        while(current != null){
            System.out.println("("+current.x+", "+current.y+")");
            current = current.connect;
        }
    }



    private static boolean mazeHeuristic(int a, int b, int pathWeight) {
        if (mazeNodes[a][b].type == 3) {
            System.out.println("goal:"+ pathWeight+ "(" + a + ", " + b + ")");
            return true;
        }
        Node temp = null;
        mazeNodes[a][b].visited = true;
        //down
        if (a < mazeNodes.length && mazeNodes[a + 1][b].type != 0 && !mazeNodes[a + 1][b].visited) {
            temp = mazeNodes[a+1][b];
            if(temp.h == 1000){
                temp.setH(calcHeuristic(a+1, b));
                pQueue.add(temp);
            }
            if(temp.weight > pathWeight+1){
                temp.setWeight(pathWeight+1);
            }
        }
        //right
        if (a < mazeNodes.length && mazeNodes[a][b+1].type != 0 && !mazeNodes[a][b+1].visited) {
            temp = mazeNodes[a][b+1];
            if(temp.h == 1000){
                temp.setH(calcHeuristic(a, b+1));
                pQueue.add(temp);
            }
            if(temp.weight > pathWeight+1){
                temp.setWeight(pathWeight+1);
            }
        }
        //up
        if (a > 0 && mazeNodes[a - 1][b].type != 0 && !mazeNodes[a-1][b].visited) {
            temp = mazeNodes[a-1][b];
            if(temp.h == 1000){
                temp.setH(calcHeuristic(a-1, b));
                pQueue.add(temp);
            }
            if(temp.weight > pathWeight+1){
                temp.setWeight(pathWeight+1);
            }
        }
        //left;
        if (b > 0 && mazeNodes[a][b-1].type != 0 && !mazeNodes[a][b-1].visited) {
            temp = mazeNodes[a][b-1];
            if(temp.h == 1000){
                temp.setH(calcHeuristic(a, b-1));
                pQueue.add(temp);
            }
            if(temp.weight > pathWeight+1){
                temp.setWeight(pathWeight+1);
            }
        }
        temp = pQueue.get(0);
        for (Node temp1 : pQueue) {
            if (temp1.priority < temp.priority) {
                temp = temp1;
            }
        }
        pQueue.remove(temp);
        if(mazeHeuristic(temp.x, temp.y, temp.weight)){
            System.out.println("("+a+", "+b+")");
            return true;
        }
        else return false;

    }
    private static int calcHeuristic(int a, int b){
        int xGoal = 8;
        int yGoal = 11;
        int xDist = xGoal - a;
        int yDist = yGoal - b;
        if(xDist < 0){
            xDist = -xDist;
        }
        if(yDist < 0){
            yDist = -yDist;
        }
        return xDist + yDist;
    }
}

class Node{
    boolean visited = false;
    int x, y, type, h;
    int weight;
    int priority; //lowest = best
    Node connect;
    public Node(int x, int y, int type){
        this.x = x;
        this.y = y;
        this.type = type;
        h = 1000;
        weight= 1000;
        priority = h + weight;
        connect = null;
    }
    public void setH(int h) {
        this.h = h;
        this.priority = weight + h;
    }
    public void setWeight(int weight) {
        this.weight = weight;
        this.priority = weight + h;
    }
}
class Position{
    int x,y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
