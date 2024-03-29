package Leetcode;

import java.util.*;

@SuppressWarnings({ "JavaDoc", "Unused" })
public class Leetcode500 {

    /**
     * #451
     *
     * @param s
     * @return
     */
    public static String frequencySort(String s) {
        List<Integer> chrs = new ArrayList<>(s.chars().boxed().toList());
        Map<Integer, Integer> freq = new HashMap<>(26);
        for (var c : chrs) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
        chrs.sort((a, b) -> freq.get(b) * 100 - freq.get(a) * 100 + b - a);
        StringBuilder sb = new StringBuilder();
        for (var c : chrs) {
            sb.append((char) (int) c);
        }
        return sb.toString();
    }

    /**
     * #452
     *
     * @param points
     * @return
     */
    public static int findMinArrowShots(int[][] points) {
        if (points.length == 1) {
            return 1;
        }
        Arrays.sort(points, Comparator.comparingInt(a -> a[1]));
        int count = 1;
        int end = points[0][1];
        for (int i = 1; i < points.length; i++) {
            var p = points[i];
            int l = p[0], r = p[1];
            if (!(end >= l && end <= r)) {
                count++;
                end = r;
            }
        }

        return count;
    }

    /**
     * #459
     *
     * @param s
     * @return
     */
    public static boolean repeatedSubstringPattern(String s) {
        return customKMP(s + s, s);
    }

    private static boolean customKMP(String T, String P) {
        int n = T.length(), m = P.length();
        int[] PI = computePI(P);
        int q = 0;
        for (int i = 0; i < n; i++) {
            while (q > 0 && P.charAt(q) != T.charAt(i)) {
                q = PI[q];
            }
            if (P.charAt(q) == T.charAt(i)) {
                q++;
            }
            if (q == m) {
                int res = i + 1 - m;
                if (res != 0 && res != P.length())
                    return true;
                q = PI[q];
            }
        }
        return false;
    }

