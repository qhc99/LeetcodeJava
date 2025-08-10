package Leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Trie {
    class Node {
        char c;
        boolean hasString;
        int data;
        HashMap<Character, Node> children = new HashMap<>(26);
    }

    Node root;

    public Trie() {
        root = new Node();
    }

    public void add(String s, int data) {
        if (s.length() == 0) {
            var n = new Node();
            n.data = data;
            root.children.put((char) 0, n);
            return;
        }
        add(root, s, 0, data);
    }

    void add(Node n, String s, int i, int data) {
        var next = n.children.computeIfAbsent(s.charAt(i), (k) -> {
            var t = new Node();
            t.c = k;
            return t;
        });
        if (i == s.length() - 1) {
            next.data = data;
            next.hasString = true;
            return;
        }
        add(next, s, i + 1, data);
    }

    // Return a list of index
    public List<Integer> getMatches(String s) {
        List<Integer> ret = new ArrayList<>();
        var n = root.children.get((char) 0);
        if (n != null) {
            ret.add(n.data);
        }
        if(s.length() == 0){
            getAllChildren(root, ret);
            return ret;
        }
        getMatches(root, s, 0, ret);
        return ret;
    }

    void getMatches(Node n, String s, int i, List<Integer> ret) {
        var next = n.children.get(s.charAt(i));
        if (next != null) {
            if (next.hasString) {
                ret.add(next.data);
            }
            if (i + 1 < s.length()) {
                getMatches(next, s, i + 1, ret);
            }
            else{
                getAllChildren(next, ret);
            }
        }
    }

    void getAllChildren(Node n, List<Integer> ret){
        for(var e : n.children.entrySet()){
            var child = e.getValue();
            if(child.hasString){
                ret.add(child.data);
            }
            getAllChildren(child, ret);
        }
    }
}
