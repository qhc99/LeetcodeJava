package Leetcode;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("JavaDoc")
public class Leetcode350 {

  /**
   * #328
   * @param head
   * @return
   */
  public static ListNode oddEvenList(ListNode head) {
    if(head == null){
      return null;
    }
    ListNode oddTail = head, evenHead, evenTail;
    if(head.next == null){
      return head;
    }
    ListNode ptr = head.next;
    evenHead = ptr;
    evenTail = ptr;
    ptr = ptr.next;
    for(int i = 3; ptr != null; i++, ptr = ptr.next){
      if(i % 2 == 1){
        oddTail.next = ptr;
        oddTail = ptr;
      }
      else {
        evenTail.next = ptr;
        evenTail = ptr;
      }
    }
    oddTail.next = evenHead;
    evenTail.next = null;
    return head;
  }

  /**
   * #312
   * @param nums
   * @return
   */
  public static int maxCoins(int[] nums) {
    return 0;
  }


  Map<TreeNode, Integer> select_max = new HashMap<>();
  Map<TreeNode, Integer> non_select_max = new HashMap<>();
  /**
   * #337
   * @param root
   * @return
   */
  public int rob(TreeNode root) {
    if(root == null){
      return 0;
    }
    robSolve(root);
    return Math.max(select_max.get(root), non_select_max.get(root));
  }

  private void robSolve(TreeNode n){
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
