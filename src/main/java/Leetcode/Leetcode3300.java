package Leetcode;

public class Leetcode3300 {
    /**
     * #3202
     * @param nums
     * @param k
     * @return
     */
    public int maximumLength(int[] nums, int k) {
        int[][] dp = new int[k][k];
        int max = 0;
        for (var n : nums) {
            n %= k;
            for (int p = 0; p < k; p++) {
                dp[p][n] = dp[n][p] + 1;
                max = Math.max(dp[p][n], max);
            }
        }
        return max;
    }
}
