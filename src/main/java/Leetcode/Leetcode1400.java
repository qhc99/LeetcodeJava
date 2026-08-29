package Leetcode;

import java.util.*;

public class Leetcode1400 {

    /**
     * #1302
     * 
     * @param root
     * @return
     */
    public int deepestLeavesSum(TreeNode root) {
        int res = 0;
        Queue<TreeNode> current = new ArrayDeque<>();
        Queue<TreeNode> next = new ArrayDeque<>();
        current.add(root);
        while (!current.isEmpty()) {
            int s = 0;
            while (!current.isEmpty()) {
                var n = current.poll();
                s += n.val;
                if (n.left != null)
                    next.add(n.left);
                if (n.right != null)
                    next.add(n.right);
            }
            res = s;
            var t = current;
            current = next;
            next = t;
        }
        return res;
    }

    /**
     * #1326
     * @param n
     * @param ranges
     * @return
     */
    public int minTaps(int n, int[] ranges) {
        int[] rightMost = new int[n + 1];
        for (int i = 0; i < ranges.length; i++) {
            int s = Math.max(0, i - ranges[i]), e = Math.min(n, i + ranges[i]);
            rightMost[s] = Math.max(rightMost[s], e);
        }

        int last = 0, cnt = 0, prev = 0;
        for (int i = 0; i < n; i++) {
            last = Math.max(last, rightMost[i]);
            if (i >= last)
                return -1;
            if (i >= prev) {
                cnt++;
                prev = last;
            }
        }
        return cnt;
    }

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
     * #1334
     * @param n
     * @param edges
     * @param distanceThreshold
     * @return
     */
    public int findTheCity(int n, int[][] edges, int distanceThreshold) {
        Map<Integer, Map<Integer, Integer>> graph = new HashMap<>();
        for (var edge : edges) {
            graph.computeIfAbsent(edge[0], k -> new HashMap<>(n)).put(edge[1],
                    edge[2]);
            graph.computeIfAbsent(edge[1], k -> new HashMap<>(n)).put(edge[0],
                    edge[2]);
        }
        int min = Integer.MAX_VALUE, res = 0;
        Queue<Dest> queue = new PriorityQueue<>(
                Comparator.comparing(d -> d.dist));
        Map<Integer, Map<Integer, Integer>> dist = new HashMap<>();
        for (int i = 0; i < n; i++) {
            queue.clear();
            dist.computeIfAbsent(i, k -> new HashMap<>(n)).put(i, 0);
            for (var node : dist.get(i).keySet()) {
                int finalI = i;
                queue.addAll(graph.getOrDefault(node, Map.of()).entrySet()
                        .stream()
                        .map(e -> new Dest(e.getKey(),
                                e.getValue() + dist.get(finalI).get(node)))
                        .toList());
            }

            while (!queue.isEmpty()) {
                var dest = queue.poll();
                if (dest.dist < dist.get(i).getOrDefault(dest.node,
                        Integer.MAX_VALUE) && dest.dist <= distanceThreshold) {
                    dist.get(i).put(dest.node, dest.dist);
                    dist.computeIfAbsent(dest.node, k -> new HashMap<>(n))
                            .put(i, dest.dist);
                    for (var nb : graph.getOrDefault(dest.node, Map.of())
                            .entrySet()) {
                        if (dest.dist + nb.getValue() < dist.get(i)
                                .getOrDefault(nb.getKey(), Integer.MAX_VALUE)
                                && dest.dist
                                        + nb.getValue() <= distanceThreshold)
                            queue.add(new Dest(nb.getKey(),
                                    dest.dist + nb.getValue()));
                    }
                }
            }
            if (dist.get(i).size() - 1 <= min) {
                min = dist.get(i).size() - 1;
                res = i;
            }
        }
        return res;
    }

    static record Dest(int node, int dist) {
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
     * #1385
     * @param arr1
     * @param arr2
     * @param d
     * @return
     */
    public int findTheDistanceValue(int[] arr1, int[] arr2, int d) {
        Arrays.sort(arr2);
        int res = 0;
        for (var v : arr1) {
            var idx = Arrays.binarySearch(arr2, v);
            if (idx < 0) {
                idx = -(idx + 1);
                if (idx >= arr2.length) {
                    if (Math.abs(arr2[arr2.length - 1] - v) > d)
                        res++;
                } else {
                    if (idx - 1 >= 0) {
                        if (Math.abs(arr2[idx] - v) > d
                                && Math.abs(arr2[idx - 1] - v) > d)
                            res++;
                    } else if (Math.abs(arr2[idx] - v) > d) {
                        res++;
                    }
                }
            }
        }
        return res;
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
