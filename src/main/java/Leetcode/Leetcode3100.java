package Leetcode;

import java.util.*;

public class Leetcode3100 {
    /**
     * #3069
     * 
     * @param nums
     * @return
     */
    public int[] resultArray(int[] nums) {
        int[] res = new int[nums.length];
        List<Integer> a = new ArrayList<>();
        List<Integer> b = new ArrayList<>();
        a.add(nums[0]);
        b.add(nums[1]);
        for (int i = 2; i < nums.length; i++) {
            if (a.getLast() > b.getLast()) {
                a.add(nums[i]);
            } else {
                b.add(nums[i]);
            }
        }
        for (int i = 0; i < res.length; i++) {
            res[i] = i < a.size() ? a.get(i) : b.get(i - a.size());
        }
        return res;
    }
}
