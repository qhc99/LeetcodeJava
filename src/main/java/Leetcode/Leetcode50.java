package Leetcode;

import java.util.*;

@SuppressWarnings({"Unused", "JavaDoc"})
public class Leetcode50 {
    /**
     * #1
     * <br/>给定 nums = [2, 7, 11, 15], target = 9
     * <br/>因为 nums[0] + nums[1] = 2 + 7 = 9
     * <br/>所以返回 [0, 1]
     *
     * @param nums   array
     * @param target target sum
     * @return numbers array
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i);
        }
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement) && map.get(complement) != i) {
                return new int[]{i, map.get(complement)};
            }
        }
        throw new AssertionError();
    }

    /**
     * #2
     * <br/>reverse order integer addition
     * <br/>输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
     * <br/>输出：7 -> 0 -> 8
     * <br/>原因：342 + 465 = 807
     *
     * @param l1 list number
     * @param l2 list number
     * @return add result
     */
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        return recursiveAddTwoNumbers(l1, l2, 0);
    }

    private static ListNode recursiveAddTwoNumbers(ListNode l1, ListNode l2, int digit) {
        if (l1 == null && l2 == null) {
            return new ListNode(digit);
        }
        else if (l1 == null) {
            addDigit(l2, digit);
            return l2;
        }
        else if (l2 == null) {
            addDigit(l1, digit);
            return l1;
        }
        ListNode n = new ListNode();
        n.val = l1.val + l2.val + digit;
        int this_digit = 0;
        if (n.val >= 10) {
            n.val -= 10;
            this_digit++;
        }
        n.next = recursiveAddTwoNumbers(l1.next, l2.next, this_digit);
        if (n.next.val == 0 && n.next.next == null) {
            n.next = null;
        }
        return n;
    }

    private static void addDigit(ListNode l, int digit) {
        l.val += digit;
        if (l.val >= 10) {
            l.val -= 10;
            if (l.next == null) {
                l.next = new ListNode(1);
            }
            else {
                addDigit(l.next, 1);
            }
        }
    }

    /**
     * #3
     * <br/>最长无重复字符长度
     * <br/>输入: "abcabcbb"
     * <br/>输出: 3
     * <br/>解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
     *
     * @param s string
     * @return result
     */
    @SuppressWarnings("SpellCheckingInspection, Unused")
    public static int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int len = s.length();
        int head = 0, tail = 0;
        int max_count = 0;
        for (int idx = 0; idx < len; idx++) {
            char c = s.charAt(idx);
            if (!map.containsKey(c)) {
                map.put(c, idx);
            }
            else if (map.get(c) < head) {
                map.put(c, idx);
            }
            else {
                head = map.get(c) + 1;
                map.put(c, idx);
            }
            tail++;
            max_count = Math.max(max_count, tail - head);
        }
        return max_count;
    }

    /**
     * #4
     * <br/>两个有序数组的中位数
     * <br/>nums1 = [1, 3]
     * <br/>nums2 = [2]
     * <br/>则中位数是 2.0
     *
     * @param nums1 array
     * @param nums2 array
     * @return median
     */
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }
        int m = nums1.length, n = nums2.length;
        int left = 0, right = m;
        while (left < right) {
            int i = (left + right) / 2 + 1;
            int j = (m + n + 1) / 2 - i;
            int A_i_minus_1 = overflowGet(nums1, i - 1);
            int B_j = overflowGet(nums2, j);
            if (A_i_minus_1 <= B_j) {
                left = i;
            }
            else {
                right = i - 1;
            }
        }
        int i = left, j = (m + n + 1) / 2 - left;
        if ((m + n) % 2 == 0) {
            return (Math.max(overflowGet(nums1, i - 1), overflowGet(nums2, j - 1)) +
                    Math.min(overflowGet(nums1, i), overflowGet(nums2, j))) / 2.;
        }
        else {
            return Math.max(overflowGet(nums1, i - 1), overflowGet(nums2, j - 1));
        }
    }

    public static int overflowGet(int[] arr, int idx) {
        if (idx < 0) return Integer.MIN_VALUE;
        else if (idx >= arr.length) return Integer.MAX_VALUE;
        else return arr[idx];
    }


    /**
     * #5
     * <br/>最长回文字符串
     * <br/>输入: "babad"
     * <br/>输出: "bab"
     * <br/>注意: "aba" 也是一个有效答案。
     *
     * @param s string
     * @return longest palindrome
     */
    @SuppressWarnings("SpellCheckingInspection, Unused")
    public static String longestPalindrome(String s) {
        int len = s.length();
        if (len <= 1) {
            return s;
        }

        int res_start = 0, res_end = 0; // zero index, close interval
        boolean[][] S = new boolean[len - 1][len - 1];
        // two
        for (int i = 0; i + 2 - 1 < len; i++) {
            if (s.charAt(i) == s.charAt(i + 1)) {
                S[i][i] = true;
                res_start = i;
                res_end = i + 1;
            }
        }
        //three
        for (int i = 0; i + 3 - 1 < len; i++) {
            if (s.charAt(i) == s.charAt(i + 2)) {
                S[i][i + 1] = true;
                res_start = i;
                res_end = i + 2;
            }
        }
        // other
        for (int l = 4; l <= len; l++) {
            for (int i = 0; i + l - 1 < len; i++) {
                if (S[i + 1][i + l - 3]) {
                    if (s.charAt(i) == s.charAt(i + l - 1)) {
                        S[i][i + l - 2] = true;
                        res_start = i;
                        res_end = i + l - 1;
                    }
                }
            }
        }
        return s.substring(res_start, res_end + 1);
    }

    /**
     * #6
     * <br/>Z形字符串转字符串
     * <br/>input:<br/>
     * <pre>
     *    L    D    C
     *    E  O E  T O
     *    E C  L E  D
     *    T    E    E
     * </pre>
     * result: LEETCODELEETCODE
     *
     * @param s       z string
     * @param numRows row count
     * @return origin
     */
    @SuppressWarnings("SpellCheckingInspection, Unused")
    public static String convert(String s, int numRows) {
        int len = s.length();
        List<StringBuilder> rows = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            rows.add(new StringBuilder());
        }
        List<Integer> T = new ArrayList<>(); // numRows==4: 0 1 2 3 2 1
        for (int i = 0; i < numRows; i++) {
            T.add(i % numRows);
        }
        for (int i = 2 * numRows - 2; i > numRows; i--) {
            T.add(i % numRows);
        }
        int t = T.size();
        for (int i = 0; i < len; i++) {
            rows.get(T.get(i % t)).append(s.charAt(i));
        }
        StringBuilder res = new StringBuilder();
        for (StringBuilder row : rows) {
            res.append(row);
        }
        return res.toString();
    }

    /**
     * #7
     * <br/>reverse int with restriction
     * <br/>123 ---> 321
     *
     * @param x int
     * @return reversed int
     */
    public static int reverse(int x) {
        int rev = 0;
        while (x != 0) {
            int pop = x % 10;
            x /= 10;
            if (rev > Integer.MAX_VALUE / 10 || (rev == Integer.MAX_VALUE / 10 && pop > 7)) {
                return 0;
            }
            if (rev < Integer.MIN_VALUE / 10 || (rev == Integer.MIN_VALUE / 10 && pop < -8)) {
                return 0;
            }
            rev = rev * 10 + pop;
        }
        return rev;
    }

    /**
     * #8
     * <br/>string to int
     *
     * @param str string
     * @return int
     */
    @SuppressWarnings("SpellCheckingInspection, Unused")
    public static int myAtoi(String str) {
        var automata = new Automation();
        int len = str.length();
        for (int i = 0; i < len; i++) {
            automata.input(str.charAt(i));
        }
        return automata.getResult();
    }

    public static int myAtoi2(String str) {
        @SuppressWarnings("SameParameterValue") var funcCheck = new Object() {
            boolean multiplyNotOverflow(int x, int y) {
                long r = (long) x * (long) y;
                return (int) r == r;
            }

            boolean addNotOverflow(int x, int y) {
                int r = x + y;
                // HD 2-12 Overflow iff both arguments have the opposite sign of the result
                return !(((x ^ r) & (y ^ r)) < 0);
            }
        };

        str = str.strip();
        if (str.equals("")) return 0;
        boolean minus = false;
        int idx = 0;
        if (str.charAt(idx) == '-') {
            minus = true;
            idx++;
        }
        else if (str.charAt(idx) == '+') {
            idx++;
        }
        int res = 0;
        while (idx < str.length()) {
            var c = str.charAt(idx);
            int diff = c - '0';
            if (diff >= 0 && diff <= 9) {
                if (funcCheck.multiplyNotOverflow(res, 10) && funcCheck.addNotOverflow(res * 10, diff)) {
                    res *= 10;
                    res += diff;
                }
                else {
                    return minus ? Integer.MIN_VALUE : Integer.MAX_VALUE;
                }
            }
            else break;
            idx++;
        }

        return minus ? -res : res;
    }

    private static final class Automation {
        private final Map<String, List<String>> transition_map = Map.of(
                "start", List.of("start", "signed", "in_number", "end"),
                "signed", List.of("end", "end", "in_number", "end"),
                "in_number", List.of("end", "end", "in_number", "end"),
                "end", List.of("end", "end", "end", "end")
        );
        private String state = "start";
        private long ans = 0;
        private int sign = 1;

        private int get_column(char c) {
            if (c == ' ') {
                return 0;
            }
            else if (c == '+' || c == '-') {
                return 1;
            }
            else if (Character.isDigit(c)) {
                return 2;
            }
            return 3;
        }

        public void input(char c) {
            state = transition_map.get(state).get(get_column(c));
            if (state.equals("in_number")) {
                ans = ans * 10 + Integer.parseInt(String.valueOf(c));
                ans = sign == 1 ? Math.min(ans, Integer.MAX_VALUE) : Math.min(ans, -(long) Integer.MIN_VALUE);
            }
            else if (state.equals("signed")) {
                sign = (c == '+') ? 1 : -1;
            }
        }

        public int getResult() {
            return ((int) ans) * sign;
        }
    }

    /**
     * #9
     * <br/>回文整数
     * <br/>输入: 121
     * <br/>输出: true
     * <br/>输入: -121
     * <br/>输出: false
     *
     * @param x int
     * @return x is palindrome
     */
    public static boolean isPalindrome(int x) {
        if (x < 0) {
            return false;
        }
        int rev = 0;
        int original = x;
        while (x != 0) {
            int pop = x % 10;
            x /= 10;
            if (rev > Integer.MAX_VALUE / 10 || (rev == Integer.MAX_VALUE / 10 && pop > 7)) {
                return false;
            }
            if (rev < Integer.MIN_VALUE / 10) {
                return false;
            }
            rev = rev * 10 + pop;
        }
        return rev == original;
    }

    /**
     * #10
     * <br/>正则表达式匹配
     *
     * @param s string
     * @param p pattern
     * @return is match
     */
    public static boolean isMatch(String s, String p) {
        int m = s.length(), n = p.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        for (int j = 1; j <= n; j++) {
            var char_p = p.charAt(j - 1);
            for (int i = 0; i <= m; i++) {
                if (char_p != '*') {
                    dp[i][j] = charMatchAt(s, p, i - 1, j - 1) && dp[i - 1][j - 1];
                }
                else {
                    dp[i][j] = charMatchAt(s, p, i - 1, j - 2) ? (dp[i][j - 2] || dp[i - 1][j]) : dp[i][j - 2];
                }
            }
        }
        return dp[m][n];
    }

    private static boolean charMatchAt(String s, String p, int pi, int pj) {
        if (pi < 0) return false;
        var cs = s.charAt(pi);
        var cp = p.charAt(pj);
        if (cp == '.') return true;
        else return cs == cp;
    }


    /**
     * #11
     * <br/>盛最多水的容器
     *
     * @param height partition array
     * @return max area
     */
    public static int maxArea(int[] height) {
        int len = height.length;
        int p = 0, q = len - 1;
        int max_area = (q - p) * Math.min(height[p], height[q]);
        while (p != q) {
            if (height[p] > height[q]) {
                int current_area = (q - p) * Math.min(height[p], height[q]);
                max_area = Math.max(max_area, current_area);
                q--;
            }
            else if (height[p] < height[q]) {
                int current_area = (q - p) * Math.min(height[p], height[q]);
                max_area = Math.max(max_area, current_area);
                p++;
            }
            else {
                int current_area = (q - p) * Math.min(height[p], height[q]);
                max_area = Math.max(max_area, current_area);
                int l = height[p + 1];
                int h = height[q - 1];
                if (l < h) {
                    q--;
                }
                else {
                    p++;
                }
            }
        }
        return max_area;
    }

    /**
     * #15
     * <br/>三数之和
     * <pre>
     * 给定数组 nums = [-1, 0, 1, 2, -1, -4]，
     *
     * 满足要求的三元组集合为：
     * [
     *   [-1, 0, 1],
     *   [-1, -1, 2]
     * ]
     * </pre>
     *
     * @param nums int array
     * @return set of three tuple
     */

    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (!(i > 0 && nums[i] == nums[i - 1])) {
                int left = i + 1;
                int right = nums.length - 1;
                while (left < right) {
                    if (nums[i] + nums[left] + nums[right] > 0) {
                        right--;
                    }
                    else if (nums[i] + nums[left] + nums[right] < 0) {
                        left++;
                    }
                    else {
                        List<Integer> l = new ArrayList<>();
                        l.add(nums[i]);
                        l.add(nums[left]);
                        l.add(nums[right]);
                        res.add(l);
                        while (right > left && nums[right] == nums[right - 1]) {
                            right--;
                        }
                        while (right > left && nums[left] == nums[left + 1]) {
                            left++;
                        }


                        left++;
                        right--;
                    }
                }
            }
        }

        return res;
    }

    /**
     * #16
     * <br/>输入：nums = [-1,2,1,-4], target = 1
     * <br/>输出：2
     * <br/>解释：与 target 最接近的和是 2 (-1 + 2 + 1 = 2) 。
     *
     * @param nums   array
     * @param target sum target
     * @return closest sum
     */

    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int ans = nums[0] + nums[1] + nums[2];
        for (int i = 0; i < nums.length - 2; i++) {
            int s = i + 1, e = nums.length - 1;
            while (s < e) {
                int t = nums[i] + nums[s] + nums[e];
                if (Math.abs(t - target) < Math.abs(ans - target)) {
                    ans = t;
                }
                if (t == target) {
                    return t;
                }
                else if (t > target) {
                    e--;
                }
                else {
                    s++;
                }
            }
        }
        return ans;
    }

    /**
     * #17
     *
     * @param digits
     * @return
     */
    public static List<String> letterCombinations(String digits) {
        if (digits.length() == 0) {
            return List.of();
        }
        Map<Character, List<String>> map =
                Map.ofEntries(
                        Map.entry('2', List.of("a", "b", "c")),
                        Map.entry('3', List.of("d", "e", "f")),
                        Map.entry('4', List.of("g", "h", "i")),
                        Map.entry('5', List.of("j", "k", "l")),
                        Map.entry('6', List.of("m", "n", "o")),
                        Map.entry('7', List.of("p", "q", "r", "s")),
                        Map.entry('8', List.of("t", "u", "v")),
                        Map.entry('9', List.of("w", "x", "y", "z")));
        Queue<StringBuilder> queue = new ArrayDeque<>((int) Math.pow(3, digits.length()));
        var chars = map.get(digits.charAt(0));
        for (var c : chars) {
            queue.add(new StringBuilder(c));
        }
        for (int i = 1; i < digits.length(); i++) {
            while (queue.size() > 0 && queue.peek().length() == i) {
                var b = queue.poll();
                var crs = map.get(digits.charAt(i));
                for (int j = 0; j < crs.size() - 1; j++) {
                    queue.add(new StringBuilder(b).append(crs.get(j)));
                }
                queue.add(b.append(crs.get(crs.size() - 1)));
            }
        }
        List<String> ans = new ArrayList<>(queue.size());
        while (queue.size() > 0) {
            var b = queue.poll();
            ans.add(b.toString());
        }
        return ans;
    }

    /**
     * #18
     * <br/>四数之和
     * <br/>给定数组 nums = [1, 0, -1, 0, -2, 2]，和 target = 0。
     * <p>
     * <br/>满足要求的四元组集合为：<br/>
     * <pre>
     * [
     *  [-1,  0, 0, 1],
     *  [-2, -1, 1, 2],
     *  [-2,  0, 0, 2]
     * ]</pre>
     *
     * @param nums   array
     * @param target sum target
     * @return list of four-tuple
     */

    public List<List<Integer>> fourSum(int[] nums, int target) {
        int len = nums.length;
        List<List<Integer>> res = new ArrayList<>();
        if (len < 4) {
            return res;
        }
        Arrays.sort(nums);
        for (int i = 0; i < len - 3; i++) {
            if (i != 0) {
                while (i < len - 3 && nums[i] == nums[i - 1]) {
                    i++;
                }
            }
            for (int j = i + 1; j < len - 2; j++) {
                if (j != i + 1) {
                    while (j < len - 2 && nums[j] == nums[j - 1]) {
                        j++;
                    }
                }
                int a = j + 1, b = len - 1;
                while (a < b) {
                    int t = nums[i] + nums[j] + nums[a] + nums[b];
                    if (t == target) {
                        res.add(List.of(nums[i], nums[j], nums[a], nums[b]));
                        a++;
                        while (a < b && nums[a] == nums[a - 1]) {
                            a++;
                        }
                    }
                    else if (t < target) {
                        a++;
                        while (a < b && nums[a] == nums[a - 1]) {
                            a++;
                        }
                    }
                    else {
                        b--;
                        while (a < b && nums[b] == nums[b + 1]) {
                            b--;
                        }
                    }
                }
            }
        }
        return res;
    }

    /**
     * #19
     * <br/>remove the nth node of reverse order
     * <br/>1->2->3->4, 2 ---> 1->2->4
     *
     * @param head linked list
     * @param n    order
     * @return processed linked list
     */

    public static ListNode removeNthFromEnd(ListNode head, int n) {
        boolean is_len_one = (head.next == null);
        int head_order = RecursiveRemoveNthFromEnd(head, n);
        if (is_len_one && n == 1) {
            return null;
        }
        else if (head_order == n) {
            return head.next;
        }
        else {
            return head;
        }
    }

    private static int RecursiveRemoveNthFromEnd(ListNode node, int n) {
        if (node.next == null) {
            return 1;
        }
        int this_order = RecursiveRemoveNthFromEnd(node.next, n) + 1;
        if (this_order == n + 1) {
            node.next = node.next.next;
        }
        return this_order;
    }


    /**
     * #22
     *
     * @param n
     * @return
     */
    public static List<String> generateParenthesisNew(int n) {
        class Tuple {
            final StringBuilder strB;
            final int unmatched;
            final int left_count;

            public Tuple(StringBuilder s, int st, int c) {
                strB = s;
                unmatched = st;
                left_count = c;
            }
        }
        Deque<Tuple> queue = new ArrayDeque<>(n);
        queue.add(new Tuple(new StringBuilder("("), 1, 1));
        for (int i = 1; i < 2 * n; i++) {
            while (queue.size() > 0 && queue.peek().strB.length() == i) {
                var t = queue.poll();
                if (t.unmatched > 0 && t.left_count < n) {
                    queue.add(new Tuple(new StringBuilder(t.strB).append(")"), t.unmatched - 1, t.left_count));
                    queue.add(new Tuple(t.strB.append("("), t.unmatched + 1, t.left_count + 1));
                }
                else if (t.left_count < n) {
                    queue.add(new Tuple(t.strB.append("("), t.unmatched + 1, t.left_count + 1));
                }
                else if (t.unmatched > 0) {
                    queue.add(new Tuple(t.strB.append(")"), t.unmatched - 1, t.left_count));
                }
                else throw new RuntimeException();
            }
        }
        List<String> ans = new ArrayList<>(queue.size());
        for (var t : queue) {
            ans.add(t.strB.toString());
        }
        return ans;
    }

    /**
     * #22
     * <br/>k pairs parenthesis permutation
     * <br/>3 ---> ["((()))", "(()())", "(())()", "()(())", "()()()"]
     *
     * @param n order
     * @return result
     */

    public static List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        StringBuilder init = new StringBuilder("(");
        RecursiveGenerateParenthesis(init, 1, 1, 1, res, n);
        return res;
    }

    private static void RecursiveGenerateParenthesis(StringBuilder stringBuilder,
                                                     int str_len, int left_count,
                                                     int stack_ptr, List<String> res, int n) {
        if (str_len == (2 * n)) {
            res.add(stringBuilder.toString());
        }
        else {
            str_len++;
            if (stack_ptr >= 1 && stack_ptr <= n - 1 && left_count <= n - 1) {
                StringBuilder t = new StringBuilder(stringBuilder);
                stringBuilder.append("(");
                t.append(")");
                RecursiveGenerateParenthesis(stringBuilder, str_len, left_count + 1, stack_ptr + 1, res, n);
                RecursiveGenerateParenthesis(t, str_len, left_count, stack_ptr - 1, res, n);
            }
            else if (stack_ptr == 0 && left_count <= n - 1) {
                stringBuilder.append("(");
                RecursiveGenerateParenthesis(stringBuilder, str_len, left_count + 1, stack_ptr + 1, res, n);
            }
            else {
                stringBuilder.append(")");
                RecursiveGenerateParenthesis(stringBuilder, str_len, left_count, stack_ptr - 1, res, n);
            }
        }
    }

    /**
     * #23
     *
     * @param lists
     * @return
     */
    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists.length == 0) return null;
        PriorityQueue<ListNode> priorityQueue = new PriorityQueue<>(lists.length,
                Comparator.comparing(listNode -> listNode.val));
        for (var lt : lists) {
            if (lt != null) {
                priorityQueue.add(lt);
            }
        }
        ListNode ans = null, tail = null;
        while (priorityQueue.size() > 0) {
            var lt = priorityQueue.poll();
            if (ans == null) {
                ans = lt;
            }
            if (tail == null) {
                tail = ans;
            }
            else {
                tail.next = lt;
                tail = lt;
            }
            lt = lt.next;
            if (lt != null) {
                priorityQueue.add(lt);
            }
        }
        return ans;
    }

    /**
     * #25
     * <br/>reverse a linked list every k elements
     * 1->2->3->4->5->6, 3  --->  3->2->1->6->5->4
     *
     * @param head linked list
     * @param k    group number
     * @return reversed linked list
     */

    public static ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || k <= 1) {
            return head;
        }
        ListNode res = new ListNode();
        ListNode handle;
        res.next = head;
        handle = res;
        while (hasKChildren(handle, k)) {
            handle = reverseGroup(handle, k);
        }
        return res.next;
    }

    private static ListNode reverseGroup(ListNode handle, int k) {
        // have at least k node
        ListNode rest, ptr1, ptr2, head;
        head = handle.next;
        ptr2 = head;
        ptr1 = head.next;
        rest = ptr1.next;
        // iterate k - 1 times
        for (int i = 0; i < k - 2; i++) {
            ptr1.next = ptr2;
            ptr2 = ptr1;
            ptr1 = rest;
            rest = rest.next;
        }
        ptr1.next = ptr2;
        ptr2 = ptr1;
        //
        head.next = rest;
        handle.next = ptr2;
        return head;
    }

    private static boolean hasKChildren(ListNode handle, int k) {
        for (int i = 0; i < k; i++) {
            handle = handle.next;
            if (handle == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * #29
     * <br/>int divide without '/', '*', '%'
     *
     * @param dividend dividend
     * @param divisor  divisor
     * @return result
     */

    public static int divide(int dividend, int divisor) {
        if ((dividend == Integer.MIN_VALUE) && (divisor == -1)) {
            return Integer.MAX_VALUE;
        }
        if (divisor == 1) {
            return dividend;
        }
        if (divisor == -1) {
            return -dividend;
        }
        if (dividend == divisor) {
            return 1;
        }

        long new_divisor = (divisor > 0) ? divisor : ((long) (~divisor)) + 1;
        long new_dividend = (dividend > 0) ? dividend : ((long) (~dividend)) + 1;
        int res = 0;
        int sign = ((dividend > 0 && divisor > 0) || (dividend < 0 && divisor < 0)) ? 1 : -1;

        if (new_dividend < new_divisor) {
            return 0;
        }

        int i;
        do {
            i = 0;
            while (((new_divisor << i) <= new_dividend)) {
                i++;
                if (i >= 1 && leftMoveOverflow(new_divisor << (i - 1))) {
                    break;
                }
            }
            if (i != 0) {
                new_dividend -= (new_divisor << (i - 1));
                res += 1 << (i - 1);
            }
        }
        while (i != 0);

        return sign * res;
    }

    private static boolean leftMoveOverflow(long n) {
        return (((n & 0x40000000) << 1) ^ (n & 0x80000000)) == 0x80000000;
    }

    /**
     * #30
     * find possible chain result<BR>
     * s = "barfoothefoobarman",<BR>
     * words = ["foo","bar"]<BR>
     * answer: [0, 9] //('barfoo' at 0, and 'foobar' at 9)<BR>
     *
     * @param s     string
     * @param words words
     * @return result index
     */
    @SuppressWarnings("SpellCheckingInspection, Unused")
    public static List<Integer> findSubstring(String s, String[] words) {
        List<Integer> res = new ArrayList<>();
        if (s == null || s.length() == 0 || words == null || words.length == 0 || words[0].length() == 0) {
            return res;
        }
        Map<String, Integer> words_and_count = new HashMap<>();
        for (var word : words) {
            Integer count = words_and_count.getOrDefault(word, 0);
            words_and_count.put(word, count + 1);
        }
        int s_len = s.length();
        int word_len = words[0].length();
        int words_count = words.length;
        int valid_word_upper_bound = s_len - word_len + 1;
        String[] match_res = new String[valid_word_upper_bound];
        for (int i = 0; i < valid_word_upper_bound; i++) {
            for (String word : words) {
                if (s.substring(i, i + word_len).equals(word)) {
                    match_res[i] = word;
                    break;
                }
            }
        }
        int valid_chain_upper_bound = s_len - word_len * words_count + 1;
        Map<String, Integer> check_map = new HashMap<>();
        for (var word : words) {
            check_map.put(word, 0);
        }
        for (int idx1 = 0; idx1 < valid_chain_upper_bound; idx1++) {
            if (canChain(idx1, word_len, words_count, match_res)) {
                int count = 0;
                for (var entry : check_map.entrySet()) {
                    entry.setValue(0);
                }
                for (int idx2 = idx1; count < words_count; idx2 += word_len) {
                    Integer c = check_map.get(match_res[idx2]);
                    check_map.put(match_res[idx2], c + 1);
                    count++;
                }
                if (check_map.equals(words_and_count)) {
                    res.add(idx1);
                }
            }
        }
        return res;
    }

    private static boolean canChain(int idx, int word_len, int words_count, String[] match_res) {
        boolean can_chain = true;
        int count = 0;
        for (; count < words_count; idx += word_len) {
            count++;
            if (match_res[idx] == null) {
                can_chain = false;
                break;
            }
        }
        return can_chain;
    }

    /**
     * #32
     *
     * @param nums
     */
    public static void nextPermutationNew(int[] nums) {
        int k = nums.length - 2;
        for (; k >= 0; k--) {
            if (nums[k] < nums[k + 1]) {
                break;
            }
        }
        if (k == -1) {
            reverseArray(nums, 0);
            return;
        }
        int l = nums.length - 1;
        for (; l >= 0; l--) {
            if (nums[l] > nums[k]) {
                break;
            }
        }
        swap(nums, k, l);
        reverseArray(nums, k + 1);
    }

    private static void reverseArray(int[] nums, int left) {
        int right = nums.length - 1;
        while (left < right) {
            swap(nums, left, right);
            left++;
            right--;
        }
    }

    private static void swap(int[] nums, int a, int b) {
        var t = nums[a];
        nums[a] = nums[b];
        nums[b] = t;
    }


    /**
     * #31
     * <br/>下一个排列
     * <br/>1,2,3 → 1,3,2
     * <br/>3,2,1 → 1,2,3
     * <br/>1,1,5 → 1,5,1
     *
     * @param nums permutation
     */

    public static void nextPermutation(int[] nums) {
        int len = nums.length;
        if (len == 0 || len == 1) {
            return;
        }
        int i = len - 1;
        for (; i >= 1; i--) {
            if (nums[i] > nums[i - 1]) {
                break;
            }
        }
        if (i == 0) {
            reverseArray(nums, 0, len);
            return;
        }
        int idx1 = i - 1;
        int idx2 = i;
        while (idx2 < len && (!((idx2 == len - 1) || (nums[idx2] > nums[idx1] && nums[idx2 + 1] <= nums[idx1])))) {
            idx2++;
        }
        int t = nums[idx1];
        nums[idx1] = nums[idx2];
        nums[idx2] = t;
        reverseArray(nums, i, len);
    }

    private static void reverseArray(int[] nums, int start, int end) {
        int idx1 = start;
        int idx2 = end - 1;
        while (idx1 < idx2) {
            int t = nums[idx1];
            nums[idx1] = nums[idx2];
            nums[idx2] = t;
            idx1++;
            idx2--;
        }
    }

    /**
     * #32
     * <br/>最长符号陪对<BR>
     *
     * @param s string
     * @return max length
     */

    public static int longestValidParentheses(String s) {
        int max_len = 0;
        int s_len = s.length();
        if (s_len == 0) {
            return 0;
        }
        int[] stack = new int[s_len + 1];
        stack[0] = -1;
        int stack_head = 1;
        for (int i = 0; i < s_len; i++) {
            if (s.charAt(i) == '(') {
                stack[stack_head++] = i;
            }
            else {
                stack_head--;
                if (stack_head == 0) {
                    stack[stack_head++] = i;
                }
                else {
                    max_len = Math.max(max_len, i - stack[stack_head - 1]);
                }
            }
        }
        return max_len;
    }

    /**
     * #33
     * <BR>旋转数组搜索
     * <br/>input: nums = [4,5,6,7,0,1,2], target = 0
     * <br/>result: 4
     * <p>
     * <br/>search border case:<br/>
     * <pre>
     * left_mid    right_mid       result
     * &gt mid       &gt mid      border in left
     * &lt mid       &lt mid      border in right
     * &lt mid       &gt mid         depends
     * &gt mid       &lt mid       not possible
     * else         else       nums is order
     * </pre>
     *
     * @param nums   array
     * @param target target
     * @return index
     */

    public static int search(int[] nums, int target) {
        int len = nums.length;
        if (len == 0) {
            return -1;
        }
        int border = searchBorder(nums);
        if (border == -1) {
            return binarySearchSubArray(nums, target, 0, len - 1);
        }
        if (target == nums[border]) {
            return border;
        }

        if (target > nums[len - 1]) {
            return binarySearchSubArray(nums, target, 0, border - 1);
        }
        else {
            return binarySearchSubArray(nums, target, border, len - 1);
        }

    }

    private static int binarySearchSubArray(int[] nums, int target, int start, int end) {
        while (end >= start) {
            int mid = (start + end) / 2;
            if (target == nums[mid]) {
                return mid;
            }
            else if (target > nums[mid]) {
                start = mid + 1;
            }
            else {
                end = mid - 1;
            }
        }
        return -1;
    }

    private static int searchBorder(int[] nums) {
        int len = nums.length;
        int left_idx = 0, right_idx = len;
        while (left_idx < right_idx) {
            int mid_idx = (left_idx + right_idx) / 2;
            if ((mid_idx - 1) >= 0 && nums[mid_idx] < nums[mid_idx - 1]) {
                return mid_idx;
            }
            else if ((mid_idx + 1) < len && nums[mid_idx] > nums[mid_idx + 1]) {
                return mid_idx + 1;
            }

            int left_mid_idx = (left_idx + mid_idx) / 2;
            int right_mid_idx = (mid_idx + right_idx) / 2;
            if (nums[left_mid_idx] > nums[mid_idx] && nums[right_mid_idx] > nums[mid_idx]) {
                left_idx = left_mid_idx;
                right_idx = mid_idx - 1;
            }
            else if (nums[left_mid_idx] < nums[mid_idx] && nums[right_mid_idx] < nums[mid_idx]) {
                left_idx = mid_idx + 1;
                right_idx = right_mid_idx;
            }
            else if (nums[left_mid_idx] < nums[mid_idx] && nums[right_mid_idx] > nums[mid_idx]) {
                if (nums[mid_idx] > nums[left_idx]) {
                    left_idx = mid_idx + 1;
                }
                else {
                    right_idx = mid_idx - 1;
                }
            }
            else {
                return -1;
            }

        }
        return -1;
    }

    /**
     * #34
     * <BR>搜搜有序数组连续数字的边界<BR>
     * <br/>输入: nums = [5,7,7,8,8,10], target = 8
     * <br/>输出: [3,4]
     * <br/>输入: nums = [5,7,7,8,8,10], target = 6
     * <br/>输出: [-1,-1]
     *
     * @param nums   array
     * @param target target
     * @return range
     */

    public static int[] SearchRange(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return new int[]{-1, -1};
        }
        int l = searchLowerBound(nums, target);
        int u = searchUpperBound(nums, target);
        if (nums[0] == target) {
            l = 0;
        }
        if (nums[nums.length - 1] == target) {
            u = nums.length - 1;
        }
        return new int[]{l, u};
    }

    private static int searchLowerBound(int[] nums, int target) {
        int start = 0, end = nums.length;
        while (end - start > 1) {
            int mid = (start + end) / 2;
            if (nums[mid] < target) {
                if (mid + 1 < nums.length && nums[mid + 1] == target) {
                    return mid + 1;
                }
                start = mid;
            }
            else if (nums[mid] > target) {
                end = mid;
            }
            else {
                end--;
            }
        }
        if (start + 1 < nums.length && nums[start] < target && nums[start + 1] == target) {
            return start + 1;
        }
        return -1;
    }

    private static int searchUpperBound(int[] nums, int target) {
        int start = 0, end = nums.length;
        while (end - start > 1) {
            int mid = (start + end) / 2;
            if (nums[mid] < target) {
                start = mid;
            }
            else if (nums[mid] > target) {
                if (mid - 1 >= 0 && nums[mid - 1] == target) {
                    return mid - 1;
                }
                end = mid;
            }
            else {
                start++;
            }
        }
        if (start - 1 >= 0 && nums[start] > target && nums[start - 1] == target) {
            return start - 1;
        }
        return -1;
    }

    /**
     * #36
     * <BR>验证9*9方格是否为数独
     *
     * @param board 9*9 board
     * @return is valid sudoku
     */

    public static boolean isValidSudoku(char[][] board) {
        List<Set<Character>> cols = new ArrayList<>();
        List<Set<Character>> rows = new ArrayList<>();
        List<Set<Character>> groups = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            cols.add(new HashSet<>());
            rows.add(new HashSet<>());
            groups.add(new HashSet<>());
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                char c = board[i][j];
                if (c == '.') {
                    continue;
                }
                var cm = cols.get(j);
                var rm = rows.get(i);
                var gm = groups.get((i / 3) * 3 + j / 3);
                if (cm.contains(c)) {
                    return false;
                }
                else if (rm.contains(c)) {
                    return false;
                }
                else if (gm.contains(c)) {
                    return false;
                }
                else {
                    cm.add(c);
                    rm.add(c);
                    gm.add(c);
                }
            }
        }
        return true;
    }

    /**
     * #37
     * <BR>解数独
     *
     * <pre>
     * char[][] t = new char[][]{
     *     {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
     *     {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
     *     {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
     *     {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
     *     {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
     *     {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
     *     {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
     *     {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
     *     {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
     * };
     * </pre>
     *
     * @param board 9*9 board
     */

    public static void solveSudoku(char[][] board) {
        // initialization
        List<MatrixIndex> spaceIndices = new ArrayList<>(81);
        Map<MatrixIndex, List<MatrixIndex>> relatedSpacesMap = new HashMap<>(81);
        List<Set<Character>> rowsFilledCharSets = new ArrayList<>(9);
        for (int i = 0; i < 9; i++) {
            rowsFilledCharSets.add(new HashSet<>(9));
        }
        List<Set<Character>> colsFilledCharSets = new ArrayList<>(9);
        for (int i = 0; i < 9; i++) {
            colsFilledCharSets.add(new HashSet<>(9));
        }
        List<Set<Character>> groupsFilledCharSets = new ArrayList<>(9);
        for (int i = 0; i < 9; i++) {
            groupsFilledCharSets.add(new HashSet<>(9));
        }

        // analyse
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                char fill = board[r][c];
                if (fill == '.') {
                    var space = new MatrixIndex(r, c);
                    spaceIndices.add(space);
                    relatedSpacesMap.put(space, new ArrayList<>(27));
                    for (int i = 0; i < spaceIndices.size() - 1; i++) {
                        var loopSpace = spaceIndices.get(i);
                        if (!space.equals(loopSpace) && isRelated(loopSpace, space)) {
                            relatedSpacesMap.get(space).add(loopSpace);
                            relatedSpacesMap.get(loopSpace).add(space);
                        }
                    }
                }
                else {
                    rowsFilledCharSets.get(r).add(fill);
                    colsFilledCharSets.get(c).add(fill);
                    groupsFilledCharSets.get(getGroupIndex(r, c)).add(fill);
                }
            }
        }

        // sort according to related spaces
        spaceIndices.sort((m1, m2) ->
        {
            var rc1 = relatedSpacesMap.get(m1).size();
            var rc2 = relatedSpacesMap.get(m2).size();
            return rc1 - rc2;
        });

        for (var v : relatedSpacesMap.values()) {
            v.sort((m1, m2) ->
            {
                var rc1 = relatedSpacesMap.get(m1).size();
                var rc2 = relatedSpacesMap.get(m2).size();
                return rc1 - rc2;
            });
        }

        // group according to whether related
        List<MatrixIndex> sortedGroupedSpaceIndices = new ArrayList<>(spaceIndices.size());
        var spaceIndicesSet = new HashSet<>(spaceIndices);
        for (var spaceIdx : spaceIndices) {
            if (spaceIndicesSet.contains(spaceIdx)) {
                sortedGroupedSpaceIndices.add(spaceIdx);
                spaceIndicesSet.remove(spaceIdx);
                var rs = relatedSpacesMap.get(spaceIdx);
                for (var e : rs) {
                    if (spaceIndicesSet.contains(e)) {
                        sortedGroupedSpaceIndices.add(e);
                        spaceIndicesSet.remove(e);
                    }
                }
            }
        }

        // get available chars for each space
        Map<MatrixIndex, Set<Character>> availableCharsMap = new HashMap<>(spaceIndices.size());
        for (var space : spaceIndices) {
            var set = new HashSet<Character>(9);
            for (int i = 1; i <= 9; i++) {
                set.add((char) (i + '0'));
            }

            for (var c : rowsFilledCharSets.get(space.row)) {
                set.remove(c);
            }

            for (var c : colsFilledCharSets.get(space.col)) {
                set.remove(c);
            }

            for (var c : groupsFilledCharSets.get(getGroupIndex(space.row, space.col))) {
                set.remove(c);
            }
            availableCharsMap.put(space, set);
        }
        depthFirstSearchSudoku(sortedGroupedSpaceIndices, 0, board, availableCharsMap, relatedSpacesMap, new HashSet<>(81));
    }

    static boolean depthFirstSearchSudoku(
            List<MatrixIndex> spaces,
            int spaceIdx,
            char[][] board,
            Map<MatrixIndex, Set<Character>> availableCharsMap,
            Map<MatrixIndex, List<MatrixIndex>> relatedSpacesMap,
            Set<MatrixIndex> encounteredSpaces) {
        if (spaceIdx == spaces.size()) {
            return true;
        }

        MatrixIndex currentSpace = spaces.get(spaceIdx);
        encounteredSpaces.add(currentSpace);
        int rIdx = currentSpace.row;
        int cIdx = currentSpace.col;
        for (var chr : availableCharsMap.get(currentSpace)) {
            board[rIdx][cIdx] = chr;
            List<Boolean> removeRecord = new ArrayList<>(relatedSpacesMap.size());
            for (var relatedSpace : relatedSpacesMap.get(currentSpace)) {
                if (!encounteredSpaces.contains(relatedSpace)) {
                    removeRecord.add(availableCharsMap.get(relatedSpace).remove(chr));
                }
            }
            boolean success = depthFirstSearchSudoku(
                    spaces,
                    spaceIdx + 1,
                    board,
                    availableCharsMap,
                    relatedSpacesMap,
                    encounteredSpaces);
            if (success) {
                return true;
            }
            int accIdx = 0;
            for (var relatedSpace : relatedSpacesMap.get(currentSpace)) {
                if (!encounteredSpaces.contains(relatedSpace)) {
                    if (removeRecord.get(accIdx++)) {
                        availableCharsMap.get(relatedSpace).add(chr);
                    }
                }
            }

        }

        board[rIdx][cIdx] = '.';
        encounteredSpaces.remove(currentSpace);
        return false;
    }

    private static int getGroupIndex(int row, int columns) {
        return (row / 3) * 3 + columns / 3;
    }

    private static boolean isRelated(MatrixIndex a, MatrixIndex b) {
        return a.row == b.row || a.col == b.col || getGroupIndex(a.row, a.col) == getGroupIndex(b.row, b.col);
    }

    private static class MatrixIndex {
        public final int row;
        public final int col;
        private final int hash;

        public MatrixIndex(int r, int c) {
            row = r;
            col = c;
            hash = Objects.hash(row, col);
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof MatrixIndex t) {
                return t.row == row && t.col == col;
            }
            else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return hash;
        }

    }

    /**
     * #38
     * <br/>外观数列
     * <pre>
     * 1.     1
     * 2.     11
     * 3.     21
     * 4.     1211
     * 5.     111221
     * </pre>
     * 第一项是数字 1<BR>
     * 描述前一项，这个数是 1 即 “一个 1 ”，记作 11<BR>
     * 描述前一项，这个数是 11 即 “两个 1 ” ，记作 21<BR>
     * 描述前一项，这个数是 21 即 “一个 2 一个 1 ” ，记作 1211<BR>
     * 描述前一项，这个数是 1211 即 “一个 1 一个 2 两个 1 ” ，记作 111221<BR>
     *
     * @param n order
     * @return result
     */

    public static String countAndSay(int n) {
        String start = "1";
        System.out.println(start);
        for (int i = 2; i <= n; i++) {
            start = next(start);
            System.out.println(start);
        }
        return start;
    }

    private static String next(String s) {
        int i = 0;
        StringBuilder res = new StringBuilder();
        while (i < s.length()) {
            int count = 1;
            while ((i + 1 < s.length()) && s.charAt(i) == s.charAt(i + 1)) {
                count++;
                i++;
            }
            res.append(count);
            res.append(s.charAt(i));
            i++;
        }
        return res.toString();
    }

    /**
     * #39
     * <br/>组合总和
     * <br/>candidates不重复,可重选
     * <pre>
     * 输入：candidates = [2,3,6,7], target = 7,
     * 所求解集为：
     * [
     *   [7],
     *   [2,2,3]
     * ]
     * 输入：candidates = [2,3,5], target = 8,
     * 所求解集为：
     * [
     *   [2,2,2,2],
     *   [2,3,3],
     *   [3,5]
     * ]
     * </pre>
     *
     * @param candidates int array
     * @param target     sum
     * @return result
     */

    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(candidates);
        recursiveCombinationSum(candidates, target, new ArrayList<>(), res);
        return res;
    }

    private static void recursiveCombinationSum(
            int[] candidates,
            int targetSum,
            List<Integer> currentCache,
            List<List<Integer>> res) {
        for (int i = 0; i < candidates.length && candidates[i] <= targetSum; i++) {
            if (currentCache.size() > 0 && candidates[i] < currentCache.get(currentCache.size() - 1)) {
                continue;
            }
            else {
                if (candidates[i] == targetSum) {
                    List<Integer> t = new ArrayList<>(currentCache);
                    t.add(candidates[i]);
                    res.add(t);
                }
                else {
                    List<Integer> t = new ArrayList<>(currentCache);
                    t.add(candidates[i]);
                    recursiveCombinationSum(candidates, targetSum - candidates[i], t, res);
                }
            }
        }
    }

    /**
     * #40
     * <br/>组合总和2
     * <br/>candidates可重复,不可重选
     * <pre>
     * 输入: candidates =[10,1,2,7,6,1,5], target =8,
     * 所求解集为:
     * [
     *   [1, 7],
     *   [1, 2, 5],
     *   [2, 6],
     *   [1, 1, 6]
     * ]
     *
     * 输入: candidates = [2,5,2,1,2], target = 5,
     * 所求解集为:
     * [
     *   [1,2,2],
     *   [5]
     * ]
     * </pre>
     *
     * @param candidates int array
     * @param target     sum
     * @return result
     */

    public static List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(candidates);
        recursiveCombinationSum2(candidates, target, new ArrayList<>(), new boolean[candidates.length], res);
        return res;
    }

    private static void recursiveCombinationSum2(
            int[] candidates,
            int targetSum,
            List<Integer> currentCache,
            boolean[] currentSelected,
            List<List<Integer>> res) {
        for (int i = 0; i < candidates.length && candidates[i] <= targetSum; i++) {
            if (currentSelected[i]) {
                continue;
            }
            else if (i < candidates.length - 1 && candidates[i] == candidates[i + 1] && !currentSelected[i + 1]) {
                continue;
            }
            else {
                if (currentCache.size() > 0 && candidates[i] < currentCache.get(currentCache.size() - 1)) {
                    continue;
                }
                else {
                    currentSelected[i] = true;
                    currentCache.add(candidates[i]);
                    if (targetSum == candidates[i]) {
                        res.add(new ArrayList<>(currentCache));
                        currentCache.remove(currentCache.size() - 1);
                        currentSelected[i] = false;
                    }
                    else {
                        recursiveCombinationSum2(candidates, targetSum - candidates[i], currentCache, currentSelected
                                , res);
                        currentSelected[i] = false;
                        currentCache.remove(currentCache.size() - 1);
                    }
                }
            }
        }
    }

    /**
     * #42
     *
     * @param height
     * @return
     */
    public static int trap(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int left_max = height[left], right_max = height[right];
        int volume = 0;
        while (left < right) {
            if (left_max <= right_max) {
                volume += left_max - height[left];
                left++;
                left_max = Math.max(left_max, height[left]);
            }
            else {
                volume += right_max - height[right];
                right--;
                right_max = Math.max(right_max, height[right]);
            }
        }
        return volume;
    }


    /**
     * #43
     * <br/>字符串乘法
     *
     * @param num1 num1
     * @param num2 num2
     * @return multiply
     */

    public static String multiply(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }
        int[] res = new int[num1.length() + num2.length()];
        for (int i = num1.length() - 1; i >= 0; i--) {
            int n1 = num1.charAt(i) - '0';
            for (int j = num2.length() - 1; j >= 0; j--) {
                int n2 = num2.charAt(j) - '0';
                int sum = (res[i + j + 1] + n1 * n2);
                res[i + j + 1] = sum % 10;
                res[i + j] += sum / 10;
            }
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < res.length; i++) {
            if (i == 0 && res[i] == 0) continue;
            result.append(res[i]);
        }
        return result.toString();
    }

    /**
     * #44
     *
     * @param s
     * @param p
     * @return
     */
    public static boolean isMatchWildcard(String s, String p) {
        int m = s.length(), n = p.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        for (int j = 1; j <= n; j++) {
            var char_p = p.charAt(j - 1);
            for (int i = 0; i <= m; i++) {
                if (char_p != '*') {
                    dp[i][j] = charMatchAtWildcard(s, p, i - 1, j - 1) && dp[i - 1][j - 1];
                }
                else {
                    if (i - 1 >= 0) dp[i][j] = dp[i][j - 1] || dp[i - 1][j];
                    else dp[i][j] = dp[i][j - 1];
                }
            }
        }
        return dp[m][n];
    }

    private static boolean charMatchAtWildcard(String s, String p, int pi, int pj) {
        if (pi < 0) return false;
        var cs = s.charAt(pi);
        var cp = p.charAt(pj);
        if (cp == '?') return true;
        else return cs == cp;
    }


    /**
     * #46
     * <br/>全排列
     * <pre>
     * 输入: [1,2,3]
     * 输出:
     * [
     *   [1,2,3],
     *   [1,3,2],
     *   [2,1,3],
     *   [2,3,1],
     *   [3,1,2],
     *   [3,2,1]
     * ]
     * </pre>
     *
     * @param nums int array
     * @return permutation
     */

    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        recursivePermute(nums, new boolean[nums.length], new ArrayList<>(), res);
        return res;
    }

    public static void recursivePermute(int[] nums, boolean[] selected, List<Integer> cache, List<List<Integer>> res) {
        for (int i = 0; i < nums.length; i++) {
            if (!selected[i]) {
                selected[i] = true;
                cache.add(nums[i]);
                if (cache.size() == nums.length) {
                    res.add(new ArrayList<>(cache));
                }
                else {
                    recursivePermute(nums, selected, cache, res);
                }
                cache.remove(cache.size() - 1);
                selected[i] = false;
            }
        }
    }

    /**
     * #47
     * <br/>全排列
     * <pre>
     * 输入: [1,1,2]
     * 输出:
     * [
     *   [1,1,2],
     *   [1,2,1],
     *   [2,1,1]
     * ]
     * </pre>
     *
     * @param nums int array
     * @return unique permutation result
     */

    public static List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> finalRes = new ArrayList<>();
        Arrays.sort(nums);
        boolean[] encountered = new boolean[nums.length];
        recursivePermuteUnique(nums, encountered, new ArrayList<>(), finalRes);
        return finalRes;
    }

    private static void recursivePermuteUnique(
            int[] nums,
            boolean[] inCache,
            List<Integer> currentCache,
            List<List<Integer>> finalRes) {
        if (currentCache.size() == nums.length) {
            finalRes.add(new ArrayList<>(currentCache));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (i + 1 < nums.length && nums[i] == nums[i + 1] && !inCache[i + 1]) {
                continue;
            }
            else if (!inCache[i]) {
                inCache[i] = true;
                currentCache.add(nums[i]);
                recursivePermuteUnique(nums, inCache, currentCache, finalRes);
                inCache[i] = false;
                currentCache.remove(currentCache.size() - 1);
            }
        }
    }

    /**
     * #48
     * <br/>旋转图像
     *
     * @param matrix image
     */

    public static void rotate(int[][] matrix) {
        if (matrix.length <= 1) {
            return;
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix.length; j++) {
                int t = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = t;
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length / 2; j++) {
                int t = matrix[i][j];
                matrix[i][j] = matrix[i][matrix.length - 1 - j];
                matrix[i][matrix.length - 1 - j] = t;
            }
        }
    }

    /**
     * #50
     *
     * @param x
     * @param n
     * @return
     */
    public static double myPow(double x, int n) {
        double ans = 1;
        boolean isMinus = n < 0;
        long ln = n;
        ln = Math.abs(ln);
        while (ln > 0) {
            long bin = ln % 2;
            if (bin != 0) ans *= bin * x;
            x *= x;
            ln /= 2;
        }
        return isMinus ? 1. / ans : ans;
    }


}
