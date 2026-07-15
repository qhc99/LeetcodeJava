package Leetcode;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.management.RuntimeErrorException;

@SuppressWarnings({ "JavaDoc" })
class Leetcode750 {

    /**
     * #701
     *
     * @param root
     * @param val
     * @return
     */
    public static TreeNode insertIntoBST(TreeNode root, int val) {
        if (root == null) {
            return new TreeNode(val);
        }
        var recurFunc = new Object() {
            void apply(TreeNode n) {
                if (n.val < root.val) {
                    if (n.left == null) {
                        n.left = new TreeNode(val);
                    } else {
                        apply(n.left);
                    }
                } else {
                    if (n.right == null) {
                        n.right = new TreeNode(val);
                    } else {
                        apply(n.right);
                    }
                }
            }
        };
        recurFunc.apply(root);
        return root;
    }

    class KthLargest {
        Queue<Integer> geq = new PriorityQueue<>();
        int k;

        public KthLargest(int k, int[] nums) {
            this.k = k;
            Arrays.sort(nums);
            for (int i = nums.length - 1; i >= 0; i--) {
                if (nums.length - i <= k) {
                    geq.add(nums[i]);
                } else {
                    break;
                }
            }
        }

        public int add(int val) {
            if (geq.size() < k || val > geq.peek())
                geq.add(val);
            if (geq.size() > k) {
                geq.poll();
            }
            return geq.peek();
        }
    }

    /**
     * #726
     *
     * @param formula
     * @return
     */
    public static String countOfAtoms(String formula) {
        // A ( 1
        // A a ( ) 1
        var map = map_of_atoms(0, formula.length(), formula);
        var l = new ArrayList<>(map.entrySet().stream().toList());
        l.sort(Map.Entry.comparingByKey());
        StringBuilder sb = new StringBuilder();
        for (var e : l) {
            sb.append(e.getKey());
            if (e.getValue() != 1) {
                sb.append(e.getValue());
            }
        }
        return sb.toString();
    }

    private static Map<String, Integer> map_of_atoms(int start, int end,
            String formula) {
        if (end - start < 1) {
            return Map.of();
        }
        Map<String, Integer> count_of_atoms = new HashMap<>();
        int idx = start;
        while (idx < end) {
            char c = formula.charAt(idx);
            if (c >= 'A' && c <= 'Z') {
                int end_idx_atom = end_of_atom(idx, formula);
                String atom = formula.substring(idx, end_idx_atom);
                if (end_idx_atom < end) {
                    var end_char = formula.charAt(end_idx_atom);
                    if (end_char <= '9' && end_char >= '0') {
                        int end_idx_num = end_of_num(end_idx_atom, formula);
                        int num = Integer.parseInt(
                                formula.substring(end_idx_atom, end_idx_num));
                        count_of_atoms.put(atom,
                                count_of_atoms.getOrDefault(atom, 0) + num);
                        idx = end_idx_num;
                    } else {
                        count_of_atoms.put(atom,
                                count_of_atoms.getOrDefault(atom, 0) + 1);
                        idx = end_idx_atom;
                    }
                } else {
                    count_of_atoms.put(atom,
                            count_of_atoms.getOrDefault(atom, 0) + 1);
                    idx = end_idx_atom;
                }
            } else if (c == '(') {
                int end_idx_str = sub_string(idx, formula);
                var sub_map = map_of_atoms(idx + 1, end_idx_str - 1, formula);
                if (end_idx_str < end) {
                    var end_char = formula.charAt(end_idx_str);
                    if (end_char <= '9' && end_char >= '0') {
                        int end_idx_num = end_of_num(end_idx_str, formula);
                        int num = Integer.parseInt(
                                formula.substring(end_idx_str, end_idx_num));
                        for (var entry : sub_map.entrySet()) {
                            entry.setValue(entry.getValue() * num);
                        }
                        count_of_atoms = merge_maps(count_of_atoms, sub_map);
                        idx = end_idx_num;
                    } else {
                        count_of_atoms = merge_maps(count_of_atoms, sub_map);
                        idx = end_idx_str;
                    }
                } else {
                    count_of_atoms = merge_maps(count_of_atoms, sub_map);
                    idx = end_idx_str;
                }
            } else {
                throw new RuntimeException("unexpected char type");
            }
        }
        return count_of_atoms;
    }

