// A Linked List is made out of multiple Nodes,
// Each contains a reference to the next Node or a Null pointer (if it's the last Node).
public class Node {
    // The Key will play the role of the Position within the Symbol Table.
    // The Position is an identifier that increments with each new Symbol added.
    Object key;

    // The Value will play the role of the Symbol.
    // It represents either an identifier or a constant.
    Object value;

    // Reference to the next Node.
    Node next;

    public Node(Object key, Object value, Node nextNode) {
        this.key = key;
        this.value = value;
        this.next = nextNode;
    }
}
