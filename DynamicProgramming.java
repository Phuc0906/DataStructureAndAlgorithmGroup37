import java.util.Arrays;
import java.util.Scanner;

public class DynamicProgramming {
    public static void main(String[] args) {
        // process the map into the maze
        Scanner scanner = new Scanner(System.in);

        // getting the row and column
        int row = scanner.nextInt();
        int column = scanner.nextInt();
        if ((row > 27) || (column > 27)) {
            System.out.println("Map cannot be process");
            return;
        }
        scanner.nextLine();
        
        String[][] matrix = new String[row][column];
        for (int i = 0; i < row; i++) {
            String line = scanner.nextLine();
            // check if the row has more colums than the input colums
            String[] lines = line.split(" ");
            if (lines.length > column) {
                System.out.println("Map cannot be processes");
                return;
            }

            // get each element in the column
            for (int j = 0; j < column; j++) {
                matrix[i][j] = lines[j];
            }
        }

        // check if the app has more rows than the input row => break the program
        if (scanner.hasNextLine()) {
            System.out.println("Map cannot be process");
            return;
        }

        // convert the map into adjacent matrix
        int[][] adjMatrix = getAdjMatrix(row, column, matrix);
        
        // apply dijkstra Algorithm to find the efficiency path
        int[] path = dijkstraSearch(adjMatrix, row, column);
        
        // create an Stack to store each node of the path to get the correct step
        LinkedListStack stackPath = new LinkedListStack();

        // get the the index of the maximum gold path (always save to the end of the path array)
        int maxIndex = path[path.length - 1];

        // create a string to store the path
        String pathString = "";

        // the each index of path array stores its parent index which indicate the efficiency path
        for (int j = maxIndex; j != -1; j = path[j]) {
            // check if its parent is -1 => break the loop
            if (path[j] == -1) {
                break;
            }

            // because the dikstra algorithm need to process the graph 
            // in the adjacent matrix converter it will generate the matrix in to and array of row*column indexs
            // to detect if its parent in the different row, we check if different between the current index and its next index is greater or equal column => in different row => move Down 
            // otherwise, move right
            if (j - path[j] >= column) {
                pathString += "D";
            }else {
                pathString += "R";
            }
            // push the indexs into stack to process the steps
            stackPath.push(path[j]);
        }
        // because the above loop is process backward, so we need to reverse the pathString to get the correct path
        String strPath = reverseString(pathString);
        
        // print the step and the path (gold is already printed in the dikjstra search method)
        System.out.println("Step: " + (stackPath.size));
        System.out.println("Path: " + strPath);
        
        
    }

    // reverse string method
    public static String reverseString(String inputString) {
        String outString = "";
        for (int i = inputString.length() - 1; i >= 0; i--) {
            outString += inputString.charAt(i);
        }
        return outString;
    }

    // djkstra algorithm 
    public static int[] dijkstraSearch(int[][] adjMatrix, int row, int col) {
        // create visited array to mark the node already processed
        int[] visisted = new int[row*col];

        // store the total gold when the program collected from the beginning to that node
        int[] totalGold = new int[row*col];

        // store the previous index which collected the most gold
        int[] prevNode = new int[row*col + 1];

        // mark all of them as -1 (has not processed)
        Arrays.fill(prevNode, -1);
        Arrays.fill(visisted, -1);
        Arrays.fill(totalGold, -1);

        // set the current gold at the beginning
        totalGold[0] = adjMatrix[0][2];

        // declare priority queue to process the dikjstra algorithm
        PriorityQueue pq = new PriorityQueue(row, col);

        // insert the first node to the queue
        pq.insert(0, adjMatrix[0][2]);

        // declare maxGold and the index of the MAxGold position
        int maxGold = 0;
        int maxINdex = 0;
        
        // when the priority is empty => stop processing
        while (!pq.isEmpty()) {
            // get the Node has the most gold to process
            int node = pq.peekMax();

            // mark that node as visited
            visisted[node] = 1;

            // traverse all neighbour node of that node
            for (int i = 0; i < 2; i++) {
                // if its neighbour is mark as -1 => ignore it
                if (adjMatrix[node][i] == -1) {
                    continue;
                }

                // if its neighbour is visited but if the current node move to that node has a greater gold => reprocess that node
                if (visisted[adjMatrix[node][i]] == 1) {
                    int newGold = totalGold[node] + adjMatrix[adjMatrix[node][i]][2];
                    if (newGold > totalGold[adjMatrix[node][i]]) {
                        if (newGold > maxGold) {
                            maxGold =  newGold;
                            maxINdex = adjMatrix[node][i];
                        }
                        // store the new parent index
                        prevNode[adjMatrix[node][i]] = node;

                        // store new gold value at that node
                        totalGold[adjMatrix[node][i]] = newGold;

                        // insert the neighbour node again to process
                        pq.insert(adjMatrix[node][i], totalGold[adjMatrix[node][i]]);
                    }
                    continue;
                }

                // if the gold at that node is -1 => set the gold value at the index of that node
                if (totalGold[adjMatrix[node][i]] == -1) {
                    prevNode[adjMatrix[node][i]] = node;
                    
                    totalGold[adjMatrix[node][i]] = totalGold[node] + adjMatrix[adjMatrix[node][i]][2];
                    if (totalGold[adjMatrix[node][i]] > maxGold) {
                        maxGold=  totalGold[adjMatrix[node][i]];
                        maxINdex = adjMatrix[node][i];
                    }
                    // insert to the priority queue to process
                    pq.insert(adjMatrix[node][i], totalGold[adjMatrix[node][i]]);
                    
                    continue;
                }
        
            }
        }

        // print the maxGold immediately to the terminal
        System.out.println("Max gold: " + maxGold);

        // store the index of max gold at the end of the prevNode array
        prevNode[prevNode.length - 1] = maxINdex;
        return prevNode;

    }

