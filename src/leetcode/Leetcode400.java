package leetcode;

public class Leetcode400 {
    public static boolean isSubsequence(String s, String t) {
        int a = 0, b = 0;
        int s_len = s.length(), t_len = t.length();
        if(s_len == 0) return true;
        while(b < t_len){
            if(s.charAt(a) == t.charAt(b)){
                a++;
                if(a == s_len) return true;
            }
            b++;
        }
        return false;
    }
}
