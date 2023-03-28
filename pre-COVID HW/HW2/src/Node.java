public class Node<T> {
    private T item;
    private Node<T> next;

    public Node(T obj) {
        this.item = obj;
        this.next = null;
    }
    
    public Node(T obj, Node<T> next) {
    	this.item = obj;
    	this.next = next;
    }
    
    public final T getItem() {
    	return item;
    }
    
    public final void setItem(T item) {
    	this.item = item;
    }
    
    public final void setNext(Node<T> next) {
    	this.next = next;
    }
    
    public Node<T> getNext() {
    	return this.next;
    }
    
    public final void insertNext(T obj) {
		// TODO

        Node<T> currNode = this;
        Node<T> nextNode = currNode.next;
        Node<T> newNode = new Node(obj, nextNode);

        currNode.next = newNode;

    }
    
    public final void removeNext() {
        // TODO

        Node<T> currNode = this;
        Node<T> nextNode = currNode.next;

        if (nextNode != null) { currNode.next = nextNode.next; }

    }
}