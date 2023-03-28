package queue;

public class LinkedQueueTest {
    public static void main(String[] args) {
        LinkedQueue<String> q = new LinkedQueue<>();
        q.enqueue("test1");
        q.enqueue("test2");
        q.enqueue("test3");
        q.dequeue();
        q.dequeue();
        q.dequeue();
        System.out.println(q.isEmpty());
    }
}
