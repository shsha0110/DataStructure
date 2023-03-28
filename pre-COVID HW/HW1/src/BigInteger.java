import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
  
  
public class BigInteger implements Comparable<BigInteger> {
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "입력이 잘못되었습니다.";
  
    // implement this
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("");

    public char sign;
    public int length;
    public int[] sequence;

    public BigInteger(int[] sequence, char sign) {
        this.length = sequence.length - 1;
        this.sign = sign;
        this.sequence = sequence;
    }

    public BigInteger(String sequence) {
        this.length = sequence.length() - 1;
        this.sign = sequence.charAt(0);
        int[] parsedBigInt = new int[this.length];
        for (int i = 1; i < sequence.length(); i++) {
            parsedBigInt[i-1] = sequence.charAt(i) - '0';
        }
        this.sequence = parsedBigInt;
    }

    /** static method **/
    private static BigInteger negateBigInteger(BigInteger big) {
        if (big.sign == '+') { big.sign = '-'; }
        else if (big.sign == '-') { big.sign = '+'; }
        return big;
    }

    private static int[] unifyLength(BigInteger big1, BigInteger big2) {
        // TASK1 ) Compare length of BigInteger
        int length = big1.length >= big2.length ? big1.length : big2.length;
        // TASK2 ) Unify length of first parameter
        // TASK2.1 ) Initialize(fill zero) absolute unified sequence
        int[] unified_sequence = new int[length];
        for (int i = 0; i < length; i++) {
            unified_sequence[i] = 0;
        }
        // TASK2.2 ) Fill elements of first parameter from back
        for (int i = 0; i < big1.length; i++) {
            unified_sequence[(length-1) - i] = big1.sequence[(big1.length-1) - i];
        }
        return unified_sequence;
    }

    private static int[] reverseSequence(int[] sequence) {
        // TASK1 ) Initialize result sequence
        int[] reversedSeq = new int[sequence.length];
        // TASK2 ) Reverse
        for (int i = 0; i < sequence.length; i++) {
            reversedSeq[i] = sequence[(sequence.length-1) - i];
        }
        return reversedSeq;
    }

    /** 1. add **/
    public BigInteger add(BigInteger big) {
        // CASE1 ) (+)+(+)
        if (this.sign == '+' && big.sign == '+') {
            return addPluses(this, big);
        }
        // CASE2 ) (-)+(-)
        else if (this.sign == '-' && big.sign == '-') {
            return addMinuses(this, big);
        }
        // CASE3 ) (+)+(-)
        else if (this.sign == '+' && big.sign == '-') {
            return this.subtract(negateBigInteger(big));
        }
        // CASE4 ) (-)+(+)
        else {
            return big.subtract(negateBigInteger(this));
        }
    }

