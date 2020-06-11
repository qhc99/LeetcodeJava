

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LeetCode {
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
        ListNode(){}
    }
    static public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
  }
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
        if(is_len_one & n == 1) return null;
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
            if(stack_ptr >= 1 & stack_ptr <= n-1 & left_count <= n-1){
                StringBuilder t = new StringBuilder(strb);
                strb.append("(");
                t.append(")");
                RecursiveGenerateParenthesis(strb, str_len, left_count+1, stack_ptr+1, res, n);
                RecursiveGenerateParenthesis(t, str_len, left_count, stack_ptr-1, res, n);
            }else if(stack_ptr == 0 & left_count <= n-1){
                strb.append("(");
                RecursiveGenerateParenthesis(strb, str_len, left_count+1, stack_ptr+1, res, n);
            }else{
                strb.append(")");
                RecursiveGenerateParenthesis(strb, str_len, left_count, stack_ptr-1, res, n);
            }
        }
    }
    //#23
    // merge k sorted lists
    //[[1 4 6] [2 3 5]]  ---> [1 2 3 4 5 6]
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
    //#25
    // reverse a linked list every k elements
    //1->2->3->4->5->6, 3  --->  3->2->1->6->5->4
    public static ListNode reverseKGroup(ListNode head, int k) {
        if(head == null | k <= 1) return head;
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
    //    3
    //   / \
    //  9  20
    //    /  \
    //   15   7
    public static TreeNode buildTree(int[] preorder, int[] inorder) {
        if(preorder.length == 0) return null;
        return recursiveBuildTree(preorder, 0, preorder.length, inorder, 0, inorder.length);
    }
    public static TreeNode recursiveBuildTree(int[] p_order,int p_start,int p_end,int[] i_order,int i_start,int i_end){
        if(p_end - p_start == 1){
            return new TreeNode(p_order[p_start]);
        }else if(p_end - p_start == 0){
            return null;
        } else {
            TreeNode root = new TreeNode(p_order[p_start]);
            int i_mid = i_start;
            for (int i = i_start; i < i_end; i++) {
                if (root.val == i_order[i]) {
                    i_mid = i;
                    break;
                }
            }
            int l_len = i_mid - i_start;
            root.left = recursiveBuildTree(p_order,p_start+1,p_start+1+l_len, i_order, i_start,i_start+l_len);
            root.right = recursiveBuildTree(p_order,p_start+1+l_len,p_end,i_order,i_start+l_len+1,i_end);
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
