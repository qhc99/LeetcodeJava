package src;

import java.util.*;

public class Leetcode101_150 {
    // #105

    // 前序遍历 preorder = [3,9,20,15,7]
    // 中序遍历 inorder = [9,3,15,20,7]
    // result:
    //    3
    //   / \
    //  9  20
    //    /  \
    //   15   7
    public static TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder.length == 0) {
            return null;
        }
        Map<Integer, Integer> m = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            m.put(inorder[i], i);
        }
        return recursiveBuildTree(preorder, 0, preorder.length, 0, m);
    }

    private static TreeNode recursiveBuildTree(int[] p_order, int p_start, int p_end, int i_start, Map<Integer, Integer> m) {
        if (p_end - p_start == 1) {
            return new TreeNode(p_order[p_start]);
        }
        else if (p_end - p_start == 0) {
            return null;
        }
        else {
            TreeNode root = new TreeNode(p_order[p_start]);
            int i_mid = m.get(root.val);
            int l_len = i_mid - i_start;
            root.left = recursiveBuildTree(p_order, p_start + 1, p_start + 1 + l_len, i_start, m);
            root.right = recursiveBuildTree(p_order, p_start + 1 + l_len, p_end, i_start + l_len + 1, m);
            return root;
        }
    }
}
