package Leetcode;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class Leetcode3050 {
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

    /**
     * #3042,#3045
     * 
     * @param words
     * @return
     */
    public long countPrefixSuffixPairs(String[] words) {
        Trie root = new Trie();
        long res = 0;

        for (var w : words) {
            var ptr = root;
            for (int i = 0; i < w.length(); i++) {
                var p = new Pair(w.charAt(i), w.charAt(w.length() - 1 - i));
                ptr = ptr.child.computeIfAbsent(p, k -> new Trie());
                res += ptr.count;
            }
            ptr.count++;
        }
        return res;

    }

    static record Pair(char s, char e) {
        @Override
        public final int hashCode() {
            return Objects.hash(s, e);
        }

        @Override
        public final boolean equals(Object arg0) {
            if (arg0 instanceof Pair o)
                return o.s == s && o.e == e;
            return false;
        }
    }

    static class Trie {
        public int count;
        public Map<Pair, Trie> child = new HashMap<>();
    }
}

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
