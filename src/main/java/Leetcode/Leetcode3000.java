package Leetcode;

public class Leetcode3000 {
    /**
     * #2970,2972
     * 
     * @param nums
     * @return
     */
    public int incremovableSubarrayCount(int[] nums) {
        int l = 1;
        int r = nums.length;

        for (; l < nums.length; l++) {
            if (nums[l - 1] >= nums[l])
                break;
        }
        l = Math.min(l, r - 1);
        long res = 0;// [l,r)
        while (r > 0) {
            res += l + 1;
            r--;
            if (r < 0 || nums[r] >= (r + 1 < nums.length ? nums[r + 1]
                    : Integer.MAX_VALUE))
                break;
            while ((l - 1 >= 0 ? nums[l - 1] : Integer.MIN_VALUE) >= nums[r]
                    || r - l < 1) {
                l--;
            }
            l++;
        }
        return (int) res;
    }
}
