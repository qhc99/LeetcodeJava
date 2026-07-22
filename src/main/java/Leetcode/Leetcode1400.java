package Leetcode;

import java.util.*;

public class Leetcode1400 {
    /**
     * #1328
     * 
     * @param palindrome
     * @return
     */
    public String breakPalindrome(String palindrome) {
        if (palindrome.length() == 1)
            return "";
        int i = 0;
        int j = palindrome.length() - 1;
        for (; i < j; i++, j--) {
            if (palindrome.charAt(i) != 'a') {
                break;
            }
        }
        var res = new StringBuilder(palindrome);
        if (i >= j) {
            res.replace(res.length() - 1, res.length(), "b");
        } else {
            res.replace(i, i + 1, "a");
        }
        return res.toString();
    }

    /**
     * #1353
     * 
     * @param events
     * @return
     */
    public int maxEvents(int[][] events) {
        Queue<Integer> comeFirst = new PriorityQueue<>((a, b) -> {
            var c = Integer.compare(events[a][0], events[b][0]);
            return c != 0 ? c : Integer.compare(events[a][1], events[b][1]);
        });
        Queue<Integer> endFirst = new PriorityQueue<>((a, b) -> {
            var c = Integer.compare(events[a][1], events[b][1]);
            return c != 0 ? c : Integer.compare(events[a][0], events[b][0]);
        });
        for (int i = 0; i < events.length; i++) {
            comeFirst.add(i);
            endFirst.add(i);
        }
        Set<Integer> joined = new HashSet<>();
        int day = 0;
        int join = 0;
        while (true) {
            while (!comeFirst.isEmpty() && (joined.contains(comeFirst.peek())
                    || events[comeFirst.peek()][1] < day)) {
                comeFirst.poll();
            }
            while (!endFirst.isEmpty() && (joined.contains(endFirst.peek())
                    || events[endFirst.peek()][1] < day)) {
                endFirst.poll();
            }
            if (comeFirst.isEmpty() || endFirst.isEmpty()) {
                break;
            }
            var come = comeFirst.peek();
            var end = endFirst.peek();
            if (day < events[end][0] || events[come][1] <= events[end][1]) {
                day = Math.max(events[come][0] + 1, day + 1);
                joined.add(comeFirst.poll());
            } else {
                day++;
                joined.add(endFirst.poll());
            }
            join++;
        }
        return join;
    }
}
