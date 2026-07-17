package Leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

interface Master {
    int guess(String word);
}

@SuppressWarnings("JavaDoc")
public class Leetcode900 {
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

    /**
     * #846
     * 
     * @param hand
     * @param groupSize
     * @return
     */
    public boolean isNStraightHand(int[] hand, int groupSize) {
        TreeMap<Integer, Integer> cardCount = new TreeMap<>();
        for (var c : hand) {
            cardCount.put(c, cardCount.getOrDefault(c, 0) + 1);
        }
        while (!cardCount.isEmpty()) {
            var first = cardCount.firstKey();
            if (cardCount.get(first) == 1)
                cardCount.remove(first);
            else
                cardCount.put(first, cardCount.get(first) - 1);
            for (int i = 1; i < groupSize; i++) {
                var next = first + i;
                if (!cardCount.containsKey(next)) {
                    return false;
                }
                var nextCount = cardCount.get(next) - 1;
                if (nextCount == 0)
                    cardCount.remove(next);
                else
                    cardCount.put(next, nextCount);
            }
        }
        return true;
    }

    /**
     * #847
     * 
     * @param graph
     * @return
     */
    public int shortestPathLength(int[][] graph) {
        if (graph.length == 1)
            return 0;
        Queue<NodePath> queue = new ArrayDeque<>();
        Set<NodePath> seen = new HashSet<>();

        for (int i = 0; i < graph.length; i++) {
            var visited = new BitSet(graph.length);
            visited.set(i);
            var np = new NodePath(i, visited, 0);
            seen.add(np);
            queue.add(np);
        }
        while (!queue.isEmpty()) {
            var np = queue.poll();
            for (var nb : graph[np.node]) {
                var visited = new BitSet(graph.length);
                visited.or(np.visited);
                visited.set(nb);
                var nextNp = new NodePath(nb, visited, np.len + 1);
                if (visited.cardinality() == graph.length)
                    return nextNp.len;
                if (!seen.contains(nextNp)) {
                    seen.add(nextNp);
                    queue.add(nextNp);
                }
            }
        }
        return -1;
    }

    static record NodePath(int node, BitSet visited, int len) {
        @Override
        public final int hashCode() {
            return Objects.hash(node, visited);
        }

        @Override
        public final boolean equals(Object arg0) {
            if (arg0 instanceof NodePath o) {
                return node == o.node && visited.equals(o.visited);
            }
            return false;
        }
    }
    /**
     * #853
     * 
     * @param target
     * @param position
     * @param speed
     * @return
     */
    public int carFleet(int target, int[] position, int[] speed) {
        var pos2speed = new TreeMap<Integer, Integer>();
        for (int i = 0; i < position.length; i++) {
            pos2speed.put(position[i], speed[i]);
        }
        int res = 0;
        double fleetTime = 0;
        while (!pos2speed.isEmpty()) {
            var car = pos2speed.pollLastEntry();
            double time = (target - car.getKey()) / (double) car.getValue();
            if (res != 0 && time < fleetTime) {
            } else {
                fleetTime = (target - car.getKey()) / (double) car.getValue();
                res++;
            }

        }
        return res;
    }

    /**
     * #854
     * 
     * @param s1
     * @param s2
     * @return
     */
    public int kSimilarity(String s1, String s2) {
        Set<Anagram> seen = new HashSet<>();
        Queue<Anagram> queue = new ArrayDeque<>();
        var target = s2.toCharArray();
        {
            var ag = new Anagram(s1.toCharArray(), 0, 0);
            queue.add(ag);
            seen.add(ag);
        }
        while (!queue.isEmpty()) {
            var ag = queue.poll();

            int head = ag.idx;
            while (head < ag.state.length && ag.state[head] == target[head]) {
                head++;
            }
            if (head == target.length)
                return ag.count;

            for (int i = head + 1; i < target.length; i++) {
                if (target[head] == ag.state[i]) {
                    var next_state = new char[s1.length()];
                    System.arraycopy(ag.state, 0, next_state, 0, target.length);
                    var t = next_state[head];
                    next_state[head] = next_state[i];
                    next_state[i] = t;
                    var a = new Anagram(next_state, head + 1, ag.count + 1);
                    if (!seen.contains(a)) {
                        queue.add(a);
                        seen.add(a);
                    }
                }
            }
        }
        return 0;
    }

    static record Anagram(char[] state, int idx, int count) {

        @Override
        public final int hashCode() {
            return Objects.hash(Arrays.hashCode(state));
        }

        @Override
        public final boolean equals(Object arg0) {
            if (arg0 instanceof Anagram other) {
                return Arrays.equals(state, other.state);
            }
            return false;
        }
    }

    /**
     * #855
     * 
     */
    public class ExamRoom {
        class Range implements Comparable<Range> {
            int[] arr = new int[2];

            public Range(int a, int b) {
                arr[0] = a;
                arr[1] = b;
            }

            public int left() {
                return arr[0];
            }

            public int right() {
                return arr[1];
            }

            public int len() {
                return (arr[0] == -1 || arr[1] == count) ? arr[1] - arr[0] - 1
                        : (arr[1] - arr[0]) >> 1;
            }

            public int seat() {
                if (arr[0] == -1) {
                    return 0;
                }
                return arr[0] + len();
            }

