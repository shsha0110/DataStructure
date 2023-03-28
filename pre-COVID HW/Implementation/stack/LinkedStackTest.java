package stack;

public class LinkedStackTest {
    public static void main(String[] args) {
        LinkedStack<Integer> s = new LinkedStack<>();
        s.push(300);
        s.push(200);
        s.push(100);
        s.pop();
    }
}
