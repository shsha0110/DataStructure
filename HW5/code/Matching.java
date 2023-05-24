import java.io.*;
import java.util.Scanner;

public class Matching
{
	////////////////////////////////////////////////////////////////////////////////////////////////////
	/** Inner Class1 : Pair **/
	public static class Pair implements Comparable<Pair> {
		public int lineNum, startPoint;

		public Pair(int lineNum, int startPoint) {
			this.lineNum = lineNum;
			this.startPoint = startPoint;
		}

		@Override
		public String toString() {
			return String.format("(%d, %d)", lineNum, startPoint);
		}

		@Override
		public int compareTo(Pair pair) {
			if (lineNum != pair.lineNum) {
				return lineNum > pair.lineNum ? 1 : -1;
			}
			if (startPoint != pair.startPoint) {
				return startPoint > pair.startPoint ? 1 : -1;
			}
			return 0;
		}

	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	/** Inner Class2 : Node **/
	public static class Node<T> {
		private T item;
		private Node<T> next;

		public Node(T obj) {
			this.item = obj;
			this.next = null;
		}

		public Node(T obj, Node<T> next) {
			this.item = obj;
			this.next = next;
		}

		public final T getItem() {
			return item;
		}

		public final void setItem(T item) {
			this.item = item;
		}

		public final void setNext(Node<T> next) {
			this.next = next;
		}

		public Node<T> getNext() {
			return this.next;
		}

		public final void insertNext(T obj) {
			Node<T> currNode = this;
			Node<T> nextNode = currNode.next;
			Node<T> newNode = new Node<T>(obj, nextNode);

			currNode.setNext(newNode);
		}

