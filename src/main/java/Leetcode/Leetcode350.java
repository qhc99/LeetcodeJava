package Leetcode;

import java.util.*;
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
    // tTodo 313
    return 0;
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
    // tTodo 316
    return null;
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
    for(int i = 0; i < words.length; i++){
      var i_chars = characters[i];
      var i_len = words[i].length();
      for(int j = i + 1; j < words.length; j++){
        var j_chars = characters[j];
        if(!i_chars.intersects(j_chars)){
          max = Math.max(max, i_len * words[j].length());
        }
      }
    }
    return max;
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
