package Leetcode;

import java.util.*;

public class Leetcode200{

    /**
     *
     */
    public static class BSTIterator {
        private final Deque<TreeNode> stack = new LinkedList<>();
        private TreeNode ptr;
        private boolean poppedBefore = false;
        private boolean finish = false;

        public BSTIterator(TreeNode root) {
            ptr = root;
        }

        public int next() {
            if(finish){
                throw new NoSuchElementException("Iterate finish.");
            }

            while (ptr != null) {
                if (ptr.left != null && !poppedBefore) // if popped before, walk to right
                {
                    stack.push(ptr);
                    ptr = ptr.left;
                }
                else {
                    var t = ptr;
                    if (ptr.right != null) {
                        ptr = ptr.right;
                        poppedBefore = false;
                    }
                    else {
                        if (stack.size() != 0) {
                            ptr = stack.pop();
                            poppedBefore = true;
                        }
                        else {
                            ptr = null;
                        }
                    }
                    return t.val;
                }
            }
            finish = true;
            throw new NoSuchElementException("Iterate finish.");
        }

        public boolean hasNext() {
            return !finish && ptr != null;
        }
    }

    /*
    *
    private final class BSTIterator implements Iterator<Tuple<K, V>> {
        private final Deque<Node<K, V>> stack = new LinkedList<>();
        private Node<K, V> ptr;
        private boolean poppedBefore = false;
        private boolean finish = false;

        public BSTIterator() {
            iterating = true;
            ptr = root;
            if (ptr == sentinel) {
                finish = true;
            }
        }

        @Override
        public boolean hasNext() {
            if(!iterating){
                throw new IllegalStateException("concurrent modification");
            }
            return !finish && ptr != null;
        }

        @Override
        public Tuple<K, V> next() {
            while (ptr != null) {
                if (ptr.left != sentinel && !poppedBefore) // if popped before, walk to right
                {
                    stack.push(ptr);
                    ptr = ptr.left;
                }
                else {
                    var t = ptr;
                    if (ptr.right != sentinel) {
                        ptr = ptr.right;
                        poppedBefore = false;
                    }
                    else {
                        if (stack.size() != 0) {
                            ptr = stack.pop();
                            poppedBefore = true;
                        }
                        else {
                            ptr = null;
                        }
                    }
                    return new Tuple<>(t.key, t.value);
                }
            }
            finish = true;
            throw new NoSuchElementException("Iterate finish.");
        }
    }
    * */
}
