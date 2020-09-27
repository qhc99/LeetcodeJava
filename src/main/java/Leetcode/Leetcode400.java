package Leetcode;


public class Leetcode400 {
    /**
     * #392
     * s = "abc", t = "ahbgdc"
     * 返回 true.
     * 示例 2:
     * s = "axc", t = "ahbgdc"
     * 返回 false.
     *
     * @param s sub string
     * @param t whole string
     * @return match result
     */
    @SuppressWarnings("SpellCheckingInspection, unused")
    public static boolean isSubsequence(String s, String t) {
        int a = 0, b = 0;
        int s_len = s.length(), t_len = t.length();
        if (s_len == 0) {
            return true;
        }
        while (b < t_len) {
            if (s.charAt(a) == t.charAt(b)) {
                a++;
                if (a == s_len) {
                    return true;
                }
            }
            b++;
        }
        return false;
    }
}
