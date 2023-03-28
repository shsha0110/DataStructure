package list;

public class ArrayList<E> implements ListInterface<E> {

    private E item[];
    private int numItems;
    private static final int DEFAULT_CAPACITY = 64;

    public ArrayList() {
        item = (E[]) new Object[DEFAULT_CAPACITY];
        numItems = 0;
    }

    public ArrayList(int n) {
        item = (E[]) new Object[n];
        numItems = 0;
    }

    @Override
    public void add(int index, E x) {
        if (index >= item.length || index > numItems || index < 0) {
            /* Error */
        } else {
            for (int i = numItems-1; i >= index; i--) {
                item[i+1] = item[i];
            }
            item[index] = x;
            numItems++;
        }
    }

    @Override
    public void append(E x) {
        if (numItems >= item.length) {
            /* Error */
        } else {
            item[numItems++] = x;
        }
    }

    @Override
    public E remove(int index) {
        if (isEmpty() || index < 0 || index > numItems-1) {
            return null;
        } else {
            E temp = item[index];
            for (int i = index+1; i < numItems; i++) {
                item[i-1] = item[i];
            }
            numItems--;
            return temp;
        }
    }

    @Override
    public boolean removeItem(E x) {
        int k = 0;
        while (k < numItems && ((Comparable)item[k]).compareTo(x) != 0) {
            k++;
        }
        if (k == numItems) {
            return false;
        }
        for (int i = k+1; i < numItems; i++) {
            item[i-1] = item[i];
        }
        numItems--;
        return true;
    }

    @Override
    public E get(int index) {
        if (index >= numItems || index < 0) {
            return null;
        }
        return item[index];
    }

    @Override
    public void set(int index, E x) {
        if (index >= numItems || index < 0) {
            /* Error*/
        } else {
            item[index] = x;
        }
    }

    private final int NOT_FOUND = -12345;
    @Override
    public int indexOf(E x) {
        for (int i = 0; i < numItems; i++) {
            if (((Comparable)item[i]).compareTo(x) == 0) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    @Override
    public int len() {
        return numItems;
    }

    @Override
    public boolean isEmpty() {
        return numItems == 0;
    }

    @Override
    public void clear() {
        item = (E[]) new Object[item.length];
        numItems = 0;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < numItems; i++) {
            result += item[i].toString() + " ";
        }
        return result;
    }
}
