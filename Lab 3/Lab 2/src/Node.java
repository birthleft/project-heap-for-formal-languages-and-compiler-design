public class Node {
    Object key;
    Object value;
    Node next;

    public Node(Object key, Object value, Node nextNode) {
        this.key = key;
        this.value = value;
        this.next = nextNode;
    }
}
