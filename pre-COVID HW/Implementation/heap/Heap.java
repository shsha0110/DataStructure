package heap;

public class Heap<E extends Comparable> implements PQInterface<E> {
    private E[] A;
    private int numItems;

    public Heap(int size) {
        A = (E[]) new Comparable[size];
        numItems = 0;
    }

    public Heap(E[] B, int numItems) {
        A = B;
        this.numItems = numItems;
    }

    @Override
    public void insert(E x) throws PQException {
        if (numItems >= A.length) {
            throw new PQException("HeapErr : Insert()-Overflow!");
        } else {
            A[numItems] = x;
            percolateUp(numItems);
            numItems++;
        }
    }

    private void percolateUp(int i) {
        int parent = (i - 1) / 2;
        if (parent >= 0 && A[i].compareTo(A[parent]) > 0) {
            E temp = A[i];
            A[i] = A[parent];
            A[parent] = temp;
            percolateUp(parent);
        }
    }

    @Override
    public E deleteMax() throws PQException {
        if (isEmpty()) {
            throw new PQException("HeapErr : DeleteMax()-Underflow");
        }
        E max = A[0];
        A[0] = A[numItems-1];
        numItems--;
        percolateDown(0);
        return max;
    }

    private void percolateDown(int i) {
        int child = 2 * i + 1;
        int rightChild = 2 * i + 2;
        if (child <= numItems-1) {
            if (rightChild <= numItems-1 && A[child].compareTo(A[rightChild]) < 0) {
                child = rightChild;
            }
            if (A[i].compareTo(A[child]) < 0) {
                E temp = A[i];
                A[i] = A[child];
                A[child] = temp;
                percolateDown(child);
            }
        }
    }

    public void buildHeap() {
        if (numItems >= 2) {
            for (int i = (numItems - 2) / 2; i >= 0; i--) {
                percolateDown(i);
            }
        }
    }

    @Override
    public E max() throws Exception {
        if (isEmpty()) {
            throw new PQException("HeapErr : Max()-Empty");
        }
        return A[0];
    }

    @Override
    public boolean isEmpty() {
        return numItems == 0;
    }

    @Override
    public void clear() {
        A = (E[]) new Comparable[A.length];
        numItems = 0;
    }
}
