package sorting;

public class SortingTest {
    static final int NUM_SCALE = 100;

    public static void prepare(int A[]) {
        for (int i = 0; i < A.length; i++) {
            A[i]= (int)(NUM_SCALE * Math.random());
        }
    }

    public static void printResult(int[] A) {
        for (int i = 0; i < A.length; i++) {
            System.out.print(A[i]);
            System.out.print(" ");
        }
    }

    static final int SIZE = 10;
    public static void main(String[] args) {
        int[] A = new int[SIZE];
        prepare(A);
        Sorting s = new Sorting(A);
//        s.selectionSort();
//        s.bubbleSort();
//        s.insertionSort();
//        s.shellSort();
//        s.mergeSort();
//        s.quickSort();
//        s.heapSort();
//        A = s.countingSort(NUM_SCALE); printResult(A);
//        s.radixSort();
        //s.bucketSort();
    }
}