    public static int sub_string(int start, String formula) {
        int i = start + 1;
        int stack_count = 1;
        for (; i < formula.length(); i++) {
            var c = formula.charAt(i);
            if (c == '(') {
                stack_count++;
            } else if (c == ')') {
                stack_count--;
                if (stack_count == 0) {
                    return i + 1;
                }
            }
        }
        throw new RuntimeException("invalid sub string");
    }

    private static Map<String, Integer> merge_maps(Map<String, Integer> m1,
            Map<String, Integer> m2) {
        if (m1.isEmpty())
            return m2;
        else if (m2.isEmpty())
            return m1;
        else {
            if (m1.size() < m2.size()) {
                for (var kv : m1.entrySet()) {
                    m2.put(kv.getKey(),
                            kv.getValue() + m2.getOrDefault(kv.getKey(), 0));
                }
                return m2;
            } else {
                for (var kv : m2.entrySet()) {
                    m1.put(kv.getKey(),
                            kv.getValue() + m1.getOrDefault(kv.getKey(), 0));
                }
                return m1;
            }
        }
    }

    private static int end_of_atom(int start, String formula) {
        int i = start + 1;
        for (; i < formula.length(); i++) {
            char c = formula.charAt(i);
            if (!(c >= 'a' && c <= 'z')) {
                return i;
            }
        }
        return i;
    }

    private static int end_of_num(int start, String formula) {
        int i = start;
        for (; i < formula.length(); i++) {
            char c = formula.charAt(i);
            if (!(c >= '0' && c <= '9')) {
                return i;
            }
        }
        return i;
    }

    /**
     * #731
     */
    class MyCalendarTwo {

        private static final int INF_R = 1_000_000_000;

        private static final class Node {
            int agg; // max bookings over this segment
            int tag; // lazy tag to push to children
            int left; // index of left child in arr (0 = none)
            int right; // index of right child in arr (0 = none)
        }

        private final ArrayList<Node> arr = new ArrayList<>(2048);

        public MyCalendarTwo() {
            arr.add(new Node()); // index 0 unused
            arr.add(new Node()); // root at index 1
        }

        public boolean book(int startTime, int endTime) {
            update(1, +1, 0, INF_R, startTime, endTime - 1); // inclusive
                                                             // [startTime,
                                                             // endTime]
            int k = 2;
            if (arr.get(1).agg > k) {
                update(1, -1, 0, INF_R, startTime, endTime - 1); // rollback
                return false;
            }
            return true;
        }

        private void update(int n, int diff, int l, int r, int ql, int qr) {
            Node cur = arr.get(n);
            // Fully covered segment
            if (l >= ql && r <= qr) {
                cur.agg += diff;
                cur.tag += diff;
                return;
            }

            int mid = l + ((r - l) >> 1);

            // Ensure children exist
            if (cur.left == 0) {
                cur.left = arr.size();
                arr.add(new Node());
            }
            if (cur.right == 0) {
                cur.right = arr.size();
                arr.add(new Node());
            }

            // Push down lazy tag
            if (cur.tag != 0) {
                Node lc = arr.get(cur.left);
                Node rc = arr.get(cur.right);
                lc.agg += cur.tag;
                lc.tag += cur.tag;
                rc.agg += cur.tag;
                rc.tag += cur.tag;
                cur.tag = 0;
            }

            // Recurse where overlaps
            if (mid >= ql)
                update(cur.left, diff, l, mid, ql, qr);
            if (mid + 1 <= qr)
                update(cur.right, diff, mid + 1, r, ql, qr);

            Node lc = arr.get(cur.left);
            Node rc = arr.get(cur.right);
            cur.agg = Math.max(lc.agg, rc.agg);
        }
    }

    /**
     * #732
     */
    class MyCalendarThree {
        private static final int INF_R = 1_000_000_000;

