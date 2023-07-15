package Leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

public class Leetcode850 {
    /**
     * #802
     * 
     * @param graph
     * @return
     */
    public static List<Integer> eventualSafeNodes(int[][] graph) {
        int n = graph.length;
        boolean[] safe = new boolean[n];
        boolean[] visited = new boolean[n];
        List<Integer> ans = new ArrayList<>();
        dfsSafeNodes(0, graph, safe, visited, ans);
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfsSafeNodes(i, graph, safe, visited, ans);
            }
        }
        return ans;
    }

    private static boolean dfsSafeNodes(int node, int[][] graph, boolean[] safe, boolean[] visited, List<Integer> ans) {
        visited[node] = true;
        var neighbors = graph[node];
        boolean is_safe = true;
        for (var n : neighbors) {
            if (!visited[n]) {
                is_safe = is_safe && dfsSafeNodes(n, graph, safe, visited, ans);
            } else {
                is_safe = is_safe && safe[n];
            }
        }
        safe[node] = is_safe;
        if (is_safe) {
            ans.add(node);
        }
        return is_safe;
    }

    /**
     * #803
     * 
     * @param grid
     * @param hits
     * @return
     */
    public static int[] hitBricks(int[][] grid, int[][] hits) {
        int m = grid.length;
        int n = grid[0].length;
        for (var h : hits) {
            int i = h[0], j = h[1];
            grid[i][j]--;
        }

        DisjointSet[][] sets = new DisjointSet[m][n];
        for (var s : sets) {
            for (int j = 0; j < n; j++) {
                s[j] = new DisjointSet();
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                unionNeighbor(i, j, grid, sets);
            }
        }
        DisjointSet top = new DisjointSet();
        top.count = 0;
        for (int j = 0; j < n; j++) {
            if (grid[0][j] == 1) {
                DisjointSet.union(top, sets[0][j]);
            }
        }
        int count = top.getCount();
        int[] ans = new int[hits.length];

        for (int k = hits.length - 1; k >= 0; k--) {
            var h = hits[k];
            int i = h[0], j = h[1];
            grid[i][j]++;
            if (grid[i][j] == 1) {
                if (i == 0) {
                    DisjointSet.union(top, sets[i][j]);
                }
                unionNeighbor(i, j, grid, sets);
                int next_count = top.getCount();
                int diff = next_count - count - 1;
                count = next_count;
                ans[k] = diff >= 0 ? diff : 0;
            }
        }

        return ans;
    }

    private static void unionNeighbor(int i, int j, int[][] grid, DisjointSet[][] sets) {
        if (grid[i][j] != 1) {
            return;
        }
        if (i - 1 >= 0 && grid[i - 1][j] == 1) {
            DisjointSet.union(sets[i][j], sets[i - 1][j]);
        }
        if (j - 1 >= 0 && grid[i][j - 1] == 1) {
            DisjointSet.union(sets[i][j], sets[i][j - 1]);
        }
        if (i + 1 < grid.length && grid[i + 1][j] == 1) {
            DisjointSet.union(sets[i][j], sets[i + 1][j]);
        }
        if (j + 1 < grid[0].length && grid[i][j + 1] == 1) {
            DisjointSet.union(sets[i][j], sets[i][j + 1]);
        }
    }

    public static final class DisjointSet {
        private int rank = 0;
        private int count = 1;
        private DisjointSet parent = this;

        /**
         * find identifier of the set of an element
         *
         * @param x element
         * @return identifier
         */
        private static DisjointSet findGroupId(DisjointSet x) {
            if (x != x.parent) {
                x.parent = findGroupId(x.parent);
                x.parent.count += x.count;
                x.count = 0;
            }

            return x.parent;
        }

        public int getCount() {
            return findGroupId(this).count;
        }

        public static boolean inSameSet(DisjointSet a, DisjointSet b) {
            return findGroupId(a) == findGroupId(b);
        }

        public static void union(DisjointSet a, DisjointSet b) {
            link(findGroupId(a), findGroupId(b));
        }

        private static void link(DisjointSet x, DisjointSet y) {
            if (x == y) {
                return;
            } else if (x.rank > y.rank) {
                y.parent = x;
                x.count += y.count;
                y.count = 0;
            } else {
                x.parent = y;
                y.count += x.count;
                x.count = 0;
                if (x.rank == y.rank) {
                    y.rank = y.rank + 1;
                }
            }
        }
    }

}
