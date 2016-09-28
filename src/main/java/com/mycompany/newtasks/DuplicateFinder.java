package com.mycompany.newtasks;

import java.io.File;

/**
 * Interface for Duplicate task.
 */
public interface DuplicateFinder {

    boolean process(File sourceFile, File targetFile);
}
