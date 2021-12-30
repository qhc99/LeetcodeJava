package Leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("JavaDoc")
public class Leetcode300 {

  /**
   * #257
   *
   * @param root
   * @return
   */
  public static List<String> binaryTreePaths(TreeNode root) {
    StringBuilder stringStack = new StringBuilder();
    List<String> ans = new ArrayList<>();
    var recurFunc = new Object() {
      void apply(TreeNode node) {
        var idx = stringStack.length();
        if (node.left == null && node.right == null) {
          if (stringStack.length() == 0) stringStack.append(node.val);
          else stringStack.append("->").append(node.val);
          ans.add(stringStack.toString());
          stringStack.delete(idx, stringStack.length());
        }
        else {
          if (node.left != null) {
            if (stringStack.length() == 0) stringStack.append(node.val);
            else stringStack.append("->").append(node.val);
            apply(node.left);
            stringStack.delete(idx, stringStack.length());
          }
          if (node.right != null) {
            if (stringStack.length() == 0) stringStack.append(node.val);
            else stringStack.append("->").append(node.val);
            apply(node.right);
            stringStack.delete(idx, stringStack.length());
          }
        }
      }

    };
    recurFunc.apply(root);
    return ans;
  }

  /**
   * #274
   *
   * @param citations
   * @return
   */
  public static int hIndex(int[] citations) {
    Arrays.sort(citations);
    int n = citations.length;
    int s = 0, e = citations.length + 1;
    while (e - s > 1) {
      int mid = (s + e) / 2;
      if (mid == 0) {
        if (citations[citations.length - 1] <= mid) s = mid;
        else e = mid;
      }
      else {
        if (citations[n - mid] >= mid) s = mid;
        else e = mid;
      }
    }
    return s;
  }

  /**
   * #279
   *
   * @param n
   * @return
   */
  public static int numSquares(int n) {
    int[] squares = new int[(int) Math.sqrt(n)];
    for (int i = 1; i <= squares.length; i++) {
      squares[squares.length - i] = i * i;
    }
    int[] dp = new int[n + 1];
    dp[0] = 0;
    for (var s : squares) {
      dp[s] = 1;
    }
    if (dp[n] == 1) return 1;


    while (true) {
      for (int i = n; i > 0; i--) {
        for (var s : squares) {
          if (i - s >= 0 && dp[i - s] != 0 && dp[i] == 0) {
            dp[i] = dp[i - s] + 1;
            if (i == n) return dp[i];
          }
        }
      }
    }
  }

  /**
   * #282
   *
   * @param num
   * @param target
   * @return
   */
  public static List<String> addOperators(String num, int target) {
    class SharedStack {
      final long num;
      final char ctr; // p d * / + -
      final SharedStack prev;

      SharedStack(long n, char c, SharedStack p) {
        num = n;
        ctr = c;
        prev = p;
      }
    }
    class SharedString {
      final SharedString prev;
      final char ctr;

      SharedString(char c, SharedString p) {
        ctr = c;
        prev = p;
      }
    }

    var digits_ctr = num.toCharArray();
    List<String> ans = new ArrayList<>();
    var recurFunc = new Object() {
      void apply(int idx, SharedStack stack, SharedString expr) {
        long current_digit = digits_ctr[idx] - '0';
        if (stack != null && stack.ctr == 'p')
          stack = new SharedStack(stack.prev.num * 10 + current_digit, 'd', stack.prev.prev);
        else
          stack = new SharedStack(current_digit, 'd', stack);
        expr = new SharedString(digits_ctr[idx], expr);

        if (idx == digits_ctr.length - 1) {
          var res = computeRightToLeft(stack);
          if (res == null) return;
          if (res.num == target) {
            StringBuilder sb = new StringBuilder();
            var ptr = expr;
            while (ptr != null) {
              sb.insert(0, ptr.ctr);
              ptr = ptr.prev;
            }
            ans.add(sb.toString());
          }
        }
        else {
          var mergedStack = mergeMulDiv(stack);
          if(mergedStack != null){
            apply(idx + 1,
                    new SharedStack(0, '*', mergedStack),
                    new SharedString('*', expr));
            if (idx + 1 < digits_ctr.length && digits_ctr[idx + 1] != '0') {
              apply(idx + 1,
                      new SharedStack(0, '/', stack),
                      new SharedString('/', expr));
            }
          }
          var computedStack = computeRightToLeft(stack);
          if(computedStack != null){
            apply(idx + 1,
                    new SharedStack(0, '+', computedStack),
                    new SharedString('+', expr));
            apply(idx + 1,
                    new SharedStack(0, '-', computedStack),
                    new SharedString('-', expr));
          }

          if (stack.num != 0) {
            apply(idx + 1,
                    new SharedStack(0, 'p', stack),
                    expr);
          }
        }
      }

      SharedStack computeRightToLeft(SharedStack stack) {
        var origin = stack;
        var numR = stack.num;
        stack = stack.prev;
        while (stack != null) {
          var op = stack.ctr;
          stack = stack.prev;
          var numL = stack.num;
          stack = stack.prev;
          switch (op) {
            case '*' -> numR *= numL;
            case '/' -> {
              if (numR == 0) return null;
              else numR = numL / numR;
            }
            case '+' -> numR += numL;
            case '-' -> numR = numL - numR;
            default -> throw new RuntimeException(String.valueOf(op));
          }
        }
        return new SharedStack(numR, 'd', null);
      }

      SharedStack mergeMulDiv(SharedStack stack) {
        var origin = stack;
        var numR = stack.num;
        stack = stack.prev;
        while (stack != null && (stack.ctr == '*' || stack.ctr == '/')) {
          var op = stack.ctr;
          stack = stack.prev;
          var numL = stack.num;
          stack = stack.prev;

          switch (op) {
            case '*' -> numR *= numL;
            case '/' -> {
              if (numR == 0) return null;
              numR = numL / numR;
            }
            default -> throw new RuntimeException();
          }
        }
        return new SharedStack(numR, 'd', stack);
      }
    };
    recurFunc.apply(0, null, null);
    return ans;
  }
}
