package queue;

public class ArrayQueueTest {
    public static void main(String[] args) {
        ArrayQueue<String> s = new ArrayQueue<>();
        s.enqueue("test1");
        s.enqueue("test2");
        s.enqueue("test3");
        s.dequeue();
    }
}
