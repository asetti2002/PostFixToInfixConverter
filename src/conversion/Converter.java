package conversion;

import java.util.Stack;

/**
 * 
 * @author akashsetti
 *
 */
/**
 * 
 * Takes in the infix expression and calculates the postfix line. If the
 * expression is faulty, it outputs the correct error message
 *
 */
public class Converter {

	/**
	 * The the postfix expression, or the error message we will display in the txt
	 * file
	 */
	private String postLine;

	/**
	 * The infix expression that we are taking from the txt file
	 */
	private String infixLine;

	/**
	 * Constructor for the converter class, this takes in the line we want to
	 * analyze
	 * 
	 * @param infixLine
	 */
	public Converter(String infixLine) {
		this.infixLine = infixLine;
		postLine = "";
	}

	/**
	 * 
	 * @return the postfix expression, or the error message if the infix expression
	 *         was faulty
	 */
	public String getPostLine() {
		return postLine;
	}

	/**
	 * Method that converts the infix expression to postfix
	 */
	public void conversion() {
		Stack<String> stk = new Stack<String>();
		String[] infxArr = infixLine.split(" ");
//		System.out.println(Arrays.toString(infxArr));

		int previousNumber = -1;
		int curr;
		boolean isError = false;

		for (int i = 0; i < infxArr.length; i++) {
			String s = infxArr[i];
			curr = checkPrior(s);
			if ((i == 0 && isOperator(s)) || (i == infxArr.length - 1 && isOperator(s))) {
				postLine = "Error: too many operators (" + infxArr[i] + ")";
				isError = true;
				break;
			}
			if ((previousNumber == 1 && curr == 1) || (previousNumber == 2 && curr == 1)) {
				postLine = "Error: too many operators (" + infxArr[i] + ")";
				isError = true;
				break;
			}
			if (previousNumber == 2 && curr == 3) {
				postLine = "Error: no subexpression detected ()";
				isError = true;
				break;
			}
			if ((previousNumber == 0 && curr == 0 || previousNumber == 3 && curr == 0)) {
				postLine = "Error: too many operands (" + infxArr[i] + ")";
				isError = true;
				break;
			}
			if ((previousNumber == 0 && curr == 2)) {
				postLine = "Error: too many operands (" + infxArr[i - 1] + ")";
				isError = true;
				break;
			}
			if (previousNumber == 1 && curr == 3) {
				postLine = "Error: too many operators (" + infxArr[i - 1] + ")";
				isError = true;
				break;
			}
			
			if (previousNumber  == 3 && curr == 2) {
				for (int j = i + 1; j < infxArr.length; j++) {
					s = infxArr[j];
					if (checkPrior(infxArr[j]) == 0) {
						postLine = "Error: too many operands (" + infxArr[j] + ")";
						isError = true;
						break;
					}
					if (checkPrior(infxArr[j]) == 1) {
						postLine = "Error too many operators (" + infxArr[j] + ")";
						isError = true;
						break;
					}
				}
				if (isError) {
					break;
				}
			}
			if (!isNotOperand(s)) {
				postLine += s + " ";
			} else if (s.equals("(")) {
				stk.push(s);
			} else if (s.equals(")")) {
				if (stk.isEmpty()) {
					postLine = "Error: no opening parenthesis detected";
					isError = true;
					break;
				}
				// if it is not empty
				while (!stk.peek().equals("(")) {
					if (stk.size() == 1 && !stk.peek().equals("(")) {
						postLine = "Error: no opening parenthesis detected";
						isError = true;
						break;
					}
					postLine += stk.pop() + " ";
				}
				if (!isError) {
					stk.pop();
				} else {
					break;
				}
			} else {
				if (stk.isEmpty()) {
					stk.push(s);
				} else {
					// && !(infxArr[i].equals("^")) && !(stk.peek().equals("^"))
					while (!stk.isEmpty() && presidenceCalc(s) <= presidenceCalc(stk.peek())) {
						if ((infxArr[i].equals("^")) && (stk.peek().equals("^"))) {
							break;
						}
						postLine += stk.pop() + " ";
					}
					stk.push(s);
				}
			}

			previousNumber = curr;

		}
		while (!stk.isEmpty() && !isError) {
			// check this
			if (stk.peek().equals("(")) {
				postLine = "Error: no closing parenthesis detected";
				break;
			}

			postLine += stk.pop() + " ";
		}
		postLine = postLine.trim(); // gets rid of the spaces at the end of the line

	}

	/**
	 * Allows each of the charcacters to be assigned to an integer, so we can work
	 * with them in the calculation.
	 * 
	 * @param o
	 * @return integer of the corresponding character value. (Assigned by me)
	 */
	private int checkPrior(String o) {
		if (o.equals("+") || o.equals("-") || o.equals("*") || o.equals("/") || o.equals("%") || o.equals("^")
				|| o.equals("–")) {
			return 1;
		} else if (o.equals("(")) {
			return 2;
		} else if (o.equals(")")) {
			return 3;
		}
		return 0;
	}

	/**
	 * Checks if the string is an operator
	 * 
	 * @param o
	 * @return true if it is an operator
	 */
	private boolean isOperator(String o) {
		if (o.equals("+") || o.equals("-") || o.equals("*") || o.equals("/") || o.equals("%") || o.equals("^")
				|| o.equals("–")) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the given string is not an operand
	 * 
	 * @param o
	 * @return true if the string is not an operand
	 */
	private boolean isNotOperand(String o) {
		if (o.equals("+") || o.equals("-") || o.equals("*") || o.equals("/") || o.equals("%") || o.equals("(")
				|| o.equals(")") || o.equals("^") || o.equals("–")) {
			return true;
		}
		return false;
	}

	/**
	 * Assigns presidence to the specific string we are looking at.
	 * 
	 * @param op
	 * @return integer value of the precidence of the string we are looking at.
	 */
	private int presidenceCalc(String op) {

		if (op.equals("+") || op.equals("-") || op.equals("–")) {
			return 1;
		} else if (op.equals("*") || op.equals("/") || op.equals("%")) {
			return 2;
		} else if (op.equals("^")) {
			return 3;
		}

		// should never occur
		return -1;
	}

}
