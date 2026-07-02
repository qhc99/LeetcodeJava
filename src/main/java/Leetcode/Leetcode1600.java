package Leetcode;

import java.util.PriorityQueue;
import java.util.Queue;

public class Leetcode1600 {
    /**
     * #1584
     * 
     * @param points
     * @return
     */
    public int minCostConnectPoints(int[][] points) {
        Queue<Edge> queue = new PriorityQueue<>((e1, e2) -> e1.len - e2.len);
        for (int i = 0; i < points.length; i++) {
            var pa = points[i];
            for (int j = i + 1; j < points.length; j++) {
                var pb = points[j];
                queue.add(new Edge(i, j,
                        Math.abs(pa[0] - pb[0]) + Math.abs(pa[1] - pb[1])));
            }
        }

        DisjointSet set = new DisjointSet(points.length);
        int res = 0;
        while (!queue.isEmpty()) {
            var e = queue.poll();
            if (!set.isLinked(e.i, e.j)) {
                set.union(e.i, e.j);
                res += e.len;
            }
        }
        return res;
    }

    static record Edge(int i, int j, int len) {
    }

    static class DisjointSet {
        int[] parent;
        int[] rank;

        DisjointSet(int len) {
            parent = new int[len];
            rank = new int[len];
            for (int i = 0; i < len; i++) {
                parent[i] = i;
            }
        }

        int find(int i) {
            if (parent[i] != i) {
                parent[i] = find(parent[i]);
            }
            return parent[i];
        }

        boolean isLinked(int a, int b) {
            return find(a) == find(b);
        }

        void union(int a, int b) {
            var pa = find(a);
            var pb = find(b);
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
