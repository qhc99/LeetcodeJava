package src.main;

import java.util.*;

public class LeetCode100 {
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
            }else if(stack_ptr == 0 && left_count <= n-1){
                strb.append("(");
                RecursiveGenerateParenthesis(strb, str_len, left_count+1, stack_ptr+1, res, n);
            }else{
                strb.append(")");
                RecursiveGenerateParenthesis(strb, str_len, left_count, stack_ptr-1, res, n);
            }
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