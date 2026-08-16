package Leetcode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.Queue;

public class Leetcode3000 {
    /**
     * #2970,2972
     * 
     * @param nums
     * @return
     */
    public int incremovableSubarrayCount(int[] nums) {
        int l = 1;
        int r = nums.length;

        for (; l < nums.length; l++) {
            if (nums[l - 1] >= nums[l])
                break;
        }
        l = Math.min(l, r - 1);
        long res = 0;// [l,r)
        while (r > 0) {
            res += l + 1;
            r--;
            if (r < 0 || nums[r] >= (r + 1 < nums.length ? nums[r + 1]
                    : Integer.MAX_VALUE))
                break;
            while ((l - 1 >= 0 ? nums[l - 1] : Integer.MIN_VALUE) >= nums[r]
                    || r - l < 1) {
                l--;
            }
            l++;
        }
        return (int) res;
    }

    /**
     * #2931
     * 
     * @param values
     * @return
     */
    public long maxSpending(int[][] values) {
        Queue<int[]> queue = new PriorityQueue<>((a, b) -> Integer
                .compare(values[a[0]][a[1]], values[b[0]][b[1]]));
        long res = 0;
        long d = 1;
        int n = values[0].length;
        for (int s = 0; s < values.length; s++) {
            queue.add(new int[] { s, n - 1 });
        }
        while (!queue.isEmpty()) {
            var shopItem = queue.poll();
            res += values[shopItem[0]][shopItem[1]] * d++;
            shopItem[1]--;
            if (shopItem[1] >= 0)
                queue.add(shopItem);
        }
        return res;
    }

    /**
     * #2945
     * 
     * @param nums
     * @return
     */
    public int findMaximumLength(int[] nums) {
        Deque<Integer> deque = new ArrayDeque<>();
        long[] sum = new long[nums.length + 1];
        long[] last = new long[nums.length + 1];
        int[] dp = new int[nums.length + 1];
        for (int i = 1; i <= nums.length; i++)
            sum[i] = nums[i - 1] + sum[i - 1];
        deque.add(0);
        for (int i = 1; i <= nums.length; i++) {
            int j = deque.peekFirst();
            while (!deque.isEmpty() && sum[i] >= sum[deque.peekFirst()]
                    + last[deque.peekFirst()]) {
                j = deque.pollFirst();
            }
            deque.addFirst(j);
            dp[i] = dp[j] + 1;
            last[i] = sum[i] - sum[j];
            while (!deque.isEmpty() && sum[i] + last[i] <= sum[deque.peekLast()]
                    + last[deque.peekLast()]) {
                deque.pollLast();
            }
            deque.add(i);

        }
        return dp[nums.length];
    }
}
