package Leetcode;

import java.util.*;

public class Leetcode3700 {

    /**
     * #3629
     * @param nums
     * @return
     */
    static class Q3629 {
        static final int MAX = 1_000_000;
        static final List<Integer>[] factors = new ArrayList[MAX + 1];
        static {
            Arrays.setAll(factors, i -> new ArrayList<>());
            for (int i = 2; i < MAX + 1; i++)
                if (factors[i].isEmpty())
                    for (int j = i; j < MAX + 1; j += i)
                        factors[j].add(i);
        }

        public int minJumps(int[] nums) {
            if (nums.length == 1)
                return 0;
            Queue<Integer> queue = new ArrayDeque<>();
            Queue<Integer> next = new ArrayDeque<>();
            Map<Integer, List<Integer>> val2pos = new HashMap<>();

            for (int i = 0; i < nums.length; i++) {
                if (factors[nums[i]].size() == 1)
                    val2pos.computeIfAbsent(nums[i], k -> new ArrayList<>())
                            .add(i);
            }
            boolean[] visited = new boolean[nums.length];
            visited[nums.length - 1] = true;
            queue.add(nums.length - 1);
            int ans = 0;
            while (!queue.isEmpty()) {
                ans++;
                while (!queue.isEmpty()) {
                    var pos = queue.poll();
                    if (pos + 1 < nums.length && !visited[pos + 1]) {
                        visited[pos + 1] = true;
                        next.add(pos + 1);
                    }
                    if (pos - 1 >= 0 && !visited[pos - 1]) {
                        visited[pos - 1] = true;
                        if (pos - 1 == 0)
                            return ans;
                        next.add(pos - 1);
                    }

                    for (int factor : factors[nums[pos]]) {
                        var factorPos = val2pos.getOrDefault(factor, List.of());
                        for (var prev : factorPos) {
                            if (!visited[prev]) {
                                visited[prev] = true;
                                if (prev == 0)
                                    return ans;
                                next.add(prev);
                            }
                        }
                    }

                }

                var t = queue;
                queue = next;
                next = t;
            }
            return -1;
        }
    }

    /**
     * #3631
     * 
     * @param n
     * @param edges
     * @param k
     * @return
     */
    public int minCost(int n, int[][] edges, int k) {
        if (k == n)
            return 0;
        Queue<int[]> queue = new PriorityQueue<>(
                (a, b) -> Integer.compare(a[2], b[2]));
        for (var e : edges)
            queue.add(e);
        int group = n;
        int max = 0;

        Disjointset set = new Disjointset(n);
        while (group > k && !queue.isEmpty()) {
            var e = queue.poll();
            int n1 = e[0], n2 = e[1];
            if (set.parent(n1) != set.parent(n2)) {
                group--;
                set.union(n1, n2);
            }
            max = Math.max(max, e[2]);
        }

        return max;
    }

    /**
     * #3652
     * @param prices
     * @param strategy
     * @param k
     * @return
     */
    public long maxProfit(int[] prices, int[] strategy, int k) {
        long s1 = 0, s2 = 0, s3 = 0;
        for (int i = k / 2; i < k; i++)
            s2 += prices[i];
        for (int i = k; i < prices.length; i++)
            s3 += prices[i] * strategy[i];
        long max = s1 + s2 + s3;
        long t = 0;
        for (int i = 0; i < k; i++)
            t += prices[i] * strategy[i];
        max = Math.max(max, t + s3);
        for (int i = k; i < prices.length; i++) {
            s1 += prices[i - k] * strategy[i - k];
            s3 -= prices[i] * strategy[i];
            s2 += prices[i] - prices[i - k / 2];
            max = Math.max(max, s1 + s2 + s3);
        }
        return max;
    }
}
