package Leetcode;


import java.util.*;

public class Leetcode100 {

    /**
     * #55
     * <br/>跳跃游戏
     * <br/>
     * <pre>
     * 输入: [2,3,1,1,4]
     * 输出: true
     * 解释: 我们可以先跳 1 步，从位置 0 到达 位置 1, 然后再从位置 1 跳 3 步到达最后一个位置。
     * </pre>
     * @param nums int array
     * @return res
     */
    public static boolean canJump(int[] nums) {
        int remotest = 0;
        for(int i = 0; i < nums.length; i++){
            if(i <= remotest){
                remotest = Math.max(remotest, i + nums[i]);
                if(remotest >= nums.length - 1){
                    return true;
                }
            }
        }
        return remotest >= nums.length - 1;
    }

    /**
     * #64
     * 说明：每次只能向下或者向右移动一步。
     * (上, 左)
     * 输入:
     * [
     * [1,3,1],
     * [1,5,1],
     * [4,2,1]
     * ]
     * 输出: 7
     * 解释: 因为路径 1→3→1→1→1 的总和最小。
     *
     * @param grid int graph
     * @return path sum
     */
    @SuppressWarnings("unused")
    public static int minPathSum(int[][] grid) {
        int r_len = grid.length;
        int c_len = grid[0].length;
        for (int s = 1; s <= r_len + c_len - 2; s++) {
            for (int i = 0; i < r_len && i <= s; i++) {
                int j = s - i;
                if (j < 0 || j >= c_len) {
                    continue;
                }
                grid[i][j] += minPath(i, j, grid);
            }
        }
        return grid[r_len - 1][c_len - 1];
    }

    private static int minPath(int i, int j, int[][] cache) {
        double u = (i - 1 < 0) ? Double.POSITIVE_INFINITY : cache[i - 1][j];
        double l = (j - 1 < 0) ? Double.POSITIVE_INFINITY : cache[i][j - 1];
        return (int) Math.min(l, u);
    }

    /**
     * #78
     * <br/>子集
     * <pre>
     * 输入: nums = [1,2,3]
     * 输出:
     * [
     *   [3],
     *  [1],
     *  [2],
     *  [1,2,3],
     *  [1,3],
     *  [2,3],
     *  [1,2],
     *  []
     * ]
     * </pre>
     *
     * @param nums int array
     * @return subsets of nums
     */
    @SuppressWarnings("unused")
    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        recursiveSubsets(nums, 0, res);
        return res;
    }

    private static void recursiveSubsets(int[] nums, int idx, List<List<Integer>> res) {
        if (idx == nums.length) {
            res.add(new ArrayList<>());
        }
        else {
            recursiveSubsets(nums, idx + 1, res);
            List<List<Integer>> temp = new ArrayList<>();
            for (var list : res) {
                List<Integer> newList = new ArrayList<>(list);
                newList.add(nums[idx]);
                temp.add(newList);
            }
            res.addAll(temp);
        }
    }

    /**
     * #96
     *
     * <br>count of all binary search tree given range [1,n]
     * <pre>
     *  3 -> 5
     *    1         3     3      2      1
     *     \       /     /      / \      \
     *      3     2     1      1   3      2
     *     /     /       \                 \
     *    2     1         2                 3
     * </pre>
     *
     * @param n range upper bound
     * @return count
     */
    @SuppressWarnings("unused")
    public static int numTrees(int n) {
        int[] G = new int[n + 1];
        G[0] = 1;
        G[1] = 1;
        for (int i = 2; i <= n; i++) {
            G[i] = 0;
            for (int j = 1; j <= i; j++) {
                G[i] += G[j - 1] * G[i - j];
            }
        }
        return G[n];
    }
}
