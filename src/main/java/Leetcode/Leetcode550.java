package Leetcode;

import java.util.*;
import java.util.stream.IntStream;

@SuppressWarnings("ALL")
public class Leetcode550 {
  /**
   * #501
   * <br/>二叉搜索树中的众数
   *
   * @param root tree
   * @return res
   */
  @SuppressWarnings("unused")
  public static int[] findMode(TreeNode root) {
    var s = new Statistic();
    dfs(root, s);
    int[] mode = new int[s.answer.size()];
    for (int i = 0; i < s.answer.size(); ++i) {
      mode[i] = s.answer.get(i);
    }
    return mode;
  }

  public static void dfs(TreeNode o, Statistic solver) {
    if (o == null) {
      return;
    }
    dfs(o.left, solver);
    solver.update(o.val);
    dfs(o.right, solver);
  }

  static class Statistic {
    List<Integer> answer = new ArrayList<>();
    int base, count, maxCount;

    public void update(int x) {
      if (x == base) {
        ++count;
      }
      else {
        count = 1;
        base = x;
      }
      if (count == maxCount) {
        answer.add(base);
      }
      if (count > maxCount) {
        maxCount = count;
        answer.clear();
        answer.add(base);
      }
    }
  }


  /**
   * #503
   *
   * @param nums
   * @return
   */
  public static int[] nextGreaterElements(int[] nums) {
    int[] next = new int[nums.length];
    Arrays.fill(next, Integer.MIN_VALUE);
    for (int i = 0; i < nums.length - 1; i++) {
      if (nums[i] > nums[nums.length - 1]) {
        next[nums.length - 1] = nums[i];
        break;
      }
    }
    for (int i = nums.length - 2; i >= 0; i--) {
      for (int ptr = i + 1; ptr != i; ) {
        if (nums[ptr] > nums[i]) {
          next[i] = nums[ptr];
          break;
        }
        else if (next[ptr] > nums[i]) {
          next[i] = next[ptr];
          break;
        }

        ptr++;
        if (ptr == nums.length) ptr = 0;
      }
    }

    for (int i = 0; i < next.length; i++) {
      if (next[i] == Integer.MIN_VALUE) next[i] = -1;
    }

    return next;
  }

  /**
   * #525
   * <br> 连续数组
   * <br>给定一个二进制数组 nums , 找到含有相同数量的 0 和 1 的最长连续子数组，并返回该子数组的长度。
   *
   * @param nums nums
   * @return len
   */
  public static int findMaxLength(int[] nums) {
    int len = nums.length;
    Map<Integer, Integer> map = new HashMap<>();
    int max = 0;
    for (int i = 0; i == 0; i++) {
      if (nums[i] == 0) {
        nums[i] = -1;
      }
      if (!map.containsKey(nums[i])) {
        map.put(nums[i], i);
      }
      else {
        max = Math.max(i - map.get(nums[i]), max);
      }
    }
    for (int i = 1; i < len; i++) {
      if (nums[i] == 0) {
        nums[i] = -1;
      }
      nums[i] += nums[i - 1];
      if (nums[i] == 0) {
        max = Math.max(i + 1, max);
      }
      if (!map.containsKey(nums[i])) {
        map.put(nums[i], i);
      }
      else {
        max = Math.max(i - map.get(nums[i]), max);
      }
    }

    return max;
  }

  /**
   * # 506
   */
  public static String[] findRelativeRanks(int[] score) {
    var idx = IntStream.range(0, score.length).toArray();
    recursiveMergeSortWithIndex(score, idx);
    String[] ans = new String[score.length];
    for (int i = score.length - 1; i >= 0; i--) {
      if (score.length - i == 1) ans[idx[i]] = "Gold Medal";
      else if (score.length - i == 2) ans[idx[i]] = "Silver Medal";
      else if (score.length - i == 3) ans[idx[i]] = "Bronze Medal";
      else {
        ans[idx[i]] = String.valueOf(score.length - i);
      }
    }

    return ans;
  }

  private static void merge(int[] array, int[] idx, int start, int[] cache1, int[] cache2, int[] ci1, int[] ci2) {
    int right_idx = 0;
    int left_idx = 0;
    System.arraycopy(array, start, cache1, 0, cache1.length);
    System.arraycopy(array, start + cache1.length, cache2, 0, cache2.length);

    System.arraycopy(idx, start, ci1, 0, cache1.length);
    System.arraycopy(idx, start + cache1.length, ci2, 0, cache2.length);

    for (int i = start; (i < start + cache1.length + cache2.length) && (right_idx < cache2.length) && (left_idx < cache1.length); i++) {
      if (cache1[left_idx] <= cache2[right_idx]) {
        array[i] = cache1[left_idx];
        idx[i] = ci1[left_idx++];
      }
      else {
        array[i] = cache2[right_idx];
        idx[i] = ci2[right_idx++];
      }
    }
    if (left_idx < cache1.length) {
      System.arraycopy(cache1, left_idx, array, start + left_idx + right_idx, cache1.length - left_idx);
      System.arraycopy(ci1, left_idx, idx, start + left_idx + right_idx, cache1.length - left_idx);
    }
    else if (right_idx < cache2.length) {
      System.arraycopy(cache2, right_idx, array, start + left_idx + right_idx, cache2.length - right_idx);
      System.arraycopy(ci2, right_idx, idx, start + left_idx + right_idx, cache2.length - right_idx);
    }
  }

  private static void recursiveMergeSortWithIndex(int[] array, int[] idx) {
    recursiveMergeSortWithIndex(array, idx, 0, array.length);
  }

