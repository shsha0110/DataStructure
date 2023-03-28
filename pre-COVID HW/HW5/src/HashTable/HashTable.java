package HashTable;

import AVLTree.*;
import LinkedList.*;

public class HashTable {
    private AVLTree[] table;
    int numItems;

    public HashTable(int n) {
        table = new AVLTree[n];
        for (int i = 0; i < n; i++) {
            table[i] = new AVLTree();
        }
        numItems = 0;
    }

    private int hash(LinkedList<Pair> input) {
        String key = input.key;
        int sumOfASCII = 0;
        for (int i = 0; i < key.length(); i++) {
            sumOfASCII += (int)(key.charAt(i));
        }
        return sumOfASCII % 100;
    }

    public void insert(LinkedList<Pair> input) {
        int slot = hash(input);
        table[slot].insert(input);
        numItems++;
    }

    public AVLNode search(LinkedList<Pair> input) {
        int slot = hash(input);
        return table[slot].search(input);
    }

    public void delete(LinkedList<Pair> input) {
        if (isEmpty()) {
            // Process Error
        } else {
            int slot = hash(input);
            table[slot].delete(input);
            numItems--;
        }
    }

    public boolean isEmpty() {
        return numItems <= 0;
    }

    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = new AVLTree();
        }
        numItems = 0;
    }

    public AVLTree getSlot(int indexNumber) {
        if (indexNumber < 100) {
            return table[indexNumber];
        }
        return null;
    }
}
