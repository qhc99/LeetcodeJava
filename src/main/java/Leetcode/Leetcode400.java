package Leetcode;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@SuppressWarnings("JavaDoc")
class Leetcode350 {

    /**
     * #300
     *
     * @param nums
     * @return
     */
    public static int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = Integer.MIN_VALUE;
        for (int i = 1; i < dp.length; i++) {
            var n = nums[i - 1];
            fill(dp, n);
        }

        for (int i = dp.length - 1; i >= 0; i--) {
            if (dp[i] != Integer.MAX_VALUE) {
                return i;
            }
        }
        throw new RuntimeException();
    }

    private static void fill(int[] dp, int val) {
        int s = 0, e = dp.length;
        while (e - s > 1) {
            int mid = (s + e) / 2;
            if (dp[mid] >= val) {
                e = mid;
            } else if (dp[mid] < val) {
                s = mid;
            }
        }
        dp[e] = val;
    }

    /**
     * #304
     */
    public static class NumMatrix {

        final int[][] matrixPrefixSum;

        public NumMatrix(int[][] matrix) {
            int m = matrix.length;
            int n = matrix[0].length;
            matrixPrefixSum = new int[m + 1][n + 1];
            System.arraycopy(matrix[0], 0, matrixPrefixSum[1], 1, n);
            for (int i = 2; i < m + 1; i++) {
                var row = matrix[i - 1];
                for (int j = 1; j < n + 1; j++) {
                    matrixPrefixSum[i][j] = row[j - 1]
                            + matrixPrefixSum[i - 1][j];
                }
            }

            for (int i = 1; i < m + 1; i++) {
                for (int j = 1; j < n + 1; j++) {
                    matrixPrefixSum[i][j] += matrixPrefixSum[i][j - 1];
                }
            }
        }

        public int sumRegion(int row1, int col1, int row2, int col2) {
            row1++;
            col1++;
            row2++;
            col2++;
            return matrixPrefixSum[row2][col2] - matrixPrefixSum[row2][col1 - 1]
                    - (matrixPrefixSum[row1 - 1][col2]
                            - matrixPrefixSum[row1 - 1][col1 - 1]);
        }
    }

    /**
     * #309
     *
     * @param prices
     * @return
     */
    public static int maxProfit(int[] prices) {
        int[][] dp = new int[prices.length][3]; // 0, have, not freeze. 1, not
                                                // have, freeze. 2 not have, not
                                                // freeze.
        dp[0][0] = -prices[0];
        dp[0][1] = 0;
        dp[0][2] = 0;

        for (int i = 1; i <= prices.length - 1; i++) {
            var p = prices[i];
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][2] - p);
            dp[i][1] = dp[i - 1][0] + p;
            dp[i][2] = Math.max(dp[i - 1][1], dp[i - 1][2]);
        }
        int n = prices.length;
        return Math.max(dp[n - 1][1], dp[n - 1][2]);

    }

    /**
     * #310
     *
     * @param n
     * @param edges
     * @return
     */
    public static List<Integer> findMinHeightTrees(int n, int[][] edges) {
        if (edges.length == 0) {
            return List.of(0);
        }
        BitSet visited = new BitSet(n);
        Map<Integer, List<Integer>> neighborsOf = new HashMap<>(n);
        for (var e : edges) {
            int n1 = e[0], n2 = e[1];
            var nbs = neighborsOf.computeIfAbsent(n1, a -> new ArrayList<>());
            nbs.add(n2);
            nbs = neighborsOf.computeIfAbsent(n2, a -> new ArrayList<>());
            nbs.add(n1);
        }
        class SharedList {
            final int node;
            final SharedList prev;

            SharedList(int n, SharedList p) {
                node = n;
                prev = p;
            }
        }

        int[] maxLen = new int[1];
        var deepest_list = new Object() {
            SharedList obj;
        };
        var func = new Object() {
            void apply(int node, int len, SharedList prev, BitSet visited) {
                if (visited.get(node)) {
                    return;
                }
                visited.set(node, true);
                len++;
                var list = new SharedList(node, prev);
                if (maxLen[0] < len) {
                    maxLen[0] = len;
                    deepest_list.obj = list;
                }
                var nbs = neighborsOf.get(node);
                for (var nb : nbs) {
                    apply(nb, len, list, visited);
                }
            }
        };

        for (var kv : neighborsOf.entrySet()) {
            var k = kv.getKey();
            var v = kv.getValue();
            if (v.size() == 1) {
                func.apply(k, 0, null, visited);
                break;
            }
        }
        visited = new BitSet(n);
        maxLen[0] = 0;
        var t = deepest_list.obj.node;
        func.apply(t, 0, null, visited);

        List<Integer> a = new ArrayList<>(maxLen[0]);
        SharedList ptr = deepest_list.obj;
        while (ptr != null) {
            a.add(ptr.node);
            ptr = ptr.prev;
        }
        if (a.size() % 2 == 1) {
            return List.of(a.get(a.size() / 2));
        } else {
            return List.of(a.get(a.size() / 2), a.get(a.size() / 2 - 1));
        }
    }

    /**
     * #312
     *
     * @param nums
     * @return
     */
    public static int maxCoins(int[] nums) {
        int[] paddedNum = new int[nums.length + 2];
        paddedNum[0] = 1;
        paddedNum[paddedNum.length - 1] = 1;
        System.arraycopy(nums, 0, paddedNum, 1, nums.length);
        int[][] cache = new int[paddedNum.length][paddedNum.length];
        for (var c : cache) {
            Arrays.fill(c, -1);
        }
        var recurFunc = new Object() {
            void apply(int i, int j) {
                if (i >= j - 1) {
                    cache[i][j] = 0;
                } else if (cache[i][j] == -1) {
                    int mid = i + 1;
                    int max = Integer.MIN_VALUE;
                    int temp = paddedNum[i] * paddedNum[j];
                    while (mid <= j - 1) {
                        apply(i, mid);
                        apply(mid, j);
                        max = Math.max(max, temp * paddedNum[mid]
                                + cache[i][mid] + cache[mid][j]);
                        mid++;
                    }

                    cache[i][j] = max;
                }
            }
        };
        recurFunc.apply(0, paddedNum.length - 1);
        return cache[0][paddedNum.length - 1];
    }

    /**
     * #313
     *
     * @param n
     * @param primes
     * @return
     */
    public static int nthSuperUglyNumber(int n, int[] primes) {
        int[] dp = new int[n];
        dp[0] = 1;
        int[] pointers = new int[primes.length];
        for (int i = 1; i < n; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 0; j < pointers.length; j++) {
                min = Math.min(min, primes[j] * dp[pointers[j]]);
            }
            dp[i] = min;
            for (int j = 0; j < pointers.length; j++) {
                if (primes[j] * dp[pointers[j]] == min) {
                    pointers[j]++;
                }
            }
        }

        return dp[n - 1];
    }

    /**
     * #315
     *
     * @param array
     * @return
     */
    public static List<Integer> countSmaller(int[] array) {
        var funcMergeSort = new Object() {
            final int[] ans = new int[array.length];

            public void apply(int[] dataArr, int[] idxArr, int start, int end) {
                if ((end - start) > 1) {
                    int middle = (start + end) / 2;
                    apply(dataArr, idxArr, start, middle);
                    apply(dataArr, idxArr, middle, end);
                    int left_len = middle - start;
                    int right_len = end - middle;
                    var left_cache = new int[left_len];
                    var right_cache = new int[right_len];
                    var left_idx_cache = new int[left_len];
                    var right_idx_cache = new int[right_len];
                    merge(dataArr, idxArr, start, left_cache, right_cache,
                            left_idx_cache, right_idx_cache);
                }
            }

            private void merge(int[] dataArr, int[] idxArr, int start,
                    int[] dataCacheL, int[] dataCacheR, int[] idxCacheL,
                    int[] idxCacheR) {
                int right_idx = 0;
                int left_idx = 0;
                System.arraycopy(dataArr, start, dataCacheL, 0,
                        dataCacheL.length);
                System.arraycopy(dataArr, start + dataCacheL.length, dataCacheR,
                        0, dataCacheR.length);
                System.arraycopy(idxArr, start, idxCacheL, 0, idxCacheL.length);
                System.arraycopy(idxArr, start + idxCacheL.length, idxCacheR, 0,
                        idxCacheR.length);

                for (int i = start; (i < start + dataCacheL.length
                        + dataCacheR.length) && (right_idx < dataCacheR.length)
                        && (left_idx < dataCacheL.length); i++) {
                    if (dataCacheL[left_idx] <= dataCacheR[right_idx]) {
                        dataArr[i] = dataCacheL[left_idx];
                        idxArr[i] = idxCacheL[left_idx];
                        ans[idxCacheL[left_idx]] += right_idx;
                        left_idx++;
                    } else {
                        dataArr[i] = dataCacheR[right_idx];
                        idxArr[i] = idxCacheR[right_idx];
                        right_idx++;
                    }
                }
                if (left_idx < dataCacheL.length) {
                    System.arraycopy(dataCacheL, left_idx, dataArr,
                            start + left_idx + right_idx,
                            dataCacheL.length - left_idx);
                    System.arraycopy(idxCacheL, left_idx, idxArr,
                            start + left_idx + right_idx,
                            idxCacheL.length - left_idx);
                    for (int i = left_idx; i < idxCacheL.length; i++) {
                        ans[idxCacheL[i]] += right_idx;
                    }
                } else if (right_idx < dataCacheR.length) {
                    System.arraycopy(dataCacheR, right_idx, dataArr,
                            start + left_idx + right_idx,
                            dataCacheR.length - right_idx);
                    System.arraycopy(idxCacheR, right_idx, idxArr,
                            start + left_idx + right_idx,
                            idxCacheR.length - right_idx);
                }
            }
        };
        funcMergeSort.apply(array, IntStream.range(0, array.length).toArray(),
                0, array.length);
        List<Integer> ans = new ArrayList<>();
        for (var i : funcMergeSort.ans) {
            ans.add(i);
        }
        return ans;
    }

    /**
     * #316
     *
     * @param s
     * @return
     */
    public static String removeDuplicateLetters(String s) {
        class CharStack {
            final char[] stack = new char[s.length()];
            final boolean[] set = new boolean['z' - 'a' + 1];
            private int len = 0;

            void add(char c) {
                stack[len] = c;
                len++;
                set[last() - 'a'] = true;
            }

            void pop() {
                set[last() - 'a'] = false;
                len--;
            }

            char last() {
                return stack[len - 1];
            }

            boolean notHas(char c) {
                return !set[c - 'a'];
            }

            @Override
            public String toString() {
                var sb = new StringBuilder();
                for (int i = 0; i < len; i++) {
                    sb.append(stack[i]);
                }
                return sb.toString();
            }
        }
        var stack = new CharStack();
        int[] remain = new int['z' - 'a' + 1];
        for (int i = 0; i < s.length(); i++) {
            var c = s.charAt(i);
            remain[c - 'a']++;
        }
        var s0 = s.charAt(0);
        stack.add(s0);
        remain[s0 - 'a']--;
        for (int i = 1; i < s.length(); i++) {
            var c = s.charAt(i);
            if (stack.last() >= c) {
                if (stack.notHas(c)) {
                    while (stack.len > 0 && stack.last() >= c
                            && remain[stack.last() - 'a'] > 0) {
                        stack.pop();
                    }
                    stack.add(c);
                }
            } else if (stack.notHas(c)) {
                stack.add(c);
            }
            remain[c - 'a']--;
        }
        return stack.toString();
    }

    /**
     * #318
     *
     * @param words
     * @return
     */
    public static int maxProduct(String[] words) {
        BitSet[] characters = new BitSet[words.length];
        for (int i = 0; i < words.length; i++) {
            var w = words[i];
            var b = new BitSet('z' - 'a' + 1);
            characters[i] = b;
            for (int j = 0; j < w.length(); j++) {
                var c = w.charAt(j);
                b.set(c - 'a', true);
            }
        }
        int max = 0;
        for (int i = 0; i < words.length; i++) {
            var i_chars = characters[i];
            var i_len = words[i].length();
            for (int j = i + 1; j < words.length; j++) {
                var j_chars = characters[j];
                if (!i_chars.intersects(j_chars)) {
                    max = Math.max(max, i_len * words[j].length());
                }
            }
        }
        return max;
    }

    /**
     * #321
     *
     * @param nums1
     * @param nums2
     * @param k
     * @return
     */
    public static int[] maxNumber(int[] nums1, int[] nums2, int k) {
        if (nums1.length > nums2.length) {
            var t = nums1;
            nums1 = nums2;
            nums2 = t;
        }

        var order1 = getOrder(nums1);
        var order2 = getOrder(nums2);

        int[] max = null;
        for (int i = 0; i <= nums1.length && i <= k; i++) {
            int j = k - i;
            if (j <= nums2.length) {
                var sub1 = subArray(nums1, order1, i);
                var sub2 = subArray(nums2, order2, j);
                int[] m = new int[k];
                merge(m, 0, sub1, sub2);
                if (max == null) {
                    max = m;
                } else if (larger(m, max)) {
                    max = m;
                }
            }
        }

        return max;
    }

    private static boolean larger(int[] a, int[] b) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] > b[i]) {
                return true;
            } else if (a[i] == b[i]) {
                continue;
            } else {
                return false;
            }
        }
        return false;
    }

    private static int[] subArray(int[] num, int[] order, int k) {
        if (k == num.length) {
            return num;
        } else {
            int[] ans = new int[k];
            for (int i = 0, idx = 0; i < num.length && idx < k; i++) {
                if (order[i] > num.length - k) {
                    ans[idx++] = num[i];
                }
            }
            return ans;
        }
    }

    private static int[] getOrder(int[] array) {
        class NumIdx {
            final int num;
            final int idx;

            NumIdx(int n, int i) {
                num = n;
                idx = i;
            }
        }
        int[] order = new int[array.length];
        int rm = 0;
        Deque<NumIdx> stack = new ArrayDeque<>();
        for (int i = 0; i < array.length; i++) {
            var n = array[i];
            if (stack.size() == 0 || stack.getLast().num >= n) {
                stack.addLast(new NumIdx(n, i));
            } else {
                while (stack.size() > 0 && stack.getLast().num < n) {
                    var t = stack.pollLast();
                    order[t.idx] = ++rm;
                }
                stack.addLast(new NumIdx(n, i));
            }
        }
        while (stack.size() > 0) {
            var t = stack.pollLast();
            order[t.idx] = ++rm;
        }
        return order;
    }

    private static void merge(int[] array, int start, int[] cache1,
            int[] cache2) {
        int right_idx = 0;
        int left_idx = 0;
        for (int i = start; (i < start + cache1.length + cache2.length)
                && (right_idx < cache2.length)
                && (left_idx < cache1.length); i++) {
            if (cache1[left_idx] > cache2[right_idx]) {
                array[i] = cache1[left_idx++];
            } else if (cache1[left_idx] < cache2[right_idx]) {
                array[i] = cache2[right_idx++];
            } else {
                int r = right_idx + 1;
                int l = left_idx + 1;
                while (l < cache1.length && r < cache2.length) {
                    if (cache1[l] > cache2[r]) {
                        array[i] = cache1[left_idx++];
                        break;
                    } else if (cache1[l] < cache2[r]) {
                        array[i] = cache2[right_idx++];
                        break;
                    } else {
                        r++;
                        l++;
                    }
                }
                if (l >= cache1.length) {
                    array[i] = cache2[right_idx++];
                } else if (r >= cache2.length) {
                    array[i] = cache1[left_idx++];
                }
            }
        }
        if (left_idx < cache1.length) {
            System.arraycopy(cache1, left_idx, array,
                    start + left_idx + right_idx, cache1.length - left_idx);
        } else if (right_idx < cache2.length) {
            System.arraycopy(cache2, right_idx, array,
                    start + left_idx + right_idx, cache2.length - right_idx);
        }
    }

    private static int randPartition(int[] a, int start, int end) { // base case
                                                                    // (end
                                                                    // -start)
        int pivot_idx = ThreadLocalRandom.current().nextInt(start, end);
        var pivot = a[pivot_idx];

        var temp = a[end - 1];
        a[end - 1] = pivot;
        a[pivot_idx] = temp;

        int i = start - 1;
        for (int j = start; j < end - 1; j++) {
            if (a[j] <= pivot) {
                var t = a[j];
                a[j] = a[++i];
                a[i] = t;
            }
        }
        a[end - 1] = a[++i];
        a[i] = pivot;
        return i; // pivot idx
    }

    /**
     * #323
     * 
     * @param n
     * @param edges
     * @return
     */
    public int countComponents(int n, int[][] edges) {
        DisjointSet set = new DisjointSet(n);
        for (var e : edges) {
            set.union(e[0], e[1]);
        }
        for (int i = 0; i < n; i++) {
            set.parent(i);
        }
        return (int) Arrays.stream(set.parent).distinct().count();
    }

    /**
     * #324
     *
     * @param nums
     */
    public static void wiggleSort(int[] nums) {
        int mid = (nums.length - 1) / 2;
        int[] a = new int[nums.length];
        System.arraycopy(nums, 0, a, 0, nums.length);
        int mid_val = rankSearch(a, mid);
        partition3way(a, mid_val);
        int i = mid, j = nums.length - 1;
        int idx = 0;
        while (i >= 0 && j > mid) {
            nums[idx++] = a[i--];
            nums[idx++] = a[j--];
        }
        if (nums.length % 2 == 1) {
            nums[idx] = a[0];
        }
    }

    private static void exchange(int[] array, int i, int j) {
        var t = array[i];
        array[i] = array[j];
        array[j] = t;
    }

    private static void partition3way(int[] array, int val) {
        int lt = 0, gt = array.length - 1, i = 0;
        while (i <= gt) {
            if (array[i] < val) {
                exchange(array, i++, lt++);
            } else if (array[i] > val) {
                exchange(array, i, gt--);
            } else {
                i++;
            }
        }
    }

    // select ith smallest element in array
    private static int rankSearch(int[] a, int start, int end, int ith) {
        if ((start - end) == 1) {
            return a[start];
        }
        int pivot_idx = randPartition(a, start, end);
        int left_total = pivot_idx - start;
        if (ith == left_total) {
            return a[pivot_idx];
        } else if (ith < left_total + 1) {
            return rankSearch(a, start, pivot_idx, ith);
        } else {
            return rankSearch(a, pivot_idx + 1, end, ith - left_total - 1);
        }
    }

    /**
     * @param a   will change array
     * @param ith start from 0
     */
    public static int rankSearch(int[] a, int ith) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        return rankSearch(a, 0, a.length, ith);
    }

    /**
     * #327
     *
     * @param num
     * @param lower
     * @param upper
     * @return
     */
    public static int countRangeSum(int[] num, int lower, int upper) {
        var res = new Object() {
            int n = 0;
        };
        var func = new Object() {
            void solveCountRangeSum(long[] array, int start, int end) {
                if ((end - start) > 1) {
                    int middle = (start + end) / 2;
                    solveCountRangeSum(array, start, middle);
                    solveCountRangeSum(array, middle, end);
                    int left_len = middle - start;
                    int right_len = end - middle;
                    var left_cache = new long[left_len];
                    var right_cache = new long[right_len];
                    merge(array, start, left_cache, right_cache);
                }
            }

            void merge(long[] array, int start, long[] cache1, long[] cache2) {
                int right_idx = 0;
                int left_idx = 0;
                System.arraycopy(array, start, cache1, 0, cache1.length);
                System.arraycopy(array, start + cache1.length, cache2, 0,
                        cache2.length);

                int l = 0, r = 0;
                for (var c1 : cache1) {
                    while (l < cache2.length && cache2[l] - c1 < lower) {
                        l++;
                    }
                    while (r < cache2.length && cache2[r] - c1 <= upper) {
                        r++;
                    }
                    res.n += r - l;
                }

                for (int i = start; (i < start + cache1.length + cache2.length)
                        && (right_idx < cache2.length)
                        && (left_idx < cache1.length); i++) {
                    if (cache1[left_idx] <= cache2[right_idx]) {
                        array[i] = cache1[left_idx++];
                    } else {
                        array[i] = cache2[right_idx++];
                    }
                }
                if (left_idx < cache1.length) {
                    System.arraycopy(cache1, left_idx, array,
                            start + left_idx + right_idx,
                            cache1.length - left_idx);
                } else if (right_idx < cache2.length) {
                    System.arraycopy(cache2, right_idx, array,
                            start + left_idx + right_idx,
                            cache2.length - right_idx);
                }
            }
        };

        var sum = new long[num.length + 1];
        for (int i = 1; i < sum.length; i++) {
            sum[i] = num[i - 1];
        }
        for (int i = 2; i < sum.length; i++) {
            sum[i] += sum[i - 1];
        }

        func.solveCountRangeSum(sum, 0, sum.length);
        return res.n;
    }

    /**
     * #328
     *
     * @param head
     * @return
     */
    public static ListNode oddEvenList(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode oddTail = head, evenHead, evenTail;
        if (head.next == null) {
            return head;
        }
        ListNode ptr = head.next;
        evenHead = ptr;
        evenTail = ptr;
        ptr = ptr.next;
        for (int i = 3; ptr != null; i++, ptr = ptr.next) {
            if (i % 2 == 1) {
                oddTail.next = ptr;
                oddTail = ptr;
            } else {
                evenTail.next = ptr;
                evenTail = ptr;
            }
        }
        oddTail.next = evenHead;
        evenTail.next = null;
        return head;
    }

    Map<TreeNode, Integer> select_max = new HashMap<>();
    Map<TreeNode, Integer> non_select_max = new HashMap<>();

    /**
     * #329
     *
     * @param matrix
     * @return
     */
    public static int longestIncreasingPath(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int[][] depthCache = new int[m][n];
        var solver = new Object() {
            int resGlobal = 0;

            boolean isStart(int i, int j) {
                int min = Integer.MAX_VALUE;
                min = i - 1 >= 0 ? Math.min(min, matrix[i - 1][j]) : min;
                min = i + 1 < m ? Math.min(min, matrix[i + 1][j]) : min;
                min = j - 1 >= 0 ? Math.min(min, matrix[i][j - 1]) : min;
                min = j + 1 < n ? Math.min(min, matrix[i][j + 1]) : min;
                return min >= matrix[i][j];
            }

            void DFS(int i, int j) {
                int res = Integer.MIN_VALUE;
                int v = matrix[i][j];
                if (i - 1 >= 0 && matrix[i - 1][j] > v) {
                    if (depthCache[i - 1][j] == 0) {
                        DFS(i - 1, j);
                    }
                    res = Math.max(res, depthCache[i - 1][j] + 1);
                }
                if (i + 1 < m && matrix[i + 1][j] > v) {
                    if (depthCache[i + 1][j] == 0) {
                        DFS(i + 1, j);
                    }
                    res = Math.max(res, depthCache[i + 1][j] + 1);
                }
                if (j - 1 >= 0 && matrix[i][j - 1] > v) {
                    if (depthCache[i][j - 1] == 0) {
                        DFS(i, j - 1);
                    }
                    res = Math.max(res, depthCache[i][j - 1] + 1);
                }
                if (j + 1 < n && matrix[i][j + 1] > v) {
                    if (depthCache[i][j + 1] == 0) {
                        DFS(i, j + 1);
                    }
                    res = Math.max(res, depthCache[i][j + 1] + 1);
                }

                depthCache[i][j] = Math.max(res, 1);
                resGlobal = Math.max(resGlobal, depthCache[i][j]);
            }

            void solve() {
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        if (depthCache[i][j] == 0 && isStart(i, j)) {
                            DFS(i, j);
                        }
                    }
                }
            }
        };
        solver.solve();
        return solver.resGlobal;
    }

    /**
     * #331
     *
     * @param preorder
     * @return
     */
    public static boolean isValidSerialization(String preorder) {
        String[] nodes = preorder.split(",");
        BitSet visited = new BitSet(nodes.length);
        var func = new Object() {
            int visit(int idx) {
                if (idx >= nodes.length)
                    return -1;
                if (visited.get(idx))
                    return -1;
                visited.set(idx, true);
                var c = nodes[idx];
                if (c.equals("#"))
                    return idx;
                else {
                    var left = visit(idx + 1);
                    if (left == -1)
                        return -1;
                    return visit(left + 1);
                }
            }
        };
        return (func.visit(0) == nodes.length - 1)
                && (visited.stream().count() == nodes.length);
    }

    /**
     * #332
     *
     * @param tickets
     * @return
     */
    public static List<String> findItinerary(List<List<String>> tickets) {
        Map<String, PriorityQueue<String>> neighbors = new HashMap<>();
        for (var pair : tickets) {
            var from = pair.get(0);
            var to = pair.get(1);
            if (!neighbors.containsKey(from)) {
                neighbors.put(from, new PriorityQueue<>());
            }
            neighbors.get(from).add(to);
        }
        List<String> reversed_res = new ArrayList<>();
        var func = new Object() {
            void visit(String node) {
                var ns = neighbors.get(node);
                while (ns != null && ns.size() > 0) {
                    var n = ns.poll();
                    visit(n);
                }
                reversed_res.add(node);
            }
        };
        func.visit("JFK");
        for (int i = 0, j = reversed_res.size() - 1; i < j; i++, j--) {
            var t = reversed_res.get(i);
            reversed_res.set(i, reversed_res.get(j));
            reversed_res.set(j, t);
        }
        return reversed_res;
    }

    /**
     * #336
     * 
     * @param words
     * @return
     */
    public List<List<Integer>> palindromePairs(String[] words) {
        var reverseTrie = new Trie();
        List<List<Integer>> ret = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            var word = words[i];
            reverseTrie.add(new StringBuilder(word).reverse().toString(), i);
        }
        for (int i = 0; i < words.length; i++) {
            var word = words[i];
            var wordMatches = reverseTrie.getMatches(word);
            for (var idx : wordMatches) {
                if (idx != i) {
                    var matchWord = words[idx];
                    if ((matchWord.length() == word.length())
                            || (matchWord.length() < word.length()
                                    && haspalindromePairSuffix(
                                            matchWord.length(), word))
                            || (matchWord.length() > word.length()
                                    && haspalindromePairSuffix(word.length(),
                                            new StringBuilder(matchWord)
                                                    .reverse().toString()))) {

                        List<Integer> pair = new ArrayList<>();
                        pair.add(i);
                        pair.add(idx);
                        ret.add(pair);
                    }
                }
            }
        }
        return ret;
    }

    private boolean haspalindromePairSuffix(int start, String s) {
        int end = s.length() - 1;
        for (; start < end; start++, end--) {
            if (s.charAt(start) != s.charAt(end)) {
                return false;
            }
        }
        return true;
    }

    /**
     * #337
     *
     * @param root
     * @return
     */
    public int rob(TreeNode root) {
        if (root == null) {
            return 0;
        }
        robSolve(root);
        return Math.max(select_max.get(root), non_select_max.get(root));
    }

    private void robSolve(TreeNode n) {
        if (n == null) {
            return;
        }

        robSolve(n.left);
        robSolve(n.right);

        var l_s = select_max.getOrDefault(n.left, 0);
        var l_n = non_select_max.getOrDefault(n.left, 0);
        var r_s = select_max.getOrDefault(n.right, 0);
        var r_n = non_select_max.getOrDefault(n.right, 0);
        select_max.put(n, n.val + l_n + r_n);
        non_select_max.put(n, Math.max(l_n, l_s) + Math.max(r_n, r_s));
    }

    // This is the interface that allows for creating nested lists.
    // You should not implement it, or speculate about its implementation
    public interface NestedInteger {

        // @return true if this NestedInteger holds a single integer, rather
        // than a nested list.
        boolean isInteger();

        // @return the single integer that this NestedInteger holds, if it holds
        // a single integer
        // Return null if this NestedInteger holds a nested list
        Integer getInteger();

        // @return the nested list that this NestedInteger holds, if it holds a
        // nested list
        // Return empty list if this NestedInteger holds a single integer
        List<NestedInteger> getList();
    }

    /**
     * #341
     */
    public static class NestedIterator implements Iterator<Integer> {

        final Deque<Integer> indicesStack = new ArrayDeque<>();
        final Deque<List<NestedInteger>> listStack = new ArrayDeque<>();
        int store;
        boolean prepared = false;

        public NestedIterator(List<NestedInteger> nestedList) {
            listStack.add(nestedList);
            indicesStack.add(0);
            clearUsedList();
        }

        @Override
        public Integer next() {
            if (prepared) {
                prepared = false;
                return store;
            } else
                throw new RuntimeException();
        }

        private Integer extractNext() {
            if (listStack.peekLast().size() == 0) {
                indicesStack.pollLast();
                listStack.pollLast();
                return null;
            } else {
                var obj = listStack.peekLast().get(indicesStack.peekLast());
                indicesStack.addLast(indicesStack.pollLast() + 1);
                clearUsedList();
                if (obj.isInteger()) {
                    return obj.getInteger();
                } else {
                    var l = obj.getList();
                    indicesStack.addLast(0);
                    listStack.addLast(l);
                    return extractNext();
                }
            }
        }

        private void clearUsedList() {
            var idx = indicesStack.peekLast();
            var l = listStack.peekLast();
            while (listStack.size() > 0 && idx >= l.size()) {
                indicesStack.pollLast();
                idx = indicesStack.peekLast();
                listStack.pollLast();
                l = listStack.peekLast();
            }
        }

        @Override
        public boolean hasNext() {
            if (prepared) {
                return true;
            } else if (listStack.size() > 0) {
                Integer p = null;
                do {
                    p = extractNext();
                } while (listStack.size() > 0 && p == null);
                if (p != null) {
                    store = p;
                    prepared = true;
                }
            }
            return prepared;
        }
    }

    /**
     * #347
     * 
     * @param nums
     * @param k
     * @return
     */
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (var n : nums) {
            freq.put(n, freq.getOrDefault(n, 0) + 1);
        }
        Map<Integer, List<Integer>> freq2num = new HashMap<>(freq.size());
        for (var e : freq.entrySet()) {
            freq2num.computeIfAbsent(e.getValue(), key -> new ArrayList<>())
                    .add(e.getKey());
        }
        Queue<Integer> freqQueue = new PriorityQueue<>((a, b) -> b - a);
        freqQueue.addAll(freq2num.keySet());
        int[] res = new int[k];
        for (int i = 0; i < k;) {
            var f = freqQueue.poll();
            var ns = freq2num.get(f);
            int ii = 0;
            while (i < k && ii < ns.size()) {
                res[i++] = ns.get(ii++);
            }
        }
        return res;
    }
}

