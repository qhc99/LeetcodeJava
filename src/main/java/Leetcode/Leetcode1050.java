package Leetcode;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;


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
    for (var clip : clips) {
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
        var s = search(root, letter);
        int count = nodeQueue.size();
        if (s != null) {
          nodeQueue.add(s.mid);
          ans = ans || s.contain;
        }

        while (count > 0) {
          var ptr = nodeQueue.poll();
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
      for (var w : words) {
        tries.put(w);
      }
    }

    public boolean query(char letter) {
      return tries.query(letter);
    }
  }
}
