package Leetcode;

import java.util.*;

public class Leetcode2700 {

    /**
     * #2603
     * 
     * @param coins
     * @param edges
     * @return
     */
    public int collectTheCoins(int[] coins, int[][] edges) {
        int n = coins.length, left = n;
        HashSet<Integer>[] graph = new HashSet[n];
        for (int i = 0; i < n; i++)
            graph[i] = new HashSet<>();
        for (var edge : edges) {
            graph[edge[0]].add(edge[1]);
            graph[edge[1]].add(edge[0]);
        }
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (graph[i].size() == 1 && coins[i] == 0)
                queue.add(i);
        }
        while (!queue.isEmpty()) {
            var node = queue.poll();
            for (var nb : graph[node]) {
                graph[nb].remove(node);
                if (graph[nb].size() == 1 && coins[nb] == 0)
                    queue.add(nb);
            }
            graph[node].clear();
            left--;
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < n; j++) {
                if (graph[j].size() == 1)
                    queue.add(j);
            }
            while (!queue.isEmpty()) {
                var node = queue.poll();
                for (var nb : graph[node])
                    graph[nb].remove(node);
                graph[node].clear();
                left--;
            }
        }

        return 2 * (Math.max(left, 1) - 1);
    }

    /**
     * #2672
     * 
     * @param n
     * @param queries
     * @return
     */
    public int[] colorTheArray(int n, int[][] queries) {
        int[] res = new int[queries.length];
        int[] color = new int[n];
        int count = 0;
        for (int i = 0; i < queries.length; i++) {
            var query = queries[i];
            int idx = query[0], clr = query[1];
            int cntBefore = (idx - 1 >= 0 && color[idx] == color[idx - 1]
                    && color[idx] != 0) ? 1 : 0;
            cntBefore += (idx + 1 < color.length && color[idx] == color[idx + 1]
                    && color[idx] != 0) ? 1 : 0;
            color[idx] = clr;
            int cntAfter = (idx - 1 >= 0 && color[idx] == color[idx - 1]
                    && color[idx] != 0) ? 1 : 0;
            cntAfter += (idx + 1 < color.length && color[idx] == color[idx + 1]
                    && color[idx] != 0) ? 1 : 0;
            count = count - cntBefore + cntAfter;
            res[i] = count;
        }
        return res;
    }

    /**
     * #2673
     * 
     * @param n
     * @param cost
     * @return
     */
    public int minIncrements(int n, int[] cost) {
        return visitInc(1, cost);
    }

    int visitInc(int i, int[] cost) {
        if (i * 2 >= cost.length)
            return 0;
        int res = 0;
        res += visitInc(i * 2, cost);
        res += visitInc(i * 2 + 1, cost);
        var left = cost[i * 2 - 1];
        var right = cost[i * 2];
        var max = Math.max(left, right);
        res += max - right + max - left;
        cost[i - 1] += max;
        return res;
    }
}
