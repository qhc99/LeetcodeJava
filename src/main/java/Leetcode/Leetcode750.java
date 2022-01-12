package Leetcode;

import java.util.PriorityQueue;

@SuppressWarnings("JavaDoc")
public class Leetcode750 {

  /**
   * #701
   * @param root
   * @param val
   * @return
   */
  public static TreeNode insertIntoBST(TreeNode root, int val) {
    if(root == null){
      return new TreeNode(val);
    }
    var recurFunc = new Object(){
      void apply(TreeNode n){
        if(n.val < root.val){
          if(n.left == null){
            n.left = new TreeNode(val);
          }
          else {
            apply(n.left);
          }
        }
        else {
          if(n.right == null){
            n.right = new TreeNode(val);
          }
          else {
            apply(n.right);
          }
        }
      }
    };
    recurFunc.apply(root);
    return root;
  }
  /**
   * #786
   *
   * @param arr
   * @param k
   * @return
   */
  public static int[] kthSmallestPrimeFraction(int[] arr, int k) {
    int n = arr.length;
    PriorityQueue<int[]> pq = new PriorityQueue<>((x, y) -> arr[x[0]] * arr[y[1]] - arr[y[0]] * arr[x[1]]);
    for (int j = 1; j < n; ++j) {
      pq.offer(new int[]{0, j});
    }
    for (int i = 1; i < k; ++i) {
      int[] frac = pq.remove();
      int x = frac[0], y = frac[1];
      if (x + 1 < y) {
        pq.offer(new int[]{x + 1, y});
      }
    }
    return new int[]{arr[pq.peek()[0]], arr[pq.peek()[1]]};
  }
}

