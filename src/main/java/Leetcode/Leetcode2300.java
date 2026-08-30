package Leetcode;

import java.util.*;
import java.util.stream.Collectors;

public class Leetcode2300 {

    /**
     * #2248
     * @param nums
     * @return
     */
    public List<Integer> intersection(int[][] nums) {
        var res = Arrays.stream(nums[0]).boxed().collect(Collectors.toSet());
        for (int i = 1; i < nums.length; i++)
            res.retainAll(
                    Arrays.stream(nums[i]).boxed().collect(Collectors.toSet()));
        return res.stream().sorted().toList();
    }

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
