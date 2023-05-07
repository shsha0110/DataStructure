import java.io.*;
import java.util.*;

public class SortingTest
{
	private static int[] DoReversedMergeSort(int[] value) {
		// TODO : Merge Sort 를 구현하라.
		int start = 0, end = value.length - 1;
		reversedMergeSort(value, start, end);
		return (value);
	}

	private static void reversedMergeSort(int[] list, int start, int end) {
		// Base case
		if (start == end) {
			return;
		}
		// Recursive case
		int mid = start + (end - start) / 2;
		reversedMergeSort(list, start, mid);
		reversedMergeSort(list, mid+1, end);
		reversedMerge(list, start, mid, end);
	}

	private static void reversedMerge(int[] list, int start, int mid, int end) {
		int i = start, j = mid+1, k = start;
		int[] temp = new int[list.length];
		while (i <= mid && j <= end) {
			if (list[i] >= list[j]) {
				temp[k++] = list[i++];
			} else {
				temp[k++] = list[j++];
			}
		}
		while (i <= mid) {
			temp[k++] = list[i++];
		}
		while (j <= end) {
			temp[k++] = list[j++];
		}
		i = start;
		k = start;
		while (k <= end) {
			list[i++] = temp[k++];
		}
	}
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
			int[] value;	// 입력 받을 숫자들의 배열
			String nums = br.readLine();	// 첫 줄을 입력 받음
			if (nums.charAt(0) == 'r')
			{
				// 난수일 경우
				isRandom = true;	// 난수임을 표시

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
				int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
				int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

				Random rand = new Random();	// 난수 인스턴스를 생성한다.

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;

				/** 삭제해야함 **/
				if (nums.charAt(1) == 's') {
					value = DoMergeSort(value);
				}
				else if (nums.charAt(1) == 'r') {
					value = DoReversedMergeSort(value);
				}

			}
			else
			{
				// 난수가 아닐 경우
				int numsize = Integer.parseInt(nums);

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
					value[i] = Integer.parseInt(br.readLine());
			}

