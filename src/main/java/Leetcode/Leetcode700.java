package Leetcode;


import java.util.*;
import java.util.function.BiFunction;

@SuppressWarnings("ALL")
public class Leetcode700 {

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
        var s = "(" +
                l +
                "," +
                n.val +
                "," +
                r +
                ")";
        if (!str2node.containsKey(s)) str2node.put(s, null);
        else str2node.put(s, n);
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
        int start = 0, end = arr.length;
        while (end - start > 1) {
            int mid = (start + end) / 2;
            if (arr[mid] > x) end = mid;
            else start = end;
        }
        int start_ans;
        if (arr[start] == x || start + 1 >= arr.length) start_ans = start;
        else {
            int d1 = Math.abs(arr[start] - x);
            int d2 = Math.abs(arr[start + 1] - x);
            start_ans = d1 <= d2 ? start : start + 1;
        }
        int end_ans_inclusive = start_ans;
        while (end_ans_inclusive + 1 - start_ans < k) {
            if (start_ans - 1 < 0) {
                end_ans_inclusive++;
            }
            else if (end_ans_inclusive + 1 >= arr.length) {
                start_ans--;
            }
            else {
                int d1 = Math.abs(arr[start_ans - 1] - x);
                int d2 = Math.abs(arr[end_ans_inclusive + 1] - x);
                if (d1 <= d2) start_ans--;
                else end_ans_inclusive++;
            }
        }
        List<Integer> ans = new ArrayList<>(end_ans_inclusive + 1 - start_ans);
        for (int i = start_ans; i <= end_ans_inclusive; i++) {
            ans.add(arr[i]);
        }
        return ans;
    }

    /**
     * # 659
     *
     * @param nums
     * @return
     */
    public static boolean isPossible(int[] nums) {
        Map<Integer, PriorityQueue<Integer>> last_elem_2_len = new HashMap<>(nums.length);
        for (var n : nums) {
            if (!last_elem_2_len.containsKey(n - 1)) {
                last_elem_2_len.computeIfAbsent(n, k -> new PriorityQueue<>(16)).add(1);
            }
            else {
                var queue_n_minus_1 = last_elem_2_len.get(n - 1);
                last_elem_2_len.computeIfAbsent(n, k -> new PriorityQueue<>(16)).add(queue_n_minus_1.poll() + 1);
                if (queue_n_minus_1.isEmpty()) last_elem_2_len.remove(n - 1);
            }
        }
        return last_elem_2_len.values().stream().allMatch(q -> q.peek() >= 3);
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

                if (i > 1 && i < nums.length - 1 && (!(nums[i] >= nums[i - 2] || nums[i + 1] >= nums[i - 1]))) {
                    return false;
                }

                found_reverse = true;
            }
        }
        return true;
    }


    /**
     * #674
     * 回文子串个数
     * 输入："aaa"
     * 输出：6
     * 解释：6个回文子串: "a", "a", "a", "aa", "aa", "aaa"
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

        //three
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
        }
        else {
            for (int i = 0; i < nums.length; i++) {
                for (int j = i + 1; j < nums.length; j++) {
                    double[] arr = new double[nums.length - 1];
                    merge(i, j, nums, arr, Double::sum);
                    if (judge24(arr)) return true;

                    merge(i, j, nums, arr, (a, b) -> a * b);
                    if (judge24(arr)) return true;

                    merge(i, j, nums, arr, (a, b) -> a - b);
                    if (judge24(arr)) return true;

                    merge(i, j, nums, arr, (a, b) -> b - a);
                    if (judge24(arr)) return true;

                    double a = nums[i], b = nums[j];
                    if (Math.abs(b) > tol) {
                        merge(i, j, nums, arr, (a1, a2) -> a1 / a2);
                        if (judge24(arr)) return true;
                    }
                    if (Math.abs(a) > tol) {
                        merge(i, j, nums, arr, (a1, a2) -> a2 / a1);
                        if (judge24(arr)) return true;
                    }
                }
            }
            return false;
        }
    }

    private static void merge(int i, int j, double[] nums, double[] res, BiFunction<Double, Double, Double> op) {
        for (int k = 0, idx = 0; k < nums.length; k++) {
            if (k != i && k != j) {
                res[idx++] = nums[k];
            }
        }
        res[res.length - 1] = op.apply(nums[i], nums[j]);
    }


    private static boolean can_be_24(double a, double b) {
        return approximate_24(a + b) ||
                approximate_24(a - b) ||
                approximate_24(b - a) ||
                approximate_24(a * b) ||
                (Math.abs(b) > tol && approximate_24(a / b)) ||
                (Math.abs(a) > tol && approximate_24(b / a));
    }

    static final double tol = 10e-6;

    private static boolean approximate_24(double d) {
        return Math.abs(d - 24) <= tol;
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
            if(order >=k){
                end = mid;
            }
            else {
                start = mid+1;
            }
        }
        return start;
    }

    private static int count_of_less_than(int x, int m, int n) {
        int ans = 0;
        for (int i = x / n + 1; i <= m; i++) {
            ans += x / i;
        }
        return ans + x / n * n;
    }

}