@SuppressWarnings({ "JavaDoc", "Unused" })
public class Leetcode400 {

    /**
     * #354
     *
     * @param envelopes
     * @return
     */
    public static int maxEnvelopes(int[][] envelopes) {
        Arrays.sort(envelopes, (a, b) -> {
            var t = a[0] - b[0];
            if (t != 0) {
                return t;
            } else {
                return b[1] - a[1];
            }
        });
        int[] h = new int[envelopes.length];
        for (int i = 0; i < h.length; i++) {
            h[i] = envelopes[i][1];
        }
        return Leetcode350.lengthOfLIS(h);
    }

    /**
     * #355
     */
    class Twitter {
        static record Tweet(int id, int time) {
        }

        Map<Integer, Set<Integer>> follow = new HashMap<>();
        Map<Integer, Deque<Tweet>> tweets = new HashMap<>();
        int time = 0;

        public Twitter() {

        }

        public void postTweet(int userId, int tweetId) {
            var queue = tweets.computeIfAbsent(userId, k -> new ArrayDeque<>());
            if (queue.size() >= 10) {
                queue.pollFirst();
            }
            queue.addLast(new Tweet(tweetId, time++));
        }

        public List<Integer> getNewsFeed(int userId) {
            Queue<Deque<Tweet>> feed = new PriorityQueue<>((q1, q2) -> {
                if (q1.isEmpty() && !q2.isEmpty()) {
                    return 1;
                } else if (!q1.isEmpty() && q2.isEmpty()) {
                    return -1;
                } else if (q1.isEmpty() && q2.isEmpty()) {
                    return 0;
                }
                return q2.peekLast().time - q1.peekLast().time;
            });
            feed.addAll(follow.getOrDefault(userId, Set.of(userId)).stream()
                    .map(u -> new ArrayDeque<>(
                            tweets.getOrDefault(u, new ArrayDeque<>())))
                    .toList());
            List<Integer> res = new ArrayList<>(10);
            for (int i = 0; i < 10 && !feed.isEmpty(); i++) {
                var queue = feed.poll();
                if (queue.isEmpty())
                    break;
                res.add(queue.pollLast().id);
                if (!queue.isEmpty())
                    feed.add(queue);
            }
            return res;
        }

