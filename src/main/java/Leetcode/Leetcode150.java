package Leetcode;

import java.util.*;

@SuppressWarnings({"JavaDoc"})
public class Leetcode150 {
    /**
     * #105
     *
     * <br>前序遍历 preorder = [3,9,20,15,7]
     * <br>中序遍历 inorder = [9,3,15,20,7]
     * <br>result:
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

    private static TreeNode recursiveBuildTree(int[] p_order, int p_start, int p_end, int i_start, Map<Integer, Integer> m) {
        if (p_end - p_start == 1) {
            return new TreeNode(p_order[p_start]);
        }
        else if (p_end - p_start == 0) {
            return null;
        }
        else {
            TreeNode root = new TreeNode(p_order[p_start]);
            int i_mid = m.get(root.val);
            int l_len = i_mid - i_start;
            root.left = recursiveBuildTree(p_order, p_start + 1, p_start + 1 + l_len, i_start, m);
            root.right = recursiveBuildTree(p_order, p_start + 1 + l_len, p_end, i_start + l_len + 1, m);
            return root;
        }
    }


    /**
     * #107
     * <br>层次遍历<br>
     * <pre>
     *  input:[3,9,20,null,null,15,7]
     *     3
     *    / \
     *   9  20
     *     /  \
     *    15   7
     * </pre>
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
     * #111
     * <br/>给定一个二叉树，找出其最小深度。
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
        }
        else {
            if (right == 0) {
                depth += left;
            }
            else {
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
     * #116
     *
     * @param root
     * @return
     */
    public static Node connect(Node root) {
        if (root == null) return null;
        //noinspection FieldMayBeFinal
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
                if (n == null) return new ArrayList<>(0);
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
     * #129
     * <br/>
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

    private static void solveSumNumbers(TreeNode node, int num, List<Integer> sum) {
        num = num * 10 + node.val;
        if (node.left == null && node.right == null) {
            sum.set(0, sum.get(0) + num);
        }
        else if (node.right != null && node.left == null) {
            solveSumNumbers(node.right, num, sum);
        }
        else if (node.right == null) {
            solveSumNumbers(node.left, num, sum);
        }
        else {
            solveSumNumbers(node.right, num, sum);
            solveSumNumbers(node.left, num, sum);
        }
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
                    var t = pLenOfPos.computeIfAbsent(i, o -> new ArrayList<>());
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
        Queue<Leetcode.Node> nodeQueue = new ArrayDeque<>(), newNodeQueue = new ArrayDeque<>();
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
            var m = new Leetcode.Node(n.val, new ArrayList<>(n.neighbors.size()));
            map.put(n, m);
            return m;
        }
        else return ans;
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
            for (int segIdx = len - 1; segIdx >= 0 && (len - segIdx) <= maxLen; segIdx--) {
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
                        }
                        else {
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
     * #144
     * <br/>前序遍历
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
            }
            else {
                var node = new DNode(key, value);
                if (count < size) {
                    count++;
                }
                else {
                    var t_l = tail.left;
                    removeNodeFromList(t_l);
                    map.remove(t_l.key);
                }
                addNodeToHead(node);
                map.put(key, node);
            }
        }
    }

    private static void solvePreorderTraversal(TreeNode node, List<Integer> res) {
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
