package Leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.function.ToDoubleFunction;

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

    private static boolean dfsSafeNodes(int node, int[][] graph, boolean[] safe,
            boolean[] visited, List<Integer> ans) {
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

    private static void unionNeighbor(int i, int j, int[][] grid,
            DisjointSet[][] sets) {
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

    /**
     * #808
     * 
     * @param n
     * @return
     */
    public static double soupServings(int n) {
        if (n >= 179 * 25) {
            return 1;
        }
        n = n / 25 + ((n % 25 != 0) ? 1 : 0);
        double[][] dp = new double[n + 1][n + 1];
        dp[0][0] = 0.5;
        for (int j = 1; j <= n; j++) {
            dp[0][j] = 1;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                double p1 = dp[i - 4 >= 0 ? i - 4 : 0][j];
                double p2 = dp[i - 3 >= 0 ? i - 3 : 0][j - 1 >= 0 ? j - 1 : 0];
                double p3 = dp[i - 2 >= 0 ? i - 2 : 0][j - 2 >= 0 ? j - 2 : 0];
                double p4 = dp[i - 1 >= 0 ? i - 1 : 0][j - 3 >= 0 ? j - 3 : 0];
                dp[i][j] = (p1 + p2 + p3 + p4) * 0.25;
            }
        }
        return dp[n][n];
    }

    /**
     * #809
     * 
     * @param s
     * @param words
     * @return
     */
    public static int expressiveWords(String s, String[] words) {
        var ss = preprocess(s);
        CharCounter[] words_count = new CharCounter[words.length];
        for (int i = 0; i < words.length; i++) {
            words_count[i] = preprocess(words[i]);
        }
        int ans = 0;
        for (int i = 0; i < words.length; i++) {
            if (ss.chrs.equals(words_count[i].chrs)
                    && countMatch(ss.count_list, words_count[i].count_list)) {
                ans++;
            }
        }
        return ans;
    }

    public static boolean countMatch(List<Integer> a, List<Integer> b) {
        if (a.size() != b.size()) {
            return false;
        }
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i) != b.get(i)
                    && (a.get(i) < b.get(i) || a.get(i) == 2)) {
                return false;
            }
        }
        return true;
    }

    public record CharCounter(String chrs, List<Integer> count_list) {
    }

    // hello -> hel2o
    public static CharCounter preprocess(String s) {
        char prev = ' ';
        int counter = 0;
        StringBuilder sb = new StringBuilder();
        List<Integer> count_list = new ArrayList<>();
        var chrs = s.toCharArray();
        for (int i = 0; i < chrs.length; i++) {
            var c = chrs[i];
            if (i == 0) {
                prev = c;
                counter = 1;
            } else if (c != prev) {
                sb.append(prev);
                count_list.add(counter);
                prev = c;
                counter = 1;
            } else {
                counter++;
            }
        }
        sb.append(prev);
        count_list.add(counter);
        return new CharCounter(sb.toString(), count_list);
    }

    /**
     * #812
     * 
     * @param points
     * @return
     */
    public double largestTriangleArea(int[][] points) {
        var convex_hull = ConvexHull.GrahamScan(
                new ArrayList<>(Arrays.stream(points).toList()), t -> t[0],
                t -> t[1]);
        double ans = 0;
        for (int i = 0; i < convex_hull.size(); i++) {
            var p1 = convex_hull.get(i);
            var x1 = p1[0];
            var y1 = p1[1];
            for (int j = i + 1; j < convex_hull.size(); j++) {
                var p2 = convex_hull.get(j);
                var x2 = p2[0];
                var y2 = p2[1];
                for (int k = j + 1; k < convex_hull.size(); k++) {
                    var p3 = convex_hull.get(k);
                    var x3 = p3[0];
                    var y3 = p3[1];
                    ans = Math.max(ans, 0.5 * Math.abs(
                            (x1 - x3) * (y2 - y1) - (x1 - x2) * (y3 - y1)));
                }
            }
        }
        return ans;
    }

    public static class ConvexHull {
        public static <E> List<E> GrahamScan(List<E> points,
                ToDoubleFunction<E> getX, ToDoubleFunction<E> getY) {
            if (points.size() <= 3) {
                return points;
            }
            var start = points.get(0);
            int start_idx = 0;
            for (var i = 0; i < points.size(); i++) {
                var p = points.get(i);
                if (getY.applyAsDouble(start) > getY.applyAsDouble(p)) {
                    start = p;
                    start_idx = i;
                }
            }

            final var final_start = start;
            swap(points, start_idx, points.size() - 1);
            points.remove(points.size() - 1);
            points.sort((a, b) -> {
                var comp_a = ccw(final_start, a, b, getX, getY);
                if (comp_a == 0) {
                    var comp_d = distance(a, final_start, getX, getY)
                            - distance(b, final_start, getX, getY);
                    return comp_d < 0 ? -1 : comp_d > 0 ? 1 : 0;
                } else
                    return -comp_a < 0 ? -1 : -comp_a > 0 ? 1 : 0;
            });
            List<E> temp = new ArrayList<>(points.size() + 1);
            temp.add(start);
            temp.addAll(points);
            points = temp;
            int s = points.size() - 1;
            while (s >= 0 && ccw(start, points.get(s),
                    points.get(points.size() - 1), getX, getY) == 0) {
                s--;
            }
            s++;

            for (int e = points.size() - 1; s < e; e--, s++) {
                swap(points, s, e);
            }

            Deque<E> points_stack = new ArrayDeque<>();

            points_stack.addLast(points.get(0));
            points_stack.addLast(points.get(1));

            for (int i = 2; i < points.size(); i++) {
                var p = points.get(i);
                var last = points_stack.removeLast();
                while (!points_stack.isEmpty() && ccw(points_stack.getLast(),
                        last, p, getX, getY) < 0) {
                    last = points_stack.removeLast();
                }
                points_stack.addLast(last);
                points_stack.addLast(p);
            }

            List<E> res = new ArrayList<>(points_stack.size());
            res.addAll(points_stack);
            return res;
        }

        private static <E> void swap(List<E> points, int i, int j) {
            var t = points.get(i);
            points.set(i, points.get(j));
            points.set(j, t);
        }

        private static <E> double distance(E a, E b, ToDoubleFunction<E> getX,
                ToDoubleFunction<E> getY) {
            double ax = getX.applyAsDouble(a);
            double ay = getY.applyAsDouble(a);

            double bx = getX.applyAsDouble(b);
            double by = getY.applyAsDouble(b);
            return Math.pow(ax - bx, 2) + Math.pow(ay - by, 2);
        }

        private static <E> double ccw(E a, E b, E c, ToDoubleFunction<E> getX,
                ToDoubleFunction<E> getY) {
            double ax = getX.applyAsDouble(a);
            double ay = getY.applyAsDouble(a);

            double bx = getX.applyAsDouble(b);
            double by = getY.applyAsDouble(b);

            double cx = getX.applyAsDouble(c);
            double cy = getY.applyAsDouble(c);
            // (b[0] - a[0]) * (c[1] - b[1]) - (b[1] - a[1]) * (c[0] - b[0]);
            return (bx - ax) * (cy - by) - (by - ay) * (cx - bx);
        }
    }

    /**
     * #813
     * 
     * @param nums
     * @param k
     * @return
     */
    public double largestSumOfAverages(int[] nums, int k) {
        var n = nums.length;

        var prefix_sum = new int[n + 1];
        System.arraycopy(nums, 0, prefix_sum, 1, n);
        for (int i = 1; i < prefix_sum.length; i++) {
            prefix_sum[i] += prefix_sum[i - 1];
        }

        double[] dp = Arrays.stream(prefix_sum).mapToDouble(v -> v).toArray();
        for (int i = 1; i < dp.length; i++) {
            dp[i] /= i;
        }

        for (int j = 2; j <= k; j++) {
            for (int i = n; i >= 1; i--) {
                for (int x = j - 1; i <= i - 1; i++) {
                    dp[i] = Math.max(dp[i],
                            dp[x] + (prefix_sum[i] - prefix_sum[x]) / (i - x));
                }
            }
        }

        return dp[n];
    }

    /**
     * #843
     * 
     * @param words
     * @param master
     */
    public void findSecretWord(String[] words, Master master) {
        int[][] cache = new int[words.length][words.length];
        List<HashSet<Integer>> sets = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            sets.add(new HashSet<>());
        }
        for (int i = 0; i < words.length; i++) {
            cache[i][i] = 6;
            for (int j = i + 1; j < words.length; j++) {
                cache[i][j] = similarity(words[i], words[j]);
                cache[j][i] = cache[i][j];
                var set = sets.get(cache[i][j]);
                set.add(i);
                set.add(j);
            }
        }

        while (true) {
            sets.sort((a, b) -> Integer.compare(a.size(), b.size()));
            sets.removeIf((s) -> s.size() == 0);
            if (sets.isEmpty()) {
                return;
            }
            var set = sets.get(0);
            var guess_idx = set.iterator().next();
            var guess = words[guess_idx];
            var ret = master.guess(guess);
            if (ret == 6) {
                return;
            }
            for (var s : sets) {
                HashSet<Integer> toRemove = new HashSet<>();
                for (var w_idx : s) {
                    if (similarity(words[w_idx], words[guess_idx]) != ret) {
                        toRemove.add(w_idx);
                    }
                }
                s.removeAll(toRemove);
            }
        }
    }

    int similarity(String s1, String s2) {
        var c1 = s1.toCharArray();
        var c2 = s2.toCharArray();
        int res = 0;
        for (int i = 0; i < c1.length; i++) {
            if (c1[i] == c2[i]) {
                res++;
            }
        }
        return res;
    }

}