        public void follow(int followerId, int followeeId) {
            follow.computeIfAbsent(followerId, k -> {
                Set<Integer> s = new HashSet<>();
                s.add(followerId);
                return s;
            }).add(followeeId);
        }

        public void unfollow(int followerId, int followeeId) {
            follow.computeIfAbsent(followerId, k -> {
                Set<Integer> s = new HashSet<>();
                s.add(followerId);
                return s;
            }).remove(followeeId);
        }
    }

    /**
     * #357
     *
     * @param n
     * @return
     */
    public static int countNumbersWithUniqueDigits(int n) {
        int[] ans = new int[] { 0, 10, 91, 739, 5275, 32491, 168571, 712891,
                2345851 };
        return ans[n];
    }

    /**
     * #363
     *
     * @param matrix
     * @param k
     * @return
     */
    public static int maxSumSubmatrix(int[][] matrix, int k) {
        int ans = Integer.MIN_VALUE;
        var mat = new RegularizedMatrix(matrix);
        var colAcc = new int[mat.lenRow + 1][mat.lenCol];
        for (int i = 0; i < mat.lenRow; i++) {
            for (int j = 0; j < mat.lenCol; j++) {
                colAcc[i + 1][j] = mat.get(i, j) + colAcc[i][j];
            }
        }

        for (int rs = 0; rs < colAcc.length; rs++) {
            for (int re = rs + 1; re < colAcc.length; re++) {
                int[] colSum = new int[mat.lenCol];
                for (int n = 0; n < mat.lenCol; n++) {
                    colSum[n] = colAcc[re][n] - colAcc[rs][n];
                }
                // b - a <= k, a >= b - k
                int acc = 0;
                TreeSet<Integer> set = new TreeSet<>();
                set.add(0);
                for (var v : colSum) {
                    acc += v;
                    var l = set.ceiling(acc - k);
                    if (l != null) {
                        ans = Math.max(ans, acc - l);
                        if (ans == k) {
                            return ans;
                        }
                    }
                    set.add(acc);
                }
            }
        }
        return ans;
    }

