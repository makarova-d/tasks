package tests;

import com.mycompany.newtasks.SubsequenceImpl;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class SubsequenceImplTest {

    private SubsequenceImpl subsequence;

    @Before
    public void setUp() throws Exception {
        subsequence = new SubsequenceImpl();
    }

    @Test
    public void equals_test() throws Exception {

        boolean b = subsequence.find(Arrays.asList("A", "B", "C", "D"),
                Arrays.asList("BD", "ABC", "A", "B", "M", "D", "M", "C", "DC", "D"));
        assertTrue(b);
    }

    @Test
    public void wrong_test() throws Exception {

        boolean b = subsequence.find(Arrays.asList("A", "B", "C", "D"),
                Arrays.asList("BD", "ABC", "A", "B", "M", "D", "M", "C", "DC"));
        assertFalse(b);
    }

}
