package Leetcode;

import java.util.*;

public class Leetcode2700 {
    /**
     * #2673
     * 
     * @param n
     * @param cost
     * @return
     */
    public int minIncrements(int n, int[] cost) {
        return visitInc(1, cost);
    }

    int visitInc(int i, int[] cost) {
        if (i * 2 >= cost.length)
            return 0;
        int res = 0;
        res += visitInc(i * 2, cost);
        res += visitInc(i * 2 + 1, cost);
        var left = cost[i * 2 - 1];
        var right = cost[i * 2];
        var max = Math.max(left, right);
        res += max - right + max - left;
        cost[i - 1] += max;
        return res;
    }
}
