package Leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("ALL")
public class Leetcode700 {

    /**
     * #605
     *
     * @param flowerbed
     * @param n
     * @return
     */
    public boolean canPlaceFlowers(int[] flowerbed, int n) {
        if (n == 0) {
            return true;
        }
        for (int i = 0; i < flowerbed.length; i++) {
            var l = i == 0 || flowerbed[i - 1] == 0;
            var r = i == flowerbed.length - 1 || flowerbed[i + 1] == 0;
            if (l && r && flowerbed[i] == 0) {
                n--;
                if (n == 0) {
                    return true;
                }
                flowerbed[i] = 1;
            }
        }
        return n == 0;
    }

    /**
     * #621
     *
     * @param tasks
     * @param n
     * @return
     */
    public int leastInterval(char[] tasks, int n) {
        Map<Character, Integer> latestPlaces = new HashMap<>();
        Map<Character, Integer> count = new HashMap<>();
        PriorityQueue<Character> queue = new PriorityQueue<>((a, b) -> {
            var c1 = count.get(a);
            var c2 = count.get(b);
            var diff = c2 - c1;
            if (diff != 0) {
                return diff; // Larger count

            }
            return a - b; // lex
        });

        for (var c : tasks) {
            count.put(c, count.getOrDefault(c, 0) + 1);
        }

        for (var k : count.keySet()) {
            queue.add(k);
        }
        int i = 1;
        Deque<Character> stack = new ArrayDeque<>();
        while (!count.isEmpty()) {
            stack.clear();
            while (!queue.isEmpty()) {
                var t = queue.poll();
                var idx = latestPlaces.getOrDefault(t, -n);
                var dist = i - idx;
                if (dist <= n) {
                    stack.add(t);
                    continue;
                }
                int c = count.get(t);
                if (c == 1) {
                    count.remove(t);
                } else {
                    count.put(t, c - 1);
                }
                if (c != 1) {
                    queue.add(t);
                }
                latestPlaces.put(t, i);
                break;
            }
            queue.addAll(stack);
            i++;
        }

        return i - 1;
    }

    /**
     * #622 MyCircularQueue
     */
    class MyCircularQueue {

        int[] arr;
        int head = 0, tail = 0;
        boolean isEmpty = true;

        public MyCircularQueue(int k) {
            arr = new int[k];
        }

        public boolean enQueue(int value) {
            if (isFull()) {
                return false;
            }
            isEmpty = false;
            arr[tail] = value;
            tail = (tail + 1) % arr.length;
            return true;
        }

        public boolean deQueue() {
            if (isEmpty()) {
                return false;
            }
            head = (head + 1) % arr.length;
            if (head == tail) {
                isEmpty = true;
            }
            return true;
        }

        public int Front() {
            if (isEmpty()) {
                return -1;
            }
            return arr[head];
        }

        public int Rear() {
            if (isEmpty()) {
                return -1;
            }
            return arr[(tail - 1 + arr.length) % arr.length];
        }

        public boolean isEmpty() {
            return head == tail && isEmpty;
        }

        public boolean isFull() {
            return head == tail && !isEmpty;
        }
    }

    /**
     * #628
     *
     * @param nums
     * @return
     */
    public int maximumProduct(int[] nums) {
        int[][] dp = new int[3][2];
        int len = 0;
        dp[0][0] = dp[1][0] = dp[2][0] = Integer.MAX_VALUE;
        dp[0][1] = dp[1][1] = dp[2][1] = Integer.MIN_VALUE;
        for (var n : nums) {
            if (len >= 2) {
                dp[2][0] = Math.min(Math.min(n * dp[1][0], n * dp[1][1]),
                        dp[2][0]);
                dp[2][1] = Math.max(Math.max(n * dp[1][0], n * dp[1][1]),
                        dp[2][1]);
            }
            if (len >= 1) {
                dp[1][0] = Math.min(Math.min(n * dp[0][0], n * dp[0][1]),
                        dp[1][0]);
                dp[1][1] = Math.max(Math.max(n * dp[0][0], n * dp[0][1]),
                        dp[1][1]);
                if (len == 1) {
                    len++;
                }
            }
            if (len >= 0) {
                dp[0][0] = Math.min(dp[0][0], n);
                dp[0][1] = Math.max(dp[0][1], n);
                if (len == 0) {
                    len++;
                }
            }
        }
        return dp[2][1];
    }

    /**
     * #631
     */
    class Excel {

        static record Cell(int row, int column) {

            @Override
            public final int hashCode() {
                return Objects.hash(row, column);
            }

