package Leetcode;

import java.util.*;

public class Leetcode3600 {

    /**
     * #3535
     * 
     * @param conversions
     * @param queries
     * @return
     */
    public int[] queryConversions(int[][] conversions, int[][] queries) {
        long mod = 1_000_000_007;
        Map<Integer, Map<Integer, Long>> graph = new HashMap<>();
        for (var convert : conversions) {
            graph.computeIfAbsent(convert[0], k -> new HashMap<>())
                    .put(convert[1], convert[2] % mod);
            graph.computeIfAbsent(convert[1], k -> new HashMap<>())
                    .put(convert[0], modInv(convert[2]));
        }
        graph.get(0).put(0, 1l);
        boolean[] visited = new boolean[conversions.length + 1];
        visited[0] = true;
        Queue<Integer> queue = new ArrayDeque<>();
        for (var nb : graph.get(0).keySet()) {
            visited[nb] = true;
        }
        queue.addAll(graph.get(0).keySet());
        while (!queue.isEmpty()) {
            var node = queue.poll();
            for (var nb : graph.getOrDefault(node, Map.of()).keySet()) {
                if (!visited[nb]) {
                    visited[nb] = true;
                    var f1 = graph.get(0).get(node);
                    var f2 = graph.get(node).get(nb);
                    var f = f1 * f2 % mod;
                    graph.get(0).put(nb, f);
                    queue.add(nb);
                }
            }
        }
        int[] res = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            var query = queries[i];
            var f1 = graph.get(0).get(query[0]);
            var f2 = graph.get(0).get(query[1]);
            res[i] = (int)(modInv(f1) * f2 % mod);
        }
        return res;
    }

    long modInv(long a) {
        long mod = 1_000_000_007;
        long p = mod - 2;
        long res = 1;

        while (p > 0) {
            if ((p & 1) == 1) {
                res = (res * a) % mod;
            }
            p >>= 1;
            a = (a * a) % mod;
        }
        return res;
    }

    /**
     * #3522
     * 
     * @param instructions
     * @param values
     * @return
     */
    public long calculateScore(String[] instructions, int[] values) {
        long res = 0;
        int i = 0;
        while (i >= 0 && i < instructions.length) {
            if (instructions[i] == null)
                break;
            var s = instructions[i];
            instructions[i] = null;
            if (s.equals("add"))
                res += values[i++];
            else
                i += values[i];
        }
        return res;
    }
}
