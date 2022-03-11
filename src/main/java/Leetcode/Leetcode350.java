package Leetcode;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@SuppressWarnings("JavaDoc")
public class Leetcode350 {

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
          matrixPrefixSum[i][j] = row[j - 1] + matrixPrefixSum[i - 1][j];
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
      return matrixPrefixSum[row2][col2] - matrixPrefixSum[row2][col1 - 1] -
              (matrixPrefixSum[row1 - 1][col2] - matrixPrefixSum[row1 - 1][col1 - 1]);
    }
  }

  /**
   * #309
   *
   * @param prices
   * @return
   */
  public static int maxProfit(int[] prices) {
    int[][] dp = new int[prices.length][3]; // 0, have, not freeze. 1, not have, freeze. 2 not have, not freeze.
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
    }
    else {
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
        }
        else if (cache[i][j] == -1) {
          int mid = i + 1;
          int max = Integer.MIN_VALUE;
          int temp = paddedNum[i] * paddedNum[j];
          while (mid <= j - 1) {
            apply(i, mid);
            apply(mid, j);
            max = Math.max(max, temp * paddedNum[mid] + cache[i][mid] + cache[mid][j]);
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
          merge(dataArr, idxArr, start, left_cache, right_cache, left_idx_cache, right_idx_cache);
        }
      }

      private void merge(
              int[] dataArr, int[] idxArr, int start,
              int[] dataCacheL, int[] dataCacheR, int[] idxCacheL, int[] idxCacheR) {
        int right_idx = 0;
        int left_idx = 0;
        System.arraycopy(dataArr, start, dataCacheL, 0, dataCacheL.length);
        System.arraycopy(dataArr, start + dataCacheL.length, dataCacheR, 0, dataCacheR.length);
        System.arraycopy(idxArr, start, idxCacheL, 0, idxCacheL.length);
        System.arraycopy(idxArr, start + idxCacheL.length, idxCacheR, 0, idxCacheR.length);

        for (int i = start; (i < start + dataCacheL.length + dataCacheR.length) && (right_idx < dataCacheR.length) && (left_idx < dataCacheL.length); i++) {
          if (dataCacheL[left_idx] <= dataCacheR[right_idx]) {
            dataArr[i] = dataCacheL[left_idx];
            idxArr[i] = idxCacheL[left_idx];
            ans[idxCacheL[left_idx]] += right_idx;
            left_idx++;
          }
          else {
            dataArr[i] = dataCacheR[right_idx];
            idxArr[i] = idxCacheR[right_idx];
            right_idx++;
          }
        }
        if (left_idx < dataCacheL.length) {
          System.arraycopy(dataCacheL, left_idx, dataArr, start + left_idx + right_idx, dataCacheL.length - left_idx);
          System.arraycopy(idxCacheL, left_idx, idxArr, start + left_idx + right_idx, idxCacheL.length - left_idx);
          for (int i = left_idx; i < idxCacheL.length; i++) {
            ans[idxCacheL[i]] += right_idx;
          }
        }
        else if (right_idx < dataCacheR.length) {
          System.arraycopy(dataCacheR, right_idx, dataArr, start + left_idx + right_idx, dataCacheR.length - right_idx);
          System.arraycopy(idxCacheR, right_idx, idxArr, start + left_idx + right_idx, idxCacheR.length - right_idx);
        }
      }
    };
    funcMergeSort.apply(array, IntStream.range(0, array.length).toArray(), 0, array.length);
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
          while (stack.len > 0 && stack.last() >= c && remain[stack.last() - 'a'] > 0) {
            stack.pop();
          }
          stack.add(c);
        }
      }
      else if (stack.notHas(c)) {
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
        }
        else if (larger(m, max)) {
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
      }
      else if (a[i] == b[i]) {
        continue;
      }
      else {
        return false;
      }
    }
    return false;
  }

  private static int[] subArray(int[] num, int[] order, int k) {
    if (k == num.length) {
      return num;
    }
    else {
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
      }
      else {
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

  private static void merge(int[] array, int start, int[] cache1, int[] cache2) {
    int right_idx = 0;
    int left_idx = 0;
    for (int i = start; (i < start + cache1.length + cache2.length) && (right_idx < cache2.length) && (left_idx < cache1.length); i++) {
      if (cache1[left_idx] > cache2[right_idx]) {
        array[i] = cache1[left_idx++];
      }
      else if (cache1[left_idx] < cache2[right_idx]) {
        array[i] = cache2[right_idx++];
      }
      else {
        int r = right_idx + 1;
        int l = left_idx + 1;
        while (l < cache1.length && r < cache2.length) {
          if (cache1[l] > cache2[r]) {
            array[i] = cache1[left_idx++];
            break;
          }
          else if (cache1[l] < cache2[r]) {
            array[i] = cache2[right_idx++];
            break;
          }
          else {
            r++;
            l++;
          }
        }
        if (l >= cache1.length) {
          array[i] = cache2[right_idx++];
        }
        else if (r >= cache2.length) {
          array[i] = cache1[left_idx++];
        }
      }
    }
    if (left_idx < cache1.length) {
      System.arraycopy(cache1, left_idx, array, start + left_idx + right_idx, cache1.length - left_idx);
    }
    else if (right_idx < cache2.length) {
      System.arraycopy(cache2, right_idx, array, start + left_idx + right_idx, cache2.length - right_idx);
    }
  }

  private static int randPartition(int[] a, int start, int end) { // base case (end -start)
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
    return i; //pivot idx
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
      }
      else if (array[i] > val) {
        exchange(array, i, gt--);
      }
      else {
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
    }
    else if (ith < left_total + 1) {
      return rankSearch(a, start, pivot_idx, ith);
    }
    else {
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
        System.arraycopy(array, start + cache1.length, cache2, 0, cache2.length);

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

        for (int i = start; (i < start + cache1.length + cache2.length) && (right_idx < cache2.length) && (left_idx < cache1.length); i++) {
          if (cache1[left_idx] <= cache2[right_idx]) {
            array[i] = cache1[left_idx++];
          }
          else {
            array[i] = cache2[right_idx++];
          }
        }
        if (left_idx < cache1.length) {
          System.arraycopy(cache1, left_idx, array, start + left_idx + right_idx, cache1.length - left_idx);
        }
        else if (right_idx < cache2.length) {
          System.arraycopy(cache2, right_idx, array, start + left_idx + right_idx, cache2.length - right_idx);
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
      }
      else {
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
        if(idx >= nodes.length) return -1;
        if (visited.get(idx)) return -1;
        visited.set(idx, true);
        var c = nodes[idx];
        if (c.equals("#")) return idx;
        else {
          var left = visit(idx + 1);
          if (left == -1) return -1;
          return visit(left + 1);
        }
      }
    };
    return (func.visit(0) == nodes.length - 1) && (visited.stream().count() == nodes.length);
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


}