            @Override
            public final boolean equals(Object arg0) {
                if (arg0 instanceof Cell o) {
                    return o.row == row && o.column == column;
                }
                return false;
            }
        }

        static record Range(int x1, int y1, int x2, int y2) {

        }

        int[][] mat;
        Map<Cell, List<Range>> formulas = new HashMap<>();

        public Excel(int height, char width) {
            mat = new int[height][width];
        }

        public void set(int row, char column, int val) {
            row--;
            int col = column - 'A';
            var cell = new Cell(row, col);
            if (formulas.containsKey(cell)) {
                formulas.remove(cell);
            }
            updateCells(row, col, val - mat[row][col]);
        }

        void updateCells(int row, int column, int diff) {
            mat[row][column] += diff;
            for (var e : formulas.entrySet()) {
                var c = e.getKey();
                var rs = e.getValue();
                for (var r : rs) {
                    if (row >= r.x1 && row <= r.x2 && column >= r.y1
                            && column <= r.y2) {
                        updateCells(c.row, c.column, diff);
                    }
                }
            }
        }

        public int get(int row, char column) {
            row--;
            int col = column - 'A';
            return mat[row][col];
        }

        public int sum(int row, char column, String[] numbers) {
            row--;
            int col = column - 'A';

            var cell = new Cell(row, col);
            List<Range> ranges = new ArrayList<>();
            int new_val = 0;
            for (var n : numbers) {
                if (!n.contains(":")) {
                    var y = (int) (n.charAt(0) - 'A');
                    var x = Integer.valueOf(n.substring(1)) - 1;
                    ranges.add(new Range(x, y, x, y));
                    new_val += mat[x][y];
                } else {
                    var cs = n.split(":");
                    var y1 = (int) (cs[0].charAt(0) - 'A');
                    var x1 = Integer.valueOf(cs[0].substring(1)) - 1;
                    var y2 = (int) (cs[1].charAt(0) - 'A');
                    var x2 = Integer.valueOf(cs[1].substring(1)) - 1;
                    ranges.add(new Range(x1, y1, x2, y2));
                    for (int i = x1; i <= x2; i++) {
                        for (int j = y1; j <= y2; j++) {
                            new_val += mat[i][j];
                        }
                    }
                }
            }
            formulas.put(cell, ranges);
            updateCells(row, col, new_val - mat[row][col]);
            return mat[row][col];
        }

    }

    /**
     * #632
     *
     * @param nums
     * @return
     */
    public int[] smallestRange(List<List<Integer>> nums) {
        int[] res = new int[2];
        res[0] = 0;
        res[1] = Integer.MAX_VALUE;
        List<Integer> idx = new ArrayList<>();
        int max = Integer.MIN_VALUE;
        for (var arr : nums) {
            max = Math.max(max, arr.get(0));
            idx.add(0);
        }
        PriorityQueue<Integer> queue = new PriorityQueue<Integer>(
                (i, j) -> nums.get(i).get(idx.get(i))
                        - nums.get(j).get(idx.get(j)));
        for (int i = 0; i < nums.size(); i++) {
            queue.add(i);
        }
        while (true) {
            var row = queue.poll();
            var i = idx.get(row);
            var val = nums.get(row).get(i);
            if (((max - val) < (res[1] - res[0]))
                    || (val < res[0] && (max - val) == (res[1] - res[0]))) {
                res[0] = val;
                res[1] = max;
            }
            i++;
            if (i >= nums.get(row).size()) {
                break;
            }
            idx.set(row, i);
            max = Math.max(max, nums.get(row).get(i));
            queue.add(row);
        }
        return res;
    }

    /**
     * #635 LogSystem
     */
    class LogSystem {

        TreeMap<String, List<Integer>> map = new TreeMap<>();
        Map<String, Integer> idx = Map.of("Year", 1, "Month", 2, "Day", 3,
                "Hour", 4, "Minute", 5, "Second", 6);
        String[] min = new String[] { "2000", "01", "01", "00", "00", "00" };
        String[] max = new String[] { "2017", "12", "31", "23", "59", "59" };

        public LogSystem() {

        }

        public void put(int id, String timestamp) {
            map.computeIfAbsent(timestamp, k -> new ArrayList<>()).add(id);
        }

        public List<Integer> retrieve(String start, String end,
                String granularity) {
            var t = map
                    .subMap(floor(start, granularity), true,
                            ceil(end, granularity), true)
                    .sequencedEntrySet().stream().map(e -> e.getValue())
                    .flatMap(l -> l.stream()).toList();
            return t;
        }

