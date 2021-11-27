package Leetcode;

public class Leetcode500 {

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
}
