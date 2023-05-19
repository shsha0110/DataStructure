import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.nio.file.*;
import java.io.IOException;

public class TestSearch {
    private static class Pair<K, V> {
        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    public static Pair<Character, Long> runSelectedSort(int[] values) {

        char sortType = DoSearch(values);

        long startTime = System.currentTimeMillis();
        switch (sortType) {
            case 'B':
                DoBubbleSort(values);
                break;
            case 'I':
                DoInsertionSort(values);
                break;
            case 'H':
                DoHeapSort(values);
                break;
            case 'M':
                DoMergeSort(values);
                break;
            case 'Q':
                DoQuickSort(values);
                break;
            case 'R':
                DoRadixSort(values);
                break;
        }
        long endTime = System.currentTimeMillis();
        return new Pair<>(sortType, endTime - startTime);
    }

    public static Pair<Character, Long> runAllSorts(int[] array) {
        long startTime = System.currentTimeMillis();
        char[] sorts = {'B', 'I', 'H', 'M', 'Q', 'R'};
        long shortestTime = Long.MAX_VALUE;
        char fastestSort = ' ';

        for (char sort : sorts) {
            int[] copiedArray = Arrays.copyOf(array, array.length);
            long sortStartTime = System.currentTimeMillis();
            switch (sort) {
                case 'B':
                    DoBubbleSort(copiedArray);
                    break;
                case 'I':
                    DoInsertionSort(copiedArray);
                    break;
                case 'H':
                    DoHeapSort(copiedArray);
                    break;
                case 'M':
                    DoMergeSort(copiedArray);
                    break;
                case 'Q':
                    DoQuickSort(copiedArray);
                    break;
                case 'R':
                    DoRadixSort(copiedArray);
                    break;
            }
            long sortEndTime = System.currentTimeMillis();
            long elapsedTime = sortEndTime - sortStartTime;
            if (elapsedTime < shortestTime) {
                shortestTime = elapsedTime;
                fastestSort = sort;
            }
        }

        long endTime = System.currentTimeMillis();
        return new Pair<>(fastestSort, endTime - startTime);
    }