        String floor(String s, String granularity) {
            var arr = s.split(":");
            for (int i = idx.get(granularity); i < arr.length; i++) {
                arr[i] = min[i];
            }
            return String.join(":", arr);
        }

        String ceil(String s, String granularity) {
            var arr = s.split(":");
            for (int i = idx.get(granularity); i < arr.length; i++) {
                arr[i] = max[i];
            }
            return String.join(":", arr);
        }
    }

    /**
     * #636
     *
     * @param n
     * @param logs
     * @return
     */
    public int[] exclusiveTime(int n, List<String> logs) {
        int[] res = new int[n];
        Stack<Integer> start = new Stack<>();
        int time = 0;
        for (var log : logs) {
            var arr = log.split(":");
            var id = Integer.valueOf(arr[0]);
            var state = arr[1];
            var t = Integer.valueOf(arr[2]);
            var isEnd = state.equals("end");
            if (isEnd) {
                t++;
            }
            var v = t - time;
            if (!start.isEmpty()) {
                res[start.peek()] += v;
            }
            time = t;
            if (!isEnd) {
                start.add(id);
            } else {
                start.pop();
            }

        }
        return res;
    }

    /**
     * #638
     *
     * @param price
     * @param special
     * @param needs
     * @return
     */
    public static int shoppingOffers(List<Integer> price,
            List<List<Integer>> special, List<Integer> needs) {
        List<List<Integer>> valid_special = new ArrayList<>(special.size());
        for (var spec : special) {
            if (spec.get(spec.size() - 1) < non_special_price_of(
                    spec.subList(0, spec.size() - 1), price)) {
                valid_special.add(spec);
            }
        }
        Map<String, Integer> cache = new HashMap<>(1024);
        return min_offer_of(needs, price, valid_special, cache);
    }

    private static int min_offer_of(List<Integer> current_needs,
            List<Integer> price, List<List<Integer>> special,
            Map<String, Integer> cache) {
        var str_current_needs = current_needs.toString();
        if (cache.containsKey(str_current_needs)) {
            return cache.get(str_current_needs);
        } else {
            int non_spec_price = non_special_price_of(current_needs, price);
            int min_spec_price = Integer.MAX_VALUE;
            for (var spec : special) {
                var new_need = new_needs(current_needs, spec);
                if (new_need != null) {
                    min_spec_price = Math.min(min_spec_price,
                            min_offer_of(new_need, price, special, cache)
                                    + spec.get(spec.size() - 1));
                }
            }
            var ans = Math.min(non_spec_price, min_spec_price);
            cache.put(str_current_needs, ans);
            return ans;
        }
    }

    private static List<Integer> new_needs(List<Integer> current_needs,
            List<Integer> spec) {
        List<Integer> n = new ArrayList<>(current_needs.size());
        for (int i = 0; i < current_needs.size(); i++) {
            var n_i = current_needs.get(i) - spec.get(i);
            if (n_i >= 0) {
                n.add(n_i);
            } else {
                return null;
            }
        }
        return n;
    }

    private static int non_special_price_of(List<Integer> needs,
            List<Integer> price) {
        int ans = 0;
        for (int i = 0; i < needs.size(); i++) {
            ans += needs.get(i) * price.get(i);
        }
        return ans;
    }

    /**
     * #642 AutocompleteSystem
     */
    public static final class AutocompleteSystem {

        static class Node {

            List<StFreq> idx = new ArrayList<>();
            Map<Character, Node> children = new HashMap<>();
        }

        static class StFreq implements Comparable<Object> {

            String sentence;
            int freq;

            public StFreq(String s, int f) {
                sentence = s;
                freq = f;
            }

            @Override
            public int compareTo(Object o) {
                if (o instanceof StFreq other) {
                    var c = Integer.compare(other.freq, freq);
                    if (c != 0) {
                        return c;
                    }
                    return sentence.compareTo(other.sentence);
                }
                throw new UnsupportedOperationException("Not supported yet.");
            }

        }

        Node root = new Node();
        Node ptr = root;
        StringBuilder builder = new StringBuilder();

        public AutocompleteSystem(String[] sentences, int[] times) {
            List<StFreq> list = new ArrayList<>();
            for (int i = 0; i < sentences.length; i++) {
                list.add(new StFreq(sentences[i], times[i]));
            }

            for (var sf : list) {
                append(sf);
            }
        }

