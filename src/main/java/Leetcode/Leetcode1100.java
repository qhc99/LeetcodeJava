package Leetcode;

import java.util.*;

@SuppressWarnings("JavaDoc")
public class Leetcode1100 {

    /**
     * #1087
     * 
     * @param s
     * @return
     */
    public String[] expand(String s) {
        List<StringBuilder> res = new ArrayList<>();
        res.add(new StringBuilder());
        Iter iter = new Iter(s);
        while (!iter.end()) {
            var chars = iter.next();
            int size = res.size() * chars.size();
            int initLength = res.size();
            for (int i = res.size(); i < size; i++) {
                res.add(new StringBuilder(res.get(i % initLength)));
            }
            for (int i = 0; i < res.size(); i++) {
                res.get(i).append(chars.get(i / initLength));
            }
        }

        return res.stream().map(sb -> sb.toString()).sorted().toList()
                .toArray(new String[0]);
    }

    static class Iter {
        String s;
        int i = 0;

        Iter(String s) {
            this.s = s;
        }

        boolean end() {
            return i >= s.length();
        }

        List<Character> next() {
            if (s.charAt(i) != '{') {
                return List.of(s.charAt(i++));
            } else {
                List<Character> res = new ArrayList<>();
                i++;
                do {
                    if (s.charAt(i) != ',')
                        res.add(s.charAt(i));
                } while (s.charAt(++i) != '}');
                i++;
                return res;
            }
        }

    }

    /**
     * #1094
     *
     * @param trips
     * @param capacity
     * @return
     */
    public static boolean carPooling(int[][] trips, int capacity) {
        class DestCap {
            final int dest;
            final int cap;

            DestCap(int d, int c) {
                dest = d;
                cap = c;
            }
        }
        Arrays.sort(trips, Comparator.comparing(i -> i[1]));
        PriorityQueue<DestCap> queue = new PriorityQueue<>(
                Comparator.comparing(d -> d.dest));
        for (var trip : trips) {
            var c = trip[0];
            var start = trip[1];
            var dest = trip[2];
            while (queue.size() > 0 && queue.peek().dest <= start) {
                capacity += queue.poll().cap;
            }
            capacity -= c;
            if (capacity < 0) {
                return false;
            } else {
                queue.add(new DestCap(dest, c));
            }
        }
        return true;
    }
}
