package list;

public class CircularLinkedListTest {
    public static void main(String[] args) {
        CircularLinkedList<Integer> list = new CircularLinkedList<>();
        list.add(0, 300);
        list.add(0,200);
        list.add(0,100);
        System.out.println(list);
        list.append(500);
        list.append(600);
        System.out.println(list);
        list.remove(3);
        System.out.println(list);
        list.add(3, 250);
        list.add(1, 50);
        list.add(0, 10);
        System.out.println(list);
        list.append(700);
        System.out.println(list);
        list.remove(1);
        list.removeItem(600);
        System.out.println(list);
    }
}