        void append(StFreq sf) {
            var n = root;
            for (var c : sf.sentence.toCharArray()) {
                n = n.children.computeIfAbsent(c, k -> new Node());
                boolean added = false;
                for (var t : n.idx) {
                    if (t.sentence.equals(sf.sentence)) {
                        t.freq += sf.freq;
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    n.idx.add(new StFreq(sf.sentence, sf.freq));
                }
            }
        }

        public List<String> input(char c) {
            if (c == '#') {
                ptr = root;
                append(new StFreq(builder.toString(), 1));
                builder.delete(0, builder.length());
                return List.of();
            }
            builder.append(c);
            ptr = ptr.children.computeIfAbsent(c, k -> new Node());
            Queue<StFreq> queue = new PriorityQueue<>();
            queue.addAll(ptr.idx);
            List<String> res = new ArrayList<>();
            while (!queue.isEmpty() && res.size() < 3) {
                res.add(queue.poll().sentence);
            }
            return res;

        }
    }

    /**
     * #643
     *
     * @param nums
     * @param k
     * @return
     */
    public static double findMaxAverage(int[] nums, int k) {
        int sum = 0;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (i >= k) {
                sum -= nums[i - k];
            }
            if (i + 1 >= k) {
                max = Math.max(sum, max);
            }

        }
        return max / (double) k;
    }

    /**
     * #647
     *
     * @param s
     * @return
     */
    public int countSubstrings2(String s) {
        char[] str = new char[2 * s.length() - 1];
        for (int i = 0; i < str.length; i++) {
            if (i % 2 == 0) {
                str[i] = s.charAt(i / 2);
            } else {
                str[i] = '$';
            }
        }
        int[] manacher = new int[str.length];
        int mid = 0, r = 0;
        Arrays.fill(manacher, 1);
        for (int i = 0; i < str.length; i++) {
            if (i < r) {
                var mi = 2 * mid - i;
                var l = 2 * mid - r;
                manacher[i] = Math.min(manacher[mi], mi - l);
            }
            var len = manacher[i];
            while (i + len < str.length && i - len >= 0
                    && str[i + len] == str[i - len]) {
                len++;
            }
            manacher[i] = len;
            if (i + len > r) {
                mid = i;
                r = len;
            }
        }
        int res = 0;
        for (int i = 0; i < str.length; i++) {
            var len = manacher[i];
            if (str[i] == '$') {
                len--;
            }
            if (str[i + manacher[i] - 1] == '$') {
                len--;
            }
            res += (len + 1) / 2;
        }
        return res;
    }

