package stack;
import list.Node;

public class LinkedStack<E> implements StackInterface<E> {
    private Node<E> topNode;
    private final E ERROR = null;

    public LinkedStack() {
        topNode = null;
    }

    @Override
    public void push(E item) {
        topNode = new Node<>(item, topNode);
    }

    @Override
    public E pop() {
        if (isEmpty()) {
            return ERROR;
        } else {
            Node<E> temp = topNode;
            topNode = topNode.next;
            return temp.item;
        }
    }

    @Override
    public E top() {
        if (isEmpty()) {
            return ERROR;
        }
        return topNode.item;
    }

    @Override
    public boolean isEmpty() {
        return topNode == null;
    }

    @Override
    public void popAll() {
        topNode = null;
    }
}
