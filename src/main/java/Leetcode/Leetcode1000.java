package Leetcode;

import java.util.*;

@SuppressWarnings({ "JavaDoc" })
public class Leetcode1000 {

    /**
     * #902
     *
     * @param digits
     * @param n
     * @return
     */
    public static int atMostNGivenDigitSet(String[] digits, int n) {
        var S = String.valueOf(n);
        int len_n = S.length();
        int[] dp = new int[len_n + 1]; // preprocess
        dp[len_n] = 1;
        for (int idx = len_n - 1; idx >= 0; idx--) {
            var S_digit = Integer.parseInt(String.valueOf(S.charAt(idx)));
            for (var num_s : digits) {
                var num = Integer.parseInt(num_s);
                if (num < S_digit)
                    dp[idx] += Math.pow(digits.length, len_n - 1 - idx);
                else if (num == S_digit)
                    dp[idx] += dp[idx + 1];
            }
        }
        for (int i = 1; i < len_n; i++) {
            dp[0] += Math.pow(digits.length, i);
        }
        return dp[0];
    }

    /**
     * #907
     * 
     * @param arr
     * @return
     */
    public int sumSubarrayMins(int[] arr) {
        long[] left = new long[arr.length];
        long[] right = new long[arr.length];
        Stack<Integer> idx = new Stack<>();
        for (int i = 0; i < arr.length; i++) {
            while (!idx.isEmpty() && arr[idx.peek()] >= arr[i]) {
                idx.pop();
            }
            left[i] = i - (idx.isEmpty() ? -1 : idx.peek());
            idx.add(i);
        }
        idx.clear();
        for (int i = arr.length - 1; i >= 0; i--) {
            while (!idx.isEmpty() && arr[idx.peek()] > arr[i]) {
                idx.pop();
            }
            right[i] = (idx.isEmpty() ? arr.length : idx.peek()) - i;
            idx.add(i);
        }
        long mod = 1_000_000_007;
        int res = 0;
        for (int i = 0; i < arr.length; i++) {
            res += ((left[i] * right[i]) % mod * arr[i]) % mod;
            res %= mod;
        }
        return res;
    }

    /**
     * @param digits
     * @param n      start from 1, radix 10
     * @return
     */
    public static int nthNum(String[] digits, int n) {
        n--;
        if (n == 0) {
            return Integer.parseInt(digits[0]);
        }
        StringBuilder num = new StringBuilder();
        while (n != 0) {
            int idx = n % digits.length;
            num.insert(0, digits[idx]);
            n -= idx;
            n = n / digits.length;
        }
        return Integer.parseInt(num.toString());
    }

    /**
     * #909
     * 
     * @param board
     * @return
     */
    public int snakesAndLadders(int[][] board) {
        int n = board.length;
        boolean[][] visited = new boolean[n][n];
        Queue<int[]> queue = new ArrayDeque<>();
        var start = board[n - 1][0] == -1 ? 1 : board[n - 1][0];
        if (start == n * n)
            return 0;
        queue.add(new int[] { start, 0 });
        while (!queue.isEmpty()) {
            var data = queue.poll();
            var num = data[0];
            for (int i = 6; i >= 1; i--) {
                if (num + i > n * n)
                    continue;
                var p = num2pos(num + i, n);
                if (!visited[p[0]][p[1]]) {
                    visited[p[0]][p[1]] = true;
                    var next = board[p[0]][p[1]] != -1 ? board[p[0]][p[1]]
                            : num + i;
                    if (next == n * n)
                        return data[1] + 1;
                    queue.add(new int[] { next, data[1] + 1 });

                }
            }
        }
        return -1;
    }

    int[] num2pos(int i, int n) {
        i--;
        var r = n - 1 - i / n;
        var c = ((i / n) % 2) == 0 ? i % n : n - 1 - i % n;
        return new int[] { r, c };
    }

    int pos2num(int[] p, int n) {
        int d = 1;
        d += (n - 1 - p[0]) * n;
        d += (n - 1 - p[0]) % 2 == 0 ? p[1] : n - 1 - p[1];
        return d;
    }