            @Override
            public int compareTo(Range o) {
                var c = Integer.compare(this.len(), o.len());
                if (c == 0) {
                    return Integer.compare(o.seat(), seat());
                }
                return c;
            }

        }

        TreeSet<Range> treeSet = new TreeSet<>();
        HashMap<Integer, Range> left;
        HashMap<Integer, Range> right;
        int count;

        public ExamRoom(int n) {
            count = n;
            left = new HashMap<>();
            right = new HashMap<>();
            var range = new Range(-1, n);
            treeSet.add(range);
            left.put(-1, range);
            right.put(n, range);
        }

        void addRange(Range r) {
            treeSet.add(r);
            left.put(r.left(), r);
            right.put(r.right(), r);
        }

        void removeRange(Range r) {
            treeSet.remove(r);
            left.remove(r.left());
            right.remove(r.right());
        }

        public int seat() {
            var range = treeSet.pollLast();
            var s = range.seat();
            var l_range = new Range(range.left(), s);
            var r_range = new Range(s, range.right());
            left.remove(range.left());
            right.remove(range.right());
            if (l_range.len() > 0) {
                addRange(l_range);
            }
            if (r_range.len() > 0) {
                addRange(r_range);
            }
            return s;
        }

        public void leave(int p) {
            var l_range = right.get(p);
            var r_range = left.get(p);
            if (l_range != null) {
                removeRange(l_range);
            }
            if (r_range != null) {
                removeRange(r_range);
            }
            Range new_range = null;
            if (l_range != null && r_range != null) {
                new_range = new Range(l_range.left(), r_range.right());
            } else if (l_range != null) {
                new_range = new Range(l_range.left(), p + 1);
            } else if (r_range != null) {
                new_range = new Range(p - 1, r_range.right());
            } else {
                new_range = new Range(p - 1, p + 1);
            }
            addRange(new_range);
        }
    }

    /**
     * #875
     * 
     * @param piles
     * @param h
     * @return
     */
    public int minEatingSpeed(int[] piles, int h) {
        int l = 1, r = Arrays.stream(piles).max().getAsInt();
        if (piles.length == h) {
            return r;
        }
        while (r - l >= 1) {
            int mid = l + (r - l) / 2;
            int time = Arrays.stream(piles).map(p -> Math.ceilDiv(p, mid))
                    .sum();
            if (time > h) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    /**
     * #876
     *
     * @param head
     * @return
     */
    public static ListNode middleNode(ListNode head) {
        if (head.next == null)
            return head;
        ListNode slow = head, fast = head;
        while (fast != null) {
            slow = slow.next;
            fast = fast != null ? fast.next : null;
            fast = fast != null ? fast.next : null;
            if (fast != null && fast.next == null) {
                return slow;
            }
        }
        return slow;
    }

    /**
     * #878 periodical method
     *
     * @param n
     * @param a
     * @param b
     * @return
     */
    public static int nthMagicalNumber(int n, int a, int b) {
        class Data {
            final long num;
            final long multiplier;
            final long result;

            Data(long n, long m, long r) {
                num = n;
                multiplier = m;
                result = r;
            }
        }
        int mod = (int) (1e9 + 7);
        var lcm_num = lcm(a, b);
        var bound_a = lcm_num / a;
        var bound_b = lcm_num / b;
        var len = bound_a + bound_b - 1;
        List<Data> array = new ArrayList<>((int) len);
        for (long i = 1; i < bound_a; i++) {
            array.add(new Data(a, i, a * i));
        }
        for (long i = 1; i < bound_b; i++) {
            array.add(new Data(b, i, b * i));
        }
        array.add(new Data(a, bound_a, a * bound_a));
        array.sort(Comparator.comparingLong(d -> d.result));

        var iter = (n - 1) / len;
        var idx = (n - 1) % len;
        var d = array.get((int) idx);
        var num = d.num;
        var mul = d.multiplier;
        if (num == a)
            return (int) (num * ((mul + iter * bound_a) % mod) % mod);
        else
            return (int) (num * ((mul + iter * bound_b) % mod) % mod);
    }

    private static long gcd(long a, long b) {
        return b != 0 ? gcd(b, a % b) : a;
    }

    private static long lcm(long a, long b) {
        return a * b / gcd(a, b);
    }

    /**
     * #894
     * 
     * @param n
     * @return
     */
    public List<TreeNode> allPossibleFBT(int n) {
        if (n % 2 == 0)
            return List.of();
        return null;
    }
}

/**
 * #895
 */
class FreqStack {

    Map<Integer, Integer> val2freq = new HashMap<>();
    Map<Integer, Deque<Integer>> freq2stack = new HashMap<>();
    int max_freq = 0;

    public FreqStack() {

    }

    public void push(int val) {
        var freq = val2freq.getOrDefault(val, 0) + 1;
        val2freq.put(val, freq);
        max_freq = Math.max(freq, max_freq);
        freq2stack.computeIfAbsent(freq, (k) -> new ArrayDeque<>())
                .addLast(val);
    }

    public int pop() {
        var stack = freq2stack.get(max_freq);
        var val = stack.pollLast();
        if (stack.isEmpty()) {
            freq2stack.remove(max_freq--);
        }
        if (val2freq.get(val) == 1) {
            val2freq.remove(val);
        } else {
            val2freq.put(val, val2freq.get(val) - 1);
        }
        return val;
    }
}
