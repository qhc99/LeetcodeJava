package Leetcode;

import java.util.*;


public class Leetcode600 {
    /**
     * #560
     * <br>find the count of continue sub-arrays which sum is k<br>
     * [1, 2, 3, 4], 3 ---> 2    (answer: [1, 2] and [3])
     *
     * @param nums array
     * @param k    sum target
     * @return count
     */
    @SuppressWarnings("unused")
    public static int subarraySum(int[] nums, int k) {
        int count = 0, pre = 0;
        HashMap<Integer, Integer> mp = new HashMap<>();
        mp.put(0, 1);
        for (int num : nums) {
            pre += num;
            if (mp.containsKey(pre - k)) {
                count += mp.get(pre - k);
            }
            mp.put(pre, mp.getOrDefault(pre, 0) + 1);
        }
        return count;
    }
}
