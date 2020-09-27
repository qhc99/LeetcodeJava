package Leetcode;


public class Leetcode450 {

    /**
     * #404
     * <br>左叶子之和
     * <pre>
     *     3
     *    / \
     *   9  20
     *     /  \
     *    15   7
     *
     * 在这个二叉树中，有两个左叶子，分别是 9 和 15，所以返回 24
     * </pre>
     *
     * @param root root of bTree
     * @return sum of left leaves
     */
    @SuppressWarnings("unused")
    public static int sumOfLeftLeaves(TreeNode root) {
        if (root == null) {
            return 0;
        }
        else {
            if (root.left == null) {
                return sumOfLeftLeaves(root.right);
            }
            else if (root.left.left == null && root.left.right == null) {
                return root.left.val + sumOfLeftLeaves(root.right);
            }
            else {
                return sumOfLeftLeaves(root.left) + sumOfLeftLeaves(root.right);
            }
        }
    }

    /**
     * #410
     * <br>输入:
     * <br>nums = [7,2,5,10,8]
     * <br>m = 2
     * <br>输出:
     * <br>18
     * <br>解释:
     * <br>一共有四种方法将nums分割为2个子数组。
     * <br>其中最好的方式是将其分为[7,2,5] 和 [10,8]，
     * <br>因为此时这两个子数组各自的和的最大值为18，在所有情况中最小。
     *
     * @param nums array
     * @param m    number of group
     * @return min group sum
     */
    @SuppressWarnings("unused")
    public static int splitArray(int[] nums, int m) {
        long sum = 0;
        long max = 0;
        for (int num : nums) {
            sum += num;
            max = Math.max(max, num);
        }
        while (max < sum) {
            long mid = (sum - max) / 2 + max;
            if (canSplit(nums, mid, m)) {
                sum = mid;
            }
            else {
                max = mid + 1;
            }
        }
        return (int) max;
    }

    private static boolean canSplit(int[] nums, long sum, int limit) {
        int split = 1;
        int tSum = 0;
        for (var n : nums) {
            if (tSum + n <= sum) {
                tSum += n;
            }
            else {
                tSum = n;
                split++;
                if (split > limit) {
                    return false;
                }
            }
        }
        return split <= limit;
    }
}
