package Leetcode;

public class Leetcode1400 {
    /**
     * #1328
     * 
     * @param palindrome
     * @return
     */
    public String breakPalindrome(String palindrome) {
        if (palindrome.length() == 1)
            return "";
        int i = 0;
        int j = palindrome.length() - 1;
        for (; i < j; i++, j--) {
            if (palindrome.charAt(i) != 'a') {
                break;
            }
        }
        var res = new StringBuilder(palindrome);
        if (i >= j) {
            res.replace(res.length() - 1, res.length(), "b");
        } else {
            res.replace(i, i + 1, "a");
        }
        return res.toString();
    }
}
