package Leetcode;

import java.util.*;

public class Leetcode150
{
    /**
     * #105
     *
     * <br>前序遍历 preorder = [3,9,20,15,7]
     * <br>中序遍历 inorder = [9,3,15,20,7]
     * <br>result:
     * <pre>
     *    3
     *   / \
     *  9  20
     *    /  \
     *   15   7
     * </pre>
     *
     * @param preorder preorder int array
     * @param inorder  inorder int array
     * @return origin tree
     */
    @SuppressWarnings("unused")
    public static TreeNode buildTree(int[] preorder, int[] inorder)
    {
        if (preorder.length == 0)
        {
            return null;
        }
        Map<Integer, Integer> m = new HashMap<>();
        for (int i = 0; i < inorder.length; i++)
        {
            m.put(inorder[i], i);
        }
        return recursiveBuildTree(preorder, 0, preorder.length, 0, m);
    }

    private static TreeNode recursiveBuildTree(int[] p_order, int p_start, int p_end, int i_start, Map<Integer, Integer> m)
    {
        if (p_end - p_start == 1)
        {
            return new TreeNode(p_order[p_start]);
        }
        else if (p_end - p_start == 0)
        {
            return null;
        }
        else
        {
            TreeNode root = new TreeNode(p_order[p_start]);
            int i_mid = m.get(root.val);
            int l_len = i_mid - i_start;
            root.left = recursiveBuildTree(p_order, p_start + 1, p_start + 1 + l_len, i_start, m);
            root.right = recursiveBuildTree(p_order, p_start + 1 + l_len, p_end, i_start + l_len + 1, m);
            return root;
        }
    }


    /**
     * #107
     * <br>层次遍历<br>
     * <pre>
     *  input:[3,9,20,null,null,15,7]
     *     3
     *    / \
     *   9  20
     *     /  \
     *    15   7
     * </pre>
     * <pre>
     * return:
     * [
     *   [15,7],
     *   [9,20],
     *   [3]
     * ]
     * </pre>
     *
     * @param root root
     * @return down to up iteration
     */
    @SuppressWarnings("unused")
    public List<List<Integer>> levelOrderBottom(TreeNode root)
    {
        List<List<Integer>> res = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty())
        {
            int size = queue.size();
            List<Integer> t = new ArrayList<>();
            for (int i = 0; i < size; i++)
            {
                var n = queue.poll();
                if (n != null)
                {
                    t.add(n.val);
                    if (n.left != null)
                    {
                        queue.add(n.left);
                    }
                    if (n.right != null)
                    {
                        queue.add(n.right);
                    }
                }
            }
            if (t.size() != 0)
            {
                res.add(t);
            }
        }
        Collections.reverse(res);
        return res;
    }


    /**
     * #111
     * <br/>给定一个二叉树，找出其最小深度。
     *
     * @param root 二叉树
     * @return 最小深度
     */
    @SuppressWarnings("unused")
    public static int minDepth(TreeNode root)
    {
        if (root == null)
        {
            return 0;
        }
        int left = minDepth(root.left);
        int right = minDepth(root.right);
        int depth = 1;
        if (left == 0)
        {
            depth += right;
        }
        else
        {
            if (right == 0)
            {
                depth += left;
            }
            else
            {
                depth += Math.min(left, right);
            }
        }

        return depth;
    }
}
