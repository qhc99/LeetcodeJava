package leetcode;

public class Leetcode51_100 {

    // #64

    //说明：每次只能向下或者向右移动一步。
    // (上, 左)
    //输入:
    //[
    //  [1,3,1],
    //  [1,5,1],
    //  [4,2,1]
    //]
    //输出: 7
    //解释: 因为路径 1→3→1→1→1 的总和最小。
    public static int minPathSum(int[][] grid) {
        int r_len = grid.length;
        int c_len = grid[0].length;
        for(int s = 1; s <= r_len + c_len - 2; s++){
            for(int i = 0; i < r_len && i <= s; i++){
                int j = s - i;
                if(j < 0 || j >= c_len) continue;
                grid[i][j] += minPath(i,j,grid);
            }
        }
        return grid[r_len - 1][c_len - 1];
    }
    private static int minPath(int i, int j, int[][] cache){
        double u = (i - 1 < 0)? Double.POSITIVE_INFINITY : cache[i - 1][j];
        double l = (j - 1 < 0)? Double.POSITIVE_INFINITY : cache[i][j - 1];
        return (int)Math.min(l,u);
    }

    // #96

    // count of all binary search tree given range [1,n]
    //  3 -> 5
    //    1         3     3      2      1
    //     \       /     /      / \      \
    //      3     2     1      1   3      2
    //     /     /       \                 \
    //    2     1         2                 3
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
