import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class TestSortingAlgorithmsWithNearlyReverseSortedArray2 {
    private static final String[] algorithms = {"H", "M", "Q", "R"};
    private static final int ARRAY_SIZE = 1000000;
    private static final double INCREMENT = 0.000001;
    private static Random rand = new Random();

    public static void main(String[] args) {
        try (FileWriter csvWriter = new FileWriter("232_resultsDependingOnReverseSortedRatio_size1e6.csv")) {
            csvWriter.append("Algorithm,swapRate,ReverseSortedRatio,ExecutionTime\n");

            int cnt = 0;
            for (double swapRate = 0; swapRate <= 1; swapRate += INCREMENT) {
                System.out.println(String.format("started : %.6f", swapRate));

                int[] originalValues = generateNearlyReverseSortedArray(swapRate, ARRAY_SIZE);
                double reverseSortedRatio = calculateReverseSortedRatio(originalValues);

                for (String algorithm : algorithms) {
                    System.out.println(String.format("\t started : %s", algorithm));
                    int[] values = originalValues.clone();

                    long startTime = System.currentTimeMillis();
                    sort(algorithm, values);
                    long endTime = System.currentTimeMillis();
                    long executionTime = endTime - startTime;

                    if (executionTime > 1000) { cnt++; }

                    csvWriter.append(String.join(",", algorithm, String.format("%.6f",swapRate), String.format("%.6f", reverseSortedRatio), String.valueOf(executionTime)));
                    csvWriter.append("\n");
                }

                if (cnt >= 10) {break;}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] generateNearlyReverseSortedArray(double swapRate, int size) {
        Random rand = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = size - i;
        }

        int swapCount = (int) (size * swapRate);
        for (int i = 0; i < swapCount; i++) {
            int idx1 = rand.nextInt(size);
            int idx2 = rand.nextInt(size);
            int temp = array[idx1];
            array[idx1] = array[idx2];
            array[idx2] = temp;
        }

        return array;
    }

    public static double calculateReverseSortedRatio(int[] array) {
        int reverseSortedPairCount = 0;
        int totalPairs = array.length - 1;

        for (int i = 0; i < totalPairs; i++) {
            if (array[i] >= array[i+1]) {
                reverseSortedPairCount++;
            }
        }

        return (double) reverseSortedPairCount / totalPairs;
    }

    private static void sort(String algorithm, int[] values) {
        switch (algorithm) {
            case "B":
                DoBubbleSort(values);
                break;
            case "I":
                DoInsertionSort(values);
                break;
            case "H":
                DoHeapSort(values);
                break;
            case "M":
                DoMergeSort(values);
                break;
            case "Q":
                DoQuickSort(values);
                break;
            case "R":
                DoRadixSort(values);
                break;
            default:
                throw new IllegalArgumentException("Invalid algorithm: " + algorithm);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoBubbleSort(int[] value) {
        for (int i = value.length - 1; i >= 1; i--) {
            boolean swapped = false;
            for (int j = 0; j < i; j++) {
                if (value[j] > value[j + 1]) {
                    int temp = value[j];
                    value[j] = value[j + 1];
                    value[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
        return value;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoInsertionSort(int[] value)
    {
        for (int i = 1; i < value.length; i++) {
            insert(value, i);
        }
        return (value);
    }

    private static void insert(int[] value, int lastIndex) {
        for (int i = lastIndex; i > 0; i--) {
            if (value[i] < value[i-1]) {
                int temp = value[i];
                value[i] = value[i-1];
                value[i-1] = temp;
            }
            else { return; }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoHeapSort(int[] value)
    {
        buildHeap(value);
        int temp;
        for (int i = value.length-1; i >= 1; i--) {
            temp = value[0];
            value[0] = value[i];
            value[i] = temp;
            percolateDown(value, 0, i-1);
        }
        return (value);
    }
    private static void buildHeap(int[] array) {
        if (array.length >= 2) {
            for (int i = (array.length-2)/2; i >=0; i--) {
                percolateDown(array, i, array.length-1);
            }
        }
    }

    private static void percolateDown(int[] array, int curr, int n) {
        int child = curr * 2 + 1;
        int rightChild = curr * 2 + 2;
        if (child <= n) {
            if (rightChild <= n && array[child] < array[rightChild]) {
                child = rightChild;
            }
            if (array[curr] < array[child]) {
                int temp = array[curr];
                array[curr] = array[child];
                array[child] = temp;
                percolateDown(array, child, n);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoMergeSort(int[] value) {
        int[] copy = value.clone();
        mergeSort(0, value.length - 1, value, copy);
        return value;
    }

    private static void mergeSort(int start, int end, int[] A, int[] B) {
        if (start < end) {
            int mid = start + (end - start) / 2;
            mergeSort(start, mid, B, A);
            mergeSort(mid + 1, end, B, A);
            merge(start, mid, end, B, A);
        } else if (start == end) {
            A[start] = B[start];
        }
    }

    private static void merge(int start, int mid, int end, int[] C, int[] D) {
        int i = start, j = mid + 1, t = start;
        while (i <= mid && j <= end) {
            if (C[i] <= C[j]) {
                D[t++] = C[i++];
            } else {
                D[t++] = C[j++];
            }
        }
        while (i <= mid) {
            D[t++] = C[i++];
        }
        while (j <= end) {
            D[t++] = C[j++];
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoQuickSort(int[] value) {
        int start = 0, end = value.length - 1;
        quickSort(value, start, end);
        return (value);
    }

    private static void quickSort(int[] list, int start, int end) {
        if (start < end) {
            int[] pivots = partition(list, start, end);
            quickSort(list, start, pivots[0] - 1);
            quickSort(list, pivots[1] + 1, end);
        }
    }

    private static int[] partition(int[] list, int start, int end) {
        int mid = start + (end - start) / 2;
        int pivotValue = medianOfThree(list[start], list[mid], list[end]);
        int pivotIndex = -1;
        if (pivotValue == list[start]) {
            pivotIndex = start;
        } else if (pivotValue == list[mid]) {
            pivotIndex = mid;
        } else {
            pivotIndex = end;
        }
        int temp = list[pivotIndex];
        list[pivotIndex] = list[end];
        list[end] = temp;

        int pivot = list[end];
        int less = start;
        int greater = end;
        int i = start;
        while (i <= greater) {
            if (list[i] < pivot) {
                temp = list[i];
                list[i++] = list[less];
                list[less++] = temp;
            } else if (list[i] > pivot) {
                temp = list[i];
                list[i] = list[greater];
                list[greater--] = temp;
            } else {
                i++;
            }
        }

        return new int[] { less, greater };
    }

    private static int medianOfThree(int a, int b, int c) {
        if (a < b) {
            if (b < c) {
                return b;
            } else if (a < c) {
                return c;
            } else {
                return a;
            }
        } else {
            if (a < c) {
                return a;
            } else if (b < c) {
                return c;
            } else {
                return b;
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoRadixSort(int[] value) {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < value.length; i++) {
            if (value[i] > max) {
                max = value[i];
            }
            if (value[i] < min) {
                min = value[i];
            }
        }

        int maxValue = Math.max(Math.abs(min), Math.abs(max));
        int numDigits = (int) (Math.log10(maxValue)) + 1;
        int[] output = new int[value.length];

        for (int digit = 0; digit < numDigits; digit++) {
            int[] count = new int[19]; // Range: -9 to 9
            int div = (int) Math.pow(10, digit);

            for (int i = 0; i < value.length; i++) {
                int targetNum = value[i] / div % 10;
                count[targetNum + 9]++;
            }

            for (int i = 1; i < count.length; i++) {
                count[i] += count[i - 1];
            }

            for (int i = value.length - 1; i >= 0; i--) {
                int targetNum = value[i] / div % 10;
                output[--count[targetNum + 9]] = value[i];
            }

            System.arraycopy(output, 0, value, 0, value.length);
        }

        return value;
    }
}
