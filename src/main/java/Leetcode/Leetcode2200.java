package Leetcode;

import java.util.*;

public class Leetcode2200 {
    /**
     * #2104
     * 
     * @param nums
     * @return
     */
    public long subArrayRanges(int[] nums) {
        int[] min = new int[nums.length];
        int[] max = new int[nums.length];
        Stack<Integer> incStack = new Stack<>();
        for (int i = 0; i < nums.length; i++) {
            while (!incStack.isEmpty() || nums[i] < incStack.peek()) {
                min[incStack.pop()] = i;
            }
            incStack.add(i);

        }
        return 0;
    }

    /**
     * #2145
     * 
     * @param differences
     * @param lower
     * @param upper
     * @return
     */
    public int numberOfArrays(int[] differences, int lower, int upper) {
        long[] diff = new long[differences.length];
        for (int i = 0; i < differences.length; i++)
            diff[i] = differences[i];
        for (int i = 1; i < differences.length; i++)
            diff[i] += diff[i - 1];
        long min = Arrays.stream(diff).min().getAsLong();
        long max = Arrays.stream(diff).max().getAsLong();
        // offset + min >= lower
        // offset >= lower
        // offset + max <= upper
        // offset <= upper
        var l = lower - min;
        l = Math.max(l, lower);
        var r = upper - max;
        r = Math.min(upper, r);
        return Math.max(0, (int) (r + 1 - l));
    }

    /**
     * #2150
     * 
     * @param nums
     * @return
     */
    public List<Integer> findLonely(int[] nums) {
        Map<Integer, Integer> count = new HashMap<>();
        for (var n : nums)
            count.put(n, 1 + count.getOrDefault(n, 0));
        List<Integer> res = new ArrayList<>();
        for (var n : nums) {
            if (count.getOrDefault(n, 0) == 1
                    && count.getOrDefault(n + 1, 0) == 0
                    && count.getOrDefault(n - 1, 0) == 0) {
                res.add(n);
            }
        }
        return res;

    }
}
