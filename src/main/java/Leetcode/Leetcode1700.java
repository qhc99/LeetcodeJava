package Leetcode;

import java.util.Iterator;

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
