package Leetcode;

public class Leetcode2500 {
    /**
     * #2483
     * 
     * @param customers
     * @return
     */
    public int bestClosingTime(String customers) {
        int totalY = 0;
        var arr = customers.toCharArray();
        for (var c : arr) {
            if (c == 'Y')
                totalY++;
        }
        int minPenalty = totalY;
        int res = 0;
        int currentY = 0;
        int currentN = 0;
        for (int i = 0; i <= arr.length; i++) {
            int penalty = currentN + totalY - currentY;
            if (penalty < minPenalty) {
                minPenalty = penalty;
                res = i;
            }
            if (i < arr.length) {
                var c = arr[i];
                if (c == 'Y')
                    currentY++;
                else
                    currentN++;
            }
        }

        return res;
    }
}
