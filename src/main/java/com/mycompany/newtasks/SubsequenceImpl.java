package com.mycompany.newtasks;

import java.util.*;
import java.util.stream.Stream;

public class SubsequenceImpl implements Subsequence {

    private static List<String> list = new ArrayList<>();
    private static boolean result;

    public List<String> getList() {
        return list;
    }

    @Override
    public boolean find(List<String> x, List<String> y) {
        
        try {

            result = listProcess(x, y) == true ? result = true : false;
        } catch (Exception e) {
        } finally {
            return result;
        }
    }

    private static boolean listProcess(List<String> x, List<String> y) {
        int index = 0;
        while (index < x.size()) {
            for (String s : y) {
                if (s.equals(x.get(index))) {
                    list.add(s);
                    index++;
                }
            }
        }
        //Stream.of(list).forEach(System.out::println);
        if (list.equals(x)) {
            result = true;
        }
        return result;
    }

    /*public static void main(String[] args) {

        SubsequenceImpl s = new SubsequenceImpl();
        boolean b = s.find(Arrays.asList("A", "B", "C", "D"),
                Arrays.asList("BD", "ABC", "A", "B", "M", "D", "M", "C", "DC"));
        System.out.println(b);
    }*/

}
