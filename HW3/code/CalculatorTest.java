import java.io.*;
import java.util.Stack;

public class CalculatorTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);
			} catch (Exception e) {
				printErrorMessage();
			}
		}
	}

	private static void command(String input) throws Exception {
		// TASK1 ) Convert infix expression ot postfix expression
		String postfix = convertInfixToPostfix(input);
		// TASK2 ) Print postfix expression
		printPostfix(postfix);
		// TASK3 ) Evaluate postfix expression
		int result = evaluatePostfix(postfix);
		// TASK4 ) Print result
		printResult(result);
		return;
	}

	private static String convertInfixToPostfix(String input) throws Exception {
		// TASK1 ) Eliminate whitespace
		String infix = eliminateWhitespace(input);
		// TASK2 ) Notate unary '-' differently
		infix = notateUnaryNegation(infix);
		// TASK3 ) Initialize postfix, operation stack
		String postfix = "";
		Stack<Integer> operandStack = new Stack<>();
		Stack<Character> operatorStack = new Stack<>();
		// TASK4 )
		for (int i = 0; i < infix.length(); i++) {
			// TASK4.1 ) operand
			char element = infix.charAt(i);
			// TASK4.2 ) Process digit(atom of operand)
			if (Character.isDigit(element)) {
				if (!operandStack.isEmpty()) {
					// TASK4.2.1 ) Update operand in stack
					int temp = operandStack.pop();
					temp = temp * 10 + (element - '0');
					operandStack.push(temp);
				} else {
					// TASK4.2.2 ) Store new operand in stack
					operandStack.push((element - '0'));
				}
				continue;
			}
			// TASK4.3 ) Process operator
			if (isOperator(element) || element == '(') {
				if (!operandStack.isEmpty()) {
					// TASK4.3.1 ) Combine complete operand to ultimate postfix
					int operand = operandStack.pop();
					postfix += " " + Integer.toString(operand);
				}
				if (!operatorStack.isEmpty() && precedence(element) < precedence(operatorStack.lastElement())) {
					char operator = operatorStack.pop();
					// TASK4.3.2 ) Combine complete operator to ultimate postfix
					postfix += " " + operator;
				}
				// TASK4.3.3 ) Store new operator in stack
				operatorStack.push(element);
				continue;
			}
			// TASK4.4 ) Process open-parentheses
			if (element == '(') {

			}
			// TASK4.5 ) Process close-parentheses
			if (element == ')') {
				if (!operandStack.isEmpty()) {
					// TASK4.5.1 ) Combine complete operand to ultimate postfix
					int operand = operandStack.pop();
					postfix += " " + Integer.toString(operand);
				}
				if (!operatorStack.isEmpty()) {
					// TASK4.5.2 ) Combine complete operator to ultimate postfix
					char operator = operatorStack.pop();
					postfix += " " + operator;
				}
				continue;
			}
			throw new Exception("Invalid Expression");
		}
		// TASK5 ) Pop until there's no element in operator stack
		while (!operandStack.isEmpty()) {
			int operand = operandStack.pop();
			postfix += " " + Integer.toString(operand);
		}
		// TASK5 ) Pop until there's no element in operator stack
		while (!operatorStack.isEmpty()) {
			char operator = operatorStack.pop();
			postfix += " " + operator;
		}

		postfix = refinePostfix(postfix);

		return postfix;
	}

	private static String eliminateWhitespace(String input) {
		String infix = input.replaceAll("\\s", "").replaceAll("\\t", "");
		return infix;
	}

	private static String notateUnaryNegation(String input) {
		String infix = "";
		for (int i = 0; i < input.length(); i++) {
			char element = input.charAt(i);
			if (element == '-') {
				if (i == 0) {
					infix += "~";
					continue;
				} else {
					char previousElement = input.charAt(i - 1);
					if (!Character.isDigit(previousElement) && previousElement != ')') {
						infix += "~";
						continue;
					}
				}
			}
			infix += Character.toString(element);
		}
		return infix;
	}

	private static int precedence(char operator) throws Exception {
		switch (operator) {
			case '+' :
				return 0;
			case '-' :
				return 0;
			case '*' :
				return 1;
			case '/' :
				return 1;
			case '%' :
				return 1;
			case '~' :
				return 2;
			case '^' :
				return 3;
			case '(' :
				return 4;
			case ')' :
				return 4;
			default :
				throw new Exception("Invalid Expression");
		}
	}

	private static String refinePostfix(String postfix) {
		postfix = postfix.replaceAll("(\\(\\s)", "");
		postfix = postfix.trim();
		return postfix;
	}

	private static void printPostfix(String postfix) {
		System.out.println(postfix);
	}

	private static int evaluatePostfix(String postfix) throws Exception {
		// TASK1 ) Initialize operands and operator
		int operand1, operand2;
		char operator;
		// TASK2 ) Initialize operation stack
		Stack<Integer> operationStack = new Stack<>();
		// TASK4 )
		// TASK4.1 ) Initialize boolean value which store if type of last element is integer
		boolean lastIsDigit = false;
		for (int i = 0; i < postfix.length(); i++) {
			// TASK4.2 ) Index character at the index
			char element = postfix.charAt(i);
			// TASK4.3 ) Digit check
			if (Character.isDigit(element)) {
				// TASK4.4 ) Last check
				if (lastIsDigit) {
					// TASK4.5 ) Combine last integer with new integer
					int temp = operationStack.pop();
					temp = temp * 10 + (element - '0');
					// TASK4.6 ) Push
					operationStack.push(temp);
				} else {
					// TASK4.7 ) Push
					operationStack.push((element - '0'));
				}
				// TASK4.8 ) Update "lastIsInteger"
				lastIsDigit = true;
				continue;
			}
			// TASK4.4 ) Operator check
			if (isOperator(element)) {
				// TASK4.4.1 ) Update "lastIsInteger"
				lastIsDigit = false;
				// TASK4.4.2 ) Set operator
				operator = element;
				// TASK4.4.3 ) Unary check
				if (isUnaryOperator(element)) {
					// TASK4.4.3.1 ) Pop one operand
					operand1 = operationStack.pop();
					// TASK4.4.3.2 ) Operation
					int temp = operate(operator, operand1);
					// TASK4.4.3.3 ) Push operation result
					operationStack.push(temp);
				} else {
					// TASK4.4.3.4 ) Pop two operands
					operand2 = operationStack.pop();
					operand1 = operationStack.pop();
					// TASK4.4.3.5 ) Operation
					int temp = operate(operator, operand1, operand2);
					// TASK4.4.3.6 ) Push operation result
					operationStack.push(temp);
				}
				continue;
			}
			// TASK4.5 ) Ignore whitespace
			if (Character.isWhitespace(element)) {
				lastIsDigit = false;
				continue;
			}
			// TASK4.6 ) Process Exception
			throw new Exception("Invalid Expression");
		}
		// TASK5 ) Return result
		int result = operationStack.pop();
		return result;
	}

	private static boolean isOperator(char element) {
		return element == '+' || element == '-' || element == '*' || element == '/' || element == '%' || element == '^' || element == '~';
	}

	private static boolean isUnaryOperator(char element) {
		return element == '~';
	}

	private static int operate(char operator, int operand) {
		return (- operand);
	}
	private static int operate(char operator, int operand1, int operand2) throws Exception {
		switch (operator) {
			case '+':
				return operand1 + operand2;
			case '-' :
				return operand1- operand2;
			case '*' :
				return operand1 * operand2;
			case '/' :
				if (operand2 != 0)
					return operand1 / operand2;
			case '%' :
				if (operand2 != 0)
					return operand1 % operand2;
			case '^' :
				if (!(operand1 == 0 && operand2 < 0))
					return (int) Math.pow(operand1, operand2);
			default :
				throw new Exception("Impossible Operation");
		}
	}

	private static void printResult(int result) {
		System.out.println(result);
	}

	private static void printErrorMessage() {
		System.out.println("ERROR");
	}

}
