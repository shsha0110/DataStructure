package sorting;

public class Sorting {
    int A[];

    public Sorting(int B[]) {
        A = B;
    }

    public void printResult() {
        for (int i = 0; i < A.length; i++) {
            System.out.print(A[i]);
            System.out.print(" ");
        }
    }

    /** 1. 선택 정렬 **/
    public void selectionSort() {
        int k, temp;
        for (int last = A.length-1; last >= 1; last--) {
            k = theLargest(last);
            temp = A[k];
            A[k] = A[last];
            A[last] = temp;
        }
        printResult();
    }

    private int theLargest(int last) {
        int largest = 0;
        for (int i = 0; i <= last; i++) {
            if (A[i] > A[largest]) {
                largest = i;
            }
        }
        return largest;
    }

    /** 2. 버블 정렬 --> 문제 있음 **/
    public void bubbleSort() {
        int temp = 0;
        boolean swapped;
        for (int last = A.length-1; last >= 2; last--) {
            swapped = false;
            for (int i = 0; i <= last-1; i++) {
                if (A[i] > A[i+1]) {
                    temp = A[i];
                    A[i] = A[i+1];
                    A[i+1] = temp;
                    swapped = true;
                }
                if (!swapped) {
                    break;
                }
            }
        }
        temp = temp;
        printResult();
    }

    /** 3. 삽입 정렬 **/
    public void insertionSort() {
        for (int i = 1; i < A.length; i++) {
            int loc = i-1;
            int newItem = A[i];
            while (loc >= 0 && newItem < A[loc]) {
                A[loc+1] = A[loc];
                loc--;
            }
            A[loc+1] = newItem;
        }
        printResult();
    }

    /** 4. 병합 정렬 **/
    public void mergeSort() {
        int[] B = new int[A.length];
        mergeSortHelp(0, A.length-1, B);
        printResult();
    }

    private void mergeSortHelp(int start, int end, int[] B) {
        if (start < end) {
            int mid = (start + end) / 2;
            mergeSortHelp(start, mid, B);
            mergeSortHelp(mid+1, end, B);
            merge(start, mid, end, B);
        }
    }

    private void merge(int start, int mid, int end, int[] B) {
        int i = start;
        int j = mid + 1;
        int k = 0;
        while (i <= mid && j <= end) {
            if (A[i] <= A[j]) {
                B[k++] = A[i++];
            } else {
                B[k++] = A[j++];
            }
        }
        while (i <= mid) {
            B[k++] = A[i++];
        }
        while (j <= end) {
            B[k++] = A[j++];
        }
        i = start;
        k = 0;
        while (i <= end) {
            A[i++] = B[k++];
        }
    }

    /** 5. 퀵 정렬 **/
    public void quickSort() {
        quickSortHelp(0, A.length-1);
        printResult();
    }

    private void quickSortHelp(int start, int end) {
        if (start < end) {
            int boundary = partition(start, end);
            quickSortHelp(start, boundary-1);
            quickSortHelp(boundary+1, end);
        }
    }

    private int partition(int start, int end) {
        int x = A[end];
        int i = start-1;
        int temp;
        for (int j = start; j <= end-1; j++) {
            if (A[j] <= x) {
                i++;
                temp = A[i];
                A[i] = A[j];
                A[j] = temp;
            }
        }
        temp = A[i+1];
        A[i+1] = A[end];
        A[end] = temp;
        return i + 1;
    }

    /** 6. 힙 정렬 **/
    public void heapSort() {
        buildHeap();
        int temp;
        for (int i = A.length-1; i >= 1; i--) {
            temp = A[0];
            A[0] = A[i];
            A[i] = temp;
            percolateDown(0, i-1);
        }
        printResult();
    }

    private void buildHeap() {
        if (A.length >= 2) {
            for (int i = (A.length - 2) / 2; i >= 0; i--) {
                percolateDown(i, A.length-1);
            }
        }
    }

