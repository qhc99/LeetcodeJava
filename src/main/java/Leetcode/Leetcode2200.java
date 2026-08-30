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

    /**
     * #2115
     * 
     * @param recipes
     * @param ingredients
     * @param supplies
     * @return
     */
    public List<String> findAllRecipes(String[] recipes,
            List<List<String>> ingredients, String[] supplies) {
        List<String> res = new ArrayList<>();
        Queue<String> unusedSupplies = new ArrayDeque<>();
        for (var s : supplies)
            unusedSupplies.add(s);
        Map<String, List<String>> unlock = new HashMap<>();
        Map<String, Set<String>> waiting = new HashMap<>();
        for (int i = 0; i < recipes.length; i++) {
            waiting.computeIfAbsent(recipes[i], k -> new HashSet<>())
                    .addAll(ingredients.get(i));
            for (var ingredient : ingredients.get(i))
                unlock.computeIfAbsent(ingredient, k -> new ArrayList<>())
                        .add(recipes[i]);
        }
        while (!unusedSupplies.isEmpty()) {
            var supply = unusedSupplies.poll();
            for (var recipe : unlock.getOrDefault(supply, List.of())) {
                if (waiting.containsKey(supply)) {
                    res.add(supply);
                    waiting.remove(supply);
                    continue;
                }
                var s = waiting.get(recipe);
                if (s != null) {
                    s.remove(supply);
                    if (s.isEmpty()) {
                        waiting.remove(recipe);
                        res.add(recipe);
                        unusedSupplies.add(recipe);
                    }
                }
            }
        }
        return res;
    }

    /**
     * #2134
     * 
     * @param nums
     * @return
     */
    public int minSwaps(int[] nums) {
        int[] prefixSum = new int[nums.length * 2 + 1];
        int res = Integer.MAX_VALUE;
        int countOf1 = 0;
        for (int i = 1; i < prefixSum.length; i++) {
            int idx = (i - 1) % nums.length;
            if (i - 1 < nums.length && nums[i - 1] == 1)
                countOf1++;
            prefixSum[i] = nums[idx] + prefixSum[i - 1];
        }
        if (countOf1 == 0)
            return 0;
        for (int r = countOf1; r < prefixSum.length; r++) {
            var l = r - countOf1;
            res = Math.min(res, countOf1 - (prefixSum[r] - prefixSum[l]));
        }
        return res;
    }

    /**
     * #2145
     * 
     * @param differences
     * @param lower
     * @param upper
     * @return
     */
    public int numberOfArrays(int[] differences, int lower, int upper) {
        long[] diff = new long[differences.length];
        for (int i = 0; i < differences.length; i++)
            diff[i] = differences[i];
        for (int i = 1; i < differences.length; i++)
            diff[i] += diff[i - 1];
        long min = Arrays.stream(diff).min().getAsLong();
        long max = Arrays.stream(diff).max().getAsLong();
        // offset + min >= lower
        // offset >= lower
        // offset + max <= upper
        // offset <= upper
        var l = lower - min;
        l = Math.max(l, lower);
        var r = upper - max;
        r = Math.min(upper, r);
        return Math.max(0, (int) (r + 1 - l));
    }

    /**
     * #2150
     * 
     * @param nums
     * @return
     */
    public List<Integer> findLonely(int[] nums) {
        Map<Integer, Integer> count = new HashMap<>();
        for (var n : nums)
            count.put(n, 1 + count.getOrDefault(n, 0));
        List<Integer> res = new ArrayList<>();
        for (var n : nums) {
            if (count.getOrDefault(n, 0) == 1
                    && count.getOrDefault(n + 1, 0) == 0
                    && count.getOrDefault(n - 1, 0) == 0) {
                res.add(n);
            }
        }
        return res;

    }

    /**
     * #2187
     * @param time
     * @param totalTrips
     * @return
     */
    public long minimumTime(int[] time, int totalTrips) {
        if (time.length == 1)
            return (long) totalTrips * time[0];
        int min = Integer.MAX_VALUE, max = -1;
        for (var t : time) {
            min = Math.min(min, t);
            max = Math.max(t, max);
        }
        long r = (long) totalTrips * max, l = (long) totalTrips / time.length;
        while (r - l > 0) {
            var mid = l + (r - l) / 2;
            long total = 0;
            for (var t : time)
                total += mid / t;
            if (total >= totalTrips) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    /**
     * #2196
     * @param descriptions
     * @return
     */
    public TreeNode createBinaryTree(int[][] descriptions) {
        Map<Integer, TreeNode> map = new HashMap<>();
        Map<Integer, Boolean> isRoot = new HashMap<>();
        for (var des : descriptions) {
            var p = map.computeIfAbsent(des[0], k -> new TreeNode(des[0]));
            var c = map.computeIfAbsent(des[1], k -> new TreeNode(des[1]));
            isRoot.put(des[1], false);
            if (des[2] == 1)
                p.left = c;
            else
                p.right = c;
        }
        for (var e : map.entrySet()) {
            if (isRoot.getOrDefault(e.getKey(), true))
                return e.getValue();
        }
        return null;
    }
}
