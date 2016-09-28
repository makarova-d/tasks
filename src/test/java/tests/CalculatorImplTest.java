package tests;

import com.mycompany.newtasks.CalculatorImpl;
import com.mycompany.newtasks.CalculatorImpl2;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CalculatorImplTest {

    private CalculatorImpl calculator1;
    private CalculatorImpl2 calculator2;

    @Before
    public void setUp() throws Exception {
        calculator1 = new CalculatorImpl();
        calculator2 = new CalculatorImpl2();
    }

    @Test
    public void zero_test() throws Exception {
        String result1 = calculator1.calculate("0");
        assertEquals(null, result1);
        String result2 = calculator2.calculate("0");
        assertEquals(null, result2);
        assertEquals(result1, result2);

    }

    @Test
    public void floating_point_test() throws Exception {
        String result1 = calculator1.calculate("3.0+2");
        assertEquals("5.0", result1);
        String result2 = calculator2.calculate("3.0+2");
        assertEquals("5.0", result2);
        assertEquals(result1, result2);

    }

    @Test
    public void division_by_zero_test() throws Exception {
        String result1 = calculator1.calculate("7-4/0");
        assertEquals(null, result1);
        String result2 = calculator2.calculate("7-4/0");
        assertEquals(null, result2);
        assertEquals(result1, result2);

    }

    @Test
    public void expression_test() throws Exception {
        String result1 = calculator1.calculate("3.7+2/0.7-15+4/2*3");
        assertEquals("-2.4429", result1);
        String result2 = calculator2.calculate("3.7+2/0.7-15+4/2*3");
        assertEquals("-2.4429", result2);
        assertEquals(result1, result2);
    }

    @Test
    public void complex_expression_test() throws Exception {
        String result1 = calculator1.calculate("321/(115-76)-(31-(19+(4-1)*2)/2)-1");
        assertEquals("-11.2693", result1);
        String result2 = calculator2.calculate("321/(115-76)-(31-(19+(4-1)*2)/2)-1");
        assertEquals("-11.2693", result2);
        assertEquals(result1, result2);
    }

    @Test
    public void function_test_wrong_expression() throws Exception {
        String result1 = calculator1.calculate(")6-7(");
        assertEquals(null, result1);
        String result2 = calculator2.calculate(")6-7(");
        assertEquals(null, result2);
        assertEquals(result1, result2);
    }

    @Test
    public void function_test() throws Exception {
        String result = calculator2.calculate("sin(1)*sin(1)+cos(1)*cos(1)");
        assertEquals("1.0", result);
    }

}
