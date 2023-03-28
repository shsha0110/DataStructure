import AVLTree.*;
import HashTable.*;
import LinkedList.*;

import java.io.*;
import java.util.Scanner;

public class Matching
{
	public static HashTable hashTable;
	public static LinkedList<String> corpus;

	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;
				command(input);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input)
	{
		// TODO
		// TASK1 ) Extract mark
		char mark = input.charAt(0);
		// TASK2 ) Call function differently depending on mark
		switch (mark) {
			// 데이터 입력 : < (FILENAME)
			case '<' :
				initializeHashTable();
				String FILENAME = input.split("\\s")[1];
				constructCorpus(FILENAME);
				inputData();
				return;
			// 저장된 데이터 출력 : @ (INDEX NUMBER)
			case '@' :
				int INDEX_NUMBER = Integer.parseInt(input.split("\\s")[1]);
				printData(INDEX_NUMBER);
				return;
			// 패턴 검색 : ? (PATTERN)
			case '?' :
				String PATTERN = input.split("\\s")[1];
				matchPattern(PATTERN);
				return;
			default :
				return;
		}
	}

	private static void initializeHashTable() {
		hashTable = new HashTable(100);
	}

	private static void constructCorpus(String filename) {
		// TASK1 ) Initialize corpus
		corpus = new LinkedList<String>();
		// TASK2 ) Open file
		File file = new File(filename);
		// TASK3 ) Process file
		try (Scanner input = new Scanner(file)) {
			while (input.hasNext()) {
				// TASK3.1 ) Read line
				String line = input.nextLine();
				// TASK3.2 ) Append new line in corpus
				corpus.append(line);
			}
		} catch (IOException ioException) {
			// Process IOException
		}
	}

	/** 데이터 입력 **/
	private static void inputData() {
		for (int lineNum = 1; lineNum <= corpus.len(); lineNum++) {
			int index = lineNum - 1;
			// TASK1 ) Get line
			String line = corpus.get(index);
			// TASK2 ) Slice sub-strings
			String[] subStrings = sliceSubStrings(line, 6);
			// TASK3 ) Hash sub-strings
			hashSubStrings(subStrings, lineNum);
		}
	}

	private static String[] sliceSubStrings(String line, int subStringLength) {
		// TASK1 ) Initialize sub-string array
		String[] subStrings = new String[line.length() - subStringLength + 1];
		// TASK2 )
		for (int i = 0; i <= line.length() - subStringLength; i++) {
			// TASK2.1 ) Slice sub-string
			String subString = line.substring(i, i + subStringLength);
			// TASK2.2 ) Append
			subStrings[i] = subString;
		}
		return subStrings;
	}

	private static void hashSubStrings(String[] subStrings, int lineNum) {
		for (int startPoint = 0; startPoint < subStrings.length; startPoint++) {
			String subString = subStrings[startPoint];
			// TASK1 ) Create item object
			LinkedList<Pair> item = new LinkedList<Pair>(subString, new Pair(lineNum, startPoint + 1));
			// TASK2 ) Insert item in hash table
			hashTable.insert(item);
		}
	}

	/** 저장된 데이터 출력 **/
	private static void printData(int indexNumber) {
		// TASK1 ) Get slot in the index number
		AVLTree slot = hashTable.getSlot(indexNumber);
		// TASK2 ) Pre-order traversal
		LinkedList<AVLNode> nodes = slot.preorder();
		// TASK3 ) Aggregate keys of nodes
		String result = "";
		for (int i = 0; i < nodes.len(); i++) {
			AVLNode node = nodes.get(i);
			result += node.item.key + " ";
		}
		// TASK4 ) Trim
		result = result.trim();
		// TASK5 ) Print
		if (result.isEmpty() || result.isBlank()) {
			System.out.println("EMPTY");
		} else {
			System.out.println(result);
		}
	}

	/** 패턴 검색 **/
	private static void matchPattern(String pattern) {
		String result = "";
		for (int lineNum = 1; lineNum <= corpus.len(); lineNum++) {
			int index = lineNum - 1;
			// TASK1 ) Get line
			String line = corpus.get(index);
			// TASK2 ) Detect pattern
			LinkedList<Pair> detected = detectPattern(line, pattern, lineNum);
			// TASK3 ) Aggregate pairs
			for (int i = 0; i < detected.len(); i++) {
				Pair node = detected.get(i);
				result += node.toString() + " ";
			}
		}
		// TASK4 ) Trim
		result = result.trim();
		// TASK5 ) Print
		if (result.isEmpty() || result.isBlank()) {
			System.out.println("(0, 0)");
		} else {
			System.out.println(result);
		}
	}

	private static LinkedList<Pair> detectPattern(String line, String pattern, int lineNum) {
		// TASK1 ) Initialize linked list that contains pair
		LinkedList<Pair> detected = new LinkedList<Pair>();
		// TASK2 ) Calculate pattern length
		int patternLen = pattern.length();
		// TASK3 )
		for (int startPoint = 0; startPoint <= line.length() - patternLen; startPoint++) {
			// TASK3.1 ) Slice sub-string
			String subString = line.substring(startPoint, startPoint + patternLen);
			// TASK3.2 ) Check
			if (subString.equals(pattern)) {
				// TASK3.2.1 ) Create pair
				Pair pair = new Pair(lineNum, startPoint + 1);
				// TASK3.2.2 ) Append
				detected.append(pair);
			}
		}
		return detected;
	}
}
