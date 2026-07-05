package Leetcode;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("JavaDoc")
public class Leetcode250 {

    /**
     * #202
     * 
     * @param n
     * @return
     */
    public boolean isHappy(int n) {
        var num = n;
        Set<Integer> seen = new HashSet<>();
        do {
            seen.add(num);
            num = next(num);
            if (num == 1)
                return true;
        } while (!seen.contains(num));
        return false;
    }

    int next(int n) {
        int res = 0;
        while (n != 0) {
            var a = n % 10;
            res += a * a;
            n = n / 10;
        }
        return res;
    }

    /**
     * #208
     */
    public static class Trie {
        static class Node {
            char ctr;
            Node left;
            Node right;
            Node mid;
            boolean contain;

            Node(char c) {
                ctr = c;
            }
        }

        Node head = null;

        public Trie() {

        }

        public void insert(String word) {
            head = put(head, word, 0);
        }

        private Node put(Node ptr, String s, int idx) {
            var current_ctr = s.charAt(idx);
            if (ptr == null) {
                ptr = new Node(current_ctr);
            }

            if (current_ctr < ptr.ctr) {
                ptr.left = put(ptr.left, s, idx);
            } else if (current_ctr > ptr.ctr) {
                ptr.right = put(ptr.right, s, idx);
            } else if (idx < s.length() - 1) {
                ptr.mid = put(ptr.mid, s, idx + 1);
            } else {
                ptr.contain = true;
            }
            return ptr;
        }

        public boolean search(String word) {
            return search(head, word, 0);
        }

        private boolean search(Node ptr, String s, int idx) {
            if (ptr == null)
                return false;
            var current_ctr = s.charAt(idx);
            if (ptr.ctr == current_ctr) {
                if (idx == s.length() - 1)
                    return ptr.contain;
                else
                    return search(ptr.mid, s, idx + 1);
            } else if (current_ctr < ptr.ctr)
                return search(ptr.left, s, idx);
            else
                return search(ptr.right, s, idx);
        }

        public boolean startsWith(String prefix) {
            return startsWith(head, prefix, 0);
        }

        private boolean startsWith(Node ptr, String prefix, int idx) {
            if (ptr == null)
                return false;
            var current_ctr = prefix.charAt(idx);
            if (ptr.ctr == current_ctr) {
                if (idx == prefix.length() - 1)
                    return ptr.mid != null || ptr.contain;
                else
                    return startsWith(ptr.mid, prefix, idx + 1);
            } else if (current_ctr < ptr.ctr)
                return startsWith(ptr.left, prefix, idx);
            else
                return startsWith(ptr.right, prefix, idx);
        }
    }

