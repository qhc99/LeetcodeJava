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
     * #1335
     * 
     * @param jobDifficulty
     * @param d
     * @return
     */
    public int minDifficulty(int[] jobDifficulty, int d) {
        if (d > jobDifficulty.length)
            return -1;

        int[][] dp = new int[2][jobDifficulty.length];
        for (int i = 0,
                max = Integer.MIN_VALUE; i < jobDifficulty.length; i++) {
            max = Math.max(max, jobDifficulty[i]);
            dp[0][i] = max;
        }

        for (int i = 1; i < d; i++) {
            Stack<int[]> stack = new Stack<>();
            for (int j = i; j < jobDifficulty.length; j++) {
                int min = dp[0][j - 1];
                while (!stack.isEmpty()
                        && jobDifficulty[stack.peek()[0]] < jobDifficulty[j]) {
                    min = Math.min(min, stack.pop()[1]);
                }
                if (stack.isEmpty())
                    dp[1][j] = min + jobDifficulty[j];
                else
                    dp[1][j] = Math.min(min + jobDifficulty[j],
                            dp[1][stack.peek()[0]]);
                stack.add(new int[] { j, dp[1][j] });
            }
            var t = dp[0];
            dp[0] = dp[1];
            dp[1] = t;
        }

        return dp[0][jobDifficulty.length - 1];
    }

    /**
     * #1352 ProductOfNumbers
     */
    class ProductOfNumbers {
        List<Node> list = new ArrayList<>();

        static record Node(long val, int dist) {
        }

        public ProductOfNumbers() {

        }

        public void add(int num) {
            var dist = num == 0 ? 0 : Integer.MAX_VALUE / 2;
            var last = list.isEmpty() ? null : list.getLast();
            var val = num;
            if (last != null) {
                dist = Math.min(last.dist + 1, dist);
                if (last.val != 0)
                    val *= last.val;
            }
            list.add(new Node(val, dist));

        }

        public int getProduct(int k) {
            var last = list.getLast();
            if (k > last.dist)
                return 0;
            if (k == last.dist || k == list.size())
                return (int) last.val;
            return (int) (last.val / list.get(list.size() - 1 - k).val);
        }
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

    /**
     * #1383
     * 
     * @param n
     * @param speed
     * @param efficiency
     * @param k
     * @return
     */
    public int maxPerformance(int n, int[] speed, int[] efficiency, int k) {
        long max = Long.MIN_VALUE;
        int[][] sortedEff = new int[n][2];
        for (int i = 0; i < n; i++) {
            sortedEff[i][0] = efficiency[i];
            sortedEff[i][1] = i;
        }
        int mod = 1_000_000_007;
        Arrays.sort(sortedEff, (a, b) -> Integer.compare(b[0], a[0]));
        Queue<Long> maxSpeed = new PriorityQueue<>();
        long speedSum = 0;
        for (var staff : sortedEff) {
            if (maxSpeed.size() >= k) {
                speedSum -= maxSpeed.poll();
            }
            long eff = staff[0];
            long s = speed[staff[1]];
            maxSpeed.add(s);
            speedSum += s;

            max = Math.max((eff * speedSum), max);

        }
        return (int) (max % mod);
    }

    /**
     * #1392
     * 
     * @param s
     * @return
     */
    public String longestPrefix(String s) {
        int[] fail = new int[s.length()];
        int ptr = 0;
        for (int j = 1; j < s.length(); j++) {
            while (ptr > 0 && s.charAt(ptr) != s.charAt(j)) {
                ptr = fail[ptr - 1];
            }
            if (s.charAt(ptr) == s.charAt(j))
                ptr++;
            fail[j] = ptr;
        }

        return s.substring(0, fail[s.length() - 1]);
    }
}
