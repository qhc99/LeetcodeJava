package Leetcode;


import java.util.*;

public class Leetcode100 {

  /**
   * #54
   * @param matrix
   * @return
   */
  public static List<Integer> spiralOrder(int[][] matrix) {
    int top = 0, left = 0, bot = matrix.length-1, right = matrix[0].length-1;
    List<Integer> ans = new ArrayList<>((bot+1)*(right+1));
    while (top <= bot && left <= right){
      for(int i = left; i <= right; i++){
        ans.add(matrix[top][i]);
      }
      for(int i = top+1; i <= bot; i++){
        ans.add(matrix[i][right]);
      }
      if(left < right && top < bot){
        for(int i = right - 1; i >= left; i--){
          ans.add(matrix[bot][i]);
        }
        for(int i = bot -1; i > top; i--){
          ans.add(matrix[i][left]);
        }
      }
      top++;
      left++;
      bot--;
      right--;
    }
    return ans;
  }

  /**
   * #55
   * <br/>跳跃游戏
   * <br/>
   * <pre>
   * 输入: [2,3,1,1,4]
   * 输出: true
   * 解释: 我们可以先跳 1 步，从位置 0 到达 位置 1, 然后再从位置 1 跳 3 步到达最后一个位置。
   * </pre>
   *
   * @param nums int array
   * @return res
   */
  public static boolean canJump(int[] nums) {
    int remotest = 0;
    for (int i = 0; i < nums.length; i++) {
      if (i <= remotest) {
        remotest = Math.max(remotest, i + nums[i]);
        if (remotest >= nums.length - 1) {
          return true;
        }
      }
    }
    return remotest >= nums.length - 1;
  }

  /**
   * #56
   * @param intervals
   * @return
   */
  public static int[][] merge(int[][] intervals) {
    Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
    List<int[]> ans = new ArrayList<>(intervals.length);
    for(int i = 0; i < intervals.length-1; i++){
      var interval = intervals[i];
      var interval_next = intervals[i+1];
      int l1 = interval[0], r1 = interval[1];
      int l2 = interval_next[0], r2 = interval_next[1];
      if((l2 <= r1 && l2 >= l1) || (r2 <= r1 && r2 >= l1)){
        interval_next[0] = Math.min(l1,l2);
        interval_next[1] = Math.max(r1,r2);
      }
      else {
        ans.add(interval);
      }
    }
    ans.add(intervals[intervals.length-1]);
    return ans.toArray(new int[][]{});
  }

  /**
   * #57
   * @param intervals
   * @param newInterval
   * @return
   */
  public static int[][] insert(int[][] intervals, int[] newInterval) {
    int[][] newIntervals = new int[intervals.length+1][];
    System.arraycopy(intervals,0,newIntervals,0, intervals.length);
    newIntervals[newIntervals.length-1] = newInterval;
    Arrays.sort(newIntervals, Comparator.comparingInt(i -> i[0]));
    List<int[]> ans = new ArrayList<>(newIntervals.length);
    for(var i : newIntervals){
      if(ans.size() == 0){
        ans.add(i);
      }
      else {
        var last = ans.get(ans.size()-1);
        if(intersect(last,i)){
          last[0] = Math.min(last[0], i[0]);
          last[1] = Math.max(last[1], i[1]);
        }
        else {
          ans.add(i);
        }
      }
    }
    return ans.toArray(new int[][]{});
  }

  public static boolean intersect(int[] a, int[] b){
    int l1 = a[0], r1 = a[1];
    int l2 = b[0], r2 = b[1];
    return (l2 >= l1 && l2 <= r1) || (r2 >= l1 && r2 <= r1);
  }

  /**
   * #64
   * 说明：每次只能向下或者向右移动一步。
   * (上, 左)
   * 输入:
   * [
   * [1,3,1],
   * [1,5,1],
   * [4,2,1]
   * ]
   * 输出: 7
   * 解释: 因为路径 1→3→1→1→1 的总和最小。
   *
   * @param grid int graph
   * @return path sum
   */
  @SuppressWarnings("unused")
  public static int minPathSum(int[][] grid) {
    int r_len = grid.length;
    int c_len = grid[0].length;
    for (int s = 1; s <= r_len + c_len - 2; s++) {
      for (int i = 0; i < r_len && i <= s; i++) {
        int j = s - i;
        if (j < 0 || j >= c_len) {
          continue;
        }
        grid[i][j] += minPath(i, j, grid);
      }
    }
    return grid[r_len - 1][c_len - 1];
  }

