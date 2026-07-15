package Leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.BitSet;
import java.util.Deque;
import java.util.NoSuchElementException;

@SuppressWarnings({ "JavaDoc" })
class Leetcode150 {
    /**
     * #105
     *
     * <br>
     * 前序遍历 preorder = [3,9,20,15,7] <br>
     * 中序遍历 inorder = [9,3,15,20,7] <br>
     * result:
     * 
     * <pre>
     *    3
     *   / \
     *  9  20
     *    /  \
     *   15   7
     * </pre>
     *
     * @param preorder preorder int array
     * @param inorder  inorder int array
     * @return origin tree
     */
    public static TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder.length == 0) {
            return null;
        }
        Map<Integer, Integer> m = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            m.put(inorder[i], i);
        }
        return recursiveBuildTree(preorder, 0, preorder.length, 0, m);
    }

    private static TreeNode recursiveBuildTree(int[] p_order, int p_start,
            int p_end, int i_start, Map<Integer, Integer> m) {
        if (p_end - p_start == 1) {
            return new TreeNode(p_order[p_start]);
        } else if (p_end - p_start == 0) {
            return null;
        } else {
            TreeNode root = new TreeNode(p_order[p_start]);
            int i_mid = m.get(root.val);
            int l_len = i_mid - i_start;
            root.left = recursiveBuildTree(p_order, p_start + 1,
                    p_start + 1 + l_len, i_start, m);
            root.right = recursiveBuildTree(p_order, p_start + 1 + l_len, p_end,
                    i_start + l_len + 1, m);
            return root;
        }
    }

    /**
     * #107 <br>
     * 层次遍历<br>
     * 
     * <pre>
     *  input:[3,9,20,null,null,15,7]
     *     3
     *    / \
     *   9  20
     *     /  \
     *    15   7
     * </pre>
     * 
     * <pre>
     * return:
     * [
     *   [15,7],
     *   [9,20],
     *   [3]
     * ]
     * </pre>
     *
     * @param root root
     * @return down to up iteration
     */
    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> t = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                var n = queue.poll();
                if (n != null) {
                    t.add(n.val);
                    if (n.left != null) {
                        queue.add(n.left);
                    }
                    if (n.right != null) {
                        queue.add(n.right);
                    }
                }
            }
            if (t.size() != 0) {
                res.add(t);
            }
        }
        Collections.reverse(res);
        return res;
    }

    /**
     * #111 <br/>
     * 给定一个二叉树，找出其最小深度。
     *
     * @param root 二叉树
     * @return 最小深度
     */
    public static int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = minDepth(root.left);
        int right = minDepth(root.right);
        int depth = 1;
        if (left == 0) {
            depth += right;
        } else {
            if (right == 0) {
                depth += left;
            } else {
                depth += Math.min(left, right);
            }
        }

        return depth;
    }

    static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {
        }

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
    }

    /**
     * 
     * 115
     * 
     * @param s
     * @param t
     * @return
     */
    public int numDistinct(String s, String t) {
        if (s.length() < t.length())
            return 0;
        int[] dp = new int[t.length() + 1];
        dp[0] = 1;
        for (int i = 1; i < s.length() + 1; i++) {
            for (int j = t.length(); j >= 1; j--) {
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[j] += dp[j - 1];
                } else {
                    dp[j] = dp[j];
                }
            }
        }
        return dp[t.length()];
    }

    /**
     * #116
     *
     * @param root
     * @return
     */
    public static Node connect(Node root) {
        if (root == null)
            return null;
        // noinspection FieldMayBeFinal
        class NodeList {
            Node head;
            Node tail;

            NodeList(Node h, Node t) {
                head = h;
                tail = t;
            }
        }
        var recurConnectFunc = new Object() {
            List<NodeList> apply(Node n) {
                if (n == null)
                    return new ArrayList<>(0);
                var left = apply(n.left);
                var right = apply(n.right);
                for (int i = 0; i < left.size(); i++) {
                    var nL = left.get(i);
                    var nR = right.get(i);
                    nL.tail.next = nR.head;
                    nL.tail = nR.tail;
                }
                left.add(new NodeList(n, n));
                return left;
            }
        };
        recurConnectFunc.apply(root);

        return root;
    }

    /**
     * #128
     *
     * @param nums
     * @return
     */
    public static int longestConsecutive(int[] nums) {
        Set<Integer> sets = new HashSet<>(nums.length * 2);
        for (var i : nums) {
            sets.add(i);
        }
        int ans = 0;
        for (var n : nums) {
            if (!sets.contains(n - 1)) {
                n++;
                int count = 1;
                while (sets.contains(n)) {
                    count++;
                    n++;
                }
                ans = Math.max(ans, count);
            }
        }
        return ans;
    }

    /**
     * #129 <br/>
     * 
     * <pre>
     *  输入: [1,2,3]
     *     1
     *    / \
     *   2   3
     * 输出: 25
     * 解释:
     * 从根到叶子节点路径 1->2 代表数字 12.
     * 从根到叶子节点路径 1->3 代表数字 13.
     * 因此，数字总和 = 12 + 13 = 25.
     * </pre>
     *
     * @param root tree
     * @return sum
     */
    public static int sumNumbers(TreeNode root) {
        if (root == null) {
            return 0;
        }
        List<Integer> res = new ArrayList<>();
        res.add(0);
        solveSumNumbers(root, 0, res);
        return res.get(0);
    }

    private static void solveSumNumbers(TreeNode node, int num,
            List<Integer> sum) {
        num = num * 10 + node.val;
        if (node.left == null && node.right == null) {
            sum.set(0, sum.get(0) + num);
        } else if (node.right != null && node.left == null) {
            solveSumNumbers(node.right, num, sum);
        } else if (node.right == null) {
            solveSumNumbers(node.left, num, sum);
        } else {
            solveSumNumbers(node.right, num, sum);
            solveSumNumbers(node.left, num, sum);
        }
    }

    /**
     * #130
     * 
     * @param board
     */
    public void solve(char[][] board) {
        int m = board.length, n = board[0].length;
        Queue<Point> queue = new ArrayDeque<>();

        for (int j = 0; j < n; j++) {
            if (board[0][j] == 'O') {
                queue.add(new Point(0, j));
            }
            if (board[m - 1][j] == 'O') {
                queue.add(new Point(m - 1, j));
            }
        }
        for (int i = 0; i < m; i++) {
            if (board[i][0] == 'O') {
                queue.add(new Point(i, 0));
            }
            if (board[i][n - 1] == 'O') {
                queue.add(new Point(i, n - 1));
            }
        }
        int[] dx = { 0, 0, 1, -1 };
        int[] dy = { 1, -1, 0, 0 };
        while (!queue.isEmpty()) {
            var point = queue.poll();
            var p = point.x;
            var q = point.y;
            board[p][q] = 'A';
            for (int i = 0; i < 4; i++) {
                int px = p + dx[i], py = q + dy[i];
                if (px < 0 || px >= m || py < 0 || py >= n
                        || board[px][py] != 'O')
                    continue;
                queue.add(new Point(px, py));
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'A')
                    board[i][j] = 'O';
                else if (board[i][j] == 'O')
                    board[i][j] = 'X';
            }
        }
    }

    static record Point(int x, int y) {

    }

    /**
     * #131
     *
     * @param s
     * @return
     */
    public static List<List<String>> partition(String s) {
        boolean[][] pLenMinus1_startAt = new boolean[s.length()][];
        for (int i = 0; i < pLenMinus1_startAt.length; i++) {
            pLenMinus1_startAt[i] = new boolean[s.length() - i];
        }
        if (s.length() >= 1) {
            var t = pLenMinus1_startAt[0];
            Arrays.fill(t, true);
        }
        if (s.length() >= 2) {
            var t = pLenMinus1_startAt[1];
            for (int i = 0; i < t.length; i++) {
                if (s.charAt(i) == s.charAt(i + 1)) {
                    t[i] = true;
                }
            }
        }
        for (int i = 2; i < pLenMinus1_startAt.length; i++) {
            var current = pLenMinus1_startAt[i];
            var prev = pLenMinus1_startAt[i - 2];
            for (int j = 0; j < current.length; j++) {
                if (prev[j + 1] && s.charAt(j) == s.charAt(j + i)) {
                    current[j] = true;
                }
            }
        }
        List<List<String>> ans = new ArrayList<>();
        Map<Integer, List<Integer>> pLenOfPos = new HashMap<>(s.length());
        for (int l = 1; l < s.length(); l++) {
            for (int i = 0; i < pLenMinus1_startAt[l].length; i++) {
                if (pLenMinus1_startAt[l][i]) {
                    var t = pLenOfPos.computeIfAbsent(i,
                            o -> new ArrayList<>());
                    t.add(l + 1);
                }
            }
        }
        var recurFunc = new Object() {
            void apply(int idx, List<String> a) {
                if (idx == s.length()) {
                    ans.add(a);
                    return;
                }
                var pLen = pLenOfPos.get(idx);
                if (pLen != null) {
                    for (var l : pLen) {
                        List<String> t = new ArrayList<>(a);
                        t.add(s.substring(idx, idx + l));
                        apply(idx + l, t);
                    }
                }
                a.add(s.substring(idx, idx + 1));
                apply(idx + 1, a);
            }
        };
        recurFunc.apply(0, new ArrayList<>());

        return ans;
    }

    /**
     * #133
     *
     * @param node
     * @return
     */
    public Leetcode.Node cloneGraph(Leetcode.Node node) {
        if (node == null) {
            return null;
        }

        Leetcode.Node graph = getMapped(node);
        Queue<Leetcode.Node> nodeQueue = new ArrayDeque<>(),
                newNodeQueue = new ArrayDeque<>();
        nodeQueue.add(node);
        newNodeQueue.add(graph);
        Set<Leetcode.Node> visited = new HashSet<>();
        visited.add(node);
        while (nodeQueue.size() > 0 && newNodeQueue.size() > 0) {
            var n = nodeQueue.poll();
            visited.add(n);
            var mappedNode = newNodeQueue.poll();
            for (var neighbor : n.neighbors) {
                var mappedNeighbor = getMapped(neighbor);
                assert mappedNode != null;
                mappedNode.neighbors.add(mappedNeighbor);
                if (!visited.contains(neighbor)) {
                    newNodeQueue.add(mappedNeighbor);
                    nodeQueue.add(neighbor);
                    visited.add(neighbor);
                }
            }
        }
        return graph;
    }

    Map<Leetcode.Node, Leetcode.Node> map = new HashMap<>();

    public Leetcode.Node getMapped(Leetcode.Node n) {
        var ans = map.get(n);
        if (ans == null) {
            var m = new Leetcode.Node(n.val,
                    new ArrayList<>(n.neighbors.size()));
            map.put(n, m);
            return m;
        } else
            return ans;
    }

    static class Nest {
        static class Node {
            int val;
            Node next;
            Node random;

            public Node(int val) {
                this.val = val;
                this.next = null;
                this.random = null;
            }
        }

        /**
         * #138
         * 
         * @param head
         * @return
         */
        public Node copyRandomList(Node head) {
            Map<Node, Node> map = new HashMap<>();
            copyNode(head, map);
            return map.get(head);
        }

        public Node copyNode(Node node, Map<Node, Node> map) {
            if (node == null)
                return null;
            var newNode = map.computeIfAbsent(node, n -> new Node(n.val));
            if (node.random != null) {
                var rand = map.computeIfAbsent(node.random,
                        n -> new Node(n.val));
                newNode.random = rand;
            }
            newNode.next = copyNode(node.next, map);
            return newNode;
        }
    }

    /**
     * #139
     *
     * @param s
     * @param wordDict
     * @return
     */
    public static boolean wordBreakOne(String s, List<String> wordDict) {
        Set<String> dict = new HashSet<>(wordDict.size());
        int maxLen = 0;
        int minLen = Integer.MAX_VALUE;
        for (var w : wordDict) {
            dict.add(w);
            maxLen = Math.max(maxLen, w.length());
            minLen = Math.min(minLen, w.length());
        }
        dict.add("");
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int len = minLen; len <= s.length(); len++) {
            for (int segIdx = len - 1; segIdx >= 0
                    && (len - segIdx) <= maxLen; segIdx--) {
                var before = dp[segIdx];
                var segment = dict.contains(s.substring(segIdx, len));
                if (before && segment) {
                    dp[len] = true;
                    break;
                }
            }
        }
        return dp[s.length()];
    }

    /**
     * #140
     *
     * @param s
     * @param wordDict
     * @return
     */
    public static List<String> wordBreakTwo(String s, List<String> wordDict) {
        if (!wordBreakOne(s, wordDict)) {
            return new ArrayList<>();
        }

        Set<String> dict = new HashSet<>(wordDict.size());
        dict.addAll(wordDict);

        Map<Integer, List<String>> memory = new HashMap<>(s.length() * 2);

        var func = new Object() {
            List<String> recursiveSolve(int start) {
                var m = memory.get(start);
                if (m != null) {
                    return m;
                }
                m = new ArrayList<>();
                for (int end = start + 1; end <= s.length(); end++) {
                    var subS = s.substring(start, end);
                    if (dict.contains(subS)) {
                        if (end == s.length()) {
                            m.add(subS);
                            break;
                        } else {
                            var tailBreak = recursiveSolve(end);
                            if (tailBreak != null) {
                                for (var t : tailBreak) {
                                    m.add(subS + " " + t);
                                }
                            }
                        }
                    }
                }
                memory.put(start, m);
                return m;
            }
        };
        return func.recursiveSolve(0);
    }

    /**
     * #142
     * 
     * @param head
     * @return
     */
    public ListNode detectCycle(ListNode head) {
        Map<ListNode, Integer> visitState = new HashMap<>(); // -1 not, 0
                                                             // visiting, 1 done
        return visitCycle(head, visitState);
    }

    ListNode visitCycle(ListNode n, Map<ListNode, Integer> visitState) {
        if (n == null)
            return null;
        if (visitState.getOrDefault(n, -1) == 0)
            return n;
        visitState.computeIfAbsent(n, k -> 0);
        if (n.next != null)
            return visitCycle(n.next, visitState);
        return null;
    }

    /**
     * #143
     * 
     * @param head
     */
    public void reorderList(ListNode head) {
        List<ListNode> arr = new ArrayList<>();
        var ptr = head;
        while (ptr != null) {
            arr.add(ptr);
            ptr = ptr.next;
        }
        boolean isEven = (arr.size() % 2) == 0;
        for (int idx = arr.size() - 1, next = 1; idx >= 0; idx--) {
            if ((isEven && idx != next) || (!isEven && idx + 1 != next)) {
                arr.get(idx).next = arr.get(next);
                next++;
            } else {
                arr.get(idx).next = null;
            }
        }
    }

    /**
     * #144 <br/>
     * 前序遍历
     *
     * @param root root
     * @return res array
     */
    public static List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        solvePreorderTraversal(root, res);
        return res;
    }

    /**
     *
     */
    public static class LRUCache {
        static class DNode {
            int val;
            final int key;
            DNode left;
            DNode right;

            DNode(int key, int val) {
                this.val = val;
                this.key = key;
            }
        }

        final DNode head;
        final DNode tail;
        final int size;
        int count;
        final Map<Integer, DNode> map;

        public LRUCache(int capacity) {
            head = new DNode(0, 0);
            tail = new DNode(0, 0);
            head.right = tail;
            head.left = tail;
            tail.left = head;
            tail.right = head;
            size = capacity;
            map = new HashMap<>(2 * size);
        }

        private void addNodeToHead(DNode n) {
            var h_r = head.right;
            h_r.left = n;
            n.right = h_r;
            head.right = n;
            n.left = head;
        }

        private void removeNodeFromList(DNode n) {
            var n_l = n.left;
            var n_r = n.right;
            n.left.right = n_r;
            n_r.left = n_l;
        }

        public int get(int key) {
            var n = map.get(key);
            if (n != null) {
                removeNodeFromList(n);
                addNodeToHead(n);
            }
            return n != null ? n.val : -1;
        }

        public void put(int key, int value) {
            var search = map.get(key);
            if (search != null) {
                search.val = value;
                removeNodeFromList(search);
                addNodeToHead(search);
            } else {
                var node = new DNode(key, value);
                if (count < size) {
                    count++;
                } else {
                    var t_l = tail.left;
                    removeNodeFromList(t_l);
                    map.remove(t_l.key);
                }
                addNodeToHead(node);
                map.put(key, node);
            }
        }
    }

    private static void solvePreorderTraversal(TreeNode node,
            List<Integer> res) {
        if (node != null) {
            res.add(node.val);
            if (node.left != null) {
                solvePreorderTraversal(node.left, res);
            }
            if (node.right != null) {
                solvePreorderTraversal(node.right, res);
            }
        }
    }

    /**
     * #148
     * 
     * @param head
     * @return
     */
    public ListNode sortList(ListNode head) {
        if (head == null)
            return null;
        return sortListRange(head, null);
    }

    ListNode sortListRange(ListNode start, ListNode end) {
        if (start.next == end) {
            return start;
        }
        if (start.next.next == end) {
            if (start.val <= start.next.val) {
                return start;
            }
            var nt = start.next;
            nt.next = start;
            start.next = end;
            return nt;
        }
        ListNode middle = start;
        ListNode fast = middle;
        while (fast != end) {
            middle = advanceNode(middle, end, 1);
            fast = advanceNode(fast, end, 2);
        }
        var list1 = sortListRange(start, middle);
        var list2 = sortListRange(middle, end);
        ListNode mergeHead = null;
        ListNode mergeEnd = null;
        while (list1 != middle && list2 != end) {
            if (list1.val <= list2.val) {
                if (mergeEnd == null) {
                    mergeHead = list1;
                } else {
                    mergeEnd.next = list1;
                }
                mergeEnd = list1;
                list1 = list1.next;
            } else {
                if (mergeEnd == null) {
                    mergeHead = list2;
                } else {
                    mergeEnd.next = list2;
                }
                mergeEnd = list2;
                list2 = list2.next;
            }
        }
        while (list1 != middle) {
            if (mergeEnd == null) {
                mergeHead = list1;
            } else {
                mergeEnd.next = list1;
            }
            mergeEnd = list1;
            list1 = list1.next;
        }
        while (list2 != end) {
            if (mergeEnd == null) {
                mergeHead = list2;
            } else {
                mergeEnd.next = list2;
            }
            mergeEnd = list2;
            list2 = list2.next;
        }
        mergeEnd.next = end;
        return mergeHead;
    }

    ListNode advanceNode(ListNode n, ListNode end, int t) {
        for (; t > 0 && n != null && n != end; t--) {
            n = n.next;
        }
        return n;
    }

    /**
     * #198
     *
     * @param nums
     * @return
     */
    public static int rob(int[] nums) {
        int[] dp = new int[3];
        dp[0] = nums[0];
        if (nums.length >= 2) {
            dp[1] = Math.max(nums[0], nums[1]);
        }
        for (int i = 2; i < nums.length; i++) {
            dp[i % 3] = Math.max(dp[(i - 2) % 3] + nums[i], dp[(i - 1) % 3]);
        }
        return dp[(nums.length - 1) % 3];
    }
}

