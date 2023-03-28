package AVLTree;

import LinkedList.*;

public class AVLNode {
    public String key;
    public LinkedList<Pair> item;
    public AVLNode left, right;
    public int height;
    public AVLNode[] child;

    public AVLNode(LinkedList<Pair> item) {
        this.key = item.key;
        this.item = item;
        this.left = this.right = AVLTree.NIL;
        this.height = 1;
        this.child = new AVLNode[]{left, right};
    }

    public AVLNode(LinkedList<Pair> item, AVLNode leftChild, AVLNode rightChild) {
        this.key = item.key;
        this.item = item;
        this.left = leftChild;
        this.right = rightChild;
        this.height = 1;
        this.child = new AVLNode[]{left, right};
    }

    public AVLNode(LinkedList<Pair> item, AVLNode leftChild, AVLNode rightChild, int height) {
        this.key = item.key;
        this.item = item;
        this.left = leftChild;
        this.right = rightChild;
        this.height = height;
        this.child = new AVLNode[]{left, right};
    }
}
