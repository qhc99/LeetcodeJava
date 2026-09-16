package Leetcode;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

public class Leetcode3700 {
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