    /**
     * #650
     * 
     * @param n
     * @return
     */
    public int minSteps(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = dp[1] = 0;
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j <= Math.sqrt(i); j++) {
                if (i % j == 0) {
                    dp[i] = Math.min(dp[i], dp[j] + i / j);
                    dp[i] = Math.min(dp[i], dp[i / j] + j);
                }
            }
        }
        return dp[n];
    }

    /**
     * #652
     *
     * @param root
     * @return
     */
    public static List<TreeNode> findDuplicateSubtrees(TreeNode root) {
        Map<String, TreeNode> map = new HashMap<>(32);
        scanTree(root, map);
        return map.values().stream().filter(Objects::nonNull).toList();
    }

    private static String scanTree(TreeNode n, Map<String, TreeNode> str2node) {
        if (n == null) {
            return "null";
        }

        var l = scanTree(n.left, str2node);
        var r = scanTree(n.right, str2node);
        var s = "(" + l + "," + n.val + "," + r + ")";
        if (!str2node.containsKey(s)) {
            str2node.put(s, null);
        } else {
            str2node.put(s, n);
        }
        return s;
    }

    /**
     * #658
     *
     * @param arr
     * @param k
     * @param x
     * @return
     */
    public static List<Integer> findClosestElements(int[] arr, int k, int x) {
        Deque<Integer> res = new ArrayDeque<>();
        var findIdx = Arrays.binarySearch(arr, x);
        if (findIdx < 0) {
            findIdx = -(findIdx + 1);
        }
        int l = findIdx - 1, r = findIdx;

        while (k > 0) {
            if (l >= 0 && r < arr.length) {
                if (Math.abs(x - arr[l]) <= Math.abs(x - arr[r])) {
                    res.addFirst(arr[l--]);
                } else {
                    res.addLast(arr[r++]);
                }
            } else if (l < 0) {
                res.addLast(arr[r++]);
            } else {
                res.addFirst(arr[l--]);
            }
            k--;
        }
        return new ArrayList<>(res);
    }

    /**
     * # 659
     *
     * @param nums
     * @return
     */
    public static boolean isPossible(int[] nums) {
        Map<Integer, PriorityQueue<Integer>> last_elem_2_len = new HashMap<>(
                nums.length);
        for (var n : nums) {
            if (!last_elem_2_len.containsKey(n - 1)) {
                last_elem_2_len.computeIfAbsent(n, k -> new PriorityQueue<>(16))
                        .add(1);
            } else {
                var queue_n_minus_1 = last_elem_2_len.get(n - 1);
                last_elem_2_len.computeIfAbsent(n, k -> new PriorityQueue<>(16))
                        .add(queue_n_minus_1.poll() + 1);
                if (queue_n_minus_1.isEmpty()) {
                    last_elem_2_len.remove(n - 1);
                }
            }
        }
        return last_elem_2_len.values().stream().allMatch(q -> q.peek() >= 3);
    }

    /**
     * #661
     *
     * @param img
     * @return
     */
    public int[][] imageSmoother(int[][] img) {
        int[] dx = new int[] { -1, -1, -1, 0, 0, 0, 1, 1, 1 };
        int[] dy = new int[] { -1, 0, 1, -1, 0, 1, -1, 0, 1 };
        int m = img.length, n = img[0].length;
        int[][] res = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int sum = 0, count = 0;
                for (int k = 0; k < 9; k++) {
                    var x = i + dx[k];
                    var y = j + dy[k];
                    if (x >= 0 && x < m && y >= 0 && y < n) {
                        sum += img[x][y];
                        count++;
                    }
                }
                res[i][j] = sum / count;
            }
        }
        return res;
    }

    /**
     * #665
     *
     * @param nums
     * @return
     */
    public static boolean checkPossibility(int[] nums) {
        boolean found_reverse = false;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < nums[i - 1]) {
                if (found_reverse) {
                    return false;
                }

                if (i > 1 && i < nums.length - 1 && (!(nums[i] >= nums[i - 2]
                        || nums[i + 1] >= nums[i - 1]))) {
                    return false;
                }

                found_reverse = true;
            }
        }
        return true;
    }

    /**
     * #668
     *
     * @param m
     * @param n
     * @param k
     * @return
     */
    public static int findKthNumber(int m, int n, int k) {
        int start = 1, end = m * n + 1;

        while (end - start > 1) {
            int mid = start + (end - start) / 2;
            int order = count_of_less_than(mid, m, n);
            if (order >= k) {
                end = mid;
            } else {
                start = mid + 1;
            }
        }
        return start;
    }

    /**
     * #670
     *
     * @param num
     * @return
     */
    public int maximumSwap(int num) {
        List<Integer> arr = new ArrayList<>();
        while (num > 0) {
            arr.add(num % 10);
            num /= 10;
        }
        Stack<Integer> idxInc = new Stack<>();
        for (int i = 0; i < arr.size(); i++) {
            if (idxInc.isEmpty() || arr.get(idxInc.peek()) < arr.get(i)) {
                idxInc.add(i);
            }
        }
        for (int i = arr.size() - 1; i > 0; i--) {
            while (!idxInc.isEmpty() && idxInc.peek() >= i) {
                idxInc.pop();
            }
            if (!idxInc.isEmpty() && arr.get(i) < arr.get(idxInc.peek())) {
                var t = arr.get(i);
                arr.set(i, arr.get(idxInc.peek()));
                arr.set(idxInc.peek(), t);
                break;
            }
        }
        int res = 0;
        for (int i = arr.size() - 1; i >= 0; i--) {
            res *= 10;
            res += arr.get(i);
        }
        return res;
    }

    /**
     * #673
     * 
     * @param nums
     * @return
     */
    public int findNumberOfLIS(int[] nums) {
        List<List<Integer>> dp = new ArrayList<>();
        List<List<Integer>> count = new ArrayList<>();
        for (var n : nums) {
            int i = 0;
            if (dp.isEmpty() || n > dp.getLast().getLast()) {
                dp.add(new ArrayList<>());
                dp.getLast().add(n);
                count.add(new ArrayList<>());
                i = dp.size() - 1;
            } else {
                int l = 0, r = dp.size() - 1;
                while (r - l > 0) {
                    int mid = l + (r - l) / 2;
                    var last = dp.get(mid).getLast();
                    if (last < n)
                        l = mid + 1;
                    else
                        r = mid;
                }
                dp.get(l).add(n);
                i = l;
            }
            if (i == 0)
                count.get(i).add(1 + (count.get(i).isEmpty() ? 0
                        : count.get(i).getLast()));
            else {
                int s = 0, e = count.get(i - 1).size() - 1;
                while (e - s > 0) {
                    int mid = s + (e - s) / 2;
                    if (dp.get(i - 1).get(mid) < n) {
                        e = mid;
                    } else {
                        s = mid + 1;
                    }
                }
                var c = count.get(i - 1).getLast()
                        - (s - 1 >= 0 ? count.get(i - 1).get(s - 1) : 0);
                count.get(i).add(c + (count.get(i).isEmpty() ? 0
                        : count.get(i).getLast()));
            }
        }
        return count.getLast().getLast();
    }

    /**
     * #674 回文子串个数 输入："aaa" 输出：6 解释：6个回文子串: "a", "a", "a", "aa", "aa", "aaa"
     *
     * @param s String
     * @return count of sub string
     */
    public static int countSubstrings(String s) {
        DPMatrix<Boolean> store = new DPMatrix<>(s.length());
        int res = s.length();
        // two
        for (int i = 0; i + 2 - 1 < s.length(); i++) {
            if (s.charAt(i) == s.charAt(i + 1)) {
                store.setAt(true, i, i);
                res++;
            }
        }

        // three
        for (int i = 0; i + 3 - 1 < s.length(); i++) {
            if (s.charAt(i) == s.charAt(i + 2)) {
                store.setAt(true, i, i + 1);
                res++;
            }
        }

        // other
        for (int l = 4; l <= s.length(); l++) {
            for (int i = 0; i + l - 1 < s.length(); i++) {
                if (store.getAt(i + 1, i + l - 3)) {
                    if (s.charAt(i) == s.charAt(i + l - 1)) {
                        store.setAt(true, i, i + l - 2);
                        res++;
                    }
                }
            }
        }

        return res;
    }

    static record CharPos(char c, int pos) {

    }

    /**
     * #678
     *
     * @param s
     * @return
     */
    public boolean checkValidString(String s) {
        Deque<CharPos> deque = new ArrayDeque<>();
        var arr = s.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            var c = arr[i];
            if (c == '(') {
                deque.addLast(new CharPos(c, i));
            } else if (c == '*') {
                deque.addFirst(new CharPos(c, i));
            } else if (!deque.isEmpty()) {
                deque.pollLast();
            } else {
                return false;
            }
        }
        while (deque.size() >= 2) {
            var left = deque.pollLast();
            var star = deque.pollFirst();
            if (left.c == '*') {
            } else if ((star.c == '(') || (left.c == '(' && star.c == '*'
                    && star.pos < left.pos)) {
                return false;
            }
        }
        while (!deque.isEmpty() && deque.peekFirst().c == '*') {
            deque.pollFirst();
        }
        return deque.isEmpty();
    }

    /**
     * #679
     *
     * @param cards
     * @return
     */
    public static boolean judgePoint24(int[] cards) {
        return judge24(Arrays.stream(cards).asDoubleStream().toArray());
    }

    private static boolean judge24(double[] nums) {
        if (nums.length == 2) {
            return can_be_24(nums[0], nums[1]);
        } else {
            for (int i = 0; i < nums.length; i++) {
                for (int j = i + 1; j < nums.length; j++) {
                    double[] arr = new double[nums.length - 1];
                    merge(i, j, nums, arr, Double::sum);
                    if (judge24(arr)) {
                        return true;
                    }

                    merge(i, j, nums, arr, (a, b) -> a * b);
                    if (judge24(arr)) {
                        return true;
                    }

                    merge(i, j, nums, arr, (a, b) -> a - b);
                    if (judge24(arr)) {
                        return true;
                    }

                    merge(i, j, nums, arr, (a, b) -> b - a);
                    if (judge24(arr)) {
                        return true;
                    }

                    double a = nums[i], b = nums[j];
                    if (Math.abs(b) > tol) {
                        merge(i, j, nums, arr, (a1, a2) -> a1 / a2);
                        if (judge24(arr)) {
                            return true;
                        }
                    }
                    if (Math.abs(a) > tol) {
                        merge(i, j, nums, arr, (a1, a2) -> a2 / a1);
                        if (judge24(arr)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    private static void merge(int i, int j, double[] nums, double[] res,
            BiFunction<Double, Double, Double> op) {
        for (int k = 0, idx = 0; k < nums.length; k++) {
            if (k != i && k != j) {
                res[idx++] = nums[k];
            }
        }
        res[res.length - 1] = op.apply(nums[i], nums[j]);
    }

    private static boolean can_be_24(double a, double b) {
        return approximate_24(a + b) || approximate_24(a - b)
                || approximate_24(b - a) || approximate_24(a * b)
                || (Math.abs(b) > tol && approximate_24(a / b))
                || (Math.abs(a) > tol && approximate_24(b / a));
    }

    static final double tol = 10e-6;

    private static boolean approximate_24(double d) {
        return Math.abs(d - 24) <= tol;
    }

    private static int count_of_less_than(int x, int m, int n) {
        int ans = 0;
        for (int i = x / n + 1; i <= m; i++) {
            ans += x / i;
        }
        return ans + x / n * n;
    }

    /**
     * #680
     *
     * @param s
     * @return
     */
    public boolean validPalindrome(String s) {
        return validPalindrome(s, 0, s.length() - 1, false);
    }

    boolean validPalindrome(String s, int l, int r, boolean retry) {
        while (l < r) {
            if (s.charAt(l) == s.charAt(r)) {
                l++;
                r--;
            } else if (!retry) {
                return validPalindrome(s, l + 1, r, true)
                        || validPalindrome(s, l, r - 1, true);
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * #684
     *
     * @param edges
     * @return
     */
    public int[] findRedundantConnection(int[][] edges) {
        Disjointset set = new Disjointset(edges.length + 1);
        for (var e : edges) {
            if (set.parent(e[0]) == set.parent(e[1])) {
                return e;
            }
            set.union(e[0], e[1]);
        }
        return null;
    }

    /**
     * #686
     *
     * @param a
     * @param b
     * @return
     */
    public static int repeatedStringMatch(String a, String b) {
        int n = (int) Math.ceil(b.length() / (float) a.length());

        var rep = a.repeat(n);
        Pattern pattern = Pattern.compile(b);
        Matcher matcher = pattern.matcher(rep);
        if (matcher.find()) {
            return n;
        } else {
            var rep1 = rep + a;
            matcher = pattern.matcher(rep1);
            if (matcher.find()) {
                return n + 1;
            } else {
                return -1;
            }
        }
    }

    /**
     * #688
     *
     * @param n
     * @param k
     * @param row
     * @param column
     * @return
     */
    public double knightProbability(int n, int k, int row, int column) {
        double[][][] dp = new double[k + 1][n][n];
        dp[0][row][column] = 1;
        int[] dx = new int[] { 1, 2, 1, 2, -1, -2, -1, -2 };
        int[] dy = new int[] { 2, 1, -2, -1, 2, 1, -2, -1 };
        for (int i = 1; i <= k; i++) {
            for (int x = 0; x < n; x++) {
                for (int y = 0; y < n; y++) {
                    for (int j = 0; j < 8; j++) {
                        var nx = x + dx[j];
                        var ny = y + dy[j];
                        if (nx >= 0 && nx < n && ny >= 0 && ny < n) {
                            dp[i][x][y] += dp[i - 1][nx][ny] / 8.;
                        }
                    }
                }
            }
        }
        return Arrays.stream(dp[k]).map(col -> Arrays.stream(col).sum())
                .mapToDouble(i -> i).sum();
    }

    /**
     * #689
     *
     * @param nums
     * @param k
     * @return
     */
    public static int[] maxSumOfThreeSubarrays(int[] nums, int k) {
        int w_sum_1 = 0, w_sum_2 = 0, w_sum_3 = 0;

        for (int i = 0; i < k; i++) {
            w_sum_1 += nums[i];
            w_sum_2 += nums[i + k];
            w_sum_3 += nums[i + k + k];
        }
        int max_1 = w_sum_1, max_2 = max_1 + w_sum_2, max_3 = max_2 + w_sum_3;
        int max_pos_1 = 0;
        int[] max_pos_2 = new int[] { 0, k };
        int[] max_pos_3 = new int[] { 0, k, 2 * k };
        for (int i = 0; i + 3 * k < nums.length; i++) {
            w_sum_1 -= nums[i];
            w_sum_1 += nums[i + k];
            if (w_sum_1 > max_1) {
                max_1 = w_sum_1;
                max_pos_1 = i + 1;
            }
            w_sum_2 -= nums[i + k];
            w_sum_2 += nums[i + 2 * k];
            if (w_sum_2 + max_1 > max_2) {
                max_2 = w_sum_2 + max_1;
                max_pos_2[0] = max_pos_1;
                max_pos_2[1] = i + k + 1;
            }
            w_sum_3 -= nums[i + 2 * k];
            w_sum_3 += nums[i + 3 * k];
            if (w_sum_3 + max_2 > max_3) {
                max_3 = w_sum_3 + max_2;

                max_pos_3[0] = max_pos_2[0];
                max_pos_3[1] = max_pos_2[1];
                max_pos_3[2] = i + 2 * k + 1;
            }
        }
        return max_pos_3;

    }

    /**
     * #691
     * 
     * @param stickers
     * @param target
     * @return
     */
    public int minStickers(String[] stickers, String target) {
        int mask = (1 << target.length()) - 1;
        int[] cache = new int[mask + 1];
        Arrays.fill(cache, -1);
        cache[0] = 0;
        var res = dp(stickers, mask, target, cache);
        return res < Integer.MAX_VALUE / 2 ? res : -1;
    }

    int dp(String[] stickers, int mask, String target, int[] cache) {
        if (cache[mask] >= 0)
            return cache[mask];
        int res = Integer.MAX_VALUE / 2;
        for (var sticker : stickers) {
            var nextMask = mask;
            int[] stickerCount = new int['z' - 'a' + 1];
            for (var c : sticker.toCharArray())
                stickerCount[c - 'a']++;
            for (int i = 0; i < target.length(); i++) {
                var c = target.charAt(i);
                if ((mask & (1 << i)) > 0 && stickerCount[c - 'a'] > 0) {
                    stickerCount[c - 'a']--;
                    nextMask ^= (1 << i);
                }
            }
            if (nextMask != mask)
                res = Math.min(res, 1 + dp(stickers, nextMask, target, cache));
        }
        cache[mask] = res;
        return res;
    }

    /**
     * #692
     *
     * @param words
     * @param k
     * @return
     */
    public List<String> topKFrequent(String[] words, int k) {
        Map<String, Integer> freq = new HashMap<>();
        for (var w : words) {
            freq.put(w, freq.getOrDefault(w, 0) + 1);
        }
        PriorityQueue<String> queue = new PriorityQueue<>((a, b) -> {
            var d = freq.get(b) - freq.get(a);
            return d != 0 ? d : (a.compareTo(b));
        });

        for (var key : freq.keySet()) {
            queue.add(key);
        }

        List<String> res = new ArrayList<>(k);
        while (k > 0) {
            res.add(queue.poll());
            k--;
        }
        return res;

    }

    /**
     * #694
     */
    public int numDistinctIslands(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        var set = new Disjointset(m * n);
        int[] dx = new int[] { 0, 0, 1, -1 };
        int[] dy = new int[] { 1, -1, 0, 0 };
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] != 1) {
                    continue;
                }
                for (int t = 0; t < 4; t++) {
                    var x = i + dx[t];
                    var y = j + dy[t];
                    if (x >= 0 && x < m && y >= 0 && y < n && grid[x][y] == 1) {
                        set.union(i * n + j, x * n + y);
                    }
                }
            }
        }
        Map<Integer, List<Pos>> islands = new HashMap<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] != 1) {
                    continue;
                }
                var p = new Pos(i, j);
                islands.computeIfAbsent(set.parent(i * n + j),
                        k -> new ArrayList<>()).add(p);
            }
        }
        TriePos root = new TriePos();
        int res = 0;
        for (var l : islands.values()) {
            var offset = l.stream().reduce(new Pos(m, n),
                    (a, b) -> new Pos(Math.min(a.i, b.i), Math.min(a.j, b.j)));
            var t = l.stream().map(p -> new Pos(p.i - offset.i, p.j - offset.j))
                    .sorted().toList();
            l.clear();
            l.addAll(t);
        }
        for (var l : islands.values()) {
            var ptr = root;
            for (var p : l) {
                ptr = ptr.children.computeIfAbsent(p, k -> new TriePos());
            }
            if (!ptr.hasIsland) {
                res++;
            }
            ptr.hasIsland = true;
        }

        return res;
    }

    static class TriePos {

        boolean hasIsland;
        Map<Pos, TriePos> children = new HashMap<>();
    }

    static record Pos(int i, int j) implements Comparable<Pos> {

        @Override
        public int compareTo(Pos o) {
            var c = Integer.compare(i, o.i);
            if (c != 0) {
                return c;
            }
            return Integer.compare(j, o.j);
        }

    }

    /**
     * #695
     *
     * @param grid
     * @return
     */
    public int maxAreaOfIsland(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        Disjointset set = new Disjointset(m * n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0) {
                    set.parent[i * n + j] = -1;
                } else {
                    if (j + 1 < n && grid[i][j + 1] == 1) {
                        set.union(i * n + j, i * n + j + 1);
                    }
                    if (i + 1 < m && grid[i + 1][j] == 1) {
                        set.union(i * n + j, (i + 1) * n + j);
                    }
                }
            }
        }
        Map<Integer, Integer> count = new HashMap<>();
        int res = 0;
        for (int i = 0; i < m * n; i++) {
            if (set.parent[i] != -1) {
                var p = set.parent(i);
                var v = count.getOrDefault(p, 0) + 1;
                count.put(p, v);
                res = Math.max(v, res);
            }
        }
        return res;
    }

}
