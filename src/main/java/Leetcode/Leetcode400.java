package Leetcode;

import java.util.*;

@SuppressWarnings({ "JavaDoc", "Unused" })
public class Leetcode400 {

    /**
     * #354
     *
     * @param envelopes
     * @return
     */
    public static int maxEnvelopes(int[][] envelopes) {
        Arrays.sort(envelopes, (a, b) -> {
            var t = a[0] - b[0];
            if (t != 0) {
                return t;
            } else {
                return b[1] - a[1];
            }
        });
        int[] h = new int[envelopes.length];
        for (int i = 0; i < h.length; i++) {
            h[i] = envelopes[i][1];
        }
        return Leetcode350.lengthOfLIS(h);
    }

    /**
     * #357
     *
     * @param n
     * @return
     */
    public static int countNumbersWithUniqueDigits(int n) {
        int[] ans = new int[] { 0, 10, 91, 739, 5275, 32491, 168571, 712891, 2345851 };
        return ans[n];
    }

    /**
     * #363
     *
     * @param matrix
     * @param k
     * @return
     */
    public static int maxSumSubmatrix(int[][] matrix, int k) {
        int ans = Integer.MIN_VALUE;
        var mat = new RegularizedMatrix(matrix);
        var colAcc = new int[mat.lenRow + 1][mat.lenCol];
        for (int i = 0; i < mat.lenRow; i++) {
            for (int j = 0; j < mat.lenCol; j++) {
                colAcc[i + 1][j] = mat.get(i, j) + colAcc[i][j];
            }
        }

        for (int rs = 0; rs < colAcc.length; rs++) {
            for (int re = rs + 1; re < colAcc.length; re++) {
                int[] colSum = new int[mat.lenCol];
                for (int n = 0; n < mat.lenCol; n++) {
                    colSum[n] = colAcc[re][n] - colAcc[rs][n];
                }
                // b - a <= k, a >= b - k
                int acc = 0;
                TreeSet<Integer> set = new TreeSet<>();
                set.add(0);
                for (var v : colSum) {
                    acc += v;
                    var l = set.ceiling(acc - k);
                    if (l != null) {
                        ans = Math.max(ans, acc - l);
                        if (ans == k) {
                            return ans;
                        }
                    }
                    set.add(acc);
                }
            }
        }
        return ans;
    }

    static class RegularizedMatrix {
        private final int[][] m;
        private final boolean inverse;

        public final int lenRow;
        public final int lenCol;

        RegularizedMatrix(int[][] m) {
            this.m = m;
            inverse = m.length > m[0].length;
            if (inverse) {
                lenCol = m.length;
                lenRow = m[0].length;
            } else {
                lenRow = m.length;
                lenCol = m[0].length;
            }
        }

        int get(int i, int j) {
            if (inverse)
                return m[j][i];
            else
                return m[i][j];
        }

        void set(int i, int j, int v) {
            if (inverse)
                m[j][i] = v;
            else
                m[i][j] = v;
        }
    }

    /**
     * #368
     *
     * @param nums
     * @return
     */
    public static List<Integer> largestDivisibleSubset(int[] nums) {
        class DPData {
            final int pre_idx;
            final int size;

            DPData(int pre, int s) {
                pre_idx = pre;
                size = s;
            }

            @Override
            public String toString() {
                return "idx: " + pre_idx + " size: " + size;
            }
        }

        Arrays.sort(nums);

        DPData[] dp = new DPData[nums.length];
        for (int i = 0; i < dp.length; i++) {
            dp[i] = new DPData(i, 1);
        }

        for (int i = 1; i < nums.length; i++) {
            var i_val = nums[i];
            for (int j = 0; j < i; j++) {
                var j_val = nums[j];
                if (i_val % j_val == 0 && dp[j].size + 1 > dp[i].size) {
                    dp[i] = new DPData(j, dp[j].size + 1);
                }
            }
        }

        DPData max_dp_data = dp[0];
        int max_dp_data_idx = 0;
        for (int i = 0; i < dp.length; i++) {
            var d = dp[i];
            if (d.size > max_dp_data.size) {
                max_dp_data = d;
                max_dp_data_idx = i;
            }
        }
        Deque<Integer> ans = new ArrayDeque<>(max_dp_data.size);

        DPData ptr = max_dp_data;
        for (int i = 0, idx = max_dp_data_idx; i < max_dp_data.size; i++) {
            ans.addFirst(nums[idx]);
            idx = ptr.pre_idx;
            ptr = dp[idx];
        }

        return ans.stream().toList();
    }

