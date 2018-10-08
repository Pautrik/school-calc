package calc;

import java.util.*;

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

    // Here are the only allowed instance variables!
    // Error messages (more on static later)
    final static String MISSING_OPERAND = "Missing or bad operand";
    final static String DIV_BY_ZERO = "Division with 0";
    final static String MISSING_OPERATOR = "Missing operator or parenthesis";
    final static String OP_NOT_FOUND = "Operator not found";

    // Definition of operators
    final static String OPERATORS = "+-*/^";

    // Method used in REPL
    double eval(String expr) {
        if (expr.length() == 0) {
            return NaN;
        }
        // TODO List<String> tokens = tokenize(expr);
        // TODO List<String> postfix = infix2Postfix(tokens);
        // TODO double result = evalPostfix(postfix);
        return 0; // result;
    }

    // ------  Evaluate RPN expression -------------------

    // TODO Eval methods

    double applyOperator(String op, double d1, double d2) {
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

    // ------- Infix 2 Postfix ------------------------

    // TODO Methods

    List<String> infix2postfix(List<String> input) {

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
                throw new RuntimeException("Faulty Input");
            }
        }

        outputQueue.addAll(operatorStack);

        return outputQueue;
    }

    void popUntilMatchingParenthesis(LinkedList<String> operatorStack, LinkedList<String> outputQueue) {
        String headOperator = operatorStack.getFirst();
        while(headOperator != "(") {
            outputQueue.add(operatorStack.pop());
            headOperator = operatorStack.getFirst();
        }
    }

    void handleOperatorPopping(String token,
                               LinkedList<String> operatorStack,
                               LinkedList<String> outputQueue) {
        while(shouldPop(operatorStack, token)) {
            String p = operatorStack.pop();
            outputQueue.add(p);
        }
    }

    boolean shouldPop(LinkedList<String> operatorStack, String token) {
        if(operatorStack.size() <= 0) return false;

        String headOperator = operatorStack.getFirst();

        if(headOperator == "(") return false;

        int tokenPrecendece = getPrecedence(token);
        int headPrecendece = getPrecedence(headOperator);

        boolean equalPriority = headPrecendece == tokenPrecendece;
        boolean isHeadLeftAssociative = getAssociativity(headOperator) == Assoc.LEFT;

        return headPrecendece > tokenPrecendece || equalPriority && isHeadLeftAssociative;
    }


    int getPrecedence(String op) {
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

    enum Assoc {
        LEFT,
        RIGHT
    }

    Assoc getAssociativity(String op) {
        if ("+-*/".contains(op)) {
            return Assoc.LEFT;
        } else if ("^".contains(op)) {
            return Assoc.RIGHT;
        } else {
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }


    // ---------- Tokenize -----------------------

    // TODO Methods to tokenize




    public static boolean isInteger(String s) {
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
