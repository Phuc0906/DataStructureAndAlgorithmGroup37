import java.util.Scanner;

public class ExhaustiveSearch {
    static LinkedListStack efficientPath = new LinkedListStack();
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int row = scanner.nextInt();
        int column = scanner.nextInt();
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

        // create a stack to store the path
        LinkedListStack list = new LinkedListStack();

        // generation method
        generatePossiblePath(list, matrix, 0, 0);

        // print the result
        System.out.println("Step: " + list.minStep + "\nGold: " + list.maxGold);
        System.out.println("Path: " + reverseString(list.path)); // the stack store the node backward so we need to reverse it to get the correct one
    }

    // reverse method
    public static String reverseString(String inputString) {
        String outString = "";
        for (int i = inputString.length() - 1; i >= 0; i--) {
            outString += inputString.charAt(i);
        }
        return outString;
    }

    public static void generatePossiblePath(LinkedListStack list, String[][]matrix, int row, int column) {

        // check if the current position is out of map => break
        if ((row >= matrix.length) || (column >= matrix[0].length)) {
            return;
        }

        // if the current node is X => not process this way
        if (matrix[row][column].equals("X")) { 
            return;
        }

        // if the current node is not . => note the gold value and process the right and bottom side recuresively
        int gold = (!matrix[row][column].equals(".")) ? Integer.parseInt(matrix[row][column]) : 0;

        // update new node to the stack
        list.push(new State(row, column, gold));

        // process the right and bottom way
        generatePossiblePath(list, matrix, row, column + 1);
        generatePossiblePath(list, matrix, row + 1, column);

        // delete that node when finished
        list.pop();
    }
}

// the state describe the node information: gold value, row, column
class State {
    int row;
    int column;
    int gold;
    public State(int row, int column, int gold) {
        this.column = column;
        this.row = row;
        this.gold = gold;
    }
}

// create a custom stack
class LinkedListStack {
    static class Node {
        State data;
        Node next;

        public Node(State data) {
            this.data = data;
            this.next = null;
        } 
    }

    Node head;
    int size;

    // totalGold store the current gold value
    int totalGold;

    // maxGold save the current max gold value
    int maxGold;

    // minStep store the current minimum step
    int minStep;

    // store the efficiency path
    String path;

    public boolean isEmpty() {
        return this.size == 0;
    }
    public LinkedListStack() {
        this.head = null;
        this.size = 0;
        this.totalGold = 0;
        this.maxGold = 0;
        this.minStep = 0;
        this.path = "";
    }

    public void push(State data) {
        Node newNode = new Node(data);
        if (this.size == 0) {
            this.head = newNode;
            this.size++;
            this.totalGold += data.gold;
            return;
        }
        this.totalGold += data.gold;
        newNode.next = this.head;
        this.head = newNode;
        this.size++;
        // when new node has been added => check the current total gold if it is greater than the current max gold => update maxGold, minSteps and path
        if (totalGold > maxGold) {
            maxGold = totalGold;
            minStep = this.size - 1;
            updatePath();
        }
    }

    private void updatePath() {
        Node current = this.head;
        // traverse from head to the current node
        this.path = "";
        while (current.next != null) {
            // if 2 node is at the same column => moving down
            // otherwise, moving right
            if (current.data.column - current.next.data.column == 0) {
                path += "D";
            }else {
                path += "R";
            }
            current = current.next;
        }
    }

    public State peek() {
        if (this.size == 0) {
            return null;
        }
        return this.head.data;
    }

    public void pop() {
        if (this.size == 0) {
            return;
        }
        this.totalGold -= this.head.data.gold;
        this.head = this.head.next;
        this.size--;
    }
}