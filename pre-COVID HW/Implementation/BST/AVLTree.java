package BST;

public class AVLTree implements IndexInterface<AVLNode> {
    private AVLNode root;
    static final AVLNode NIL = new AVLNode(null, null, null, 0);

    public AVLTree() {
        root = NIL;
    }

    @Override
    public AVLNode search(Comparable x) {
        return searchHelp(root, x);
    }

    private AVLNode searchHelp(AVLNode currNode, Comparable x) {
        if (currNode == NIL) {
            return NIL;
        }
        if (x.compareTo(currNode.item) == 0) {
            return currNode;
        } else if (x.compareTo(currNode.item) < 0) {
            return searchHelp(currNode.left, x);
        } else {
            return searchHelp(currNode.right, x);
        }
    }

    @Override
    public void insert(Comparable x) {
        root = insertHelp(root, x);
    }

    private AVLNode insertHelp(AVLNode currNode, Comparable x) {
        if (currNode == NIL) {
            return new AVLNode(x);
        }
        if (x.compareTo(currNode.item) < 0) {
            currNode.left = insertHelp(currNode.left, x);
            currNode.height = 1 + Math.max(currNode.left.height, currNode.right.height);
            int type = needBalance(currNode);
            if (type != NO_NEED) {
                currNode = balanceAVL(currNode, type);
            }
        } else {
            currNode.right = insertHelp(currNode.right, x);
            currNode.height = 1 + Math.max(currNode.left.height, currNode.right.height);
            int type = needBalance(currNode);
            if (type != NO_NEED) {
                currNode = balanceAVL(currNode, type);
            }
        }
        return currNode;
    }

    @Override
    public void delete(Comparable x) {
        root = deleteHelp(root, x);
    }

    private AVLNode deleteHelp(AVLNode currNode, Comparable x) {
        if (currNode == NIL) {
            return NIL;
        } else {
            if (x.compareTo(currNode.item) == 0) {
                currNode = deleteNode(currNode);
            } else if (x.compareTo(currNode.item) < 0) {
                currNode.left = deleteHelp(currNode.left, x);
                currNode.height = 1 + Math.max(currNode.left.height, currNode.right.height);
                int type = needBalance(currNode);
                if (type != NO_NEED) {
                    currNode = balanceAVL(currNode, type);
                }
            } else {
                currNode.right = deleteHelp(currNode.right, x);
                currNode.height = 1 + Math.max(currNode.left.height, currNode.right.height);
                int type = needBalance(currNode);
                if (type != NO_NEED) {
                    currNode = balanceAVL(currNode, type);
                }
            }
            return currNode;
        }
    }

    private AVLNode deleteNode(AVLNode currNode) {
        if (currNode.left == NIL && currNode.right == NIL) {
            return NIL;
        } else if (currNode.left != NIL && currNode.right == NIL) {
            return currNode.left;
        } else if (currNode.left == NIL && currNode.right != NIL) {
            return currNode.right;
        } else {
            returnPair rPair = deleteNodeHelp(currNode.right);
            currNode.item = rPair.item;
            currNode.right = rPair.node;
            currNode.height = 1 + Math.max(currNode.left.height, currNode.right.height);
            int type = needBalance(currNode);
            if (type != NO_NEED) {
                currNode = balanceAVL(currNode, type);
            }
            return currNode;
        }
    }

    private returnPair deleteNodeHelp(AVLNode currNode) {
        if (currNode.left == NIL) {
            return new returnPair(currNode.item, currNode.right);
        } else {
            returnPair rPair = deleteNodeHelp(currNode.left);
            currNode.left = rPair.node;
            rPair.node = currNode;
            return rPair;
        }
    }

    private class returnPair {
        private Comparable item;
        private AVLNode node;

        private returnPair(Comparable item, AVLNode node) {
            this.item = item;
            this.node = node;
        }
    }

    private final int ILLEGAL = -1, NO_NEED = 0, LL = 1, LR = 2, RR = 3, RL = 4;
    private int needBalance(AVLNode currNode) {
        int type = ILLEGAL;
        if (currNode.left.height >= currNode.right.height + 2) {
            if (currNode.left.left.height >= currNode.left.right.height) {
                type = LL;
            } else {
                type = LR;
            }
        } else if (currNode.right.height >= currNode.left.height + 2) {
            if (currNode.right.right.height >= currNode.right.left.height) {
                type = RR;
            } else {
                type = RL;
            }
        } else {
            type = NO_NEED;
        }
        return type;
    }

    private AVLNode balanceAVL(AVLNode currNode, int type) {
        AVLNode returnNode = NIL;
        switch (type) {
            case LL :
                returnNode = rightRotate(currNode);
                break;
            case LR :
                returnNode.left = leftRotate(currNode.left);
                returnNode = rightRotate(currNode);
                break;
            case RR :
                returnNode = leftRotate(currNode);
                break;
            case RL :
                returnNode.right = rightRotate(currNode.right);
                returnNode = leftRotate(currNode);
                break;
            default :
                break;
        }
        return returnNode;
    }

    private AVLNode leftRotate(AVLNode currNode) {
        AVLNode RChild = currNode.right;
        if (RChild == NIL) {
            return NIL;
        }
        AVLNode RLChild = RChild.left;
        RChild.left = currNode;
        currNode.right = RLChild;
        currNode.height = 1 + Math.max(currNode.left.height, currNode.right.height);
        RChild.height = 1 + Math.max(RChild.left.height, RChild.right.height);
        return RChild;
    }

    private AVLNode rightRotate(AVLNode currNode) {
        AVLNode LChild = currNode.left;
        if (LChild == NIL) {
            return NIL;
        }
        AVLNode LRChild = LChild.right;
        LChild.right = currNode;
        currNode.left = LRChild;
        currNode.height = 1 + Math.max(currNode.left.height, currNode.right.height);
        LChild.height = 1 + Math.max(LChild.left.height, LChild.right.height);
        return LChild;
    }

    @Override
    public boolean isEmpty() {
        return root == NIL;
    }

    @Override
    public void clear() {
        root = NIL;
    }
}
