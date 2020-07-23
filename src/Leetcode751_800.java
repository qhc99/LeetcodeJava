package src;

import java.util.*;

public class Leetcode751_800 {
    // #785

    // bipartite graph: vertex and its neighbors has different color
    private enum Color {
        GRAY, RED, GREEN
    }
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
