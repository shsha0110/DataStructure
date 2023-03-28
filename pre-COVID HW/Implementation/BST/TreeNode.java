package BST;

public class TreeNode {
    public Comparable key;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(Comparable key) {
        this.key = key;
        left = right = null;
    }

    public TreeNode(Comparable key, TreeNode left, TreeNode right) {
        this.key = key;
        this.left = left;
        this.right = right;
    }
}
