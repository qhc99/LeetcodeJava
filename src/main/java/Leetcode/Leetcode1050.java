package Leetcode;

import java.util.*;


@SuppressWarnings("JavaDoc")
public class Leetcode1050 {

  /**
   * #1006
   * <br/>笨阶乘
   * <pre>
   * clumsy(10) = 10 * 9 / 8 + 7 - 6 * 5 / 4 + 3 - 2 * 1
   * </pre>
   *
   * @param N number
   * @return result
   */
  public static int clumsy(int N) {
    Deque<Integer> stack = new LinkedList<>();
    stack.addLast(N);
    N--;
    int flag = 0;
    while (N >= 1) {
      int reminder = flag % 4;
      switch (reminder) {
        case 0 -> {
          stack.addLast(stack.removeLast() * N);
          flag++;
        }
        case 1 -> {
          stack.addLast(stack.removeLast() / N);
          flag++;
        }
        case 2 -> {
          stack.addLast(N);
          flag++;
        }
        case 3 -> {
          stack.addLast(-N);
          flag = 0;
        }
      }
      N--;
    }
    return stack.parallelStream().mapToInt(Integer::intValue).sum();
  }

  /**
   * #1010
   *
   * @param time
   * @return
   */
  public static int numPairsDivisibleBy60(int[] time) {
    Map<Integer, Integer> remCount = new HashMap<>();
    for (int t : time) {
      int r = t % 60;
      remCount.put(r, remCount.getOrDefault(r, 0) + 1);
    }

    int ans = 0;
    for (int i = 1; i < 30; i++) {
      if (remCount.containsKey(i) && remCount.containsKey(60 - i)) {
        ans += remCount.get(i) * remCount.get(60 - i);
      }
    }
    if(remCount.containsKey(30)){
      int c = remCount.get(30);
      ans += c * (c - 1) / 2;
    }

    if(remCount.containsKey(0)){
      int c = remCount.get(0);
      ans += c * (c - 1) / 2;
    }

    return ans;
  }

  /**
   * #1015
   *
   * @param k
   * @return
   */
  public static int smallestRepunitDivByK(int k) {
    if (k % 2 == 0 || k % 5 == 0) {
      return -1;
    }
    int temp = 1;
    int len = 1;
    while (temp % k != 0) {
      temp = temp % k;
      temp = temp * 10 + 1;
      len += 1;
    }
    return len;
  }

  /**
   * #1022
   * @param root
   * @return
   */
  public static int sumRootToLeaf(TreeNode root) {
    int[] sum = new int[1];
    StringBuilder str = new StringBuilder();
    sumRootToLeafRecursiveSolve(root,str,sum);
    return sum[0];
  }

  private static void sumRootToLeafRecursiveSolve(TreeNode n, StringBuilder str, int[] sum){
    int len = str.length();
    str.append(n.val);
    if(n.left == null && n.right == null){
      sum[0] += Integer.parseInt(str.toString(),2);
    }
    else if(n.left == null){
      sumRootToLeafRecursiveSolve(n.right, str,sum);
    }
    else if(n.right == null){
      sumRootToLeafRecursiveSolve(n.left,str,sum);
    }
    else {
      sumRootToLeafRecursiveSolve(n.left,str,sum);
      sumRootToLeafRecursiveSolve(n.right,str,sum);
    }
    str.delete(len, str.length());
  }

  /**
   * #1026
   *
   * @param root
   * @return
   */
  public static int maxAncestorDiff(TreeNode root) {
    int[] ans = new int[1];
    ans[0] = Integer.MIN_VALUE;

    maxAncestorDiffApply(root, Integer.MIN_VALUE, Integer.MAX_VALUE,ans);
    return ans[0];
  }

