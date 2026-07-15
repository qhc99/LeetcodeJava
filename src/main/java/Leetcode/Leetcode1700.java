package Leetcode;

import java.util.*;
import java.util.Iterator;

class Leetcode1650 {
    /**
     * #1604
     * 
     * @param keyName
     * @param keyTime
     * @return
     */
    public List<String> alertNames(String[] keyName, String[] keyTime) {

        Set<String> alerted = new HashSet<>();
        List<String> res = new ArrayList<>();
        List<Entry> entries = new ArrayList<>(keyName.length);
        for (int i = 0; i < keyName.length; i++) {
            entries.add(new Entry(keyName[i], keyTime[i].split(":")));
        }
        entries.sort((a, b) -> {
            var c1 = a.name.compareTo(b.name);
            if (c1 != 0)
                return c1;
            return Arrays.compare(a.time, b.time);
        });
        int s = 0;
        for (int e = 0; e < entries.size(); e++) {
            var entry = entries.get(e);
            if (alerted.contains(entry.name))
                continue;
            if (!entries.get(s).name.equals(entry.name)) {
                s = e;
                continue;
            }
            while (!inOneHour(entries.get(s).time, entry.time)) {
                s++;
            }
            if (e - s >= 2) {
                alerted.add(entry.name);
                res.add(entry.name);
            }

        }
        return res;
    }

    boolean inOneHour(String[] s, String[] e) {
        var d1 = Integer.valueOf(e[0]) - Integer.valueOf(s[0]);
        var d2 = Integer.valueOf(e[1]) - Integer.valueOf(s[1]);
        return d1 * 60 + d2 <= 60;
    }

    static record Entry(String name, String[] time) {
    }
}

@SuppressWarnings({"Unused", "JavadocDeclaration"})
public class Leetcode1700 {
    /**
     * #1662
     *
     * @param word1
     * @param word2
     * @return
     */
    public static boolean arrayStringsAreEqual(String[] word1, String[] word2) {
        var iter1 = new IterStrArr(word1);
        var iter2 = new IterStrArr(word2);
        while (iter1.hasNext()) {
            if (iter2.hasNext()) {
                var i1 = iter1.next();
                var i2 = iter2.next();
                if (!i1.equals(i2)) return false;
            }
            else return false;
        }
        return !iter2.hasNext();
    }

    private static class IterStrArr implements Iterator<Character> {
        final String[] words;
        int arr_idx = 0;
        int word_idx = -1;

        IterStrArr(String[] arr) {
            words = arr;
        }

        @Override
        public boolean hasNext() {
            if (word_idx + 1 < words[arr_idx].length()) {
                return true;
            }
            else {
                return arr_idx + 1 < words.length;
            }
        }

        @Override
        public Character next() {
            word_idx++;
            if (word_idx >= words[arr_idx].length()) {
                word_idx = 0;
                arr_idx++;
            }
            return words[arr_idx].charAt(word_idx);
        }
    }
}