    public static void main(String[] args) {
        int trials = 1000;
        long totalSelectedSortTime = 0;
        long totalAllSortsTime = 0;
        int matchCount = 0;

        // Prepare the CSV writer
        String csvFile = "resultsOfDoSearch_1e3Trials.csv";
        try {
            Files.write(Paths.get(csvFile), "selectedSortType,selectedSortTime,allSortsType,allSortsTime\n".getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < trials; i++) {

            System.out.println(String.format("#%d TEST", i+1));

            int[] randomArray = generateRandomArray();
            int[] randomArrayCopy1 = Arrays.copyOf(randomArray, randomArray.length);
            int[] randomArrayCopy2 = Arrays.copyOf(randomArray, randomArray.length);

            Pair<Character, Long> selectedSortResult = runSelectedSort(randomArrayCopy1);
            Pair<Character, Long> allSortsResult = runAllSorts(randomArrayCopy2);

            System.out.println("\tSelected Sort (" + selectedSortResult.getKey() + ") Time = " + selectedSortResult.getValue());
            System.out.println("\tAll Sorts Fastest (" + allSortsResult.getKey() + ") Time = " + allSortsResult.getValue());

            totalSelectedSortTime += selectedSortResult.getValue();
            totalAllSortsTime += allSortsResult.getValue();

            if (selectedSortResult.getKey().equals(allSortsResult.getKey())) {
                matchCount++;
            } else {
                // If the selected sort methods differ, re-run the selected sorts and print times
                int[] randomArrayCopy3 = Arrays.copyOf(randomArray, randomArray.length);
                int[] randomArrayCopy4 = Arrays.copyOf(randomArray, randomArray.length);
                long startTime = System.nanoTime();
                switch (selectedSortResult.getKey()) {
                    case 'B':
                        DoBubbleSort(randomArrayCopy3);
                        break;
                    case 'I':
                        DoInsertionSort(randomArrayCopy3);
                        break;
                    case 'H':
                        DoHeapSort(randomArrayCopy3);
                        break;
                    case 'M':
                        DoMergeSort(randomArrayCopy3);
                        break;
                    case 'Q':
                        DoQuickSort(randomArrayCopy3);
                        break;
                    case 'R':
                        DoRadixSort(randomArrayCopy3);
                        break;
                }
                long endTime = System.nanoTime();
                long selectedSortReTime = (endTime - startTime);
                System.out.println("\t\tRe-run selected sort (" + selectedSortResult.getKey() + ") time = " + selectedSortReTime);

                startTime = System.nanoTime();
                switch (allSortsResult.getKey()) {
                    case 'B':
                        DoBubbleSort(randomArrayCopy4);
                        break;
                    case 'I':
                        DoInsertionSort(randomArrayCopy4);
                        break;
                    case 'H':
                        DoHeapSort(randomArrayCopy4);
                        break;
                    case 'M':
                        DoMergeSort(randomArrayCopy4);
                        break;
                    case 'Q':
                        DoQuickSort(randomArrayCopy4);
                        break;
                    case 'R':
                        DoRadixSort(randomArrayCopy4);
                        break;
                }
                endTime = System.nanoTime();
                long allSortsReTime = endTime - startTime;
                System.out.println("\t\tRe-run all sorts fastest (" + allSortsResult.getKey() + ") time = " + allSortsReTime);

                if (selectedSortReTime <= allSortsReTime) { matchCount++; }
            }

            // Write to CSV
            String csvLine = String.format("%c,%d,%c,%d\n", selectedSortResult.getKey(), selectedSortResult.getValue(), allSortsResult.getKey(), allSortsResult.getValue());
            try {
                Files.write(Paths.get(csvFile), csvLine.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }

            LocalTime now = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("종료시각 : HH시 mm분 ss초");
            String formatedNow = now.format(formatter);
            System.out.println(formatedNow);
        }

        double averageSelectedSortTime = totalSelectedSortTime / (double) trials;
        double averageAllSortsTime = totalAllSortsTime / (double) trials;
        double speedup = averageAllSortsTime / averageSelectedSortTime;
        double matchRate = (matchCount / (double) trials) * 100;

        String summaryLine = String.format("Average selected sort time: %f\nAverage all sorts time: %f\nOn average, runSelectedSort was %.2f times faster than runAllSorts.\nMatch rate of selected sort and all sorts fastest: %.2f percent\n", averageSelectedSortTime, speedup, averageAllSortsTime, matchRate);
        try {
            Files.write(Paths.get(csvFile), summaryLine.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Average selected sort time: " + averageSelectedSortTime);
        System.out.println("Average all sorts time: " + averageAllSortsTime);
        System.out.println("Selected sort is faster : " + speedup + " times than All Sorts");
        System.out.println("Match rate of selected sort and all sorts fastest: " + matchRate + "%");
    }

    private static double calculateSortedRate(int[] values) {
        int numSorted = 0;

        for (int i = 0; i < values.length - 1; i++) {
            if (values[i] <= values[i + 1]) {
                numSorted++;
            }
        }

        double sortedRate = (double) numSorted / (values.length - 1);
        return sortedRate;
    }

    private static double calculateReverseSortedRate(int[] values) {
        int numReverseSorted = 0;

        for (int i = 0; i < values.length - 1; i++) {
            if (values[i] >= values[i + 1]) {
                numReverseSorted++;
            }
        }

        double reverseSortedRate = (double) numReverseSorted / (values.length - 1);
        return reverseSortedRate;
    }

    private static char DoSearch(int[] value)
    {
        double sortedRate = calculateSortedRate(value);
        double reverseSortedRate = calculateReverseSortedRate(value);
        double standardDeviation = calculateStandardDeviation(value);

        if (sortedRate >= 0.99995) { return 'I'; }
        else if (reverseSortedRate >= 0.99) { return 'M'; }
        else if (standardDeviation <= 0.6) { return 'Q'; }
        else { return 'R'; }
    }

    public static double calculateStandardDeviation(int[] array) {
        double mean = 0.0;
        double s = 0.0;
        int length = array.length;

        for (int i = 0; i < length; i++) {
            double temp = array[i];
            double oldMean = mean;
            mean = oldMean + (temp - oldMean) / (i + 1);
            s = s + (temp - oldMean) * (temp - mean);
        }

        double variance = s / (length - 1);

        return Math.sqrt(variance);
    }

    public static int[] generateRandomArray() {
        Random rand = new Random();

        // Step 1: Generate two random numbers and assign min and max
        int num1 = rand.nextInt();
        int num2 = rand.nextInt();
        int min = Math.min(num1, num2);
        int max = Math.max(num1, num2);

        // Step 2: Generate an array of 50000 random numbers within the specified range
        int[] array = new int[50000];
        for (int i = 0; i < array.length; i++) {
            int bound = max - min <= 0 ? Integer.MAX_VALUE : max - min + 1;
            array[i] = min + rand.nextInt(bound);
        }

        // Step 3: Sort the array based on random probability
        double sortChance = rand.nextDouble();
        if (sortChance < 0.3) {
            Arrays.sort(array);
        } else if (sortChance < 0.6) {
            Arrays.sort(array);
            // Reverse the array
            for (int i = 0; i < array.length / 2; i++) {
                int temp = array[i];
                array[i] = array[array.length - 1 - i];
                array[array.length - 1 - i] = temp;
            }
        }

        // Step 4: Swap k*50000 random pairs in the array
        double k = rand.nextDouble();
        for (int i = 0; i < (int)(k * 50000); i++) {
            int idx1 = rand.nextInt(array.length);
            int idx2 = rand.nextInt(array.length);
            int temp = array[idx1];
            array[idx1] = array[idx2];
            array[idx2] = temp;
        }
        return array;
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
