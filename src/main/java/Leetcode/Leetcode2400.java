package Leetcode;

public class Leetcode2400 {
    /**
     * #2373
     * 
     * @param grid
     * @return
     */
    public int[][] largestLocal(int[][] grid) {
        int n = grid.length;
        int[][] res = new int[n - 2][n - 2];
        for (int i = 1; i < n - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        var x = i + dx;
                        var y = j + dy;
                        if (x >= 0 && x < n && y >= 0 && y < n) {
                            res[i - 1][j - 1] = Math.max(res[i - 1][j - 1],
                                    grid[x][y]);
                        }
                    }
                }
            }
        }
        return res;
    }
}