    /**
     * max match length of prefix of P and suffix end with ith(start from 1)
     * character
     *
     * @param P string
     * @return prefix function (length = len(p) + 1)
     */
    private static int[] computePI(String P) {
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

    /**
     * #460
     */
    public static class LFUCache {

        public static class DLinkMatrix {

            final DMNode head, tail;

            DLinkMatrix() {
                head = new DMNode(-1);
                tail = new DMNode(-1);
                head.right = tail;
                head.left = tail;
                tail.left = head;
                tail.right = head;
            }

            static void insertAfter(DMNode n, DMNode insert) {
                var r = n.right;
                n.right = insert;
                insert.left = n;

                insert.right = r;
                r.left = insert;
            }

            DMNode firstNode() {
                if (head.right != tail) {
                    return head.right;
                } else
                    throw new RuntimeException();
            }

            boolean isEmpty() {
                return head.right == tail;
            }

            static class DMNode {
                DMNode left;
                DMNode right;
                final int freq;

                DMNode(int f) {
                    freq = f;
                }

                DLinkList list = new DLinkList();

                void detach() {
                    var l = left;
                    var r = right;
                    l.right = r;
                    r.left = l;
                }
            }

            static class DLinkList {
                final DLNode head, tail;

                DLinkList() {
                    head = new DLNode(-1);
                    tail = new DLNode(-1);
                    head.right = tail;
                    head.left = tail;
                    tail.left = head;
                    tail.right = head;
                }

                boolean isEmpty() {
                    return head.right == tail;
                }

                void addNode(DLNode n) {
                    var r = head.right;
                    head.right = n;
                    n.left = head;

                    r.left = n;
                    n.right = r;
                }

                DLNode lastNode() {
                    if (head.right != tail) {
                        return tail.left;
                    } else
                        throw new RuntimeException();
                }

                static class DLNode {
                    DLNode left;
                    DLNode right;
                    int key;

                    DLNode(int key) {
                        this.key = key;
                    }

                    void detach() {
                        var l = left;
                        var r = right;
                        l.right = r;
                        r.left = l;
                    }
                }
            }
        }

        static class Info {
            int val;
            DLinkMatrix.DMNode nodeM;
            DLinkMatrix.DLinkList.DLNode nodeL;

            Info(int val, DLinkMatrix.DMNode nodeM, DLinkMatrix.DLinkList.DLNode nodeL) {
                this.val = val;
                this.nodeL = nodeL;
                this.nodeM = nodeM;
            }
        }

        final Map<Integer, Info> key_info_map;

        final int capacity;

        final DLinkMatrix dLinkMatrix = new DLinkMatrix();

        public LFUCache(int capacity) {
            this.capacity = capacity;
            key_info_map = new HashMap<>(capacity);
        }

        public int get(int key) {
            if (capacity == 0)
                return -1;
            var info = key_info_map.get(key);
            int ans = -1;
            if (info == null)
                return ans;
            ans = info.val;

            var mNode = info.nodeM;
            var lNode = info.nodeL;
            var freq = mNode.freq;
            freq++;
            if (mNode.right.freq == freq) {
                lNode.detach();
                if (mNode.list.isEmpty())
                    mNode.detach();
                mNode.right.list.addNode(lNode);
                info.nodeM = mNode.right;
            } else {
                var rMNode = new DLinkMatrix.DMNode(freq);
                DLinkMatrix.insertAfter(mNode, rMNode);
                info.nodeM = rMNode;

                lNode.detach();
                if (mNode.list.isEmpty())
                    mNode.detach();
                rMNode.list.addNode(lNode);
            }
            return ans;
        }

        public void put(int key, int value) {
            if (capacity == 0)
                return;
            if (key_info_map.containsKey(key)) {
                get(key);
                key_info_map.get(key).val = value;
            } else {
                if (key_info_map.size() >= capacity) {
                    // evict
                    var n = dLinkMatrix.firstNode().list.lastNode();
                    key_info_map.remove(n.key);
                    n.detach();
                    if (dLinkMatrix.firstNode().list.isEmpty())
                        dLinkMatrix.firstNode().detach();
                }

                if (dLinkMatrix.isEmpty()) {
                    var mNode = new DLinkMatrix.DMNode(1);
                    var lNode = new DLinkMatrix.DLinkList.DLNode(key);
                    mNode.list.addNode(lNode);
                    DLinkMatrix.insertAfter(dLinkMatrix.head, mNode);
                    key_info_map.put(key, new Info(value, mNode, lNode));
                } else {
                    var fMNode = dLinkMatrix.firstNode();
                    if (fMNode.freq == 1) {
                        var lNode = new DLinkMatrix.DLinkList.DLNode(key);
                        fMNode.list.addNode(lNode);
                        key_info_map.put(key, new Info(value, fMNode, lNode));
                    } else {
                        var mNode = new DLinkMatrix.DMNode(1);
                        DLinkMatrix.insertAfter(dLinkMatrix.head, mNode);
                        var lNode = new DLinkMatrix.DLinkList.DLNode(key);
                        mNode.list.addNode(lNode);
                        key_info_map.put(key, new Info(value, mNode, lNode));
                    }
                }
            }
        }
    }

    /**
     * Your LFUCache object will be instantiated and called as such:
     * LFUCache obj = new LFUCache(capacity);
     * int param_1 = obj.get(key);
     * obj.put(key,value);
     */
    /**
     * #463
     * 
     * <pre>
     * 输入:
     * [[0,1,0,0],
     *  [1,1,1,0],
     *  [0,1,0,0],
     *  [1,1,0,0]]
     * 输出: 16
     * 解释: 它的周长是 16 个方格的边：
     * </pre>
     *
     * @param grid lake and island
     * @return perimeter
     */
    public static int islandPerimeter(int[][] grid) {
        int[] dx = { 0, 1, 0, -1 };
        int[] dy = { 1, 0, -1, 0 };
        int n = grid.length, m = grid[0].length;
        int ans = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                if (grid[i][j] == 1) {
                    int cnt = 0;
                    for (int k = 0; k < 4; ++k) {
                        int tx = i + dx[k];
                        int ty = j + dy[k];
                        if (tx < 0 || tx >= n || ty < 0 || ty >= m || grid[tx][ty] == 0) {
                            cnt += 1;
                        }
                    }
                    ans += cnt;
                }
            }
        }
        return ans;
    }

    /**
     * #474
     *
     * @param strs
     * @param m
     * @param n
     * @return
     */
    public static int findMaxForm(String[] strs, int m, int n) {
        int[][] dp = new int[m + 1][n + 1];
        for (var s : strs) {
            var count = countZerosAndOnes(s);
            for (int i = m; i >= 0; i--) {
                for (int j = n; j >= 0; j--) {
                    if (i >= count[0] && j >= count[1]) {
                        dp[i][j] = Math.max(dp[i][j], dp[i - count[0]][j - count[1]] + 1);
                    }
                }
            }
        }
        int max = -1;
        for (var a : dp) {
            for (var num : a) {
                max = Math.max(num, max);
            }
        }
        return max;
    }

    private static int[] countZerosAndOnes(String s) {
        int[] ans = new int[2];
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0')
                ans[0]++;
            else
                ans[1]++;
        }
        return ans;
    }

    /**
     * #475
     *
     * @param houses
     * @param heaters
     * @return
     */
    public static int findRadius(int[] houses, int[] heaters) {
        var tree = new TreeSet<Integer>();
        for (var n : heaters)
            tree.add(n);
        int ans = -1;
        for (var n : houses) {
            var l = tree.floor(n);
            var r = tree.ceiling(n);
            if (l == null)
                ans = Math.max(ans, Math.abs(r - n));
            else if (r == null)
                ans = Math.max(ans, Math.abs(l - n));
            else
                ans = Math.max(ans, Math.min(Math.abs(l - n), Math.abs(r - n)));
        }
        return ans;
    }

    /**
     * #476
     *
     * @param num
     * @return
     */
    public static int findComplement(int num) {
        if (num == 0)
            return 1;
        int i = 31;
        while (i >= 1 && kthBinDigit(num, i) == 0) {
            i--;
        }
        num = lastKBinDigits(~num, i);

        return num;
    }

    private static int lastKBinDigits(int num, int k) {
        return num & ((1 << k) - 1);
    }

    private static int kthBinDigit(int num, int k) {
        return num >>> k & 1;
    }

    /**
     * #480
     *
     * @param nums
     * @param k
     * @return
     */
    public static double[] medianSlidingWindow(int[] nums, int k) {
        double[] ans = new double[nums.length - k + 1];
        var MDH = new MedianDualHeap(k);
        int idx = 0;
        for (var n : nums) {
            MDH.add(n);
            if (MDH.hasMediean())
                ans[idx++] = MDH.median();
        }
        return ans;
    }

    static final class MedianDualHeap {
        final PriorityQueue<Long> minHeap;
        final PriorityQueue<Long> maxHeap;

        final Deque<Long> queue;
        final int capacity;

        MedianDualHeap(int cap) {
            minHeap = new PriorityQueue<>((cap + 1) / 2);
            maxHeap = new PriorityQueue<>((cap + 1) / 2, Comparator.reverseOrder());
            queue = new ArrayDeque<>(cap + 1);
            capacity = cap;
        }

        private boolean isEmpty() {
            return maxHeap.isEmpty();
        }

        public boolean hasMediean() {
            return queue.size() == capacity;
        }

        public void add(long n) {
            queue.addLast(n);
            pollQueueExcess();

            if (isEmpty())
                maxHeap.add(n);
            else {
                if (n <= maxHeap.peek())
                    maxHeap.add(n);
                else
                    minHeap.add(n);

                while (maxHeap.size() > minHeap.size() + 1) {
                    minHeap.add(maxHeap.poll());
                }

                while (maxHeap.size() < minHeap.size()) {
                    maxHeap.add(minHeap.poll());
                }
            }
        }

        public double median() {
            if (maxHeap.size() > minHeap.size())
                return maxHeap.peek();
            else
                return (minHeap.peek() + maxHeap.peek()) / 2.;
        }

        private void pollQueueExcess() {
            if (queue.size() > capacity) {
                var q = queue.pollFirst();
                if (!minHeap.remove(q)) {
                    if (!maxHeap.remove(q))
                        throw new RuntimeException();
                }
            }
        }
    }

    /**
     * #481
     *
     * @param n
     * @return
     */
    public static int magicalString(int n) {
        if (n <= 3)
            return 1;

        Deque<Integer> deque = new ArrayDeque<>(n);
        deque.add(2);
        boolean isOne = true;
        int l = 3, ans = 1;
        while (l < n) {
            int group = deque.poll();
            if (isOne)
                ans += group;
            deque.add(isOne ? 1 : 2);
            if (group == 2)
                deque.add(isOne ? 1 : 2);
            l += group;
            isOne = !isOne;
        }
        if (l > n && !isOne)
            ans -= l - n;

        return ans;
    }

    /**
     * #482
     *
     * @param s
     * @param k
     * @return
     */
    public static String licenseKeyFormatting(String s, int k) {
        Deque<Character> stack = new ArrayDeque<>(s.length());
        for (int i = 0; i < s.length(); i++) {
            var c = s.charAt(i);
            if (c != '-') {
                if (c >= 'a' && c <= 'z')
                    c -= 'a' - 'A';
                stack.addLast(c);
            }
        }
        StringBuilder sb = new StringBuilder();
        int count = 0;
        while (stack.size() > 0) {
            sb.insert(0, stack.pollLast());
            count++;
            if (count == k && stack.size() != 0) {
                sb.insert(0, "-");
                count = 0;
            }
        }
        return sb.toString();
    }

    /**
     * #483
     *
     * @param n
     * @return
     */
    public static String smallestGoodBase(String n) {
        long N = Long.parseLong(n);
        for (int m = (int) Math.floor(Math.log(N) / Math.log(2)); m > 1; m--) {
            int b = (int) Math.pow(N, 1. / m);
            if (b >= 2 && baseOnes(m, b) == N)
                return String.valueOf(b);
        }
        return String.valueOf(N - 1);
    }

    private static long baseOnes(int m, int b) {
        long mul = 1, sum = 1;
        for (int i = 0; i < m; i++) {
            mul *= b;
            sum += mul;
        }
        return sum;
    }

    /**
     * #485
     *
     * @param nums
     * @return
     */
    public static int findMaxConsecutiveOnes(int[] nums) {
        int max = 0, count = 0;
        for (int i = 0; i < nums.length; i++) {
            var n = nums[i];
            if (n == 1) {
                if (i - 1 >= 0 && nums[i - 1] == 1)
                    count++;
                else
                    count = 1;
            } else
                max = Math.max(max, count);
        }
        max = Math.max(max, count);
        return max;
    }

    /**
     * #493
     *
     * @param nums
     * @return
     */
    public static int reversePairs(int[] nums) {
        return (new ReversePairsSolver()).solve(nums);
    }

    static class ReversePairsSolver {
        private void merge(int[] array, int start, int[] cache1, int[] cache2) {
            int right_idx = 0;
            int left_idx = 0;
            System.arraycopy(array, start, cache1, 0, cache1.length);
            System.arraycopy(array, start + cache1.length, cache2, 0, cache2.length);
            count(cache1, cache2);
            for (int i = start; (i < start + cache1.length + cache2.length) && (right_idx < cache2.length)
                    && (left_idx < cache1.length); i++) {
                if (cache1[left_idx] <= cache2[right_idx]) {
                    array[i] = cache1[left_idx++];
                } else {
                    array[i] = cache2[right_idx++];
                }
            }
            if (left_idx < cache1.length) {
                System.arraycopy(cache1, left_idx, array, start + left_idx + right_idx, cache1.length - left_idx);
            } else if (right_idx < cache2.length) {
                System.arraycopy(cache2, right_idx, array, start + left_idx + right_idx, cache2.length - right_idx);
            }
        }

        private int count = 0;

        private void count(int[] a, int[] b) {
            int i = 0, j = 0;
            while (i < a.length) {
                while (j < b.length && (long) a[i] > 2 * (long) b[j]) {
                    j++;
                }
                count += j;
                i++;
            }
        }

        public int solve(int[] array) {
            mergeSortReversePairs(array, 0, array.length);
            return count;
        }

        private void mergeSortReversePairs(int[] array, int start, int end) {
            if ((end - start) > 1) {
                int middle = (start + end) / 2;
                mergeSortReversePairs(array, start, middle);
                mergeSortReversePairs(array, middle, end);
                int left_len = middle - start;
                int right_len = end - middle;
                var left_cache = new int[left_len];
                var right_cache = new int[right_len];
                merge(array, start, left_cache, right_cache);
            }
        }
    }

    /**
     * #498
     *
     * @param mat
     * @return
     */
    public static int[] findDiagonalOrder(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        int lines = m + n - 1;
        int[] ans = new int[m * n];
        int idx = 0;
        for (int sum = 0; sum < lines; sum++) {
            if (sum % 2 == 0) {
                int i = Math.min(sum, m - 1);
                int j = sum - i;
                while (i >= 0 && j < n)
                    ans[idx++] = mat[i--][j++];
            } else {
                int j = Math.min(sum, n - 1);
                int i = sum - j;
                while (i < m && j >= 0)
                    ans[idx++] = mat[i++][j--];
            }
        }
        return ans;
    }
}
