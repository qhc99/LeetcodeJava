package Leetcode;

import java.util.Arrays;
import java.util.HashMap;


@SuppressWarnings({"JavadocDeclaration"})
public class Leetcode600 {

  /**
   * #551
   *
   * @param s
   * @return
   */
  public static boolean checkRecord(String s) {
    int a = 0, l = 0;
    for (int i = 0; i < s.length(); i++) {
      var c = s.charAt(i);
      if (c == 'A') {
        a++;
        l++;
        if (a == 2) return false;
      }
      else if (c == 'L') {
        l++;
        if (l == 3) return false;
      }
      else l = 0;
    }
    return true;
  }

  /**
   * #552
   *
   * @param n
   * @return
   */
  public static int checkRecord(int n) {
    // (0 A) P, L, LL, (1 A) P, L, LL, A
    long[] dp = new long[7];
    dp[0] = 1;
    dp[1] = 1;
    dp[6] = 1;
    int mod = 1_000_000_000 + 7;
    for (int i = 2; i <= n; i++) {
      var zero_A_P = dp[0];
      var zero_A_L = dp[1];
      var zero_A_LL = dp[2];
      var one_A_P = dp[3];
      var one_A_L = dp[4];
      var one_A_LL = dp[5];
      var one_A_A = dp[6];
      dp[0] = (zero_A_P + zero_A_L + zero_A_LL) % mod;
      dp[1] = zero_A_P;
      dp[2] = zero_A_L;
      dp[3] = (one_A_P + one_A_L + one_A_LL + one_A_A) % mod;
      dp[4] = (one_A_P + one_A_A) % mod;
      dp[5] = one_A_L;
      dp[6] = dp[0];
    }
    long ans = 0;
    for (var i : dp) {
      ans += i;
    }
    return (int) (ans % mod);
  }

  /**
   * #560
   * <br>find the count of continue sub-arrays which sum is k<br>
   * [1, 2, 3, 4], 3 ---> 2    (answer: [1, 2] and [3])
   *
   * @param nums array
   * @param k    sum target
   * @return count
   */
  public static int subarraySum(int[] nums, int k) {
    int count = 0, pre = 0;
    HashMap<Integer, Integer> mp = new HashMap<>();
    mp.put(0, 1);
    for (int num : nums) {
      pre += num;
      if (mp.containsKey(pre - k)) {
        count += mp.get(pre - k);
      }
      mp.put(pre, mp.getOrDefault(pre, 0) + 1);
    }
    return count;
  }

  /**
   * #583
   *
   * @param word1
   * @param word2
   * @return
   */
  public static int minDistance(String word1, String word2) {
    if (word2.length() > word1.length()) {
      var t = word1;
      word1 = word2;
      word2 = t;
    }
    int[] jump = new int['z' - 'a' + 1];
    Arrays.fill(jump, word1.length());
    int[][] next = new int[word1.length()][jump.length];
    for (int i = word1.length() - 1; i >= 0; i--) {
      var t = new int[jump.length];
      System.arraycopy(jump, 0, t, 0, jump.length);
      next[i] = t;
      var c = word1.charAt(i);
      jump[c - 'a'] = i;
    }
    int i = 0;
    int j = 0;
    int len = 0;
    while ( j < word2.length()) {
      jump = next[i];
      int min = Integer.MAX_VALUE;
      int min_idx = word2.length();
      for (int k = j; k < word2.length(); k++) {
        var c = word2.charAt(k);
        if (jump[c - 'a'] < min) {
          min = jump[c - 'a'];
          min_idx = k;
        }
      }
      j = min_idx + 1;
      i = jump[word2.charAt(min_idx) - 'a'];
      if (i < word1.length()) len++;
      else break;
    }
    return word1.length() - len + word2.length() - len;
  }
}
