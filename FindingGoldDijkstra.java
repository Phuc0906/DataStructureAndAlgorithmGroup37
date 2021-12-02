import java.util.Arrays;
import java.util.Scanner;

public class FindingGoldDijkstra {
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
        int cTracking = 1;
        int rTracking = 0;

        int[] path = dijkstraSearch(adjMatrix, row, column);
        LinkedListStack stackPath = new LinkedListStack();
        int maxIndex = path[path.length - 1];
        for (int j = maxIndex; j != -1; j = path[j]) {
            stackPath.push(path[j]);
        }
        System.out.println("Step: " + (stackPath.size - 1));
        stackPath.del();
        stackPath.del();

        System.out.print("+" + " ");
        for (int i = 1; i < path.length - 1; i++) {
            if (cTracking >= column) {
                System.out.println();
                cTracking = 0;
                rTracking++;
                if (rTracking >= row) {
                    break;
                }
            }
            if (i == stackPath.peek()) {
                if (matrix[rTracking][cTracking] != '.') {
                    System.out.print("G" + " ");
                }else {
                    System.out.print("+" + " ");
                }
                
                stackPath.del();
            }else{
                System.out.print(matrix[rTracking][cTracking] + " ");
            }
            
            cTracking++;
        }
        System.out.println();
        
    }

    public static int[] dijkstraSearch(int[][] adjMatrix, int row, int col) {
        int[] visisted = new int[row*col];
        int[] totalGold = new int[row*col];
        int[] prevNode = new int[row*col + 1];
        Arrays.fill(prevNode, -1);
        Arrays.fill(visisted, -1);
        Arrays.fill(totalGold, -1);
        totalGold[0] = adjMatrix[0][2];
        PriorityQueue pq = new PriorityQueue(row, col);
        pq.insert(0, adjMatrix[0][2]);
        int maxGold = 0;
        int maxINdex = 0;
        
        while (!pq.isEmpty()) {
            
            int node = pq.peekMax();
            visisted[node] = 1;
            for (int i = 0; i < adjMatrix[node].length - 1; i++) {
                
                if (adjMatrix[node][i] == -1) {
                    continue;
                }
                if (visisted[adjMatrix[node][i]] == 1) {
                    int newGold = totalGold[node] + adjMatrix[adjMatrix[node][i]][2];
                    if (newGold > totalGold[adjMatrix[node][i]]) {
                        if (newGold > maxGold) {
                            maxGold =  newGold;
                            maxINdex = adjMatrix[node][i];
                        }
                        prevNode[adjMatrix[node][i]] = node;
                        totalGold[adjMatrix[node][i]] = newGold;
                        pq.insert(adjMatrix[node][i], totalGold[adjMatrix[node][i]]);
                    }
                    continue;
                }
                if (totalGold[adjMatrix[node][i]] == -1) {
                    prevNode[adjMatrix[node][i]] = node;
                    
                    totalGold[adjMatrix[node][i]] = totalGold[node] + adjMatrix[adjMatrix[node][i]][2];
                    if (totalGold[adjMatrix[node][i]] > maxGold) {
                        maxGold=  totalGold[adjMatrix[node][i]];
                        maxINdex = adjMatrix[node][i];
                    }
                    pq.insert(adjMatrix[node][i], totalGold[adjMatrix[node][i]]);
                    
                    continue;
                }
                
                int newGold = totalGold[node] + adjMatrix[adjMatrix[node][i]][2];
                if (newGold > totalGold[adjMatrix[node][i]]) {
                    if (newGold > maxGold) {
                        maxGold =  newGold;
                        maxINdex = adjMatrix[node][i];
                    }
                    prevNode[adjMatrix[node][i]] = node;
                    totalGold[adjMatrix[node][i]] = newGold;
                    pq.insert(adjMatrix[node][i], totalGold[adjMatrix[node][i]]);
                    
                }
            }
        }
        System.out.println("Max gold: " + maxGold);
        prevNode[prevNode.length - 1] = maxINdex;
        return prevNode;

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
                adjMatrix[i][0] = -1;
                adjMatrix[i][1] = -1;
                cTracking++;
                continue;
            }
            for (int j = 0; j < 3; j++) {
                if (count == 0) {
                    // save right
                    if ((cTracking + 1 < col) && (matrix[rTracking][cTracking + 1] != 'X')) {
                        adjMatrix[i][count] = i + 1;
                    }else {
                        adjMatrix[i][count] = -1;
                    }
                }else if (count == 1) {
                    if ((rTracking + 1 < row) && (matrix[rTracking + 1][cTracking] != 'X')) {
                        adjMatrix[i][count] = i + col;
                    }else {
                        adjMatrix[i][count] = -1;
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

class LinkedListStack {
    static class Node {
        int data;
        Node next;

        public Node(int data) {
            this.data = data;
            this.next = null;
        } 
    }

    Node head;
    int size;

    public boolean isEmpty() {
        return this.size == 0;
    }
    public LinkedListStack() {
        this.head = null;
        this.size = 0;
    }

    public void push(int data) {
        Node newNode = new Node(data);
        if (this.size == 0) {
            this.head = newNode;
            this.size++;
            return;
        }

        newNode.next = this.head;
        this.head = newNode;
        this.size++;
    }

    public int peek() {
        if (this.size == 0) {
            return -1;
        }
        return this.head.data;
    }

    public void del() {
        if (this.size == 0) {
            return;
        }
        this.head = this.head.next;
        this.size--;
    }

}

class PriorityQueue {
    int size;
    int[] nodeArray;
    int[] goldArray;

    public PriorityQueue(int row, int col) {
        this.size  = 0;
        nodeArray = new int[row*col];
        goldArray = new int[row*col];
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public void printQueue() {
        for (int i = 0; i < this.size; i++) {
            System.out.println("Node: " + this.nodeArray[i] + " - Gold: " + this.goldArray[i]);
        }
    }

    public void insert(int node, int gold) {
        this.nodeArray[this.size] = node;
        this.goldArray[this.size] = gold;
        this.size++;
        fix_up();
    }

    public int peekMax() {
        this.size--;
        if (this.size < 0) {
            return -1;
        }
        if (this.size == 0) {
            return this.nodeArray[0];
        }
        int tmp = this.nodeArray[0];
        this.nodeArray[0] = this.nodeArray[this.size];
        this.nodeArray[this.size] = tmp;

        int tmpGold = this.goldArray[0];
        this.goldArray[0] = this.goldArray[this.size];
        this.goldArray[this.size] = tmpGold;
        fix_down();
        return this.nodeArray[this.size];
    }

    private void fix_up() {
        int position = this.size - 1;
        while ((getParent(position) >= 0) && (this.goldArray[getParent(position)] < this.goldArray[position])) {
            // swap
            int tmpNode = this.nodeArray[position];
            this.nodeArray[position] = this.nodeArray[getParent(position)];
            this.nodeArray[getParent(position)] = tmpNode; 
            int tmpGold = this.goldArray[position];
            this.goldArray[position] = this.goldArray[getParent(position)];
            this.goldArray[getParent(position)] = tmpGold; 
            position = getParent(position);
        }
    }

    private void fix_down() {
        int i = 0;
        while (i < this.size) {
            int j = getLeftChild(i);
            if ((this.goldArray[j] < this.goldArray[j + 1]) && (j <= this.size - 1)) {
                j++;
            }
            if ((this.goldArray[i] >= this.goldArray[j]) || (j >= this.size)) {
                break;
            }
            // System.out.println("Swap node " + this.nodeArray[i] + " and " + this.nodeArray[j] + " - i: " + i + " - j: " + j);
            // System.out.println("Swap gold " + this.goldArray[i] + " and " + this.goldArray[j]);
            // System.out.println("------------");
            int tmpNode = this.nodeArray[i];
            this.nodeArray[i] = this.nodeArray[j];
            this.nodeArray[j] = tmpNode; 
            int tmpGold = this.goldArray[i];
            this.goldArray[i] = this.goldArray[j];
            this.goldArray[j] = tmpGold;
            i = j;
        }
    }

    private int getParent(int i) {
        return (i - 2) / 2;
    }

    private int getLeftChild(int i) {
        return (2 * i) + 1;
    }

}