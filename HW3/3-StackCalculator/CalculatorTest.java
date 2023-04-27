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
				//e.printStackTrace();
			}
		}
	}

	private static void command(String input) throws Exception {
		// TASK1 ) Convert infix expression ot postfix expression
		String postfix = convertInfixToPostfix(input);
		// TASK2 ) Evaluate postfix expression
		long result = evaluatePostfix(postfix);
		// TASK3 ) Print postfix expression
		printPostfix(postfix);
		// TASK4 ) Print result
		printResult(result);

		return;
	}

	private static String convertInfixToPostfix(String input) throws Exception {
		// TASK2 ) Notate unary '-' differently
		String infix = notateUnaryNegation(input);
		// TASK3 ) Initialize postfix, operation stack
		String postfix = "";
		Stack<Character> operatorStack = new Stack<>();
		// TASK4 )
		for (int i = 0; i < infix.length(); i++) {
			char element = infix.charAt(i);
			if (Character.isDigit(element)) {
				while (i < infix.length() && Character.isDigit(infix.charAt(i))) {
					postfix += infix.charAt(i);
					i++;
				}
				postfix += " ";
				i--;
			} else if (element == '(') {
				operatorStack.push(element);
			} else if (element == ')') {
				while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
					postfix += operatorStack.pop();
					postfix += " ";
				}
				if (operatorStack.isEmpty() || operatorStack.peek() != '(') {
					throw new Exception("Invalid Expression");
				} else {
					operatorStack.pop();
				}
			} else if (isOperator(element)) {
				if (element == '^' || element == '~') {
					while (!operatorStack.isEmpty() && precedence(element) < precedence(operatorStack.peek())) {
						if (operatorStack.peek() == '(') {
							throw new Exception("Invalid Expression");
						} else {
							postfix += operatorStack.pop();
							postfix += " ";
						}
					}
				} else {
					while (!operatorStack.isEmpty() && precedence(element) <= precedence(operatorStack.peek())) {
						if (operatorStack.peek() == '(') {
							throw new Exception("Invalid Expression");
						} else {
							postfix += operatorStack.pop();
							postfix += " ";
						}
					}
				}
				operatorStack.push(element);
			} else if (Character.isWhitespace(element)) {
				continue;
			} else {
				throw new Exception("Invalid Expression");
			}
		}
		while (!operatorStack.isEmpty()) {
			if (isOperator(operatorStack.peek())) {
				postfix += operatorStack.pop();
				postfix += " ";
			} else if (operatorStack.peek() == '(') {
				throw new Exception("Invalid Expression");
			}
		}
		postfix = refinePostfix(postfix);
		return postfix;
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
					char previousElement = input.charAt(i-1);
					for (int j = i-1; j >= 0; j--) {
						if (!Character.isWhitespace(input.charAt(j))) {
							previousElement = input.charAt(j);
							break;
						}
					}
					if (previousElement == '-' || !Character.isDigit(previousElement) && previousElement != ')') {
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
			case '-' :
				return 0;
			case '*' :
			case '/' :
			case '%' :
				return 1;
			case '~' :
				return 2;
			case '^' :
				return 3;
			default :
				return -1;
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

	private static long evaluatePostfix(String postfix) throws Exception {
		// TASK1 ) Initialize operands and operator
		long operand1, operand2;
		char operator;
		// TASK2 ) Initialize operation stack
		Stack<Long> operationStack = new Stack<>();
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
					long temp = operationStack.pop();
					temp = temp * 10 + (element - '0');
					// TASK4.6 ) Push
					operationStack.push(temp);
				} else {
					// TASK4.7 ) Push
					operationStack.push((long)(element - '0'));
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
					long temp = operate(operator, operand1);
					// TASK4.4.3.3 ) Push operation result
					operationStack.push(temp);
				} else {
					// TASK4.4.3.4 ) Pop two operands
					operand2 = operationStack.pop();
					operand1 = operationStack.pop();
					// TASK4.4.3.5 ) Operation
					long temp = operate(operator, operand1, operand2);
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
		if (operationStack.size() > 1) {
			throw new Exception("Invalid Expression");
		}
		long result = operationStack.pop();
		return result;
	}

	private static boolean isOperator(char element) {
		return element == '+' || element == '-' || element == '*' || element == '/' || element == '%' || element == '^' || element == '~';
	}

	private static boolean isUnaryOperator(char element) {
		return element == '~';
	}

	private static long operate(char operator, long operand) {
		return (- operand);
	}
	private static long operate(char operator, long operand1, long operand2) throws Exception {
		switch (operator) {
			case '+':
				return operand1 + operand2;
			case '-' :
				return operand1- operand2;
			case '*' :
				return operand1 * operand2;
			case '/' :
				if (operand2 != 0) return operand1 / operand2;
				else throw new Exception("Impossible Operation");
			case '%' :
				if (operand2 != 0) return operand1 % operand2;
				else throw new Exception("Impossible Operation");
			case '^' :
				if (!(operand1 == 0 && operand2 < 0)) return (long) Math.pow(operand1, operand2);
				else throw new Exception("Impossible Operation");
			default :
				throw new Exception("Impossible Operation");
		}
	}

	private static void printResult(long result) {
		System.out.println(result);
	}

	private static void printErrorMessage() {
		System.out.println("ERROR");
	}

}