  private static int minPath(int i, int j, int[][] cache) {
    double u = (i - 1 < 0) ? Double.POSITIVE_INFINITY : cache[i - 1][j];
    double l = (j - 1 < 0) ? Double.POSITIVE_INFINITY : cache[i][j - 1];
    return (int) Math.min(l, u);
  }

  /**
   * #74
   * <br/>搜索二维矩阵
   *
   * @param matrix matrix
   * @param target integer target
   * @return target exist
   */
  public static boolean searchMatrix(int[][] matrix, int target) {
    int len = matrix.length * matrix[0].length;
    int col_len = matrix[0].length;
    int l_idx = 0, r_idx = len;

    while (r_idx - l_idx > 0) {
      int mid = (r_idx + l_idx) / 2;
      int r = mid / col_len;
      int c = mid - col_len * r;
      int v = matrix[r][c];
      if (target > v) {
        l_idx = mid + 1;
      }
      else if (target < v) {
        r_idx = mid;
      }
      else {
        return true;
      }
    }

    return false;
  }

  /**
   * #78
   * <br/>子集
   * <pre>
   * 输入: nums = [1,2,3]
   * 输出:
   * [
   *   [3],
   *  [1],
   *  [2],
   *  [1,2,3],
   *  [1,3],
   *  [2,3],
   *  [1,2],
   *  []
   * ]
   * </pre>
   *
   * @param nums int array
   * @return subsets of nums
   */
  @SuppressWarnings("unused")
  public static List<List<Integer>> subsets(int[] nums) {
    List<List<Integer>> res = new ArrayList<>();
    recursiveSubsets(nums, 0, res);
    return res;
  }

  private static void recursiveSubsets(int[] nums, int idx, List<List<Integer>> res) {
    if (idx == nums.length) {
      res.add(new ArrayList<>());
    }
    else {
      recursiveSubsets(nums, idx + 1, res);
      List<List<Integer>> temp = new ArrayList<>();
      for (var list : res) {
        List<Integer> newList = new ArrayList<>(list);
        newList.add(nums[idx]);
        temp.add(newList);
      }
      res.addAll(temp);
    }
  }

  /**
   * #90
   * <br/>子集 II
   *
   * @param nums number array
   * @return subset
   */


  public static List<List<Integer>> subsetsWithDup(int[] nums) {
    List<Integer> t = new ArrayList<>();
    List<List<Integer>> ans = new ArrayList<>();
    Arrays.sort(nums);
    dfs(false, 0, nums, t, ans);
    return ans;
  }

  public static void dfs(boolean choosePre, int cur, int[] nums, List<Integer> t, List<List<Integer>> ans) {
    if (cur == nums.length) {
      ans.add(new ArrayList<>(t));
      return;
    }
    dfs(false, cur + 1, nums, t, ans);
    if (!choosePre && cur > 0 && nums[cur - 1] == nums[cur]) {
      return;
    }
    t.add(nums[cur]);
    dfs(true, cur + 1, nums, t, ans);
    t.remove(t.size() - 1);
  }

  /**
   * #96
   *
   * <br>count of all binary search tree given range [1,n]
   * <pre>
   *  3 -> 5
   *    1         3     3      2      1
   *     \       /     /      / \      \
   *      3     2     1      1   3      2
   *     /     /       \                 \
   *    2     1         2                 3
   * </pre>
   *
   * @param n range upper bound
   * @return count
   */
  @SuppressWarnings("unused")
  public static int numTrees(int n) {
    int[] G = new int[n + 1];
    G[0] = 1;
    G[1] = 1;
    for (int i = 2; i <= n; i++) {
      G[i] = 0;
      for (int j = 1; j <= i; j++) {
        G[i] += G[j - 1] * G[i - j];
      }
    }
    return G[n];
  }
}
