package com.mycompany.newtasks;

import static java.lang.Double.NaN;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CalculatorImpl implements Calculator {

    /*A pattern to check that an expression doesn't contains anything except:
         -numbers,
         -mathematical characters (+,-,*,/),
         -round open and close brackets,
         -division by zero.
     */
    private static final Pattern PATTERN = Pattern.compile("^(?:\\d+\\.?\\d*+([*+-/]))+\\d+\\.?\\d*+$");

    // A list to subespressions of the income expression
    private static List<String> listOfSubExpressions = new ArrayList<>();

    private static boolean isSimple; //false - if the expression contains round open and close brackets

    //a list to store indices of "(" position in the expression
    private static List<Integer> indexesOfOpenRoundBrackets = new ArrayList<>();
    //a list to store indices of ")" position in the expression
    private static List<Integer> indexesOfCloseRoundBrackets = new ArrayList<>();

    @Override
    public String calculate(String expression) {
        isSimple = true;
        String result = null;
        if (!isValid(expression)) {
            return result;
        }
        result = expression;

        if (isSimple == false) {
            do {
                /*after this step we have expression without round brackets,
                 i.e. all subexpressions in braskets have been already calculated.*/
                try {
                    result = fragmentation(result);
                } catch (Exception e) {
                    result = null;
                    return result;
                }
                //Condition to avoid calculating expression such as "6+(15-4+)5-2(-1)"
                if (result.equals("false")) {
                    result = null;
                    return result;
                }

            } while (result.contains("("));
        }

        try {
            double doubleResult = mathCount(result);
            //Rounding to the fourth decimal
            double roundDoubleResult = new BigDecimal(doubleResult).setScale(4, RoundingMode.UP).doubleValue();
            result = String.valueOf(roundDoubleResult);

        } catch (Exception e) {
            result = null;
            return result;
        }

        return result;
    }

    //A method for checking an income expression is a valid mathematical expression. 
    private static boolean isValid(String expression) {
        boolean result = false;
        String expressionWithoutBrackets = "";
        while (expression.contains(" ")) {
            expression = expression.replaceAll(" ", "");
        }
        expressionWithoutBrackets = expression;
        //System.out.println("expressionWithoutBrackets = " + expressionWithoutBrackets);

        while (expressionWithoutBrackets.contains(")") && expressionWithoutBrackets.contains("(")) {
            isSimple = false;
            expressionWithoutBrackets = expressionWithoutBrackets.replaceAll("\\)", "");
            expressionWithoutBrackets = expressionWithoutBrackets.replaceAll("\\(", "");
        }
        //System.out.println("expressionWithoutBrackets = " + expressionWithoutBrackets + "\n" + "isSimple = " + isSimple);
        Matcher matcher = PATTERN.matcher(expressionWithoutBrackets);
        //System.out.println("Expression  " + expression + '\n' + "Validation for pattern  " + expressionWithoutBrackets + "  : " + (matcher.matches() ? " passed." : "not passed."));

        indicesPositioning(expression);
        //Stream.of(indexesOfOpenRoundBrackets).forEach(System.out::println);
        //Stream.of(indexesOfCloseRoundBrackets).forEach(System.out::println);
        if (matcher.matches() == true && indexesOfOpenRoundBrackets.size() == indexesOfCloseRoundBrackets.size()) {
            result = true;
        }

        if (indexesOfOpenRoundBrackets.size() != 0) {
            if (indexesOfOpenRoundBrackets.get(0) < indexesOfCloseRoundBrackets.get(0)) {
                result = true;
            } else {
                result = false;
                return result;
            }
        }
        //System.out.println("isValid = " + result);
        //System.out.println("***********************************************");
        return result;
    }

    /* A method which :
        -determine a subexpression in brackets,
        -send it to mathCount() method,
        -receive a result of the subexpression calculation from a mathCount() method,
        -and place this result in the start expression.
     */
    private static String fragmentation(String expression) throws Exception {
        String result = null;
        int indexOfOpenBrasket = 0;
        int indexOfCloseBrasket = 0;
        String subResult = "";
        double d = 0;

        indicesPositioning(expression);
        /*A loop to determine indexOfOpenBrasket and indexOfCloseBrasket 
        taking into account cases with brackets nested in each other.
        For example (6+(15-(4+1)+1)/3).
         */
        for (int i = 0; i < indexesOfOpenRoundBrackets.size(); i++) {
            indexOfOpenBrasket = 0;
            if (indexesOfCloseRoundBrackets.get(0) > indexesOfOpenRoundBrackets.get(i)) {
                indexOfOpenBrasket = indexesOfOpenRoundBrackets.get(i);
            } else {
                indexOfOpenBrasket = indexesOfOpenRoundBrackets.get(0);
            }
            indexOfCloseBrasket = indexesOfCloseRoundBrackets.get(0);
        }
        //Pass a subexpression within brackets in method mathCount() for calculation.
        d = mathCount(expression.substring(indexOfOpenBrasket + 1, indexOfCloseBrasket));
        //SSystem.out.println("expression = " + expression.substring(indexOfOpenBrasket + 1, indexOfCloseBrasket) + "   result = " + d);

        if (Double.valueOf(d).equals(NaN)) {
            result = "false";
            return result;
        } else if (d < 0) {
            /*add signs "|" to simplify further negative value processing,  
               for example (15*|-7|/2+1).*/
            subResult = "|" + String.valueOf(d) + "|";
        } else {
            subResult = String.valueOf(d);
        }
        if (!subResult.equals(null)) {
            result = expression.substring(0, indexOfOpenBrasket) + subResult + expression.substring(indexOfCloseBrasket + 1, expression.length());
        }
        //System.out.println("fragmentation result = " + result);
        return result;
    }

    // A method to fill two lists with indices of OpenRoundBrackets and CloseRoundBrackets respectively.
    private static void indicesPositioning(String expression) {
        indexesOfOpenRoundBrackets.clear();
        indexesOfCloseRoundBrackets.clear();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '(') {
                indexesOfOpenRoundBrackets.add(i);
            }
            if (c == ')') {
                indexesOfCloseRoundBrackets.add(i);
            }
        }
    }

    //A method to calculate a subexpression (part of the start expression without braackets).
    private static double mathCount(String expression) throws Exception {
        String result = null;
        //Additional variables
        String s = "";
        int count = 0;
        double d = 0;
        double d1 = 0;
        double d2 = 0;

        Deque<String> stackOfNumbers = new ArrayDeque<>();
        Deque<String> stackOfSigns = new ArrayDeque<>();
        /* A list to store all elements of the subexpression,
        i.e. each number and each sign store separately.*/
        List<String> list = new ArrayList<>();
        //A loop to fill list with elements.
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                if (s.length() > 0) {
                    list.add(s);
                    s = "";
                }
                list.add(String.valueOf(c));
            } else if (c == '|' && count == 0) {
                s += expression.charAt(i + 1);
                i++;
                count++;
            } else if (c == '|' && count == 1) {
                count = 0;
            } else {
                s += c;
            }
        }
        if (s.length() > 0) {
            list.add(s);
        }

        //Condition to avoid calculating expression such as "6+(15-4+)5-2(-1)"
        if (list.size() % 2 == 0) {
            d = NaN;
            return d;
        }

        /*A loop to calculate a part of the expression with "/" and "*" signs  if there are such signs
          in the expression (in this case signs "+" and "-"  with respective numbers just place
          in the stackOfSigns/stackOfNumbers and not pop out of it).*/
        for (int i = 0; i < list.size(); i++) {
            String current = list.get(i);
            if (current.equals("|")) {
                continue;
            }
            if (!current.equals("*") && !current.equals("/") && !current.equals("-") && !current.equals("+")) {
                stackOfNumbers.addLast(current);
                //Stream.of(stackOfNumbers).forEach(System.out::println);
            }
            if (current.equals("-") || current.equals("+")) {
                stackOfSigns.addLast(current);
                //Stream.of(stackOfSigns).forEach(System.out::println);
            }
            if (current.equals("/") && stackOfNumbers.size() > 1) {
                stackOfNumbers.addLast(list.get(i + 1));
                d2 = Double.parseDouble(stackOfNumbers.pollLast());
                d1 = Double.parseDouble(stackOfNumbers.pollLast());
                d = d1 / d2;
                stackOfNumbers.addLast(String.valueOf(d));
                i++;
            }
            if (current.equals("/") && stackOfNumbers.size() == 1) {
                stackOfNumbers.addLast(list.get(i + 1));
                d = Double.parseDouble(stackOfNumbers.pollFirst()) / Double.parseDouble(stackOfNumbers.pollFirst());
                stackOfNumbers.addLast(String.valueOf(d));
                i++;
            }
            if (current.equals("*")) {
                stackOfNumbers.addLast(list.get(i + 1));
                d = Double.parseDouble(stackOfNumbers.pollLast()) * Double.parseDouble(stackOfNumbers.pollLast());
                stackOfNumbers.addLast(String.valueOf(d));
                i++;
            }
        }

        /*System.out.print("stackOfNumbers = ");
        Stream.of(stackOfNumbers).forEach(System.out::println);
        System.out.print("stackOfSigns = ");
        Stream.of(stackOfSigns).forEach(System.out::println);*/

 /*A loop to calculate the expression with "+" and "-" signs if there are such signs in the expression
        (in this case signs have the same priority and calculated from left to right side of the expression).*/
        while (stackOfSigns.size() != 0) {

            switch (stackOfSigns.pollFirst()) {
                case "+":
                    d = Double.parseDouble(stackOfNumbers.pollFirst()) + Double.parseDouble(stackOfNumbers.pollFirst());
                    stackOfNumbers.addFirst(String.valueOf(d));
                    break;
                case "-":
                    d = Double.parseDouble(stackOfNumbers.pollFirst()) - Double.parseDouble(stackOfNumbers.pollFirst());
                    stackOfNumbers.addFirst(String.valueOf(d));
                    break;
            }
        }
        return Double.parseDouble(stackOfNumbers.pop());
    }
}
