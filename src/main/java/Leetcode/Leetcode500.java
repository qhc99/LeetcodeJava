package Leetcode;

import java.util.*;

@SuppressWarnings("JavaDoc")
public class Leetcode500 {

  /**
   * #452
   * @param points
   * @return
   */
  public static int findMinArrowShots(int[][] points) {
    if(points.length == 1){
      return 1;
    }
    Arrays.sort(points, Comparator.comparingInt(a -> a[1]));
    int count = 1;
    int end = points[0][1];
    for(int i = 1; i < points.length; i++){
      var p = points[i];
      int l = p[0], r = p[1];
      if(!(end >= l && end <= r)){
        count++;
        end = r;
      }
    }

    return count;
  }

  /**
   * #463
   * <pre>
   * 输入:
   * [[0,1,0,0],
   *  [1,1,1,0],
   *  [0,1,0,0],
   *  [1,1,0,0]]
   * 输出: 16
   * 解释: 它的周长是 16 个方格的边：
   * </pre>
   *
   * @param grid lake and island
   * @return perimeter
   */
  @SuppressWarnings("unused")
  public static int islandPerimeter(int[][] grid) {
    int[] dx = {0, 1, 0, -1};
    int[] dy = {1, 0, -1, 0};
    int n = grid.length, m = grid[0].length;
    int ans = 0;
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < m; ++j) {
        if (grid[i][j] == 1) {
          int cnt = 0;
          for (int k = 0; k < 4; ++k) {
            int tx = i + dx[k];
            int ty = j + dy[k];
            if (tx < 0 || tx >= n || ty < 0 || ty >= m || grid[tx][ty] == 0) {
              cnt += 1;
            }
          }
          ans += cnt;
        }
      }
    }
    return ans;
  }

  /**
   * #476
   * @param num
   * @return
   */
  public static int findComplement(int num) {
    if(num == 0) return 1;
    int i = 31;
    while (i >= 1 && kthBinDigit(num,i) == 0){
      i--;
    }
    num = lastKBinDigits(~num,i);

    return num;
  }

  private static int lastKBinDigits(int num, int k){
    return num & ((1 << k) - 1);
  }

  private static int kthBinDigit(int num, int k){
    return num >>> k & 1;
  }

}
