package Leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("JavaDoc")
public class Leetcode250 {

  /**
   * #208
   */
  public static class Trie {
    static class Node {
      char ctr;
      Node left;
      Node right;
      Node mid;
      boolean contain;

      Node(char c) {
        ctr = c;
      }
    }

    Node head = null;

    public Trie() {

    }

    public void insert(String word) {
      head = put(head, word, 0);
    }

    private Node put(Node ptr, String s, int idx) {
      var current_ctr = s.charAt(idx);
      if (ptr == null) {
        ptr = new Node(current_ctr);
      }

      if (current_ctr < ptr.ctr) {
        ptr.left = put(ptr.left, s, idx);
      }
      else if (current_ctr > ptr.ctr) {
        ptr.right = put(ptr.right, s, idx);
      }
      else if (idx < s.length() - 1) {
        ptr.mid = put(ptr.mid, s, idx + 1);
      }
      else {
        ptr.contain = true;
      }
      return ptr;
    }

    public boolean search(String word) {
      return search(head, word, 0);
    }

    private boolean search(Node ptr, String s, int idx) {
      if (ptr == null) return false;
      var current_ctr = s.charAt(idx);
      if (ptr.ctr == current_ctr) {
        if (idx == s.length() - 1) return ptr.contain;
        else return search(ptr.mid, s, idx + 1);
      }
      else if (current_ctr < ptr.ctr) return search(ptr.left, s, idx);
      else return search(ptr.right, s, idx);
    }

    public boolean startsWith(String prefix) {
      return startsWith(head, prefix, 0);
    }

    private boolean startsWith(Node ptr, String prefix, int idx) {
      if (ptr == null) return false;
      var current_ctr = prefix.charAt(idx);
      if (ptr.ctr == current_ctr) {
        if (idx == prefix.length() - 1) return ptr.mid != null || ptr.contain;
        else return startsWith(ptr.mid, prefix, idx + 1);
      }
      else if (current_ctr < ptr.ctr) return startsWith(ptr.left, prefix, idx);
      else return startsWith(ptr.right, prefix, idx);
    }
  }

  /**
   * #214
   *
   * @param s
   * @return
   */
  public static String shortestPalindrome(String s) {
    char[] inserted = new char[2 * s.length() + 1];
    int idx_inserted = 0, idx_str = 0;
    char placeHolder = '$';
    while (idx_str < s.length()) {
      inserted[idx_inserted++] = placeHolder;
      inserted[idx_inserted++] = s.charAt(idx_str++);
    }
    inserted[idx_inserted] = placeHolder;

    int[] ArmLen = new int[inserted.length];
    int pos = 0, arm_len = 1;
    int target_pos = 0, target_len = 1;
    while (pos < inserted.length) {
      while ((pos + arm_len) < inserted.length &&
              (pos - arm_len) >= 0 &&
              inserted[pos + arm_len] == inserted[pos - arm_len]) {
        arm_len++;
      }
      ArmLen[pos] = arm_len;
      if(arm_len > target_len && (pos - (arm_len - 1) == 0)){
        target_len = arm_len;
        target_pos = pos;
      }

      int mid_pos = pos, mid_arm_len = arm_len;
      pos++;
      arm_len = 1;
      while (pos <= mid_pos + mid_arm_len - 1){
        int sym_pos = mid_pos - (pos - mid_pos);
        int sym_pos_arm_len = ArmLen[sym_pos];

        int mid_left_bound = mid_pos - (mid_arm_len - 1);
        int sym_left_bound = sym_pos - (sym_pos_arm_len - 1);

        if(sym_left_bound > mid_left_bound){
          ArmLen[pos] = sym_pos_arm_len;
          pos++;
        }
        else if (sym_left_bound < mid_left_bound){
          ArmLen[pos] = sym_pos - mid_left_bound + 1;
          pos++;
        }
        else {
          arm_len = sym_pos_arm_len;
          break;
        }
      }
    }

    int right = (target_pos + target_len - 1);
    int palindrome_len = right / 2;
    StringBuilder ans = new StringBuilder(s.substring(palindrome_len));
    ans = ans.reverse();
    ans.append(s);
    return ans.toString();
  }


  /**
   * #234
   * <br/>回文链表
   *
   * @param head node
   * @return res
   */
  public static boolean isPalindrome(ListNode head) {
    //TODO O(1) memory
    List<Integer> vals = new ArrayList<>();

    // 将链表的值复制到数组中
    ListNode currentNode = head;
    while (currentNode != null) {
      vals.add(currentNode.val);
      currentNode = currentNode.next;
    }

    int front = 0;
    int back = vals.size() - 1;
    while (front < back) {
      if (!vals.get(front).equals(vals.get(back))) {
        return false;
      }
      front++;
      back--;
    }
    return true;
  }

  /**
   * #235
   * <br/>二叉搜索树的最近公共祖先
   *
   * @param root tree
   * @param p    node 1
   * @param q    node 2
   * @return closest ancestor
   */
  @SuppressWarnings("unused")
  public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if (root.val > p.val && root.val > q.val) {
      return lowestCommonAncestor(root.left, p, q);
    }
    else if (root.val < p.val && root.val < q.val) {
      return lowestCommonAncestor(root.right, p, q);
    }
    else {
      return root;
    }
  }
}
