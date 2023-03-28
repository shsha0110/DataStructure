package LinkedList;

public class LinkedList<T> implements ListInterface<T>, Comparable {
    public String key;
    public Node<T> head;
    private int numItems;

    public LinkedList() {
        key = "";
        head = new Node<T>(null);
        numItems = 0;
    }

    public LinkedList(String key) {
        this.key = key;
        head = new Node<T>(null);
        numItems = 0;
    }

    public LinkedList(String key, T item) {
        this.key = key;
        head = new Node<T>(null);
        head.insertNext(item);
        numItems = 1;
    }

    @Override
    public void add(int i, T x) {
        Node<T> currNode = head;
        for (int index = 0; index < i; index++) {
            currNode = currNode.getNext();
        }
        currNode.insertNext(x);
        numItems++;
    }

    @Override
    public void append(T x) {
        Node<T> prevNode = null;
        Node<T> currNode = head;
        while (currNode != null) {
            prevNode = currNode;
            currNode = currNode.getNext();
        }
        prevNode.insertNext(x);
        numItems++;
    }

    @Override
    public T remove(int i) {
        Node<T> currNode = head;
        for (int index = 0; index < i; index++) {
            if (currNode == null) {
                return null;
            }
            currNode = currNode.getNext();
        }
        Node<T> nextNode = currNode.getNext();
        currNode.removeNext();
        numItems--;
        return nextNode.getItem();
    }

    @Override
    public boolean removeItem(T x) {
        Node<T> prevNode = null;
        Node<T> currNode = head;
        for (int index = 0; index < numItems; index++) {
            prevNode = currNode;
            currNode = currNode.getNext();
            if (x.equals(currNode.getItem())) {
                prevNode.removeNext();
                numItems--;
                return true;
            }
        }
        return false;
    }

    @Override
    public T get(int i) {
        Node<T> currNode = head;
        for (int index = 0; index <= i; index++) {
            if (currNode == null) {
                return null;
            }
            currNode = currNode.getNext();
        }
        return currNode.getItem();
    }

    @Override
    public void set(int i, T x) {
        Node<T> currNode = head;
        for (int index = 0; index <= i; index++) {
            if (currNode == null) {
                return;
            }
            currNode = currNode.getNext();
        }
        currNode.setItem(x);
    }

    @Override
    public int indexOf(T x) {
        Node<T> currNode = head;
        for (int index = 0; index < numItems; index++) {
            currNode = currNode.getNext();
            if (x.equals(currNode.getItem())) {
                return index;
            }
        }
        return -1;
    }

    @Override
    public int len() {
        return numItems;
    }

    @Override
    public boolean isEmpty() {
        return head.getNext() == null;
    }

    @Override
    public void clear() {
        head.setNext(null);
    }

    @Override
    public T first() {
        if (head.getNext() == null) {
            return null;
        }
        return head.getNext().getItem();
    }

    @Override
    public int compareTo(Object o) {
        LinkedList<T> obj = (LinkedList<T>) o;
        return key.compareTo(obj.key);
    }
}
