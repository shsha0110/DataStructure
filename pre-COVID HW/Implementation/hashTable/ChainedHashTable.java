package hashTable;

import BST.IndexInterface;
import list.Node;
import list.LinkedList;

public class ChainedHashTable {
    private LinkedList<Integer>[] table;
    int numItems = 0;

    public ChainedHashTable(int n) {
        table = (LinkedList<Integer>[]) new LinkedList[n];
        for (int i = 0; i < n; i++) {
            table[i] = new LinkedList<>();
        }
        numItems = 0;
    }

    private int hash(Integer x) {
        return x % table.length;
    }

    public Node search(Integer x) {
        int slot = hash(x);
        if (table[slot].isEmpty()) {
            return null;
        } else {
            int i = table[slot].indexOf(x);
            if (i == LinkedList.NOT_FOUND) {
                return null;
            } else {
                return table[slot].getNode(i);
            }
        }
    }

    public void insert(Integer x) {
        int slot = hash(x);
        table[slot].add(0, x);
        numItems++;
    }

    public void delete(Integer x) {
        int slot = hash(x);
        table[slot].removeItem(x);
        numItems--;
    }

    public boolean isEmpty() {
        return numItems == 0;
    }

    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<>();
        }
        numItems = 0;
    }
}
