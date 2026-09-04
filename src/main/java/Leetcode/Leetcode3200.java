package Leetcode;

public class Leetcode3200 {
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
}
