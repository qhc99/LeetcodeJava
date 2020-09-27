package Leetcode;

public class Leetcode250 {

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
