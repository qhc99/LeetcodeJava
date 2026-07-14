package Leetcode;

import java.util.*;

public class Leetcode1150 {
    /**
     * #1143
     * 
     * @param text1
     * @param text2
     * @return
     */
    public int longestCommonSubsequence(String text1, String text2) {
        int[][] dp = new int[2][text2.length() + 1];
        for (int i = 1; i <= text1.length(); i++) {
            for (int j = 1; j <= text2.length(); j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[1][j] = dp[0][j - 1] + 1;
                } else {
                    dp[1][j] = Math.max(dp[0][j - 1], dp[1][j - 1]);
                }
            }
            System.arraycopy(dp[1], 0, dp[0], 0, text2.length() + 1);
        }
        return dp[1][text2.length()];
    }

    /**
     * #1146 SnapshotArray
     */
    class SnapshotArray {
        static class Node {
            int snap_id;
            int val;
        }

        List<List<Node>> list = null;
        int snapId;

        public SnapshotArray(int length) {
            list = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                List<Node> l = new ArrayList<>();
                l.add(new Node());
                list.add(l);
            }
        }

        public void set(int index, int val) {
            var n = list.get(index).getLast();
            if (n.snap_id == snapId) {
                n.val = val;
            } else {
                n = new Node();
                n.snap_id = snapId;
                n.val = val;
                list.get(index).add(n);
            }
        }

        public int snap() {
            return snapId++;
        }

        public int get(int index, int snap_id) {
            var l = list.get(index);
            int i = 0, j = l.size();
            while (j - i > 1) {
                int mid = i + (j - i) / 2;
                var n = l.get(mid);
                if (n.snap_id > snap_id) {
                    j = mid;
                } else if (n.snap_id < snap_id) {
                    i = mid;
                } else {
                    return l.get(mid).val;
                }
            }
            return l.get(i).val;
        }
    }

}
