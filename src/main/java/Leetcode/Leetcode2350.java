package Leetcode;

import java.util.*;

public class Leetcode2350 {
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
     * #2050
     * 
     * @param n
     * @param relations
     * @param time
     * @return
     */
    public int minimumTime(int n, int[][] relations, int[] time) {
        int[] totalTime = new int[n + 1];
        int[] depCount = new int[n + 1];
        List<Integer> toVisit = new ArrayList<>();
        Map<Integer, List<Integer>> dep = new HashMap<>();
        Map<Integer, List<Integer>> unblock = new HashMap<>();
        for (var r : relations) {
            dep.computeIfAbsent(r[1], k -> new ArrayList<>()).add(r[0]);
            depCount[r[1]]++;
            unblock.computeIfAbsent(r[0], k -> new ArrayList<>()).add(r[1]);
        }

        for (int i = 1; i <= n; i++) {
            if (depCount[i] == 0)
                toVisit.add(i);
        }

        while (!toVisit.isEmpty()) {
            List<Integer> next = new ArrayList<>();
            for (var i : toVisit) {
                for (var ub : unblock.getOrDefault(i, List.of())) {
                    depCount[ub]--;
                    if (depCount[ub] == 0) {
                        next.add(ub);
                    }
                }
                totalTime[i] = time[i - 1] + dep.getOrDefault(i, List.of())
                        .stream().map(idx -> totalTime[idx])
                        .max((a, b) -> a - b).orElse(0);
            }
            toVisit = next;
        }

        return Arrays.stream(totalTime).max().getAsInt();
    }
}
