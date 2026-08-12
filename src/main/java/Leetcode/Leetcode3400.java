package Leetcode;

public class Leetcode3400 {
    /**
     * #3389
     * 
     * @param s
     * @return
     */
    public int makeStringGood(String s) {
        long res = Long.MAX_VALUE;
        int[] count = new int['z' - 'a' + 1];
        for (var c : s.toCharArray())
            count[c - 'a']++;
        for (int len = 0; len <= s.length(); len++) {
            long[][] dp = new long[2][2];

            for (int c = 0; c < 'z' - 'a' + 1; c++) {
                if (count[c] == 0 || count[c] == len) {
                    continue;
                }
                if (count[c] > len) {
                    dp[1][0] = Math.min(dp[0][0], dp[0][1]) + count[c] - len;
                    dp[1][1] = dp[1][0];
                } else {
                    dp[1][0] = Math.min(dp[0][0], dp[0][1]) + count[c];
                    int borrow = 0;
                    if (c - 1 >= 0 && count[c - 1] != len
                            && count[c - 1] != 0) {
                        borrow = (count[c - 1] > len) ? count[c - 1] - len
                                : count[c - 1];
                    }
                    dp[1][1] = Math.min(dp[0][1] + len - count[c],
                            dp[0][0] + len - count[c]
                                    - Math.min(borrow, len - count[c]));
                }
                dp[0][0] = dp[1][0];
                dp[0][1] = dp[1][1];
            }
            res = Math.min(Math.min(dp[0][0], dp[0][1]), res);
        }
        return (int) res;
    }
}