package Leetcode;

import java.util.*;

public class Leetcode400 {

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
}
