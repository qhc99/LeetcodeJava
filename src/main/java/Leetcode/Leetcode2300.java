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
     * #2251
     * @param flowers
     * @param people
     * @return
     */
    public int[] fullBloomFlowers(int[][] flowers, int[] people) {
        int[] res = new int[people.length];
        var root = new SegTree(1, 1_000_000_000);
        for (var f : flowers)
            root.insert(f[0], f[1], 1);
        for (int i = 0; i < people.length; i++)
            res[i] = root.query(people[i]);
        return res;
    }

    static class SegTree {
        int count, cache, rangeLeft, rangeRight;
        SegTree leftTree, rightTree;

        SegTree(int l, int r) {
            rangeLeft = l;
            rangeRight = r;
        }

        int rangeMid() {
            return rangeLeft + (rangeRight - rangeLeft) / 2;
        }

        SegTree getLeftTree() {
            if (leftTree == null)
                leftTree = new SegTree(rangeLeft, rangeMid());
            return leftTree;
        }

        SegTree getRighTree() {
            if (rightTree == null)
                rightTree = new SegTree(rangeMid() + 1, rangeRight);
            return rightTree;
        }

        int query(int i) {
            if (rangeLeft == rangeRight)
                return count;
            clearCache();
            var rangeMid = rangeMid();
            if (i <= rangeMid && leftTree != null)
                return leftTree.query(i);
            else if (i >= rangeMid + 1 && rightTree != null)
                return rightTree.query(i);
            return 0;
        }

        void clearCache() {
            if (cache > 0) {
                var rangeMid = rangeMid();
                getLeftTree().insert(rangeLeft, rangeMid, cache);
                getRighTree().insert(rangeMid + 1, rangeRight, cache);
                cache = 0;
            }
        }

        void insert(int l, int r, int v) {
            if (rangeLeft == rangeRight) {
                count += v;
                return;
            }
            if (l == rangeLeft && r == rangeRight) {
                cache += v;
                return;
            }
            clearCache();
            var rangeMid = rangeMid();
            if (r >= rangeMid + 1) {
                getRighTree().insert(Math.max(rangeMid + 1, l), r, v);
            }
            if (l <= rangeMid) {
                getLeftTree().insert(l, Math.min(rangeMid, r), v);
            }
        }
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

    /**
     * #2282
     * @param heights
     * @return
     */
    public int[][] seePeople(int[][] heights) {
        int m = heights.length, n = heights[0].length;
        int[][] res = new int[m][n];
        Deque<Integer> incStack = new ArrayDeque<>();
        for (int i = 0; i < m; i++) {
            incStack.clear();
            for (int j = n - 1; j >= 0; j--) {
                int seen = 0, maxBetween = -1;
                while (!incStack.isEmpty()
                        && heights[i][j] >= heights[i][incStack.peekLast()]) {
                    maxBetween = Math.max(maxBetween,
                            heights[i][incStack.pollLast()]);
                    seen++;
                }
                res[i][j] += seen
                        + (!incStack.isEmpty() && maxBetween < heights[i][j] ? 1
                                : 0);
                incStack.addLast(j);
            }
        }

        for (int j = 0; j < n; j++) {
            incStack.clear();
            for (int i = m - 1; i >= 0; i--) {
                int seen = 0, maxBetween = -1;
                while (!incStack.isEmpty()
                        && heights[i][j] >= heights[incStack.peekLast()][j]) {
                    maxBetween = Math.max(maxBetween,
                            heights[incStack.pollLast()][j]);
                    seen++;
                }
                res[i][j] += seen
                        + (!incStack.isEmpty() && maxBetween < heights[i][j] ? 1
                                : 0);
                incStack.addLast(i);
            }
        }

        return res;
    }
}
