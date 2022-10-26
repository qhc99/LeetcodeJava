package Leetcode;

import java.util.*;


public class Leetcode800 {

  /**
   * #763
   * <br/>划分字母区间
   * <pre>
   * 输入：S = "ababcbacadefegdehijhklij"
   * 输出：[9,7,8]
   * 解释：
   * 划分结果为 "ababcbaca", "defegde", "hijhklij"。
   * 每个字母最多出现在一个片段中。
   * 像 "ababcbacadefegde", "hijhklij" 的划分是错误的，因为划分的片段数较少。
   *
   * 来源：力扣（LeetCode）
   * 链接：https://leetcode-cn.com/problems/partition-labels
   * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
   * </pre>
   *
   * @param S string
   * @return res
   */
  public static List<Integer> partitionLabels(String S) {
    int[] lastIndexOfChar = new int[26];
    for (int i = 0; i < S.length(); i++) {
      lastIndexOfChar[S.charAt(i) - 'a'] = i;
    }
    int startIdx = 0, endIndex = 0;
    List<Integer> res = new ArrayList<>();
    for (int i = 0; i < S.length(); i++) {
      endIndex = Math.max(lastIndexOfChar[S.charAt(i) - 'a'], endIndex);
      if (i == endIndex) {
        res.add(endIndex - startIdx + 1);
        startIdx = endIndex + 1;
      }
    }
    return res;
  }

  /**
   * #781
   * <br/>森林中的兔子
   * <br/>森林中，每个兔子都有颜色。其中一些兔子（可能是全部）告诉你还有多少其他的兔子和自己有相同的颜色。
   * 我们将这些回答放在answers数组里。
   * <br/>要求返回森林中兔子的最少数量。
   *
   * @param answers answers
   * @return count of rabbit
   */
  public static int numRabbits(int[] answers) {
    Map<Integer, Integer> count = new HashMap<>();
    for (int y : answers) {
      count.put(y, count.getOrDefault(y, 0) + 1);
    }
    int ans = 0;
    for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
      int y = entry.getKey(), x = entry.getValue();
      ans += (x + y) / (y + 1) * (y + 1);
    }
    return ans;
  }

  /**
   * #785
   * <br>bipartite graph: vertex and its neighbors has different color
   *
   * @param graph graph
   * @return graph is bipartite
   */
  public static boolean isBipartite(int[][] graph) {
    int vertices_count = graph.length;
    Map<Integer, Color> vertex_color = new HashMap<>();
    for (int i = 0; i < vertices_count; i++) {
      vertex_color.put(i, Color.GRAY);
    }
    Queue<Integer> queue = new LinkedList<>();
    for (int i = 0; i < vertices_count; i++) {
      if (vertex_color.get(i) == Color.GRAY) {
        var is_bipartite = BFS(i, queue, vertex_color, graph);
        if (!is_bipartite) {
          return false;
        }
      }
    }
    return true;
  }

  private enum Color {
    GRAY, RED, GREEN
  }

  private static boolean BFS(Integer start, Queue<Integer> queue, Map<Integer, Color> vertex_color, int[][] graph) {
    queue.add(start);
    vertex_color.put(start, Color.RED);
    while (!queue.isEmpty()) {
      var vertex = queue.remove();
      for (var neighbor : graph[vertex]) {
        if (vertex_color.get(neighbor) == Color.GRAY) {
          queue.add(neighbor);
        }
        if (vertex_color.get(neighbor) == vertex_color.get(vertex)) {
          return false;
        }
        else {
          vertex_color.put(neighbor, (vertex_color.get(vertex) == Color.RED) ? Color.GREEN : Color.RED);
        }
      }
    }
    return true;
  }
}
