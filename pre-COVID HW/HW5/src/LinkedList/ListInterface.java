package LinkedList;

public interface ListInterface<T> {
    public void add(int i, T x);

    public void append(T x);

    public T remove(int i);

    public boolean removeItem(T x);

    public T get(int i);

    public void set(int i, T x);

    public int indexOf(T x);

    public int len();

    public boolean isEmpty();

    public void clear();

    public T first();
}
