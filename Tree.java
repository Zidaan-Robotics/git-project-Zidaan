import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Tree {
    Node root;

    public Tree(Node root) {
        this.root = root;
    }

    public Tree(char c, int value) {
        this.root = new Node(c, value);
    }

    public Node getRoot() {
        return root;
    }

    public static Tree makeTree(HashMap<Character, Integer> map) {
        PriorityQueue<Node> pq = new PriorityQueue<Node>();
        for (Character c : map.keySet()) {
            pq.add(new Node(c, map.get(c)));
        }
        while (pq.size() > 1) {
            Node node1 = pq.remove();
            Node node2 = pq.remove();
            Node newNode = new Node(node1, node2);
            node1.setParent(newNode);
            node2.setParent(newNode);
            pq.add(newNode);
        }
        return new Tree(pq.remove());
    }

    public String toString() {
        // queue traverses l->r, top down
        StringBuilder returnString = new StringBuilder();
        LinkedList<Node> list = new LinkedList<Node>();
        list.add(root);
        while (!list.isEmpty()) {
            Node temp = list.pop();
            if (temp != null) {
                returnString.append(temp.toString());
                list.add(temp.getLeftChild());
                list.add(temp.getRightChild());
            }
        }
        return returnString.toString();
    }
}
