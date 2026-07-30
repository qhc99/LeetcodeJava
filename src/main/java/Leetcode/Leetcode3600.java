package Leetcode;

public class Leetcode3600 {
    /**
     * #3522
     * 
     * @param instructions
     * @param values
     * @return
     */
    public long calculateScore(String[] instructions, int[] values) {
        long res = 0;
        int i = 0;
        while (i >= 0 && i < instructions.length) {
            if (instructions[i] == null)
                break;
            var s = instructions[i];
            instructions[i] = null;
            if (s.equals("add"))
                res += values[i++];
            else
                i += values[i];
        }
        return res;
    }
}
