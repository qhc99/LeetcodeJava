package Leetcode;

public class Leetcode1500 {
    /**
     * #1448
     * 
     * @param root
     * @return
     */
    public int goodNodes(TreeNode root) {
        return goodNodesVisit(root, Integer.MIN_VALUE);
    }

    public int goodNodesVisit(TreeNode node, int max) {
        if (node == null)
            return 0;
        int res = 0;
        if (node.val >= max)
            res++;
        return res + goodNodesVisit(node.left, Math.max(max, node.val))
                + goodNodesVisit(node.right, Math.max(max, node.val));
    }
}
