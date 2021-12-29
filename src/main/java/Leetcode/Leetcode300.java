package Leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("JavaDoc")
public class Leetcode300 {

  /**
   * #257
   *
   * @param root
   * @return
   */
  public static List<String> binaryTreePaths(TreeNode root) {
    StringBuilder stringStack = new StringBuilder();
    List<String> ans = new ArrayList<>();
    var recurFunc = new Object() {
      void apply(TreeNode node) {
        var idx = stringStack.length();
        if (node.left == null && node.right == null) {
          if (stringStack.length() == 0) stringStack.append(node.val);
          else stringStack.append("->").append(node.val);
          ans.add(stringStack.toString());
          stringStack.delete(idx, stringStack.length());
        }
        else {
          if (node.left != null) {
            if (stringStack.length() == 0) stringStack.append(node.val);
            else stringStack.append("->").append(node.val);
            apply(node.left);
            stringStack.delete(idx, stringStack.length());
          }
          if (node.right != null) {
            if (stringStack.length() == 0) stringStack.append(node.val);
            else stringStack.append("->").append(node.val);
            apply(node.right);
            stringStack.delete(idx, stringStack.length());
          }
        }
      }

    };
    recurFunc.apply(root);
    return ans;
  }

  /**
   * #274
   *
   * @param citations
   * @return
   */
  public static int hIndex(int[] citations) {
    Arrays.sort(citations);
    int n = citations.length;
    int s = 0, e = citations.length + 1;
    while (e - s > 1) {
      int mid = (s + e) / 2;
      if(mid == 0){
        if(citations[citations.length - 1] <= mid) s = mid;
        else e = mid;
      }
      else{
        if(citations[n - mid] >= mid) s = mid;
        else e = mid;
      }
    }
    return s;

  }
}