    /**
     * #373
     *
     * @param nums1
     * @param nums2
     * @param k
     * @return
     */
    public static List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        @SuppressWarnings("hiding")
        class Data {
            final int idx;
            final int first;
            final int second;
            final int sum;

            Data(int i, int f, int s, int sum) {
                idx = i;
                first = f;
                second = s;
                this.sum = sum;
            }
        }

        PriorityQueue<Data> queue = new PriorityQueue<>(Comparator.comparing(d -> d.sum));
        List<List<Integer>> ans = new ArrayList<>();
        for (int i = 0; i < nums1.length; i++) {
            queue.add(new Data(0, nums1[i], nums2[0], nums1[i] + nums2[0]));
        }
        while (queue.size() > 0 && k > 0) {
            k--;
            var d = queue.poll();
            ans.add(List.of(d.first, d.second));
            if (d.idx + 1 < nums2.length) {
                queue.add(new Data(d.idx + 1, d.first, nums2[d.idx + 1], d.first + nums2[d.idx + 1]));
            }
        }
        return ans;
    }

    /**
     * #375
     *
     * @param n
     * @return
     */
    public static int getMoneyAmount(int n) {
        return getCost(1, n);
    }

    private record IntTuple(int start, int end) {

    }

    private static final HashMap<IntTuple, Integer> cache = new HashMap<>(32);

    private static int getCost(int s, int e) {
        if (s >= e)
            return 0;
        var idx = new IntTuple(s, e);
        var cache_val = cache.get(idx);
        if (cache_val != null)
            return cache_val;
        else {
            int min = Integer.MAX_VALUE;
            for (int i = s; i <= e; i++) {
                var l = getCost(s, i - 1);
                var r = getCost(i + 1, e);
                min = Math.min(min, i + Math.max(l, r));
            }
            cache.put(idx, min);
            return min;
        }
    }

    /**
     * #377
     *
     * @param nums
     * @param target
     * @return
     */
    public static int combinationSum4(int[] nums, int target) {
        int[] dp = new int[target + 1];
        for (var n : nums) {
            if (n < dp.length) {
                dp[n] = 1;
            }
        }
        for (int i = 1; i < dp.length; i++) {
            if (dp[i] > 0) {
                for (var n : nums) {
                    if (i + n < dp.length) {
                        dp[i + n] += dp[i];
                    }
                }
            }
        }
        return dp[target];
    }

    /**
     * #378
     *
     * @param matrix
     * @param k
     * @return
     */
    public static int kthSmallest(int[][] matrix, int k) {
        var n = matrix.length;
        int l = matrix[0][0];
        int r = matrix[n - 1][n - 1];
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (rankGEQ(matrix, mid, k)) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    private static boolean rankGEQ(int[][] matrix, int mid, int k) {
        int i = matrix.length - 1;
        int j = 0;
        int num = 0;
        while (i >= 0 && j < matrix.length) {
            if (matrix[i][j] <= mid) {
                num += i + 1;
                j++;
            } else {
                i--;
            }
        }
        return num >= k;
    }

    /**
     * #380
     */
    public static class RandomizedSet {
        final HashMap<Integer, Integer> idxToVal = new HashMap<>(32);
        final HashMap<Integer, Integer> valToIdx = new HashMap<>(32);
        final Random random = new Random();

        public RandomizedSet() {

        }

        public boolean insert(int val) {
            if (!valToIdx.containsKey(val)) {
                valToIdx.put(val, idxToVal.size());
                idxToVal.put(idxToVal.size(), val);
                return true;
            } else {
                return false;
            }
        }

        public boolean remove(int val) {
            if (valToIdx.containsKey(val)) {
                var val_idx = valToIdx.remove(val);
                var tail_val = idxToVal.remove(idxToVal.size() - 1);
                if (tail_val != val) {
                    valToIdx.put(tail_val, val_idx);
                    idxToVal.put(val_idx, tail_val);
                }
                return true;
            } else
                return false;
        }

        public int getRandom() {
            var idx = random.nextInt(0, idxToVal.size());
            return idxToVal.get(idx);
        }
    }

    /**
     * #381
     * <br/>
     * 设计一个支持在平均 时间复杂度 O(1) 下， 执行以下操作的数据结构。
     * <br/>
     * insert(val)：向集合中插入元素 val。
     * <br/>
     * remove(val)：当 val 存在时，从集合中移除一个 val。
     * <br/>
     * getRandom：从现有集合中随机获取一个元素。每个元素被返回的概率应该与其在集合中的数量呈线性相关。
     */
    public static class RandomizedCollection {
        private final Map<Integer, Set<Integer>> idx;
        private final List<Integer> nums;

        /**
         * Initialize your data structure here.
         */
        public RandomizedCollection() {
            idx = new HashMap<>();
            nums = new ArrayList<>();
        }

        /**
         * Inserts a value to the collection. Returns true if the collection did not
         * already contain the specified element.
         */
        public boolean insert(int val) {
            nums.add(val);
            Set<Integer> set = idx.getOrDefault(val, new HashSet<>());
            set.add(nums.size() - 1);
            idx.put(val, set);
            return set.size() == 1;
        }

        /**
         * Removes a value from the collection. Returns true if the collection contained
         * the specified element.
         */
        public boolean remove(int val) {
            if (!idx.containsKey(val)) {
                return false;
            }
            Iterator<Integer> it = idx.get(val).iterator();
            int i = it.next();
            int lastNum = nums.get(nums.size() - 1);
            nums.set(i, lastNum);
            idx.get(val).remove(i);
            idx.get(lastNum).remove(nums.size() - 1);
            if (i < nums.size() - 1) {
                idx.get(lastNum).add(i);
            }
            if (idx.get(val).size() == 0) {
                idx.remove(val);
            }
            nums.remove(nums.size() - 1);
            return true;
        }

        /**
         * Get a random element from the collection.
         */
        public int getRandom() {
            return nums.get((int) (Math.random() * nums.size()));
        }
    }

    /**
     * #383
     *
     * @param ransomNote
     * @param magazine
     * @return
     */
    public static boolean canConstruct(String ransomNote, String magazine) {
        int[] count_ransomNote = new int['z' - 'a' + 1];
        int[] count_magazine = new int['z' - 'a' + 1];
        var chars_ran = ransomNote.toCharArray();
        var chars_mag = magazine.toCharArray();

        for (int i = 0; i < chars_ran.length; i++) {
            var c = chars_ran[i];
            count_ransomNote[c - 'a']++;
        }
        for (int i = 0; i < chars_mag.length; i++) {
            var c = chars_mag[i];
            count_magazine[c - 'a']++;
        }
        for (int i = 0; i < count_ransomNote.length; i++) {
            if (count_magazine[i] < count_ransomNote[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * #387
     * 
     * @param s
     * @return
     */
    public static int firstUniqChar(String s) {
        int[] counter = new int['z' - 'a' + 1];
        char[] chrs = s.toCharArray();
        for (var c : chrs) {
            counter[c - 'a']++;
        }
        for (int i = 0; i < chrs.length; i++) {
            var c = chrs[i];
            if (counter[c - 'a'] == 1) {
                return i;
            }
        }
        return -1;
    }

    /**
     * #388
     *
     * @param input
     * @return
     */
    public static int lengthLongestPath(String input) {
        var arr = input.split("((?<=\t)|(?=\t)|(?<=\n)|(?=\n))", -1);
        int level = 0;
        List<Integer> dir_stack = new ArrayList<>();
        int max_len = 0;
        for (var s : arr) {
            if (s.equals("\n")) {
                level = 0;
            } else if (s.equals("\t")) {
                level++;
            } else if (s.contains(".")) {
                int dir_len = 0;
                for (int i = 0; i < level; i++) {
                    dir_len += dir_stack.get(i) + 1;
                }
                max_len = Math.max(max_len, dir_len + s.length());
            } else {
                if (level < dir_stack.size()) {
                    dir_stack.set(level, s.length());
                } else {
                    dir_stack.add(s.length());
                }
            }
        }
        return max_len;
    }

    /**
     * #391
     *
     * @param rectangles
     * @return
     */
    public static boolean isRectangleCover(int[][] rectangles) {
        record Point(int X, int Y) {
        }
        int area_sum = 0;
        int whole_area = 0;
        Point left_bottom = null;
        Point right_bottom = null;
        Point left_top = null;
        Point right_top = null;
        {
            var first_rect = rectangles[0];
            int x = first_rect[0], y = first_rect[1], a = first_rect[2], b = first_rect[3];
            left_bottom = new Point(x, y);
            right_top = new Point(a, b);
            left_top = new Point(x, b);
            right_bottom = new Point(a, y);
        }

        Map<Point, Integer> occurrence = new HashMap<>(4 * rectangles.length);
        for (var rect : rectangles) {
            int x = rect[0], y = rect[1], a = rect[2], b = rect[3];
            var lb = new Point(x, y);
            var rt = new Point(a, b);
            var lt = new Point(x, b);
            var rb = new Point(a, y);

            area_sum += (a - x) * (b - y);

            occurrence.put(lb, occurrence.getOrDefault(lb, 0) + 1);
            occurrence.put(rt, occurrence.getOrDefault(rt, 0) + 1);
            occurrence.put(lt, occurrence.getOrDefault(lt, 0) + 1);
            occurrence.put(rb, occurrence.getOrDefault(rb, 0) + 1);

            if (x <= left_bottom.X && y <= left_bottom.Y) {
                left_bottom = lb;
            }
            if (a >= right_top.X && b >= right_top.Y) {
                right_top = rt;
            }
            if (x <= left_top.X && b >= left_top.Y) {
                left_top = lt;
            }
            if (a >= right_bottom.X && y <= right_bottom.Y) {
                right_bottom = rb;
            }
        }

        if (left_bottom.X != left_top.X || (right_top.X != right_bottom.X) ||
                left_bottom.Y != right_bottom.Y || left_top.Y != right_top.Y) {
            return false;
        }

        for (var p_o : occurrence.entrySet()) {
            var p = p_o.getKey();
            var o = p_o.getValue();
            if (!(o == 2 || o == 4)) {
                if (o == 1) {
                    if (!(p.equals(left_bottom) ||
                            p.equals(left_top) ||
                            p.equals(right_bottom) ||
                            p.equals(right_top))) {
                        return false;
                    }
                } else
                    return false;
            }
        }

        whole_area = (right_top.X - left_bottom.X) * (right_top.Y - left_bottom.Y);
        return area_sum == whole_area;
    }

    /**
     * #392
     * s = "abc", t = "ahbgdc"
     * 返回 true.
     * 示例 2:
     * s = "axc", t = "ahbgdc"
     * 返回 false.
     *
     * @param s sub string
     * @param t whole string
     * @return match result
     */
    @SuppressWarnings("SpellCheckingInspection, Unused")
    public static boolean isSubsequence(String s, String t) {
        int a = 0, b = 0;
        int s_len = s.length(), t_len = t.length();
        if (s_len == 0) {
            return true;
        }
        while (b < t_len) {
            if (s.charAt(a) == t.charAt(b)) {
                a++;
                if (a == s_len) {
                    return true;
                }
            }
            b++;
        }
        return false;
    }

    /**
     * #393
     *
     * @param data
     * @return
     */
    public static boolean validUtf8(int[] data) {
        int prev_type = 1;
        int trail_num = 0;
        for (var d : data) {
            var current_type = valType(d);
            if (current_type == 6)
                return false;

            switch (prev_type) {
                case 1 -> {
                    if (current_type >= 5)
                        return false;
                    trail_num = current_type - 1;
                }
                case 2 -> {
                    if (current_type != 5)
                        return false;
                    trail_num = 0;
                }
                case 3 -> {
                    if (current_type != 5)
                        return false;
                    trail_num = 1;
                }
                case 4 -> {
                    if (current_type != 5)
                        return false;
                    trail_num = 2;
                }
                case 5 -> {
                    if (current_type != 5) {
                        if (trail_num != 0)
                            return false;
                    } else {
                        if (trail_num <= 0)
                            return false;
                        trail_num--;
                    }
                }
                default -> {
                    System.out.println(prev_type);
                    throw new RuntimeException("algo error");
                }
            }
            prev_type = current_type;
        }
        return trail_num == 0;
    }

    private static int valType(int d) {
        int range_1_l = 0b00000000, range_1_r = 0b01111111;
        int range_2_l = 0b11000000, range_2_r = 0b11011111;
        int range_3_l = 0b11100000, range_3_r = 0b11101111;
        int range_4_l = 0b11110000, range_4_r = 0b11110111;
        int range_trail_l = 0b10000000, range_trail_r = 0b10111111;
        if (d >= range_1_l && d <= range_1_r) {
            return 1;
        } else if (d >= range_2_l && d <= range_2_r) {
            return 2;
        } else if (d >= range_3_l && d <= range_3_r) {
            return 3;
        } else if (d >= range_4_l && d <= range_4_r) {
            return 4;
        } else if (d >= range_trail_l && d <= range_trail_r) {
            return 5;
        } else {
            return 6;
        }
    }

    /**
     * 394
     *
     * @param s
     * @return
     */
    public static String decodeString(String s) {
        StringBuilder ans = null;
        int idx = 0;
        while (idx < s.length()) { // link early return
            var d = recursiveDecode(s, idx);
            idx = d.end_idx;
            if (ans == null)
                ans = d.builder;
            else
                ans.append(d.builder);
            idx++;
        }
        return ans.toString();
    }

    static class Data {
        final StringBuilder builder;
        final int end_idx;

        Data(StringBuilder sb, int e) {
            builder = sb;
            end_idx = e;
        }
    }

    private static Data recursiveDecode(String s, int idx) {
        var sb = new StringBuilder();
        int num = 0;
        while (idx < s.length()) {
            var ctr = s.charAt(idx);
            switch (ctr) {
                case '[' -> {
                    var d = recursiveDecode(s, ++idx);
                    idx = d.end_idx;
                    sb.append(d.builder.toString().repeat(num));
                    num = 0;
                }
                case ']' -> {
                    return new Data(sb, idx);
                }
                default -> {
                    var op_int = tryParseInt(String.valueOf(ctr));
                    if (op_int.isPresent()) {
                        num = num * 10 + op_int.get();
                    } else {
                        sb.append(ctr);
                    }
                }
            }
            idx++;
        }
        return new Data(sb, idx);
    }

    private static Optional<Integer> tryParseInt(String s) {
        int radix = 10;
        if (s == null) {
            return Optional.empty();
        }

        if (radix < Character.MIN_RADIX) {
            return Optional.empty();
        }

        if (radix > Character.MAX_RADIX) {
            return Optional.empty();
        }

        boolean negative = false;
        int i = 0, len = s.length();
        int limit = -Integer.MAX_VALUE;

        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Integer.MIN_VALUE;
                } else if (firstChar != '+') {
                    return Optional.empty();
                }

                if (len == 1) { // Cannot have lone "+" or "-"
                    return Optional.empty();
                }
                i++;
            }
            int multmin = limit / radix;
            int result = 0;
            while (i < len) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                int digit = Character.digit(s.charAt(i++), radix);
                if (digit < 0 || result < multmin) {
                    return Optional.empty();
                }
                result *= radix;
                if (result < limit + digit) {
                    return Optional.empty();
                }
                result -= digit;
            }
            return Optional.of(negative ? result : -result);
        } else {
            return Optional.empty();
        }
    }

    /**
     * #397
     *
     * @param n
     * @return
     */
    public static int integerReplacement(int n) {
        if (n == 1) {
            return 0;
        }

        if (n % 2 == 0) {
            return 1 + integerReplacement(n / 2);
        } else {
            return 2 + Math.min(integerReplacement(n / 2 + 1), integerReplacement(n / 2));
        }
    }

    /**
     * #399
     *
     * @param equations
     * @param values
     * @param queries
     * @return
     */
    public static double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        Map<String, Integer> symToIdx = new HashMap<>(equations.size() * 2);
        {
            int idx = 0;
            for (int i = 0; i < equations.size(); i++) {
                var eq = equations.get(i);
                var first_symbol = eq.get(0);
                var second_symbol = eq.get(1);
                if (!symToIdx.containsKey(first_symbol)) {
                    symToIdx.put(first_symbol, idx++);
                }
                if (!symToIdx.containsKey(second_symbol)) {
                    symToIdx.put(second_symbol, idx++);
                }
            }
        }
        double[][] graph = new double[symToIdx.size()][symToIdx.size()];
        for (var arr : graph) {
            Arrays.fill(arr, -1);
        }
        for (int i = 0; i < equations.size(); i++) {
            var eq = equations.get(i);
            var first_symbol = eq.get(0);
            var second_symbol = eq.get(1);
            var first_idx = symToIdx.get(first_symbol);
            var second_idx = symToIdx.get(second_symbol);
            graph[first_idx][second_idx] = values[i];
            graph[first_idx][first_idx] = 1;
            graph[second_idx][second_idx] = 1;
            graph[second_idx][first_idx] = 1 / values[i];
        }
        double[] ans = new double[queries.size()];
        var visited = new BitSet(graph.length);
        for (int i = 0; i < queries.size(); i++) {
            var q = queries.get(i);
            var first_symbol = q.get(0);
            var second_symbol = q.get(1);
            if ((!symToIdx.containsKey(first_symbol)) || (!symToIdx.containsKey(second_symbol))) {
                ans[i] = -1;
            } else {
                var start_idx = symToIdx.get(first_symbol);
                var target_idx = symToIdx.get(second_symbol);
                ans[i] = DFS(graph, start_idx, target_idx, visited, 1);
            }
        }
        return ans;
    }

    private static double DFS(
            double[][] graph,
            Integer start_idx,
            Integer target_idx,
            BitSet visited,
            double ans) {
        visited.set(start_idx, true);

        if (graph[start_idx][target_idx] != -1) {
            visited.set(start_idx, false);
            return ans * graph[start_idx][target_idx];
        } else {
            for (int i = 0; i < graph.length; i++) {
                if (!visited.get(i) && graph[start_idx][i] != -1) {
                    var val = DFS(graph, i, target_idx, visited, ans * graph[start_idx][i]);
                    if (val != -1) {
                        visited.set(start_idx, false);
                        return val;
                    }
                }
            }
        }

        visited.set(start_idx, false);
        return -1;
    }
}
