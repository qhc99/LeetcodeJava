package Leetcode;

import java.util.ArrayDeque;
import java.util.Queue;

public class Leetcode2900 {
    /**
     * #2850
     * 
     * @param grid
     * @return
     */
    public int minimumMoves(int[][] grid) {
        int res = 0;
        Queue<int[]> queue = new ArrayDeque<>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j] > 1) {
                    queue.add(new int[] { i, j, i, j });
                }
            }
        }
        int[] dx = new int[] { 0, 0, 1, -1 };
        int[] dy = new int[] { 1, -1, 0, 0 };
        while (!queue.isEmpty()) {
            var arr = queue.poll(); // x,y,sx,sy;
            for (int i = 0; i < 4; i++) {
                var x = arr[0] + dx[i];
                var y = arr[1] + dy[i];
                if (x >= 0 && x < 3 && y >= 0 && y < 3) {
                    if (grid[x][y] == 0 && grid[arr[2]][arr[3]] > 1) {
                        grid[arr[2]][arr[3]]--;
                        res += Math.abs(x - arr[2]) + Math.abs(y - arr[3]);
                    }
                    if (grid[arr[2]][arr[3]] > 1) {
                        queue.add(new int[] { x, y, arr[2], arr[3] });
                    }
                }
            }
        }
        return res;
    }
}
