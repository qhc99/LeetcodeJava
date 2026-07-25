package Leetcode;

import java.util.*;
import java.util.function.*;

public class OrderStatTree<K, V> {

    final Node<K, V> sentinel = new Node<>(RBTreeTemplate.BLACK);
    Node<K, V> root = sentinel;

    private final RBTreeTemplate<K, Node<K, V>> template;

    public OrderStatTree(Comparator<K> comparator) {
        template = new RBTreeTemplate<>(sentinel, comparator, n -> n.key,
                () -> this.root, r -> this.root = r, n -> n.parent,
                (n, p) -> n.parent = p, n -> n.left, (n, l) -> n.left = l,
                n -> n.right, (n, r) -> n.right = r, n -> n.color,
                (n, c) -> n.color = c);
    }

    public K getKeyOfRank(int rank) {
        if (rank <= 0 || rank > size()) {
            throw new IndexOutOfBoundsException();
        }
        Node<K, V> n = getNodeOfRank(rank);
        return n.key;
    }

    Node<K, V> getNodeOfRank(int ith) {
        return getNodeOfRank(root, ith);
    }

    Node<K, V> getNodeOfRank(Node<K, V> current, int ith) {
        int rank = current.left.size + 1;
        if (rank == ith) {
            return current;
        } else if (ith < rank) {
            return getNodeOfRank(current.left, ith);
        } else {
            return getNodeOfRank(current.right, ith - rank);
        }
    }

    public int size() {
        return root.size;
    }

    public void insertKV(K key, V val) {
        var n = new Node<>(key, val);
        template.insert(n);
    }

    static final class Node<key, val> {
        key key;
        val value;
        boolean color;
        Node<key, val> parent;
        Node<key, val> left;
        Node<key, val> right;
        int size;

        Node(boolean color) {
            this.color = color;
        }

        Node(key key, val val) {
            color = RBTreeTemplate.RED;
            this.key = key;
            this.value = val;
        }

