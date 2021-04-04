package Leetcode;

import java.util.*;

public class Leetcode200{

    /**
     * #152
     * <br/>乘积最大子数组
     * @param nums numbers
     * @return max sub array multiple
     */
    public static int maxProduct(int[] nums) {
        int maxF = nums[0], minF = nums[0], ans = nums[0];
        int length = nums.length;
        for (int i = 1; i < length; ++i) {
            int mx = maxF, mn = minF;
            maxF = Math.max(mx * nums[i], Math.max(nums[i], mn * nums[i]));
            minF = Math.min(mn * nums[i], Math.min(nums[i], mx * nums[i]));
            ans = Math.max(maxF, ans);
        }
        return ans;
    }

    /**
     * #173
     */
    public static class BSTIterator {
        private final Deque<TreeNode> stack = new LinkedList<>();
        private TreeNode ptr;
        private boolean poppedBefore = false;
        private boolean finish = false;

        public BSTIterator(TreeNode root) {
            ptr = root;
        }

        public int next() {
            if(finish){
                throw new NoSuchElementException("Iterate finish.");
            }

            while (ptr != null) {
                if (ptr.left != null && !poppedBefore) // if popped before, walk to right
                {
                    stack.push(ptr);
                    ptr = ptr.left;
                }
                else {
                    var t = ptr;
                    if (ptr.right != null) {
                        ptr = ptr.right;
                        poppedBefore = false;
                    }
                    else {
                        if (stack.size() != 0) {
                            ptr = stack.pop();
                            poppedBefore = true;
                        }
                        else {
                            ptr = null;
                        }
                    }
                    return t.val;
                }
            }
            finish = true;
            throw new NoSuchElementException("Iterate finish.");
        }

        public boolean hasNext() {
            return !finish && ptr != null;
        }
    }

    /**
     * #190
     * @param n number
     * @return binary reversed
     */
    public static int reverseBits(int n) {
        int M1 = 0x55555555; // 01010101010101010101010101010101
        int M2 = 0x33333333; // 00110011001100110011001100110011
        int M4 = 0x0f0f0f0f; // 00001111000011110000111100001111
        int M8 = 0x00ff00ff; // 00000000111111110000000011111111
        n = (n >>> 1) & M1 | ((n & M1) << 1);
        n = (n >>> 2) & M2 | ((n & M2) << 2);
        n = (n >>> 4) & M4 | ((n & M4) << 4);
        n = (n >>> 8) & M8 | ((n & M8) << 8);
        return n >>> 16 | n << 16;
    }
}
