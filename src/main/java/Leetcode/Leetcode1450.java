package Leetcode;

@SuppressWarnings({"JavaDoc"})
public class Leetcode1450 {
    /**
     * #1446
     *
     * @param s
     * @return
     */
    public static int maxPower(String s) {
        int left = 0, right = 1;
        int ans = 1;
        for (; right < s.length(); right++) {
            if (s.charAt(left) != s.charAt(right)) {
                left = right;
            }
            ans = Math.max(right + 1 - left, ans);
        }
        return ans;
    }
}
