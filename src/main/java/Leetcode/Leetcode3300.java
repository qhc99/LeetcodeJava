package Leetcode;

public class Leetcode3300 {
    /**
     * #3202
     * @param nums
     * @param k
     * @return
     */
    public int maximumLength(int[] nums, int k) {
        int[] len = new int[k];
        int max = 0;
        for (var n : nums) {
            len[n % k]++;
            max = Math.max(max, len[n % k]);
        }
        return max;
    }
}
