import java.io.*;
import java.util.*;

public class SortingTest
{
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
					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue);
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

	////////////////////////////////////////////////////////////////////////////////////////////////////
    private static char DoSearch(int[] value)
	{
		return (' ');
	}



}
