package com.mycompany.newtasks;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.*;
import java.util.stream.Stream;

public class DuplicateFinderImpl implements DuplicateFinder {

    /*To store processed strings, for example:
        aaaa [2]
        ee[1]
        zfrzfrzfrzfr[4] 
     */
    private static Set<String> setOfProcessedStrings = new TreeSet<>();

    public Set<String> getSet() {
        return setOfProcessedStrings;
    }

    @Override
    public boolean process(File sourceFile, File targetFile) {
        try {
            readFile(sourceFile, targetFile);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void readFile(File sourceFile, File targetFile) throws Exception {
        List<String> list = new ArrayList<>();
        Scanner in = new Scanner(sourceFile);
        while (in.hasNextLine()) {
            list.add(in.nextLine());
            //Stream.of(list).forEach(System.out::println);

        }
        processSourceFile(list, targetFile);
    }

    private static void writeFile(File targetFile, Set<String> setOfProcessedStrings) throws Exception {

        FileWriter writer = new FileWriter(targetFile, true);
        for (String set : setOfProcessedStrings) {
            writer.write(set + "\r\n");
            writer.flush();
        }
    }

    private static void processSourceFile(List<String> list, File targetFile) throws Exception {
        Map<String, Integer> map = new HashMap<>();
        for (String s : list) {
            map.put(s, map.containsKey(s) ? map.get(s) + 1 : 1);
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            setOfProcessedStrings.add(entry.getKey() + " [" + entry.getValue() + "]");
        }

        writeFile(targetFile, setOfProcessedStrings);
    }

}
