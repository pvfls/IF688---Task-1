import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

import java.util.Map;
import java.util.Stack;
import java.util.Scanner;
import java.util.HashMap;
import java.util.function.DoubleBinaryOperator;

public class RPNStacker {
    public static Stack <Double> stack = new Stack<Double>();

    public static Map <String, DoubleBinaryOperator> exp = new HashMap<>();
    
    public static Scanner input = new Scanner(System.in);

    public static String[] split() { return input.nextLine().split(" \n"); }
    
    // Simple code to rule out if it is a signal
    public static boolean signalDecider(String s) {
        return (
            s.equals("-") || 
            s.equals("*") ||
            s.equals("+") ||
            s.equals("/")
        );
    } 

    // Reading file and handling exceptions
    public static String readFile (String filepath) throws IOException, FileNotFoundException {
        BufferedReader buffer = new BufferedReader(new FileReader(filepath));
        String everything;
        try {
            String line = buffer.readLine();
            StringBuilder builder = new StringBuilder();
            
            while (line != null) {
                builder.append(line + " ");
                line = buffer.readLine();
            }
            everything = builder.toString();
        } finally {
            buffer.close();
        }
        return everything;
    }
    
    // Transforms Strings in lists of terms and calls evaluate to resolve
    public static Double operate (String expression) {
        return evaluate(expression.split(" "));
    }

    // Sets up the base expression
    public static void setUp() {
        exp.put("+", (a, b) -> a + b);
        exp.put("-", (a, b) -> a - b);
        exp.put("/", (a, b) -> a / b);
        exp.put("*", (a, b) -> a * b);
    }

    // Resolves the expression on the list of terms
    public static Double evaluate(String[] expression) {
        for (String s: expression) {
            if (!signalDecider(s)) stack.push(Double.parseDouble(s));
            else {
                Double a = stack.pop();
                Double b = stack.pop();
                Double result = exp.get(s).applyAsDouble(b, a);
                stack.push(result);
            }
        }
        return stack.pop();
    }

    public static void fromFile (String filepath) {
        try {
            String exp = readFile(filepath);
            System.out.println(operate(exp));
        } catch (Exception notFound) {
            System.out.println("Error: " + notFound);    
        }
    }

    public static void main(String[] args) {
        setUp();

        fromFile("Calc1.stk");
        
        input.close();
    }
}
