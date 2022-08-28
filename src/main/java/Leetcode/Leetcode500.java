package Leetcode;

import java.util.*;

@SuppressWarnings({"JavaDoc", "unused"})
public class Leetcode500 {

  /**
   * #451
   *
   * @param s
   * @return
   */
  public static String frequencySort(String s) {
    List<Integer> chrs = new ArrayList<>(s.chars().boxed().toList());
    Map<Integer, Integer> freq = new HashMap<>(26);
    for (var c : chrs) {
      freq.put(c, freq.getOrDefault(c, 0) + 1);
    }
    chrs.sort((a, b) -> freq.get(b) * 100 - freq.get(a) * 100 + b - a);
    StringBuilder sb = new StringBuilder();
    for (var c : chrs) {
      sb.append((char) (int) c);
    }
    return sb.toString();
  }

  /**
   * #452
   *
   * @param points
   * @return
   */
  public static int findMinArrowShots(int[][] points) {
    if (points.length == 1) {
      return 1;
    }
    Arrays.sort(points, Comparator.comparingInt(a -> a[1]));
    int count = 1;
    int end = points[0][1];
    for (int i = 1; i < points.length; i++) {
      var p = points[i];
      int l = p[0], r = p[1];
      if (!(end >= l && end <= r)) {
        count++;
        end = r;
      }
    }

    return count;
  }

  /**
   * #459
   *
   * @param s
   * @return
   */
  public static boolean repeatedSubstringPattern(String s) {
    return customKMP(s + s, s);
  }

  private static boolean customKMP(String T, String P) {
    int n = T.length(), m = P.length();
    int[] PI = computePI(P);
    int q = 0;
    for (int i = 0; i < n; i++) {
      while (q > 0 && P.charAt(q) != T.charAt(i)) {
        q = PI[q];
      }
      if (P.charAt(q) == T.charAt(i)) {
        q++;
      }
      if (q == m) {
        int res = i + 1 - m;
        if (res != 0 && res != P.length()) return true;
        q = PI[q];
      }
    }
    return false;
  }

  /**
   * max match length of prefix of P and suffix end with ith(start from 1) character
   *
   * @param P string
   * @return prefix function (length = len(p) + 1)
   */
  private static int[] computePI(String P) {
    int m = P.length();
    int[] pi = new int[m + 1];
    pi[1] = 0;
    for (int q = 1, k = 0; q < m; q++) {
      while (k > 0 && P.charAt(k) != P.charAt(q)) {
        k = pi[k];
      }
      if (P.charAt(k) == P.charAt(q)) {
        k++;
      }
      pi[q + 1] = k;
    }
    return pi;
  }

  public static class LFUCache {

    public static class DLinkMatrix {

      final DMNode head, tail;

      DLinkMatrix() {
        head = new DMNode(-1);
        tail = new DMNode(-1);
        head.right = tail;
        head.left = tail;
        tail.left = head;
        tail.right = head;
      }

      static void insertAfter(DMNode n, DMNode insert) {
        var r = n.right;
        n.right = insert;
        insert.left = n;

        insert.right = r;
        r.left = insert;
      }

      DMNode firstNode() {
        if (head.right != tail) {
          return head.right;
        }
        else throw new RuntimeException();
      }

      boolean isEmpty() {
        return head.right == tail;
      }

      static class DMNode {
        DMNode left;
        DMNode right;
        final int freq;

        DMNode(int f) {
          freq = f;
        }

        DLinkList list = new DLinkList();

        void detach() {
          var l = left;
          var r = right;
          l.right = r;
          r.left = l;
        }
      }

      static class DLinkList {
        final DLNode head, tail;

        DLinkList() {
          head = new DLNode(-1);
          tail = new DLNode(-1);
          head.right = tail;
          head.left = tail;
          tail.left = head;
          tail.right = head;
        }

        boolean isEmpty() {
          return head.right == tail;
        }

        void addNode(DLNode n) {
          var r = head.right;
          head.right = n;
          n.left = head;

          r.left = n;
          n.right = r;
        }

        DLNode lastNode() {
          if (head.right != tail) {
            return tail.left;
          }
          else throw new RuntimeException();
        }

        static class DLNode {
          DLNode left;
          DLNode right;
          int key;

          DLNode(int key) {
            this.key = key;
          }

          void detach() {
            var l = left;
            var r = right;
            l.right = r;
            r.left = l;
          }
        }
      }
    }

    static class Info {
      int val;
      DLinkMatrix.DMNode nodeM;
      DLinkMatrix.DLinkList.DLNode nodeL;

