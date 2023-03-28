package stack;

public class ArrayStackTest {
    public static void main(String[] args) {
        ArrayStack<Integer> s = new ArrayStack<>();
        s.push(300);
        s.push(200);
        s.push(100);
        s.pop();
    }
}