    static class RegularizedMatrix {
        private final int[][] m;
        private final boolean inverse;

        public final int lenRow;
        public final int lenCol;

        RegularizedMatrix(int[][] m) {
            this.m = m;
            inverse = m.length > m[0].length;
            if (inverse) {
                lenCol = m.length;
                lenRow = m[0].length;
            } else {
                lenRow = m.length;
                lenCol = m[0].length;
            }
        }

        int get(int i, int j) {
            if (inverse)
                return m[j][i];
            else
                return m[i][j];
        }

        void set(int i, int j, int v) {
            if (inverse)
                m[j][i] = v;
            else
                m[i][j] = v;
        }
    }

    /**
     * #368
     *
     * @param nums
     * @return
     */
    public static List<Integer> largestDivisibleSubset(int[] nums) {
        class DPData {
            final int pre_idx;
            final int size;

            DPData(int pre, int s) {
                pre_idx = pre;
                size = s;
            }

            @Override
            public String toString() {
                return "idx: " + pre_idx + " size: " + size;
            }
        }

        Arrays.sort(nums);

        DPData[] dp = new DPData[nums.length];
        for (int i = 0; i < dp.length; i++) {
            dp[i] = new DPData(i, 1);
        }

        for (int i = 1; i < nums.length; i++) {
            var i_val = nums[i];
            for (int j = 0; j < i; j++) {
                var j_val = nums[j];
                if (i_val % j_val == 0 && dp[j].size + 1 > dp[i].size) {
                    dp[i] = new DPData(j, dp[j].size + 1);
                }
            }
        }

        DPData max_dp_data = dp[0];
        int max_dp_data_idx = 0;
        for (int i = 0; i < dp.length; i++) {
            var d = dp[i];
            if (d.size > max_dp_data.size) {
                max_dp_data = d;
                max_dp_data_idx = i;
            }
        }
        Deque<Integer> ans = new ArrayDeque<>(max_dp_data.size);

        DPData ptr = max_dp_data;
        for (int i = 0, idx = max_dp_data_idx; i < max_dp_data.size; i++) {
            ans.addFirst(nums[idx]);
            idx = ptr.pre_idx;
            ptr = dp[idx];
        }

        return ans.stream().toList();
    }

