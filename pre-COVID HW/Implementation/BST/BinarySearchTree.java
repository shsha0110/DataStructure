package BST;

public class BinarySearchTree implements IndexInterface<TreeNode> {
    private TreeNode root;

    public BinarySearchTree() {
        root = null;
    }

    @Override
    public TreeNode search(Comparable x) {
        return searchHelp(root, x);
    }

    private TreeNode searchHelp(TreeNode currNode, Comparable x) {
        if (currNode == null) {
            return null;
        }
        if (x.compareTo(currNode.key) == 0) {
            return currNode;
        } else if (x.compareTo(currNode.key) < 0) {
            return searchHelp(currNode.left, x);
        } else {
            return searchHelp(currNode.right, x);
        }
    }

    @Override
    public void insert(Comparable x) {
        root = insertHelp(root, x);
    }

    private TreeNode insertHelp(TreeNode currNode, Comparable x) {
        if (currNode == null) {
            return new TreeNode(x);
        }
        if (x.compareTo(currNode.key) < 0) {
            currNode.left = insertHelp(currNode.left, x);
        } else {
            currNode.right = insertHelp(currNode.right, x);
        }
        return currNode;
    }

    @Override
    public void delete(Comparable x) {
        root = deleteHelp(root ,x);
    }

    private TreeNode deleteHelp(TreeNode currNode, Comparable x) {
        if (currNode == null) {
            return null;
        } else {
            if (x.compareTo(currNode.key) == 0) {
                currNode = deleteNode(currNode);
            } else if (x.compareTo(currNode.key) < 0) {
                currNode.left = deleteHelp(currNode.left, x);
            } else {
                currNode.right = deleteHelp(currNode.right, x);
            }
            return currNode;
        }
    }

    private TreeNode deleteNode(TreeNode currNode) {
        if (currNode.left == null && currNode.right == null) {
            return null;
        } else if (currNode.left != null && currNode.right == null) {
            return currNode.left;
        } else if (currNode.left == null && currNode.right != null) {
            return currNode.right;
        } else {
            returnPair rPair = deleteNodeHelp(currNode.right);
            currNode.key = rPair.key;
            currNode.right = rPair.node;
            return currNode;
        }
    }

    private returnPair deleteNodeHelp(TreeNode currNode) {
        if (currNode.left == null) {
            return new returnPair(currNode.key, currNode.right);
        } else {
            returnPair rPair = deleteNodeHelp(currNode.left);
            currNode.left = rPair.node;
            rPair.node = currNode;
            return rPair;
        }
    }

    private class returnPair {
        private Comparable key;
        private TreeNode node;

        private returnPair(Comparable key, TreeNode node) {
            this.key = key;
            this.node = node;
        }
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public void clear() {
        root = null;
    }
}
