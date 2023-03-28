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

				String command = br.readLine();

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
					for (int i = 0; i < newvalue.length; i++)
					{
						System.out.println(newvalue[i]);
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
		return value;
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
	private static int[] DoMergeSort(int[] value)
	{
		// TODO : Merge Sort 를 구현하라.
		int start = 0, end = value.length-1;
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
			int boundary = partition(list, start, end);
			quickSort(list, start, boundary-1);
			quickSort(list, boundary+1, end);
		}
	}

	private static int partition(int[] list, int start, int end) {
		int criterion = list[end];
		int frontBoundary = start-1, backBoundary = start;

		for (int i = start; i < end; i++) {
			backBoundary = i;
			if (list[i] <= criterion) {
				frontBoundary++;
				int temp = list[frontBoundary];
				list[frontBoundary] = list[backBoundary];
				list[backBoundary] = temp;
			}
		}

		int temp = list[frontBoundary + 1];
		list[frontBoundary + 1] = list[end];
		list[end] = temp;

		return frontBoundary + 1;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////

	// TODO
	private static int[] DoRadixSort(int[] value) {
		int[] cnt = new int[10], start = new int[10];
		int[] B = new int[value.length];
		int max = -1;
		for (int i = 0; i < value.length; i++) {
			if (value[i] > max) {
				max = value[i];
			}
		}
		int numDigits = (int)Math.log10(max) + 1;
		for (int digit = 1; digit <= numDigits; digit++) {
			for (int d = 0; d <= 9; d++) {
				cnt[d] = 0;
			}
			for (int i = 0; i < value.length; i++) {
				cnt[(int)(value[i] / Math.pow(10, digit-1)) % 10]++;
			}
			start[0] = 0;
			for (int d = 1; d <= 9; d++) {
				start[d] = start[d-1] + cnt[d-1];
			}
			for (int i = 0; i < value.length; i++) {
				B[start[(int)(value[i] / Math.pow(10, digit-1)) % 10]++] = value[i];
			}
			for (int i = 0; i < value.length; i++) {
				value[i] = B[i];
			}
		}
		return value;
	}

//	private static int[] DoRadixSort(int[] value)
//	{
//		// TODO : Radix Sort 를 구현하라.
//		int maxNumOfDigits = getMaxNumOfDigits(value);
//		radixSort(value, maxNumOfDigits);
//		return (value);
//	}
//
//	private static int getMaxNumOfDigits(int[] array) {
//		int maxNumOfDigits = 0;
//		for (int num : array) {
//			int numOfDigits = 0;
//			while (num >= 1) {
//				num /= 10;
//				numOfDigits++;
//			}
//			maxNumOfDigits = numOfDigits > maxNumOfDigits ? numOfDigits : maxNumOfDigits;
//		}
//		return maxNumOfDigits;
//	}
//
//	private static void radixSort(int[] array, int maxNumOfDigits) {
//		for (int nod = 0; nod < maxNumOfDigits; nod++) {
//			for (int i = 1; i < array.length; i++) {
//				for (int j = i; j > 0; j--) {
//					if (positionDigit(array[j], nod) < positionDigit(array[j-1], nod)) {
//						int temp = array[j];
//						array[j] = array[j-1];
//						array[j-1] = temp;
//					}
//				}
//			}
//		}
//	}
//
//	private static int positionDigit(int number, int numOfDigits) {
//		return (number - ((number / (int)Math.pow(10, numOfDigits+1)) * (int)Math.pow(10, numOfDigits+1))) / (int)Math.pow(10, numOfDigits);
//	}
}