        @Override
        public String toString() {
            return "Node{" + "key=" + key + ", value=" + value + ", size="
                    + size + '}';
        }
    }
}

 class RBTreeTemplate<Key, Node> {
    final Node sentinel;
    final Comparator<Key> comparator;
    final Gettable<Node> getRoot;
    final Consumer<Node> setRoot;
    static final boolean RED = false;
    static final boolean BLACK = true;
    final Function<Node, Node> getParent;
    final BiConsumer<Node, Node> setParent;
    final Function<Node, Node> getLeft;
    final BiConsumer<Node, Node> setLeft;
    final Function<Node, Node> getRight;
    final BiConsumer<Node, Node> setRight;
    final Function<Node, Boolean> getColor;
    final BiConsumer<Node, Boolean> setColor;
    final Function<Node, Key> getKey;

    RBTreeTemplate(Node sentinel, Comparator<Key> comparator,
            Function<Node, Key> getKey, Gettable<Node> getRoot,
            Consumer<Node> setRoot, Function<Node, Node> getParent,
            BiConsumer<Node, Node> setParent, Function<Node, Node> getLeft,
            BiConsumer<Node, Node> setLeft, Function<Node, Node> getRight,
            BiConsumer<Node, Node> setRight, Function<Node, Boolean> getColor,
            BiConsumer<Node, Boolean> setColor) {
        this.sentinel = sentinel;
        this.comparator = comparator;
        this.getRoot = getRoot;
        this.setRoot = setRoot;
        this.getParent = getParent;
        this.setParent = setParent;
        this.getRight = getRight;
        this.setRight = setRight;
        this.getLeft = getLeft;
        this.setLeft = setLeft;
        this.getColor = getColor;
        this.setColor = setColor;
        this.getKey = getKey;
    }

    /**
     * insert a node <b>WITHOUT</b> duplicate key
     *
     * @param z node or sentinel
     */
    @SuppressWarnings({ "SuspiciousNameCombination" })
    void insert(Node z) {
        var y = sentinel;
        var x = getRoot.get();
        while (x != sentinel) {
            y = x;
            {//
                if (x instanceof OrderStatTree.Node<?, ?>) {
                    OrderStatTree.Node<?, ?> xo = (OrderStatTree.Node<?, ?>) x;
                    xo.size++;
                }
            }
            if (comparator.compare(getKey.apply(z), getKey.apply(x)) < 0) {
                x = getLeft.apply(x);
            } else if (comparator.compare(getKey.apply(z),
                    getKey.apply(x)) > 0) {
                x = getRight.apply(x);
            } else {
                throw new IllegalArgumentException("duplicate key.");
            }
        }
        setParent.accept(z, y);
        if (y == sentinel) {
            setRoot.accept(z);
        } else if (comparator.compare(getKey.apply(z), getKey.apply(y)) < 0) {
            setLeft.accept(y, z);
        } else if (comparator.compare(getKey.apply(z), getKey.apply(y)) > 0) {
            setRight.accept(y, z);
        } else {
            throw new RuntimeException("impossible error.");
        }
        setLeft.accept(z, sentinel);
        setRight.accept(z, sentinel);
        setColor.accept(z, RED);
        {//
            if (z instanceof OrderStatTree.Node<?, ?>) {
                OrderStatTree.Node<?, ?> zo = (OrderStatTree.Node<?, ?>) z;
                zo.size = 1;
            }
        }
        insertFixUp(z);
    }

    private void insertFixUp(Node z) {
        while (getColor.apply(getParent.apply(z)) == RED) {
            if (getParent.apply(z) == getLeft
                    .apply(getParent.apply(getParent.apply(z)))) {
                var y = getRight.apply(getParent.apply(getParent.apply(z)));
                if (getColor.apply(y) == RED) {
                    setColor.accept(getParent.apply(z), BLACK);
                    setColor.accept(y, BLACK);
                    setColor.accept(getParent.apply(getParent.apply(z)), RED);
                    z = getParent.apply(getParent.apply(z));
                } else {
                    if (z == getRight.apply(getParent.apply(z))) {
                        z = getParent.apply(z);
                        leftRotate(z);
                    }
                    setColor.accept(getParent.apply(z), BLACK);
                    setColor.accept(getParent.apply(getParent.apply(z)), RED);
                    rightRotate(getParent.apply(getParent.apply(z)));
                }
            } else {
                var y = getLeft.apply(getParent.apply(getParent.apply(z)));
                if (getColor.apply(y) == RED) {
                    setColor.accept(getParent.apply(z), BLACK);
                    setColor.accept(y, BLACK);
                    setColor.accept(getParent.apply(getParent.apply(z)), RED);
                    z = getParent.apply(getParent.apply(z));
                } else {
                    if (z == getLeft.apply(getParent.apply(z))) {
                        z = getParent.apply(z);
                        rightRotate(z);
                    }
                    setColor.accept(getParent.apply(z), BLACK);
                    setColor.accept(getParent.apply(getParent.apply(z)), RED);
                    leftRotate(getParent.apply(getParent.apply(z)));
                }
            }
        }
        setColor.accept(getRoot.get(), BLACK);
    }

    void delete(Node z) {
        var y = z;
        var y_origin_color = getColor.apply(y);
        Node x;
        if (getLeft.apply(z) == sentinel) {
            x = getRight.apply(z);
            RBTransplant(z, getRight.apply(z));
        } else if (getRight.apply(z) == sentinel) {
            x = getLeft.apply(z);
            RBTransplant(z, getLeft.apply(z));
        } else {
            y = minimumNodeOf(getRight.apply(z));
            y_origin_color = getColor.apply(y);
            x = getRight.apply(y);
            if (getParent.apply(y) == z) {
                setParent.accept(x, y);
            } else {
                RBTransplant(y, getRight.apply(y));
                setRight.accept(y, getRight.apply(z));
                setParent.accept(getRight.apply(y), y);
            }
            RBTransplant(z, y);
            setLeft.accept(y, getLeft.apply(z));
            setParent.accept(getLeft.apply(y), y);
            setColor.accept(y, getColor.apply(z));
        }

        {//
            if (y instanceof OrderStatTree.Node<?, ?>) {
                if (getRight.apply(y) != sentinel) {
                    y = minimumNodeOf(getRight.apply(y));
                }
                // noinspection PatternVariableCanBeUsed
                var yo = (OrderStatTree.Node<?, ?>) y;
                while (yo != sentinel) {
                    yo.size = yo.right.size + yo.left.size + 1;
                    yo = yo.parent;
                }
            }
        }

        if (y_origin_color == BLACK) {
            deleteFixUp(x);
        }
    }

    private void deleteFixUp(Node x) {
        while (x != getRoot.get() && getColor.apply(x) == BLACK) {
            if (x == getLeft.apply(getParent.apply(x))) {
                var w = getRight.apply(getParent.apply(x));
                if (getColor.apply(w) == RED) {
                    setColor.accept(w, BLACK);
                    setColor.accept(getParent.apply(x), RED);
                    leftRotate(getParent.apply(x));
                    w = getRight.apply(getParent.apply(x));
                }
                if (getColor.apply(getLeft.apply(w)) == BLACK
                        && getColor.apply(getRight.apply(w)) == BLACK) {
                    setColor.accept(w, RED);
                    x = getParent.apply(x);
                } else {
                    if (getColor.apply(getRight.apply(w)) == BLACK) {
                        setColor.accept(getLeft.apply(w), BLACK);
                        setColor.accept(w, RED);
                        rightRotate(w);
                        w = getRight.apply(getParent.apply(x));
                    }
                    setColor.accept(w, getColor.apply(getParent.apply(x)));
                    setColor.accept(getParent.apply(x), BLACK);
                    setColor.accept(getRight.apply(w), BLACK);
                    leftRotate(getParent.apply(x));
                    x = getRoot.get();
                }
            } else {
                var w = getLeft.apply(getParent.apply(x));
                if (getColor.apply(w) == RED) {
                    setColor.accept(w, BLACK);
                    setColor.accept(getParent.apply(x), RED);
                    rightRotate(getParent.apply(x));
                    w = getLeft.apply(getParent.apply(x));
                }
                if (getColor.apply(getLeft.apply(w)) == BLACK
                        && getColor.apply(getRight.apply(w)) == BLACK) {
                    setColor.accept(w, RED);
                    x = getParent.apply(x);
                } else {
                    if (getColor.apply(getLeft.apply(w)) == BLACK) {
                        setColor.accept(getRight.apply(w), BLACK);
                        setColor.accept(w, RED);
                        leftRotate(w);
                        w = getLeft.apply(getParent.apply(x));
                    }
                    setColor.accept(w, getColor.apply(getParent.apply(x)));
                    setColor.accept(getParent.apply(x), BLACK);
                    setColor.accept(getLeft.apply(w), BLACK);
                    rightRotate(getParent.apply(x));
                    x = getRoot.get();
                }
            }
        }
        setColor.accept(x, BLACK);
    }

    private Node minimumNodeOf(Node x) {
        while (getLeft.apply(x) != sentinel) {
            x = getLeft.apply(x);
        }
        return x;
    }

    private void RBTransplant(Node u, Node v) {
        if (getParent.apply(u) == sentinel) {
            setRoot.accept(v);
        } else if (u == getLeft.apply(getParent.apply(u))) {
            setLeft.accept(getParent.apply(u), v);
        } else {
            setRight.accept(getParent.apply(u), v);
        }
        setParent.accept(v, getParent.apply(u));
    }

    private void leftRotate(Node x) {
        var y = getRight.apply(x);

        setRight.accept(x, getLeft.apply(y));
        if (getLeft.apply(y) != sentinel) {
            setParent.accept(getLeft.apply(y), x);
        }

        setParent.accept(y, getParent.apply(x));
        if (getParent.apply(x) == sentinel) {
            setRoot.accept(y);
        } else if (x == getLeft.apply(getParent.apply(x))) {
            setLeft.accept(getParent.apply(x), y);
        } else {
            setRight.accept(getParent.apply(x), y);
        }

        setLeft.accept(y, x);
        setParent.accept(x, y);
        {//
            if (x instanceof OrderStatTree.Node<?, ?>
                    && y instanceof OrderStatTree.Node<?, ?>) {
                OrderStatTree.Node<?, ?> xo = (OrderStatTree.Node<?, ?>) x;
                OrderStatTree.Node<?, ?> yo = (OrderStatTree.Node<?, ?>) y;
                yo.size = xo.size;
                xo.size = xo.left.size + xo.right.size + 1;
            }
        }
    }

    private void rightRotate(Node x) {
        var y = getLeft.apply(x);

        setLeft.accept(x, getRight.apply(y));
        if (getRight.apply(y) != sentinel) {
            setParent.accept(getRight.apply(y), x);
        }

        setParent.accept(y, getParent.apply(x));
        if (getParent.apply(x) == sentinel) {
            setRoot.accept(y);
        } else if (x == getRight.apply(getParent.apply(x))) {
            setRight.accept(getParent.apply(x), y);
        } else {
            setLeft.accept(getParent.apply(x), y);
        }

        setRight.accept(y, x);
        setParent.accept(x, y);
        {//
            if (x instanceof OrderStatTree.Node<?, ?>
                    && y instanceof OrderStatTree.Node<?, ?>) {
                OrderStatTree.Node<?, ?> yo = (OrderStatTree.Node<?, ?>) y;
                OrderStatTree.Node<?, ?> xo = (OrderStatTree.Node<?, ?>) x;
                yo.size = xo.size;
                xo.size = xo.left.size + xo.right.size + 1;
            }
        }
    }
}
    @FunctionalInterface
     interface Gettable<Item> {
        Item get();
    }