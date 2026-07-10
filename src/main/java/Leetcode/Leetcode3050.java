package Leetcode;

public class Leetcode3050 {
    /**
     * #3019
     * 
     * @param s
     * @return
     */
    public int countKeyChanges(String s) {
        s = s.toLowerCase();
        int res = 0;
        var arr = s.toCharArray();
        for (int i = 0; i < s.length() - 1; i++) {
            if (arr[i] != arr[i + 1]) {
                res++;
            }
        }
        return res;
    }

    /**
     * #3034,#3036
     * 
     * @param nums
     * @param pattern
     * @return
     */
    public int countMatchingSubarrays(int[] nums, int[] pattern) {
        if (nums.length - 1 < pattern.length)
            return 0;
        var next = computeNext(pattern);
        int p = 0;
        int res = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            var v = nums[i] < nums[i + 1] ? 1
                    : (nums[i] > nums[i + 1] ? -1 : 0);

            while (p > 0 && pattern[p] != v)
                p = next[p - 1];

            if (pattern[p] == v)
                p++;
            if (p == pattern.length) {
                p = next[p - 1];
                res++;
            }
        }

        return res;
    }

    int[] computeNext(int[] pattern) {
        int[] next = new int[pattern.length];
        int p = 0;
        for (int i = 1; i < pattern.length; i++) {
            while (p > 0 && pattern[i] != pattern[p]) {
                p = next[p - 1];
            }
            if (pattern[p] == pattern[i]) {
                p++;
            }
            next[i] = p;
        }
        return next;
    }
}
