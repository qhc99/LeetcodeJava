package Leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