        private static final class Node {
            int agg; // max bookings over this segment
            int tag; // lazy tag to push to children
            int left; // index of left child in arr (0 = none)
            int right; // index of right child in arr (0 = none)
        }

        private final ArrayList<Node> arr = new ArrayList<>(2048);

        public MyCalendarThree() {
            arr.add(new Node()); // index 0 unused
            arr.add(new Node()); // root at index 1
        }

        public int book(int startTime, int endTime) {
            update(1, +1, 0, INF_R, startTime, endTime - 1); // inclusive
                                                             // [startTime,
                                                             // endTime]
            return arr.get(1).agg;
        }

        private void update(int n, int diff, int l, int r, int ql, int qr) {
            Node cur = arr.get(n);
            // Fully covered segment
            if (l >= ql && r <= qr) {
                cur.agg += diff;
                cur.tag += diff;
                return;
            }

            int mid = l + ((r - l) >> 1);

            // Ensure children exist
            if (cur.left == 0) {
                cur.left = arr.size();
                arr.add(new Node());
            }
            if (cur.right == 0) {
                cur.right = arr.size();
                arr.add(new Node());
            }

            // Push down lazy tag
            if (cur.tag != 0) {
                Node lc = arr.get(cur.left);
                Node rc = arr.get(cur.right);
                lc.agg += cur.tag;
                lc.tag += cur.tag;
                rc.agg += cur.tag;
                rc.tag += cur.tag;
                cur.tag = 0;
            }

            // Recurse where overlaps
            if (mid >= ql)
                update(cur.left, diff, l, mid, ql, qr);
            if (mid + 1 <= qr)
                update(cur.right, diff, mid + 1, r, ql, qr);

            Node lc = arr.get(cur.left);
            Node rc = arr.get(cur.right);
            cur.agg = Math.max(lc.agg, rc.agg);
        }
    }

    /**
     * #735
     * 
     * @param asteroids
     * @return
     */
    public int[] asteroidCollision(int[] asteroids) {
        Deque<Integer> queue = new ArrayDeque<>(asteroids.length);
        for (var n : asteroids) {
            if (queue.isEmpty()) {
                queue.add(n);
                continue;
            }
            if (n < 0) {
                while (!queue.isEmpty() && queue.getLast() > 0
                        && -n > queue.getLast()) {
                    queue.pollLast();
                }
                if (!queue.isEmpty() && queue.getLast() == -n)
                    queue.pollLast();
                else if (queue.isEmpty() || queue.getLast() < 0)
                    queue.add(n);
            } else {
                queue.add(n);
            }
        }
        return queue.stream().mapToInt(a -> a).toArray();
    }

    /**
     * #739
     * 
     * @param temperatures
     * @return
     */
    public static int[] dailyTemperatures(int[] temperatures) {
        Deque<int[]> stack = new ArrayDeque<>(temperatures.length);
        int[] ans = new int[temperatures.length];
        for (int i = 0; i < temperatures.length; i++) {
            var t = temperatures[i];
            if (stack.size() == 0 || stack.getLast()[0] >= t) {
                stack.addLast(new int[] { t, i });
            } else {
                while (stack.size() > 0 && stack.getLast()[0] < t) {
                    var idx = stack.pollLast()[1];
                    ans[idx] = i - idx;
                }
                stack.addLast(new int[] { t, i });
            }
        }
        return ans;
    }

    /**
     * #743
     * 
     * @param times
     * @param n
     * @param k
     * @return
     */
    public int networkDelayTime(int[][] times, int n, int k) {
        int time = 0;
        BitSet visited = new BitSet(n + 1);
        Map<Integer, List<Edge>> graph = new HashMap<>();
        for (var e : times) {
            graph.computeIfAbsent(e[0], key -> new ArrayList<>())
                    .add(new Edge(e[1], e[2]));
        }
        Queue<Edge> queue = new PriorityQueue<>((a, b) -> a.time - b.time);
        queue.addAll(graph.getOrDefault(k, List.of()));
        visited.set(k);
        while (visited.cardinality() < n && !queue.isEmpty()) {
            var e = queue.poll();
            if (visited.get(e.node)) {
                continue;
            }
            visited.set(e.node);
            time = Math.max(time, e.time);
            for (var nextEdge : graph.getOrDefault(e.node, List.of())) {
                if (!visited.get(nextEdge.node)) {
                    queue.add(new Edge(nextEdge.node, time + nextEdge.time));
                }
            }
        }
        return visited.cardinality() == n ? time : -1;
    }

