package Leetcode;

public class Leetcode1550 {
    /**
     * #1529
     * 
     * @param target
     * @return
     */
    public static int minFlips(String target) {
        char[] chrs = target.toCharArray();
        char d = chrs[0];
        int groups_count = 0;
        for (int i = 1; i < chrs.length; i++) {
            if (chrs[i] != d) {
                groups_count++;
            }
            d = chrs[i];
        }
        if(chrs[0] == '1'){
            groups_count++;
        }
        return groups_count;
    }
}
