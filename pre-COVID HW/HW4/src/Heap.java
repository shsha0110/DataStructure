public class Heap {

    int[] heap;
    int numItems;

    Heap(int[] array) {
        buildHeap(array);
        numItems = array.length;
    }

    private void buildHeap(int[] array) {
        heap = array;
        if (numItems > 1) {
            for (int i = (numItems-2)/2; i >= 0; i--) {
                percolateDown(i);
            }
        }
    }

    public void percolateDown(int curr) {
        int child = curr * 2 + 1;
        int rightChild = curr * 2 + 2;
        if (child < numItems) {
            if (rightChild < numItems && heap[child] < heap[rightChild]) {
                child = rightChild;
            }
            if (heap[curr] < heap[child]) {
                int temp = heap[curr];
                heap[curr] = heap[child];
                heap[child] = temp;
                percolateDown(child);
            }
        }
    }

    public void insert(int x) {
        append(x);
        percolateUp(numItems);
    }

    private void percolateUp(int curr) {
        int parent = (curr - 1) / 2;
        if (parent >=0 && heap[curr] > heap[parent]) {
            int temp = heap[curr];
            heap[curr] = heap[parent];
            heap[parent] = temp;
            percolateUp(parent);
        }
    }

    private void append(int x) {
        numItems++;
        int[] appendedHeap = new int[numItems];
        for (int i = 0; i < numItems; i++) {
            if (i == numItems-1) {
                appendedHeap[i] = x;
            } else {
                appendedHeap[i] = heap[i];
            }
        }
        heap = appendedHeap;
    }

    public int deleteMax() throws EmptyHeapException {
        if (!isEmpty()) {
            int max = heap[0];
            heap[0] = heap[numItems-1];
            pop();
            percolateDown(0);
            return max;
        }
        throw new EmptyHeapException();
    }

    private void pop() {
        numItems--;
        int[] poppedHeap = new int[numItems];
        for (int i = 0; i < numItems; i++) {
            poppedHeap[i] = heap[i];
        }
        heap = poppedHeap;
    }

    public int max() throws EmptyHeapException {
        if (!isEmpty()) {
            return heap[0];
        } else {
            throw new EmptyHeapException();
        }
    }

    public boolean isEmpty() {
        return numItems == 0;
    }

    public void clear() {
        heap = new int[0];
        numItems = 0;
    }
}