@SuppressWarnings({ "JavaDoc" })
public class Leetcode200 {

    /**
     * #152 <br/>
     * 乘积最大子数组
     *
     * @param nums numbers
     * @return max sub array multiple
     */
    public static int maxProduct(int[] nums) {
        int maxF = nums[0];
        int minF = nums[0];
        int ans = nums[0];
        int length = nums.length;
        for (int i = 1; i < length; ++i) {
            int mx = maxF;
            int mn = minF;
            maxF = Math.max(mx * nums[i], Math.max(nums[i], mn * nums[i]));
            minF = Math.min(mn * nums[i], Math.min(nums[i], mx * nums[i]));
            ans = Math.max(maxF, ans);
        }
        return ans;
    }

    /**
     * #155
     */
    public static class MinStack {

        Deque<Integer> deque = new LinkedList<>();
        Deque<Integer> minDeque = new LinkedList<>();

        public MinStack() {
            minDeque.add(Integer.MAX_VALUE);
        }

        public void push(int val) {
            deque.addLast(val);
            minDeque.addLast(Math.min(minDeque.getLast(), val));
        }

        public void pop() {
            deque.removeLast();
            minDeque.removeLast();
        }

        public int top() {
            return deque.getLast();
        }

        public int getMin() {
            return minDeque.getLast();
        }
    }