		public final void removeNext() {
			Node<T> currNode = this;
			Node<T> nextNode = currNode.next;

			if (nextNode != null) { currNode.next = nextNode.next; }
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	/** Inner Class3 : Linked List **/
	public static class LinkedList<T> implements Comparable {
		public String key;
		public Node<T> head;
		private int numItems;

		public LinkedList() {
			key = "";
			head = new Node<T>(null);
			numItems = 0;
		}

		public LinkedList(String key) {
			this.key = key;
			head = new Node<T>(null);
			numItems = 0;
		}

		public LinkedList(String key, T item) {
			this.key = key;
			head = new Node<T>(null);
			head.insertNext(item);
			numItems = 1;
		}

		public void add(int i, T x) {
			Node<T> currNode = head;
			for (int index = 0; index < i; index++) {
				currNode = currNode.getNext();
			}
			currNode.insertNext(x);
			numItems++;
		}

		public void append(T x) {
			Node<T> prevNode = null;
			Node<T> currNode = head;
			while (currNode != null) {
				prevNode = currNode;
				currNode = currNode.getNext();
			}
			prevNode.insertNext(x);
			numItems++;
		}

		public T remove(int i) {
			Node<T> currNode = head;
			for (int index = 0; index < i; index++) {
				if (currNode == null) {
					return null;
				}
				currNode = currNode.getNext();
			}
			Node<T> nextNode = currNode.getNext();
			currNode.removeNext();
			numItems--;
			return nextNode.getItem();
		}

		public boolean removeItem(T x) {
			Node<T> prevNode = null;
			Node<T> currNode = head;
			for (int index = 0; index < numItems; index++) {
				prevNode = currNode;
				currNode = currNode.getNext();
				if (x.equals(currNode.getItem())) {
					prevNode.removeNext();
					numItems--;
					return true;
				}
			}
			return false;
		}

		public T get(int i) {
			Node<T> currNode = head;
			for (int index = 0; index <= i; index++) {
				if (currNode == null) {
					return null;
				}
				currNode = currNode.getNext();
			}
			return currNode.getItem();
		}

		public void set(int i, T x) {
			Node<T> currNode = head;
			for (int index = 0; index <= i; index++) {
				if (currNode == null) {
					return;
				}
				currNode = currNode.getNext();
			}
			currNode.setItem(x);
		}

		public int indexOf(T x) {
			Node<T> currNode = head;
			for (int index = 0; index < numItems; index++) {
				currNode = currNode.getNext();
				if (x.equals(currNode.getItem())) {
					return index;
				}
			}
			return -1;
		}

		public int len() {
			return numItems;
		}

		public boolean isEmpty() {
			return head.getNext() == null;
		}

		public void clear() {
			head.setNext(null);
		}

		public T first() {
			if (head.getNext() == null) {
				return null;
			}
			return head.getNext().getItem();
		}

		public int compareTo(Object o) {
			LinkedList<T> obj = (LinkedList<T>) o;
			return key.compareTo(obj.key);
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	/** Inner Class4 : AVLNode **/
	public static class AVLNode {
		public String key;
		public LinkedList<Pair> item;
		public AVLNode left, right;
		public int height;

		public AVLNode(LinkedList<Pair> item) {
			this.key = item.key;
			this.item = item;
			this.left = this.right = AVLTree.NIL;
			this.height = 1;
		}

		public AVLNode(LinkedList<Pair> item, AVLNode leftChild, AVLNode rightChild) {
			this.key = item.key;
			this.item = item;
			this.left = leftChild;
			this.right = rightChild;
			this.height = 1;
		}

		public AVLNode(LinkedList<Pair> item, AVLNode leftChild, AVLNode rightChild, int height) {
			this.key = item.key;
			this.item = item;
			this.left = leftChild;
			this.right = rightChild;
			this.height = height;
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	/** Inner Class5 : AVLTree **/
	public static class AVLTree {
		private AVLNode root;
		static final AVLNode NIL = new AVLNode(new LinkedList<Pair>(), null, null, 0);

		public AVLTree() {
			root = NIL;
		}

		/** 검색 **/
		public AVLNode search(LinkedList<Pair> x) {
			return searchItem(root, x);
		}

		private AVLNode searchItem(AVLNode currNode, LinkedList<Pair> x) {
			if (currNode == NIL) {
				return NIL;
			}
			if ((x.key).equals(currNode.key) && (x.key).compareTo(currNode.key) == 0) {
				return currNode;
			} else if ((x.key).compareTo(currNode.key) < 0) {
				return searchItem(currNode.left, x);
			} else if ((x.key).compareTo(currNode.key) > 0) {
				return searchItem(currNode.right, x);
			}
			return NIL;
		}

		/** 삽입 **/
		public void insert(LinkedList<Pair> x) {
			root = insertItem(root, x);
		}

		private AVLNode insertItem(AVLNode currNode, LinkedList<Pair> x) {
			if (currNode == NIL) {
				currNode = new AVLNode(x);
			} else {
				if ((x.key).equals(currNode.key) && (x.key).compareTo(currNode.key) == 0) {
					/** key collision **/
					currNode.item.append(x.first());
				} else if ((x.key).compareTo(currNode.key) < 0) {
					currNode.left = insertItem(currNode.left, x);
					currNode.height = 1 + Math.max(currNode.left.height, currNode.right.height);
					int type = needBalance(currNode);
					if (type != NO_NEED) {
						currNode = balanceAVL(currNode, type);
					}
				} else if ((x.key).compareTo(currNode.key) > 0) {
					currNode.right = insertItem(currNode.right, x);
					currNode.height = 1 + Math.max(currNode.left.height, currNode.right.height);
					int type = needBalance(currNode);
					if (type != NO_NEED) {
						currNode = balanceAVL(currNode, type);
					}
				}
			}
			return currNode;
		}

		/** 삭제 **/
		public void delete(LinkedList<Pair> x) {
			root = deleteItem(root, x);
		}

		public AVLNode deleteItem(AVLNode currNode, LinkedList<Pair> x) {
			if (currNode == NIL) {
				return NIL;
			}
			if ((x.key).equals(currNode.key) && (x.key).compareTo(currNode.key) == 0) {
				return deleteNode(currNode);
			} else if ((x.key).compareTo(currNode.key) < 0) {
				currNode.left = deleteItem(currNode.left, x);
				currNode.height = 1 + Math.max(currNode.left.height, currNode.right.height);
				int type = needBalance(currNode);
				if (type != NO_NEED) {
					currNode = balanceAVL(currNode, type);
				}
			} else if ((x.key).compareTo(currNode.key) > 0) {
				currNode.right = deleteItem(currNode.right, x);
				currNode.height = 1 + Math.max(currNode.left.height, currNode.right.height);
				int type = needBalance(currNode);
				if (type != NO_NEED) {
					currNode = balanceAVL(currNode, type);
				}
			}
			return currNode;
		}

		private AVLNode deleteNode(AVLNode currNode) {
			if (currNode.left == NIL && currNode.right == NIL) {
				return NIL;
			} else if (currNode.left != NIL && currNode.right == NIL) {
				return currNode.left;
			} else if (currNode.left == NIL && currNode.right != NIL) {
				return currNode.right;
			} else if (currNode.left != NIL && currNode.right != NIL) {
				returnPair rPair = deleteMinItem(currNode.right);
				currNode.item = rPair.item;
				currNode.right = rPair.node;
				currNode.height = 1 + Math.max(currNode.left.height, currNode.right.height);
				int type = needBalance(currNode);
				if (type != NO_NEED) {
					currNode = balanceAVL(currNode, type);
				}
				return currNode;
			}
			return NIL;
		}

		public class returnPair {
			LinkedList<Pair> item;
			AVLNode node;

			public returnPair(LinkedList<Pair> item, AVLNode node) {
				this.item = item;
				this.node = node;
			}
		}

		private returnPair deleteMinItem(AVLNode currNode) {
			if (currNode.left == NIL) {
				return new returnPair(currNode.item, currNode.right);
			}
			returnPair rPair = deleteMinItem(currNode.left);
			currNode.left = rPair.node;
			currNode.height = 1 + Math.max(currNode.left.height, currNode.right.height);
			int type = needBalance(currNode);
			if (type != NO_NEED) {
				currNode = balanceAVL(currNode, type);
			}
			rPair.node = currNode;
			return rPair;
		}

		/** 균형잡기 **/
		private AVLNode balanceAVL(AVLNode currNode, int type) {
			AVLNode returnNode = NIL;
			switch (type) {
				case LL :
					returnNode = rightRotate(currNode);
					break;
				case LR :
					currNode.left = leftRotate(currNode.left);
					returnNode = rightRotate(currNode);
					break;
				case RL :
					currNode.right = rightRotate(currNode.right);
					returnNode = leftRotate(currNode);
					break;
				case RR :
					returnNode = leftRotate(currNode);
				default :
					break;
			}
			return returnNode;
		}

		/** 회전 **/
		static final int ILLEGAL = -1, NO_NEED = 0, LL = 1, LR = 2, RR = 3, RL = 4;

		private int needBalance(AVLNode rootNode) {
			int type = ILLEGAL;
			if (rootNode.left.height >= rootNode.right.height + 2) {
				if (rootNode.left.left.height >= rootNode.left.right.height) {
					type = LL;
				} else {
					type = LR;
				}
			} else if (rootNode.left.height + 2 <= rootNode.right.height) {
				if (rootNode.right.right.height >= rootNode.right.left.height) {
					type = RR;
				} else {
					type = RL;
				}
			} else {
				type = NO_NEED;
			}
			return type;
		}

		/** 좌회전 **/
		private AVLNode leftRotate(AVLNode rootNode) {
			AVLNode RChild = rootNode.right;
			if (RChild != NIL) {
				AVLNode RLChild = RChild.left;
				RChild.left = rootNode;
				rootNode.right = RLChild;
				rootNode.height = 1 + Math.max(rootNode.left.height, rootNode.right.height);
				RChild.height = 1 + Math.max(RChild.left.height, RChild.right.height);
			}
			return RChild;
		}

		/** 우회전 **/
		private AVLNode rightRotate(AVLNode rootNode) {
			AVLNode LChild = rootNode.left;
			if (LChild != NIL) {
				AVLNode LRChild = LChild.right;
				LChild.right = rootNode;
				rootNode.left = LRChild;
				rootNode.height = 1 + Math.max(rootNode.left.height, rootNode.right.height);
				LChild.height = 1 + Math.max(LChild.left.height, LChild.right.height);
			}
			return LChild;
		}

		/** 기타 **/
		public boolean isEmpty() {
			return root == NIL;
		}

		public void clear() {
			root = NIL;
		}

		/** 순회 **/
		public LinkedList<AVLNode> preorder() {
			LinkedList<AVLNode> visited = new LinkedList<AVLNode>();
			preorderHelp(root, visited);
			return visited;
		}

		private void preorderHelp(AVLNode currNode, LinkedList<AVLNode> visited) {
			if (currNode == NIL) {
				return;
			}
			visit(currNode, visited);
			AVLNode[] children = new AVLNode[]{currNode.left, currNode.right};
			for (AVLNode child : children) {
				preorderHelp(child, visited);
			}
		}

		private void visit(AVLNode currNode, LinkedList<AVLNode> visited) {
			visited.append(currNode);
		}

	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	/** Inner Class6 : HashTable **/
	public static class HashTable {
		private AVLTree[] table;
		int numItems;

		public HashTable(int n) {
			table = new AVLTree[n];
			for (int i = 0; i < n; i++) {
				table[i] = new AVLTree();
			}
			numItems = 0;
		}

		private int hash(LinkedList<Pair> input) {
			String key = input.key;
			int sumOfASCII = 0;
			for (int i = 0; i < key.length(); i++) {
				sumOfASCII += (int)(key.charAt(i));
			}
			return sumOfASCII % 100;
		}

		public void insert(LinkedList<Pair> input) {
			int slot = hash(input);
			table[slot].insert(input);
			numItems++;
		}

		public AVLNode search(LinkedList<Pair> input) {
			int slot = hash(input);
			return table[slot].search(input);
		}

		public void delete(LinkedList<Pair> input) {
			if (isEmpty()) {
				// Process Error
			} else {
				int slot = hash(input);
				table[slot].delete(input);
				numItems--;
			}
		}

		public boolean isEmpty() {
			return numItems <= 0;
		}

		public void clear() {
			for (int i = 0; i < table.length; i++) {
				table[i] = new AVLTree();
			}
			numItems = 0;
		}

		public AVLTree getSlot(int indexNumber) {
			if (indexNumber < 100) {
				return table[indexNumber];
			}
			return null;
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	/** Main **/
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
				String FILENAME = input.replaceFirst("<\\s", "");
				constructCorpus(FILENAME);
				initializeHashTable();
				constructHashTable();
				return;
			// 저장된 데이터 출력 : @ (INDEX NUMBER)
			case '@' :
				int INDEX_NUMBER = Integer.parseInt(input.replaceFirst("@\\s", ""));
				printData(INDEX_NUMBER);
				return;
			// 패턴 검색 : ? (PATTERN)
			case '?' :
				String PATTERN = input.replaceFirst("\\?\\s", "");
				matchPattern(PATTERN);
				return;
			// 문자열 삭제 : / (6의 길이를 가진 문자열)
			case '/' :
				String SUBSTRING = input.replaceFirst("/\\s", "");
				removeSubString(SUBSTRING);
				return;
			// 문장 추가 : + (문장)
			case '+' :
				String SENTENCE = input.replaceFirst("\\+\\s", "");
				appendSentence(SENTENCE);
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
	private static void constructHashTable() {
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
		// TASK1 ) Return empty array if length of input line is smaller than 6
		if (line.length() < 6) {
			return new String[0];
		}
		// TASK2 ) Initialize sub-string array
		String[] subStrings = new String[line.length() - subStringLength + 1];
		// TASK3 )
		for (int i = 0; i <= line.length() - subStringLength; i++) {
			// TASK3.1 ) Slice sub-string
			String subString = line.substring(i, i + subStringLength);
			// TASK3.2 ) Append
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

	private static void removeSubString(String substring) {
		// TASK1 ) Create linked list which of key is input sub-string
		LinkedList<Pair> targetPattern = new LinkedList<>(substring);
		// TASK2 ) Search AVLNode which of key is input sub-string
		AVLNode nodes = hashTable.search(targetPattern);
		// TASK3 ) Remove input sub-string in corpus
		removeSubStringInCorpus(substring);
		// TASK4 ) Reconstruct hash table
		initializeHashTable();
		constructHashTable();
		System.out.println(nodes.item.len());
	}

	private static void removeSubStringInCorpus(String substring) {
		for (int i = 0; i < corpus.len(); i++) {
			String updatedSentence = corpus.get(i).replace(substring, "");
			corpus.set(i, updatedSentence);
		}
	}

	private static void appendSentence(String sentence) {
		// TASK1 ) Append input sentence in corpus
		corpus.append(sentence);
		int lineNum = corpus.len();
		// TASK2 ) Slice sub-strings
		String[] subStrings = sliceSubStrings(sentence, 6);
		// TASK3 ) Hash sub-strings
		hashSubStrings(subStrings, lineNum);
		System.out.println(lineNum);
	}

}