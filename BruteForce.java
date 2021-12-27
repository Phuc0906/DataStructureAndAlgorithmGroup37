import java.util.Scanner;

public class BruteForce {
    static LinkedListStack efficientPath = new LinkedListStack();
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int row = scanner.nextInt();
        int column = scanner.nextInt();
        scanner.nextLine();
        char[][] matrix = new char[row][column];
        for (int i = 0; i < row; i++) {
            String lines = scanner.nextLine();
            for (int j = 0; j < column; j++) {
                matrix[i][j] = lines.charAt(j);
            }
        }
        LinkedListStack list = new LinkedListStack();
        generatePossiblePath(list, matrix, 0, 0);
        System.out.println("Step: " + efficientPath.minStep + " - Gold: " + efficientPath.maxGold);
    }

    public static void generatePossiblePath(LinkedListStack list, char[][]matrix, int row, int column) {
        if ((row >= matrix.length) || (column >= matrix[0].length)) {
            if (efficientPath.maxGold < list.maxGold) {
                efficientPath = list;
            }
            return;
        }
        if ((row + 1 >= matrix.length) && (column + 1 >= matrix[0].length)) {
            if (efficientPath.maxGold < list.maxGold) {
                efficientPath = list;
            }
            return;
        }
        if (matrix[row][column] == 'X') {
            if (efficientPath.maxGold < list.maxGold) {
                efficientPath = list;
            }
            return;
        }
        int gold = (matrix[row][column] != '.') ? (matrix[row][column] - '0') : 0;
        list.push(new State(row, column, gold));
        generatePossiblePath(list, matrix, row, column + 1);
        generatePossiblePath(list, matrix, row + 1, column);
        list.pop();
    }
}


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

class LinkedListStack {
    static class Node {
        State data;
        Node next;
        Node prev;

        public Node(State data) {
            this.data = data;
            this.next = null;
        } 
    }

    Node head;
    int size;
    int totalGold;
    int maxGold;
    int minStep;
    boolean isCheckMinStep = false;

    public boolean isEmpty() {
        return this.size == 0;
    }
    public LinkedListStack() {
        this.head = null;
        this.size = 0;
        this.totalGold = 0;
        this.maxGold = 0;
        this.minStep = 0;
        
    }

    public void push(State data) {
        Node newNode = new Node(data);
        if (this.size == 0) {
            this.head = newNode;
            this.size++;
            this.totalGold += data.gold;
            if (totalGold > maxGold) {
                maxGold = totalGold;
                minStep = this.size;
            }
            return;
        }
        this.totalGold += data.gold;
        newNode.next = this.head;
        this.head = newNode;
        this.size++;
        if (totalGold > maxGold) {
            maxGold = totalGold;
            minStep = this.size - 1;
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

    public void printElement() {
        System.out.println("Step: " + minStep + " - Gold: " + maxGold);
    }

}