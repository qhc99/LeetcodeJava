package Leetcode;

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
}
