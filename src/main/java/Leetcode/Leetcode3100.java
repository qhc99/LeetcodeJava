package Leetcode;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Leetcode3100 {
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

    static class InfiniteStream {
        public InfiniteStream(int[] bits) {
        }

        public int next() {
            return 0;
        }
    }

    /**
     * #3023/3037
     * 
     * @param infiniteStream
     * @param pattern
     * @return
     */
    public int findPattern(InfiniteStream infiniteStream, int[] pattern) {
        return 0;
    }

    /**
     * #3030
     * 
     * @param image
     * @param threshold
     * @return
     */
    public int[][] resultGrid(int[][] image, int threshold) {
        int m = image.length, n = image[0].length;
        int[][] regionCount = new int[m][n];
        int[][] regionSum = new int[m][n];

        int[] ndx = new int[] { 0, 0, 1, -1 };
        int[] ndy = new int[] { 1, -1, 0, 0 };
        for (int i = 0; i < m - 2; i++) {
            for (int j = 0; j < n - 2; j++) {
                int lx = i, rx = i + 3;
                int ly = j, ry = j + 3;
                int sum = 0;
                boolean isRegion = true;
                for (int d = 0; d < 9 && isRegion; d++) {
                    var dx = d / 3;
                    var dy = d % 3;
                    int x = dx + i;
                    int y = dy + j;
                    sum += image[x][y];
                    for (int k = 0; k < 4 && isRegion; k++) {
                        int nx = x + ndx[k];
                        int ny = y + ndy[k];
                        if (nx >= lx && nx < rx && ny >= ly && ny < ry && Math
                                .abs(image[x][y] - image[nx][ny]) > threshold) {
                            isRegion = false;
                        }
                    }
                }
                sum /= 9;
                if (isRegion) {
                    for (int d = 0; d < 9; d++) {
                        var dx = d / 3;
                        var dy = d % 3;
                        int x = dx + i;
                        int y = dy + j;
                        regionSum[x][y] += sum;
                        regionCount[x][y] += 1;
                    }
                }
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                regionSum[i][j] = regionCount[i][j] == 0 ? image[i][j]
                        : regionSum[i][j] / regionCount[i][j];
            }
        }

        return regionSum;
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

    /**
     * #3043
     * 
     * @param arr1
     * @param arr2
     * @return
     */
    public int longestCommonPrefix(int[] arr1, int[] arr2) {
        int res = 0;
        if (arr2.length > arr1.length) {
            var t = arr1;
            arr1 = arr2;
            arr2 = t;
        }
        var root = new IntTrieNode();
        for (var n : arr1) {
            var s = String.valueOf(n);
            var ptr = root;
            for (var c : s.toCharArray()) {
                ptr = ptr.children.computeIfAbsent(c, k -> new IntTrieNode());
            }
        }
        for (var n : arr2) {
            var s = String.valueOf(n);
            var ptr = root;
            int len = 0;
            for (var c : s.toCharArray()) {
                ptr = ptr.children.get(c);
                if (ptr != null) {
                    len++;
                    res = Math.max(len, res);
                } else
                    break;
            }
        }
        return res;
    }

    static class IntTrieNode {
        Map<Character, IntTrieNode> children = new HashMap<>();
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
     * #3071
     * 
     * @param grid
     * @return
     */
    public int minimumOperationsToWriteY(int[][] grid) {
        int min = Integer.MAX_VALUE, n = grid.length;
        Map<Integer, Integer> yCount = new HashMap<>(),
                backgoundCount = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i <= n / 2) {
                    if (i == j || i + j == n - 1)
                        yCount.put(grid[i][j],
                                1 + yCount.getOrDefault(grid[i][j], 0));
                    else
                        backgoundCount.put(grid[i][j],
                                1 + backgoundCount.getOrDefault(grid[i][j], 0));
                } else {
                    if (j == n / 2)
                        yCount.put(grid[i][j],
                                1 + yCount.getOrDefault(grid[i][j], 0));
                    else
                        backgoundCount.put(grid[i][j],
                                1 + backgoundCount.getOrDefault(grid[i][j], 0));
                }
            }
        }
        int yTotal = yCount.values().stream().mapToInt(i -> i).sum(),
                bTotal = backgoundCount.values().stream().mapToInt(i -> i)
                        .sum();
        int[] yOps = new int[] { yTotal - yCount.getOrDefault(0, 0),
                yTotal - yCount.getOrDefault(1, 0),
                yTotal - yCount.getOrDefault(2, 0) };
        int[] bOps = new int[] { bTotal - backgoundCount.getOrDefault(0, 0),
                bTotal - backgoundCount.getOrDefault(1, 0),
                bTotal - backgoundCount.getOrDefault(2, 0) };
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i != j) {
                    min = Math.min(yOps[i] + bOps[j], min);
                }
            }
        }
        return min;
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

    /**
     * #3074
     * 
     * @param apple
     * @param capacity
     * @return
     */
    public int minimumBoxes(int[] apple, int[] capacity) {
        int sum = Arrays.stream(apple).sum();
        Arrays.sort(capacity);
        int res = 0;
        for (int i = capacity.length - 1; sum > 0; i--) {
            sum -= capacity[i];
            res++;
        }
        return res;
    }

    /**
     * #3086
     * 
     * @param nums
     * @param k
     * @param maxChanges
     * @return
     */
    public long minimumMoves(int[] nums, int k, int maxChanges) {
        int continuous = 0;
        List<Long> posOf1 = new ArrayList<>();

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 1)
                posOf1.add((long) i);
            if (nums[i] == 1) {
                continuous = Math.max(1, continuous);
                if (i - 1 >= 0 && nums[i - 1] == 1) {
                    continuous = Math.max(2, continuous);
                    if (i - 2 >= 0 && nums[i - 2] == 1)
                        continuous = Math.max(3, continuous);
                }

            }
        }
        if (continuous >= k) {
            return k - 1;
        }
        if (continuous + maxChanges >= k) {
            int changes = k - continuous;
            return Math.max(continuous - 1, 0) + changes * 2;
        }
        int len = k - maxChanges;
        long[] prefixPosOf1 = new long[posOf1.size() + 1];
        for (int i = 1; i < prefixPosOf1.length; i++) {
            prefixPosOf1[i] = posOf1.get(i - 1) + prefixPosOf1[i - 1];
        }
        long minDist = Long.MAX_VALUE;
        for (int e = len - 1; e < posOf1.size(); e++) {
            int s = e + 1 - len;
            int mid = s + (e - s) / 2;
            long midPos = posOf1.get(mid);
            long dist = 0;
            dist += (mid + 1 - s) * midPos
                    - (prefixPosOf1[mid + 1] - prefixPosOf1[s]);
            dist += (prefixPosOf1[e + 1] - prefixPosOf1[mid])
                    - (e + 1 - mid) * midPos;

            minDist = Math.min(minDist, dist);
        }
        return minDist + maxChanges * 2;
    }
}
