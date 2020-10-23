package Leetcode;

import java.util.*;

public class Leetcode250 {

    /**
     * #234
     * <br/>回文链表
     * @param head node
     * @return res
     */
    public static boolean isPalindrome(ListNode head) {
        //TODO O(1) memory
        List<Integer> vals = new ArrayList<>();

        // 将链表的值复制到数组中
        ListNode currentNode = head;
        while (currentNode != null) {
            vals.add(currentNode.val);
            currentNode = currentNode.next;
        }

        int front = 0;
        int back = vals.size() - 1;
        while (front < back) {
            if (!vals.get(front).equals(vals.get(back))) {
                return false;
            }
            front++;
            back--;
        }
        return true;
    }

    /**
     * #235
     * <br/>二叉搜索树的最近公共祖先
     *
     * @param root tree
     * @param p    node 1
     * @param q    node 2
     * @return closest ancestor
     */
    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root.val > p.val && root.val > q.val) {
            return lowestCommonAncestor(root.left, p, q);
        }
        else if (root.val < p.val && root.val < q.val) {
            return lowestCommonAncestor(root.right, p, q);
        }
        else {
            return root;
        }
    }
}
