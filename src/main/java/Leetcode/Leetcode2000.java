package Leetcode;

import java.nio.file.Path;
import java.util.*;

import Leetcode.Leetcode2000.PathNode;

public class Leetcode2000 {
    /**
     * #1948
     * 
     * @param paths
     * @return
     */
    public List<List<String>> deleteDuplicateFolder(List<List<String>> paths) {
        var root = new PathNode("");
        Set<PathNode> duplicated = new HashSet<>();
        for (var path : paths) {
            var ptr = root;
            for (var folder : path) {
                ptr = ptr.children.computeIfAbsent(folder,
                        key -> new PathNode(key));
            }
        }
        Map<PathNode, String> idCache = new HashMap<>();
        root.toId(idCache);
        Map<String, List<PathNode>> id2NodeGroup = new HashMap<>();
        for (var e : idCache.entrySet()) {
            id2NodeGroup.computeIfAbsent(e.getValue(), k -> new ArrayList<>())
                    .add(e.getKey());
        }
        for (var group : id2NodeGroup.values()) {
            if (group.size() < 2)
                continue;
            for (int i = 0; i < group.size(); i++) {
                if (!group.get(i).children.isEmpty())
                    duplicated.add(group.get(i));
            }
        }
        List<List<String>> res = new ArrayList<>();
        visitPaths(root, res, new ArrayList<>(), duplicated);
        return res;
    }

    void visitPaths(PathNode ptr, List<List<String>> res, List<String> current,
            Set<PathNode> duplicated) {
        if (duplicated.contains(ptr))
            return;
        boolean added = false;
        if (ptr.name.length() > 0) {
            current.add(ptr.name);
            added = true;
        }
        if (current.size() > 0)
            res.add(new ArrayList<>(current));
        for (var c : ptr.children.values()) {
            visitPaths(c, res, current, duplicated);
        }
        if (added)
            current.removeLast();
    }

    static class PathNode {
        String name;
        Map<String, PathNode> children = new HashMap<>();

        PathNode(String n) {
            name = n;
        }

        String toId(Map<PathNode, String> idCache) {
            var id = idCache.get(this);
            if (id == null) {
                id = children.values().stream()
                        .map(n -> n.name + n.toId(idCache)).sorted().toList()
                        .toString();
                idCache.put(this, id);
            }
            return id;
        }
    }
}
