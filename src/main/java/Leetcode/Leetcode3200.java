package Leetcode;

public class Leetcode3200 {
    /**
     * #3163
     * 
     * @param word
     * @return
     */
    public String compressedString(String word) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        char chr = ' ';
        for (var c : word.toCharArray()) {
            if (c != chr) {
                if (count > 0) {
                    sb.append(count);
                    sb.append(chr);
                    count = 0;
                }
                chr = c;
            }
            count += 1;
            if (count > 9) {
                sb.append(count);
                sb.append(chr);
                count -= 9;
            }
        }
        if (count > 0) {
            sb.append(count);
            sb.append(chr);
        }
        return sb.toString();
    }
}
