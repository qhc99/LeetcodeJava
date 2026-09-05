package Leetcode;

import java.util.*;

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
     * #2912
     * 
     * @param n
     * @param m
     * @param k
     * @param source
     * @param dest
     * @return
     */
    public int numberOfWays(int n, int m, int k, int[] source, int[] dest) {
        int mod = 1_000_000_007;
        long center = Arrays.equals(source, dest) ? 1 : 0;
        long row = source[0] == dest[0] && source[1] != dest[1] ? 1 : 0;
        long col = source[1] == dest[1] && source[0] != dest[0] ? 1 : 0;
        long other = (row == 0 && col == 0 && center == 0) ? 1 : 0;
        for (int i = 1; i <= k; i++) {
            long prev_center = center, prev_row = row, prev_col = col,
                    prev_other = other;
            center = (prev_row + prev_col) % mod;
            row = ((prev_center * (m - 1) + prev_row * (m - 2)) % mod
                    + prev_other) % mod;
            col = ((prev_center * (n - 1) % mod + prev_col * (n - 2) % mod)
                    % mod + prev_other) % mod;
            other = ((prev_row * (n - 1) % mod + prev_col * (m - 1) % mod) % mod
                    + prev_other * (m - 2 + n - 2) % mod) % mod;
        }
        return (int) center;
    }

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

    /**
     * #2958
     * 
     * @param nums
     * @param k
     * @return
     */
    public int maxSubarrayLength(int[] nums, int k) {
        Map<Integer, Integer> count = new HashMap<>();
        int l = 0, res = 0;
        for (int r = 0; r < nums.length; r++) {
            var v = nums[r];
            count.put(v, 1 + count.getOrDefault(v, 0));
            while (count.get(v) > k && l <= r) {
                var numL = nums[l++];
                var cnt = count.get(numL);
                if (cnt == 1)
                    count.remove(numL);
                else
                    count.put(numL, cnt - 1);
            }
            res = Math.max(res, r + 1 - l);
        }
        return res;
    }
}
