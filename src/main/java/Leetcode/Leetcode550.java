package Leetcode;

import java.util.*;

public class Leetcode550 {
    /**
     * #501
     * <br/>二叉搜索树中的众数
     *
     * @param root tree
     * @return res
     */
    @SuppressWarnings("unused")
    public static int[] findMode(TreeNode root) {
        var s = new Statistic();
        dfs(root, s);
        int[] mode = new int[s.answer.size()];
        for (int i = 0; i < s.answer.size(); ++i) {
            mode[i] = s.answer.get(i);
        }
        return mode;
    }

    public static void dfs(TreeNode o, Statistic solver) {
        if (o == null) {
            return;
        }
        dfs(o.left, solver);
        solver.update(o.val);
        dfs(o.right, solver);
    }

    static class Statistic {
        List<Integer> answer = new ArrayList<>();
        int base, count, maxCount;

        public void update(int x) {
            if (x == base) {
                ++count;
            }
            else {
                count = 1;
                base = x;
            }
            if (count == maxCount) {
                answer.add(base);
            }
            if (count > maxCount) {
                maxCount = count;
                answer.clear();
                answer.add(base);
            }
        }
    }

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
     *
     * @param root tree root
     * @return converted tree
     */
    @SuppressWarnings("unused")
    public static TreeNode convertBST(TreeNode root) {
        traverseAndConvertBST(root, 0);
        return root;
    }

    private static int traverseAndConvertBST(TreeNode r, int sum) {
        if (r == null) {
            return sum;
        }
        sum = traverseAndConvertBST(r.right, sum);
        r.val += sum;
        sum = r.val;
        sum = traverseAndConvertBST(r.left, sum);
        return sum;
    }
}
