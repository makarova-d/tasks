package com.mycompany.newtasks;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class CalculatorImpl2 implements Calculator {

    @Override
    public String calculate(String expression) {
        String result = null;
        if (expression.contains("+") == false && expression.contains("-") == false && expression.contains("*") == false && expression.contains("/") == false) {
            return result;
        }
        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            defineMathFunctions(scriptEngine);
            double doubleResult = ((Number) scriptEngine.eval(expression)).doubleValue();
            double roundDoubleResult = new BigDecimal(doubleResult).setScale(4, RoundingMode.UP).doubleValue();
            result = Double.toString(roundDoubleResult);
            return result;
        } catch (Exception se) {
            //throw new IllegalArgumentException("Failde to evaluate expression");
            return result;
        }
    }
    public static void defineMathFunctions(ScriptEngine scriptEngine) throws ScriptException {
        for (String function : new String[]{"sin", "cos", "sqrt"}) {
            scriptEngine.eval("function " + function + "(x) { return Java.type('java.lang.Math')." + function + "(x); }");
        }
    }
}