    static record Edge(int node, int time) {
    }

    /**
     * #746
     * 
     * @param cost
     * @return
     */
    public int minCostClimbingStairs(int[] cost) {
        int[] dp = new int[cost.length + 1];
        for (int i = 2; i < dp.length; i++) {
            dp[i] = Math.min(dp[i - 2] + cost[i - 2], dp[i - 1] + cost[i - 1]);
        }
        return dp[cost.length];
    }
}

@SuppressWarnings({ "unused", "JavaDoc" })
public class Leetcode800 {
    /**
     * #739
     * 
     * @param temperatures
     * @return
     */
    public int[] dailyTemperatures(int[] temperatures) {
        var queue = new ArrayDeque<Integer>(temperatures.length);
        int[] res = new int[temperatures.length];
        for (int i = 0; i < temperatures.length; i++) {
            while (!queue.isEmpty()
                    && temperatures[i] > temperatures[queue.getLast()]) {
                var ii = queue.pollLast();
                res[ii] = i - ii;
            }
            if (queue.isEmpty()
                    || temperatures[queue.getLast()] >= temperatures[i]) {
                queue.add(i);
            }
        }
        return res;
    }

    /**
     * #751
     * 
     * @param ip
     * @param n
     * @return
     */
    public List<String> ipToCIDR(String ip, int n) {
        List<String> res = new ArrayList<>();
        ipToCIDR(ipToInt(ip), n, res);
        return res;
    }

    void ipToCIDR(long ip, int n, List<String> res) {
        if (n <= 0)
            return;
        if (n == 1) {
            res.add(CIDR2Ip(new CIDR(ip, 32)));
            return;
        }
        int digitsCount = 9;
        long l = ip;
        long r = l + n - 1;
        long s = 0, e = 0;
        for (; digitsCount >= 0; digitsCount--) {
            s = l & (-1 << (digitsCount));
            e = l | ((1 << digitsCount) - 1);
            if (l <= s && e <= r) {
                res.add(CIDR2Ip(new CIDR(s, 32 - digitsCount)));
                break;
            }
        }

        ipToCIDR(l, (int) (s - l), res);
        ipToCIDR(e + 1, (int) (r - e), res);

    }

    static record CIDR(long base, int mask) {
    }

    public String CIDR2Ip(CIDR cidr) {
        var base = cidr.base;
        var mask = cidr.mask;
        StringBuilder sb = new StringBuilder();
        for (int i = 3; i >= 0; i--) {
            sb.append((base >> (i * 8)) & ~(-1 << 8));
            if (i != 0)
                sb.append(".");
        }
        sb.append("/");
        sb.append(mask);

        return sb.toString();
    }

    long ipToInt(String ip) {
        var arr = ip.split("\\.");
        long res = 0;
        for (int i = 0; i < 4; i++) {
            var shift = (3 - i) * 8;
            res += Long.valueOf(arr[i]) << shift;
        }

        return res;
    }

    /**
     * #753
     * 
     * @param n
     * @param k
     * @return
     */
    public static String crackSafe(int n, int k) {
        if (n == 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < k; i++) {
                sb.append(i);
            }
            return sb.toString();
        }
        int[] biggest_edge = new int[(int) Math.pow(k, n - 1)];
        Arrays.fill(biggest_edge, k - 1);
        StringBuilder sb = new StringBuilder("0".repeat(n - 1));
        int current = 0;
        while (biggest_edge[current] != -1) {
            int edge = biggest_edge[current]--;
            sb.append(edge);
            current = (current * k) % (int) Math.pow(k, n - 1) + edge;
        }

