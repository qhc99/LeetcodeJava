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
}
