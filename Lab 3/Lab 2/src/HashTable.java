public class HashTable {
    private Node[] hashTable;

    public HashTable(int initialSize) {
        hashTable = new Node[initialSize];
    }

    private int getASCIICodeSum(String identifier) {
        Integer ASCIICodeSum = 0;
        for (Character singleChar : identifier.toCharArray()) {
            ASCIICodeSum += (int) singleChar;
        }
        return ASCIICodeSum;
    }

    private Integer hash(String identifier, Integer hashTableSize) {
        return Math.abs(this.getASCIICodeSum(identifier) % hashTableSize);
    }

    public void put(String key, Object value) {
        Integer listIndex = this.hash(key, this.hashTable.length);
        Node list = hashTable[listIndex];

        while (list != null) {
            if (list.key.equals(key)) {
                list.value = value;
                return;
            }
            list = list.next;
        }

        /*
        if (count >= 0.7 * hashTable.length) {
            this.resize();
        }
        */

        Node newNode = new Node(key, value, hashTable[listIndex]);
        hashTable[listIndex] = newNode;
    }

    public Object get(String key) {
        int listIndex = this.hash(key, this.hashTable.length);
        Node list = hashTable[listIndex];
        while (list != null) {
            if (list.key.equals(key))
                return list.value;
            list = list.next;
        }
        return null;
    }

    public void viewContent() {
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
}
