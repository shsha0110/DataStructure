package BST;

public class AVLTreeTest {
    public static void main(String[] args) {
        AVLTree avl = new AVLTree();
        System.out.println("isEmpty() : " + avl.isEmpty());
        avl.insert(10);
        avl.insert(20);
        avl.insert(5);
        avl.insert(80);
        avl.insert(90);
        avl.delete(80);
    }
}
