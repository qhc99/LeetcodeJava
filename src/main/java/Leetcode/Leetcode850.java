package Leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Leetcode850 {
    /**
     * #802
     * 
     * @param graph
     * @return
     */
    public static List<Integer> eventualSafeNodes(int[][] graph) {
        int n = graph.length;
        boolean[] safe = new boolean[n];
        boolean[] visited = new boolean[n];
        List<Integer> ans = new ArrayList<>();
        dfsSafeNodes(0, graph, safe, visited, ans);
        for(int i = 0; i < n; i++){
            if(!visited[i]){
                dfsSafeNodes(i,graph, safe, visited, ans);
            }
        }
        return ans;
    }

    private static boolean dfsSafeNodes(int node, int[][] graph, boolean[] safe, boolean[] visited, List<Integer> ans) {
        visited[node] = true;
        var neighbors = graph[node];
        boolean is_safe = true;
        for (var n : neighbors) {
            if (!visited[n]) {
                is_safe = is_safe && dfsSafeNodes(n, graph, safe, visited, ans);
            } else {
                is_safe = is_safe && safe[n];
            }
        }
        safe[node] = is_safe;
        if(is_safe){
            ans.add(node);
        }
        return is_safe;
    }
}
