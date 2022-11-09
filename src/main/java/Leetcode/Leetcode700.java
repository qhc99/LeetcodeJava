package Leetcode;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
public class Leetcode700 {

    /**
     * #652
     *
     * @param root
     * @return
     */
    public static List<TreeNode> findDuplicateSubtrees(TreeNode root) {
        Map<String, TreeNode> map = new HashMap<>(32);
        scanTree(root,map);
        return map.values().stream().filter(Objects::nonNull).toList();
    }

    private static String scanTree(TreeNode n, Map<String, TreeNode> str2node) {
        if (n == null) {
            return "null";
        }

        var l = scanTree(n.left, str2node);
        var r = scanTree(n.right, str2node);
        var s = "(" +
                l +
                "," +
                n.val +
                "," +
                r +
                ")";
        if (!str2node.containsKey(s)) str2node.put(s, null);
        else str2node.put(s, n);
        return s;
    }


    /**
     * #674
     * 回文子串个数
     * 输入："aaa"
     * 输出：6
     * 解释：6个回文子串: "a", "a", "a", "aa", "aa", "aaa"
     *
     * @param s String
     * @return count of sub string
     */
    public static int countSubstrings(String s) {
        DPMatrix<Boolean> store = new DPMatrix<>(s.length());
        int res = s.length();
        // two
        for (int i = 0; i + 2 - 1 < s.length(); i++) {
            if (s.charAt(i) == s.charAt(i + 1)) {
                store.setAt(true, i, i);
                res++;
            }
        }

        //three
        for (int i = 0; i + 3 - 1 < s.length(); i++) {
            if (s.charAt(i) == s.charAt(i + 2)) {
                store.setAt(true, i, i + 1);
                res++;
            }
        }

        // other
        for (int l = 4; l <= s.length(); l++) {
            for (int i = 0; i + l - 1 < s.length(); i++) {
                if (store.getAt(i + 1, i + l - 3)) {
                    if (s.charAt(i) == s.charAt(i + l - 1)) {
                        store.setAt(true, i, i + l - 2);
                        res++;
                    }
                }
            }
        }

        return res;
    }
}
