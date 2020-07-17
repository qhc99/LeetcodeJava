package src.main;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LeetCode100 {
    // #1
    // 给定 nums = [2, 7, 11, 15], target = 9
    // 因为 nums[0] + nums[1] = 2 + 7 = 9
    // 所以返回 [0, 1]
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i);
        }
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement) && map.get(complement) != i) {
                return new int[] { i, map.get(complement) };
            }
        }
        throw new AssertionError();
    }

    // #2
    // reverse order integer addition
    // 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
    // 输出：7 -> 0 -> 8
    // 原因：342 + 465 = 807
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        return recursiveAddTwoNumbers(l1, l2, 0);
    }
    private static ListNode recursiveAddTwoNumbers(ListNode l1, ListNode l2, int digit){
        if(l1 == null && l2 == null){
            return new ListNode(digit);
        }
        else if(l1 == null){
            addDigit(l2,digit);
            return l2;
        }
        else if(l2 == null){
            addDigit(l1,digit);
            return l1;
        }
        ListNode n = new ListNode();
        n.val = l1.val + l2.val + digit;
        int this_digit = 0;
        if(n.val >= 10){
            n.val -= 10;
            this_digit++;
        }
        n.next = recursiveAddTwoNumbers(l1.next,l2.next, this_digit);
        if(n.next.val == 0 && n.next.next == null){
            n.next = null;
        }
        return n;
    }
    private static void addDigit(ListNode l, int digit){
        l.val += digit;
        if(l.val >= 10){
            l.val -= 10;
            if (l.next == null) {
                l.next = new ListNode(1);
            }
            else{
                addDigit(l.next, 1);
            }
        }
    }

    // #3
    // 输入: "abcabcbb"
    // 输出: 3
    // 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
    public static int lengthOfLongestSubstring(String s) {
        Map<Character,Integer> map = new ConcurrentHashMap<>();
        int len = s.length();
        int head = 0, tail = 0;
        int max_count = tail - head;
        for(int idx = 0; idx < len; idx++){
            char c = s.charAt(idx);
            if(!map.containsKey(c)){
                map.put(c,idx);
            }
            else if(map.get(c) < head){
                map.put(c,idx);
            }
            else{
                head = map.get(c)+1;
                map.put(c,idx);
            }
            tail++;
            max_count = Math.max(max_count, tail - head);
        }
        return max_count;
    }

    // #4
    // 两个有序数组的中位数
    // nums1 = [1, 3]
    // nums2 = [2]
    // 则中位数是 2.0
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int len1 = nums1.length, len2 = nums2.length;
        if(len1 > len2){
            int[] t = nums1;
            nums1 = nums2;
            nums2 = t;
            len1 = nums1.length;
            len2 = nums2.length;
        }
        if(len1 == 0){
            if(len2 % 2 == 1){
                return nums2[len2/2];
            }
            else{
                return (nums2[len2/2] + nums2[(len2-1)/2])/2.0;
            }
        }
        int res = 0;
        var array = new ConcatenatedArrays(nums1,nums2);
        if((len1 + len2) % 2 == 1){
            for(int i = 0; i <= (len1+len2)/2; i++){
                res = array.nextInt();
            }
            return res;
        }
        else{
            for(int i = 0; i <= (len1+len2)/2-1; i++){
                res = array.nextInt();
            }
            return (res + array.nextInt()) / 2.0;
        }

    }
    private static class ConcatenatedArrays{
        private final int[] a1;
        private int head1 = 0;
        private final int[] a2;
        private int head2 = 0;
        public ConcatenatedArrays(int[] a1, int[] a2){
            this.a1 = a1;
            this.a2 = a2;
        }
        public int nextInt(){
            if(head1 == a1.length){
                return a2[head2++];
            }
            else if(head2 == a2.length){
                return a1[head1++];
            }
            if(a1[head1] < a2[head2]){
                return a1[head1++];
            }
            else{
                return a2[head2++];
            }
        }
    }

    // #5
    // 最长回文字符串
    // 输入: "babad"
    // 输出: "bab"
    // 注意: "aba" 也是一个有效答案。
    public static String longestPalindrome(String s) {
        int len = s.length();
        if(len <= 1){
            return s;
        }

        int res_start = 0, res_end = 0; // zero index, close interval
        boolean[][] S = new boolean[len - 1][len - 1];
        // two
        for(int i = 0; i + 2 - 1 < len; i++){
            if(s.charAt(i) == s.charAt(i+1)){
                S[i][i] = true;
                res_start = i;
                res_end = i+1;
            }
        }
        //three
        for(int i = 0; i + 3 - 1 < len; i++){
            if(s.charAt(i) == s.charAt(i+2)){
                S[i][i+1] = true;
                res_start = i;
                res_end = i + 2;
            }
        }
        // other
        for(int l = 4; l <= len; l++){
            for(int i = 0; i + l - 1 < len; i++){
                if(S[i + 1][ i + l - 3]){
                    if(s.charAt(i) == s.charAt(i+l-1)){
                        S[i][i + l - 2] = true;
                        res_start = i;
                        res_end = i + l - 1;
                    }
                }
            }
        }
        return s.substring(res_start, res_end + 1);
    }

    // #6
    // Z形字符串转字符串
    // input:
    //      L    D    C
    //      E  O E  T O
    //      E C  L E  D
    //      T    E    E
    // result: LEETCODELEETCODE
    public static String convert(String s, int numRows) {
        int len = s.length();
        List<StringBuilder> rows = new ArrayList<StringBuilder>();
        for(int i = 0; i < numRows; i++) {
            rows.add(new StringBuilder());
        }
        List<Integer> T = new ArrayList<Integer>(); // numRows==4: 0 1 2 3 2 1
        for(int i = 0; i < numRows; i++){
            T.add(i%numRows);
        }
        for(int i = 2 * numRows - 2; i > numRows; i--){
            T.add(i%numRows);
        }
        int t = T.size();
        for(int i = 0; i < len; i++){
            rows.get(T.get(i%t)).append(s.charAt(i));
        }
        StringBuilder res = new StringBuilder();
        for(StringBuilder row : rows){
            res.append(row);
        }
        return res.toString();
    }

    // #7
    // reverse int with restriction
    // 123 ---> 321
    public static int reverse(int x){
        int rev = 0;
        while (x != 0) {
            int pop = x % 10;
            x /= 10;
            if (rev > Integer.MAX_VALUE/10 || (rev == Integer.MAX_VALUE / 10 && pop > 7)) return 0;
            if (rev < Integer.MIN_VALUE/10 || (rev == Integer.MIN_VALUE / 10 && pop < -8)) return 0;
            rev = rev * 10 + pop;
        }
        return rev;
    }

    // #8
    // string to int
    public static int myAtoi(String str) {
        var automata = new Automation();
        int len = str.length();
        for(int i = 0; i < len; i++){
            automata.input(str.charAt(i));
        }
        return automata.getResult();
    }
    private static final class Automation{
        private String state = "start";
        private final Map<String, List<String>> transition_map = Map.of(
                "start", List.of("start", "signed", "in_number", "end"),
                "signed", List.of("end", "end", "in_number", "end"),
                "in_number", List.of("end", "end", "in_number", "end"),
                "end", List.of("end", "end", "end", "end")
        );
        private int get_column(char c){
            if(c == ' ') return 0;
            else if(c == '+' || c == '-') return 1;
            else if(Character.isDigit(c)) return 2;
            return 3;
        }
        private long ans = 0;
        private int sign = 1;

        public void input(char c){
            state = transition_map.get(state).get(get_column(c));
            if(state.equals("in_number")){
                ans = ans * 10 + Integer.parseInt(String.valueOf(c));
                ans = sign == 1 ? Math.min(ans, Integer.MAX_VALUE) : Math.min(ans, -(long)Integer.MIN_VALUE);
            }
            else if(state.equals("signed")){
                sign = (c == '+')? 1 : -1;
            }
        }
        public int getResult() { return ((int)ans)*sign; }
    }

    // #9
    // 回文整数
    // 输入: 121
    // 输出: true
    //
    // 输入: -121
    // 输出: false
    public static boolean isPalindrome(int x) {
        if(x < 0){
            return false;
        }
        int rev = 0;
        int original = x;
        while (x != 0) {
            int pop = x % 10;
            x /= 10;
            if (rev > Integer.MAX_VALUE/10 || (rev == Integer.MAX_VALUE / 10 && pop > 7)) {
                return false;
            }
            if (rev < Integer.MIN_VALUE/10 || (rev == Integer.MIN_VALUE / 10 && pop < -8)) {
                return false;
            }
            rev = rev * 10 + pop;
        }
        return rev == original;
    }

    // #19
    // remove the nth node of reverse order
    // 1->2->3->4, 2 ---> 1->2->4
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        boolean is_len_one = (head.next == null);
        int head_order = RecursiveRemoveNthFromEnd(head, n);
        if(is_len_one && n == 1) return null;
        else if(head_order == n) return head.next;
        else return head;
    }
    private static int RecursiveRemoveNthFromEnd(ListNode node, int n){
        if(node.next == null) return 1;
        int this_order = RecursiveRemoveNthFromEnd(node.next, n) + 1;
        if(this_order == n + 1) node.next = node.next.next;
        return this_order;
    }

    // #22
    // k pairs parenthesis permutation
    // 3 ---> ["((()))", "(()())", "(())()", "()(())", "()()()"]
    public static List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        StringBuilder init = new StringBuilder("(");
        RecursiveGenerateParenthesis(init, 1, 1, 1, res, n);
        return res;
    }
    private static void RecursiveGenerateParenthesis(StringBuilder strb, int str_len, int left_count, int stack_ptr, List<String> res, int n){
        if(str_len == (2*n)) res.add(strb.toString());
        else{
            str_len++;
            if(stack_ptr >= 1 && stack_ptr <= n-1 && left_count <= n-1){
                StringBuilder t = new StringBuilder(strb);
                strb.append("(");
                t.append(")");
                RecursiveGenerateParenthesis(strb, str_len, left_count+1, stack_ptr+1, res, n);
                RecursiveGenerateParenthesis(t, str_len, left_count, stack_ptr-1, res, n);
            }
            else if(stack_ptr == 0 && left_count <= n-1){
                strb.append("(");
                RecursiveGenerateParenthesis(strb, str_len, left_count+1, stack_ptr+1, res, n);
            }
            else{
                strb.append(")");
                RecursiveGenerateParenthesis(strb, str_len, left_count, stack_ptr-1, res, n);
            }
        }
    }

    // #23
    // 合并K个排序链表
    // 输入:
    // [
    //   1->4->5,
    //   1->3->4,
    //   2->6
    // ]
    // 输出: 1->1->2->3->4->4->5->6
    public static ListNode mergeKLists(ListNode[] lists) {
        if(lists.length == 0) return null;
        ListNode[] lst = new ListNode[lists.length];
        int heap_size = lst.length;
        int idx = 0;
        for(var i : lists){
            if(i != null){
                lst[idx++] = i;
            }else{
                heap_size--;
            }
        }
        if(heap_size == 0) return null;
        buildMinHeap(lst, heap_size);
        ListNode head;
        ListNode head_ptr;
        head = lst[0];
        head_ptr = head;
        lst[0] = lst[0].next;
        if(lst[0] == null){
            lst[0] = lst[heap_size - 1];
            heap_size--;
            minHeapify(lst, 0, heap_size);
        }
        while(heap_size > 0){
            minHeapify(lst, 0, heap_size);
            head_ptr.next = lst[0];
            head_ptr = head_ptr.next;
            lst[0] = lst[0].next;
            if(lst[0] == null){
                lst[0] = lst[heap_size - 1];
                heap_size--;
                minHeapify(lst, 0, heap_size);
            }
        }
        return head;
    }
    private static void minHeapify(ListNode[] arr, int idx, int heap_size){
        int l = 2 * (idx + 1);
        int l_idx = l - 1;
        int r = 2 * (idx + 1) + 1;
        int r_idx = r - 1;
        int min_idx = idx;
        if ((l_idx < heap_size) && (arr[l_idx].val < arr[min_idx].val)) {
            min_idx = l_idx;
        }
        if ((r_idx < heap_size) && (arr[r_idx].val < arr[min_idx].val)) {
            min_idx = r_idx;
        }
        if (min_idx != idx) {
            var t = arr[min_idx];
            arr[min_idx] = arr[idx];
            arr[idx] = t;
            minHeapify(arr, min_idx, heap_size);
        }
    }
    private static void buildMinHeap(ListNode[] arr, int heap_size){
        for(int i = heap_size/2 - 1; i >= 0; i--){
            minHeapify(arr, i, heap_size);
        }
    }

    // #25
    // reverse a linked list every k elements
    //1->2->3->4->5->6, 3  --->  3->2->1->6->5->4
    public static ListNode reverseKGroup(ListNode head, int k) {
        if(head == null || k <= 1) return head;
        ListNode res = new ListNode();
        ListNode handle;
        res.next = head;
        handle = res;
        while(hasKChildren(handle, k)) handle = reverseGroup(handle, k);
        return res.next;
    }
    private static ListNode reverseGroup(ListNode handle, int k){
        // have at least k node
        ListNode rest, ptr1, ptr2, head;
        head = handle.next;
        ptr2 = head;
        ptr1 = head.next;
        rest = ptr1.next;
        // iterate k - 1 times
        for(int i = 0; i < k - 2; i++){
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
    private static boolean hasKChildren(ListNode handle, int k){
        for(int i = 0; i < k; i++){
            handle = handle.next;
            if(handle == null) return false;
        }
        return true;
    }

    // #29
    // int divide without '/', '*', '%'
    public static int divide(int dividend, int divisor) {
        if((dividend == Integer.MIN_VALUE) && (divisor == -1)) return Integer.MAX_VALUE;
        if(divisor == 1) return dividend;
        if(divisor == - 1) return -dividend;
        if(dividend == divisor) return 1;

        long new_divisor = (divisor > 0)? divisor: ((long)(~divisor))+1;
        long new_dividend = (dividend > 0)? dividend : ((long)(~dividend))+1;
        int res = 0;
        int sign = ((dividend > 0 && divisor > 0 ) || (dividend < 0 && divisor < 0))? 1: -1;

        if(new_dividend < new_divisor) return 0;

        int i;
        do{
            i = 0;
            while (((new_divisor << i) <= new_dividend)) {
                i++;
                if(i >= 1 && leftMoveOverflow(new_divisor << (i-1))){
                    break;
                }
            }
            if(i != 0){
                new_dividend -= (new_divisor<< (i-1));
                res += 1 << (i - 1);
            }
        }while(i != 0);

        return sign*res;
    }
    private static boolean leftMoveOverflow(long n){
        return (((n & 0x40000000) << 1) ^ (n & 0x80000000)) == 0x80000000;
    }

    // #30
    // find possible chain result
    // s = "barfoothefoobarman",
    // words = ["foo","bar"]
    // answer: [0, 9] //('barfoo' at 0, and 'foobar' at 9)
    public static List<Integer> findSubstring(String s, String[] words) {
        List<Integer> res = new ArrayList<>();
        if (s == null || s.length() == 0 || words == null || words.length == 0 || words[0].length() == 0){
            return res;
        }
        Map<String, Integer> words_and_count = new HashMap<>();
        for(var word : words){
            Integer count = words_and_count.getOrDefault(word,0);
            words_and_count.put(word, count+1);
        }
        int s_len = s.length();
        int word_len = words[0].length();
        int words_count = words.length;
        int valid_word_upper_bound = s_len - word_len + 1;
        String[] match_res = new String[valid_word_upper_bound];
        for(int i = 0; i < valid_word_upper_bound; i++){
            for (String word : words) {
                if (s.substring(i, i + word_len).equals(word)) {
                    match_res[i] = word;
                    break;
                }
            }
        }
        int valid_chain_upper_bound = s_len - word_len * words_count + 1;
        Map<String, Integer> check_map = new HashMap<>();
        for(var word : words){
            check_map.put(word,0);
        }
        for(int idx1 = 0; idx1 < valid_chain_upper_bound; idx1++){
            if(canChain(idx1,word_len,words_count,match_res)){
                int count = 0;
                for(var entry : check_map.entrySet()){
                    entry.setValue(0);
                }
                for(int idx2 = idx1; count < words_count; idx2 += word_len){
                    Integer c = check_map.get(match_res[idx2]);
                    check_map.put(match_res[idx2], c+1);
                    count++;
                }
                if(check_map.equals(words_and_count)){
                    res.add(idx1);
                }
            }
        }
        return res;
    }
    private static boolean canChain(int idx, int word_len, int words_count, String[] match_res){
        boolean can_chain = true;
        int count = 0;
        for(; count < words_count; idx += word_len){
            count++;
            if (match_res[idx] == null) {
                can_chain = false;
                break;
            }
        }
        return can_chain;
    }

    // #31
    // next permutation
    // 1,2,3 → 1,3,2
    // 3,2,1 → 1,2,3
    // 1,1,5 → 1,5,1
    public static void nextPermutation(int[] nums){
        int len = nums.length;
        if(len == 0 || len == 1){
            return;
        }
        int i = len - 1;
        for(; i >= 1; i--){
            if(nums[i] > nums[i-1]){
                break;
            }
        }
        if(i == 0){
            reverseArray(nums, 0, len);
            return;
        }
        int swap_idx1 = i - 1;
        int swap_idx2 = i;
        for(; swap_idx2 < len; swap_idx2++){
            if((swap_idx2 == len - 1) || (nums[swap_idx2] > nums[swap_idx1] && nums[swap_idx2+1] <= nums[swap_idx1])){
                break;
            }
        }
        int t = nums[swap_idx1];
        nums[swap_idx1] = nums[swap_idx2];
        nums[swap_idx2] = t;
        reverseArray(nums,i, len);
    }
    private static void reverseArray(int[] nums, int start, int end){
        int idx1 = start;
        int idx2 = end-1;
        while(idx1 < idx2){
            int t = nums[idx1];
            nums[idx1] = nums[idx2];
            nums[idx2] = t;
            idx1++;
            idx2--;
        }
    }

    // #32
    // longest valid parentheses
    public static int longestValidParentheses(String s) {
        int max_len = 0;
        int s_len = s.length();
        if(s_len == 0){
            return 0;
        }
        int[] stack = new int[s_len+1];
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

    // #33
    // search target in concatenated sorted array
    // input: nums = [4,5,6,7,0,1,2], target = 0
    // result: 4
    public static int search(int[] nums, int target) {
        int len = nums.length;
        if(len <= 4){
            for(int i = 0; i < len; i++){
                if(nums[i] == target){
                    return i;
                }
            }
            return -1;
        }
        int l_idx = 0, r_idx = len-1;
        if(target == nums[l_idx]){
            return l_idx;
        }
        else if(target == nums[r_idx]){
            return r_idx;
        }
        int border = searchBorder(nums);
        if(border == -1){
            return binarySearchSubArray(nums,target,0, len);
        }
        if(target == nums[border]){
            return border;
        }
        if(target > nums[r_idx]){
            return binarySearchSubArray(nums, target, 0, border);
        }
        else{
            return binarySearchSubArray(nums, target, border, len);
        }
    }
    private static int binarySearchSubArray(int[] nums, int target, int start, int end){
        while(end - start > 1){
            int mid = (start + end) / 2;
            if(target == nums[mid]){
                return mid;
            }
            else if(target > nums[mid]){
                start = mid;
            }
            else{
                end = mid;
            }
        }
        if(nums[start] == target){
            return start;
        }
        else {
            return -1;
        }
    }
    private static int searchBorder(int[] nums){
        int len = nums.length;
        int left_border = 0, right_border = len;
        while(left_border < right_border){
            int mid_idx = (left_border + right_border) / 2;
            if((mid_idx - 1) > 0  && nums[mid_idx] < nums[mid_idx - 1]){
                return mid_idx;
            }
            else if((mid_idx + 1) < len && nums[mid_idx] > nums[mid_idx + 1]){
                return mid_idx + 1;
            }
            int left_mid_idx = (left_border + mid_idx) / 2;
            int right_mid_idx = (mid_idx + right_border) / 2;
            if(nums[left_mid_idx] > nums[mid_idx] && nums[right_mid_idx] > nums[mid_idx]){
                left_border = left_mid_idx;
                right_border = mid_idx;
            }
            else if(nums[left_mid_idx] < nums[mid_idx] && nums[right_mid_idx] < nums[mid_idx]){
                left_border = mid_idx;
                right_border = right_mid_idx;
            }
            else if(nums[left_mid_idx] < nums[mid_idx] && nums[right_mid_idx] > nums[mid_idx]){
                if(nums[mid_idx] > nums[left_border]){
                    left_border = mid_idx;
                }
                else{
                    right_border = mid_idx;
                }
            }
            else{
                return -1;
            }
        }
        return -1;
    }
    //left_mid  right_mid    result
    // >mid       >mid    border in left
    // <mid       <mid    border in right
    // <mid       >mid       depends
    // >mid       <mid     not possible
    // else       else     nums is order


    // #96
    // count of all binary search tree given range [1,n]
    //  3 -> 5
    //    1         3     3      2      1
    //     \       /     /      / \      \
    //      3     2     1      1   3      2
    //     /     /       \                 \
    //    2     1         2                 3
    public static int numTrees(int n) {
        int[] G = new int[n+1];
        G[0] = 1;
        G[1] = 1;
        for(int i = 2; i <= n; i++){
            G[i] = 0;
            for(int j = 1; j <= i; j++){
                G[i] += G[j-1]*G[i-j];
            }
        }
        return G[n];
    }
}