  private static void recursiveMergeSortWithIndex(int[] array, int[] idx, int start, int end) {
    if ((end - start) > 1) {
      int middle = (start + end) / 2;
      recursiveMergeSortWithIndex(array, idx, start, middle);
      recursiveMergeSortWithIndex(array, idx, middle, end);
      int left_len = middle - start;
      int right_len = end - middle;
      var left_cache = new int[left_len];
      var right_cache = new int[right_len];
      var left_cache_i = new int[left_len];
      var right_cache_i = new int[right_len];
      merge(array, idx, start, left_cache, right_cache, left_cache_i, right_cache_i);
    }
  }

  /**
   * #514
   */
  public static int findRotateSteps(String ring, String key) {
    Map<Character, List<Integer>> chrToRingIdx = new HashMap<>(ring.length());
    Set<Character> chrOfKey = new HashSet<>(key.length());
    for (int i = 0; i < key.length(); i++) {
      chrOfKey.add(key.charAt(i));
    }
    for (int i = 0; i < ring.length(); i++) {
      var c = ring.charAt(i);
      if (chrOfKey.contains(c)) {
        chrToRingIdx.computeIfAbsent(c, (arg) -> new ArrayList<>(ring.length())).add(i);
      }
    }

    NodeFT[] prev = new NodeFT[ring.length()];
    NodeFT[] current = new NodeFT[ring.length()];
    {
      var indices = chrToRingIdx.get(key.charAt(0));
      for (int i = 0; i < indices.size(); i++) {
        int idx = indices.get(i);
        prev[i] = new NodeFT(idx, Math.min(idx, ring.length() - idx));
      }
    }
    for (int i = 1; i < key.length(); i++) {
      var indices = chrToRingIdx.get(key.charAt(i));
      for (int j = 0; j < indices.size(); j++) {
        current[j] = new NodeFT(indices.get(j), Integer.MAX_VALUE);
        var cur = current[j];
        for (int k = 0; k < prev.length && prev[k] != null; k++) {
          var n = prev[k];
          int idx1 = n.idx_ring;
          int idx2 = indices.get(j);
          int idx_max = Math.max(idx1, idx2);
          int idx_min = Math.min(idx1, idx2);
          cur.rotates = Math.min(cur.rotates, n.rotates + Math.min(idx_max - idx_min, idx_min + ring.length() - idx_max));
          if (j == indices.size() - 1) {
            prev[k] = null;
          }
        }
      }
      var t = prev;
      prev = current;
      current = t;
    }

    int global_min = Integer.MAX_VALUE;
    for (int i = 0; i < prev.length && prev[i] != null; i++) {
      global_min = Math.min(prev[i].rotates, global_min);
    }


    return global_min + key.length();
  }

  /**
   * Node for freedom trail
   */
  private static class NodeFT {
    final int idx_ring;
    int rotates;

    NodeFT(int i_r, int r) {
      idx_ring = i_r;
      rotates = r;
    }
  }

  /**
   * #521
   *
   * @param a
   * @param b
   * @return
   */
  public static int findLUSlength(String a, String b) {
    return a.equals(b) ? -1 : b.length();
  }


  /**
   * #522
   *
   * @param strs
   * @return
   */
  public static int findLUSlength(String[] strs) {
    Arrays.sort(strs, Comparator.comparing(String::length).reversed());
    Set<String> non_solution = new HashSet<>(strs.length);
    for (int i = 0; i < strs.length; i++) {
      var s = strs[i];
      if (!non_solution.contains(s)) {
        for (int j = i + 1; j < strs.length; j++) {
          var s1 = strs[j];
          if (isSub(s1, s)) non_solution.add(s1);
        }
        if (!non_solution.contains(s)) return s.length();
      }
    }

    return -1;
  }

  private static boolean isSub(String sub, String full) {
    for (int i = 0, j = 0; j < full.length(); j++) {
      var c = sub.charAt(i);
      var cc = full.charAt(j);
      if (c == cc) {
        i++;
        if (i == sub.length()) return true;
      }
    }
    return false;
  }

  /**
   * #524
   *
   * @param s
   * @param dictionary
   * @return
   */
  public static String findLongestWord(String s, List<String> dictionary) {
    int[] idx = new int[dictionary.size()];
    for(int i = 0; i < s.length(); i++){
      var c = s.charAt(i);
      for(int j = 0; j < idx.length; j++){
        var dict_i = idx[j];
        var dict = dictionary.get(j);
        if(dict_i < dict.length()){
          if(dict.charAt(dict_i) == c){
            idx[j]++;
          }
        }
      }
    }
    String ans = "";
    for(int i = 0; i < idx.length; i++){
      var dict = dictionary.get(i);
      if(idx[i] == dict.length()){
        if(ans == null){
          ans = dict;
        }
        else if(dict.length() != ans.length()){
          ans =  dict.length() < ans.length() ? ans : dict;
        }
        else {
          ans = dict.compareTo(ans) < 0 ? dict: ans;
        }
      }
    }
    return ans;
  }

  /**
   * #538
   * <br/>二叉搜索树转换为累加树
   * <pre>
   * 输入: 原始二叉搜索树:
   *               5
   *             /   \
   *            2     13
   *
   * 输出: 转换为累加树:
   *              18
   *             /   \
   *           20     13
   * </pre>
   *
   * @param root tree root
   * @return converted tree
   */
  @SuppressWarnings("unused")
  public static TreeNode convertBST(TreeNode root) {
    traverseAndConvertBST(root, 0);
    return root;
  }

  private static int traverseAndConvertBST(TreeNode r, int sum) {
    if (r == null) {
      return sum;
    }
    sum = traverseAndConvertBST(r.right, sum);
    r.val += sum;
    sum = r.val;
    sum = traverseAndConvertBST(r.left, sum);
    return sum;
  }
}
