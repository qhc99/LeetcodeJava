package Leetcode;

import java.util.*;

public class Leetcode2400 {
    /**
     * #2303
     * 
     * @param brackets
     * @param income
     * @return
     */
    public double calculateTax(int[][] brackets, int income) {
        double tax = 0;
        for (int i = 0; i < brackets.length && income > 0; i++) {
            int amount = brackets[i][0];
            if (i > 0) {
                amount -= brackets[i - 1][0];
            }
            int taxAmount = Math.min(amount, income);
            income -= taxAmount;
            tax += taxAmount * brackets[i][1] / 100.;
        }
        return tax;
    }

    /**
     * #2365
     * 
     * @param tasks
     * @param space
     * @return
     */
    public long taskSchedulerII(int[] tasks, int space) {
        long day = 1;
        Map<Integer, Long> record = new HashMap<>();
        for(var task : tasks){
            var d = record.get(task);
            if(d != null){
                if(day - d <= space){
                    day+= space - day + d + 1;
                }
            }
            record.put(task, day++);
        }
        return --day;
    }

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
