package Leetcode;

public class Leetcode2300 {
    /**
     * #2270
     * 
     * @param nums
     * @return
     */
    public int waysToSplitArray(int[] nums) {
        long[] n = new long[nums.length];
        n[0] = nums[0];
        for (int i = 1; i < n.length; i++) {
            n[i] += n[i - 1] + nums[i];
        }
        int res = 0;
        for (int i = 0; i < n.length - 1; i++) {
            if (n[i] >= n[n.length - 1] - n[i]) {
                res++;
            }
        }
        return res;
    }
}