    /**
     * #212
     *
     * @param board
     * @param words
     * @return
     */
    @SuppressWarnings("UseBulkOperation")
    public static List<String> findWords(char[][] board, String[] words) {
        if (words.length == 0)
            return new ArrayList<>();
        Trie trie = new Trie();
        int max_len = 0;
        for (var w : words) {
            max_len = Math.max(max_len, w.length());
            trie.insert(w);
        }
        StringBuilder sb = new StringBuilder();
        int finalMax_len = max_len;
        Set<String> ans = new HashSet<>();
        var findWordsDFS_Func = new Object() {
            public void apply(int r, int c, Trie.Node ptr, int depth) {
                if (depth > finalMax_len || ptr == null) {
                    return;
                }
                var ctr = board[r][c];
                if (ctr == '#') {
                    return;
                }
                while (ptr != null) {
                    if (ptr.ctr == ctr) {
                        sb.append(ctr);
                        board[r][c] = '#';
                        if (ptr.contain) {
                            ans.add(sb.toString());
                        }
                        ptr = ptr.mid;

                        if (r - 1 >= 0) {
                            apply(r - 1, c, ptr, depth + 1);
                        }
                        if (r + 1 < board.length) {
                            apply(r + 1, c, ptr, depth + 1);
                        }
                        if (c - 1 >= 0) {
                            apply(r, c - 1, ptr, depth + 1);
                        }
                        if (c + 1 < board[0].length) {
                            apply(r, c + 1, ptr, depth + 1);
                        }

                        board[r][c] = ctr;
                        sb.deleteCharAt(sb.length() - 1);

                        break;
                    } else if (ctr < ptr.ctr) {
                        ptr = ptr.left;
                    } else
                        ptr = ptr.right;
                }
            }
        };
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                findWordsDFS_Func.apply(i, j, trie.head, 1);
            }
        }
        List<String> ans_list = new ArrayList<>(ans.size());
        for (var s : ans) {
            ans_list.add(s);
        }

        return ans_list;
    }

    /**
     * #213
     * 
     * @param nums
     * @return
     */
    public int rob(int[] nums) {
        int[] dp_first = new int[2];
        int[] dp_no_first = new int[2];
        dp_first[0] = nums[0];
        dp_first[1] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (i > 1 && i < nums.length - 1) {
                var no = Math.max(dp_first[0], dp_first[1]);
                var yes = dp_first[0] + nums[i];
                dp_first[0] = no;
                dp_first[1] = yes;
            }

            var no = Math.max(dp_no_first[0], dp_no_first[1]);
            var yes = dp_no_first[0] + nums[i];
            dp_no_first[0] = no;
            dp_no_first[1] = yes;
        }
        return Math.max(Math.max(dp_first[0], dp_first[1]),
                Math.max(dp_no_first[0], dp_no_first[1]));
    }

    /**
     * #214
     *
     * @param s
     * @return
     */
    public static String shortestPalindrome(String s) {
        char[] inserted = new char[2 * s.length() + 1];
        int idx_inserted = 0, idx_str = 0;
        char placeHolder = '$';
        while (idx_str < s.length()) {
            inserted[idx_inserted++] = placeHolder;
            inserted[idx_inserted++] = s.charAt(idx_str++);
        }
        inserted[idx_inserted] = placeHolder;

        int[] ArmLen = new int[inserted.length];
        int pos = 0, arm_len = 1;
        int target_pos = 0, target_len = 1;
        while (pos < inserted.length) {
            while ((pos + arm_len) < inserted.length && (pos - arm_len) >= 0
                    && inserted[pos + arm_len] == inserted[pos - arm_len]) {
                arm_len++;
            }
            ArmLen[pos] = arm_len;
            if (arm_len > target_len && (pos - (arm_len - 1) == 0)) {
                target_len = arm_len;
                target_pos = pos;
            }

            int mid_pos = pos, mid_arm_len = arm_len;
            pos++;
            arm_len = 1;
            while (pos <= mid_pos + mid_arm_len - 1) {
                int sym_pos = mid_pos - (pos - mid_pos);
                int sym_pos_arm_len = ArmLen[sym_pos];

                int mid_left_bound = mid_pos - (mid_arm_len - 1);
                int sym_left_bound = sym_pos - (sym_pos_arm_len - 1);

                if (sym_left_bound > mid_left_bound) {
                    ArmLen[pos] = sym_pos_arm_len;
                    pos++;
                } else if (sym_left_bound < mid_left_bound) {
                    ArmLen[pos] = sym_pos - mid_left_bound + 1;
                    pos++;
                } else {
                    arm_len = sym_pos_arm_len;
                    break;
                }
            }
        }

        int right = (target_pos + target_len - 1);
        int palindrome_len = right / 2;
        StringBuilder ans = new StringBuilder(s.substring(palindrome_len));
        ans = ans.reverse();
        ans.append(s);
        return ans.toString();
    }

    public int findKthLargest(int[] nums, int k) {
        if (nums.length == 1)
            return nums[0];
        return partition(nums, 0, nums.length, nums.length - k);
    }

    int partition(int[] nums, int s, int e, int k) {
        if (nums[e - 1] < nums[s]) {
            var t = nums[e - 1];
            nums[e - 1] = nums[s];
            nums[s] = t;
        }
        int i = s, j = s, t = e - 2;
        while (j + 1 <= t) {
            if (nums[j] == nums[j + 1]) {
                j++;
            } else if (nums[j + 1] < nums[j]) {
                var tt = nums[i];
                nums[i] = nums[j + 1];
                nums[j + 1] = tt;
                i++;
                j++;
            } else {
                if (j + 1 == t) {
                    t--;
                } else {
                    var tt = nums[j + 1];
                    nums[j + 1] = nums[t];
                    nums[t] = tt;
                    t--;
                }
            }
        }
        if (i - s <= k && j - s >= k) {
            return nums[i];
        } else if (i - s > k) {
            return this.partition(nums, s, i, k);
        } else {
            return this.partition(nums, j + 1, e, k - (j + 1 - s));
        }
    }

    /**
     * #217
     * 
     * @param nums
     * @return
     */
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (var n : nums) {
            if (set.contains(n)) {
                return true;
            }
            set.add(n);
        }
        return false;
    }

    /**
     * #218
     *
     * @param buildings
     * @return
     */
    public static List<List<Integer>> getSkyline(int[][] buildings) {
        class RightHeight {
            final int right;
            final int height;

            RightHeight(int r, int h) {
                right = r;
                height = h;
            }
        }

        class XHeight {
            final int x;
            final int height;
            final int right;

            XHeight(int x, int h, int i) {
                this.x = x;
                this.height = h;
                right = i;
            }
        }

        List<XHeight> scan = new ArrayList<>(buildings.length * 2);
        for (var b : buildings) {
            int x = b[0];
            int x1 = b[1];
            int h = b[2];
            scan.add(new XHeight(x, h, x1));
            scan.add(new XHeight(x1, h, x1));
        }
        scan.sort(Comparator.comparing(t -> t.x));

        PriorityQueue<RightHeight> queue = new PriorityQueue<>(
                (a, b) -> b.height - a.height);
        List<List<Integer>> ans = new ArrayList<>();
        for (var s : scan) {
            int x = s.x;
            int height = s.height;
            int right = s.right;
            if (x != right) {
                queue.add(new RightHeight(right, height));
            }
            while (queue.size() > 0 && x >= queue.peek().right) {
                queue.poll();
            }
            int max_height = queue.peek() != null ? queue.peek().height : 0;
            ans.add(List.of(x, max_height));

            if (ans.size() >= 2 && ans.get(ans.size() - 1).get(0)
                    .equals(ans.get(ans.size() - 2).get(0))) {
                var last = ans.remove(ans.size() - 1);
                var t = ans.remove(ans.size() - 1);
                ans.add(List.of(x, Math.max(t.get(1), last.get(1))));
            }
            if (ans.size() >= 2 && ans.get(ans.size() - 1).get(1)
                    .equals(ans.get(ans.size() - 2).get(1))) {
                ans.remove(ans.size() - 1);
            }

        }
        return ans;
    }

    /**
     * #224
     *
     * @param s
     * @return
     */
    public int calculate(String s) {
        Deque<Double> queue = new ArrayDeque<>();
        StringBuilder num = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c == '(') {
                addNum(num, queue);
                queue.add(Double.NaN);
            } else if (c >= '0' && c <= '9') {
                num.append(c);
            } else if (c == '-') {
                addNum(num, queue);
                queue.addLast(Double.NEGATIVE_INFINITY);
            } else if (c == '+') {
                addNum(num, queue);
            } else if (c == ')') {
                addNum(num, queue);
                double n = queue.pollLast();
                while (!queue.getLast().isNaN()) {
                    n += queue.pollLast();
                }
                queue.pollLast();
                num.append(n);
                addNum(num, queue);
            }
        }
        addNum(num, queue);
        return (int) (double) queue.stream().reduce((a, b) -> a + b).get();
    }

    void addNum(StringBuilder num, Deque<Double> queue) {
        if (!num.isEmpty()) {
            var val = Double.valueOf(num.toString());
            if (!queue.isEmpty() && queue.getLast().isInfinite()) {
                queue.pollLast();
                val = -val;
            }
            queue.addLast(val);
            num.delete(0, num.length());
        }
    }

    /**
     * #227
     *
     * @param s
     * @return
     */
    public int calculate2(String s) {
        // NAN: -, +INF *, -INF /
        Deque<Double> queue = new ArrayDeque<>();
        StringBuilder num = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c == '*') {
                addNum2(num, queue);
                queue.add(Double.POSITIVE_INFINITY);
            } else if (c == '/') {
                addNum2(num, queue);
                queue.add(Double.NEGATIVE_INFINITY);
            } else if (c >= '0' && c <= '9') {
                num.append(c);
            } else if (c == '-') {
                addNum2(num, queue);
                queue.addLast(Double.NaN);
            } else if (c == '+') {
                addNum2(num, queue);
            }
        }
        addNum2(num, queue);
        return (int) (double) queue.stream().reduce((a, b) -> a + b).get();
    }

    void addNum2(StringBuilder num, Deque<Double> queue) {
        if (!num.isEmpty()) {
            int val = Integer.valueOf(num.toString());
            if (!queue.isEmpty()) {
                var op = queue.pollLast();
                if (op.isNaN())
                    val = -val;
                else if (op.isInfinite() && op > 0)
                    val = (int) (double) queue.pollLast() * val;
                else if (op.isInfinite() && op < 0)
                    val = (int) (double) queue.pollLast() / val;
                else
                    queue.addLast(op);
            }
            queue.addLast((double) val);
            num.delete(0, num.length());
        }
    }

    /**
     * #228
     *
     * @param nums
     * @return
     */
    public static List<String> summaryRanges(int[] nums) {
        if (nums.length == 0)
            return new ArrayList<>();
        int left = nums[0];
        List<String> ans = new ArrayList<>();
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] - 1 != nums[i - 1]) {
                int right = nums[i - 1];
                if (right == left) {
                    ans.add(String.valueOf(left));
                } else {
                    ans.add(left + "->" + right);
                }
                left = nums[i];
            }
        }
        {
            int right = nums[nums.length - 1];
            if (right == left) {
                ans.add(String.valueOf(left));
            } else {
                ans.add(left + "->" + right);
            }
        }
        return ans;
    }

    /**
     * #230
     *
     * @param root
     * @param k
     * @return
     */
    public static int kthSmallest(TreeNode root, int k) {
        var tree = new OrderStatTree<Integer, Void>(
                Comparator.comparingInt(Integer::intValue));
        add(root, tree);
        return tree.getKeyOfRank(k);
    }

    private static void add(TreeNode n, OrderStatTree<Integer, Void> tree) {
        if (n == null) {
            return;
        }
        tree.insertKV(n.val, null);
        add(n.left, tree);
        add(n.right, tree);
    }

    private static class OrderStatTree<K, V> {

        final Node<K, V> sentinel = new Node<>(RBTreeTemplate.BLACK);
        Node<K, V> root = sentinel;

        private final RBTreeTemplate<K, Node<K, V>> template;

        public OrderStatTree(Comparator<K> comparator) {
            template = new RBTreeTemplate<>(sentinel, comparator, n -> n.key,
                    () -> this.root, r -> this.root = r, n -> n.parent,
                    (n, p) -> n.parent = p, n -> n.left, (n, l) -> n.left = l,
                    n -> n.right, (n, r) -> n.right = r, n -> n.color,
                    (n, c) -> n.color = c);
        }

        public K getKeyOfRank(int rank) {
            if (rank <= 0 || rank > size()) {
                throw new IndexOutOfBoundsException();
            }
            Node<K, V> n = getNodeOfRank(rank);
            return n.key;
        }

        Node<K, V> getNodeOfRank(int ith) {
            return getNodeOfRank(root, ith);
        }

        Node<K, V> getNodeOfRank(Node<K, V> current, int ith) {
            int rank = current.left.size + 1;
            if (rank == ith) {
                return current;
            } else if (ith < rank) {
                return getNodeOfRank(current.left, ith);
            } else {
                return getNodeOfRank(current.right, ith - rank);
            }
        }

        public int size() {
            return root.size;
        }

        public void insertKV(K key, V val) {
            var n = new Node<>(key, val);
            template.insert(n);
        }

        static final class Node<key, val> {
            key key;
            val value;
            boolean color;
            Node<key, val> parent;
            Node<key, val> left;
            Node<key, val> right;
            int size;

            Node(boolean color) {
                this.color = color;
            }

            Node(key key, val val) {
                color = RBTreeTemplate.RED;
                this.key = key;
                this.value = val;
            }

            @Override
            public String toString() {
                return "Node{" + "key=" + key + ", value=" + value + ", size="
                        + size + '}';
            }
        }
    }

    @FunctionalInterface
    public interface Gettable<Item> {
        Item get();
    }

    /**
     * #231
     *
     * @param n
     * @return
     */
    public static boolean isPowerOfTwo(int n) {
        if (n <= 0)
            return false;
        while (n != 1) {
            var rem = n % 2;
            if (rem == 1)
                return false;
            n /= 2;
        }
        return true;
    }

    @SuppressWarnings({ "ClassCanBeRecord", "PatternVariableCanBeUsed",
            "hiding" })
    static class RBTreeTemplate<Key, Node> {
        final Node sentinel;
        final Comparator<Key> comparator;
        final Gettable<Node> getRoot;
        final Consumer<Node> setRoot;
        static final boolean RED = false;
        static final boolean BLACK = true;
        final Function<Node, Node> getParent;
        final BiConsumer<Node, Node> setParent;
        final Function<Node, Node> getLeft;
        final BiConsumer<Node, Node> setLeft;
        final Function<Node, Node> getRight;
        final BiConsumer<Node, Node> setRight;
        final Function<Node, Boolean> getColor;
        final BiConsumer<Node, Boolean> setColor;
        final Function<Node, Key> getKey;

        RBTreeTemplate(Node sentinel, Comparator<Key> comparator,
                Function<Node, Key> getKey, Gettable<Node> getRoot,
                Consumer<Node> setRoot, Function<Node, Node> getParent,
                BiConsumer<Node, Node> setParent, Function<Node, Node> getLeft,
                BiConsumer<Node, Node> setLeft, Function<Node, Node> getRight,
                BiConsumer<Node, Node> setRight,
                Function<Node, Boolean> getColor,
                BiConsumer<Node, Boolean> setColor) {
            this.sentinel = sentinel;
            this.comparator = comparator;
            this.getRoot = getRoot;
            this.setRoot = setRoot;
            this.getParent = getParent;
            this.setParent = setParent;
            this.getRight = getRight;
            this.setRight = setRight;
            this.getLeft = getLeft;
            this.setLeft = setLeft;
            this.getColor = getColor;
            this.setColor = setColor;
            this.getKey = getKey;
        }

        /**
         * insert a node <b>WITHOUT</b> duplicate key
         *
         * @param z node or sentinel
         */
        @SuppressWarnings({ "SuspiciousNameCombination" })
        void insert(Node z) {
            var y = sentinel;
            var x = getRoot.get();
            while (x != sentinel) {
                y = x;
                {//
                    if (x instanceof OrderStatTree.Node<?, ?>) {
                        OrderStatTree.Node<?, ?> xo = (OrderStatTree.Node<?, ?>) x;
                        xo.size++;
                    }
                }
                if (comparator.compare(getKey.apply(z), getKey.apply(x)) < 0) {
                    x = getLeft.apply(x);
                } else if (comparator.compare(getKey.apply(z),
                        getKey.apply(x)) > 0) {
                    x = getRight.apply(x);
                } else {
                    throw new IllegalArgumentException("duplicate key.");
                }
            }
            setParent.accept(z, y);
            if (y == sentinel) {
                setRoot.accept(z);
            } else if (comparator.compare(getKey.apply(z),
                    getKey.apply(y)) < 0) {
                setLeft.accept(y, z);
            } else if (comparator.compare(getKey.apply(z),
                    getKey.apply(y)) > 0) {
                setRight.accept(y, z);
            } else {
                throw new RuntimeException("impossible error.");
            }
            setLeft.accept(z, sentinel);
            setRight.accept(z, sentinel);
            setColor.accept(z, RED);
            {//
                if (z instanceof OrderStatTree.Node<?, ?>) {
                    OrderStatTree.Node<?, ?> zo = (OrderStatTree.Node<?, ?>) z;
                    zo.size = 1;
                }
            }
            insertFixUp(z);
        }

        private void insertFixUp(Node z) {
            while (getColor.apply(getParent.apply(z)) == RED) {
                if (getParent.apply(z) == getLeft
                        .apply(getParent.apply(getParent.apply(z)))) {
                    var y = getRight.apply(getParent.apply(getParent.apply(z)));
                    if (getColor.apply(y) == RED) {
                        setColor.accept(getParent.apply(z), BLACK);
                        setColor.accept(y, BLACK);
                        setColor.accept(getParent.apply(getParent.apply(z)),
                                RED);
                        z = getParent.apply(getParent.apply(z));
                    } else {
                        if (z == getRight.apply(getParent.apply(z))) {
                            z = getParent.apply(z);
                            leftRotate(z);
                        }
                        setColor.accept(getParent.apply(z), BLACK);
                        setColor.accept(getParent.apply(getParent.apply(z)),
                                RED);
                        rightRotate(getParent.apply(getParent.apply(z)));
                    }
                } else {
                    var y = getLeft.apply(getParent.apply(getParent.apply(z)));
                    if (getColor.apply(y) == RED) {
                        setColor.accept(getParent.apply(z), BLACK);
                        setColor.accept(y, BLACK);
                        setColor.accept(getParent.apply(getParent.apply(z)),
                                RED);
                        z = getParent.apply(getParent.apply(z));
                    } else {
                        if (z == getLeft.apply(getParent.apply(z))) {
                            z = getParent.apply(z);
                            rightRotate(z);
                        }
                        setColor.accept(getParent.apply(z), BLACK);
                        setColor.accept(getParent.apply(getParent.apply(z)),
                                RED);
                        leftRotate(getParent.apply(getParent.apply(z)));
                    }
                }
            }
            setColor.accept(getRoot.get(), BLACK);
        }

        void delete(Node z) {
            var y = z;
            var y_origin_color = getColor.apply(y);
            Node x;
            if (getLeft.apply(z) == sentinel) {
                x = getRight.apply(z);
                RBTransplant(z, getRight.apply(z));
            } else if (getRight.apply(z) == sentinel) {
                x = getLeft.apply(z);
                RBTransplant(z, getLeft.apply(z));
            } else {
                y = minimumNodeOf(getRight.apply(z));
                y_origin_color = getColor.apply(y);
                x = getRight.apply(y);
                if (getParent.apply(y) == z) {
                    setParent.accept(x, y);
                } else {
                    RBTransplant(y, getRight.apply(y));
                    setRight.accept(y, getRight.apply(z));
                    setParent.accept(getRight.apply(y), y);
                }
                RBTransplant(z, y);
                setLeft.accept(y, getLeft.apply(z));
                setParent.accept(getLeft.apply(y), y);
                setColor.accept(y, getColor.apply(z));
            }

            {//
                if (y instanceof OrderStatTree.Node<?, ?>) {
                    if (getRight.apply(y) != sentinel) {
                        y = minimumNodeOf(getRight.apply(y));
                    }
                    // noinspection PatternVariableCanBeUsed
                    var yo = (OrderStatTree.Node<?, ?>) y;
                    while (yo != sentinel) {
                        yo.size = yo.right.size + yo.left.size + 1;
                        yo = yo.parent;
                    }
                }
            }

            if (y_origin_color == BLACK) {
                deleteFixUp(x);
            }
        }

        private void deleteFixUp(Node x) {
            while (x != getRoot.get() && getColor.apply(x) == BLACK) {
                if (x == getLeft.apply(getParent.apply(x))) {
                    var w = getRight.apply(getParent.apply(x));
                    if (getColor.apply(w) == RED) {
                        setColor.accept(w, BLACK);
                        setColor.accept(getParent.apply(x), RED);
                        leftRotate(getParent.apply(x));
                        w = getRight.apply(getParent.apply(x));
                    }
                    if (getColor.apply(getLeft.apply(w)) == BLACK
                            && getColor.apply(getRight.apply(w)) == BLACK) {
                        setColor.accept(w, RED);
                        x = getParent.apply(x);
                    } else {
                        if (getColor.apply(getRight.apply(w)) == BLACK) {
                            setColor.accept(getLeft.apply(w), BLACK);
                            setColor.accept(w, RED);
                            rightRotate(w);
                            w = getRight.apply(getParent.apply(x));
                        }
                        setColor.accept(w, getColor.apply(getParent.apply(x)));
                        setColor.accept(getParent.apply(x), BLACK);
                        setColor.accept(getRight.apply(w), BLACK);
                        leftRotate(getParent.apply(x));
                        x = getRoot.get();
                    }
                } else {
                    var w = getLeft.apply(getParent.apply(x));
                    if (getColor.apply(w) == RED) {
                        setColor.accept(w, BLACK);
                        setColor.accept(getParent.apply(x), RED);
                        rightRotate(getParent.apply(x));
                        w = getLeft.apply(getParent.apply(x));
                    }
                    if (getColor.apply(getLeft.apply(w)) == BLACK
                            && getColor.apply(getRight.apply(w)) == BLACK) {
                        setColor.accept(w, RED);
                        x = getParent.apply(x);
                    } else {
                        if (getColor.apply(getLeft.apply(w)) == BLACK) {
                            setColor.accept(getRight.apply(w), BLACK);
                            setColor.accept(w, RED);
                            leftRotate(w);
                            w = getLeft.apply(getParent.apply(x));
                        }
                        setColor.accept(w, getColor.apply(getParent.apply(x)));
                        setColor.accept(getParent.apply(x), BLACK);
                        setColor.accept(getLeft.apply(w), BLACK);
                        rightRotate(getParent.apply(x));
                        x = getRoot.get();
                    }
                }
            }
            setColor.accept(x, BLACK);
        }

        private Node minimumNodeOf(Node x) {
            while (getLeft.apply(x) != sentinel) {
                x = getLeft.apply(x);
            }
            return x;
        }

        private void RBTransplant(Node u, Node v) {
            if (getParent.apply(u) == sentinel) {
                setRoot.accept(v);
            } else if (u == getLeft.apply(getParent.apply(u))) {
                setLeft.accept(getParent.apply(u), v);
            } else {
                setRight.accept(getParent.apply(u), v);
            }
            setParent.accept(v, getParent.apply(u));
        }

        private void leftRotate(Node x) {
            var y = getRight.apply(x);

            setRight.accept(x, getLeft.apply(y));
            if (getLeft.apply(y) != sentinel) {
                setParent.accept(getLeft.apply(y), x);
            }

            setParent.accept(y, getParent.apply(x));
            if (getParent.apply(x) == sentinel) {
                setRoot.accept(y);
            } else if (x == getLeft.apply(getParent.apply(x))) {
                setLeft.accept(getParent.apply(x), y);
            } else {
                setRight.accept(getParent.apply(x), y);
            }

            setLeft.accept(y, x);
            setParent.accept(x, y);
            {//
                if (x instanceof OrderStatTree.Node<?, ?>
                        && y instanceof OrderStatTree.Node<?, ?>) {
                    OrderStatTree.Node<?, ?> xo = (OrderStatTree.Node<?, ?>) x;
                    OrderStatTree.Node<?, ?> yo = (OrderStatTree.Node<?, ?>) y;
                    yo.size = xo.size;
                    xo.size = xo.left.size + xo.right.size + 1;
                }
            }
        }

        private void rightRotate(Node x) {
            var y = getLeft.apply(x);

            setLeft.accept(x, getRight.apply(y));
            if (getRight.apply(y) != sentinel) {
                setParent.accept(getRight.apply(y), x);
            }

            setParent.accept(y, getParent.apply(x));
            if (getParent.apply(x) == sentinel) {
                setRoot.accept(y);
            } else if (x == getRight.apply(getParent.apply(x))) {
                setRight.accept(getParent.apply(x), y);
            } else {
                setLeft.accept(getParent.apply(x), y);
            }

            setRight.accept(y, x);
            setParent.accept(x, y);
            {//
                if (x instanceof OrderStatTree.Node<?, ?>
                        && y instanceof OrderStatTree.Node<?, ?>) {
                    OrderStatTree.Node<?, ?> yo = (OrderStatTree.Node<?, ?>) y;
                    OrderStatTree.Node<?, ?> xo = (OrderStatTree.Node<?, ?>) x;
                    yo.size = xo.size;
                    xo.size = xo.left.size + xo.right.size + 1;
                }
            }
        }
    }

    /**
     * #234 <br/>
     * 回文链表
     *
     * @param head node
     * @return res
     */
    public static boolean isPalindrome(ListNode head) {
        List<Integer> vals = new ArrayList<>();

        // 将链表的值复制到数组中
        ListNode currentNode = head;
        while (currentNode != null) {
            vals.add(currentNode.val);
            currentNode = currentNode.next;
        }

        int front = 0;
        int back = vals.size() - 1;
        while (front < back) {
            if (!vals.get(front).equals(vals.get(back))) {
                return false;
            }
            front++;
            back--;
        }
        return true;
    }

    /**
     * #235 <br/>
     * 二叉搜索树的最近公共祖先
     *
     * @param root tree
     * @param p    node 1
     * @param q    node 2
     * @return closest ancestor
     */
    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p,
            TreeNode q) {
        if (root.val > p.val && root.val > q.val) {
            return lowestCommonAncestor(root.left, p, q);
        } else if (root.val < p.val && root.val < q.val) {
            return lowestCommonAncestor(root.right, p, q);
        } else {
            return root;
        }
    }

    /**
     * #239
     *
     * @param nums
     * @param k
     * @return
     */
    public static int[] maxSlidingWindow(int[] nums, int k) {
        if (k == 1)
            return nums;
        var reverse_bin_str = toReversedBinaryString(k);
        int[][] dp = new int[reverse_bin_str.length()][];
        dp[0] = nums;
        for (int i = 1; i < dp.length; i++) {
            int wind_len = (int) Math.pow(2, i);
            dp[i] = new int[nums.length + 1 - wind_len];
        }
        for (int i = 1; i < dp.length; i++) {
            int wind_len = (int) Math.pow(2, i);
            for (int j = 0; j < dp[i].length; j++) {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j + wind_len / 2]);
            }
        }
        int[] ans = new int[nums.length + 1 - k];
        for (int i = 0; i < ans.length; i++) {
            int max = Integer.MIN_VALUE;
            int start = i;
            for (int j = 0; j < reverse_bin_str.length(); j++) {
                if (reverse_bin_str.charAt(j) == '1') {
                    max = Math.max(max, dp[j][start]);
                    start += Math.pow(2, j);
                }
            }
            ans[i] = max;
        }
        return ans;
    }

    private static String toReversedBinaryString(int k) {
        StringBuilder sb = new StringBuilder();
        while (k != 0) {
            var rem = k % 2;
            sb.append(rem);
            k = k / 2;
        }
        return sb.toString();
    }

    public static int[] maxSlidingWindow2(int[] nums, int k) {
        if (k == 1)
            return nums;
        PriorityQueue<Integer> queue = new PriorityQueue<>((a, b) -> b - a);
        int[] ans = new int[nums.length + 1 - k];
        for (int i = 0; i < k; i++) {
            queue.add(nums[i]);
        }
        Map<Integer, Integer> waitToDelete = new HashMap<>();
        for (int i = 0, tail = 0,
                head = k; i < ans.length; i++, tail++, head++) {
            while (waitToDelete.containsKey(queue.peek())) {
                var peek = queue.poll();
                var count = waitToDelete.get(peek);
                if (count == 1)
                    waitToDelete.remove(peek);
                else
                    waitToDelete.put(peek, count - 1);
            }
            ans[i] = queue.peek();
            waitToDelete.put(nums[tail],
                    waitToDelete.getOrDefault(nums[tail], 0) + 1);
            if (head < nums.length) {
                queue.add(nums[head]);
            }
        }
        return ans;
    }

    public static int[] maxSlidingWindow3(int[] nums, int k) {
        if (k == 1)
            return nums;
        int[] ans = new int[nums.length + 1 - k];
        class IndexValue {
            final int idx;
            final int val;

            IndexValue(int i, int v) {
                idx = i;
                val = v;
            }
        }

        Deque<IndexValue> deque = new ArrayDeque<>(k);
        var funcAddToDeque = new Object() {
            void apply(int i, int current_val) {
                if (deque.size() == 0) {
                    deque.addLast(new IndexValue(i, current_val));
                } else if (deque.peekLast().val > current_val) {
                    deque.addLast(new IndexValue(i, current_val));
                } else {
                    while (deque.size() > 0
                            && deque.peekLast().val <= current_val) {
                        deque.pollLast();
                    }
                    deque.addLast(new IndexValue(i, current_val));
                }
            }
        };
        for (int i = 0; i < k; i++) {
            funcAddToDeque.apply(i, nums[i]);
        }

        for (int i = 0, head = k; i < ans.length; i++, head++) {
            while (deque.size() > 0 && deque.peekFirst().idx < i) {
                deque.pollFirst();
            }
            ans[i] = deque.peekFirst().val;
            if (head < nums.length) {
                funcAddToDeque.apply(head, nums[head]);
            }
        }
        return ans;
    }

    /**
     * #240
     *
     * @param matrix
     * @param target
     * @return
     */
    public static boolean searchMatrix(int[][] matrix, int target) {
        class MatrixRange {
            final int RowStart;
            final int RowEnd;
            final int ColStart;
            final int ColEnd;

            MatrixRange(int rs, int re, int cs, int ce) {
                RowStart = rs;
                RowEnd = re;
                ColStart = cs;
                ColEnd = ce;
            }
        }
        var funcMatrixBinarySearch = new Object() {
            /**
             *
             * @param mr always square
             */
            boolean apply(MatrixRange mr) {
                int rLen = mr.RowEnd - mr.RowStart;
                int cLen = mr.ColEnd - mr.ColStart;
                if (rLen < 1 || cLen < 1) {
                    return false;
                } else if (rLen == 1) {
                    int s = mr.ColStart, e = mr.ColEnd;
                    while (e - s > 1) {
                        var mid = (s + e) / 2;
                        if (matrix[mr.RowStart][mid] <= target)
                            s = mid;
                        else
                            e = mid;
                    }
                    return matrix[mr.RowStart][s] == target;
                } else if (cLen == 1) {
                    int s = mr.RowStart, e = mr.RowEnd;
                    while (e - s > 1) {
                        var mid = (s + e) / 2;
                        if (matrix[mid][mr.ColStart] <= target)
                            s = mid;
                        else
                            e = mid;
                    }
                    return matrix[s][mr.ColStart] == target;
                } else {
                    int s = 0, e = Math.min(rLen, cLen);
                    while (e - s > 1) {
                        var mid = (s + e) / 2;
                        if (matrix[mr.RowStart + mid][mr.ColStart
                                + mid] <= target)
                            s = mid;
                        else
                            e = mid;
                    }
                    if (matrix[mr.RowStart + s][mr.ColStart + s] == target) {
                        return true;
                    } else {
                        var res1 = apply(new MatrixRange(mr.RowStart + s + 1,
                                mr.RowEnd, mr.ColStart, mr.ColStart + s + 1));
                        var res2 = apply(new MatrixRange(mr.RowStart,
                                mr.RowStart + s + 1, mr.ColStart + s + 1,
                                mr.ColEnd));
                        return res1 || res2;
                    }
                }
            }
        };
        return funcMatrixBinarySearch
                .apply(new MatrixRange(0, matrix.length, 0, matrix[0].length));
    }

    public static boolean searchMatrix2(int[][] matrix, int target) {
        int x = 0, y = matrix[0].length - 1;
        while (x < matrix.length && y >= 0) {
            var val = matrix[x][y];
            if (val == target)
                return true;
            else if (val > target)
                y--;
            else
                x++;
        }
        return false;
    }

    /**
     * #242
     * 
     * @param s
     * @param t
     * @return
     */
    public boolean isAnagram(String s, String t) {
        Map<Character, Integer> statS = new HashMap<>();
        Map<Character, Integer> statT = new HashMap<>();
        if (s.length() != t.length())
            return false;
        for (var c : s.toCharArray()) {
            statS.put(c, statS.getOrDefault(c, 0) + 1);
        }
        for (var c : t.toCharArray()) {
            statT.put(c, statT.getOrDefault(c, 0) + 1);
        }
        return statS.equals(statT);
    }
}
