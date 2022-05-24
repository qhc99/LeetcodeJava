package Leetcode;

import java.util.*;

@SuppressWarnings("JavaDoc")
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
      }
      else {
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
   * #357
   *
   * @param n
   * @return
   */
  public static int countNumbersWithUniqueDigits(int n) {
    int[] ans = new int[]{0, 10, 91, 739, 5275, 32491, 168571, 712891, 2345851};
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
      }
      else {
        lenRow = m.length;
        lenCol = m[0].length;
      }
    }

    int get(int i, int j) {
      if (inverse) return m[j][i];
      else return m[i][j];
    }

    void set(int i, int j, int v) {
      if (inverse) m[j][i] = v;
      else m[i][j] = v;
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
   * #373
   *
   * @param nums1
   * @param nums2
   * @param k
   * @return
   */
  public static List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
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


    PriorityQueue<Data> queue = new PriorityQueue<>(Comparator.comparing(d -> d.sum));
    List<List<Integer>> ans = new ArrayList<>();
    for (int i = 0; i < nums1.length; i++) {
      queue.add(new Data(0, nums1[i], nums2[0], nums1[i] + nums2[0]));
    }
    while (queue.size() > 0 && k > 0) {
      k--;
      var d = queue.poll();
      ans.add(List.of(d.first, d.second));
      if (d.idx + 1 < nums2.length) {
        queue.add(new Data(d.idx + 1, d.first, nums2[d.idx + 1], d.first + nums2[d.idx + 1]));
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
    int s = 1, e = n + 1;
    int ans = 0;
    while (e - s > 2) {
      int mid = (e + s) / 2;
      ans += mid;
      s = mid;
    }
    return ans;
  }


  /**
   * #381
   * <br/>设计一个支持在平均 时间复杂度 O(1) 下， 执行以下操作的数据结构。
   * <br/>insert(val)：向集合中插入元素 val。
   * <br/>remove(val)：当 val 存在时，从集合中移除一个 val。
   * <br/>getRandom：从现有集合中随机获取一个元素。每个元素被返回的概率应该与其在集合中的数量呈线性相关。
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
     * Inserts a value to the collection. Returns true if the collection did not already contain the specified element.
     */
    public boolean insert(int val) {
      nums.add(val);
      Set<Integer> set = idx.getOrDefault(val, new HashSet<>());
      set.add(nums.size() - 1);
      idx.put(val, set);
      return set.size() == 1;
    }

    /**
     * Removes a value from the collection. Returns true if the collection contained the specified element.
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
   * #392
   * s = "abc", t = "ahbgdc"
   * 返回 true.
   * 示例 2:
   * s = "axc", t = "ahbgdc"
   * 返回 false.
   *
   * @param s sub string
   * @param t whole string
   * @return match result
   */
  @SuppressWarnings("SpellCheckingInspection, unused")
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
   * 394
   *
   * @param s
   * @return
   */
  public static String decodeString(String s) {
    StringBuilder ans = null;
    int idx = 0;
    while (idx < s.length()) { // link early return
      var d = recursiveDecode(s, idx);
      idx = d.end_idx;
      if (ans == null) ans = d.builder;
      else ans.append(d.builder);
      idx++;
    }
    return ans.toString();
  }

  static class Data {
    final StringBuilder builder;
    final int end_idx;

    Data(StringBuilder sb, int e) {
      builder = sb;
      end_idx = e;
    }
  }

  private static Data recursiveDecode(String s, int idx) {
    var sb = new StringBuilder();
    int num = 0;
    while (idx < s.length()) {
      var ctr = s.charAt(idx);
      switch (ctr) {
        case '[' -> {
          var d = recursiveDecode(s, ++idx);
          idx = d.end_idx;
          sb.append(d.builder.toString().repeat(num));
          num = 0;
        }
        case ']' -> {
          return new Data(sb, idx);
        }
        default -> {
          var op_int = tryParseInt(String.valueOf(ctr));
          if (op_int.isPresent()) {
            num = num * 10 + op_int.get();
          }
          else {
            sb.append(ctr);
          }
        }
      }
      idx++;
    }
    return new Data(sb, idx);
  }

  private static Optional<Integer> tryParseInt(String s) {
    int radix = 10;
    if (s == null) {
      return Optional.empty();
    }

    if (radix < Character.MIN_RADIX) {
      return Optional.empty();
    }

    if (radix > Character.MAX_RADIX) {
      return Optional.empty();
    }

    boolean negative = false;
    int i = 0, len = s.length();
    int limit = -Integer.MAX_VALUE;

    if (len > 0) {
      char firstChar = s.charAt(0);
      if (firstChar < '0') { // Possible leading "+" or "-"
        if (firstChar == '-') {
          negative = true;
          limit = Integer.MIN_VALUE;
        }
        else if (firstChar != '+') {
          return Optional.empty();
        }

        if (len == 1) { // Cannot have lone "+" or "-"
          return Optional.empty();
        }
        i++;
      }
      int multmin = limit / radix;
      int result = 0;
      while (i < len) {
        // Accumulating negatively avoids surprises near MAX_VALUE
        int digit = Character.digit(s.charAt(i++), radix);
        if (digit < 0 || result < multmin) {
          return Optional.empty();
        }
        result *= radix;
        if (result < limit + digit) {
          return Optional.empty();
        }
        result -= digit;
      }
      return Optional.of(negative ? result : -result);
    }
    else {
      return Optional.empty();
    }
  }
}
