package Leetcode;

import java.util.*;
import java.util.stream.IntStream;

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
     * #525
     * <br/> 连续数组
     * <br/>给定一个二进制数组 nums , 找到含有相同数量的 0 和 1 的最长连续子数组，并返回该子数组的长度。
     * @param nums nums
     * @return len
     */
    public static int findMaxLength(int[] nums) {
        int len = nums.length;
        Map<Integer, Integer> map = new HashMap<>();
        int max = 0;
        for(int i = 0; i < len; i++){
            if(nums[i] == 0){
                nums[i] = -1;
            }
            if(i > 0){
                nums[i] += nums[i-1];
            }
            if(nums[i] == 0){
                max = Math.max(i+1, max);
            }
            if(!map.containsKey(nums[i])){
                map.put(nums[i], i);
            }
            else {
                max = Math.max(i - map.get(nums[i]), max);
            }
        }

        return max;
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
