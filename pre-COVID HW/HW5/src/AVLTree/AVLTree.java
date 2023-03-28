package AVLTree;

import LinkedList.*;

public class AVLTree {
    private AVLNode root;
    static final AVLNode NIL = new AVLNode(new LinkedList<Pair>(), null, null, 0);

    public AVLTree() {
        root = NIL;
    }

    /** 검색 **/
    public AVLNode search(LinkedList<Pair> x) {
        return searchItem(root, x);
    }

    private AVLNode searchItem(AVLNode currNode, LinkedList<Pair> x) {
        if (currNode == NIL) {
            return NIL;
        }
        if (x.compareTo(currNode.item) == 0) {
            return currNode;
        } else if (x.compareTo(currNode.item) < 0) {
            return searchItem(currNode.left, x);
        } else if (x.compareTo(currNode.item) > 0) {
            return searchItem(currNode.right, x);
        }
        return NIL;
    }

    /** 삽입 **/
    public void insert(LinkedList<Pair> x) {
        root = insertItem(root, x);
    }

    private AVLNode insertItem(AVLNode currNode, LinkedList<Pair> x) {
        if (currNode == NIL) {
            currNode = new AVLNode(x);
        } else {
            if ((x.key).compareTo(currNode.key) == 0) {
                /** key collision **/
                currNode.item.append(x.first());
            } else if ((x.key).compareTo(currNode.key) < 0) {
                currNode.left = insertItem(currNode.left, x);
                currNode.height = 1 + Math.max(currNode.left.height, currNode.right.height);
                int type = needBalance(currNode);
                if (type != NO_NEED) {
                    currNode = balanceAVL(currNode, type);
                }
            } else if ((x.key).compareTo(currNode.key) > 0) {
                currNode.right = insertItem(currNode.right, x);
                currNode.height = 1 + Math.max(currNode.left.height, currNode.right.height);
                int type = needBalance(currNode);
                if (type != NO_NEED) {
                    currNode = balanceAVL(currNode, type);
                }
            }
        }
        return currNode;
    }

    /** 삭제 **/
    public void delete(LinkedList<Pair> x) {
        root = deleteItem(root, x);
    }

    public AVLNode deleteItem(AVLNode currNode, LinkedList<Pair> x) {
        if (currNode == NIL) {
            return NIL;
        }
        if (x.compareTo(currNode.item) == 0) {
            return deleteNode(currNode);
        } else if (x.compareTo(currNode.item) < 0) {
            currNode.left = deleteItem(currNode.left, x);
            currNode.height = 1 + Math.max(currNode.left.height, currNode.right.height);
            int type = needBalance(currNode);
            if (type != NO_NEED) {
                currNode = balanceAVL(currNode, type);
            }
        } else if (x.compareTo(currNode.item) > 0) {
            currNode.right = deleteItem(currNode.right, x);
            currNode.height = 1 + Math.max(currNode.left.height, currNode.right.height);
            int type = needBalance(currNode);
            if (type != NO_NEED) {
                currNode = balanceAVL(currNode, type);
            }
        }
        return currNode;
    }

    private AVLNode deleteNode(AVLNode currNode) {
        if (currNode.left == NIL && currNode.right == NIL) {
            return NIL;
        } else if (currNode.left != NIL && currNode.right == NIL) {
            return currNode.left;
        } else if (currNode.left == NIL && currNode.right != NIL) {
            return currNode.right;
        } else if (currNode.left != NIL && currNode.right != NIL) {
            returnPair rPair = deleteMinItem(currNode.right);
            currNode.item = rPair.item;
            currNode.right = rPair.node;
            currNode.height = 1 + Math.max(currNode.left.height, currNode.right.height);
            int type = needBalance(currNode);
            if (type != NO_NEED) {
                currNode = balanceAVL(currNode, type);
            }
            return currNode;
        }
        return NIL;
    }

    public class returnPair {
        LinkedList<Pair> item;
        AVLNode node;

        public returnPair(LinkedList<Pair> item, AVLNode node) {
            this.item = item;
            this.node = node;
        }
    }

    private returnPair deleteMinItem(AVLNode currNode) {
        if (currNode.left == NIL) {
            return new returnPair(currNode.item, currNode.right);
        }
        returnPair rPair = deleteMinItem(currNode.left);
        currNode.left = rPair.node;
        currNode.height = 1 + Math.max(currNode.left.height, currNode.right.height);
        int type = needBalance(currNode);
        if (type != NO_NEED) {
            currNode = balanceAVL(currNode, type);
        }
        rPair.node = currNode;
        return rPair;
    }

    /** 균형잡기 **/
    private AVLNode balanceAVL(AVLNode currNode, int type) {
        AVLNode returnNode = NIL;
        switch (type) {
            case LL :
                returnNode = rightRotate(currNode);
                break;
            case LR :
                currNode.left = leftRotate(currNode.left);
                returnNode = rightRotate(currNode);
                break;
            case RL :
                currNode.right = rightRotate(currNode.right);
                returnNode = leftRotate(currNode);
                break;
            case RR :
                returnNode = leftRotate(currNode);
            default :
                break;
        }
        return returnNode;
    }

    /** 회전 **/
    static final int ILLEGAL = -1, NO_NEED = 0, LL = 1, LR = 2, RR = 3, RL = 4;

    private int needBalance(AVLNode rootNode) {
        int type = ILLEGAL;
        if (rootNode.left.height >= rootNode.right.height + 2) {
            if (rootNode.left.left.height >= rootNode.left.right.height) {
                type = LL;
            } else {
                type = LR;
            }
        } else if (rootNode.left.height + 2 <= rootNode.right.height) {
            if (rootNode.right.right.height >= rootNode.right.left.height) {
                type = RR;
            } else {
                type = RL;
            }
        } else {
            type = NO_NEED;
        }
        return type;
    }

    /** 좌회전 **/
    private AVLNode leftRotate(AVLNode rootNode) {
        AVLNode RChild = rootNode.right;
        if (RChild != NIL) {
            AVLNode RLChild = RChild.left;
            RChild.left = rootNode;
            rootNode.right = RLChild;
            rootNode.height = 1 + Math.max(rootNode.left.height, rootNode.right.height);
            RChild.height = 1 + Math.max(RChild.left.height, RChild.right.height);
        }
        return RChild;
    }

    /** 우회전 **/
    private AVLNode rightRotate(AVLNode rootNode) {
        AVLNode LChild = rootNode.left;
        if (LChild != NIL) {
            AVLNode LRChild = LChild.right;
            LChild.right = rootNode;
            rootNode.left = LRChild;
            rootNode.height = 1 + Math.max(rootNode.left.height, rootNode.right.height);
            LChild.height = 1 + Math.max(LChild.left.height, LChild.right.height);
        }
        return LChild;
    }

    /** 기타 **/
    public boolean isEmpty() {
        return root == NIL;
    }

    public void clear() {
        root = NIL;
    }

    /** 순회 **/
    public LinkedList<AVLNode> preorder() {
        LinkedList<AVLNode> visited = new LinkedList<AVLNode>();
        preorderHelp(root, visited);
        return visited;
    }

    private void preorderHelp(AVLNode currNode, LinkedList<AVLNode> visited) {
        if (currNode == NIL) {
            return;
        }
        visit(currNode, visited);
        for (AVLNode child : currNode.child) {
            preorderHelp(child, visited);
        }
    }

    private void visit(AVLNode currNode, LinkedList<AVLNode> visited) {
        visited.append(currNode);
    }

}
