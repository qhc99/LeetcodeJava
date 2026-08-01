package Leetcode;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

public class Leetcode1900 {
    /**
     * #1851
     * 
     * @param intervals
     * @param queries
     * @return
     */
    public int[] minInterval(int[][] intervals, int[] queries) {
        var root = new RangeNode(1, 10_000_000);
        for (var it : intervals) {
            root.insert(it[0], it[1], it[1] + 1 - it[0]);
        }
        int[] res = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            res[i] = root.query(queries[i]);
            if (res[i] == Integer.MAX_VALUE)
                res[i] = -1;
        }
        return res;
    }

    static class RangeNode {
        int l;
        int r;
        RangeNode left;
        RangeNode right;
        int minSize = Integer.MAX_VALUE;

        RangeNode(int l, int r) {
            this.l = l;
            this.r = r;
        }

        void insert(int s, int e, int size) {
            int mid = l + (r - l) / 2;
            if (s == l && e == r) {
                minSize = Math.min(minSize, size);
                return;
            }
            if (s <= mid) {
                if (left == null)
                    left = new RangeNode(l, mid);
                left.insert(s, Math.min(mid, e), size);
            }
            if (e >= mid + 1) {
                if (right == null)
                    right = new RangeNode(mid + 1, r);
                right.insert(Math.max(s, mid + 1), e, size);
            }
        }

        int query(int q) {
            int mid = l + (r - l) / 2;
            if (q <= mid) {
                if (left == null)
                    return minSize;
                return Math.min(minSize, left.query(q));
            } else {
                if (right == null)
                    return minSize;
                return Math.min(minSize, right.query(q));
            }
        }
    }

    /**
     * #1854
     * 
     * @param logs
     * @return
     */
    public int maximumPopulation(int[][] logs) {
        Queue<int[]> birth = new PriorityQueue<>(
                (a, b) -> Integer.compare(a[0], b[0]));
        Queue<int[]> death = new PriorityQueue<>(
                (a, b) -> Integer.compare(a[1], b[1]));
        for (var l : logs) {
            birth.add(l);
            death.add(l);
        }
        int maxP = 0;
        int p = 0;
        int year = 0;
        while (!birth.isEmpty()) {
            var b = birth.poll();
            var y = b[0];
            p++;
            while (!death.isEmpty() && death.peek()[1] <= y) {
                death.poll();
                p--;
            }
            if (p > maxP) {
                maxP = p;
                year = y;
            }
        }
        return year;
    }

    /**
     * #1899
     * 
     * @param triplets
     * @param target
     * @return
     */
    public boolean mergeTriplets(int[][] triplets, int[] target) {
        var res = Arrays.stream(triplets)
                .filter(t -> t[0] <= target[0] && t[1] <= target[1]
                        && t[2] <= target[2])
                .reduce((a, b) -> new int[] { Math.max(a[0], b[0]),
                        Math.max(a[1], b[1]), Math.max(a[2], b[2]) });
        if (res.isEmpty())
            return false;

        return Arrays.equals(target, res.get());
    }
}
