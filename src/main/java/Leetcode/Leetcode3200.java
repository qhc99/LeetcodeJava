package Leetcode;

import java.util.*;

public class Leetcode3200 {

    /**
     * #3161
     * 
     * @param queries
     * @return
     */
    public List<Boolean> getResults(int[][] queries) {
        List<Boolean> res = new ArrayList<>();
        TreeMap<Integer, Integer> list = new TreeMap<>();
        list.put(0, 0);
        for (var query : queries) {
            var block = query[1];
            if (query[0] == 1) {
                var floorEntry = list.floorEntry(block);
                var ceilEntry = list.ceilingEntry(block);
                var v = Math.max(block - floorEntry.getKey(),
                        floorEntry.getValue());
                list.put(block, v);
                if (ceilEntry != null)
                    list.put(ceilEntry.getKey(), Math.max(ceilEntry.getKey() - block, v));
                
            } else {
                var floorEntry = list.floorEntry(block);
                var v = Math.max(block - floorEntry.getKey(),
                        floorEntry.getValue());
                res.add(v >= query[2]);
            }
        }
        return res;
    }

    /**
     * #3163
     * 
     * @param word
     * @return
     */
    public String compressedString(String word) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        char chr = ' ';
        for (var c : word.toCharArray()) {
            if (c != chr) {
                if (count > 0) {
                    sb.append(count);
                    sb.append(chr);
                    count = 0;
                }
                chr = c;
            }
            count += 1;
            if (count > 9) {
                sb.append(count);
                sb.append(chr);
                count -= 9;
            }
        }
        if (count > 0) {
            sb.append(count);
            sb.append(chr);
        }
        return sb.toString();
    }

    /**
     * #3191
     * 
     * @param nums
     * @return
     */
    public int minOperations(int[] nums) {
        int res = 0;
        for (int i = 0; i < nums.length - 2; i++) {
            if (nums[i] == 0) {
                res++;
                for (int j = i; j < i + 3; j++)
                    nums[j] = Math.abs(nums[j] - 1);
            }
        }
        if (nums[nums.length - 1] != 1 || nums[nums.length - 2] != 1)
            return -1;
        return res;
    }

    /**
     * #3192
     * 
     * @param nums
     * @return
     */
    public int minOperations2(int[] nums) {
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            var v = Math.abs(nums[i] - res % 2);
            if (v == 0)
                res++;
        }
        return res;
    }
}
