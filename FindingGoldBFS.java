
import java.util.Arrays;
import java.util.Scanner;

public class FindingGoldBFS {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int row = scanner.nextInt();
        int column = scanner.nextInt();
        scanner.nextLine();
        char[][] matrix = new char[row][column];
        int[][] visisted = new int[row][column];
        for (int i = 0; i < row; i++) {
            String lines = scanner.nextLine();
            for (int j = 0; j < column; j++) {
                matrix[i][j] = lines.charAt(j);
            }
        }
        int[][] adjMatrix = getAdjMatrix(row, column, matrix);
        int[] pathway = bfs(adjMatrix);
        // for (int i = 0; i < pathway.length; i++) {
        //     System.out.print(pathway[i] + " ");
        // }
        // System.out.println();

        int maxGold = 0;
        for (int i = pathway.length - 1; i >= 0; i--) {
            if (adjMatrix[i][2] != 0) {
                int sum = 0;
                int step = -1;
                for (int j = i; j != -2; j = pathway[j]) {
                    if (adjMatrix[j][2] != 0) {
                        sum += adjMatrix[j][2];
                    }
                    System.out.print(j + ", ");
                    if (sum > maxGold) {
                        maxGold = sum;
                    }
                    step++;
                }
                System.out.print("Sum: " + sum + ", " + "Step: " + step);
                System.out.println();
                System.out.println();
            }
        }
        System.out.println(maxGold);
    }

    public static int[] bfs(int[][] adjMatrix) {
        int[] pathMemories = new int[adjMatrix.length];
        int[] visisted = new int[adjMatrix.length];
        Arrays.fill(visisted, -1);
        Arrays.fill(pathMemories, -1);
        pathMemories[0] = -2;
        LinkedListQueue arrQueue = new LinkedListQueue();
        visisted[0] = 1;
        arrQueue.enQueue(0);
        while (!arrQueue.isEmpty()) {
            int node = arrQueue.peek();
            arrQueue.deQueue();
            for (int i = 0; i < adjMatrix[node].length - 1; i++) {
                if (visisted[adjMatrix[node][i]] != 1) {
                    pathMemories[adjMatrix[node][i]] = node;
                    visisted[adjMatrix[node][i]] = 1;
                    arrQueue.enQueue(adjMatrix[node][i]);
                }
            }
        }
        return pathMemories;
    }

    public static int[][] getAdjMatrix(int row, int col, char[][] matrix) {
        int[][] adjMatrix = new int[row*col][3];
        int rTracking = 0;
        int cTracking = 0;
        for (int i = 0; i < (row*col); i++) {
            int count = 0;
            if (cTracking >= col) {
                cTracking = 0;
                rTracking++;
            }
            if (matrix[rTracking][cTracking] == 'X') {
                cTracking++;
                continue;
            }
            for (int j = 0; j < 3; j++) {
                if (count == 0) {
                    // save right
                    if ((cTracking + 1 < col) && (matrix[rTracking][cTracking + 1] != 'X')) {
                        adjMatrix[i][count] = i + 1;
                    }else {
                        adjMatrix[i][count] = 0;
                    }
                }else if (count == 1) {
                    if ((rTracking + 1 < row) && (matrix[rTracking + 1][cTracking] != 'X')) {
                        adjMatrix[i][count] = i + col;
                    }else {
                        adjMatrix[i][count] = 0;
                    }
                }else if (count == 2) {
                    if ((matrix[rTracking][cTracking] != '.') && (matrix[rTracking][cTracking] != 'X')) {
                        adjMatrix[i][count] = matrix[rTracking][cTracking] - '0';
                    }else {
                        adjMatrix[i][count] = 0;
                    }
                }else {

                }
                count++;
            }
            cTracking++;
        }

        return adjMatrix;
    }
}

class LinkedListQueue {
    static class Node {
        int data;
        Node next;

        public Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    int size;
    Node head;
    Node tail;

    public LinkedListQueue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean enQueue(int data) {
        Node newNode = new Node(data);
        if (this.size == 0) {
            this.head = newNode;
            this.tail = this.head;
            this.size++;
            return true;
        }
        this.tail.next = newNode;
        this.tail = this.tail.next;
        this.size++;
        return true;
    }

    public boolean deQueue() {
        if (this.size == 0) {
            return false;
        }

        this.head = this.head.next;
        this.size--;
        return true;
    }

    public int peek() {
        if (this.size == 0) {
            return -1;
        }
        return this.head.data;
    }
}
