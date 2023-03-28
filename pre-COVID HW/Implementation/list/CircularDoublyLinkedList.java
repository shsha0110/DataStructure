package list;

import java.text.Bidi;

public class CircularDoublyLinkedList<E> implements ListInterface<E> {
    private BidirectionalNode<E> head;
    private int numItems;

    public CircularDoublyLinkedList() {
        numItems = 0;
        head = new BidirectionalNode<>(null);
        head.prev = head.next = head;
    }

    @Override
    public void add(int index, E x) {
        if (index > numItems || index < 0) {
            /* Error */
        } else {
            BidirectionalNode<E> prevNode = getNode(index-1);
            BidirectionalNode<E> currNode = prevNode.next;
            BidirectionalNode<E> newNode = new BidirectionalNode<>(prevNode, currNode, x);
            prevNode.next = newNode;
            currNode.prev = newNode;
            numItems++;
        }
    }

    @Override
    public void append(E x) {
        BidirectionalNode<E> prevNode = head.prev;
        BidirectionalNode<E> newNode = new BidirectionalNode<>(prevNode, head, x);
        prevNode.next = newNode;
        head.prev = newNode;
        numItems++;
    }

    @Override
    public E remove(int index) {
        if (index >= numItems || index < 0) {
            return null;
        }
        BidirectionalNode<E> currNode = getNode(index);
        currNode.prev.next = currNode.next;
        currNode.next.prev = currNode.prev;
        numItems--;
        return currNode.item;
    }

    @Override
    public boolean removeItem(E x) {
        BidirectionalNode<E> currNode = head;
        for (int i = 0; i < numItems; i++) {
            currNode = currNode.next;
            if (((Comparable)(currNode.item)).compareTo(x) == 0) {
                currNode.prev.next = currNode.next;
                currNode.next.prev = currNode.prev;
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

    public BidirectionalNode<E> getNode(int index) {
        if (index >= numItems || index < -1) {
            return null;
        }
        BidirectionalNode<E> currNode = head;
        if (index < numItems/2) {
            for (int i = 0; i <= index; i++) {
                currNode = currNode.next;
            }
        } else {
            for (int i = numItems-1; i >= index; i--) {
                currNode = currNode.prev;
            }
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
        BidirectionalNode<E> currNode = head;
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
        head.prev = head.next = head;
    }

    @Override
    public String toString() {
        String result = "";
        BidirectionalNode<E> currNode = head;
        for (int i = 0; i < numItems; i++) {
            currNode = currNode.next;
            result += currNode.item.toString() + " ";
        }
        return result;
    }
}
