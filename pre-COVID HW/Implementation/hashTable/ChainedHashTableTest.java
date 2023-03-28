package hashTable;
import list.Node;

public class ChainedHashTableTest {
    public static void main(String[] args) {
        ChainedHashTable h = new ChainedHashTable(11);
        h.insert(10);
        h.delete(20);
        h.insert(20);
        h.insert(5);
        h.insert(80);
        h.insert(90);
        h.delete(20);
        h.delete(44);

        Node<Integer> nd = h.search(80);
        if (nd == null) {
            System.out.println("Search Failed");
        } else {
            System.out.println("Found : " + nd.item);
        }
    }
}
