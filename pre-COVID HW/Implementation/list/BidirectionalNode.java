package list;

public class BidirectionalNode<E> {
    public BidirectionalNode<E> prev;
    public E item;
    public BidirectionalNode<E> next;

    public BidirectionalNode(E item) {
        prev = next = null;
        this.item = item;
    }

    public BidirectionalNode(BidirectionalNode<E> prev, BidirectionalNode<E> next, E item) {
        this.prev = prev;
        this.next = next;
        this.item = item;
    }
}