    // adjacent matrix converter method
    public static int[][] getAdjMatrix(int row, int col, String[][] matrix) {
        // create row*col length and each element is the array of 3 indexs
        // index 0 and 1 : store neighbour on the left and down
        // index 2: store the node's gold
        int[][] adjMatrix = new int[row*col][3];

        // declare cTracking and rTracking to track the original matrix
        int rTracking = 0;
        int cTracking = 0;

        // traverse all node in the original matrix
        for (int i = 0; i < (row*col); i++) {

            // track if the cTracking is greater or equal column => reset and insrease rTracking
            if (cTracking >= col) {
                cTracking = 0;
                rTracking++;
            }

            // check if the current node is X => mark all -1 and skip the rest step
            if (matrix[rTracking][cTracking].equals("X")) {
                adjMatrix[i][0] = -1;
                adjMatrix[i][1] = -1;
                cTracking++;
                continue;
            }

            // setting the node components
            for (int j = 0; j < 3; j++) {
                // if count == 0 => process right neighbour
                if (j == 0) {
                    // if it is the edge node or its right neighbour is X => mark -1, if not mark the neighbour value
                    if (cTracking + 1 < col) {
                        if (!matrix[rTracking][cTracking + 1].equals("X")) {
                            adjMatrix[i][j] = i + 1;
                        }else {
                            adjMatrix[i][j] = -1;
                        }
                        
                    }else {
                        adjMatrix[i][j] = -1;
                    }
                // if count == 1 => process down neighbour
                }else if (j == 1) {
                    // if it is the edge node or its down neighbour is X => mark -1, if not mark the neighbour value
                    if (rTracking + 1 < row) {
                        if (!matrix[rTracking + 1][cTracking].equals("X")) {
                            adjMatrix[i][j] = i + col;
                        }else {
                            adjMatrix[i][j] = -1;
                        }
                        
                    }else {
                        adjMatrix[i][j] = -1;
                    }
                // the rest case is process the gold, if the node character is  . and X => set 0 or set the gold using Interger.parseInt();
                }else if (j == 2) {
                    if ((!matrix[rTracking][cTracking].equals(".")) && (!matrix[rTracking][cTracking].equals("X"))) {
                        adjMatrix[i][j] = Integer.parseInt(matrix[rTracking][cTracking]);
                    }else {
                        adjMatrix[i][j] = 0;
                    }
                }else {

                }
            }
            // increase the cTracking to track the next node
            cTracking++;
        }

        return adjMatrix;
    }
}

// declare own linkedlist stack
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

// declare priority queue
class PriorityQueue {
    int size;
    int[] nodeArray;
    int[] goldArray;

    public PriorityQueue(int row, int col) {
        // initial arrays
        this.size  = 0;
        nodeArray = new int[row*col];
        goldArray = new int[row*col];
    }

    public boolean isEmpty() {
        return this.size == 0;
    }
    

    public void insert(int node, int gold) {
        this.nodeArray[this.size] = node;
        this.goldArray[this.size] = gold;
        this.size++;
        fix_up();
    }

    // peek the max value
    public int peekMax() {
        // after peeking the max value => relocate the current index using fix_down() method
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
            if (j >= this.size) {
                break;
            }
            if (this.goldArray[i] >= this.goldArray[j]) {
                break;
            }
            if ((this.goldArray[j] < this.goldArray[j + 1]) && (j <= this.size - 1)) {
                j++;
            }
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
        return (i - 1) / 2;
    }

    private int getLeftChild(int i) {
        return (2 * i) + 1;
    }

}