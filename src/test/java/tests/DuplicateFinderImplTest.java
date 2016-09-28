package tests;

import com.mycompany.newtasks.DuplicateFinderImpl;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DuplicateFinderImplTest {

    private DuplicateFinderImpl duplicateFinder;
    private File sourceFile;
    private File targetFile;

    @Before

    public void setUp() throws Exception {
        duplicateFinder = new DuplicateFinderImpl();
        sourceFile = new File("D:\\Test\\sourceFile.txt");
        targetFile = new File("D:\\Test\\targetFile.txt");
        

    }

    @Test
    public void output_file_creation_test() throws Exception {
        if (targetFile.exists() == false) {
            duplicateFinder.process(sourceFile, targetFile);
        }
        assertTrue(targetFile.exists());
    }

    @Test
    public void writing_already_exist_file_without_rewriting_it_contents_test() throws Exception {
        int lineNumberBefore = 0;
        int lineNumberAfter = 0;
        
        duplicateFinder = new DuplicateFinderImpl();
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(targetFile));
        while (lineNumberReader.readLine() != null) {
            lineNumberBefore++;
        }
       
        duplicateFinder.process(sourceFile, targetFile);

        int setLineNumber = duplicateFinder.getSet().size();

        LineNumberReader lineNumberReader2 = new LineNumberReader(new FileReader(targetFile));

        while (lineNumberReader2.readLine() != null) {
            lineNumberAfter++;

        }
        assertEquals(lineNumberBefore + setLineNumber, lineNumberAfter);
    }
}