    private BigInteger addPluses(BigInteger big1, BigInteger big2) {
        // TASK1 ) Induce length of result
        int length = big1.length >= big2.length ? big1.length+1 : big2.length+1;
        // TASK2 ) Initialize(fill zero) result
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = 0;
        }
        // TASK2 ) Unify length of BigInteger
        int[] sequence1 = unifyLength(big1, big2);
        int[] sequence2 = unifyLength(big2, big1);
        // TASK3 ) Reverse sequence
        int[] reversedSeq1 = reverseSequence(sequence1);
        int[] reversedSeq2 = reverseSequence(sequence2);
        // TASK3 ) Add position by position
        // TASK3.1 ) Initialize temporary position int and carry
        int temp = 0;
        int carry = 0;
        for (int i = 0; i < sequence1.length; i++) {
            int operand1 = reversedSeq1[i];
            int operand2 = reversedSeq2[i];
            // TASK3.2 ) Load carry
            temp = carry;
            // TASK3.3 ) Initialize carry
            carry = 0;
            // TASK3.4 ) Positional add
            temp += (operand1 + operand2);
            // TASK3.5 ) Process carry
            if (temp >= 10) {
                temp -= 10;
                carry = 1;
            }
            // TASK3.6 ) Write temp int in result
            result[(length-1)-i] = temp;
            // TASK3.7 ) Initialize temp
            temp = 0;
        }
        return new BigInteger(result, '+');
    }

    private BigInteger addMinuses(BigInteger big1, BigInteger big2) {
        // TASK1 ) Call addPluses
        BigInteger result = addPluses(big1, big2);
        // TASK2 ) Negate
        result = negateBigInteger(result);
        return result;
    }

    /** 2. subtract **/
    public BigInteger subtract(BigInteger big) {
        // CASE1 ) (+)-(+)
        if (this.sign == '+' && big.sign == '+') {
            return subtract(this, big);
        }
        // CASE2 ) (+)-(-)
        else if (this.sign == '+' && big.sign == '-') {
            return addPluses(this, negateBigInteger(big));
        }
        // CASE3 ) (-)-(+)
        else if (this.sign == '-' && big.sign == '+') {
            return addMinuses(this, negateBigInteger(big));
        }
        // CASE4 ) (-)-(-)
        else {
            return subtract(negateBigInteger(big), negateBigInteger(this));
        }
    }

    public BigInteger subtract(BigInteger big1, BigInteger big2) {
        // TASK1 ) Induce length of result
        int length = big1.length >= big2.length ? big1.length : big2.length;
        // TASK2 ) Initialize(fill zero) result
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = 0;
        }
        // TASK3 ) Sort operand by absolute value
        BigInteger firstOperand = big1.compareTo(big2) > 0 ? big1 : big2;
        BigInteger secondOperand = big1.compareTo(big2) > 0 ? big2 : big1;
        // TASK4 ) Unify length of BigInteger
        int[] sequence1 = unifyLength(firstOperand, secondOperand);
        int[] sequence2 = unifyLength(secondOperand, firstOperand);
        // TASK5 ) Reverse sequence
        int[] reversedSeq1 = reverseSequence(sequence1);
        int[] reversedSeq2 = reverseSequence(sequence2);
        // TASK6 ) Subtract position by position
        // TASK6.1 ) Initialize temporary position int and borrow
        int temp = 0;
        int borrow = 0;
        for (int i = 0; i < sequence1.length; i++) {
            int operand1 = reversedSeq1[i] + borrow;
            int operand2 = reversedSeq2[i];
            // TASK6.2 ) Initialize carry
            borrow = 0;
            // TASK6.3 ) Borrowing
            if (operand1 < operand2) {
                operand1 += 10;
                borrow = -1;
            }
            // TASK6.4 ) Compute
            temp = operand1 - operand2;
            // TASK6.5 ) Write temp int in result
            result[(length-1)-i] = temp;
            // TASK6.6 ) Initialize temp
            temp = 0;
        }
        // TASK7 ) Sign
        if (big1 == firstOperand) {
            return new BigInteger(result, '+');
        } else {
            return new BigInteger(result, '-');
        }
    }

    /** 3. multiply **/
    public BigInteger multiply(BigInteger big) {
        // TASK1 ) Initialize result
        BigInteger result;
        if (sign == big.sign) {
            result = new BigInteger("+0");
        } else {
            result = new BigInteger("-0");
        }
        // TASK2 ) Arrange operand
        BigInteger firstOperand = this;
        BigInteger secondOperand = big;
        int[] sequence1 = this.sequence;
        int[] sequence2 = big.sequence;
        // TASK3 ) Reverse sequence
        int[] reversedSeq1 = reverseSequence(sequence1);
        int[] reversedSeq2 = reverseSequence(sequence2);
        // TASK4 ) Multiply position by position
        for (int i = 0; i < reversedSeq2.length; i++) {
            int operand1 = reversedSeq1[i];
            int operand2 = reversedSeq2[i];
            // TASK4.1 ) Initialize temporary BingInteger
            BigInteger temp = new BigInteger("+0");
            // TASK4.2 ) Multiple addition as much as operand2
            for (int j = 0; j < operand2; j++) {
                temp.add(firstOperand);
            }
            // TASK4.3 ) Shift left as much as position of operand2
            temp.shiftLeft(i);
            // TASK4.4 ) Update result
            result.add(temp);
        }
        return result;
    }

    private void shiftLeft() {
        int[] originalSequence = this.sequence;
        int[] shiftedSequence = new int[originalSequence.length + 1];
        for (int i = 0; i < originalSequence.length; i++) {
            shiftedSequence[i] = originalSequence[i];
        }
        shiftedSequence[originalSequence.length] = 0;
    }

    private void shiftLeft(int times) {
        if (times == 0) {
            return;
        }
        else {
            this.shiftLeft();
            this.shiftLeft(times - 1);
        }
    }

    @Override
    public String toString() {
        String strBigInteger = "";
        for (int num : sequence) {
            if (num == 0 && strBigInteger.length() == 0) {
                continue;
            }
            strBigInteger += Integer.toString(num);
        }
        if (sign == '-') {
            strBigInteger = sign + strBigInteger;
        }
        return strBigInteger;
    }

    @Override
    public int compareTo(BigInteger big) {
        // TASK1 ) Arrange operands
        BigInteger BigInteger1 = this;
        BigInteger BigInteger2 = big;
        // TASK2 ) Process differently signed case
        // CASE1 ) (+) vs (-)
        if (BigInteger1.sign == '+' && BigInteger2.sign == '-') {
            return 1;
        }
        // CASE2 ) (-) vs (+)
        else if (BigInteger1.sign == '-' && BigInteger2.sign == '+') {
            return -1;
        }
        // TASK3 ) Unify length of sequence
        int[] sequence1 = unifyLength(BigInteger1, BigInteger2);
        int[] sequence2 = unifyLength(BigInteger2, BigInteger1);
        // CASE3 ) (+) vs (+)
        if (BigInteger1.sign == '+' && BigInteger2.sign == '+') {
            // TASK5 ) Compare number position by position
            for (int i = 0; i < sequence1.length; i++) {
                // TASK5.1 ) Arrange positional number
                int num1 = sequence1[i];
                int num2 = sequence2[i];
                // TASK5.2 ) Compare
                if (num1 == num2) { continue; }
                return num1 > num2 ? 1 : -1;
            }
        }
        // CASE3 ) (-) vs (-)
        else if (BigInteger1.sign == '-' && BigInteger2.sign == '-') {
            // TASK5 ) Compare number position by position
            for (int i = 0; i < sequence1.length; i++) {
                // TASK5.1 ) Arrange positional number
                int num1 = sequence1[i];
                int num2 = sequence2[i];
                // TASK5.2 ) Compare
                if (num1 == num2) { continue; }
                return num1 > num2 ? -1 : 1;
            }
        }
        return 0;
    }

    static BigInteger evaluate(String input) throws IllegalArgumentException
    {
        // TASK1 ) Remove whitespace
        input = input.replaceAll("\\s", "");
        // TASK3 ) Regularize input command
        String regularInput = regularizeCommand(input);

        System.out.println(regularInput);

        // TASK4 ) Parse regular input
        String[] elements = regularInput.split(",");
        BigInteger firstOperand = new BigInteger(elements[0]);
        String operator = elements[1];
        BigInteger secondOperand = new BigInteger(elements[2]);
        // TASK5 ) Compute
        switch (operator) {
            case "+" :
                return firstOperand.add(secondOperand);
            case "-" :
                return firstOperand.subtract(secondOperand);
            case "*" :
                return firstOperand.multiply(secondOperand);
            default :
                throw new IllegalArgumentException();
        }
    }

    private static String regularizeCommand(String input) {
        // TASK1 ) Initialize result
        String regularInput = "";
        // TASK2 ) Initialize procedure
        boolean firstSign = false, firstOperand = false, operator = false, secondSign = false, secondOperand = false;
        for (int i = 0; i < input.length(); i++) {
            // TASK3.1 ) Extract current element and next element
            char currElement = input.charAt(i);
            // TASK3.2 ) Process first sign
            if (!firstSign) {
                // CASE1 ) Digit
                if (Character.isDigit(currElement)) {
                    regularInput += "+";
                    firstSign = true;
                }
                // CASE2 ) Sign
                else if (currElement == '+' || currElement == '-') {
                    regularInput += currElement;
                    firstSign = true;
                    continue;
                }
                // CASE3 ) Illegal argument
                else {
                    throw new IllegalArgumentException();
                }
            }
            // TASK3.3 ) Process first operand
            if (!firstOperand) {
                char nextElement = input.charAt(i + 1);
                // CASE1 ) Digit
                if (Character.isDigit(currElement)) {
                    regularInput += currElement;
                    // CASE1.1 ) End of first operand
                    if (!Character.isDigit(nextElement)) {
                        // TASK3.3.1 ) Delimit elements by comma
                        regularInput += ",";
                        firstOperand = true;
                        continue;
                    }
                    // CASE1.2 ) Not end of first operand
                    else {
                        continue;
                    }
                }
                // CASE2 ) Illegal argument
                else {
                    throw new IllegalArgumentException();
                }
            }
            // TASK3.4 ) Process operator
            if (!operator) {
                // CASE1 ) Operator
                if (currElement == '+' || currElement == '-' || currElement == '*') {
                    regularInput += currElement;
                    // TASK3.3.1 ) Delimit elements by comma
                    regularInput += ",";
                    operator = true;
                    continue;
                }
                // CASE2 ) Illegal argument
                else {
                    throw new IllegalArgumentException();
                }
            }
            // TASK3.5 ) Process second sign
            if (!secondSign) {
                // CASE1 ) Digit
                if (Character.isDigit(currElement)) {
                    regularInput += "+";
                    secondSign = true;
                }
                // CASE2 ) Sign
                else if (currElement == '+' || currElement == '-') {
                    regularInput += currElement;
                    secondSign = true;
                    continue;
                }
                // CASE3 ) Illegal argument
                else {
                    throw new IllegalArgumentException();
                }
            }
            // TASK3.6 ) Process second operand
            if (!secondOperand) {
                // CASE1 ) Digit
                if (Character.isDigit(currElement)) {
                    regularInput += currElement;
                    continue;
                }
                // CASE2 ) Illegal argument
                else {
                    throw new IllegalArgumentException();
                }
            }
        }
        return regularInput;
    }


    private static BigInteger[] processOperands(String[] raw_operands) {
        // TASK1 ) Initialize refined operands array
        BigInteger[] refined_operands = new BigInteger[2];
        // TASK2 ) Initialize count of elements in refined operands array
        int count = 0;
        for (String operand : raw_operands) {
            // TASK3 ) Pass empty tokens
            if (operand.isBlank() || operand.isEmpty()) { continue; }
            // TASK4 ) Remove plus sign in operand token
            String operand_without_plus_sign = operand.replaceAll("\\+", "");
            // TASK4 ) Remove all whitespace in operand token
            String trimed_operand = operand_without_plus_sign.replaceAll(" ", "");
            // TASK5 ) Create new BigInteger object
            BigInteger refined_operand = new BigInteger(trimed_operand);
            // TASK6 ) Add the object in array
            refined_operands[count++] = refined_operand;
        }
        return refined_operands;
    }

    private static BigInteger[] processMultiplicationOperands(String input) {
        // TASK1 ) Split input command by operator
        String[] raw_operands = input.split("\\*");
        BigInteger[] refined_operands = processOperands(raw_operands);
        return refined_operands;
    }

    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();
  
                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }
  
    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);
  
        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);

            System.out.println(result.toString());
  
            return false;
        }
    }
  
    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
