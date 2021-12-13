package Leetcode;

import java.util.*;

@SuppressWarnings("JavaDoc")
public class Leetcode450 {

  /**
   * #404
   * <br>左叶子之和
   * <pre>
   *     3
   *    / \
   *   9  20
   *     /  \
   *    15   7
   *
   * 在这个二叉树中，有两个左叶子，分别是 9 和 15，所以返回 24
   * </pre>
   *
   * @param root root of bTree
   * @return sum of left leaves
   */
  @SuppressWarnings("unused")
  public static int sumOfLeftLeaves(TreeNode root) {
    if (root == null) {
      return 0;
    }
    else {
      if (root.left == null) {
        return sumOfLeftLeaves(root.right);
      }
      else if (root.left.left == null && root.left.right == null) {
        return root.left.val + sumOfLeftLeaves(root.right);
      }
      else {
        return sumOfLeftLeaves(root.left) + sumOfLeftLeaves(root.right);
      }
    }
  }

  /**
   * #410
   * <br>输入:
   * <br>nums = [7,2,5,10,8]
   * <br>m = 2
   * <br>输出:
   * <br>18
   * <br>解释:
   * <br>一共有四种方法将nums分割为2个子数组。
   * <br>其中最好的方式是将其分为[7,2,5] 和 [10,8]，
   * <br>因为此时这两个子数组各自的和的最大值为18，在所有情况中最小。
   *
   * @param nums array
   * @param m    number of group
   * @return min group sum
   */
  @SuppressWarnings("unused")
  public static int splitArray(int[] nums, int m) {
    long sum = 0;
    long max = 0;
    for (int num : nums) {
      sum += num;
      max = Math.max(max, num);
    }
    while (max < sum) {
      long mid = (sum - max) / 2 + max;
      if (canSplit(nums, mid, m)) {
        sum = mid;
      }
      else {
        max = mid + 1;
      }
    }
    return (int) max;
  }

  private static boolean canSplit(int[] nums, long sum, int limit) {
    int split = 1;
    int tSum = 0;
    for (var n : nums) {
      if (tSum + n <= sum) {
        tSum += n;
      }
      else {
        tSum = n;
        split++;
        if (split > limit) {
          return false;
        }
      }
    }
    return split <= limit;
  }

  /**
   * #416
   *
   * @param nums
   * @return
   */
  public static boolean canPartition(int[] nums) {
    if (nums.length <= 1) {
      return false;
    }

    var sum = Arrays.stream(nums).sum();
    if (sum % 2 == 1) {
      return false;
    }

    BitSet dp = new BitSet(sum / 2 + 1);
    dp.set(0, true);

    for (int c = 1; c <= nums.length; c++) {
      var num = nums[c - 1];
      for (int j = sum / 2; j - num >= 0; j--) {
        if(dp.get(j-num)){
          dp.set(j, true);
        }
      }
    }

    return dp.get(sum / 2);
  }

  /**
   * #438
   *
   * @param s
   * @param p
   * @return
   */
  public static List<Integer> findAnagrams(String s, String p) {
    int p_len = p.length();
    Map<Character, Integer> chars = new HashMap<>(p_len);
    Map<Character, Integer> current = new HashMap<>(p_len);
    for (int i = 0; i < p_len; i++) {
      var c = p.charAt(i);
      chars.put(c, chars.getOrDefault(c, 0) + 1);
    }
    List<Integer> ans = new ArrayList<>(16);
    int l = 0;
    for (int r = 0; r < s.length(); r++) {
      var c = s.charAt(r);
      if (chars.containsKey(c)) {
        if (canFillIn(c, current, chars)) {
          current.put(c, current.getOrDefault(c, 0) + 1);
          if (r + 1 - l == p_len) {
            ans.add(l);
            var last_c = s.charAt(l);
            current.put(last_c, current.get(last_c) - 1);
            l++;
          }
        }
        else {
          do {
            var last_c = s.charAt(l);
            current.put(last_c, current.get(last_c) - 1);
            l++;
          } while (!canFillIn(c, current, chars));
          current.put(c, current.getOrDefault(c, 0) + 1);
        }
      }
      else {
        l = r + 1;
        current.clear();
      }
    }
    return ans;
  }

  private static boolean canFillIn(char c, Map<Character, Integer> current, Map<Character, Integer> chars) {
    return current.getOrDefault(c, 0) + 1 <= chars.get(c);
  }
}
