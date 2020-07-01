package src.main;

import java.util.*;

public class LeetCode {
    //#7
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

    //#19
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

    //#22
    //  k pairs parenthesis permutation
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

    //#25
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

    //#105
    //前序遍历 preorder = [3,9,20,15,7]
    //中序遍历 inorder = [9,3,15,20,7]
    //result:
    //    3
    //   / \
    //  9  20
    //    /  \
    //   15   7
    public static TreeNode buildTree(int[] preorder, int[] inorder) {
        if(preorder.length == 0) return null;
        Map<Integer, Integer> m = new HashMap<>();
        for(int i = 0; i < inorder.length; i++){
            m.put(inorder[i], i);
        }
        return recursiveBuildTree(preorder, 0, preorder.length, inorder, 0, inorder.length, m);
    }
    public static TreeNode recursiveBuildTree(int[] p_order,int p_start,int p_end,int[] i_order,int i_start,int i_end,Map<Integer, Integer> m){
        if(p_end - p_start == 1){
            return new TreeNode(p_order[p_start]);
        }else if(p_end - p_start == 0){
            return null;
        } else {
            TreeNode root = new TreeNode(p_order[p_start]);
            int i_mid = m.get(root.val);
            int l_len = i_mid - i_start;
            root.left = recursiveBuildTree(p_order,p_start+1,p_start+1+l_len, i_order, i_start,i_start+l_len,m);
            root.right = recursiveBuildTree(p_order,p_start+1+l_len,p_end,i_order,i_start+l_len+1,i_end,m);
            return root;
        }
    }

    //#560
    // find the count of continue sub-arrays which sum is k
    //[1, 2, 3, 4], 3 ---> 2    ([1, 2] and [3])
    public static int subarraySum(int[] nums, int k) {
        int count = 0, pre = 0;
        HashMap< Integer, Integer > mp = new HashMap < > ();
        mp.put(0, 1);
        for (int num : nums) {
            pre += num;
            if (mp.containsKey(pre - k))
                count += mp.get(pre - k);
            mp.put(pre, mp.getOrDefault(pre, 0) + 1);
        }
        return count;
    }
}
