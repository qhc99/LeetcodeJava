package Leetcode;

import java.util.HashMap;
import java.util.Map;

public class Leetcode350 {

  /**
   * #337
   * @param root
   * @return
   */

  Map<TreeNode, Integer> select_max = new HashMap<>();
  Map<TreeNode, Integer> non_select_max = new HashMap<>();
  public int rob(TreeNode root) {
    if(root == null){
      return 0;
    }
    robSolve(root);
    return Math.max(select_max.get(root), non_select_max.get(root));
  }

  public void robSolve(TreeNode n){
    if(n == null){
      return;
    }

    robSolve(n.left);
    robSolve(n.right);

    var l_s = select_max.getOrDefault(n.left, 0);
    var l_n = non_select_max.getOrDefault(n.left, 0);
    var r_s = select_max.getOrDefault(n.right, 0);
    var r_n =  non_select_max.getOrDefault(n.right,0);
    select_max.put(n, n.val + l_n + r_n);
    non_select_max.put(n, Math.max(l_n, l_s) + Math.max(r_n,r_s));
  }


}
