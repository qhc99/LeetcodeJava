package Leetcode;

import java.util.*;
import java.util.stream.IntStream;
import java.util.function.ToDoubleFunction;

@SuppressWarnings("ALL")
public class Leetcode600 {

    /**
     * #501 <br/>
     * 二叉搜索树中的众数
     *
     * @param root tree
     * @return res
     */
    public static int[] findMode(TreeNode root) {
        var s = new Statistic();
        dfs(root, s);
        int[] mode = new int[s.answer.size()];
        for (int i = 0; i < s.answer.size(); ++i) {
            mode[i] = s.answer.get(i);
        }
        return mode;
    }

    public static void dfs(TreeNode o, Statistic solver) {
        if (o == null) {
            return;
        }
        dfs(o.left, solver);
        solver.update(o.val);
        dfs(o.right, solver);
    }

    static class Statistic {

        List<Integer> answer = new ArrayList<>();
        int base, count, maxCount;

        public void update(int x) {
            if (x == base) {
                ++count;
            } else {
                count = 1;
                base = x;
            }
            if (count == maxCount) {
                answer.add(base);
            }
            if (count > maxCount) {
                maxCount = count;
                answer.clear();
                answer.add(base);
            }
        }
    }

    /**
     * #503
     *
     * @param nums
     * @return
     */
    public static int[] nextGreaterElements(int[] nums) {
        int[] next = new int[nums.length];
        Arrays.fill(next, Integer.MIN_VALUE);
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] > nums[nums.length - 1]) {
                next[nums.length - 1] = nums[i];
                break;
            }
        }
        for (int i = nums.length - 2; i >= 0; i--) {
            for (int ptr = i + 1; ptr != i;) {
                if (nums[ptr] > nums[i]) {
                    next[i] = nums[ptr];
                    break;
                } else if (next[ptr] > nums[i]) {
                    next[i] = next[ptr];
                    break;
                }

                ptr++;
                if (ptr == nums.length) {
                    ptr = 0;
                }
            }
        }

        for (int i = 0; i < next.length; i++) {
            if (next[i] == Integer.MIN_VALUE) {
                next[i] = -1;
            }
        }

        return next;
    }

    /**
     * #525 <br>
     * 连续数组 <br>
     * 给定一个二进制数组 nums , 找到含有相同数量的 0 和 1 的最长连续子数组，并返回该子数组的长度。
     *
     * @param nums nums
     * @return len
     */
    public static int findMaxLength(int[] nums) {
        int len = nums.length;
        Map<Integer, Integer> map = new HashMap<>();
        int max = 0;
        for (int i = 0; i == 0; i++) {
            if (nums[i] == 0) {
                nums[i] = -1;
            }
            if (!map.containsKey(nums[i])) {
                map.put(nums[i], i);
            } else {
                max = Math.max(i - map.get(nums[i]), max);
            }
        }
        for (int i = 1; i < len; i++) {
            if (nums[i] == 0) {
                nums[i] = -1;
            }
            nums[i] += nums[i - 1];
            if (nums[i] == 0) {
                max = Math.max(i + 1, max);
            }
            if (!map.containsKey(nums[i])) {
                map.put(nums[i], i);
            } else {
                max = Math.max(i - map.get(nums[i]), max);
            }
        }

        return max;
    }

    /**
     * # 506
     */
    public static String[] findRelativeRanks(int[] score) {
        var idx = IntStream.range(0, score.length).toArray();
        recursiveMergeSortWithIndex(score, idx);
        String[] ans = new String[score.length];
        for (int i = score.length - 1; i >= 0; i--) {
            if (score.length - i == 1) {
                ans[idx[i]] = "Gold Medal";
            } else if (score.length - i == 2) {
                ans[idx[i]] = "Silver Medal";
            } else if (score.length - i == 3) {
                ans[idx[i]] = "Bronze Medal";
            } else {
                ans[idx[i]] = String.valueOf(score.length - i);
            }
        }

        return ans;
    }

    private static void merge(int[] array, int[] idx, int start, int[] cache1,
            int[] cache2, int[] ci1, int[] ci2) {
        int right_idx = 0;
        int left_idx = 0;
        System.arraycopy(array, start, cache1, 0, cache1.length);
        System.arraycopy(array, start + cache1.length, cache2, 0,
                cache2.length);

        System.arraycopy(idx, start, ci1, 0, cache1.length);
        System.arraycopy(idx, start + cache1.length, ci2, 0, cache2.length);

        for (int i = start; (i < start + cache1.length + cache2.length)
                && (right_idx < cache2.length)
                && (left_idx < cache1.length); i++) {
            if (cache1[left_idx] <= cache2[right_idx]) {
                array[i] = cache1[left_idx];
                idx[i] = ci1[left_idx++];
            } else {
                array[i] = cache2[right_idx];
                idx[i] = ci2[right_idx++];
            }
        }
        if (left_idx < cache1.length) {
            System.arraycopy(cache1, left_idx, array,
                    start + left_idx + right_idx, cache1.length - left_idx);
            System.arraycopy(ci1, left_idx, idx, start + left_idx + right_idx,
                    cache1.length - left_idx);
        } else if (right_idx < cache2.length) {
            System.arraycopy(cache2, right_idx, array,
                    start + left_idx + right_idx, cache2.length - right_idx);
            System.arraycopy(ci2, right_idx, idx, start + left_idx + right_idx,
                    cache2.length - right_idx);
        }
    }

    private static void recursiveMergeSortWithIndex(int[] array, int[] idx) {
        recursiveMergeSortWithIndex(array, idx, 0, array.length);
    }

    private static void recursiveMergeSortWithIndex(int[] array, int[] idx,
            int start, int end) {
        if ((end - start) > 1) {
            int middle = (start + end) / 2;
            recursiveMergeSortWithIndex(array, idx, start, middle);
            recursiveMergeSortWithIndex(array, idx, middle, end);
            int left_len = middle - start;
            int right_len = end - middle;
            var left_cache = new int[left_len];
            var right_cache = new int[right_len];
            var left_cache_i = new int[left_len];
            var right_cache_i = new int[right_len];
            merge(array, idx, start, left_cache, right_cache, left_cache_i,
                    right_cache_i);
        }
    }

    /**
     * #514
     */
    public static int findRotateSteps(String ring, String key) {
        Map<Character, List<Integer>> chrToRingIdx = new HashMap<>(
                ring.length());
        Set<Character> chrOfKey = new HashSet<>(key.length());
        for (int i = 0; i < key.length(); i++) {
            chrOfKey.add(key.charAt(i));
        }
        for (int i = 0; i < ring.length(); i++) {
            var c = ring.charAt(i);
            if (chrOfKey.contains(c)) {
                chrToRingIdx.computeIfAbsent(c,
                        (arg) -> new ArrayList<>(ring.length())).add(i);
            }
        }

        NodeFT[] prev = new NodeFT[ring.length()];
        NodeFT[] current = new NodeFT[ring.length()];
        {
            var indices = chrToRingIdx.get(key.charAt(0));
            for (int i = 0; i < indices.size(); i++) {
                int idx = indices.get(i);
                prev[i] = new NodeFT(idx, Math.min(idx, ring.length() - idx));
            }
        }
        for (int i = 1; i < key.length(); i++) {
            var indices = chrToRingIdx.get(key.charAt(i));
            for (int j = 0; j < indices.size(); j++) {
                current[j] = new NodeFT(indices.get(j), Integer.MAX_VALUE);
                var cur = current[j];
                for (int k = 0; k < prev.length && prev[k] != null; k++) {
                    var n = prev[k];
                    int idx1 = n.idx_ring;
                    int idx2 = indices.get(j);
                    int idx_max = Math.max(idx1, idx2);
                    int idx_min = Math.min(idx1, idx2);
                    cur.rotates = Math.min(cur.rotates,
                            n.rotates + Math.min(idx_max - idx_min,
                                    idx_min + ring.length() - idx_max));
                    if (j == indices.size() - 1) {
                        prev[k] = null;
                    }
                }
            }
            var t = prev;
            prev = current;
            current = t;
        }

        int global_min = Integer.MAX_VALUE;
        for (int i = 0; i < prev.length && prev[i] != null; i++) {
            global_min = Math.min(prev[i].rotates, global_min);
        }

        return global_min + key.length();
    }

    /**
     * Node for freedom trail
     */
    private static class NodeFT {

        final int idx_ring;
        int rotates;

        NodeFT(int i_r, int r) {
            idx_ring = i_r;
            rotates = r;
        }
    }

    /**
     * #518
     * 
     * @param amount
     * @param coins
     * @return
     */
    public int change(int amount, int[] coins) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        for (var c : coins) {
            for (int i = 0; c + i < dp.length; i++) {
                dp[c + i] += dp[i];
            }
        }
        return dp[amount];
    }

    /**
     * #521
     *
     * @param a
     * @param b
     * @return
     */
    public static int findLUSlength(String a, String b) {
        return a.equals(b) ? -1 : b.length();
    }

    /**
     * #522
     *
     * @param strs
     * @return
     */
    public static int findLUSlength(String[] strs) {
        Arrays.sort(strs, Comparator.comparing(String::length).reversed());
        Set<String> non_solution = new HashSet<>(strs.length);
        for (int i = 0; i < strs.length; i++) {
            var s = strs[i];
            if (!non_solution.contains(s)) {
                for (int j = i + 1; j < strs.length; j++) {
                    var s1 = strs[j];
                    if (isSub(s1, s)) {
                        non_solution.add(s1);
                    }
                }
                if (!non_solution.contains(s)) {
                    return s.length();
                }
            }
        }

        return -1;
    }

    private static boolean isSub(String sub, String full) {
        for (int i = 0, j = 0; j < full.length(); j++) {
            var c = sub.charAt(i);
            var cc = full.charAt(j);
            if (c == cc) {
                i++;
                if (i == sub.length()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * #524
     *
     * @param s
     * @param dictionary
     * @return
     */
    public static String findLongestWord(String s, List<String> dictionary) {
        dictionary = new ArrayList<>(dictionary);
        dictionary.sort((a, b) -> {
            if (a.length() > b.length()) {
                return -1;
            } else if (a.length() < b.length()) {
                return 1;
            } else {
                return a.compareTo(b);
            }
        });
        var jump = preprocess(s);
        for (var d : dictionary) {
            if (isSub(s, d, jump)) {
                return d;
            }
        }
        return "";
    }

    private static boolean isSub(String s, String sub, int[][] jump) {
        int i = 0;
        int j = 0;
        while (i < s.length() && j < sub.length()) {
            var c = s.charAt(i);
            var dc = sub.charAt(j);
            if (c == dc) {
                i++;
                j++;
            } else {
                var next = jump[i][dc - 'a'];
                if (next != -1) {
                    i = next;
                } else {
                    return false;
                }
            }
        }
        return j == sub.length();
    }

    private static int[][] preprocess(String s) {
        int count = 'z' - 'a' + 1;
        int[][] ans = new int[s.length()][count];
        int[] arr = new int[count];
        Arrays.fill(arr, -1);
        for (int i = s.length() - 1; i >= 0; i--) {
            int[] copy = new int[count];
            System.arraycopy(arr, 0, copy, 0, count);
            ans[i] = copy;
            var c = s.charAt(i);
            arr[c - 'a'] = i;
        }
        return ans;
    }

    /**
     * #526
     *
     * @param n
     * @return
     */
    public static int countArrangement(int n) {
        int[] F = new int[(int) Math.pow(2, n)];
        BitSet b = new BitSet(n);
        for (int l = 1; l <= n; l++) {
            recursiveSet(n, l, 0, l, b, F);
        }
        return F[F.length - 1];
    }

    private static void recursiveSet(int size, int select_size, int i,
            int layer, BitSet b, int[] F) {
        if (layer == 1) {
            for (int j = i; j < size; j++) {
                b.set(j, true);
                int current = bitsetToInt(b);
                if (select_size == 1) {
                    F[current] += 1;
                } else {
                    for (int t = 0; t < size; t++) {
                        if (b.get(t) && ((t + 1) % select_size == 0
                                || select_size % (t + 1) == 0)) {
                            int child = (int) (current - Math.pow(2, t));
                            F[current] += F[child];
                        }
                    }
                }
                b.clear(j);
            }
        } else {
            for (int j = i; j < size; j++) {
                b.set(j, true);
                recursiveSet(size, select_size, j + 1, layer - 1, b, F);
                b.clear(j);
            }
        }
    }

    private static int bitsetToInt(BitSet bits) {
        int value = 0;
        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? (1 << i) : 0;
        }
        return value;
    }

    /**
     * #538 二叉搜索树转换为累加树
     *
     * <pre>
     * 输入: 原始二叉搜索树:
     *               5
     *             /   \
     *            2     13
     *
     * 输出: 转换为累加树:
     *              18
     *             /   \
     *           20     13
     * </pre>
     *
     * @param root tree root
     * @return converted tree
     */
    public static TreeNode convertBST(TreeNode root) {
        traverseAndConvertBST(root, 0);
        return root;
    }

    private static int traverseAndConvertBST(TreeNode r, int sum) {
        if (r == null) {
            return sum;
        }
        sum = traverseAndConvertBST(r.right, sum);
        r.val += sum;
        sum = r.val;
        sum = traverseAndConvertBST(r.left, sum);
        return sum;
    }

    /**
     * #541
     *
     * @param s
     * @param k
     * @return
     */
    public static String reverseStr(String s, int k) {
        int start = 0, end = k - 1;
        StringBuilder sb = new StringBuilder();
        for (; start < s.length(); start += 2 * k, end += 2 * k) {
            if (end >= s.length()) {
                end = s.length() - 1;
                reverseAppend(start, end, s, sb);
                break;
            } else {
                reverseAppend(start, end, s, sb);
                append(start + k,
                        end + k < s.length() ? end + k : s.length() - 1, s, sb);
            }
        }
        return sb.toString();
    }

    private static void reverseAppend(int start, int end, String s,
            StringBuilder sb) {
        for (int i = end; i >= start; i--) {
            sb.append(s.charAt(i));
        }
    }

    private static void append(int start, int end, String s, StringBuilder sb) {
        for (int i = start; i <= end; i++) {
            sb.append(s.charAt(i));
        }
    }

    /**
     * #542
     *
     * @param mat
     * @return
     */
    public static int[][] updateMatrix(int[][] mat) {
        Deque<Axis> axisDeque = new ArrayDeque<>(mat.length * mat[0].length);
        Set<Axis> seen = new HashSet<>(mat.length * mat[0].length);
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                if (mat[i][j] == 0) {
                    var t = new Axis(i, j, 0);
                    seen.add(t);
                    axisDeque.addLast(t);
                }
            }
        }
        while (!axisDeque.isEmpty()) {
            var a = axisDeque.pollFirst();
            searchAround(a, mat, seen, axisDeque);
        }
        return mat;
    }

    private static void searchAround(Axis a, int[][] mat, Set<Axis> seen,
            Deque<Axis> deque) {
        int x = a.x;
        int y = a.y;
        int l = a.layer;
        if (x - 1 >= 0) {
            var t = new Axis(x - 1, y, l + 1);
            if (!seen.contains(t)) {
                mat[x - 1][y] = l + 1;
                deque.add(t);
                seen.add(t);
            }
        }
        if (y - 1 >= 0) {
            var t = new Axis(x, y - 1, l + 1);
            if (!seen.contains(t)) {
                mat[x][y - 1] = l + 1;
                deque.add(t);
                seen.add(t);
            }
        }
        if (x + 1 < mat.length) {
            var t = new Axis(x + 1, y, l + 1);
            if (!seen.contains(t)) {
                mat[x + 1][y] = l + 1;
                deque.add(t);
                seen.add(t);
            }
        }
        if (y + 1 < mat[0].length) {
            var t = new Axis(x, y + 1, l + 1);
            if (!seen.contains(t)) {
                mat[x][y + 1] = l + 1;
                deque.add(t);
                seen.add(t);
            }
        }
    }

    private static class Axis {

        final int layer;
        final int x;
        final int y;

        Axis(int x, int y, int l) {
            this.x = x;
            this.y = y;
            layer = l;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Axis axis = (Axis) o;
            return x == axis.x && y == axis.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    /**
     * #543
     *
     * @param root
     * @return
     */
    public static int diameterOfBinaryTree(TreeNode root) {
        return recursivediameterOfBinaryTree(root).max_diam;
    }

    public static TreeDiamInfo recursivediameterOfBinaryTree(TreeNode node) {
        var left = node.left == null ? new TreeDiamInfo(-1, 0)
                : recursivediameterOfBinaryTree(node);
        var right = node.right == null ? new TreeDiamInfo(-1, 0)
                : recursivediameterOfBinaryTree(node);
        var diam = left.max_depth + right.max_depth + 2;
        var child_max_diam = Math.max(left.max_diam, right.max_diam);
        return new TreeDiamInfo(
                left.max_depth > right.max_depth ? left.max_depth + 1
                        : right.max_depth + 1,
                Math.max(child_max_diam, diam));
    }

    private static class TreeDiamInfo {
        final int max_depth;
        final int max_diam;

        TreeDiamInfo(int depth, int max) {
            this.max_depth = depth;
            max_diam = max;
        }
    }
    /**
     * #551
     *
     * @param s
     * @return
     */
    public static boolean checkRecord(String s) {
        int a = 0, l = 0;
        for (int i = 0; i < s.length(); i++) {
            var c = s.charAt(i);
            if (c == 'A') {
                a++;
                l++;
                if (a == 2)
                    return false;
            } else if (c == 'L') {
                l++;
                if (l == 3)
                    return false;
            } else
                l = 0;
        }
        return true;
    }

    /**
     * #552
     *
     * @param n
     * @return
     */
    public static int checkRecord(int n) {
        // (0 A) P, L, LL, (1 A) P, L, LL, A
        long[] dp = new long[7];
        dp[0] = 1;
        dp[1] = 1;
        dp[6] = 1;
        int mod = 1_000_000_000 + 7;
        for (int i = 2; i <= n; i++) {
            var zero_A_P = dp[0];
            var zero_A_L = dp[1];
            var zero_A_LL = dp[2];
            var one_A_P = dp[3];
            var one_A_L = dp[4];
            var one_A_LL = dp[5];
            var one_A_A = dp[6];
            dp[0] = (zero_A_P + zero_A_L + zero_A_LL) % mod;
            dp[1] = zero_A_P;
            dp[2] = zero_A_L;
            dp[3] = (one_A_P + one_A_L + one_A_LL + one_A_A) % mod;
            dp[4] = (one_A_P + one_A_A) % mod;
            dp[5] = one_A_L;
            dp[6] = dp[0];
        }
        long ans = 0;
        for (var i : dp) {
            ans += i;
        }
        return (int) (ans % mod);
    }

    /**
     * #560 <br>
     * find the count of continue sub-arrays which sum is k<br>
     * [1, 2, 3, 4], 3 ---> 2 (answer: [1, 2] and [3])
     *
     * @param nums array
     * @param k    sum target
     * @return count
     */
    public static int subarraySum(int[] nums, int k) {
        int count = 0, pre = 0;
        HashMap<Integer, Integer> mp = new HashMap<>();
        mp.put(0, 1);
        for (int num : nums) {
            pre += num;
            if (mp.containsKey(pre - k)) {
                count += mp.get(pre - k);
            }
            mp.put(pre, mp.getOrDefault(pre, 0) + 1);
        }
        return count;
    }

    /**
     * #567
     * 
     * @param s1
     * @param s2
     * @return
     */
    public boolean checkInclusion(String s1, String s2) {
        Map<Character, Integer> target = new HashMap<>(s1.length());
        for (var c : s1.toCharArray()) {
            target.put(c, target.getOrDefault(c, 0) + 1);
        }
        Map<Character, Integer> window = new HashMap<>(s1.length());
        int l = -1;
        for (int r = 0; r < s2.length(); r++) {
            var c = s2.charAt(r);
            if (target.containsKey(c)
                    && window.getOrDefault(c, 0) + 1 <= target.get(c)) {
                if (l == -1) {
                    l = r;
                }
                window.put(c, window.getOrDefault(c, 0) + 1);
                if (r - l + 1 == s1.length())
                    return true;
            } else if (l != -1) {
                if (!target.containsKey(c)) {
                    l = -1;
                    window.clear();
                } else {
                    while (l < r
                            && window.getOrDefault(c, 0) + 1 > target.get(c)) {
                        window.put(s2.charAt(l), window.get(s2.charAt(l)) - 1);
                        l++;
                    }
                    window.put(c, window.getOrDefault(c, 0) + 1);
                }
            }

        }
        return false;
    }

    /**
     * #583
     *
     * @param word1
     * @param word2
     * @return
     */
    public static int minDistance(String word1, String word2) {
        return word1.length() + word2.length() - 2 * maxCommonLen(word1, word2);
    }

    private static int maxCommonLen(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 1; i < a.length() + 1; i++) {
            var ac = a.charAt(i - 1);
            for (int j = 1; j < b.length() + 1; j++) {
                var bc = b.charAt(j - 1);
                if (ac == bc)
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                else
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }
        return dp[a.length()][b.length()];
    }

    /**
     * #587
     *
     * @param trees
     * @return
     */
    public static int[][] outerTrees(int[][] trees) {
        int n = trees.length;
        if (n < 4) {
            return trees;
        }
        int bottom = 0;
        /* 找到 y 最小的点 bottom */
        for (int i = 0; i < n; i++) {
            if (trees[i][1] < trees[bottom][1]) {
                bottom = i;
            }
        }
        swap(trees, bottom, 0);
        /* 以 bottom 原点，按照极坐标的角度大小进行排序 */
        Arrays.sort(trees, 1, n, (a, b) -> {
            int diff = ccw(trees[0], a, b);
            if (diff == 0) {
                return distance(trees[0], a) - distance(trees[0], b);
            } else {
                return -diff;
            }
        });
        /* 对于凸包最后且在同一条直线的元素按照距离从大到小进行排序 */
        int r = n - 1;
        while (r >= 0 && ccw(trees[0], trees[n - 1], trees[r]) == 0) {
            r--;
        }
        for (int l = r + 1, h = n - 1; l < h; l++, h--) {
            swap(trees, l, h);
        }
        Deque<Integer> stack = new ArrayDeque<Integer>();
        stack.push(0);
        stack.push(1);
        for (int i = 2; i < n; i++) {
            int top = stack.pop();
            /* 如果当前元素与栈顶的两个元素构成的向量顺时针旋转，则弹出栈顶元素 */
            while (!stack.isEmpty()
                    && ccw(trees[stack.peek()], trees[top], trees[i]) < 0) {
                top = stack.pop();
            }
            stack.push(top);
            stack.push(i);
        }

        int size = stack.size();
        int[][] res = new int[size][2];
        for (int i = 0; i < size; i++) {
            res[i] = trees[stack.pop()];
        }
        return res;
    }

    private static int ccw(int[] a, int[] b, int[] c) {
        // (qx - px) * (ry - py) - (by - ay) * (cx - bx);
        return (b[0] - a[0]) * (c[1] - b[1]) - (b[1] - a[1]) * (c[0] - b[0]);
    }

    private static int distance(int[] p, int[] q) {
        return (p[0] - q[0]) * (p[0] - q[0]) + (p[1] - q[1]) * (p[1] - q[1]);
    }

    private static void swap(int[][] trees, int i, int j) {
        int temp0 = trees[i][0], temp1 = trees[i][1];
        trees[i][0] = trees[j][0];
        trees[i][1] = trees[j][1];
        trees[j][0] = temp0;
        trees[j][1] = temp1;
    }

    public static int[][] outerTrees2(int[][] trees) {
        List<int[]> ps = new ArrayList<>(trees.length);
        Collections.addAll(ps, trees);
        var out = ConvexHull.GrahamScan(ps, i -> i[0], i -> i[1]);
        int[][] ans = new int[out.size()][2];
        for (int i = 0; i < out.size(); i++) {
            ans[i] = out.get(i);
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

}
