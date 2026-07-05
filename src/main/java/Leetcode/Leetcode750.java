package Leetcode;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SuppressWarnings({ "JavaDoc" })
public class Leetcode750 {

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
