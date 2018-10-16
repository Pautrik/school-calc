package calc;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

/**
 *  Calculator program
 *
 *  Read Eval Print Loop for Calculator (a command line)
 *
 *  To use the calculator run this.
 *
 */
class REPL {

    public static void main(String[] args) {
        new REPL().program();
    }

    final private Scanner scan = new Scanner(in);
    final private Calculator calculator = new Calculator();

    public void program() {
        while (true) {
            out.print("> ");
            String userInput = scan.nextLine();
            try {
                double result = calculator.eval(userInput);
                out.println(result);
            }catch( Exception e){
                e.printStackTrace(System.out);

                out.println(e.getMessage());
            }
        }
    }
}
