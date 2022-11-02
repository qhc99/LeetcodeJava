package Leetcode;

import java.util.*;
import java.util.stream.IntStream;

@SuppressWarnings("ALL")
public class Leetcode550 {

    /**
     * #501
     * <br/>
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
            }
            else {
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
            for (int ptr = i + 1; ptr != i; ) {
                if (nums[ptr] > nums[i]) {
                    next[i] = nums[ptr];
                    break;
                }
                else if (next[ptr] > nums[i]) {
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
     * #525
     * <br>
     * 连续数组
     * <br>
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
            }
            else {
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
            }
            else {
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
            }
            else if (score.length - i == 2) {
                ans[idx[i]] = "Silver Medal";
            }
            else if (score.length - i == 3) {
                ans[idx[i]] = "Bronze Medal";
            }
            else {
                ans[idx[i]] = String.valueOf(score.length - i);
            }
        }

        return ans;
    }

    private static void merge(int[] array, int[] idx, int start, int[] cache1, int[] cache2, int[] ci1, int[] ci2) {
        int right_idx = 0;
        int left_idx = 0;
        System.arraycopy(array, start, cache1, 0, cache1.length);
        System.arraycopy(array, start + cache1.length, cache2, 0, cache2.length);

        System.arraycopy(idx, start, ci1, 0, cache1.length);
        System.arraycopy(idx, start + cache1.length, ci2, 0, cache2.length);

        for (int i = start; (i < start + cache1.length + cache2.length) && (right_idx < cache2.length)
                && (left_idx < cache1.length); i++) {
            if (cache1[left_idx] <= cache2[right_idx]) {
                array[i] = cache1[left_idx];
                idx[i] = ci1[left_idx++];
            }
            else {
                array[i] = cache2[right_idx];
                idx[i] = ci2[right_idx++];
            }
        }
        if (left_idx < cache1.length) {
            System.arraycopy(cache1, left_idx, array, start + left_idx + right_idx, cache1.length - left_idx);
            System.arraycopy(ci1, left_idx, idx, start + left_idx + right_idx, cache1.length - left_idx);
        }
        else if (right_idx < cache2.length) {
            System.arraycopy(cache2, right_idx, array, start + left_idx + right_idx, cache2.length - right_idx);
            System.arraycopy(ci2, right_idx, idx, start + left_idx + right_idx, cache2.length - right_idx);
        }
    }

    private static void recursiveMergeSortWithIndex(int[] array, int[] idx) {
        recursiveMergeSortWithIndex(array, idx, 0, array.length);
    }

    private static void recursiveMergeSortWithIndex(int[] array, int[] idx, int start, int end) {
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
            merge(array, idx, start, left_cache, right_cache, left_cache_i, right_cache_i);
        }
    }

    /**
     * #514
     */
    public static int findRotateSteps(String ring, String key) {
        Map<Character, List<Integer>> chrToRingIdx = new HashMap<>(ring.length());
        Set<Character> chrOfKey = new HashSet<>(key.length());
        for (int i = 0; i < key.length(); i++) {
            chrOfKey.add(key.charAt(i));
        }
        for (int i = 0; i < ring.length(); i++) {
            var c = ring.charAt(i);
            if (chrOfKey.contains(c)) {
                chrToRingIdx.computeIfAbsent(c, (arg) -> new ArrayList<>(ring.length())).add(i);
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
                            n.rotates + Math.min(idx_max - idx_min, idx_min + ring.length() - idx_max));
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
            }
            else if (a.length() < b.length()) {
                return 1;
            }
            else {
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
            }
            else {
                var next = jump[i][dc - 'a'];
                if (next != -1) {
                    i = next;
                }
                else {
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

    private static void recursiveSet(int size, int select_size, int i, int layer, BitSet b, int[] F) {
        if (layer == 1) {
            for (int j = i; j < size; j++) {
                b.set(j, true);
                int current = bitsetToInt(b);
                if (select_size == 1) {
                    F[current] += 1;
                }
                else {
                    for (int t = 0; t < size; t++) {
                        if (b.get(t) && ((t + 1) % select_size == 0 || select_size % (t + 1) == 0)) {
                            int child = (int) (current - Math.pow(2, t));
                            F[current] += F[child];
                        }
                    }
                }
                b.clear(j);
            }
        }
        else {
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
            }
            else {
                reverseAppend(start, end, s, sb);
                append(start + k, end + k < s.length() ? end + k : s.length() - 1, s, sb);
            }
        }
        return sb.toString();
    }

    private static void reverseAppend(int start, int end, String s, StringBuilder sb) {
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

    private static void searchAround(Axis a, int[][] mat, Set<Axis> seen, Deque<Axis> deque) {
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
        var left = node.left == null ? new TreeDiamInfo(-1, 0) : recursivediameterOfBinaryTree(node);
        var right = node.right == null ? new TreeDiamInfo(-1, 0) : recursivediameterOfBinaryTree(node);
        var diam = left.max_depth + right.max_depth + 2;
        var child_max_diam = Math.max(left.max_diam, right.max_diam);
        return new TreeDiamInfo(
                left.max_depth > right.max_depth ? left.max_depth + 1 : right.max_depth + 1,
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
}
