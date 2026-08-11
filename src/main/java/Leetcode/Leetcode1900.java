package Leetcode;

import java.util.*;

public class Leetcode1900 {

    /**
     * #1802
     * 
     * @param n
     * @param index
     * @param maxSum
     * @return
     */
    public int maxValue(int n, int index, int maxSum) {
        int l = 1, r = maxSum + 1;
        while (r - l > 1) {
            int mid = l + (r - l) / 2;
            var sum = sumOfSeq(mid, n, index);
            if (sum <= maxSum) {
                l = mid;
            } else {
                r = mid;
            }
        }
        return l;
    }

    long sumOfSeq(long last, int n, int idx) {
        var len = Math.min(idx + 1, last);
        var first = Math.max(1, last - idx);
        var s = len * (first + last) / 2;
        s += idx + 1 - len; // fill 1

        len = (Math.min(n - idx, last));
        first = Math.max(1, last - (n - 1 - idx));
        s += len * (first + last) / 2;
        s += n - (idx + len);

        s -= last; // remove duplicate

        return s;
    }

    /**
     * #1813
     */
    public boolean areSentencesSimilar(String sentence1, String sentence2) {
        var arrFirst = sentence1.split(" ");
        var arrSecond = sentence2.split(" ");
        if (arrSecond.length > arrFirst.length) {
            var t = arrFirst;
            arrFirst = arrSecond;
            arrSecond = t;
        }
        int i = 0;
        for (; i < arrFirst.length && i < arrSecond.length
                && arrFirst[i].equals(arrSecond[i]); i++) {
        }

        int j = 1;
        for (; arrFirst.length - j >= 0 && arrSecond.length - j >= 0
                && arrFirst[arrFirst.length - j]
                        .equals(arrSecond[arrSecond.length - j]); j++) {
        }

        return i + j - 1 == arrSecond.length;
    }

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
        // Queue<int[]> birth = new PriorityQueue<>(
        // (a, b) -> Integer.compare(a[0], b[0]));
        // Queue<int[]> death = new PriorityQueue<>(
        // (a, b) -> Integer.compare(a[1], b[1]));
        // for (var l : logs) {
        // birth.add(l);
        // death.add(l);
        // }
        // int maxP = 0;
        // int p = 0;
        // int year = 0;
        // while (!birth.isEmpty()) {
        // var b = birth.poll();
        // var y = b[0];
        // p++;
        // while (!death.isEmpty() && death.peek()[1] <= y) {
        // death.poll();
        // p--;
        // }
        // if (p > maxP) {
        // maxP = p;
        // year = y;
        // }
        // }
        // return year;

        int[] diff = new int[102];
        for (var l : logs) {
            diff[l[0] - 1950]++;
            diff[l[1] - 1950]--;
        }
        int max_people = 0, s = 0, year = 0;
        for (int i = 0; i < 102; i++) {
            s += diff[i];
            if (s > max_people) {
                max_people = s;
                year = i;
            }
        }
        return year + 1950;

    }

    /**
     * #1860
     * 
     * @param memory1
     * @param memory2
     * @return
     */
    public int[] memLeak(int memory1, int memory2) {
        int i = 1;
        for (; true; i++) {
            if (memory1 >= memory2 && memory1 >= i) {
                memory1 -= i;
            } else if (memory1 < memory2 && memory2 >= i) {
                memory2 -= i;
            } else
                break;
        }

        return new int[] { i, memory1, memory2 };
    }

    /**
     * #1870
     * 
     * @param dist
     * @param hour
     * @return
     */
    public int minSpeedOnTime(int[] dist, double hour) {
        int r = Integer.MAX_VALUE;
        int l = 0; // (l,r]
        while (r - l > 1) {
            int mid = l + (r - l) / 2;
            double time = 0;
            for (int i = 0; i < dist.length - 1; i++) {
                time += Math.ceilDiv(dist[i], mid);
            }
            time += dist[dist.length - 1] / (double) mid;
            if (time <= hour)
                r = mid;
            else
                l = mid;
        }
        double time = 0;
        for (int i = 0; i < dist.length - 1; i++) {
            time += Math.ceilDiv(dist[i], r);
        }
        time += dist[dist.length - 1] / (double) r;
        if (time > hour)
            return -1;
        return r;
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
