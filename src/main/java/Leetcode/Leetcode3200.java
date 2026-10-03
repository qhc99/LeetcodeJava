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
        var max = 5_0000;
        TreeSet<Integer> tree = new TreeSet<>();
        tree.add(0);
        tree.add(max);
        var seg = new BlockSeg(max);
        seg.insert(max, max);
        for (var q : queries) {
            if (q[0] == 1) {
                var floor = Optional.ofNullable(tree.floor(q[1] - 1)).orElse(0);
                var ceil = Optional.ofNullable(tree.ceiling(q[1] + 1))
                        .orElse(max);
                seg.insert(q[1], q[1] - floor);
                seg.insert(ceil, ceil - q[1]);
                tree.add(q[1]);
            } else {
                var floor = Optional.ofNullable(tree.floor(q[1])).orElse(0);
                var b = seg.query(0, floor);
                b = Math.max(b, q[1] - floor);
                res.add(b >= q[2]);
            }
        }
        return res;
    }

    static class BlockSeg {
        int[] seg;
        int max = 0;

        BlockSeg(int m) {
            max = m;
            seg = new int[(m + 1) * 4 + 1];
        }

        void _insert(int idx, int val, int id, int l, int r) {
            if (l == r) {
                seg[id] = val;
                return;
            }
            int mid = l + (r - l) / 2;
            if (idx <= mid) {
                _insert(idx, val, id * 2, l, mid);
            } else {
                _insert(idx, val, id * 2 + 1, mid + 1, r);
            }
            seg[id] = Math.max(seg[id * 2], seg[id * 2 + 1]);
        }

        void insert(int idx, int val) {
            _insert(idx, val, 1, 0, max);
        }

        int query(int rangeL, int rangeR) {
            return _query(rangeL, rangeR, 1, 0, max);
        }

        int _query(int rangeL, int rangeR, int id, int l, int r) {
            if (rangeL <= l && rangeR >= r) {
                return seg[id];
            }
            int mid = l + (r - l) / 2;
            int res = Integer.MIN_VALUE;
            if (rangeL <= mid) {
                res = Math.max(res,
                        _query(rangeL, Math.min(mid, rangeR), id * 2, l, mid));
            }
            if (rangeR >= mid + 1) {
                res = Math.max(res, _query(Math.max(rangeL, mid + 1), rangeR,
                        id * 2 + 1, mid + 1, r));
            }
            return res;
        }
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
