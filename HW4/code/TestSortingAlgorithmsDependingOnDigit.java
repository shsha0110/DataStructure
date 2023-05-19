import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;

public class TestSortingAlgorithmsDependingOnDigit {
    public static void main(String[] args) {
        String[] testCases = {
                "r 50000 -10 10",
                "r 50000 -100 100",
                "r 50000 -1000 1000",
                "r 50000 -10000 10000",
                "r 50000 -100000 100000",
                "r 50000 -1000000 1000000",
                "r 50000 -10000000 10000000",
                "r 50000 -100000000 100000000",
                "r 50000 -1000000000 1000000000",
        };

        String[] algorithms = {"B", "I", "H", "M", "Q", "R"};
        String[] algorithmNames = {"Bubble Sort", "Insertion Sort", "Heap Sort", "Merge Sort", "Quick Sort", "Radix Sort"};

        int testRuns = 10;

        try (FileWriter writer = new FileWriter("resultsDependingOnDigit_size50000.csv")) {
            writer.write("Test Case,Algorithm,Avg Time\n");

            for (String testCase : testCases) {
                System.out.println(String.format("started : %s", testCase));
                for (int algoIndex = 0; algoIndex < algorithms.length; algoIndex++) {
                    String algorithm = algorithms[algoIndex];
                    String algoName = algorithmNames[algoIndex];
                    long[] runTimes = new long[testRuns];
                    long totalTime = 0;

                    System.out.println(String.format("\tstarted : %s", algorithm));
                    for (int run = 0; run < testRuns; run++) {
                        System.out.println(String.format("\t\tstarted : #%d run", run+1));
                        int[] values = generateValues(testCase);
                        long startTime = System.nanoTime();
                        sort(algorithm, values);
                        long endTime = System.nanoTime();
                        long elapsedTime = endTime - startTime;

                        runTimes[run] = elapsedTime;
                        totalTime += elapsedTime;
                    }

                    double avgTime = totalTime / (double) testRuns;

                    writer.write(String.format("%s,%s,%.2f", testCase, algoName, avgTime));
                }
                LocalTime now = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("종료시각 : HH시 mm분 ss초");
                String formatedNow = now.format(formatter);
                System.out.println(formatedNow);
                System.out.println(String.format("finished : %s", testCase));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private static double calculateStdDev(long[] runTimes, double avgTime) {
        double sumSquares = 0.0;

        for (long runTime : runTimes) {
            sumSquares += Math.pow(runTime - avgTime, 2);
        }

        return Math.sqrt(sumSquares / runTimes.length);
    }

    private static int[] generateValues(String testCase) {
        String[] testCaseArgs = testCase.split(" ");
        int numSize = Integer.parseInt(testCaseArgs[1]);
        int rMinimum = Integer.parseInt(testCaseArgs[2]);
        int rMaximum = Integer.parseInt(testCaseArgs[3]);

        Random rand = new Random();
        int[] values = new int[numSize];

        for (int i = 0; i < numSize; i++) {
            values[i] = rand.nextInt(rMaximum - rMinimum + 1) + rMinimum; }
        if (testCaseArgs[0].equals("rs")) {
            Arrays.sort(values);
        } else if (testCaseArgs[0].equals("rr")) {
            Arrays.sort(values);
            for (int i = 0; i < numSize / 2; i++) {
                int temp = values[i];
                values[i] = values[numSize - 1 - i];
                values[numSize - 1 - i] = temp;
            }
        }
        return values;
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