    private void percolateDown(int i, int n) {
        int child = 2 * i + 1;
        int rightChild = 2 * i + 2;
        if (child <= n) {
            if (rightChild <= n && A[child] < A[rightChild]) {
                child = rightChild;
            }
            if (A[i] < A[child]) {
                int temp = A[i];
                A[i] = A[child];
                A[child] = temp;
                percolateDown(child, n);
            }
        }
    }

    /** 7. 셸 정렬 **/
    public void shellSort() {
        for (int h = A.length/7; h > 5; h = h/5 -1) {
            for (int k = 0; k <= h-1; k++) {
                stepInsertionSort(k, h);
            }
        }
        stepInsertionSort(0, 1);
        printResult();
    }

    private void stepInsertionSort(int k, int h) {
        int j, insItem;
        for (int i = k + h; i <= A.length-1; i += h) {
            insItem = A[i];
            for (j = i-h; j >= 0 && A[j] > insItem; j -= h) {
                A[j + h] = A[j];
            }
            A[j + h] = insItem;
        }
    }

    /** 8. 계수 정렬 **/
    public int[] countingSort(int K) {
        int[] cnt = new int[K];
        for (int i = 0; i < K; i++) {
            cnt[i] = 0;
        }
        for (int i = 0; i < A.length; i++) {
            cnt[A[i]]++;
        }
        cnt[0]--;
        for (int i = 1; i < K; i++) {
            cnt[i] += cnt[i-1];
        }
        int[] B = new int[A.length];
        for (int j = A.length-1; j >= 0; j--) {
            B[cnt[A[j]]] = A[j];
            cnt[A[j]]--;
        }
        return B;
    }

    /** 9. 기수 정렬 **/
    public void radixSort() {
        int[] cnt = new int[10];
        int[] start = new int[10];
        int[] B = new int[A.length];
        int max = -1;
        for (int i = 0; i < A.length; i++) {
            if (A[i] > max) {
                max = A[i];
            }
        }
        int numDigits = (int) Math.log10(max) + 1;
        for (int digit = 1; digit <= numDigits; digit++) {
            for (int d = 0; d <= 9; d++) {
                cnt[d] = 0;
            }
            for (int i = 0; i <A.length; i++) {
                cnt[(int)(A[i] / Math.pow(10, digit-1)) % 10]++;
            }
            start[0] = 0;
            for (int d = 1; d <= 9; d++) {
                start[d] = start[d-1] + cnt[d-1];
            }
            for (int i = 0; i < A.length; i++) {
                B[start[(int)(A[i] / Math.pow(10, digit-1)) % 10]++] = A[i];
            }
            for (int i = 0; i < A.length; i++) {
                A[i] = B[i];
            }
        }
        printResult();
    }

    /** 10. 버킷 정렬 **/
//    public void bucketSort() {
//        intLinkedList B[];
//        int numLists = A.length;
//        B = new intLinkedList[numLists];
//        for (int i = 0; i < numLists; i++) {
//            B[i] = new intLinkedList();
//        }
//        int max;
//        if (A[0] < A[1]) {
//            max = 1;
//        } else {
//            max = 0;
//        }
//        for (int i = 2; i < A.length; i++) {
//            if (A[max] < A[i]) {
//                max = i;
//            }
//        }
//        int band = A[max] + 1;
//        int bucketID;
//        for (int i = 0; i < A.length; i++) {
//            bucketID = (int)((float)(A[i] / band) * numLists);
//            B[bucketID].add(0, A[i]);
//        }
//        int finger = 0, p, r = -1;
//        for (int i = 0; i < numLists; i++) {
//            for (int j = 0; j < B[i].len(); j++) {
//                A[finger++] = B[i].getNode(j).item;
//            }
//            p = r + 1; r = finger - 1;
//            rangeInsertionSort(p, r);
//        }
//    }
//
//    private void rangeInsertionSort(int p, int r) {
//        for (int i = p + 1; i <= r; i++) {
//            int loc = i - 1;
//            int x = A[i];
//            while (loc >= p && x < A[loc]) {
//                A[loc + 1] = A[loc];
//                loc--;
//            }
//            A[loc + 1] = x;
//        }
//    }
}
