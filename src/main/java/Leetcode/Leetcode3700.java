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

        DisjointSet set = new DisjointSet(n);
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

    static class DisjointSet {
        public int[] parent;
        int[] rank;

        DisjointSet(int len) {
            parent = new int[len];
            rank = new int[len];
            for (int i = 0; i < len; i++) {
                parent[i] = i;
            }
        }

        int parent(int i) {
            if (parent[i] != i) {
                parent[i] = parent(parent[i]);
            }
            return parent[i];
        }

        void union(int a, int b) {
            var pa = parent(a);
            var pb = parent(b);
            if (pa == pb)
                return;
            if (rank[pa] < rank[pb]) {
                parent[pa] = pb;
            } else {
                parent[pb] = pa;
                if (rank[pa] == rank[pb])
                    rank[pa]++;
            }
        }
    }
}