			// 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
			while (true)
			{
				int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.
                char algo = ' ';

				if (args.length == 4) {
                    return;
                }

				String command = args.length > 0 ? args[0] : br.readLine();

				if (args.length > 0) {
                    args = new String[4];
                }
				
				long t = System.currentTimeMillis();
				switch (command.charAt(0))
				{
					case 'B':	// Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':	// Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':	// Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M':	// Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;

					/** In-Place Merge Sort **/
					case 'm':
						newvalue = DoMergeSort2(newvalue);
						break;

					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue);
						break;

					/** Improved Quick Sort1 **/
					case 'q':
						newvalue = DoQuickSort2(newvalue);
						break;

					/** Improved Quick Sort2 **/
					case 'P':
						newvalue = DoQuickSort3(newvalue);
						break;

					/** Improved Quick Sort2 **/
					case 'p':
						newvalue = DoQuickSort4(newvalue);
						break;

					case 'R':	// Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'S':	// Search
						algo = DoSearch(newvalue);
						break;
					case 'X':
						return;	// 프로그램을 종료한다.
					default:
						throw new IOException("잘못된 정렬 방법을 입력했습니다.");
				}
				if (isRandom)
				{
					// 난수일 경우 수행시간을 출력한다.
					System.out.println((System.currentTimeMillis() - t) + " ms");
				}
				else
				{
					// 난수가 아닐 경우 정렬된 결과값을 출력한다.
                    if (command.charAt(0) != 'S') {
                        for (int i = 0; i < newvalue.length; i++) {
                            System.out.println(newvalue[i]);
                        }
                    } else {
                        System.out.println(algo);
                    }
				}

			}
		}
		catch (IOException e)
		{
			System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoBubbleSort(int[] value)
	{
		// TODO : Bubble Sort 를 구현하라.
		// value는 정렬안된 숫자들의 배열이며 value.length 는 배열의 크기가 된다.
		// 결과로 정렬된 배열은 리턴해 주어야 하며, 두가지 방법이 있으므로 잘 생각해서 사용할것.
		// 주어진 value 배열에서 안의 값만을 바꾸고 value를 다시 리턴하거나
		// 같은 크기의 새로운 배열을 만들어 그 배열을 리턴할 수도 있다.
		for (int i = value.length-1; i >= 1; i--) {
			int last = i;
			for (int j = 0; j < last; j++) {
				if (value[j] > value[j+1]) {
					int temp = value[j];
					value[j] = value[j+1];
					value[j+1] = temp;
				}
			}
		}
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoInsertionSort(int[] value)
	{
		// TODO : Insertion Sort 를 구현하라.
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
		// TODO : Heap Sort 를 구현하라.
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
		// TODO : Merge Sort 를 구현하라.
		int start = 0, end = value.length - 1;
		mergeSort(value, start, end);
		return (value);
	}

	private static void mergeSort(int[] list, int start, int end) {
		// Base case
		if (start == end) {
			return;
		}
		// Recursive case
		int mid = start + (end - start) / 2;
		mergeSort(list, start, mid);
		mergeSort(list, mid+1, end);
		merge(list, start, mid, end);
	}

	private static void merge(int[] array, int start, int mid, int end) {
		int index = start, frontIndex = 0, backIndex = 0;
		int[] frontSubList = Arrays.copyOfRange(array, start, mid+1);
		int[] backSubList = Arrays.copyOfRange(array, mid+1, end+1);

		while (frontIndex < frontSubList.length && backIndex < backSubList.length) {
			if (frontSubList[frontIndex] < backSubList[backIndex]) {
				array[index++] = frontSubList[frontIndex++];
			} else {
				array[index++] = backSubList[backIndex++];
			}
		}

		if (frontIndex < frontSubList.length) {
			while (frontIndex < frontSubList.length) {
				array[index++] = frontSubList[frontIndex++];
			}
		}
		if (backIndex < backSubList.length) {
			while (backIndex < backSubList.length) {
				array[index++] = backSubList[backIndex++];
			}
		}
	}

	private static int[] DoMergeSort2(int[] value) {
		// TODO : In Place Merge Sort 를 구현하라.
		int[] copy = new int[value.length];
		for (int i = 0; i < value.length; i++) {
			copy[i] = value[i];
		}
		mergeSort2(0, value.length-1, value, copy);
		return (value);
	}

	private static void mergeSort2(int start, int end, int[] A, int[] B) {
		if (start < end) {
			int mid = start + (end - start) / 2;
			mergeSort2(start, mid, B, A);
			mergeSort2(mid+1, end, B, A);
			merge2(start, mid, end, B, A);
		}
	}

	private static void merge2(int start, int mid, int end, int[] C, int[] D) {
		int i = start, j = mid+1, t = start;
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
	private static int[] DoQuickSort(int[] value)
	{
		// TODO : Quick Sort 를 구현하라.
		int start = 0, end = value.length-1;
		quickSort(value, start, end);
		return (value);
	}

	private static void quickSort(int[] list, int start, int end) {
		if (start < end) {
			int pivot = partition(list, start, end);
			quickSort(list, start, pivot-1);
			quickSort(list, pivot+1, end);
		}
	}

	private static int partition(int[] list, int start, int end) {
		int pivot = list[end];
		int i = start-1;
		for (int j = start; j < end; j++) {
			if (list[j] <= pivot) {
				int temp = list[++i];
				list[i] = list[j];
				list[j] = temp;
			}
		}
		int temp = list[i + 1];
		list[i + 1] = list[end];
		list[end] = temp;

		return i + 1;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoQuickSort2(int[] value)
	{
		// TODO : Quick Sort 를 구현하라.
		int start = 0, end = value.length-1;
		quickSort2(value, start, end);
		return (value);
	}
	private static void quickSort2(int[] list, int start, int end) {
		if (start < end) {
			int pivot = partition2(list, start, end);
			quickSort2(list, start, pivot-1);
			quickSort2(list, pivot+1, end);
		}
	}

	private static int partition2(int[] list, int start, int end) {
		int pivot = list[end];
		int i = start-1;
		for (int j = start; j < end; j++) {
			if (list[j] < pivot || (list[j] == pivot && j%2 == 0)) {
				int temp = list[++i];
				list[i] = list[j];
				list[j] = temp;
			}
		}
		int temp = list[i + 1];
		list[i + 1] = list[end];
		list[end] = temp;

		return i + 1;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoQuickSort3(int[] value)
	{
		// TODO : Quick Sort 를 구현하라.
		int start = 0, end = value.length-1;
		quickSort3(value, start, end);
		return (value);
	}

	private static void quickSort3(int[] list, int start, int end) {
		if (start < end) {
			int pivot = partition3(list, start, end);
			quickSort3(list, start, pivot-1);
			quickSort3(list, pivot+1, end);
		}
	}

	private static int partition3(int[] list, int start, int end) {
		int pivot = list[end];
		int i = start-1;
		for (int j = start; j < end; j++) {
			if (list[j] < pivot || (list[j] == pivot && (i - start + 1) < (j - i - 1))) {
				int temp = list[++i];
				list[i] = list[j];
				list[j] = temp;
			}
		}
		int temp = list[i + 1];
		list[i + 1] = list[end];
		list[end] = temp;

		return i + 1;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoQuickSort4(int[] value)
	{
		// TODO : Quick Sort 를 구현하라.
		int start = 0, end = value.length-1;
		quickSort4(value, start, end);
		return (value);
	}

	private static void quickSort4(int[] list, int start, int end) {
		if (start < end) {
			int[] partitions = partition4(list, start, end);
			quickSort4(list, start, partitions[0]-1);
			quickSort4(list, partitions[1]+1, end);
		}
	}

	private static int[] partition4(int[] list, int start, int end) {
		int pivot = list[end];
		int i = start-1, j = start, k = end;
		while (j < end && j < k) {
			if (list[j] < pivot) {
				int temp = list[++i];
				list[i] = list[j];
				list[j] = temp;
				j++;
			} else if (list[j] == pivot) {
				int temp = list[--k];
				list[k] = list[j];
				list[j] = temp;
			} else {
				j++;
			}
		}
		int last = end;
		for (int index = i+1; index < k; index++) {
			int temp = list[index];
			list[index] = list[last];
			list[last] = temp;
			last--;
		}
		int[] partitions = {i+1, i+1+(end-k)};
		return partitions;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoRadixSort(int[] value) {
		// TODO : Radix Sort 를 구현하라.
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

		int[] positiveValues = new int[value.length];
		int[] negativeValues = new int[value.length];
		int posIdx = 0;
		int negIdx = 0;

		// Separate positive and negative numbers
		for (int i = 0; i < value.length; i++) {
			if (value[i] >= 0) {
				positiveValues[posIdx++] = value[i];
			} else {
				negativeValues[negIdx++] = -value[i]; // Store as positive to apply radix sort
			}
		}

		// Resize the positive and negative arrays
		positiveValues = Arrays.copyOf(positiveValues, posIdx);
		negativeValues = Arrays.copyOf(negativeValues, negIdx);

		// Radix Sort for positive numbers
		positiveValues = radixSort(positiveValues, max);
		// Radix Sort for negative numbers
		negativeValues = radixSort(negativeValues, -min);

		// Merge the sorted arrays
		for (int i = 0; i < negIdx; i++) {
			value[i] = -negativeValues[negIdx - i - 1]; // Reverse and convert back to negative
		}
		System.arraycopy(positiveValues, 0, value, negIdx, posIdx);

		return (value);
	}

	private static int[] radixSort(int[] value, int maxValue) {
		int numDigits = (int) (Math.log10(maxValue)) + 1;
		for (int i = 0; i < numDigits; i++) {
			int[] cnt = new int[10];
			for (int j = 0; j < 10; j++) {
				cnt[i] = 0;
			}

			for (int j = 0; j < value.length; j++) {
				int targetNum = value[j] / (int) (Math.pow(10, i)) % 10;
				cnt[targetNum]++;
			}

			cnt[0]--;
			for (int j = 1; j < 10; j++) {
				cnt[j] += cnt[j - 1];
			}

			int[] copy = new int[value.length];
			for (int j = value.length - 1; j >= 0; j--) {
				int targetNum = value[j] / (int) (Math.pow(10, i)) % 10;
				copy[cnt[targetNum]--] = value[j];
			}

			for (int j = 0; j < copy.length; j++) {
				value[j] = copy[j];
			}
		}
		return value;
	}


	////////////////////////////////////////////////////////////////////////////////////////////////////
    private static char DoSearch(int[] value)
	{
		// TODO : Search 를 구현하라.
		return (' '); // 여러 조건을 설정하고, 각 조건에 따라 B, I, H, M, Q, R 중 하나를 리턴하라.
	}
}
