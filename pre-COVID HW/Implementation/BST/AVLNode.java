package BST;

public class AVLNode {
    public Comparable item;
    public AVLNode left, right;
    public int height;

    public AVLNode(Comparable item) {
        this.item = item;
        left = right = AVLTree.NIL;
        height = 1;
    }

    public AVLNode(Comparable item, AVLNode left, AVLNode right) {
        this.item = item;
        this.left = left;
        this.right = right;
        height = 1;
    }

    public AVLNode(Comparable item, AVLNode left, AVLNode right, int height) {
        this.item = item;
        this.left = left;
        this.right = right;
        this.height = height;
    }
}
