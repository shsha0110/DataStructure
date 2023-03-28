package list;

public class ArrayListTest {
    public static void main(String[] args) {
        ArrayList<Integer> list= new ArrayList<>();
        list.add(0, 300);
        list.add(0, 200);
        list.add(0, 100);
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
