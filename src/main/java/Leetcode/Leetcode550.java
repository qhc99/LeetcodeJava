package Leetcode;

public class Leetcode550
{
    /**
     * #538
     * <br/>二叉搜索树转换为累加树
     * <pre>
     * 输入: 原始二叉搜索树:
     *               5
     *             /   \
     *            2     13
     *
     * 输出: 转换为累加树:
     *              18
     *             /   \
     *           20     13
     * </pre>
     * @param root tree root
     * @return converted tree
     */
    @SuppressWarnings("unused")
    public static TreeNode convertBST(TreeNode root)
    {
        traverseAndConvertBST(root,0);
        return root;
    }

    private static int traverseAndConvertBST(TreeNode r, int sum)
    {
        if(r == null){
            return sum;
        }
        sum = traverseAndConvertBST(r.right,sum);
        r.val += sum;
        sum = r.val;
        sum = traverseAndConvertBST(r.left,sum);
        return sum;
    }
}
