package Leetcode;

import java.util.*;
import java.util.stream.Collectors;

public class Leetcode2300 {

    /**
     * #2243
     * @param s
     * @param k
     * @return
     */
    public String digitSum(String s, int k) {
        while (s.length() > k) {
            int sum = 0;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                if (i % k == 0 && i != 0) {
                    sb.append(sum);
                    sum = 0;
                }
                sum += s.charAt(i) - '0';
            }
            sb.append(sum);
            s = sb.toString();
        }
        return s;
    }

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
     * #2261
     * @param nums
     * @param k
     * @param p
     * @return
     */
    public int countDistinct(int[] nums, int k, int p) {
        var root = new PrefixNode();
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            var ptr = root;
            for (int c = 0, j = i; j < nums.length && (nums[j] % p == 0 ? 1 : 0)
                    + c <= k; c += (nums[j] % p == 0 ? 1 : 0), j++) {
                var n = nums[j];
                if (!ptr.children.containsKey(n)) {
                    res++;
                }
                ptr = ptr.children.computeIfAbsent(n, key -> new PrefixNode());
            }
        }
        return res;
    }

    static class PrefixNode {
        Map<Integer, PrefixNode> children = new HashMap<>();
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
