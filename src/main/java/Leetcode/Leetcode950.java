package Leetcode;

@SuppressWarnings({"JavaDoc", "unused"})
public class Leetcode950 {

    /**
     * #902
     *
     * @param digits
     * @param n
     * @return
     */
    public static int atMostNGivenDigitSet(String[] digits, int n) {
        var S = String.valueOf(n);
        int len_n = S.length();
        int[] dp = new int[len_n + 1]; // preprocess
        dp[len_n] = 1;
        for (int idx = len_n - 1; idx >= 0; idx--) {
            var S_digit = Integer.parseInt(String.valueOf(S.charAt(idx)));
            for (var num_s : digits) {
                var num = Integer.parseInt(num_s);
                if (num < S_digit) dp[idx] += Math.pow(digits.length, len_n - 1 - idx);
                else if (num == S_digit) dp[idx] += dp[idx + 1];
            }
        }
        for (int i = 1; i < len_n; i++) {
            dp[0] += Math.pow(digits.length, i);
        }
        return dp[0];
    }

    /**
     * @param digits
     * @param n      start from 1, radix 10
     * @return
     */
    public static int nthNum(String[] digits, int n) {
        n--;
        if (n == 0) {
            return Integer.parseInt(digits[0]);
        }
        StringBuilder num = new StringBuilder();
        while (n != 0) {
            int idx = n % digits.length;
            num.insert(0, digits[idx]);
            n -= idx;
            n = n / digits.length;
        }
        return Integer.parseInt(num.toString());
    }

    /**
     * #925
     *
     * @param name  name string
     * @param typed input string
     * @return is long press
     */
    public static boolean isLongPressedName(String name, String typed) {
        if (name.charAt(0) != typed.charAt(0)) {
            return false;
        }
        char lastChar = name.charAt(0);
        int namePtr = 1, typedPtr = 1;
        for (; typedPtr < typed.length(); typedPtr++) {
            if (namePtr < name.length()) {
                if (typed.charAt(typedPtr) == name.charAt(namePtr)) {
                    lastChar = name.charAt(namePtr);
                    namePtr++;
                }
                else {
                    if (typed.charAt(typedPtr) != lastChar) {
                        return false;
                    }
                }
            }
            else {
                if (typed.charAt(typedPtr) != lastChar) {
                    return false;
                }
            }
        }
        return namePtr == name.length();
    }
}