    /**
     * #371
     * 
     * @param a
     * @param b
     * @return
     */
    public int getSum(int a, int b) {
        do {
            var c = (a & b) << 1;
            a ^= b;
            b = c;
        } while (b != 0);
        return a;
    }

    /**
     * #373
     *
     * @param nums1
     * @param nums2
     * @param k
     * @return
     */
    public static List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2,
            int k) {
        @SuppressWarnings("hiding")
        class Data {
            final int idx;
            final int first;
            final int second;
            final int sum;

            Data(int i, int f, int s, int sum) {
                idx = i;
                first = f;
                second = s;
                this.sum = sum;
            }
        }

        PriorityQueue<Data> queue = new PriorityQueue<>(
                Comparator.comparing(d -> d.sum));
        List<List<Integer>> ans = new ArrayList<>();
        for (int i = 0; i < nums1.length; i++) {
            queue.add(new Data(0, nums1[i], nums2[0], nums1[i] + nums2[0]));
        }
        while (queue.size() > 0 && k > 0) {
            k--;
            var d = queue.poll();
            ans.add(List.of(d.first, d.second));
            if (d.idx + 1 < nums2.length) {
                queue.add(new Data(d.idx + 1, d.first, nums2[d.idx + 1],
                        d.first + nums2[d.idx + 1]));
            }
        }
        return ans;
    }

    /**
     * #375
     *
     * @param n
     * @return
     */
    public static int getMoneyAmount(int n) {
        return getCost(1, n);
    }

    private record IntTuple(int start, int end) {

    }

    private static final HashMap<IntTuple, Integer> cache = new HashMap<>(32);

    private static int getCost(int s, int e) {
        if (s >= e)
            return 0;
        var idx = new IntTuple(s, e);
        var cache_val = cache.get(idx);
        if (cache_val != null)
            return cache_val;
        else {
            int min = Integer.MAX_VALUE;
            for (int i = s; i <= e; i++) {
                var l = getCost(s, i - 1);
                var r = getCost(i + 1, e);
                min = Math.min(min, i + Math.max(l, r));
            }
            cache.put(idx, min);
            return min;
        }
    }

    /**
     * #377
     *
     * @param nums
     * @param target
     * @return
     */
    public static int combinationSum4(int[] nums, int target) {
        int[] dp = new int[target + 1];
        for (var n : nums) {
            if (n < dp.length) {
                dp[n] = 1;
            }
        }
        for (int i = 1; i < dp.length; i++) {
            if (dp[i] > 0) {
                for (var n : nums) {
                    if (i + n < dp.length) {
                        dp[i + n] += dp[i];
                    }
                }
            }
        }
        return dp[target];
    }

    /**
     * #378
     *
     * @param matrix
     * @param k
     * @return
     */
    public static int kthSmallest(int[][] matrix, int k) {
        var n = matrix.length;
        int l = matrix[0][0];
        int r = matrix[n - 1][n - 1];
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (rankGEQ(matrix, mid, k)) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    private static boolean rankGEQ(int[][] matrix, int mid, int k) {
        int i = matrix.length - 1;
        int j = 0;
        int num = 0;
        while (i >= 0 && j < matrix.length) {
            if (matrix[i][j] <= mid) {
                num += i + 1;
                j++;
            } else {
                i--;
            }
        }
        return num >= k;
    }

    /**
     * #380
     */
    public static class RandomizedSet {
        final HashMap<Integer, Integer> idxToVal = new HashMap<>(32);
        final HashMap<Integer, Integer> valToIdx = new HashMap<>(32);
        final Random random = new Random();

        public RandomizedSet() {

        }

        public boolean insert(int val) {
            if (!valToIdx.containsKey(val)) {
                valToIdx.put(val, idxToVal.size());
                idxToVal.put(idxToVal.size(), val);
                return true;
            } else {
                return false;
            }
        }

        public boolean remove(int val) {
            if (valToIdx.containsKey(val)) {
                var val_idx = valToIdx.remove(val);
                var tail_val = idxToVal.remove(idxToVal.size() - 1);
                if (tail_val != val) {
                    valToIdx.put(tail_val, val_idx);
                    idxToVal.put(val_idx, tail_val);
                }
                return true;
            } else
                return false;
        }

        public int getRandom() {
            var idx = random.nextInt(0, idxToVal.size());
            return idxToVal.get(idx);
        }
    }

    /**
     * #381 <br/>
     * 设计一个支持在平均 时间复杂度 O(1) 下， 执行以下操作的数据结构。 <br/>
     * insert(val)：向集合中插入元素 val。 <br/>
     * remove(val)：当 val 存在时，从集合中移除一个 val。 <br/>
     * getRandom：从现有集合中随机获取一个元素。每个元素被返回的概率应该与其在集合中的数量呈线性相关。
     */
    public static class RandomizedCollection {
        private final Map<Integer, Set<Integer>> idx;
        private final List<Integer> nums;

        /**
         * Initialize your data structure here.
         */
        public RandomizedCollection() {
            idx = new HashMap<>();
            nums = new ArrayList<>();
        }

        /**
         * Inserts a value to the collection. Returns true if the collection did
         * not already contain the specified element.
         */
        public boolean insert(int val) {
            nums.add(val);
            Set<Integer> set = idx.getOrDefault(val, new HashSet<>());
            set.add(nums.size() - 1);
            idx.put(val, set);
            return set.size() == 1;
        }

        /**
         * Removes a value from the collection. Returns true if the collection
         * contained the specified element.
         */
        public boolean remove(int val) {
            if (!idx.containsKey(val)) {
                return false;
            }
            Iterator<Integer> it = idx.get(val).iterator();
            int i = it.next();
            int lastNum = nums.get(nums.size() - 1);
            nums.set(i, lastNum);
            idx.get(val).remove(i);
            idx.get(lastNum).remove(nums.size() - 1);
            if (i < nums.size() - 1) {
                idx.get(lastNum).add(i);
            }
            if (idx.get(val).size() == 0) {
                idx.remove(val);
            }
            nums.remove(nums.size() - 1);
            return true;
        }

        /**
         * Get a random element from the collection.
         */
        public int getRandom() {
            return nums.get((int) (Math.random() * nums.size()));
        }
    }

    /**
     * #383
     *
     * @param ransomNote
     * @param magazine
     * @return
     */
    public static boolean canConstruct(String ransomNote, String magazine) {
        int[] count_ransomNote = new int['z' - 'a' + 1];
        int[] count_magazine = new int['z' - 'a' + 1];
        var chars_ran = ransomNote.toCharArray();
        var chars_mag = magazine.toCharArray();

        for (int i = 0; i < chars_ran.length; i++) {
            var c = chars_ran[i];
            count_ransomNote[c - 'a']++;
        }
        for (int i = 0; i < chars_mag.length; i++) {
            var c = chars_mag[i];
            count_magazine[c - 'a']++;
        }
        for (int i = 0; i < count_ransomNote.length; i++) {
            if (count_magazine[i] < count_ransomNote[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * #387
     * 
     * @param s
     * @return
     */
    public static int firstUniqChar(String s) {
        int[] counter = new int['z' - 'a' + 1];
        char[] chrs = s.toCharArray();
        for (var c : chrs) {
            counter[c - 'a']++;
        }
        for (int i = 0; i < chrs.length; i++) {
            var c = chrs[i];
            if (counter[c - 'a'] == 1) {
                return i;
            }
        }
        return -1;
    }

    /**
     * #388
     *
     * @param input
     * @return
     */
    public static int lengthLongestPath(String input) {
        var arr = input.split("((?<=\t)|(?=\t)|(?<=\n)|(?=\n))", -1);
        int level = 0;
        List<Integer> dir_stack = new ArrayList<>();
        int max_len = 0;
        for (var s : arr) {
            if (s.equals("\n")) {
                level = 0;
            } else if (s.equals("\t")) {
                level++;
            } else if (s.contains(".")) {
                int dir_len = 0;
                for (int i = 0; i < level; i++) {
                    dir_len += dir_stack.get(i) + 1;
                }
                max_len = Math.max(max_len, dir_len + s.length());
            } else {
                if (level < dir_stack.size()) {
                    dir_stack.set(level, s.length());
                } else {
                    dir_stack.add(s.length());
                }
            }
        }
        return max_len;
    }

    public int lastRemaining(int n) {
        int step = 1;
        int s = 1;
        int len = n;
        boolean left = true;
        while (len > 1) {
            if (left || (!left && len % 2 == 1)) {
                s += step;
            }
            len /= 2;
            step *= 2;
            left = !left;
        }
        return s;
    }

    /**
     * #391
     *
     * @param rectangles
     * @return
     */
    public static boolean isRectangleCover(int[][] rectangles) {
        record Point(int X, int Y) {
        }
        int area_sum = 0;
        int whole_area = 0;
        Point left_bottom = null;
        Point right_bottom = null;
        Point left_top = null;
        Point right_top = null;
        {
            var first_rect = rectangles[0];
            int x = first_rect[0], y = first_rect[1], a = first_rect[2],
                    b = first_rect[3];
            left_bottom = new Point(x, y);
            right_top = new Point(a, b);
            left_top = new Point(x, b);
            right_bottom = new Point(a, y);
        }

        Map<Point, Integer> occurrence = new HashMap<>(4 * rectangles.length);
        for (var rect : rectangles) {
            int x = rect[0], y = rect[1], a = rect[2], b = rect[3];
            var lb = new Point(x, y);
            var rt = new Point(a, b);
            var lt = new Point(x, b);
            var rb = new Point(a, y);

            area_sum += (a - x) * (b - y);

            occurrence.put(lb, occurrence.getOrDefault(lb, 0) + 1);
            occurrence.put(rt, occurrence.getOrDefault(rt, 0) + 1);
            occurrence.put(lt, occurrence.getOrDefault(lt, 0) + 1);
            occurrence.put(rb, occurrence.getOrDefault(rb, 0) + 1);

            if (x <= left_bottom.X && y <= left_bottom.Y) {
                left_bottom = lb;
            }
            if (a >= right_top.X && b >= right_top.Y) {
                right_top = rt;
            }
            if (x <= left_top.X && b >= left_top.Y) {
                left_top = lt;
            }
            if (a >= right_bottom.X && y <= right_bottom.Y) {
                right_bottom = rb;
            }
        }

        if (left_bottom.X != left_top.X || (right_top.X != right_bottom.X)
                || left_bottom.Y != right_bottom.Y
                || left_top.Y != right_top.Y) {
            return false;
        }

        for (var p_o : occurrence.entrySet()) {
            var p = p_o.getKey();
            var o = p_o.getValue();
            if (!(o == 2 || o == 4)) {
                if (o == 1) {
                    if (!(p.equals(left_bottom) || p.equals(left_top)
                            || p.equals(right_bottom) || p.equals(right_top))) {
                        return false;
                    }
                } else
                    return false;
            }
        }

        whole_area = (right_top.X - left_bottom.X)
                * (right_top.Y - left_bottom.Y);
        return area_sum == whole_area;
    }

    /**
     * #392 s = "abc", t = "ahbgdc" 返回 true. 示例 2: s = "axc", t = "ahbgdc" 返回
     * false.
     *
     * @param s sub string
     * @param t whole string
     * @return match result
     */
    @SuppressWarnings("SpellCheckingInspection, Unused")
    public static boolean isSubsequence(String s, String t) {
        int a = 0, b = 0;
        int s_len = s.length(), t_len = t.length();
        if (s_len == 0) {
            return true;
        }
        while (b < t_len) {
            if (s.charAt(a) == t.charAt(b)) {
                a++;
                if (a == s_len) {
                    return true;
                }
            }
            b++;
        }
        return false;
    }

    /**
     * #393
     *
     * @param data
     * @return
     */
    public static boolean validUtf8(int[] data) {
        int prev_type = 1;
        int trail_num = 0;
        for (var d : data) {
            var current_type = valType(d);
            if (current_type == 6)
                return false;

            switch (prev_type) {
            case 1 -> {
                if (current_type >= 5)
                    return false;
                trail_num = current_type - 1;
            }
            case 2 -> {
                if (current_type != 5)
                    return false;
                trail_num = 0;
            }
            case 3 -> {
                if (current_type != 5)
                    return false;
                trail_num = 1;
            }
            case 4 -> {
                if (current_type != 5)
                    return false;
                trail_num = 2;
            }
            case 5 -> {
                if (current_type != 5) {
                    if (trail_num != 0)
                        return false;
                } else {
                    if (trail_num <= 0)
                        return false;
                    trail_num--;
                }
            }
            default -> {
                System.out.println(prev_type);
                throw new RuntimeException("algo error");
            }
            }
            prev_type = current_type;
        }
        return trail_num == 0;
    }

    private static int valType(int d) {
        int range_1_l = 0b00000000, range_1_r = 0b01111111;
        int range_2_l = 0b11000000, range_2_r = 0b11011111;
        int range_3_l = 0b11100000, range_3_r = 0b11101111;
        int range_4_l = 0b11110000, range_4_r = 0b11110111;
        int range_trail_l = 0b10000000, range_trail_r = 0b10111111;
        if (d >= range_1_l && d <= range_1_r) {
            return 1;
        } else if (d >= range_2_l && d <= range_2_r) {
            return 2;
        } else if (d >= range_3_l && d <= range_3_r) {
            return 3;
        } else if (d >= range_4_l && d <= range_4_r) {
            return 4;
        } else if (d >= range_trail_l && d <= range_trail_r) {
            return 5;
        } else {
            return 6;
        }
    }

    /**
     * 394
     *
     * @param s
     * @return
     */
    public String decodeString(String s) {
        return decodeString(s.chars().mapToObj(c -> (char) c).iterator())
                .toString();
    }

    public String decodeString(Iterator<Character> it) {
        StringBuilder res = new StringBuilder();
        while (it.hasNext()) {
            char c = it.next();
            if (c >= '0' && c <= '9') {
                StringBuilder num = new StringBuilder();
                num.append(c);
                res.append(decodeDuplicated(it, num));
            } else if (c == ']') {
                break;
            } else {
                res.append(c);
            }
        }
        return res.toString();
    }

    public String decodeDuplicated(Iterator<Character> it, StringBuilder num) {
        StringBuilder res = new StringBuilder();
        int n = 1;
        while (it.hasNext()) {
            char c = it.next();
            if (c >= '0' && c <= '9') {
                num.append(c);
            } else if (c == '[') {
                n = Integer.valueOf(num.toString());
                res.append(decodeString(it).repeat(n));
                break;
            }
        }
        return res.toString();
    }

    /**
     * #397
     *
     * @param n
     * @return
     */
    public static int integerReplacement(int n) {
        if (n == 1) {
            return 0;
        }

        if (n % 2 == 0) {
            return 1 + integerReplacement(n / 2);
        } else {
            return 2 + Math.min(integerReplacement(n / 2 + 1),
                    integerReplacement(n / 2));
        }
    }

    /**
     * #399
     *
     * @param equations
     * @param values
     * @param queries
     * @return
     */
    public static double[] calcEquation(List<List<String>> equations,
            double[] values, List<List<String>> queries) {
        Map<String, Integer> symToIdx = new HashMap<>(equations.size() * 2);
        {
            int idx = 0;
            for (int i = 0; i < equations.size(); i++) {
                var eq = equations.get(i);
                var first_symbol = eq.get(0);
                var second_symbol = eq.get(1);
                if (!symToIdx.containsKey(first_symbol)) {
                    symToIdx.put(first_symbol, idx++);
                }
                if (!symToIdx.containsKey(second_symbol)) {
                    symToIdx.put(second_symbol, idx++);
                }
            }
        }
        double[][] graph = new double[symToIdx.size()][symToIdx.size()];
        for (var arr : graph) {
            Arrays.fill(arr, -1);
        }
        for (int i = 0; i < equations.size(); i++) {
            var eq = equations.get(i);
            var first_symbol = eq.get(0);
            var second_symbol = eq.get(1);
            var first_idx = symToIdx.get(first_symbol);
            var second_idx = symToIdx.get(second_symbol);
            graph[first_idx][second_idx] = values[i];
            graph[first_idx][first_idx] = 1;
            graph[second_idx][second_idx] = 1;
            graph[second_idx][first_idx] = 1 / values[i];
        }
        double[] ans = new double[queries.size()];
        var visited = new BitSet(graph.length);
        for (int i = 0; i < queries.size(); i++) {
            var q = queries.get(i);
            var first_symbol = q.get(0);
            var second_symbol = q.get(1);
            if ((!symToIdx.containsKey(first_symbol))
                    || (!symToIdx.containsKey(second_symbol))) {
                ans[i] = -1;
            } else {
                var start_idx = symToIdx.get(first_symbol);
                var target_idx = symToIdx.get(second_symbol);
                ans[i] = DFS(graph, start_idx, target_idx, visited, 1);
            }
        }
        return ans;
    }

    private static double DFS(double[][] graph, Integer start_idx,
            Integer target_idx, BitSet visited, double ans) {
        visited.set(start_idx, true);

        if (graph[start_idx][target_idx] != -1) {
            visited.set(start_idx, false);
            return ans * graph[start_idx][target_idx];
        } else {
            for (int i = 0; i < graph.length; i++) {
                if (!visited.get(i) && graph[start_idx][i] != -1) {
                    var val = DFS(graph, i, target_idx, visited,
                            ans * graph[start_idx][i]);
                    if (val != -1) {
                        visited.set(start_idx, false);
                        return val;
                    }
                }
            }
        }

        visited.set(start_idx, false);
        return -1;
    }
}