    /**
     * #160 相交链表
     *
     * @param headA linked list
     * @param headB linked list
     * @return intersect
     */
    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }
        ListNode pA = headA;
        ListNode pB = headB;
        while (pA != pB) {
            pA = pA == null ? headB : pA.next;
            pB = pB == null ? headA : pB.next;
        }
        return pA;
    }

    /**
     * #161
     * 
     * @param s
     * @param t
     * @return
     */
    public boolean isOneEditDistance(String s, String t) {
        if (s.length() > t.length()) {
            var tt = s;
            s = t;
            t = tt;
        }
        if (t.length() - s.length() > 1 || s.equals(t))
            return false;
        int i = 0, j = s.length() - 1;
        int m = 0, n = t.length() - 1;
        int l = 0, r = 0;
        while (i < s.length() && j >= 0 && m < t.length() && n >= 0) {
            var a = s.charAt(i);
            var b = s.charAt(j);
            var c = t.charAt(m);
            var d = t.charAt(n);
            if (a == c) {
                i++;
                m++;
                l++;
            }
            if (b == d) {
                j--;
                n--;
                r++;
            }
            if (a != c && b != d)
                break;
        }
        return (s.length() == t.length() && (l + r) >= s.length() - 1
                && (l + r) >= t.length() - 1)
                || ((l + r) >= s.length() && (l + r) >= t.length() - 1);
    }

    /**
     * #162
     *
     * @param nums
     * @return
     */
    public static int findPeakElement(int[] nums) {
        int len = nums.length;
        if (len == 1) {
            return 0;
        }

        if (nums[0] > nums[1]) {
            return 0;
        } else if (nums[len - 1] > nums[len - 2]) {
            return len - 1;
        }
        for (int i = 1; i < len - 1; i++) {
            if (nums[i] > nums[i - 1] && nums[i] > nums[i + 1]) {
                return i;
            }
        }

        throw new RuntimeException();
    }

    /**
     * #166
     *
     * @param numerator
     * @param denominator
     * @return
     */
    public static String fractionToDecimal(int numerator, int denominator) {
        StringBuilder sb = new StringBuilder();
        boolean geqZero = (long) numerator * (long) denominator >= 0;
        long nu = Math.abs((long) numerator);
        long de = Math.abs((long) denominator);
        var integer = nu / de;
        sb.append(integer);
        var frac = nu % de;
        if (frac != 0) {
            sb.append(".");
            int idx = sb.length();
            Map<Long, Integer> remainderToIdx = new HashMap<>(16);
            remainderToIdx.put(frac, idx);
            while (frac != 0) {
                var this_remainder = frac;
                frac *= 10;
                var d = frac / de;
                sb.append(d);

                idx++;
                frac %= de;

                var searchIdx = remainderToIdx.get(frac);
                if (searchIdx != null) {
                    sb.append(")");
                    sb.insert(searchIdx, "(");
                    break;
                } else {
                    remainderToIdx.put(this_remainder, idx - 1);
                }
            }
        }
        if (!geqZero) {
            sb.insert(0, "-");
        }
        return sb.toString();
    }

    /**
     * #167
     * 
     * @param numbers
     * @param target
     * @return
     */
    public int[] twoSum(int[] numbers, int target) {
        int[] res = new int[2];
        int i = 0, j = numbers.length - 1;
        while (i < j) {
            var sum = numbers[i] + numbers[j];
            if (sum == target) {
                res[0] = i + 1;
                res[1] = j + 1;
                break;
            } else if (sum < target) {
                i++;
                while (i - 1 >= 0 && numbers[i] == numbers[i - 1]) {
                    i++;
                }
            } else {
                j--;
                while (j + 1 < numbers.length && numbers[j] == numbers[j + 1]) {
                    j--;
                }
            }
        }
        return res;
    }

    /**
     * #169
     * 
     * @param nums
     * @return
     */
    public int majorityElement(int[] nums) {
        Map<Integer, Integer> count = new HashMap<>();
        int max = 0;
        int res = 0;
        for (var n : nums) {
            var c = count.getOrDefault(n, 0) + 1;
            count.put(n, c);
            if (c > max) {
                max = c;
                res = n;
            }
        }
        return res;
    }

    /**
     * #173
     */
    public static class BSTIterator {
        private final Deque<TreeNode> stack = new LinkedList<>();
        private TreeNode ptr;
        private boolean poppedBefore = false;
        private boolean finish = false;

        public BSTIterator(TreeNode root) {
            ptr = root;
        }

        public int next() {
            if (finish) {
                throw new NoSuchElementException("Iterate finish.");
            }

            while (ptr != null) {
                if (ptr.left != null && !poppedBefore) // if popped before, walk
                                                       // to right
                {
                    stack.push(ptr);
                    ptr = ptr.left;
                } else {
                    var t = ptr;
                    if (ptr.right != null) {
                        ptr = ptr.right;
                        poppedBefore = false;
                    } else {
                        if (stack.size() != 0) {
                            ptr = stack.pop();
                            poppedBefore = true;
                        } else {
                            ptr = null;
                        }
                    }
                    return t.val;
                }
            }
            finish = true;
            throw new NoSuchElementException("Iterate finish.");
        }

        public boolean hasNext() {
            return !finish && ptr != null;
        }
    }

    /**
     * #190
     *
     * @param n number
     * @return binary reversed
     */
    public static int reverseBits(int n) {
        int M1 = 0x55555555; // 01010101010101010101010101010101
        int M2 = 0x33333333; // 00110011001100110011001100110011
        int M4 = 0x0f0f0f0f; // 00001111000011110000111100001111
        int M8 = 0x00ff00ff; // 00000000111111110000000011111111
        n = (n >>> 1) & M1 | ((n & M1) << 1);
        n = (n >>> 2) & M2 | ((n & M2) << 2);
        n = (n >>> 4) & M4 | ((n & M4) << 4);
        n = (n >>> 8) & M8 | ((n & M8) << 8);
        return n >>> 16 | n << 16;
    }

    /**
     * #200
     *
     * @param grid
     * @return
     */
    public static int numIslands(char[][] grid) {
        class BooleanMatrix {
            final BitSet[] matrix;

            public BooleanMatrix(int m, int n) {
                matrix = new BitSet[m];
                for (int i = 0; i < m; i++) {
                    matrix[i] = new BitSet(n);
                }
            }

            public boolean get(int m, int n) {
                return matrix[m].get(n);
            }

            public void set(int m, int n, boolean b) {
                matrix[m].set(n, b);
            }
        }
        int m = grid.length;
        int n = grid[0].length;
        var bMatrix = new BooleanMatrix(m, n);

        var funcVisit = new Object() {
            void apply(int i, int j) {
                if (grid[i][j] == '1' && !bMatrix.get(i, j)) {
                    bMatrix.set(i, j, true);
                    if (i - 1 >= 0) {
                        apply(i - 1, j);
                    }
                    if (i + 1 < m) {
                        apply(i + 1, j);
                    }
                    if (j + 1 < n) {
                        apply(i, j + 1);
                    }
                    if (j - 1 >= 0) {
                        apply(i, j - 1);
                    }
                }
            }
        };
        int ans = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1' && !bMatrix.get(i, j)) {
                    ans++;
                    funcVisit.apply(i, j);
                }
            }
        }
        return ans;
    }
}
