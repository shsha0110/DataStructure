package queue;

public class ArrayQueue<E> implements QueueInterface<E> {
    private E queue[];
    private int front, tail, numItems;
    private static final int DEFAULT_CAPACITY = 64;
    private final E ERROR = null;

    public ArrayQueue() {
        queue = (E[]) new Object[DEFAULT_CAPACITY];
        front = 0;
        tail = DEFAULT_CAPACITY-1;
        numItems = 0;
    }

    public ArrayQueue(int n) {
        queue = (E[]) new Object[n];
        front = 0;
        tail = n-1;
        numItems = 0;
    }

    @Override
    public void enqueue(E x) {
        if (isFull()) {
            /* Error */
        } else {
            tail = (tail + 1) % queue.length;
            queue[tail] = x;
            numItems++;
        }
    }

    @Override
    public E dequeue() {
        if (isEmpty()) {
            return ERROR;
        } else {
            E temp = queue[front];
            front = (front + 1) % queue.length;
            numItems--;
            return temp;
        }
    }

    @Override
    public E front() {
        if (isEmpty()) {
            return ERROR;
        }
        return queue[front];
    }

    @Override
    public boolean isEmpty() {
        return numItems == 0;
    }

    public boolean isFull() {
        return numItems >= queue.length;
    }

    @Override
    public void dequeueAll() {
        queue = (E[]) new Object[queue.length];
        front = 0;
        tail = queue.length-1;
        numItems = 0;
    }
}
