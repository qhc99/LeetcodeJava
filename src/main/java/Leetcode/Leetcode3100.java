package Leetcode;

import java.util.*;

public class Leetcode3100 {
    /**
     * #3069
     * 
     * @param nums
     * @return
     */
    public int[] resultArray(int[] nums) {
        int[] res = new int[nums.length];
        List<Integer> a = new ArrayList<>();
        List<Integer> b = new ArrayList<>();
        a.add(nums[0]);
        b.add(nums[1]);
        for (int i = 2; i < nums.length; i++) {
            if (a.getLast() > b.getLast()) {
                a.add(nums[i]);
            } else {
                b.add(nums[i]);
            }
        }
        for (int i = 0; i < res.length; i++) {
            res[i] = i < a.size() ? a.get(i) : b.get(i - a.size());
        }
        return res;
    }

    /**
     * #3072
     * 
     * @param nums
     * @return
     */
    public int[] resultArray2(int[] nums) {
        int[] res = new int[nums.length];
        List<Integer> a = new ArrayList<>();
        List<Integer> b = new ArrayList<>();

        a.add(nums[0]);
        b.add(nums[1]);
        int max = 1_000_000_000;
        var st1 = new SegmentTree(1, max);
        var st2 = new SegmentTree(1, max);
        st1.add(nums[0]);
        st2.add(nums[1]);
        for (int i = 2; i < nums.length; i++) {
            var c1 = st1.queryCount(nums[i] + 1, max);
            var c2 = st2.queryCount(nums[i] + 1, max);
            if (c1 > c2 || (c1 == c2 && b.size() >= a.size())) {
                a.add(nums[i]);
                st1.add(nums[i]);

            } else if (c1 < c2 || (c1 == c2 && b.size() < a.size())) {
                b.add(nums[i]);
                st2.add(nums[i]);
            }
        }
        for (int i = 0; i < res.length; i++) {
            res[i] = i < a.size() ? a.get(i) : b.get(i - a.size());
        }
        return res;
    }

    static class SegmentTree {
        SegmentTree left;
        SegmentTree right;
        int rangeLeft;
        int rangeRight;
        int count;

        SegmentTree(int rl, int rr) {
            rangeLeft = rl;
            rangeRight = rr;
        }

        void add(int v) {
            count++;
            if (rangeLeft == rangeRight) {
                return;
            }
            int mid = rangeLeft + (rangeRight - rangeLeft) / 2;
            if (v <= mid) {
                if (left == null)
                    left = new SegmentTree(rangeLeft, mid);
                left.add(v);
            }
            if (v >= mid + 1) {
                if (right == null)
                    right = new SegmentTree(mid + 1, rangeRight);
                right.add(v);
            }
        }

        int queryCount(int l, int r) {
            if (l == rangeLeft && r == rangeRight) {
                return count;
            }
            int mid = rangeLeft + (rangeRight - rangeLeft) / 2;
            int res = 0;
            if (l <= mid && left != null) {
                res += left.queryCount(l, Math.min(mid, r));
            }
            if (r >= mid + 1 && right != null) {
                res += right.queryCount(Math.max(mid + 1, l), r);
            }
            return res;
        }
    }
}
