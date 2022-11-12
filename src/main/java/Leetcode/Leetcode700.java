package Leetcode;


import java.util.*;

@SuppressWarnings("unused")
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
}
