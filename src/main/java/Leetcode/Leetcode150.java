package Leetcode;

import java.util.*;

public class Leetcode150 {
  /**
   * #105
   *
   * <br>前序遍历 preorder = [3,9,20,15,7]
   * <br>中序遍历 inorder = [9,3,15,20,7]
   * <br>result:
   * <pre>
   *    3
   *   / \
   *  9  20
   *    /  \
   *   15   7
   * </pre>
   *
   * @param preorder preorder int array
   * @param inorder  inorder int array
   * @return origin tree
   */
  @SuppressWarnings("unused")
  public static TreeNode buildTree(int[] preorder, int[] inorder) {
    if (preorder.length == 0) {
      return null;
    }
    Map<Integer, Integer> m = new HashMap<>();
    for (int i = 0; i < inorder.length; i++) {
      m.put(inorder[i], i);
    }
    return recursiveBuildTree(preorder, 0, preorder.length, 0, m);
  }

  private static TreeNode recursiveBuildTree(int[] p_order, int p_start, int p_end, int i_start, Map<Integer, Integer> m) {
    if (p_end - p_start == 1) {
      return new TreeNode(p_order[p_start]);
    }
    else if (p_end - p_start == 0) {
      return null;
    }
    else {
      TreeNode root = new TreeNode(p_order[p_start]);
      int i_mid = m.get(root.val);
      int l_len = i_mid - i_start;
      root.left = recursiveBuildTree(p_order, p_start + 1, p_start + 1 + l_len, i_start, m);
      root.right = recursiveBuildTree(p_order, p_start + 1 + l_len, p_end, i_start + l_len + 1, m);
      return root;
    }
  }


  /**
   * #107
   * <br>层次遍历<br>
   * <pre>
   *  input:[3,9,20,null,null,15,7]
   *     3
   *    / \
   *   9  20
   *     /  \
   *    15   7
   * </pre>
   * <pre>
   * return:
   * [
   *   [15,7],
   *   [9,20],
   *   [3]
   * ]
   * </pre>
   *
   * @param root root
   * @return down to up iteration
   */
  @SuppressWarnings("unused")
  public List<List<Integer>> levelOrderBottom(TreeNode root) {
    List<List<Integer>> res = new ArrayList<>();
    Queue<TreeNode> queue = new LinkedList<>();
    queue.add(root);
    while (!queue.isEmpty()) {
      int size = queue.size();
      List<Integer> t = new ArrayList<>();
      for (int i = 0; i < size; i++) {
        var n = queue.poll();
        if (n != null) {
          t.add(n.val);
          if (n.left != null) {
            queue.add(n.left);
          }
          if (n.right != null) {
            queue.add(n.right);
          }
        }
      }
      if (t.size() != 0) {
        res.add(t);
      }
    }
    Collections.reverse(res);
    return res;
  }


  /**
   * #111
   * <br/>给定一个二叉树，找出其最小深度。
   *
   * @param root 二叉树
   * @return 最小深度
   */
  @SuppressWarnings("unused")
  public static int minDepth(TreeNode root) {
    if (root == null) {
      return 0;
    }
    int left = minDepth(root.left);
    int right = minDepth(root.right);
    int depth = 1;
    if (left == 0) {
      depth += right;
    }
    else {
      if (right == 0) {
        depth += left;
      }
      else {
        depth += Math.min(left, right);
      }
    }

    return depth;
  }

  /**
   * #128
   * @param nums
   * @return
   */
  public static int longestConsecutive(int[] nums) {
    Set<Integer> sets = new HashSet<>(nums.length * 2);
    for(var i : nums){
      sets.add(i);
    }
    int ans = 0;
    for(var n : nums){
      if(!sets.contains(n-1)){
        n++;
        int count = 1;
        while (sets.contains(n)){
          count++;
          n++;
        }
        ans = Math.max(ans, count);
      }
    }
    return ans;
  }

  /**
   * #129
   * <br/>
   * <pre>
   *  输入: [1,2,3]
   *     1
   *    / \
   *   2   3
   * 输出: 25
   * 解释:
   * 从根到叶子节点路径 1->2 代表数字 12.
   * 从根到叶子节点路径 1->3 代表数字 13.
   * 因此，数字总和 = 12 + 13 = 25.
   * </pre>
   *
   * @param root tree
   * @return sum
   */
  @SuppressWarnings("unused")
  public static int sumNumbers(TreeNode root) {
    if (root == null) {
      return 0;
    }
    List<Integer> res = new ArrayList<>();
    res.add(0);
    solveSumNumbers(root, 0, res);
    return res.get(0);
  }

  private static void solveSumNumbers(TreeNode node, int num, List<Integer> sum) {
    num = num * 10 + node.val;
    if (node.left == null && node.right == null) {
      sum.set(0, sum.get(0) + num);
    }
    else if (node.right != null && node.left == null) {
      solveSumNumbers(node.right, num, sum);
    }
    else if (node.right == null) {
      solveSumNumbers(node.left, num, sum);
    }
    else {
      solveSumNumbers(node.right, num, sum);
      solveSumNumbers(node.left, num, sum);
    }
  }

  /**
   * #133
   * @param node
   * @return
   */
  public Node cloneGraph(Node node) {
    if(node == null){
      return null;
    }

    Node graph = getMapped(node);
    Queue<Node> nodeQueue = new ArrayDeque<>(), newNodeQueue = new ArrayDeque<>();
    nodeQueue.add(node);
    newNodeQueue.add(graph);
    Set<Node> visited = new HashSet<>();
    visited.add(node);
    while (nodeQueue.size() > 0 && newNodeQueue.size() > 0){
      var n = nodeQueue.poll();
      visited.add(n);
      var mappedNode = newNodeQueue.poll();
      for(var neighbor : n.neighbors){
        var mappedNeighbor = getMapped(neighbor);
        assert mappedNode != null;
        mappedNode.neighbors.add(mappedNeighbor);
        if(!visited.contains(neighbor)){
          newNodeQueue.add(mappedNeighbor);
          nodeQueue.add(neighbor);
          visited.add(neighbor);
        }
      }
    }
    return graph;
  }

  Map<Node, Node> map = new HashMap<>();
  public Node getMapped(Node n){
    var ans  = map.get(n);
    if(ans == null){
      var m = new Node(n.val, new ArrayList<>(n.neighbors.size()));
      map.put(n, m);
      return m;
    }
    else return ans;
  }

  /**
   * #144
   * <br/>前序遍历
   *
   * @param root root
   * @return res array
   */
  @SuppressWarnings("unused")
  public static List<Integer> preorderTraversal(TreeNode root) {
    List<Integer> res = new ArrayList<>();
    solvePreorderTraversal(root, res);
    return res;
  }

  private static void solvePreorderTraversal(TreeNode node, List<Integer> res) {
    if (node != null) {
      res.add(node.val);
      if (node.left != null) {
        solvePreorderTraversal(node.left, res);
      }
      if (node.right != null) {
        solvePreorderTraversal(node.right, res);
      }
    }
  }

  /**
   * #198
   *
   * @param nums
   * @return
   */
  public static int rob(int[] nums) {
    int[] dp = new int[3];
    dp[0] = nums[0];
    if(nums.length >= 2){
      dp[1] = Math.max(nums[0], nums[1]);
    }
    for (int i = 2; i < nums.length; i++) {
      dp[i%3] = Math.max(dp[(i-2)%3]+nums[i], dp[(i-1)%3]);
    }
    return dp[(nums.length - 1)%3];
  }
}