      Info(int val, DLinkMatrix.DMNode nodeM, DLinkMatrix.DLinkList.DLNode nodeL) {
        this.val = val;
        this.nodeL = nodeL;
        this.nodeM = nodeM;
      }
    }

    final Map<Integer, Info> key_info_map;

    final int capacity;

    final DLinkMatrix dLinkMatrix = new DLinkMatrix();

    public LFUCache(int capacity) {
      this.capacity = capacity;
      key_info_map = new HashMap<>(capacity);
    }

    public int get(int key) {
      if (capacity == 0) return -1;
      var info = key_info_map.get(key);
      int ans = -1;
      if (info == null) return ans;
      ans = info.val;

      var mNode = info.nodeM;
      var lNode = info.nodeL;
      var freq = mNode.freq;
      freq++;
      if (mNode.right.freq == freq) {
        lNode.detach();
        if (mNode.list.isEmpty()) mNode.detach();
        mNode.right.list.addNode(lNode);
        info.nodeM = mNode.right;
      }
      else {
        var rMNode = new DLinkMatrix.DMNode(freq);
        DLinkMatrix.insertAfter(mNode, rMNode);
        info.nodeM = rMNode;

        lNode.detach();
        if (mNode.list.isEmpty()) mNode.detach();
        rMNode.list.addNode(lNode);
      }
      return ans;
    }

    public void put(int key, int value) {
      if (capacity == 0) return;
      if (key_info_map.containsKey(key)) {
        get(key);
        key_info_map.get(key).val = value;
      }
      else {
        if (key_info_map.size() >= capacity) {
          // evict
          var n = dLinkMatrix.firstNode().list.lastNode();
          key_info_map.remove(n.key);
          n.detach();
          if (dLinkMatrix.firstNode().list.isEmpty()) dLinkMatrix.firstNode().detach();
        }

        if (dLinkMatrix.isEmpty()) {
          var mNode = new DLinkMatrix.DMNode(1);
          var lNode = new DLinkMatrix.DLinkList.DLNode(key);
          mNode.list.addNode(lNode);
          DLinkMatrix.insertAfter(dLinkMatrix.head, mNode);
          key_info_map.put(key, new Info(value, mNode, lNode));
        }
        else {
          var fMNode = dLinkMatrix.firstNode();
          if (fMNode.freq == 1) {
            var lNode = new DLinkMatrix.DLinkList.DLNode(key);
            fMNode.list.addNode(lNode);
            key_info_map.put(key, new Info(value, fMNode, lNode));
          }
          else {
            var mNode = new DLinkMatrix.DMNode(1);
            DLinkMatrix.insertAfter(dLinkMatrix.head, mNode);
            var lNode = new DLinkMatrix.DLinkList.DLNode(key);
            mNode.list.addNode(lNode);
            key_info_map.put(key, new Info(value, mNode, lNode));
          }
        }
      }
    }
  }


  /**
   * #463
   * <pre>
   * 输入:
   * [[0,1,0,0],
   *  [1,1,1,0],
   *  [0,1,0,0],
   *  [1,1,0,0]]
   * 输出: 16
   * 解释: 它的周长是 16 个方格的边：
   * </pre>
   *
   * @param grid lake and island
   * @return perimeter
   */
  @SuppressWarnings("unused")
  public static int islandPerimeter(int[][] grid) {
    int[] dx = {0, 1, 0, -1};
    int[] dy = {1, 0, -1, 0};
    int n = grid.length, m = grid[0].length;
    int ans = 0;
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < m; ++j) {
        if (grid[i][j] == 1) {
          int cnt = 0;
          for (int k = 0; k < 4; ++k) {
            int tx = i + dx[k];
            int ty = j + dy[k];
            if (tx < 0 || tx >= n || ty < 0 || ty >= m || grid[tx][ty] == 0) {
              cnt += 1;
            }
          }
          ans += cnt;
        }
      }
    }
    return ans;
  }

  /**
   * #476
   *
   * @param num
   * @return
   */
  public static int findComplement(int num) {
    if (num == 0) return 1;
    int i = 31;
    while (i >= 1 && kthBinDigit(num, i) == 0) {
      i--;
    }
    num = lastKBinDigits(~num, i);

    return num;
  }

  private static int lastKBinDigits(int num, int k) {
    return num & ((1 << k) - 1);
  }

  private static int kthBinDigit(int num, int k) {
    return num >>> k & 1;
  }

}
