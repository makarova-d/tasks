package com.mycompany.newtasks;

import java.util.*;
import java.util.stream.Stream;

public class SubsequenceImpl implements Subsequence {

    private static List<String> list = new ArrayList<>();

    public List<String> getList() {
        return list;
    }

    @Override
    public boolean find(List<String> x, List<String> y) {
        boolean result = false;
        try {
            if (listProcess(x, y) == true) {
                result = true;
            }
        } catch (Exception e) {
        } finally {
            return result;
        }
    }

    public static void main(String[] args) {

        SubsequenceImpl s = new SubsequenceImpl();
        boolean b = s.find(Arrays.asList("A", "B", "C", "D"),
                Arrays.asList("BD", "ABC", "A", "B", "M", "D", "M", "C", "DC"));
        System.out.println(b);
    }

    private static boolean listProcess(List<String> x, List<String> y) {
        boolean result = false;
        try {
            //A map to store contents of the list "y".
            Map<String, String> map = new LinkedHashMap<>();

            for (String s : y) {
                map.put(String.valueOf(new Random()), s);
            }
            
            //Stream.of(map).forEach(System.out::println);

            CIRCL1:
            for (int i = 0; i < x.size(); i++) {
                CIRCL2:
                for (Iterator<Map.Entry<String, String>> it = map.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<String, String> entry = it.next();
                    if (entry.getValue().equals(x.get(i))) {
                        list.add(entry.getValue());
                        it.remove();
                        continue CIRCL1;
                    } else {
                        it.remove();
                        continue CIRCL2;
                    }
                }
            }

            if (list.equals(x)) {
                result = true;
            }
        } catch (Exception e) {

        }
        return result;
    }

}
