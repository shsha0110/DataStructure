package list;

public class CircularLinkedList<E> implements ListInterface<E> {
    private Node<E> tail;
    private int numItems;

    public CircularLinkedList() {
        numItems = 0;
        tail = new Node(-1);
        tail.next = tail;
    }

    @Override
    public void add(int index, E x) {
        if (index > numItems || index < 0) {
            /* Error */
        } else {
            Node<E> prevNode = getNode(index-1);
            Node<E> newNode = new Node<>(x, prevNode.next);
            prevNode.next = newNode;
            if (index == numItems) {
                tail = newNode;
            }
            numItems++;
        }
    }

    @Override
    public void append(E x) {
        Node<E> prevNode = tail;
        Node<E> newNode = new Node<>(x, tail.next);
        prevNode.next = newNode;
        tail = newNode;
        numItems++;
    }

    @Override
    public E remove(int index) {
        if (index >= numItems || index < 0) {
            return null;
        }
        Node<E> prevNode = getNode(index-1);
        Node<E> currNode = prevNode.next;
        prevNode.next = currNode.next;
        if (index == numItems) {
            tail = prevNode;
        }
        numItems--;
        return currNode.item;
    }

    @Override
    public boolean removeItem(E x) {
        Node<E> currNode = tail.next;
        Node<E> prevNode;
        for (int i = 0; i < numItems; i++) {
            prevNode = currNode;
            currNode = currNode.next;
            if (((Comparable)(currNode.item)).compareTo(x) == 0) {
                prevNode.next = currNode.next;
                numItems--;
                return true;
            }
        }
        return false;
    }

    @Override
    public E get(int index) {
        if (index >= numItems || index < 0) {
            return null;
        }
        return getNode(index).item;
    }

    private Node<E> getNode(int index) {
        if (index >= numItems || index < -1) {
            return null;
        }
        Node<E> currNode = tail.next;
        for (int i = 0; i <= index; i++) {
            currNode = currNode.next;
        }
        return currNode;
    }

    @Override
    public void set(int index, E x) {
        if (index >= numItems || index < 0) {
            /* Error */
        } else {
            getNode(index).item = x;
        }
    }

    public final int NOT_FOUND = -12345;
    @Override
    public int indexOf(E x) {
        Node<E> currNode = tail.next;
        for (int i = 0; i < numItems; i++) {
            currNode = currNode.next;
            if (((Comparable)(currNode.item)).compareTo(x) == 0) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    @Override
    public int len() {
        return numItems;
    }

    @Override
    public boolean isEmpty() {
        return numItems == 0;
    }

    @Override
    public void clear() {
        numItems = 0;
        tail = new Node(-1);
        tail.next = tail;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < numItems; i++) {
            result += getNode(i).item.toString() + " ";
        }
        return result;
    }
}
