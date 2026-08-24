package Leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

@SuppressWarnings({ "unused", "JavaDoc" })
public class Leetcode800 {

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
            if (geq.size() < k || val > geq.peek()) {
                geq.add(val);
            }
            if (geq.size() > k) {
                geq.poll();
            }
            return geq.peek();
        }
    }

    /**
     * #706 MyHashMap
     */
    class MyHashMap {

        static class Node {

            int key;
            int val;

            Node(int k, int v) {
                key = k;
                val = v;
            }
        }

        int size = 10000;
        List<List<Node>> map = new ArrayList<>();

        public MyHashMap() {
            for (int i = 0; i < size; i++) {
                map.add(new ArrayList<>());
            }
        }

        public void put(int key, int value) {
            var i = Integer.hashCode(key) % size;
            var l = map.get(i);
            for (var n : l) {
                if (n.key == key) {
                    n.val = value;
                    return;
                }
            }
            l.add(new Node(key, value));
        }

        public int get(int key) {
            var i = Integer.hashCode(key) % size;
            var l = map.get(i);
            for (var n : l) {
                if (n.key == key) {
                    return n.val;
                }
            }
            return -1;
        }

        public void remove(int key) {
            var i = Integer.hashCode(key) % size;
            var l = map.get(i);
            for (var n : l) {
                if (n.key == key) {
                    l.remove(n);
                    return;
                }
            }
        }
    }

    /**
     * #707 MyLinkedList
     */
    class MyLinkedList {

        static class Node {

            Node prev;
            Node next;
            int val;
        }

        Node head = new Node();
        Node tail = new Node();

        public MyLinkedList() {
            head.next = tail;
            tail.prev = head;

        }

        public int get(int index) {
            int i = 0;
            var ptr = head.next;
            for (; index > 0 && ptr != tail; index--) {
                ptr = ptr.next;
            }
            if (index == 0 && ptr != tail) {
                return ptr.val;
            }
            return -1;
        }

        public void addAtHead(int val) {
            var n = new Node();
            n.val = val;
            var next = head.next;
            head.next = n;
            n.prev = head;
            n.next = next;
            next.prev = n;
        }

        public void addAtTail(int val) {
            var prev = tail.prev;
            var n = new Node();
            n.val = val;
            prev.next = n;
            n.prev = prev;
            n.next = tail;
            tail.prev = n;
        }

        public void addAtIndex(int index, int val) {
            int i = 0;
            var ptr = head.next;
            for (; index > 0 && ptr != tail; index--) {
                ptr = ptr.next;
            }
            if (index == 0) {
                var n = new Node();
                n.val = val;
                var prev = ptr.prev;
                prev.next = n;
                n.prev = prev;
                n.next = ptr;
                ptr.prev = n;
            }
        }

        public void deleteAtIndex(int index) {
            int i = 0;
            var ptr = head.next;
            for (; index > 0 && ptr != tail; index--) {
                ptr = ptr.next;
            }
            if (index == 0 && ptr != tail) {
                var prev = ptr.prev;
                var next = ptr.next;
                prev.next = next;
                next.prev = prev;
            }
        }
    }

    /**
     * #708
     * 
     * @param head
     * @param insertVal
     * @return
     */

    static class Next708 {
        static class Node {
            public int val;
            public Node next;

            public Node() {
            }

            public Node(int _val) {
                val = _val;
            }

            public Node(int _val, Node _next) {
                val = _val;
                next = _next;
            }
        };

        public Node insert(Node head, int insertVal) {
            var n = new Node(insertVal);
            if (head == null) {
                n.next = n;
                return n;
            }
            if (head.next == head) {
                head.next = n;
                n.next = head;
                return head;
            }
            var ptr = head;
            while (true) {
                if (ptr.val < ptr.next.val) {
                    if (insertVal >= ptr.val && insertVal <= ptr.next.val) {
                        var next = ptr.next;
                        ptr.next = n;
                        n.next = next;
                        break;
                    }
                } else if (ptr.val > ptr.next.val) {
                    if (insertVal >= ptr.val || insertVal <= ptr.next.val) {
                        var next = ptr.next;
                        ptr.next = n;
                        n.next = next;
                        break;
                    }
                } else {
                    if (ptr.next == head) {
                        var next = ptr.next;
                        ptr.next = n;
                        n.next = next;
                        break;
                    }
                }
                ptr = ptr.next;
            }
            return head;
        }
    }

    static class Nest710 {
        class Solution {
            Map<Integer, Integer> b2w;
            int size;
            Random rand = new Random();

            public Solution(int n, int[] blacklist) {
                size = n - blacklist.length;
                int w = size;
                Arrays.sort(blacklist);
                int bw2Size = 0;
                Set<Integer> black = new HashSet<>(blacklist.length);
                for (var b : blacklist) {
                    black.add(b);
                    if (b < size)
                        bw2Size++;
                }
                b2w = new HashMap<>(bw2Size);
                for (var b : blacklist) {
                    if (b >= size)
                        break;
                    while (black.contains(w)) {
                        w++;
                    }
                    b2w.put(b, w++);

                }
            }

            public int pick() {
                var idx = rand.nextInt(size);
                return b2w.getOrDefault(idx, idx);
            }
        }

        class Solution2 {
            TreeMap<Integer, Integer> list = new TreeMap<>();
            int size;
            Random rand = new Random();

            public Solution2(int n, int[] blacklist) {
                Arrays.sort(blacklist);
                int l = -1;
                for (var b : blacklist) {
                    if (b - (l + 1) >= 1) {
                        list.put(size, l + 1);
                        size += b - (l + 1);
                    }
                    l = b;
                }
                if (n - (l + 1) >= 1) {
                    list.put(size, l + 1);
                    size += n - (l + 1);
                }
            }

            public int pick() {
                var idx = rand.nextInt(size);
                var floor = list.floorKey(idx);
                return list.get(floor) + (idx - floor);
            }
        }
    }

    /**
     * #713
     *
     * @param nums
     * @param k
     * @return
     */
    public int numSubarrayProductLessThanK(int[] nums, int k) {
        if (k == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0] == k ? 1 : 0;
        }
        int res = 0;
        long v = nums[0];
        if (v == k) {
            res++;
        }
        int l = 0;
        for (int i = 1; i < nums.length; i++) {
            while (v * nums[i] >= k && l < i) {
                v /= nums[l++];
            }
            if (v * nums[i] < k) {
                res += i + 1 - l;
                v *= nums[i];
            } else {
                l = i + 1;
            }
        }
        return res;
    }

    /**
     * #714
     *
     * @param prices
     * @param fee
     * @return
     */
    public int maxProfit(int[] prices, int fee) {
        int[] buy = new int[2];
        int[] sell = new int[2];
        buy[0] = -prices[0] - fee;
        for (int i = 1; i < prices.length; i++) {
            buy[1] = Math.max(buy[0], sell[0] - fee - prices[i]);
            sell[1] = Math.max(sell[0], buy[0] - fee + prices[i]);
            buy[0] = buy[1];
            sell[0] = sell[1];
        }
        return sell[0];
    }

    static class Nest716 {
        /**
         * #716 MaxStack
         */
        class MaxStack {

            static record IntId(int val, int id) {
            }

            int id = 0;
            Stack<IntId> stack = new Stack<>();
            Set<Integer> deleteIds = new HashSet<>();
            TreeMap<Integer, Stack<Integer>> numIds = new TreeMap<>();

            public MaxStack() {

            }

            public void push(int x) {
                stack.add(new IntId(x, id));
                numIds.computeIfAbsent(x, k -> new Stack<>()).add(id);
                id++;
            }

            int removeNumId(int n) {
                var s = numIds.get(n);
                var id = s.pop();
                if (s.isEmpty())
                    numIds.remove(n);
                return id;
            }

            public int pop() {
                var n = stack.pop();
                removeNumId(n.val);
                clearStackTop();
                return n.val;
            }

            void clearStackTop() {
                while (!stack.isEmpty() && !deleteIds.isEmpty()
                        && deleteIds.contains(stack.peek().id)) {
                    deleteIds.remove(stack.pop().id);
                }
            }

            public int top() {
                return stack.peek().val;
            }

            public int peekMax() {
                return numIds.lastKey();
            }

            public int popMax() {
                var n = peekMax();
                deleteIds.add(removeNumId(n));
                clearStackTop();
                return n;
            }
        }
    }

    /**
     * #723
     * 
     * @param board
     * @return
     */
    public int[][] candyCrush(int[][] board) {
        var rm = mark(board);
        while (!rm.isEmpty()) {
            for (var e : rm.entrySet()) {
                var j = e.getKey();
                List<Integer> col = new ArrayList<>();
                for (int i = 0; i < board.length; i++) {
                    if (!e.getValue().contains(i))
                        col.add(board[i][j]);
                }
                for (int i = 0, k = 0; i < board.length; i++) {
                    if (i + col.size() >= board.length) {
                        board[i][j] = col.get(k++);
                    } else
                        board[i][j] = 0;
                }
            }
            rm = mark(board);
            int t = rm.size();
        }
        return board;
    }

    Map<Integer, Set<Integer>> mark(int[][] board) {
        int m = board.length, n = board[0].length;
        Map<Integer, Set<Integer>> remove = new HashMap<>(m);
        for (int i = 0; i < m; i++) {
            int l = 0;
            for (int j = 0; j < n; j++) {
                if (board[i][l] != board[i][j]) {
                    if (j - l >= 3 && board[i][l] != 0) {
                        while (l < j)
                            remove.computeIfAbsent(l++, k -> new HashSet<>())
                                    .add(i);
                    }
                    l = j;
                }
            }
            if (n - l >= 3 && board[i][l] != 0) {
                while (l < n)
                    remove.computeIfAbsent(l++, k -> new HashSet<>()).add(i);
            }
        }
        for (int j = 0; j < n; j++) {
            int l = 0;
            for (int i = 0; i < m; i++) {
                if (board[l][j] != board[i][j]) {
                    if (i - l >= 3 && board[l][j] != 0) {
                        while (l < i)
                            remove.computeIfAbsent(j, k -> new HashSet<>())
                                    .add(l++);
                    }
                    l = i;
                }
            }
            if (m - l >= 3 && board[l][j] != 0) {
                while (l < m)
                    remove.computeIfAbsent(j, k -> new HashSet<>()).add(l++);
            }
        }
        return remove;
    }

    /**
     * #724
     *
     * @param nums
     * @return
     */
    public int pivotIndex(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            nums[i] += nums[i - 1];
        }
        for (int i = 0; i < nums.length; i++) {
            var l = i - 1 >= 0 ? nums[i - 1] : 0;
            var r = nums[nums.length - 1] - nums[i];
            if (l == r) {
                return i;
            }
        }

        return -1;
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
        if (m1.isEmpty()) {
            return m2;
        } else if (m2.isEmpty()) {
            return m1;
        } else {
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
     * #729 MyCalendar
     */
    class MyCalendar {

        static class SegTree {

            int rangeLeft;
            int rangeRight;
            SegTree left;
            SegTree right;
            boolean bookLeft;
            boolean bookRight;

            SegTree(int s, int e) {
                rangeLeft = s;
                rangeRight = e;
            }

            void insert(int s, int e) {
                if (s == rangeLeft && e == rangeRight) {
                    bookLeft = bookRight = true;
                    return;
                }
                int mid = rangeLeft + (rangeRight - rangeLeft) / 2;

                if (e >= mid + 1) {
                    if (right == null) {
                        right = new SegTree(mid + 1, rangeRight);
                    }
                    right.insert(Math.max(s, mid + 1), e);
                    bookRight = true;
                }
                if (s <= mid) {
                    if (left == null) {
                        left = new SegTree(rangeLeft, mid);
                    }
                    left.insert(s, Math.min(e, mid));
                    bookLeft = true;
                }
            }

            boolean overlap(int s, int e) {
                if (s == rangeLeft && e == rangeRight) {
                    return bookLeft || bookRight;
                }
                int mid = rangeLeft + (rangeRight - rangeLeft) / 2;
                boolean inLeft = false, inRight = false;
                if (e >= mid + 1) {
                    inRight = right != null
                            ? right.overlap(Math.max(s, mid + 1), e)
                            : bookRight;
                }
                if (s <= mid && !inRight) {
                    inLeft = left != null ? left.overlap(s, Math.min(e, mid))
                            : bookLeft;
                }
                return inLeft || inRight;
            }
        }

        SegTree tree = new SegTree(0, 1_000_000_000 - 1);

        public MyCalendar() {

        }

        public boolean book(int startTime, int endTime) {
            if (tree.overlap(startTime, endTime - 1)) {
                return false;
            }
            tree.insert(startTime, endTime - 1);
            return true;
        }
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
            if (mid >= ql) {
                update(cur.left, diff, l, mid, ql, qr);
            }
            if (mid + 1 <= qr) {
                update(cur.right, diff, mid + 1, r, ql, qr);
            }

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
            if (mid >= ql) {
                update(cur.left, diff, l, mid, ql, qr);
            }
            if (mid + 1 <= qr) {
                update(cur.right, diff, mid + 1, r, ql, qr);
            }

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
                if (!queue.isEmpty() && queue.getLast() == -n) {
                    queue.pollLast();
                } else if (queue.isEmpty() || queue.getLast() < 0) {
                    queue.add(n);
                }
            } else {
                queue.add(n);
            }
        }
        return queue.stream().mapToInt(a -> a).toArray();
    }

    /**
     * #736
     *
     * @param expression
     * @return
     */
    public int evaluate(String expression) {
        return eval(lexer(expression), null);
    }

    int eval(Queue<String> exp, SymTable symTable) {
        boolean scope = false;
        if (exp.peek().equals("(")) {
            exp.poll();
            scope = true;
            symTable = new SymTable(symTable);
        }

        if (isVar(exp.peek())) {
            int res = symTable.getVar(exp.poll());
            if (scope) {
                exp.poll();
            }
            return res;

        } else if (isNum(exp.peek())) {
            int res = Integer.valueOf(exp.poll());
            if (scope) {
                exp.poll();
            }
            return res;
        }
        var cmd = exp.poll();
        if (cmd.equals("add") || cmd.equals("mult")) {
            int a = eval(exp, symTable);
            int b = eval(exp, symTable);
            exp.poll(); // ')'
            return cmd.equals("add") ? a + b : a * b;
        } else {
            // let
            while (isVar(exp.peek())) {
                var v = exp.poll();
                if (exp.peek().equals(")")) {
                    exp.poll();
                    return symTable.getVar(v);
                }
                int val = eval(exp, symTable);
                symTable.put(v, val);
            }
            int res = eval(exp, symTable);
            exp.poll(); // ')'
            return res;
        }
    }

    boolean isVar(String s) {
        return Character.isAlphabetic(s.charAt(0)) && !s.equals("add")
                && !s.equals("let") && !s.equals("mult");
    }

    boolean isNum(String s) {
        return s.charAt(0) == '-' || Character.isDigit(s.charAt(0));
    }

    Queue<String> lexer(String expression) {
        int i = 0;
        Queue<String> res = new ArrayDeque<>();
        while (i < expression.length()) {
            var c = expression.charAt(i);
            if (c == ' ') {
                i++;
            } else if (c == '(' || c == ')') {
                res.add(String.valueOf(c));
                i++;
            } else {
                var s = i;
                while (i < expression.length() && expression.charAt(i) != ' '
                        && expression.charAt(i) != '('
                        && expression.charAt(i) != ')') {
                    i++;
                }
                res.add(expression.substring(s, i));
            }
        }
        return res;
    }

    static class SymTable {

        SymTable prev = null;
        Map<String, Integer> table = new HashMap<>();

        SymTable(SymTable p) {
            prev = p;
        }

        int getVar(String s) {
            if (table.containsKey(s)) {
                return table.get(s);
            }
            if (prev != null) {
                return prev.getVar(s);
            }
            throw new RuntimeException();
        }

        void put(String var, int val) {
            table.put(var, val);
        }
    }

    /**
     * #737
     *
     * @param sentence1
     * @param sentence2
     * @param similarPairs
     * @return
     */
    public boolean areSentencesSimilarTwo(String[] sentence1,
            String[] sentence2, List<List<String>> similarPairs) {
        if (sentence1.length != sentence2.length) {
            return false;
        }
        int id = 0;
        Map<String, Integer> str2id = new HashMap<>();
        for (var p : similarPairs) {
            for (var s : p) {
                if (!str2id.containsKey(s)) {
                    str2id.put(s, id++);
                }
            }
        }
        Disjointset set = new Disjointset(str2id.size());
        for (var p : similarPairs) {
            set.union(str2id.get(p.get(0)), str2id.get(p.get(1)));
        }
        for (int i = 0; i < sentence1.length; i++) {
            var s1 = sentence1[i];
            var s2 = sentence2[i];
            var id1 = str2id.getOrDefault(s1, -1);
            var id2 = str2id.getOrDefault(s2, -1);
            if (!s1.equals(s2) && (id1 < 0 || id2 < 0
                    || set.parent(id1) != set.parent(id2))) {
                return false;
            }
        }
        return true;
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
     * #740
     * 
     * @param nums
     * @return
     */
    public int deleteAndEarn(int[] nums) {
        Map<Integer, Integer> count = new HashMap<>();
        for (var n : nums)
            count.put(n, 1 + count.getOrDefault(n, 0));

        int[] dp = new int[count.size()]; // 0/1 no/yes
        var sortedCount = new TreeMap<Integer, Integer>();
        for (var e : count.entrySet()) {
            sortedCount.put(e.getKey(), e.getValue());
        }
        int i = 0;
        for (var e : sortedCount.entrySet()) {
            var n = e.getKey();
            var c = e.getValue();
            var prev = count.getOrDefault(n - 1, 0);
            if (prev == 0) {
                dp[i] = (i - 1 >= 0 ? dp[i - 1] : 0) + c * n;
            } else {
                dp[i] = (i - 2 >= 0 ? dp[i - 2] : 0) + c * n;
                dp[i] = Math.max(dp[i], dp[i - 1]);
            }
            i++;
        }
        return dp[count.size() - 1];
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
        if (n <= 0) {
            return;
        }
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
            if (i != 0) {
                sb.append(".");
            }
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
     * #752
     */
    public int openLock(String[] deadends, String target) {
        Set<String> locks = new HashSet<>();
        locks.addAll(Arrays.asList(deadends));
        if (locks.contains("0000"))
            return -1;
        if (target.equals("0000"))
            return 0;

        Queue<String> queue = new ArrayDeque<>();
        Map<String, Integer> visited = new HashMap<>();
        visited.put("0000", 0);
        queue.add("0000");
        int[] dir = new int[] { -1, 1 };
        while (!queue.isEmpty()) {
            var str = queue.poll();
            var num = str.toCharArray();
            var step = visited.get(str);
            for (int i = 0; i < 4; i++) {
                var save = num[i];
                for (var d : dir) {
                    var n = save - '0';
                    n += d + 10;
                    n %= 10;
                    num[i] = (char) ('0' + n);
                    var s = new String(num);
                    if (s.equals(target))
                        return step + 1;
                    if (!locks.contains(s) && !visited.containsKey(s)) {
                        queue.add(s);
                        visited.put(s, step + 1);
                    }

                }
                num[i] = save;
            }
        }
        return -1;
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
     * #759
     *
     * @param schedule
     * @return
     */
    public List<Interval> employeeFreeTime(List<List<Interval>> schedule) {
        List<Interval> res = new ArrayList<>();
        int maxWorkTime = Integer.MIN_VALUE;
        Queue<Interval> queue = new PriorityQueue<>(
                (a, b) -> Integer.compare(a.start, b.start));
        for (var s : schedule) {
            for (var i : s) {
                queue.add(i);
            }
        }
        while (!queue.isEmpty()) {
            var it = queue.poll();
            if (it.start > maxWorkTime) {
                if (maxWorkTime != Integer.MIN_VALUE) {
                    res.add(new Interval(maxWorkTime, it.start));
                }
            }
            maxWorkTime = Math.max(maxWorkTime, it.end);
        }
        return res;
    }

    class Interval {

        public int start;
        public int end;

        public Interval() {
        }

        public Interval(int _start, int _end) {
            start = _start;
            end = _end;
        }
    };

    /**
     * #761
     *
     * @param s
     * @return
     */
    public String makeLargestSpecial(String s) {
        return makeLargestSpecial(s, 0, s.length() - 1);
    }

    String makeLargestSpecial(String s, int l, int r) {
        if (l >= r) {
            return "";
        }
        List<String> sub = new ArrayList<>();
        int diff = 0;
        int start = l;
        for (int i = l; i <= r; i++) {
            var c = s.charAt(i);
            if (c == '1') {
                diff++;
            } else {
                diff--;
            }
            if (diff == 0) {
                sub.add("(" + makeLargestSpecial(s, start + 1, i - 1) + ")");
                start = i + 1;
            }
        }
        sub.sort((a, b) -> b.compareTo(a));
        StringBuilder sb = new StringBuilder();
        for (var ss : sub) {
            sb.append(ss);
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

    /**
     * #765
     *
     * @param row
     * @return
     */
    public static int minSwapsCouples(int[] row) {
        int N = row.length / 2;
        Disjointset unions = new Disjointset(N);

        for (int i = 0; i < row.length; i += 2) {
            int couple1 = row[i] / 2;
            int couple2 = row[i + 1] / 2;
            unions.union(couple1, couple2);
        }
        int count = 0;
        for (int d = 0; d < unions.parent.length; d++) {
            if (d == unions.parent(d)) {
                count++;
            }
        }
        return N - count;
    }

    /**
     * #767
     */
    public String reorganizeString(String s) {
        StringBuilder res = new StringBuilder();
        int[] count = new int['z' - 'a' + 1];
        for (var c : s.toCharArray()) {
            count[c - 'a']++;
        }
        Queue<Character> queue = new PriorityQueue<>(
                (a, b) -> Integer.compare(count[b - 'a'], count[a - 'a']));
        Queue<CharPos> stash = new ArrayDeque<>();
        for (int i = 0; i < 'z' - 'a' + 1; i++) {
            if (count[i] != 0) {
                queue.add((char) ('a' + i));
            }
        }
        for (int i = 0; i < s.length(); i++) {
            while (!stash.isEmpty() && i > stash.peek().pos + 1) {
                queue.add(stash.poll().c);
            }
            if (queue.isEmpty()) {
                return "";
            }
            var c = queue.poll();
            res.append(c);
            count[c - 'a']--;
            if (count[c - 'a'] != 0) {
                stash.add(new CharPos(c, i));
            }
        }
        return res.toString();
    }

    static class CharPos implements Comparable<CharPos> {

        char c;
        int pos;

        public CharPos(char c, int count) {
            this.c = c;
            this.pos = count;
        }

        @Override
        public int compareTo(CharPos o) {
            return Integer.compare(o.pos, pos);
        }

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
     * #769
     *
     * @param arr
     * @return
     */
    public int maxChunksToSorted2(int[] arr) {
        int res = 1;
        int[] maxLeft = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            if (i - 1 < 0 || arr[i] >= maxLeft[i - 1]) {
                maxLeft[i] = arr[i];
            } else {
                maxLeft[i] = maxLeft[i - 1];
            }
        }
        int min = Integer.MAX_VALUE;
        for (int j = arr.length - 1; j >= 1; j--) {
            min = Math.min(min, arr[j]);
            if (min >= maxLeft[j - 1]) {
                res++;
            }
        }
        return res;
    }

    /**
     * #772
     *
     * @param s
     * @return
     */
    public int calculate(String s) {
        var exp = lexerCalc(s);
        exp.addFirst("(");
        exp.addLast(")");
        return calc(exp);
    }

    int calc(Deque<String> exp) {
        Deque<String> mediumRes = new ArrayDeque<>();
        while (!exp.isEmpty()) {
            var sym = exp.pollFirst();
            if (isOp(sym) || isNumCalc(sym)) {
                mediumRes.add(sym);
            } else if (sym.equals("(")) {
                exp.addFirst(String.valueOf(calc(exp)));
            } else {
                break;
            }
        }
        computeMediumRes(mediumRes);
        return Integer.valueOf(mediumRes.peek());
    }

    int calcOp(String sa, String op, String sb) {
        int res = 0;
        var b = Integer.valueOf(sb);
        var a = Integer.valueOf(sa);
        if (op.equals("+")) {
            res = a + b;
        } else if (op.equals("-")) {
            res = a - b;
        } else if (op.equals("*")) {
            res = a * b;
        } else if (op.equals("/")) {
            res = a / b;
        }
        return res;
    }

    void computeMediumRes(Deque<String> mediumRes) {
        Deque<String> sum = new ArrayDeque<>();
        while (mediumRes.size() >= 3) {
            var a = mediumRes.pollFirst();
            var op = mediumRes.pollFirst();
            if (op.equals("*") || op.equals("/")) {
                mediumRes.addFirst(
                        String.valueOf(calcOp(a, op, mediumRes.pollFirst())));
            } else {
                sum.addLast(a);
                sum.addLast(op);
            }
        }
        while (!mediumRes.isEmpty()) {
            sum.addLast(mediumRes.pollFirst());
        }
        while (sum.size() >= 3) {
            var a = sum.pollFirst();
            var op = sum.pollFirst();
            var b = sum.pollFirst();
            sum.addFirst(String.valueOf(calcOp(a, op, b)));
        }
        mediumRes.add(sum.poll());
    }

    boolean isNumCalc(String s) {
        return Character.isDigit(s.charAt(0));
    }

    boolean isOp(String s) {
        return s.charAt(0) == '+' || s.charAt(0) == '-' || s.charAt(0) == '*'
                || s.charAt(0) == '/';
    }

    Deque<String> lexerCalc(String s) {
        Deque<String> res = new ArrayDeque<>();
        int i = 0;
        var arr = s.toCharArray();
        while (i < arr.length) {
            if (s.charAt(i) == '(' || s.charAt(i) == ')' || s.charAt(i) == '+'
                    || s.charAt(i) == '-' || s.charAt(i) == '*'
                    || s.charAt(i) == '/') {
                res.add(String.valueOf(arr[i++]));
            } else {
                int j = i;
                while (j < arr.length && Character.isDigit(s.charAt(j))) {
                    j++;
                }
                res.add(s.substring(i, j));
                i = j;
            }
        }
        return res;
    }

    /**
     * #773
     *
     * @param board
     * @return
     */
    public int slidingPuzzle(int[][] board) {
        int[] target = new int[6];
        System.arraycopy(board[0], 0, target, 0, 3);
        System.arraycopy(board[1], 0, target, 3, 3);
        Set<Board2x3> visited = new HashSet<>();
        Board2x3 init = new Board2x3(new int[] { 1, 2, 3, 4, 5, 0 }, 1, 2, 0);
        if (Arrays.equals(target, init.board)) {
            return 0;
        }
        visited.add(init);
        Queue<Board2x3> queue = new ArrayDeque<>();
        queue.add(init);
        int[] dx = new int[] { -1, 1, 0, 0 };
        int[] dy = new int[] { 0, 0, -1, 1 };
        while (!queue.isEmpty()) {
            var b = queue.poll();
            var idx0 = b.i0 * 3 + b.j0;
            for (int t = 0; t < 4; t++) {
                var i = b.i0 + dx[t];
                var j = b.j0 + dy[t];
                if (i >= 0 && i <= 1 && j >= 0 && j <= 2) {
                    var nb = new int[6];
                    System.arraycopy(b.board, 0, nb, 0, 6);
                    var idx = i * 3 + j;
                    var v = nb[idx];
                    nb[idx] = nb[idx0];
                    nb[idx0] = v;
                    var nextBoard = new Board2x3(nb, i, j, b.dist + 1);
                    if (Arrays.equals(target, nb)) {
                        return b.dist + 1;
                    }
                    if (!visited.contains(nextBoard)) {
                        visited.add(nextBoard);
                        queue.add(nextBoard);
                    }
                }

            }

        }
        return -1;
    }

    static record Board2x3(int[] board, int i0, int j0, int dist) {

        @Override
        public final boolean equals(Object arg0) {
            if (arg0 instanceof Board2x3 b) {
                return Arrays.equals(board, b.board);
            }
            return false;
        }

        @Override
        public final int hashCode() {
            return Arrays.hashCode(board);
        }
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
        } else {
            return false;
        }
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
     * #784
     * 
     * @param s
     * @return
     */
    public List<String> letterCasePermutation(String s) {
        List<String> res = new ArrayList<>();
        permute(0, s.toCharArray(), res);
        return res;
    }

    void permute(int i, char[] s, List<String> res) {
        if (i >= s.length) {
            res.add(new String(s));
            return;
        }
        permute(i + 1, s, res);
        if (Character.isAlphabetic(s[i])) {
            if (Character.isUpperCase(s[i]))
                s[i] = Character.toLowerCase(s[i]);
            else
                s[i] = Character.toUpperCase(s[i]);
            permute(i + 1, s, res);
            if (Character.isUpperCase(s[i]))
                s[i] = Character.toLowerCase(s[i]);
            else
                s[i] = Character.toUpperCase(s[i]);
        }
    }

    /**
     * #785 <br>
     * bipartite graph: vertex and its neighbors has different color
     *
     * @param graph graph
     * @return graph is bipartite
     */
    public static boolean isBipartite(int[][] graph) {
        int n = graph.length;
        Disjointset set = new Disjointset(n);
        for (var nbs : graph) {
            for (int i = 0; i < nbs.length - 1; i++) {
                set.union(nbs[i], nbs[i + 1]);
            }
        }

        for (int i = 0; i < n; i++) {
            var nbs = graph[i];
            for (var nb : nbs) {
                if (set.parent(i) == set.parent(nb)) {
                    return false;
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

    /**
     * #799
     * 
     * @param poured
     * @param query_row
     * @param query_glass
     * @return
     */
    public double champagneTower(int poured, int query_row, int query_glass) {
        double[][] rows = new double[query_row + 1][];
        for (int i = 1; i <= rows.length; i++) {
            rows[i - 1] = new double[i];
        }
        rows[0][0] = poured;
        for (int i = 0; i < rows.length; i++) {
            for (int j = 0; j < rows[i].length; j++) {
                if (rows[i][j] > 1) {
                    var overflow = rows[i][j] - 1;
                    rows[i][j] = 1;
                    if (i < query_row) {
                        rows[i + 1][j] += overflow / 2;
                        rows[i + 1][j + 1] += overflow / 2;
                    }
                }
            }
        }
        return rows[query_row][query_glass];
    }
}
