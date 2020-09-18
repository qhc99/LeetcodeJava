package Leetcode;


public class Leetcode450 {
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
