// The Symbol Table is implemented using a Hash Table of Variable Dimension.
public class SymbolTable {

    // A Hash Table is fundamentally an Array of Linked Lists.
    private Node[] hashTable;

    // The Count will be used for calculating the position of each new Symbol.
    private int count = 0;

    public SymbolTable() {
        hashTable = new Node[2];
    }

    // A Hash Table requires a "hash" function.
    // The Hash function is the Hash Code of the Symbol modulo the size of the Hash Table.
    private Integer hash(Object value, Integer hashTableSize) {
        return Math.abs(this.getHashCode(value) % hashTableSize);
    }

    // The Hash Code is the sum of each ASCII character contained by the Symbol.
    private int getHashCode(Object value) {
        int ASCIICodeSum = 0;
        for (char singleChar : String.valueOf(value).toCharArray()) {
            ASCIICodeSum += singleChar;
        }
        return ASCIICodeSum;
    }

    // The "put" function adds a Symbol to the Symbol Table.
    public void put(Object value) {
        // It calculates the hash of the Symbol given.
        Integer listIndex = this.hash(value, this.hashTable.length);

        // Afterwards, it pinpoints the location of the Linked List having as index the hash of the Symbol Given.
        Node list = hashTable[listIndex];

        // While the Linked List contains Nodes and not a Null Pointer,
        // it checks for the existence of the Symbol.
        while (list != null) {
            if (list.value.equals(value)) {
                // If the Symbol already exists,
                // the function simply ends its execution.
                return;
            }
            list = list.next;
        }

        // If the Symbol hasn't been found or the Linked List doesn't contain valid Nodes,
        // it runs some additional checks before adding the new Symbol into the Hash Table
        if (count >= 0.75 * hashTable.length) {
            // If the amount of Nodes is more than 3/4 of the Hash Table's size,
            // it executes the "resize" function.
            this.resize();
            // After the resize, it calculates the hash of the Symbol given,
            // this time taking the new size of the Hash Table into account.
            listIndex = this.hash(value, this.hashTable.length);
        }

        // It adds the new Symbol as the new head of the Linked List,
        // placing the previous head of the Linked List as its "next Node" reference.
        Node newNode = new Node(count++, value, hashTable[listIndex]);
        hashTable[listIndex] = newNode;
    }

    // The "resize" function reorganizes the Hash Table in order to better contain the new Symbols.
    private void resize() {
        // We create a new empty Hash Table, double the size of the initial Hash Table.
        Node[] newTable = new Node[hashTable.length * 2];
        // For each Linked List within the Hash Table, the function executes a set of instructions.
        for (Node node : hashTable) {
            Node list = node;
            // While the Linked List contains Nodes and not a Null Pointer,
            // it executes the Symbol transfer operations.
            while (list != null) {
                // It stores the Node succeeding the found Symbol within the old Hash Table's Linked List.
                Node next = list.next;
                // It calculates the new Hash of the Symbol using the length of the new Hash Table.
                int hash = this.hash(list.value, newTable.length);
                // It adds the Symbol as the new head of the new Hash Table's Linked List,
                // placing the previous head of the Linked List as its "next Node" reference.
                list.next = newTable[hash];
                newTable[hash] = list;
                // It proceeds to the next Symbol found within the old Hash Table's Linked List.
                list = next;
            }
        }
        // After all the transfer operations are complete, the old Hash Table is overwritten by the new Hash Table.
        hashTable = newTable;
    }

    // The "getKey" function returns the position of a given Symbol within the Symbol Table.
    public Object getKey(Object value) {
        int listIndex = this.hash(value, this.hashTable.length);
        Node list = hashTable[listIndex];
        while (list != null) {
            if (list.value.equals(value))
                return list.key;
            list = list.next;
        }
        // If no such Symbol exists, it returns a Null Pointer.
        return null;
    }

    // The "getValue" function returns the Symbol from a given Position within the Symbol Table.
    public Object getValue(Object key) {
        for (Node node : hashTable) {
            Node list = node;
            while (list != null) {
                if (list.key.equals(key))
                    return list.value;
                list = list.next;
            }
        }
        // If no such position exists, it returns a Null Pointer.
        return null;
    }

    public void viewContentAsHashTable() {
        System.out.println();
        for (int index = 0; index < hashTable.length; index++) {
            System.out.print(index + ":");
            Node list = hashTable[index];
            while (list != null) {
                System.out.print(" (" + list.key + "," + list.value + ");");
                list = list.next;
            }
            System.out.println();
        }
    }

    public void viewContentAsSymbolTable() {
        System.out.println();
        for (int index = 0; index < count; index++) {
            System.out.println("" + index + " => " + getValue(index) + "");
        }
    }
}