  private static void maxAncestorDiffApply(TreeNode n, int max_ancestor, int min_ancestor,int[] ans) {
    max_ancestor = Math.max(n.val, max_ancestor);
    min_ancestor = Math.min(n.val, min_ancestor);
    if (n.left != null) {
      ans[0] = Math.max(ans[0], Math.abs(max_ancestor - n.left.val));
      ans[0] = Math.max(ans[0], Math.abs(min_ancestor - n.left.val));
      maxAncestorDiffApply(n.left, max_ancestor, min_ancestor,ans);
    }
    if (n.right != null) {
      ans[0] = Math.max(ans[0], Math.abs(max_ancestor - n.right.val));
      ans[0] = Math.max(ans[0], Math.abs(min_ancestor - n.right.val));
      maxAncestorDiffApply(n.right, max_ancestor, min_ancestor,ans);
    }
  }

  /**
   * #1024
   * <br/>视频剪辑
   * <pre>
   * 输入：clips = [[0,2],[4,6],[8,10],[1,9],[1,5],[5,9]], T = 10
   * 输出：3
   * 解释：
   * 我们选中 [0,2], [8,10], [1,9] 这三个片段。
   * 然后，按下面的方案重制比赛片段：
   * 将 [1,9] 再剪辑为 [1,2] + [2,8] + [8,9] 。
   * 现在我们手上有 [0,2] + [2,8] + [8,10]，而这些涵盖了整场比赛 [0, 10]。
   * </pre>
   *
   * @param clips video clips
   * @param T     video length
   * @return is intact
   */
  public static int videoStitching(int[][] clips, int T) {
    int[] startAndEnd = new int[T];
    for (int[] clip : clips) {
      if (clip[0] < T) {
        startAndEnd[clip[0]] = Math.max(startAndEnd[clip[0]], clip[1]);
      }
    }
    int remotest = 0, preRemotest = 0, count = 0;
    for (int i = 0; i < startAndEnd.length; i++) {
      remotest = Math.max(remotest, startAndEnd[i]);
      if (i == remotest) {
        return -1;
      }
      if (i == preRemotest) {
        count++;
        preRemotest = remotest;
      }
    }
    return count;
  }

  /**
   * #1032
   */
  public static class StreamChecker {

    public static class ModifiedTernaryTries {
      Node root;

      public static class Node {
        private char ctr;
        private boolean contain;
        private Node left, mid, right;

        public char getChar() {
          return ctr;
        }

        public boolean isContain() {
          return contain;
        }

        public Node getLeft() {
          return left;
        }

        public Node getMid() {
          return mid;
        }

        public Node getRight() {
          return right;
        }
      }

      public void put(String key) {
        root = put(root, key, 0);
      }

      private Node put(Node x, String key, int d) {
        char c = key.charAt(d);
        if (x == null) {
          x = new Node();
          x.ctr = c;
        }
        if (c < x.ctr) {
          x.left = put(x.left, key, d);
        }
        else if (c > x.ctr) {
          x.right = put(x.right, key, d);
        }
        else if (d < key.length() - 1) {
          x.mid = put(x.mid, key, d + 1);
        }
        else {
          x.contain = true;
        }
        return x;
      }

      Queue<Node> nodeQueue = new LinkedList<>();

      public boolean query(char letter) {
        boolean ans = false;
        Node s = search(root, letter);
        int count = nodeQueue.size();
        if (s != null) {
          nodeQueue.add(s.mid);
          ans = ans || s.contain;
        }

        while (count > 0) {
          Node ptr = nodeQueue.poll();
          if (ptr != null) {
            Node n = search(ptr, letter);
            if (n != null) {
              ans = ans || n.contain;
              nodeQueue.add(n.mid);
            }
          }
          count--;
        }

        return ans;
      }

      Node search(Node current, char letter) {
        if (current == null) {
          return null;
        }
        if (letter == current.ctr) {
          return current;
        }
        else if (letter < current.ctr) {
          return search(current.left, letter);
        }
        else return search(current.right, letter);
      }
    }


    ModifiedTernaryTries tries = new ModifiedTernaryTries();

    public StreamChecker(String[] words) {
      for (String w : words) {
        tries.put(w);
      }
    }

    public boolean query(char letter) {
      return tries.query(letter);
    }
  }
}