        return sb.toString();
    }

    /**
     * max match length of prefix of P and suffix end with ith(start from 1)
     * character
     *
     * @param P string
     * @return prefix function (length = len(p) + 1)
     */
    private static int[] computePrefixFunction(String P) {
        int m = P.length();
        int[] pi = new int[m + 1];
        pi[1] = 0;
        for (int q = 1, k = 0; q < m; q++) {
            while (k > 0 && P.charAt(k) != P.charAt(q)) {
                k = pi[k];
            }
            if (P.charAt(k) == P.charAt(q)) {
                k++;
            }
            pi[q + 1] = k;
        }
        return pi;
    }

    private static boolean next(int[] arr, int k) {
        boolean remain = false;
        for (int i = arr.length - 1; i >= 0; i--) {
            arr[i]++;
            if (arr[i] == k) {
                arr[i] = 0;
                remain = true;
            } else {
                remain = false;
                break;
            }
        }
        return !remain;
    }

    private static String numArrToString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (var i : arr) {
            sb.append(i);
        }
        return sb.toString();
    }

    /**
     * #763 <br/>
     * 划分字母区间
     * 
     * <pre>
     * 输入：S = "ababcbacadefegdehijhklij"
     * 输出：[9,7,8]
     * 解释：
     * 划分结果为 "ababcbaca", "defegde", "hijhklij"。
     * 每个字母最多出现在一个片段中。
     * 像 "ababcbacadefegde", "hijhklij" 的划分是错误的，因为划分的片段数较少。
     * </pre>
     *
     * @param S string
     * @return res
     */
    public static List<Integer> partitionLabels(String S) {
        int[] lastIndexOfChar = new int[26];
        for (int i = 0; i < S.length(); i++) {
            lastIndexOfChar[S.charAt(i) - 'a'] = i;
        }
        int startIdx = 0, endIndex = 0;
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < S.length(); i++) {
            endIndex = Math.max(lastIndexOfChar[S.charAt(i) - 'a'], endIndex);
            if (i == endIndex) {
                res.add(endIndex - startIdx + 1);
                startIdx = endIndex + 1;
            }
        }
        return res;
    }

    public static final class DisjointSet {
        private int rank = 0;
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
            }
            return x.parent;
        }

        public static boolean inSameSet(DisjointSet a, DisjointSet b) {
            return findGroupId(a) == findGroupId(b);
        }

        public static void union(DisjointSet a, DisjointSet b) {
            link(findGroupId(a), findGroupId(b));
        }

        private static void link(DisjointSet x, DisjointSet y) {
            if (x.rank > y.rank) {
                y.parent = x;
            } else {
                x.parent = y;
                if (x.rank == y.rank) {
                    y.rank = y.rank + 1;
                }
            }
        }
    }

    /**
     * #765
     * 
     * @param row
     * @return
     */
    public static int minSwapsCouples(int[] row) {
        int N = row.length / 2;
        DisjointSet[] unions = new DisjointSet[N];
        for (int i = 0; i < unions.length; i++) {
            unions[i] = new DisjointSet();
        }
        for (int i = 0; i < row.length; i += 2) {
            int couple1 = row[i] / 2;
            int couple2 = row[i + 1] / 2;
            DisjointSet.union(unions[couple1], unions[couple2]);
        }
        int count = 0;
        for (var d : unions) {
            if (d == d.parent) {
                count++;
            }
        }
        return N - count;
    }

    /**
     * #768
     * 
     * @param arr
     * @return
     */
    public int maxChunksToSorted(int[] arr) {
        var future_min = new int[arr.length];
        var current_min = Integer.MAX_VALUE;
        for (int i = arr.length - 1; i >= 0; i--) {
            future_min[i] = current_min;
            current_min = Math.min(current_min, arr[i]);
        }
        int current_max = arr[0];
        int groups = 0;
        int last_split = 0;
        for (int i = 0; i < arr.length; i++) {
            current_max = Math.max(current_max, arr[i]);
            if (current_max <= future_min[i]) {
                groups++;
                last_split = i;
            }
        }
        return groups + (last_split == arr.length - 1 ? 0 : 1);
    }

    /**
     * #778
     * 
     * @param grid
     * @return
     */
    public static int swimInWater(int[][] grid) {
        final int n = grid.length;
        if (n == 1) {
            return grid[0][0];
        }

        int currentHeight = grid[0][0];

        Queue<SwimPoolHeight> surrounding = new PriorityQueue<>(
                Comparator.comparing(d -> d.height));
        surrounding.add(new SwimPoolHeight(0, 1, grid[0][1]));
        surrounding.add(new SwimPoolHeight(1, 0, grid[1][0]));

        boolean[][] visited = new boolean[n][n];
        visited[0][0] = true;
        visited[0][1] = true;
        visited[1][0] = true;

        while (!surrounding.isEmpty()) {
            while (surrounding.peek().height <= currentHeight) {
                var d = surrounding.poll();
                int x = d.x, y = d.y;
                if (x == n - 1 && y == n - 1) {
                    return currentHeight;
                }
                if (x - 1 >= 0 && !visited[x - 1][y]) {
                    visited[x - 1][y] = true;
                    surrounding
                            .add(new SwimPoolHeight(x - 1, y, grid[x - 1][y]));
                }
                if (y - 1 >= 0 && !visited[x][y - 1]) {
                    visited[x][y - 1] = true;
                    surrounding
                            .add(new SwimPoolHeight(x, y - 1, grid[x][y - 1]));
                }
                if (x + 1 < n && !visited[x + 1][y]) {
                    visited[x + 1][y] = true;
                    surrounding
                            .add(new SwimPoolHeight(x + 1, y, grid[x + 1][y]));
                }
                if (y + 1 < n && !visited[x][y + 1]) {
                    visited[x][y + 1] = true;
                    surrounding
                            .add(new SwimPoolHeight(x, y + 1, grid[x][y + 1]));
                }
            }
            currentHeight = surrounding.peek().height;
        }

        throw new RuntimeException();
    }

    private static class SwimPoolHeight {
        final int x;
        final int y;
        final int height;

        public SwimPoolHeight(int x, int y, int h) {
            this.x = x;
            this.y = y;
            this.height = h;
        }
    }

    /**
     * # 780
     * 
     * @param sx
     * @param sy
     * @param tx
     * @param ty
     * @return
     */
    public static boolean reachingPoints(int sx, int sy, int tx, int ty) {
        if (tx < sx || ty < sy) {
            return false;
        }
        while (tx > sx && ty > sy) {
            if (tx > ty && tx - ty >= sx) {
                tx -= ty;
            } else if (tx < ty && ty - tx >= sy) {
                ty -= tx;
            } else {
                break;
            }
        }
        if (tx == sx && ty == sy) {
            return true;
        } else if (tx == sx) {
            int diff = ty - sy;
            return diff % sx == 0;
        } else if (ty == sy) {
            int diff = tx - sx;
            return diff % sy == 0;
        } else
            return false;
    }

    /**
     * #781 <br/>
     * 森林中的兔子 <br/>
     * 森林中，每个兔子都有颜色。其中一些兔子（可能是全部）告诉你还有多少其他的兔子和自己有相同的颜色。 我们将这些回答放在answers数组里。
     * <br/>
     * 要求返回森林中兔子的最少数量。
     *
     * @param answers answers
     * @return count of rabbit
     */
    public static int numRabbits(int[] answers) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int y : answers) {
            count.put(y, count.getOrDefault(y, 0) + 1);
        }
        int ans = 0;
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            int y = entry.getKey(), x = entry.getValue();
            ans += (x + y) / (y + 1) * (y + 1);
        }
        return ans;
    }

    /**
     * #782
     * 
     * @param board
     * @return
     */
    public static int movesToChessboard(int[][] board) {
        int n = board.length;
        var r = new Ref();
        int exchange = 0;
        var col_mask = findExchange(board, true, r);
        exchange = r.val;
        var raw_mask = findExchange(board, false, r);
        exchange += r.val;
        if (col_mask == null || raw_mask == null) {
            return -1;
        }
        for (var col : board) {
            for (int i = 1; i < col_mask.length; i++) {
                int idx = col_mask[i];
                int prev_idx = col_mask[i - 1];
                if (col[idx] == col[prev_idx]) {
                    return -1;
                }
            }
        }

        return exchange;
    }

    private static class Ref {
        int val;
    }

    private static int[] findExchange(int[][] board, boolean find_col,
            Ref ref) {
        int n = board.length;
        List<Integer> zero_odd = new ArrayList<>(n / 2 + 1);
        List<Integer> zero_even = new ArrayList<>(n / 2 + 1);
        List<Integer> one_odd = new ArrayList<>(n / 2 + 1);
        List<Integer> one_even = new ArrayList<>(n / 2 + 1);
        Deque<Integer> indices1 = null, indices2 = null;

        for (int i = 0; i < n; i++) {
            boolean is_one = find_col ? board[0][i] == 1 : board[i][0] == 1;
            if (i % 2 == 0) {
                if (is_one) {
                    one_even.add(i);
                } else {
                    zero_even.add(i);
                }
            } else {
                if (is_one) {
                    one_odd.add(i);
                } else {
                    zero_odd.add(i);
                }
            }
        }
        int diff = (zero_even.size() + zero_odd.size())
                - (one_even.size() + one_odd.size());
        if (n % 2 == 0) {
            if (diff != 0) {
                return null;
            }
            if (zero_even.size() < zero_odd.size()) {
                ref.val = zero_even.size();
                indices1 = new ArrayDeque<>(zero_even);
                indices2 = new ArrayDeque<>(one_odd);
            } else {
                ref.val = zero_odd.size();
                indices1 = new ArrayDeque<>(zero_odd);
                indices2 = new ArrayDeque<>(one_even);
            }

        } else {
            if (Math.abs(diff) != 1) {
                return null;
            }
            if (zero_even.size() == one_odd.size()) {
                ref.val = zero_even.size();
                indices1 = new ArrayDeque<>(zero_even);
                indices2 = new ArrayDeque<>(one_odd);
            } else {
                ref.val = zero_odd.size();
                indices1 = new ArrayDeque<>(zero_odd);
                indices2 = new ArrayDeque<>(one_even);
            }
        }

        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = i;
        }
        while (!indices1.isEmpty()) {
            var idx1 = indices1.pollLast();
            var idx2 = indices2.pollLast();
            ans[idx1] = idx2;
            ans[idx2] = idx1;
        }
        return ans;
    }

    /**
     * #785 <br>
     * bipartite graph: vertex and its neighbors has different color
     *
     * @param graph graph
     * @return graph is bipartite
     */
    public static boolean isBipartite(int[][] graph) {
        int vertices_count = graph.length;
        Map<Integer, Color> vertex_color = new HashMap<>();
        for (int i = 0; i < vertices_count; i++) {
            vertex_color.put(i, Color.GRAY);
        }
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < vertices_count; i++) {
            if (vertex_color.get(i) == Color.GRAY) {
                var is_bipartite = BFS(i, queue, vertex_color, graph);
                if (!is_bipartite) {
                    return false;
                }
            }
        }
        return true;
    }

    private enum Color {
        GRAY, RED, GREEN
    }

    private static boolean BFS(Integer start, Queue<Integer> queue,
            Map<Integer, Color> vertex_color, int[][] graph) {
        queue.add(start);
        vertex_color.put(start, Color.RED);
        while (!queue.isEmpty()) {
            var vertex = queue.remove();
            for (var neighbor : graph[vertex]) {
                if (vertex_color.get(neighbor) == Color.GRAY) {
                    queue.add(neighbor);
                }
                if (vertex_color.get(neighbor) == vertex_color.get(vertex)) {
                    return false;
                } else {
                    vertex_color.put(neighbor,
                            (vertex_color.get(vertex) == Color.RED)
                                    ? Color.GREEN
                                    : Color.RED);
                }
            }
        }
        return true;
    }

    /**
     * #786
     *
     * @param arr
     * @param k
     * @return
     */
    public static int[] kthSmallestPrimeFraction(int[] arr, int k) {
        int n = arr.length;
        PriorityQueue<int[]> pq = new PriorityQueue<>(
                (x, y) -> arr[x[0]] * arr[y[1]] - arr[y[0]] * arr[x[1]]);
        for (int j = 1; j < n; ++j) {
            pq.offer(new int[] { 0, j });
        }
        for (int i = 1; i < k; ++i) {
            int[] frac = pq.remove();
            int x = frac[0], y = frac[1];
            if (x + 1 < y) {
                pq.offer(new int[] { x + 1, y });
            }
        }
        return new int[] { arr[pq.peek()[0]], arr[pq.peek()[1]] };
    }

    private record DigitDp(int pos, boolean bound, boolean diff) {
    }

    /**
     * #788
     * 
     * @param n
     * @return
     */
    public static int rotatedDigits(int n) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                Arrays.fill(memo[i][j], -1);
            }
        }
        digits = new ArrayList<>();
        while (n != 0) {
            digits.add(n % 10);
            n /= 10;
        }
        Collections.reverse(digits);
        return digitDp(0, 1, 0);
    }

    private static int[][][] memo = new int[5][2][2];
    private static List<Integer> digits = null;
    private static int[] check = { 0, 0, 1, -1, -1, 1, 1, -1, 0, 1 };

    private static int digitDp(int pos, int bound, int diff) {
        if (pos == digits.size()) {
            return diff;
        }
        var m = memo[pos][bound][diff];
        if (m != -1) {
            return m;
        }

        int ans = 0;
        for (int i = 0; i <= (bound == 1 ? digits.get(pos) : 9); i++) {
            if (check[i] != -1) {
                ans += digitDp(pos + 1,
                        (bound == 1 && i == digits.get(pos)) ? 1 : 0,
                        (diff == 1 || check[i] == 1) ? 1 : 0);
            }
        }
        memo[pos][bound][diff] = ans;
        return ans;
    }

    /**
     * #789
     * 
     * @param ghosts
     * @param target
     * @return
     */
    public static boolean escapeGhosts(int[][] ghosts, int[] target) {
        int dist_player = Math.abs(target[0]) + Math.abs(target[1]);
        for (var g : ghosts) {
            int dist_ghost = Math.abs(g[0] - target[0])
                    + Math.abs(g[1] - target[1]);
            if (dist_ghost <= dist_player) {
                return false;
            }
        }
        return true;
    }

    /**
     * #790
     * 
     * @param n
     * @return
     */
    public static int numTilings(int n) {
        int[][] dp = new int[n + 1][4]; // 00, 10,01,11
        dp[0][3] = 1;
        int mod = 1_000_000_007;
        for (int i = 1; i <= n; i++) {
            dp[i][0] = dp[i - 1][3];
            dp[i][1] = (dp[i - 1][0] + dp[i - 1][2]) % mod;
            dp[i][2] = (dp[i - 1][0] + dp[i - 1][1]) % mod;
            dp[i][3] = (((dp[i - 1][3] + dp[i - 1][2]) % mod + dp[i - 1][1])
                    % mod + dp[i - 1][0]) % mod;
        }
        return dp[n][3];
    }

    /**
     * #792
     * 
     * @param s
     * @param words
     * @return
     */
    public static int numMatchingSubseq(String s, String[] words) {
        int count_alphabet = 'z' - 'a' + 1;
        int[][] transition = new int[s.length() + 1][count_alphabet];
        int[] latest_indices = new int[count_alphabet];
        char[] chrs = s.toCharArray();
        for (int i = chrs.length - 1; i >= 0; i--) {
            char c = s.charAt(i);
            transition[i + 1] = new int[count_alphabet];
            System.arraycopy(latest_indices, 0, transition[i + 1], 0,
                    count_alphabet);
            latest_indices[c - 'a'] = i + 1;
        }
        transition[0] = latest_indices;
        int ans = 0;
        for (var word : words) {
            if (is_sub_string(word, transition)) {
                ans++;
            }
        }
        return ans;
    }

    private static boolean is_sub_string(String word, int[][] transition) {
        int trainsition_idx = 0;
        for (var c : word.toCharArray()) {
            int[] t = transition[trainsition_idx];
            int next = t[c - 'a'];
            if (next == 0) {
                return false;
            }
            trainsition_idx = next;
        }
        return true;
    }
}
