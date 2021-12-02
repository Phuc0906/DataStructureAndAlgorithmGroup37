import java.util.Scanner;

public class findingGold {
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
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
        illustrateGold(matrix, 0, 0, 0, visisted);
    }

    public static void bfs(char[][] matrix) {
        LinkedListGold goldList = new LinkedListGold();
        CustomQueue<Character> customQueue = new CustomQueue<Character>();
        int x = 0;
        int y = 0;

    }

    public static void illustrateGold(char[][] matrix, int step, int x, int y, int[][] visisted) {
        if ((x >= matrix.length) || (y >= matrix[0].length) || (matrix[x][y] == 'X') || (visisted[x][y] == 1)) {
            return;
        }
        step++;
        visisted[x][y] = 1;
        if (matrix[x][y] != '.') {
            System.out.println("Current step: " + step + " - Gold: " + matrix[x][y] + " - x: " + x + ", y: " + y);
        }
        // move down
        illustrateGold(matrix, step, x + 1, y, visisted);

        // move right
        illustrateGold(matrix, step, x, y + 1, visisted);
    }
}

class LinkedListGold {
    static class NodeGold {
        int x;
        int y;
        int value;
        NodeGold next;

        public NodeGold(int x, int y, int value) {
            this.next = null;
            this.x = x;
            this.y = y;
            this.value = value;
        }
    }

    NodeGold head;
    NodeGold tail;
    int size = 0;
    public LinkedListGold() {
        this.head = null;
        this.tail = null;
        this.size = 0;   
    }

    public void insert(int x, int y, int value) {
        NodeGold newNode = new NodeGold(x, y, value);
        if (size == 0) {
            this.head = newNode;
            this.tail = this.head;
            size++;
            return;
        }
        this.tail.next = newNode;
        this.tail = this.tail.next;
        size++;
    }
}

class CustomQueue<T> {
    static class Node<T> {
        T data;
        Node<T> next;

        public Node(T data) {
            this.next = null;
            this.data = data;
        }
    }

    int size;
    Node<T> head;
    Node<T> tail;
    public CustomQueue() {
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean enQueue(T data) {
        Node<T> newNode = new Node<T>(data);
        if (size == 0) {
            this.head = newNode;
            this.tail = this.head;
            size++;
            return true;
        }
        this.tail.next = newNode;
        this.tail = this.tail.next;
        size++;
        return true;
    }

    public boolean deQueue() {
        if (size == 0) {
            return false;
        }
        this.head = this.head.next;
        size++;
        return true;
    }

    public T peekFront() {
        if (size == 0) {
            return null;
        }
        return this.head.data;
    }
}
