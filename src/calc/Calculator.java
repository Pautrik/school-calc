package calc;

import java.util.*;
import java.util.StringTokenizer;

import static java.lang.Double.NaN;
import static java.lang.Math.pow;

/*
 *   A calculator for rather simple arithmetic expressions
 *
 *   This is not the program, it's a class declaration (with methods) in it's
 *   own file (which must be named Calculator.java)
 *
 *   NOTE:
 *   - No negative numbers implemented
 */
class Calculator {

    final private static String MISSING_OPERAND = "Missing or bad operand";
    final private static String DIV_BY_ZERO = "Division by 0";
    final private static String MISSING_OPERATOR = "Missing operator or parenthesis";
    final private static String OP_NOT_FOUND = "Operator not found";

    final private static String OPERATORS = "+-*/^";

    public double eval(String expression) {
        expression = expression.replaceAll("\\s+", "");

        if (expression.length() == 0) {
            return NaN;
        }


        LinkedList<String> tokens = tokenize(expression);
        List<String> postfix = infix2postfix(tokens);

        return evaluatePostfix(postfix);
    }

    private double applyOperator(String op, double d1, double d2) {
        switch (op) {
            case "+":
                return d1 + d2;
            case "-":
                return d2 - d1;
            case "*":
                return d1 * d2;
            case "/":
                if (d1 == 0) {
                    throw new IllegalArgumentException(DIV_BY_ZERO);
                }
                return d2 / d1;
            case "^":
                return pow(d2, d1);
        }

        throw new RuntimeException(OP_NOT_FOUND);
    }

    private List<String> infix2postfix(List<String> input) {
        LinkedList<String> outputQueue = new LinkedList<>();
        LinkedList<String> operatorStack = new LinkedList<>();

        for(String token : input) {
            if("+-*/^".contains(token)) {
                handleOperatorPopping(token, operatorStack, outputQueue);
                operatorStack.push(token);
            }
            else if(isInteger(token)) {
                outputQueue.addLast(token);
            }
            else if("(".contains(token)) {
                operatorStack.push(token);
            }
            else if(")".contains(token)) {
                popUntilMatchingParenthesis(operatorStack, outputQueue);
                operatorStack.pop();
            }
            else {
                throw new RuntimeException(String.format("Faulty Input: %s", token));
            }
        }

        outputQueue.addAll(operatorStack);

        return outputQueue;
    }

    private double evaluatePostfix(List<String> expression) {
        Stack<Double> stack = new Stack<Double>();
        Iterator<String> expressionIterator = expression.iterator();

        while (expressionIterator.hasNext()) {
            String token = expressionIterator.next();

            if (this.isInteger(token)) {
                double operand = Double.parseDouble(token);
                stack.push(operand);
            } else {
                double firstOperand = stack.pop();
                double secondOperand = stack.pop();

                stack.push(this.applyOperator(token, firstOperand, secondOperand));
            }
        }

        return stack.pop();
    }

    private void popUntilMatchingParenthesis(LinkedList<String> operatorStack, LinkedList<String> outputQueue) {
        String headOperator = operatorStack.getFirst();
        while(headOperator != "(") {
            outputQueue.add(operatorStack.pop());
            headOperator = operatorStack.getFirst();
        }
    }

    private void handleOperatorPopping(String token,
                               LinkedList<String> operatorStack,
                               LinkedList<String> outputQueue) {
        while(shouldPop(operatorStack, token)) {
            String p = operatorStack.pop();
            outputQueue.add(p);
        }
    }

    private boolean shouldPop(LinkedList<String> operatorStack, String token) {
        if(operatorStack.size() <= 0) return false;

        String headOperator = operatorStack.getFirst();

        if(headOperator == "(") return false;

        int tokenPrecendece = getPrecedence(token);
        int headPrecendece = getPrecedence(headOperator);

        boolean equalPriority = headPrecendece == tokenPrecendece;
        boolean isHeadLeftAssociative = getAssociativity(headOperator) == Assoc.LEFT;

        return (headPrecendece > tokenPrecendece) || (equalPriority && isHeadLeftAssociative);
    }


    private int getPrecedence(String op) {
        if ("+-".contains(op)) {
            return 2;
        } else if ("*/".contains(op)) {
            return 3;
        } else if ("^".contains(op)) {
            return 4;
        } else {
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }

    private enum Assoc {
        LEFT,
        RIGHT
    }

    private Assoc getAssociativity(String op) {
        if ("+-*/".contains(op)) {
            return Assoc.LEFT;
        } else if ("^".contains(op)) {
            return Assoc.RIGHT;
        } else {
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }

    private LinkedList<String> tokenize(String stringToTokenize) {
        LinkedList<String> tokens = new LinkedList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(
            stringToTokenize,
            OPERATORS + "()",
            true
        );

        while (stringTokenizer.hasMoreElements()) {
            String token = stringTokenizer.nextToken();

            // I don't really understand this.
            // tokens.add(token) should be the only line needed.
            // Maybe input from terminal is broken.
            if ("(".contains(token)) {
                tokens.add("(");
            } else if (")".contains(token)) {
                tokens.add(")");
            } else {
                tokens.add(token);
            }
        }

        return tokens;
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }

        return true;
    }
}
