package hashTable;

import list.Node;

public class ArrayHashTableTest {
    public static void main(String[] args) {
        ArrayHashTable h = new ArrayHashTable(11);
        h.insert(10);
        h.delete(20);
        h.insert(20);
        h.insert(5);
        h.insert(80);
        h.insert(90);
        h.delete(20);
        h.delete(44);

        int slot = h.search(80);
        if (slot == ArrayHashTable.NOT_FOUND) {
            System.out.println("Search Failed");
        } else {
            System.out.println("Found : " + h.getItem(slot));
        }
    }
}