    /**
     * #925
     *
     * @param name  name string
     * @param typed input string
     * @return is long press
     */
    public static boolean isLongPressedName(String name, String typed) {
        if (name.charAt(0) != typed.charAt(0)) {
            return false;
        }
        char lastChar = name.charAt(0);
        int namePtr = 1, typedPtr = 1;
        for (; typedPtr < typed.length(); typedPtr++) {
            if (namePtr < name.length()) {
                if (typed.charAt(typedPtr) == name.charAt(namePtr)) {
                    lastChar = name.charAt(namePtr);
                    namePtr++;
                } else {
                    if (typed.charAt(typedPtr) != lastChar) {
                        return false;
                    }
                }
            } else {
                if (typed.charAt(typedPtr) != lastChar) {
                    return false;
                }
            }
        }
        return namePtr == name.length();
    }

    /**
     * #930
     * 
     * @param nums
     * @param goal
     * @return
     */
    public int numSubarraysWithSum(int[] nums, int goal) {
        int res = 0;
        for (int i = 1; i < nums.length; i++) {
            nums[i] += nums[i - 1];
        }
        Map<Integer, Integer> count = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            var v = nums[i];
            if (v == goal)
                res++;
            res += count.getOrDefault(v - goal, 0);
            count.put(v, count.getOrDefault(v, 0) + 1);
        }
        return res;
    }

    /**
     * #931
     * 
     * @param matrix
     * @return
     */
    public int minFallingPathSum(int[][] matrix) {
        int n = matrix.length;
        int[][] dp = new int[2][n];
        System.arraycopy(matrix[0], 0, dp[0], 0, n);
        for (int i = 1; i < n; i++) {
            System.arraycopy(matrix[i], 0, dp[1], 0, n);
            for (int j = 0; j < n; j++) {
                int min = Integer.MAX_VALUE;
                if (j - 1 >= 0)
                    min = Math.min(min, dp[0][j - 1]);
                min = Math.min(min, dp[0][j]);
                if (j + 1 < n)
                    min = Math.min(min, dp[0][j + 1]);
                dp[1][j] += min;
            }
            var t = dp[0];
            dp[0] = dp[1];
            dp[1] = t;
        }
        return Arrays.stream(dp[0]).min().getAsInt();
    }

    /**
     * #933 RecentCounter
     */
    class RecentCounter {
        Queue<Integer> queue = new ArrayDeque<>();

        public RecentCounter() {

        }

        public int ping(int t) {
            while (!queue.isEmpty() && queue.peek() < t - 3000) {
                queue.poll();
            }
            queue.add(t);
            return queue.size();
        }
    }

    /**
     * #953
     * 
     * @param words
     * @param order
     * @return
     */
    public boolean isAlienSorted(String[] words, String order) {
        Map<Character, Integer> ord = new HashMap<>();
        for (int i = 0; i < order.length(); i++) {
            ord.put(order.charAt(i), i);
        }
        for (int i = 0; i < words.length - 1; i++) {
            var a = words[i];
            var b = words[i + 1];
            if (isAlienSortedCompare(a.toCharArray(), b.toCharArray(),
                    ord) > 0) {
                return false;
            }
        }
        return true;
    }

    int isAlienSortedCompare(char[] a, char[] b,
            Map<Character, Integer> order) {
        for (int i = 0; i < a.length && i < b.length; i++) {
            var m = order.get(a[i]);
            var n = order.get(b[i]);
            var c = Integer.compare(m, n);
            if (c != 0)
                return c;
        }
        return Integer.compare(a.length, b.length);
    }

    /**
     * #968 <br/>
     * 监控二叉树 grady algorithm
     *
     * @param root tree
     * @return number of camera
     */
    public int minCameraCover(TreeNode root) {
        var solver = new MinCameraCoverSolver();
        if (solver
                .dfs(root) == MinCameraCoverSolver.Status.NOT_BEING_MONITORED) {
            solver.res++;
        }
        return solver.res;
    }

    private static class MinCameraCoverSolver {
        enum Status {
            NOT_BEING_MONITORED, BEING_MONITORED, CAMERA_INSTALLED
        }

        int res = 0;

        Status dfs(TreeNode tn) {
            if (tn == null) {
                return Status.BEING_MONITORED;
            }
            Status left = dfs(tn.left);
            Status right = dfs(tn.right);
            // 左右子节点均已被监控，此时跳过
            if (left == Status.BEING_MONITORED
                    && right == Status.BEING_MONITORED) {
                return Status.NOT_BEING_MONITORED;
            }
            // 2种情况，1、一个子节点安装监控，另一个已被监控 2、两个子节点均有监控 此时不需要安装监控器，且该节点已被监控
            if ((left == Status.CAMERA_INSTALLED
                    && right == Status.CAMERA_INSTALLED)
                    || (left == Status.CAMERA_INSTALLED
                            && right == Status.BEING_MONITORED)
                    || (left == Status.BEING_MONITORED
                            && right == Status.CAMERA_INSTALLED)) {
                return Status.BEING_MONITORED;
            }
            // 其他情况均需要安装监控，不然会有节点监控不到
            res++;
            return Status.CAMERA_INSTALLED;
        }
    }

    /**
     * #986
     * 
     * @param firstList
     * @param secondList
     * @return
     */
    public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
        int i = 0, j = 0;
        List<int[]> res = new ArrayList<>();
        while (i < firstList.length && j < secondList.length) {
            var a = firstList[i];
            var b = secondList[j];
            if (a[1] <= b[1]) {
                if (a[1] >= b[0]) {
                    res.add(new int[] { Math.max(a[0], b[0]),
                            Math.min(a[1], b[1]) });
                }
                i++;
            } else {
                if (b[1] >= a[0]) {
                    res.add(new int[] { Math.max(a[0], b[0]),
                            Math.min(a[1], b[1]) });
                }
                j++;
            }
        }

        int[][] ans = new int[res.size()][];
        for (int t = 0; t < res.size(); t++)
            ans[t] = res.get(t);
        return ans;
    }

    /**
     * #973
     *
     * @param points
     * @param k
     * @return
     */
    public static int[][] kClosest(int[][] points, int k) {
        class Data {
            final int dist;
            final int[] point;

            Data(int d, int[] p) {
                dist = d;
                point = p;
            }
        }
        PriorityQueue<Data> queue = new PriorityQueue<>(
                Comparator.comparing(d -> d.dist));
        int[][] ans = new int[k][];
        for (var p : points) {
            queue.add(new Data(p[0] * p[0] + p[1] * p[1], p));
        }
        for (int i = 0; i < k; i++) {
            ans[i] = queue.poll().point;
        }

        return ans;
    }

    /**
     * #996
     * 
     * @param nums
     * @return
     */
    public int numSquarefulPerms(int[] nums) {
        int res = 0;
        Map<Pair, Integer> cache = new HashMap<>();
        Set<Integer> placed = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            if (!placed.contains(nums[i])) {
                placed.add(nums[i]);
                res += visit(nums[i], 1 << i, cache, nums);

            }
        }
        return res;
    }

    static record Pair(int n, int selected) {
    }

    int visit(int n, int selected, Map<Pair, Integer> cache, int[] nums) {
        var ce = cache.get(new Pair(n, selected));
        if (ce != null)
            return ce;
        if (((~(-1 << nums.length)) ^ selected) == 0) {
            cache.put(new Pair(n, selected), 1);
            return 1;
        }
        int res = 0;
        Set<Integer> placed = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            var sum = n + nums[i];
            int sq = (int) Math.sqrt(sum);
            if (sq * sq == sum && ((1 << i) & selected) == 0
                    && !placed.contains(nums[i])) {
                placed.add(nums[i]);
                res += visit(nums[i], 1 << i | selected, cache, nums);
            }
        }
        cache.put(new Pair(n, selected), res);
        return res;
    }

    /**
     * #997
     *
     * @param n
     * @param trust
     * @return
     */
    public static int findJudge(int n, int[][] trust) {
        int[] in = new int[n + 1];
        int[] out = new int[n + 1];
        for (var t : trust) {
            var a = t[0];
            var b = t[1];
            out[a]++;
            in[b]++;
        }
        for (int i = 1; i <= n; i++) {
            if (in[i] == n - 1 && out[i] == 0) {
                return i;
            }
        }
        return -1;
    }
}
