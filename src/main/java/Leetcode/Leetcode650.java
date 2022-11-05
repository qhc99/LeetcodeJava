package Leetcode;

public class Leetcode650 {
    /**
     * #643
     * @param nums
     * @param k
     * @return
     */
    public static double findMaxAverage(int[] nums, int k) {
        int sum = 0;
        int max = Integer.MIN_VALUE;
        for(int i = 0; i < nums.length; i++){
            sum += nums[i];
            if(i >= k){
                sum -= nums[i-k];
            }
            if(i + 1 >= k){
                max = Math.max(sum,max);
            }

        }
        return max / (double)k;
    }
}
