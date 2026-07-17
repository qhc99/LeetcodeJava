package Leetcode;

@SuppressWarnings({"JavaDoc"})
public class Leetcode1500 {
    /**
     * #1446
     *
     * @param s
     * @return
     */
    public static int maxPower(String s) {
        int left = 0, right = 1;
        int ans = 1;
        for (; right < s.length(); right++) {
            if (s.charAt(left) != s.charAt(right)) {
                left = right;
            }
            ans = Math.max(right + 1 - left, ans);
        }
        return ans;
    }
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
