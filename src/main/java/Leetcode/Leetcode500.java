package Leetcode;

import java.util.*;

@SuppressWarnings("ALL")
public class Leetcode500 {

    /**
     * #400
     *
     * @param n
     * @return
     */
    public static int findNthDigit(int n) {
        long len = 9;
        int size = 1;
        while (n - len * size > 0) {
            n -= len * size;
            len *= 10;
            size++;
        }
        return ithDigit((n - 1) % size, (int) (Math.pow(10, size - 1) - 1
                + Math.ceil(((double) n) / size)), size);

    }

    private static int ithDigit(int i, int num, int size) {
        while (i != 0) {
            num -= ((int) (num / Math.pow(10, size - 1)))
                    * Math.pow(10, size - 1);
            size--;
            i--;
        }
        return (int) (num / Math.pow(10, size - 1));
    }

    /**
     * #402
     *
     * @param num
     * @param k
     * @return
     */
    public static String removeKdigits(String num, int k) {

        if (num.length() == 1) {
            if (k == 1)
                return "0";
            else if (k == 0)
                return num;
            else
                throw new RuntimeException();
        }

        char[] chrs = num.toCharArray();
        StringBuilder sb = new StringBuilder(num.length());
        sb.append(chrs[0]);
        for (int i = 1; i < chrs.length; i++) {
            var current = chrs[i];
            while (sb.length() > 0 && k > 0
                    && sb.charAt(sb.length() - 1) > current) {
                sb.deleteCharAt(sb.length() - 1);
                k--;
            }
            sb.append(current);
        }

        while (k > 0) {
            k--;
            sb.deleteCharAt(sb.length() - 1);
        }
        while (sb.length() > 0 && sb.charAt(0) == '0') {
            sb.deleteCharAt(0);
        }
        return sb.length() > 0 ? sb.toString() : "0";
    }

    /**
     * #404 <br>
     * 左叶子之和
     * 
     * <pre>
     *     3
     *    / \
     *   9  20
     *     /  \
     *    15   7
     *
     * 在这个二叉树中，有两个左叶子，分别是 9 和 15，所以返回 24
     * </pre>
     *
     * @param root root of bTree
     * @return sum of left leaves
     */
    public static int sumOfLeftLeaves(TreeNode root) {
        if (root == null) {
            return 0;
        } else {
            if (root.left == null) {
                return sumOfLeftLeaves(root.right);
            } else if (root.left.left == null && root.left.right == null) {
                return root.left.val + sumOfLeftLeaves(root.right);
            } else {
                return sumOfLeftLeaves(root.left) + sumOfLeftLeaves(root.right);
            }
        }
    }

    /**
     * #406
     *
     * @param people
     * @return
     */
    public static int[][] reconstructQueue(int[][] people) {
        Arrays.sort(people, (a, b) -> {
            if (a[0] != b[0]) {
                return b[0] - a[0];
            } else {
                return a[1] - b[1];
            }
        });

        TreeList<int[]> list = new TreeList<>();
        for (var tuple : people) {
            list.add(tuple[1], tuple);
        }

        int[][] ans = new int[people.length][2];
        for (int i = 0; i < people.length; i++) {
            ans[i] = list.get(i);
        }
        return ans;
    }

    /*
     * Licensed to the Apache Software Foundation (ASF) under one or more
     * contributor license agreements. See the NOTICE file distributed with this
     * work for additional information regarding copyright ownership. The ASF
     * licenses this file to You under the Apache License, Version 2.0 (the
     * "License"); you may not use this file except in compliance with the
     * License. You may obtain a copy of the License at
     *
     * http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
     * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
     * License for the specific language governing permissions and limitations
     * under the License.
     */
    interface OrderedIterator<E> extends Iterator<E> {

        /**
         * Checks to see if there is a previous element that can be iterated to.
         *
         * @return <code>true</code> if the iterator has a previous element
         */
        boolean hasPrevious();

        /**
         * Gets the previous element from the container.
         *
         * @return the previous element in the iteration
         * @throws java.util.NoSuchElementException if the iteration is finished
         */
        E previous();

    }

    /**
     * A <code>List</code> implementation that is optimised for fast insertions
     * and removals at any index in the list.
     * <p>
     * This list implementation utilises a tree structure internally to ensure
     * that all insertions and removals are O(log n). This provides much faster
     * performance than both an <code>ArrayList</code> and a
     * <code>LinkedList</code> where elements are inserted and removed
     * repeatedly from anywhere in the list.
     * </p>
     * <p>
     * The following relative performance statistics are indicative of this
     * class:
     * </p>
     * 
     * <pre>
     *              get  add  insert  iterate  remove
     * TreeList       3    5       1       2       1
     * ArrayList      1    1      40       1      40
     * LinkedList  5800    1     350       2     325
     * </pre>
     * <p>
     * <code>ArrayList</code> is a good general purpose list implementation. It
     * is faster than <code>TreeList</code> for most operations except inserting
     * and removing in the middle of the list. <code>ArrayList</code> also uses
     * less memory as <code>TreeList</code> uses one object per entry.
     * </p>
     * <p>
     * <code>LinkedList</code> is rarely a good choice of implementation.
     * <code>TreeList</code> is almost always a good replacement for it,
     * although it does use slightly more memory.
     * </p>
     *
     * @since 3.1
     */
    public static class TreeList<E> extends AbstractList<E> {
        // add; toArray; iterator; insert; get; indexOf; remove
        // TreeList = 1260;7360;3080; 160; 170;3400; 170;
        // ArrayList = 220;1480;1760; 6870; 50;1540; 7200;
        // LinkedList = 270;7360;3350;55860;290720;2910;55200;

        /**
         * The root node in the AVL tree
         */
        private AVLNode<E> root;

        /**
         * The current size of the list
         */
        private int size;

        // -----------------------------------------------------------------------

        /**
         * Constructs a new empty list.
         */
        public TreeList() {
            super();
        }

        /**
         * Constructs a new empty list that copies the specified collection.
         *
         * @param coll the collection to copy
         * @throws NullPointerException if the collection is null
         */
        public TreeList(final Collection<? extends E> coll) {
            super();
            if (!coll.isEmpty()) {
                root = new AVLNode<>(coll);
                size = coll.size();
            }
        }

        // -----------------------------------------------------------------------

        /**
         * Gets the element at the specified index.
         *
         * @param index the index to retrieve
         * @return the element at the specified index
         */
        @Override
        public E get(final int index) {
            checkInterval(index, 0, size() - 1);
            return root.get(index).getValue();
        }

        /**
         * Gets the current size of the list.
         *
         * @return the current size
         */
        @Override
        public int size() {
            return size;
        }

        /**
         * Gets an iterator over the list.
         *
         * @return an iterator over the list
         */
        @Override
        public Iterator<E> iterator() {
            // override to go 75% faster
            return listIterator(0);
        }

        /**
         * Gets a ListIterator over the list.
         *
         * @return the new iterator
         */
        @Override
        public ListIterator<E> listIterator() {
            // override to go 75% faster
            return listIterator(0);
        }

        /**
         * Gets a ListIterator over the list.
         *
         * @param fromIndex the index to start from
         * @return the new iterator
         */
        @Override
        public ListIterator<E> listIterator(final int fromIndex) {
            // override to go 75% faster
            // cannot use EmptyIterator as iterator.add() must work
            checkInterval(fromIndex, 0, size());
            return new TreeListIterator<>(this, fromIndex);
        }

        /**
         * Searches for the index of an object in the list.
         *
         * @param object the object to search
         * @return the index of the object, -1 if not found
         */
        @Override
        public int indexOf(final Object object) {
            // override to go 75% faster
            if (root == null) {
                return -1;
            }
            return root.indexOf(object, root.relativePosition);
        }

        /**
         * Searches for the presence of an object in the list.
         *
         * @param object the object to check
         * @return true if the object is found
         */
        @Override
        public boolean contains(final Object object) {
            return indexOf(object) >= 0;
        }

        /**
         * Converts the list into an array.
         *
         * @return the list as an array
         */
        @Override
        public Object[] toArray() {
            // override to go 20% faster
            final Object[] array = new Object[size()];
            if (root != null) {
                root.toArray(array, root.relativePosition);
            }
            return array;
        }

        // -----------------------------------------------------------------------

        /**
         * Adds a new element to the list.
         *
         * @param index the index to add before
         * @param obj   the element to add
         */
        @Override
        public void add(final int index, final E obj) {
            modCount++;
            checkInterval(index, 0, size());
            if (root == null) {
                root = new AVLNode<>(index, obj, null, null);
            } else {
                root = root.insert(index, obj);
            }
            size++;
        }

        /**
         * Appends all of the elements in the specified collection to the end of
         * this list, in the order that they are returned by the specified
         * collection's Iterator.
         * <p>
         * This method runs in O(n + log m) time, where m is the size of this
         * list and n is the size of {@code c}.
         *
         * @param c the collection to be added to this list
         * @return {@code true} if this list changed as a result of the call
         * @throws NullPointerException {@inheritDoc}
         */
        @Override
        public boolean addAll(final Collection<? extends E> c) {
            if (c.isEmpty()) {
                return false;
            }
            modCount += c.size();
            final AVLNode<E> cTree = new AVLNode<>(c);
            root = root == null ? cTree : root.addAll(cTree, size);
            size += c.size();
            return true;
        }

        /**
         * Sets the element at the specified index.
         *
         * @param index the index to set
         * @param obj   the object to store at the specified index
         * @return the previous object at that index
         * @throws IndexOutOfBoundsException if the index is invalid
         */
        @Override
        public E set(final int index, final E obj) {
            checkInterval(index, 0, size() - 1);
            final AVLNode<E> node = root.get(index);
            final E result = node.value;
            node.setValue(obj);
            return result;
        }

        /**
         * Removes the element at the specified index.
         *
         * @param index the index to remove
         * @return the previous object at that index
         */
        @Override
        public E remove(final int index) {
            modCount++;
            checkInterval(index, 0, size() - 1);
            final E result = get(index);
            root = root.remove(index);
            size--;
            return result;
        }

        /**
         * Clears the list, removing all entries.
         */
        @Override
        public void clear() {
            modCount++;
            root = null;
            size = 0;
        }

        // -----------------------------------------------------------------------

        /**
         * Checks whether the index is valid.
         *
         * @param index      the index to check
         * @param startIndex the first allowed index
         * @param endIndex   the last allowed index
         * @throws IndexOutOfBoundsException if the index is invalid
         */
        private void checkInterval(final int index, final int startIndex,
                final int endIndex) {
            if (index < startIndex || index > endIndex) {
                throw new IndexOutOfBoundsException(
                        "Invalid index:" + index + ", size=" + size());
            }
        }

        // -----------------------------------------------------------------------

        /**
         * Implements an AVLNode which keeps the offset updated.
         * <p>
         * This node contains the real work. TreeList is just there to implement
         * {@link java.util.List}. The nodes don't know the index of the object
         * they are holding. They do know however their position relative to
         * their parent node. This allows to calculate the index of a node while
         * traversing the tree.
         * <p>
         * The Faedelung calculation stores a flag for both the left and right
         * child to indicate if they are a child (false) or a link as in linked
         * list (true).
         */
        static class AVLNode<E> {
            /**
             * The left child node or the predecessor if
             * {@link #leftIsPrevious}.
             */
            private AVLNode<E> left;
            /**
             * Flag indicating that left reference is not a subtree but the
             * predecessor.
             */
            private boolean leftIsPrevious;
            /**
             * The right child node or the successor if {@link #rightIsNext}.
             */
            private AVLNode<E> right;
            /**
             * Flag indicating that right reference is not a subtree but the
             * successor.
             */
            private boolean rightIsNext;
            /**
             * How many levels of left/right are below this one.
             */
            private int height;
            /**
             * The relative position, root holds absolute position.
             */
            private int relativePosition;
            /**
             * The stored element.
             */
            private E value;

            /**
             * Constructs a new node with a relative position.
             *
             * @param relativePosition the relative position of the node
             * @param obj              the value for the node
             * @param rightFollower    the node with the value following this
             *                         one
             * @param leftFollower     the node with the value leading this one
             */
            private AVLNode(final int relativePosition, final E obj,
                    final AVLNode<E> rightFollower,
                    final AVLNode<E> leftFollower) {
                this.relativePosition = relativePosition;
                value = obj;
                rightIsNext = true;
                leftIsPrevious = true;
                right = rightFollower;
                left = leftFollower;
            }

            /**
             * Constructs a new AVL tree from a collection.
             * <p>
             * The collection must be nonempty.
             *
             * @param coll a nonempty collection
             */
            private AVLNode(final Collection<? extends E> coll) {
                this(coll.iterator(), 0, coll.size() - 1, 0, null, null);
            }

            /**
             * Constructs a new AVL tree from a collection.
             * <p>
             * This is a recursive helper for {@link #AVLNode(Collection)}. A
             * call to this method will construct the subtree for elements
             * {@code start} through {@code end} of the collection, assuming the
             * iterator {@code e} already points at element {@code start}.
             *
             * @param iterator                 an iterator over the collection,
             *                                 which should already point to the
             *                                 element at index {@code start}
             *                                 within the collection
             * @param start                    the index of the first element in
             *                                 the collection that should be in
             *                                 this subtree
             * @param end                      the index of the last element in
             *                                 the collection that should be in
             *                                 this subtree
             * @param absolutePositionOfParent absolute position of this node's
             *                                 parent, or 0 if this node is the
             *                                 root
             * @param prev                     the {@code AVLNode} corresponding
             *                                 to element (start - 1) of the
             *                                 collection, or null if start is 0
             * @param next                     the {@code AVLNode} corresponding
             *                                 to element (end + 1) of the
             *                                 collection, or null if end is the
             *                                 last element of the collection
             */
            private AVLNode(final Iterator<? extends E> iterator,
                    final int start, final int end,
                    final int absolutePositionOfParent, final AVLNode<E> prev,
                    final AVLNode<E> next) {
                final int mid = start + (end - start) / 2;
                if (start < mid) {
                    left = new AVLNode<>(iterator, start, mid - 1, mid, prev,
                            this);
                } else {
                    leftIsPrevious = true;
                    left = prev;
                }
                value = iterator.next();
                relativePosition = mid - absolutePositionOfParent;
                if (mid < end) {
                    right = new AVLNode<>(iterator, mid + 1, end, mid, this,
                            next);
                } else {
                    rightIsNext = true;
                    right = next;
                }
                recalcHeight();
            }

            /**
             * Gets the value.
             *
             * @return the value of this node
             */
            E getValue() {
                return value;
            }

            /**
             * Sets the value.
             *
             * @param obj the value to store
             */
            void setValue(final E obj) {
                this.value = obj;
            }

            /**
             * Locate the element with the given index relative to the offset of
             * the parent of this node.
             */
            AVLNode<E> get(final int index) {
                final int indexRelativeToMe = index - relativePosition;

                if (indexRelativeToMe == 0) {
                    return this;
                }

                final AVLNode<E> nextNode = indexRelativeToMe < 0
                        ? getLeftSubTree()
                        : getRightSubTree();
                if (nextNode == null) {
                    return null;
                }
                return nextNode.get(indexRelativeToMe);
            }

            /**
             * Locate the index that contains the specified object.
             */
            int indexOf(final Object object, final int index) {
                if (getLeftSubTree() != null) {
                    final int result = left.indexOf(object,
                            index + left.relativePosition);
                    if (result != -1) {
                        return result;
                    }
                }
                if (value == null ? value == object : value.equals(object)) {
                    return index;
                }
                if (getRightSubTree() != null) {
                    return right.indexOf(object,
                            index + right.relativePosition);
                }
                return -1;
            }

            /**
             * Stores the node and its children into the array specified.
             *
             * @param array the array to be filled
             * @param index the index of this node
             */
            void toArray(final Object[] array, final int index) {
                array[index] = value;
                if (getLeftSubTree() != null) {
                    left.toArray(array, index + left.relativePosition);
                }
                if (getRightSubTree() != null) {
                    right.toArray(array, index + right.relativePosition);
                }
            }

            /**
             * Gets the next node in the list after this one.
             *
             * @return the next node
             */
            AVLNode<E> next() {
                if (rightIsNext || right == null) {
                    return right;
                }
                return right.min();
            }

            /**
             * Gets the node in the list before this one.
             *
             * @return the previous node
             */
            AVLNode<E> previous() {
                if (leftIsPrevious || left == null) {
                    return left;
                }
                return left.max();
            }

            /**
             * Inserts a node at the position index.
             *
             * @param index is the index of the position relative to the
             *              position of the parent node.
             * @param obj   is the object to be stored in the position.
             */
            AVLNode<E> insert(final int index, final E obj) {
                final int indexRelativeToMe = index - relativePosition;

                if (indexRelativeToMe <= 0) {
                    return insertOnLeft(indexRelativeToMe, obj);
                }
                return insertOnRight(indexRelativeToMe, obj);
            }

            private AVLNode<E> insertOnLeft(final int indexRelativeToMe,
                    final E obj) {
                if (getLeftSubTree() == null) {
                    setLeft(new AVLNode<>(-1, obj, this, left), null);
                } else {
                    setLeft(left.insert(indexRelativeToMe, obj), null);
                }

                if (relativePosition >= 0) {
                    relativePosition++;
                }
                final AVLNode<E> ret = balance();
                recalcHeight();
                return ret;
            }

            private AVLNode<E> insertOnRight(final int indexRelativeToMe,
                    final E obj) {
                if (getRightSubTree() == null) {
                    setRight(new AVLNode<>(+1, obj, right, this), null);
                } else {
                    setRight(right.insert(indexRelativeToMe, obj), null);
                }
                if (relativePosition < 0) {
                    relativePosition--;
                }
                final AVLNode<E> ret = balance();
                recalcHeight();
                return ret;
            }

            // -----------------------------------------------------------------------

            /**
             * Gets the left node, returning null if its a faedelung.
             */
            private AVLNode<E> getLeftSubTree() {
                return leftIsPrevious ? null : left;
            }

            /**
             * Gets the right node, returning null if its a faedelung.
             */
            private AVLNode<E> getRightSubTree() {
                return rightIsNext ? null : right;
            }

            /**
             * Gets the rightmost child of this node.
             *
             * @return the rightmost child (greatest index)
             */
            private AVLNode<E> max() {
                return getRightSubTree() == null ? this : right.max();
            }

            /**
             * Gets the leftmost child of this node.
             *
             * @return the leftmost child (smallest index)
             */
            private AVLNode<E> min() {
                return getLeftSubTree() == null ? this : left.min();
            }

            /**
             * Removes the node at a given position.
             *
             * @param index is the index of the element to be removed relative
             *              to the position of the parent node of the current
             *              node.
             */
            AVLNode<E> remove(final int index) {
                final int indexRelativeToMe = index - relativePosition;

                if (indexRelativeToMe == 0) {
                    return removeSelf();
                }
                if (indexRelativeToMe > 0) {
                    setRight(right.remove(indexRelativeToMe), right.right);
                    if (relativePosition < 0) {
                        relativePosition++;
                    }
                } else {
                    setLeft(left.remove(indexRelativeToMe), left.left);
                    if (relativePosition > 0) {
                        relativePosition--;
                    }
                }
                recalcHeight();
                return balance();
            }

            private AVLNode<E> removeMax() {
                if (getRightSubTree() == null) {
                    return removeSelf();
                }
                setRight(right.removeMax(), right.right);
                if (relativePosition < 0) {
                    relativePosition++;
                }
                recalcHeight();
                return balance();
            }

            private AVLNode<E> removeMin() {
                if (getLeftSubTree() == null) {
                    return removeSelf();
                }
                setLeft(left.removeMin(), left.left);
                if (relativePosition > 0) {
                    relativePosition--;
                }
                recalcHeight();
                return balance();
            }

            /**
             * Removes this node from the tree.
             *
             * @return the node that replaces this one in the parent
             */
            private AVLNode<E> removeSelf() {
                if (getRightSubTree() == null && getLeftSubTree() == null) {
                    return null;
                }
                if (getRightSubTree() == null) {
                    if (relativePosition > 0) {
                        left.relativePosition += relativePosition;
                    }
                    left.max().setRight(null, right);
                    return left;
                }
                if (getLeftSubTree() == null) {
                    right.relativePosition += relativePosition
                            - (relativePosition < 0 ? 0 : 1);
                    right.min().setLeft(null, left);
                    return right;
                }

                if (heightRightMinusLeft() > 0) {
                    // more on the right, so delete from the right
                    final AVLNode<E> rightMin = right.min();
                    value = rightMin.value;
                    if (leftIsPrevious) {
                        left = rightMin.left;
                    }
                    right = right.removeMin();
                    if (relativePosition < 0) {
                        relativePosition++;
                    }
                } else {
                    // more on the left or equal, so delete from the left
                    final AVLNode<E> leftMax = left.max();
                    value = leftMax.value;
                    if (rightIsNext) {
                        right = leftMax.right;
                    }
                    final AVLNode<E> leftPrevious = left.left;
                    left = left.removeMax();
                    if (left == null) {
                        // special case where left that was deleted was a double
                        // link
                        // only occurs when height difference is equal
                        left = leftPrevious;
                        leftIsPrevious = true;
                    }
                    if (relativePosition > 0) {
                        relativePosition--;
                    }
                }
                recalcHeight();
                return this;
            }

            // -----------------------------------------------------------------------

            /**
             * Balances according to the AVL algorithm.
             */
            private AVLNode<E> balance() {
                switch (heightRightMinusLeft()) {
                case 1:
                case 0:
                case -1:
                    return this;
                case -2:
                    if (left.heightRightMinusLeft() > 0) {
                        setLeft(left.rotateLeft(), null);
                    }
                    return rotateRight();
                case 2:
                    if (right.heightRightMinusLeft() < 0) {
                        setRight(right.rotateRight(), null);
                    }
                    return rotateLeft();
                default:
                    throw new RuntimeException("tree inconsistent!");
                }
            }

            /**
             * Gets the relative position.
             */
            private int getOffset(final AVLNode<E> node) {
                if (node == null) {
                    return 0;
                }
                return node.relativePosition;
            }

            /**
             * Sets the relative position.
             */
            private int setOffset(final AVLNode<E> node, final int newOffest) {
                if (node == null) {
                    return 0;
                }
                final int oldOffset = getOffset(node);
                node.relativePosition = newOffest;
                return oldOffset;
            }

            /**
             * Sets the height by calculation.
             */
            private void recalcHeight() {
                height = Math.max(
                        getLeftSubTree() == null ? -1 : getLeftSubTree().height,
                        getRightSubTree() == null ? -1
                                : getRightSubTree().height)
                        + 1;
            }

            /**
             * Returns the height of the node or -1 if the node is null.
             */
            private int getHeight(final AVLNode<E> node) {
                return node == null ? -1 : node.height;
            }

            /**
             * Returns the height difference right - left
             */
            private int heightRightMinusLeft() {
                return getHeight(getRightSubTree())
                        - getHeight(getLeftSubTree());
            }

            private AVLNode<E> rotateLeft() {
                final AVLNode<E> newTop = right; // can't be faedelung!
                final AVLNode<E> movedNode = getRightSubTree().getLeftSubTree();

                final int newTopPosition = relativePosition + getOffset(newTop);
                final int myNewPosition = -newTop.relativePosition;
                final int movedPosition = getOffset(newTop)
                        + getOffset(movedNode);

                setRight(movedNode, newTop);
                newTop.setLeft(this, null);

                setOffset(newTop, newTopPosition);
                setOffset(this, myNewPosition);
                setOffset(movedNode, movedPosition);
                return newTop;
            }

            private AVLNode<E> rotateRight() {
                final AVLNode<E> newTop = left; // can't be faedelung
                final AVLNode<E> movedNode = getLeftSubTree().getRightSubTree();

                final int newTopPosition = relativePosition + getOffset(newTop);
                final int myNewPosition = -newTop.relativePosition;
                final int movedPosition = getOffset(newTop)
                        + getOffset(movedNode);

                setLeft(movedNode, newTop);
                newTop.setRight(this, null);

                setOffset(newTop, newTopPosition);
                setOffset(this, myNewPosition);
                setOffset(movedNode, movedPosition);
                return newTop;
            }

            /**
             * Sets the left field to the node, or the previous node if that is
             * null
             *
             * @param node     the new left subtree node
             * @param previous the previous node in the linked list
             */
            private void setLeft(final AVLNode<E> node,
                    final AVLNode<E> previous) {
                leftIsPrevious = node == null;
                left = leftIsPrevious ? previous : node;
                recalcHeight();
            }

            /**
             * Sets the right field to the node, or the next node if that is
             * null
             *
             * @param node the new left subtree node
             * @param next the next node in the linked list
             */
            private void setRight(final AVLNode<E> node,
                    final AVLNode<E> next) {
                rightIsNext = node == null;
                right = rightIsNext ? next : node;
                recalcHeight();
            }

            /**
             * Appends the elements of another tree list to this tree list by
             * efficiently merging the two AVL trees. This operation is
             * destructive to both trees and runs in O(log(m + n)) time.
             *
             * @param otherTree   the root of the AVL tree to merge with this
             *                    one
             * @param currentSize the number of elements in this AVL tree
             * @return the root of the new, merged AVL tree
             */
            private AVLNode<E> addAll(AVLNode<E> otherTree,
                    final int currentSize) {
                final AVLNode<E> maxNode = max();
                final AVLNode<E> otherTreeMin = otherTree.min();

                // We need to efficiently merge the two AVL trees while keeping
                // them
                // balanced (or nearly balanced). To do this, we take the
                // shorter
                // tree and combine it with a similar-height subtree of the
                // taller
                // tree. There are two symmetric cases:
                // * this tree is taller, or
                // * otherTree is taller.
                if (otherTree.height > height) {
                    // CASE 1: The other tree is taller than this one. We will
                    // thus
                    // merge this tree into otherTree.

                    // STEP 1: Remove the maximum element from this tree.
                    final AVLNode<E> leftSubTree = removeMax();

                    // STEP 2: Navigate left from the root of otherTree until we
                    // find a subtree, s, that is no taller than me. (While we
                    // are
                    // navigating left, we store the nodes we encounter in a
                    // stack
                    // so that we can re-balance them in step 4.)
                    final Deque<AVLNode<E>> sAncestors = new ArrayDeque<>();
                    AVLNode<E> s = otherTree;
                    int sAbsolutePosition = s.relativePosition + currentSize;
                    int sParentAbsolutePosition = 0;
                    while (s != null && s.height > getHeight(leftSubTree)) {
                        sParentAbsolutePosition = sAbsolutePosition;
                        sAncestors.push(s);
                        s = s.left;
                        if (s != null) {
                            sAbsolutePosition += s.relativePosition;
                        }
                    }

                    // STEP 3: Replace s with a newly constructed subtree whose
                    // root
                    // is maxNode, whose left subtree is leftSubTree, and whose
                    // right
                    // subtree is s.
                    maxNode.setLeft(leftSubTree, null);
                    maxNode.setRight(s, otherTreeMin);
                    if (leftSubTree != null) {
                        leftSubTree.max().setRight(null, maxNode);
                        leftSubTree.relativePosition -= currentSize - 1;
                    }
                    if (s != null) {
                        s.min().setLeft(null, maxNode);
                        s.relativePosition = sAbsolutePosition - currentSize
                                + 1;
                    }
                    maxNode.relativePosition = currentSize - 1
                            - sParentAbsolutePosition;
                    otherTree.relativePosition += currentSize;

                    // STEP 4: Re-balance the tree and recalculate the heights
                    // of s's ancestors.
                    s = maxNode;
                    while (!sAncestors.isEmpty()) {
                        final AVLNode<E> sAncestor = sAncestors.pop();
                        sAncestor.setLeft(s, null);
                        s = sAncestor.balance();
                    }
                    return s;
                }
                otherTree = otherTree.removeMin();

                final Deque<AVLNode<E>> sAncestors = new ArrayDeque<>();
                AVLNode<E> s = this;
                int sAbsolutePosition = s.relativePosition;
                int sParentAbsolutePosition = 0;
                while (s != null && s.height > getHeight(otherTree)) {
                    sParentAbsolutePosition = sAbsolutePosition;
                    sAncestors.push(s);
                    s = s.right;
                    if (s != null) {
                        sAbsolutePosition += s.relativePosition;
                    }
                }

                otherTreeMin.setRight(otherTree, null);
                otherTreeMin.setLeft(s, maxNode);
                if (otherTree != null) {
                    otherTree.min().setLeft(null, otherTreeMin);
                    otherTree.relativePosition++;
                }
                if (s != null) {
                    s.max().setRight(null, otherTreeMin);
                    s.relativePosition = sAbsolutePosition - currentSize;
                }
                otherTreeMin.relativePosition = currentSize
                        - sParentAbsolutePosition;

                s = otherTreeMin;
                while (!sAncestors.isEmpty()) {
                    final AVLNode<E> sAncestor = sAncestors.pop();
                    sAncestor.setRight(s, null);
                    s = sAncestor.balance();
                }
                return s;
            }

            /**
             * Used for debugging.
             */
            @Override
            public String toString() {
                return "AVLNode(" + relativePosition + ',' + (left != null)
                        + ',' + value + ',' + (getRightSubTree() != null)
                        + ", faedelung " + rightIsNext + " )";
            }
        }

        /**
         * A list iterator over the linked list.
         */
        static class TreeListIterator<E>
                implements ListIterator<E>, OrderedIterator<E> {
            /**
             * The parent list
             */
            private final TreeList<E> parent;
            /**
             * Cache of the next node that will be returned by {@link #next()}.
             */
            private AVLNode<E> next;
            /**
             * The index of the next node to be returned.
             */
            private int nextIndex;
            /**
             * Cache of the last node that was returned by {@link #next()} or
             * {@link #previous()}.
             */
            private AVLNode<E> current;
            /**
             * The index of the last node that was returned.
             */
            private int currentIndex;
            /**
             * The modification count that the list is expected to have. If the
             * list doesn't have this count, then a
             * {@link java.util.ConcurrentModificationException} may be thrown
             * by the operations.
             */
            private int expectedModCount;

            /**
             * Create a ListIterator for a list.
             *
             * @param parent    the parent list
             * @param fromIndex the index to start at
             */
            protected TreeListIterator(final TreeList<E> parent,
                    final int fromIndex) throws IndexOutOfBoundsException {
                super();
                this.parent = parent;
                this.expectedModCount = parent.modCount;
                this.next = parent.root == null ? null
                        : parent.root.get(fromIndex);
                this.nextIndex = fromIndex;
                this.currentIndex = -1;
            }

            /**
             * Checks the modification count of the list is the value that this
             * object expects.
             *
             * @throws ConcurrentModificationException If the list's
             *                                         modification count isn't
             *                                         the value that was
             *                                         expected.
             */
            protected void checkModCount() {
                if (parent.modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public boolean hasNext() {
                return nextIndex < parent.size();
            }

            @Override
            public E next() {
                checkModCount();
                if (!hasNext()) {
                    throw new NoSuchElementException(
                            "No element at index " + nextIndex + ".");
                }
                if (next == null) {
                    next = parent.root.get(nextIndex);
                }
                final E value = next.getValue();
                current = next;
                currentIndex = nextIndex++;
                next = next.next();
                return value;
            }

            @Override
            public boolean hasPrevious() {
                return nextIndex > 0;
            }

            @Override
            public E previous() {
                checkModCount();
                if (!hasPrevious()) {
                    throw new NoSuchElementException(
                            "Already at start of list.");
                }
                if (next == null) {
                    next = parent.root.get(nextIndex - 1);
                } else {
                    next = next.previous();
                }
                final E value = next.getValue();
                current = next;
                currentIndex = --nextIndex;
                return value;
            }

            @Override
            public int nextIndex() {
                return nextIndex;
            }

            @Override
            public int previousIndex() {
                return nextIndex() - 1;
            }

            @Override
            public void remove() {
                checkModCount();
                if (currentIndex == -1) {
                    throw new IllegalStateException();
                }
                parent.remove(currentIndex);
                if (nextIndex != currentIndex) {
                    // remove() following next()
                    nextIndex--;
                }
                // the AVL node referenced by next may have become stale after a
                // remove
                // reset it now: will be retrieved by next call to
                // next()/previous() via nextIndex
                next = null;
                current = null;
                currentIndex = -1;
                expectedModCount++;
            }

            @Override
            public void set(final E obj) {
                checkModCount();
                if (current == null) {
                    throw new IllegalStateException();
                }
                current.setValue(obj);
            }

            @Override
            public void add(final E obj) {
                checkModCount();
                parent.add(nextIndex, obj);
                current = null;
                currentIndex = -1;
                nextIndex++;
                expectedModCount++;
            }
        }

    }

    /**
     * #407
     *
     * @param heightMap
     * @return
     */
    public static int trapRainWater(int[][] heightMap) {

        int m = heightMap.length, n = heightMap[0].length;
        BitSet isBrink = new BitSet(m * n);
        PriorityQueue<PosHeight> queue = new PriorityQueue<>(2 * (m + n),
                Comparator.comparing(PosHeight::height));
        for (int j = 0; j < n; j++) {
            int i = 0;
            if (!isBrink.get(i * n + j))
                queue.add(new PosHeight(i, j, heightMap[i][j]));
            isBrink.set(i * n + j, true);
            i = m - 1;
            if (!isBrink.get(i * n + j))
                queue.add(new PosHeight(i, j, heightMap[i][j]));
            isBrink.set(i * n + j, true);
        }

        for (int i = 0; i < m; i++) {
            int j = 0;
            if (!isBrink.get(i * n + j))
                queue.add(new PosHeight(i, j, heightMap[i][j]));
            isBrink.set(i * n + j, true);
            j = n - 1;
            if (!isBrink.get(i * n + j))
                queue.add(new PosHeight(i, j, heightMap[i][j]));
            isBrink.set(i * n + j, true);
        }
        int ans = 0;
        while (queue.size() > 0) {
            var gap = queue.poll();
            // System.out.printf("poll %s \n",gap.toString());
            int i = gap.i, j = gap.j;
            ans = fill(i, j, i + 1, j, heightMap, queue, isBrink, ans);
            ans = fill(i, j, i - 1, j, heightMap, queue, isBrink, ans);
            ans = fill(i, j, i, j + 1, heightMap, queue, isBrink, ans);
            ans = fill(i, j, i, j - 1, heightMap, queue, isBrink, ans);
        }

        return ans;
    }

    record PosHeight(int i, int j, int height) {
    }

    private static int fill(int bi, int bj, int i, int j, int[][] heightMap,
            PriorityQueue<PosHeight> queue, BitSet isBrink, int ans) {
        int m = heightMap.length, n = heightMap[0].length;
        if (i < m && i >= 0 && j < n && j >= 0 && !isBrink.get(i * n + j)) {
            isBrink.set(i * n + j, true);
            if (heightMap[bi][bj] > heightMap[i][j]) {
                ans += heightMap[bi][bj] - heightMap[i][j];
                heightMap[i][j] = heightMap[bi][bj];
                // System.out.printf("i %s,j %s, min %s\n",i,j,min);
            }
            queue.add(new PosHeight(i, j, heightMap[i][j]));
            // System.out.printf("add i %s, j %s, height
            // %s\n",i,j,heightMap[i][j]);
        }
        return ans;
    }

    /**
     * #409
     *
     * @param s
     * @return
     */
    public static int longestPalindrome(String s) {
        boolean has_odd = false;
        Map<Character, Integer> occur = new HashMap<>(26 * 2);
        var chrs = s.toCharArray();
        for (var c : chrs) {
            var i = occur.get(c);
            var put = i != null ? i + 1 : 1;

            occur.put(c, put);
        }
        var values = occur.values();
        int ans = 0;
        for (var v : values) {
            if (v % 2 == 0) {
                ans += v;
            } else {
                has_odd = true;
                ans += v - 1;
            }
        }

        return has_odd ? ans + 1 : ans;
    }

    /**
     * #410 <br>
     * 输入: <br>
     * nums = [7,2,5,10,8] <br>
     * m = 2 <br>
     * 输出: <br>
     * 18 <br>
     * 解释: <br>
     * 一共有四种方法将nums分割为2个子数组。 <br>
     * 其中最好的方式是将其分为[7,2,5] 和 [10,8]， <br>
     * 因为此时这两个子数组各自的和的最大值为18，在所有情况中最小。
     *
     * @param nums array
     * @param m    number of group
     * @return min group sum
     */
    public static int splitArray(int[] nums, int m) {
        long sum = 0;
        long max = 0;
        for (int num : nums) {
            sum += num;
            max = Math.max(max, num);
        }
        while (max < sum) {
            long mid = (sum - max) / 2 + max;
            if (canSplit(nums, mid, m)) {
                sum = mid;
            } else {
                max = mid + 1;
            }
        }
        return (int) max;
    }

    private static boolean canSplit(int[] nums, long sum, int limit) {
        int split = 1;
        int tSum = 0;
        for (var n : nums) {
            if (tSum + n <= sum) {
                tSum += n;
            } else {
                tSum = n;
                split++;
                if (split > limit) {
                    return false;
                }
            }
        }
        return split <= limit;
    }

    /**
     * #412
     * 
     * @param n
     * @return
     */
    public List<String> fizzBuzz(int n) {
        List<String> res = new ArrayList<>(n);
        for (int i = 1; i <= n; i++) {
            boolean three = i % 3 == 0;
            boolean five = i % 5 == 0;
            if (three && five)
                res.add("FizzBuzz");
            else if (three)
                res.add("Fizz");
            else if (five)
                res.add("Buzz");
            else
                res.add(String.valueOf(i));
        }
        return res;
    }

    /**
     * #415
     *
     * @param num1
     * @param num2
     * @return
     */
    public static String addStrings(String num1, String num2) {
        StringBuilder sb = new StringBuilder();
        if (num1.length() < num2.length()) {
            var t = num1;
            num1 = num2;
            num2 = t;
        }
        int i = num1.length() - 1, j = num2.length() - 1;
        var n1 = num1.toCharArray();
        var n2 = num2.toCharArray();
        int next = 0;
        while (j >= 0) {
            int c1 = n1[i] - '0';
            int c2 = n2[j] - '0';
            int sum = c1 + c2 + next;
            next = 0;
            if (sum > 9) {
                next = 1;
                sum -= 10;
            }
            sb.insert(0, sum);
            i--;
            j--;
        }
        while (i >= 0) {
            int c1 = n1[i] - '0';
            int sum = c1 + next;
            next = 0;
            if (sum > 9) {
                next = 1;
                sum -= 10;
            }
            sb.insert(0, sum);
            i--;
        }
        if (next == 1) {
            sb.insert(0, 1);
        }

        return sb.toString();
    }

    /**
     * #416
     *
     * @param nums
     * @return
     */
    public static boolean canPartition(int[] nums) {
        if (nums.length <= 1) {
            return false;
        }

        var sum = Arrays.stream(nums).sum();
        if (sum % 2 == 1) {
            return false;
        }

        BitSet dp = new BitSet(sum / 2 + 1);
        dp.set(0, true);

        for (int c = 1; c <= nums.length; c++) {
            var num = nums[c - 1];
            for (int j = sum / 2; j - num >= 0; j--) {
                if (dp.get(j - num)) {
                    dp.set(j, true);
                }
            }
        }

        return dp.get(sum / 2);
    }

    /**
     * #417
     *
     * @param heights
     * @return
     */
    public static List<List<Integer>> pacificAtlantic(int[][] heights) {

        int m = heights.length, n = heights[0].length;
        Set<Tuple> set_pacific = new HashSet<>(m * n);
        Set<Tuple> set_atlantic = new HashSet<>(m * n);
        Deque<Tuple> bfs_pacific = new ArrayDeque<>(m * n);
        Deque<Tuple> bfs_atlantic = new ArrayDeque<>(m * n);

        for (int j = 0; j < n; j++) {
            var t = new Tuple(0, j);
            set_pacific.add(t);
            bfs_pacific.addLast(t);
        }
        for (int i = 1; i < m; i++) {
            var t = new Tuple(i, 0);
            set_pacific.add(t);
            bfs_pacific.addLast(t);
        }
        for (int j = 0; j < n; j++) {
            var t = new Tuple(m - 1, j);
            set_atlantic.add(t);
            bfs_atlantic.addLast(t);
        }
        for (int i = 0; i < m - 1; i++) {
            var t = new Tuple(i, n - 1);
            set_atlantic.add(t);
            bfs_atlantic.addLast(t);
        }
        while (bfs_pacific.size() > 0) {
            var t = bfs_pacific.pollFirst();
            bfs(t, bfs_pacific, heights, set_pacific);
        }
        while (bfs_atlantic.size() > 0) {
            var t = bfs_atlantic.pollFirst();
            bfs(t, bfs_atlantic, heights, set_atlantic);
        }
        List<List<Integer>> ans = new ArrayList<>();
        for (var t : set_pacific) {
            if (set_atlantic.contains(t)) {
                ans.add(List.of(t.i, t.j));
            }
        }
        return ans;
    }

    record Tuple(int i, int j) {
    }

    /**
     * #421
     *
     * @param nums
     * @return
     */
    public static int findMaximumXOR(int[] nums) {
        Map<Integer, String> intToString = new HashMap<>(nums.length);
        var tries = new TernaryTries<Integer>();

        for (var n : nums) {
            StringBuilder sb = new StringBuilder();
            for (int i = 30; i >= 0; i--) {
                sb.append(kthBinDigit(n, i));
            }
            var s = sb.toString();
            intToString.put(n, s);
            tries.put(s, n);
        }
        int max = Integer.MIN_VALUE;

        for (var n : nums) {
            var s = intToString.get(n);
            var ptr = tries.root;
            for (int i = 0; i < s.length(); i++) {
                var c = s.charAt(i);
                if (c == ptr.c) {
                    if (c == '0' & ptr.right != null && ptr.right.mid != null) {
                        ptr = ptr.right.mid;

                    } else if (c == '1' & ptr.left != null
                            && ptr.left.mid != null) {
                        ptr = ptr.left.mid;
                    } else {
                        if (!ptr.contain)
                            ptr = ptr.mid;
                    }
                } else {
                    if (!ptr.contain)
                        ptr = ptr.mid;
                }
            }
            max = Math.max(max, n ^ ptr.val);
        }

        return max;
    }

    public static int kthBinDigit(int num, int k) {
        return (num >>> k) & 1;
    }

    static class TernaryTries<Value> {
        private int n;
        Node<Value> root;

        public static class Node<Value> {
            private char c;
            private boolean contain;
            private Node<Value> left, mid, right;
            private Value val;

            public char getChar() {
                return c;
            }

            @Override
            public String toString() {
                return "Node{" + "char=" + c + ", contain=" + contain + ", val="
                        + val + '}';
            }

            public boolean isContain() {
                return contain;
            }

            public Node<Value> getLeft() {
                return left;
            }

            public Node<Value> getMid() {
                return mid;
            }

            public Node<Value> getRight() {
                return right;
            }

            public Value getVal() {
                return val;
            }
        }

        public Node<Value> getRoot() {
            return root;
        }

        public int size() {
            return n;
        }

        public boolean contains(String key) {
            var n = get(root, key, 0);
            return n != null && n.contain;
        }

        public Value get(String key) {
            if (key.length() == 0) {
                throw new IllegalArgumentException("key must have length >= 1");
            }
            var x = get(root, key, 0);
            if (x == null) {
                return null;
            }
            return x.val;
        }

        private Node<Value> get(Node<Value> x, String key, int d) {
            if (x == null) {
                return null;
            }
            if (key.length() == 0) {
                throw new IllegalArgumentException("key must have length >= 1");
            }
            char c = key.charAt(d);
            if (c < x.c) {
                return get(x.left, key, d);
            } else if (c > x.c) {
                return get(x.right, key, d);
            } else if (d < key.length() - 1) {
                return get(x.mid, key, d + 1);
            } else {
                return x;
            }
        }

        private enum Children {
            LEFT, MID, RIGHT
        }

        public boolean remove(String key) {
            if (key.length() == 0) {
                throw new IllegalArgumentException("key must have length >= 1");
            }
            var ret = remove(root, null, null, key, 0);
            if (ret) {
                n--;
            }
            return ret;
        }

        private boolean remove(Node<Value> n, Node<Value> p, Children direct,
                String key, int d) {
            if (n == null) {
                return false;
            }
            char c = key.charAt(d);
            if (c < n.c) {
                var ret = remove(n.left, n, Children.LEFT, key, d);
                removeSelf(n, p, direct);
                return ret;
            } else if (c > n.c) {
                var ret = remove(n.right, n, Children.RIGHT, key, d);
                removeSelf(n, p, direct);
                return ret;
            } else if (d < key.length() - 1) {
                var ret = remove(n.mid, n, Children.MID, key, d + 1);
                removeSelf(n, p, direct);
                return ret;
            } else if (n.contain) {
                n.contain = false;
                removeSelf(n, p, direct);
                return true;
            } else {
                return false;
            }
        }

        private void removeSelf(Node<Value> n, Node<Value> p, Children direct) {
            if (!n.contain && n.left == null && n.mid == null && n.right == null
                    && p != null) {
                switch (direct) {
                case MID -> p.mid = null;
                case LEFT -> p.left = null;
                case RIGHT -> p.right = null;
                }
            }
            if (p == null && !n.contain && n.left == null && n.mid == null
                    && n.right == null) {
                root = null;
            }
        }

        public void put(String key, Value val) {
            if (!contains(key)) {
                n++;
            }
            root = put(root, key, val, 0);
        }

        private Node<Value> put(Node<Value> x, String key, Value val, int d) {
            char c = key.charAt(d);
            if (x == null) {
                x = new Node<>();
                x.c = c;
            }
            if (c < x.c) {
                x.left = put(x.left, key, val, d);
            } else if (c > x.c) {
                x.right = put(x.right, key, val, d);
            } else if (d < key.length() - 1) {
                x.mid = put(x.mid, key, val, d + 1);
            } else {
                x.val = val;
                x.contain = true;
            }
            return x;
        }

        public String longestPrefixOf(String query) {
            if (query.length() == 0) {
                return null;
            }
            int length = 0;
            var x = root;
            int i = 0;
            while (x != null && i < query.length()) {
                char c = query.charAt(i);
                if (c < x.c) {
                    x = x.left;
                } else if (c > x.c) {
                    x = x.right;
                } else {
                    i++;
                    if (x.contain) {
                        length = i;
                    }
                    x = x.mid;
                }
            }
            return query.substring(0, length);
        }

        public Deque<String> keys() {
            Deque<String> queue = new ArrayDeque<>();
            collect(root, new StringBuilder(), queue);
            return queue;
        }

        public Deque<String> keysWithPrefix(String prefix) {
            Deque<String> queue = new ArrayDeque<>();
            Node<Value> x = get(root, prefix, 0);
            if (x == null) {
                return queue;
            }
            if (x.contain) {
                queue.add(prefix);
            }
            collect(x.mid, new StringBuilder(prefix), queue);
            return queue;
        }

        private void collect(Node<Value> x, StringBuilder prefix,
                Queue<String> queue) {
            if (x == null) {
                return;
            }
            collect(x.left, prefix, queue);
            if (x.contain) {
                queue.add(prefix.toString() + x.c);
            }
            collect(x.mid, prefix.append(x.c), queue);
            prefix.deleteCharAt(prefix.length() - 1);
            collect(x.right, prefix, queue);
        }
    }

    private static void bfs(Tuple t, Deque<Tuple> deque, int[][] heights,
            Set<Tuple> set) {
        int i = t.i, j = t.j, m = heights.length, n = heights[0].length;
        if (i - 1 >= 0) {
            var tt = new Tuple(i - 1, j);
            if (!set.contains(tt) && heights[i - 1][j] >= heights[i][j]) {
                deque.addLast(tt);
                set.add(tt);
            }
        }
        if (i + 1 < m) {
            var tt = new Tuple(i + 1, j);
            if (!set.contains(tt) && heights[i + 1][j] >= heights[i][j]) {
                deque.addLast(tt);
                set.add(tt);
            }
        }
        if (j - 1 >= 0) {
            var tt = new Tuple(i, j - 1);
            if (!set.contains(tt) && heights[i][j - 1] >= heights[i][j]) {
                deque.addLast(tt);
                set.add(tt);
            }
        }
        if (j + 1 < n) {
            var tt = new Tuple(i, j + 1);
            if (!set.contains(tt) && heights[i][j + 1] >= heights[i][j]) {
                deque.addLast(tt);
                set.add(tt);
            }
        }
    }

    /**
     * #438
     *
     * @param s
     * @param p
     * @return
     */
    public static List<Integer> findAnagrams(String s, String p) {
        int p_len = p.length();
        Map<Character, Integer> chars = new HashMap<>(p_len);
        Map<Character, Integer> current = new HashMap<>(p_len);
        for (int i = 0; i < p_len; i++) {
            var c = p.charAt(i);
            chars.put(c, chars.getOrDefault(c, 0) + 1);
        }
        List<Integer> ans = new ArrayList<>(16);
        int l = 0;
        for (int r = 0; r < s.length(); r++) {
            var c = s.charAt(r);
            if (chars.containsKey(c)) {
                if (canFillIn(c, current, chars)) {
                    current.put(c, current.getOrDefault(c, 0) + 1);
                    if (r + 1 - l == p_len) {
                        ans.add(l);
                        var last_c = s.charAt(l);
                        current.put(last_c, current.get(last_c) - 1);
                        l++;
                    }
                } else {
                    do {
                        var last_c = s.charAt(l);
                        current.put(last_c, current.get(last_c) - 1);
                        l++;
                    } while (!canFillIn(c, current, chars));
                    current.put(c, current.getOrDefault(c, 0) + 1);
                }
            } else {
                l = r + 1;
                current.clear();
            }
        }
        return ans;
    }

    private static boolean canFillIn(char c, Map<Character, Integer> current,
            Map<Character, Integer> chars) {
        return current.getOrDefault(c, 0) + 1 <= chars.get(c);
    }

    /**
     * #447
     *
     * @param points
     * @return
     */
    public static int numberOfBoomerangs(int[][] points) {
        int ans = 0;
        for (int i = 0; i < points.length; i++) {
            var p = points[i];
            Map<Integer, Integer> distCount = new HashMap<>(
                    points.length - i - 1);
            for (int j = 0; j < points.length; j++) {
                if (j != i) {
                    var pp = points[j];
                    var ds = distSquare(p, pp);
                    distCount.put(ds, distCount.getOrDefault(ds, 0) + 1);
                }
            }
            for (var v : distCount.values()) {
                ans += v * (v - 1);
            }
        }
        return ans;
    }

    private static int distSquare(int[] a, int[] b) {
        var t1 = Math.abs(a[0] - b[0]);
        var t2 = Math.abs(a[1] - b[1]);
        return t1 * t1 + t2 * t2;
    }

    /**
     * 3448
     *
     * @param nums
     * @return
     */
    public static List<Integer> findDisappearedNumbers(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            var n = nums[i];
            nums[(n % (nums.length + 1)) - 1] += nums.length + 1;
        }
        List<Integer> ans = new ArrayList<>(nums.length);
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] <= nums.length) {
                ans.add(i + 1);
            }
        }
        return ans;
    }

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
                if (res != 0 && res != P.length())
                    return true;
                q = PI[q];
            }
        }
        return false;
    }

    /**
     * max match length of prefix of P and suffix end with ith(start from 1)
     * character
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

    /**
     * #460
     */
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
                } else
                    throw new RuntimeException();
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
                    } else
                        throw new RuntimeException();
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

            Info(int val, DLinkMatrix.DMNode nodeM,
                    DLinkMatrix.DLinkList.DLNode nodeL) {
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
            if (capacity == 0)
                return -1;
            var info = key_info_map.get(key);
            int ans = -1;
            if (info == null)
                return ans;
            ans = info.val;

            var mNode = info.nodeM;
            var lNode = info.nodeL;
            var freq = mNode.freq;
            freq++;
            if (mNode.right.freq == freq) {
                lNode.detach();
                if (mNode.list.isEmpty())
                    mNode.detach();
                mNode.right.list.addNode(lNode);
                info.nodeM = mNode.right;
            } else {
                var rMNode = new DLinkMatrix.DMNode(freq);
                DLinkMatrix.insertAfter(mNode, rMNode);
                info.nodeM = rMNode;

                lNode.detach();
                if (mNode.list.isEmpty())
                    mNode.detach();
                rMNode.list.addNode(lNode);
            }
            return ans;
        }

        public void put(int key, int value) {
            if (capacity == 0)
                return;
            if (key_info_map.containsKey(key)) {
                get(key);
                key_info_map.get(key).val = value;
            } else {
                if (key_info_map.size() >= capacity) {
                    // evict
                    var n = dLinkMatrix.firstNode().list.lastNode();
                    key_info_map.remove(n.key);
                    n.detach();
                    if (dLinkMatrix.firstNode().list.isEmpty())
                        dLinkMatrix.firstNode().detach();
                }

                if (dLinkMatrix.isEmpty()) {
                    var mNode = new DLinkMatrix.DMNode(1);
                    var lNode = new DLinkMatrix.DLinkList.DLNode(key);
                    mNode.list.addNode(lNode);
                    DLinkMatrix.insertAfter(dLinkMatrix.head, mNode);
                    key_info_map.put(key, new Info(value, mNode, lNode));
                } else {
                    var fMNode = dLinkMatrix.firstNode();
                    if (fMNode.freq == 1) {
                        var lNode = new DLinkMatrix.DLinkList.DLNode(key);
                        fMNode.list.addNode(lNode);
                        key_info_map.put(key, new Info(value, fMNode, lNode));
                    } else {
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
     * Your LFUCache object will be instantiated and called as such: LFUCache
     * obj = new LFUCache(capacity); int param_1 = obj.get(key);
     * obj.put(key,value);
     */
    /**
     * #463
     * 
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
    public static int islandPerimeter(int[][] grid) {
        int[] dx = { 0, 1, 0, -1 };
        int[] dy = { 1, 0, -1, 0 };
        int n = grid.length, m = grid[0].length;
        int ans = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                if (grid[i][j] == 1) {
                    int cnt = 0;
                    for (int k = 0; k < 4; ++k) {
                        int tx = i + dx[k];
                        int ty = j + dy[k];
                        if (tx < 0 || tx >= n || ty < 0 || ty >= m
                                || grid[tx][ty] == 0) {
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
     * #474
     *
     * @param strs
     * @param m
     * @param n
     * @return
     */
    public static int findMaxForm(String[] strs, int m, int n) {
        int[][] dp = new int[m + 1][n + 1];
        for (var s : strs) {
            var count = countZerosAndOnes(s);
            for (int i = m; i >= 0; i--) {
                for (int j = n; j >= 0; j--) {
                    if (i >= count[0] && j >= count[1]) {
                        dp[i][j] = Math.max(dp[i][j],
                                dp[i - count[0]][j - count[1]] + 1);
                    }
                }
            }
        }
        int max = -1;
        for (var a : dp) {
            for (var num : a) {
                max = Math.max(num, max);
            }
        }
        return max;
    }

    private static int[] countZerosAndOnes(String s) {
        int[] ans = new int[2];
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0')
                ans[0]++;
            else
                ans[1]++;
        }
        return ans;
    }

    /**
     * #475
     *
     * @param houses
     * @param heaters
     * @return
     */
    public static int findRadius(int[] houses, int[] heaters) {
        var tree = new TreeSet<Integer>();
        for (var n : heaters)
            tree.add(n);
        int ans = -1;
        for (var n : houses) {
            var l = tree.floor(n);
            var r = tree.ceiling(n);
            if (l == null)
                ans = Math.max(ans, Math.abs(r - n));
            else if (r == null)
                ans = Math.max(ans, Math.abs(l - n));
            else
                ans = Math.max(ans, Math.min(Math.abs(l - n), Math.abs(r - n)));
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
        if (num == 0)
            return 1;
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

    /**
     * #480
     *
     * @param nums
     * @param k
     * @return
     */
    public static double[] medianSlidingWindow(int[] nums, int k) {
        double[] ans = new double[nums.length - k + 1];
        var MDH = new MedianDualHeap(k);
        int idx = 0;
        for (var n : nums) {
            MDH.add(n);
            if (MDH.hasMediean())
                ans[idx++] = MDH.median();
        }
        return ans;
    }

    static final class MedianDualHeap {
        final PriorityQueue<Long> minHeap;
        final PriorityQueue<Long> maxHeap;

        final Deque<Long> queue;
        final int capacity;

        MedianDualHeap(int cap) {
            minHeap = new PriorityQueue<>((cap + 1) / 2);
            maxHeap = new PriorityQueue<>((cap + 1) / 2,
                    Comparator.reverseOrder());
            queue = new ArrayDeque<>(cap + 1);
            capacity = cap;
        }

        private boolean isEmpty() {
            return maxHeap.isEmpty();
        }

        public boolean hasMediean() {
            return queue.size() == capacity;
        }

        public void add(long n) {
            queue.addLast(n);
            pollQueueExcess();

            if (isEmpty())
                maxHeap.add(n);
            else {
                if (n <= maxHeap.peek())
                    maxHeap.add(n);
                else
                    minHeap.add(n);

                while (maxHeap.size() > minHeap.size() + 1) {
                    minHeap.add(maxHeap.poll());
                }

                while (maxHeap.size() < minHeap.size()) {
                    maxHeap.add(minHeap.poll());
                }
            }
        }

        public double median() {
            if (maxHeap.size() > minHeap.size())
                return maxHeap.peek();
            else
                return (minHeap.peek() + maxHeap.peek()) / 2.;
        }

        private void pollQueueExcess() {
            if (queue.size() > capacity) {
                var q = queue.pollFirst();
                if (!minHeap.remove(q)) {
                    if (!maxHeap.remove(q))
                        throw new RuntimeException();
                }
            }
        }
    }

    /**
     * #481
     *
     * @param n
     * @return
     */
    public static int magicalString(int n) {
        if (n <= 3)
            return 1;

        Deque<Integer> deque = new ArrayDeque<>(n);
        deque.add(2);
        boolean isOne = true;
        int l = 3, ans = 1;
        while (l < n) {
            int group = deque.poll();
            if (isOne)
                ans += group;
            deque.add(isOne ? 1 : 2);
            if (group == 2)
                deque.add(isOne ? 1 : 2);
            l += group;
            isOne = !isOne;
        }
        if (l > n && !isOne)
            ans -= l - n;

        return ans;
    }

    /**
     * #482
     *
     * @param s
     * @param k
     * @return
     */
    public static String licenseKeyFormatting(String s, int k) {
        Deque<Character> stack = new ArrayDeque<>(s.length());
        for (int i = 0; i < s.length(); i++) {
            var c = s.charAt(i);
            if (c != '-') {
                if (c >= 'a' && c <= 'z')
                    c -= 'a' - 'A';
                stack.addLast(c);
            }
        }
        StringBuilder sb = new StringBuilder();
        int count = 0;
        while (stack.size() > 0) {
            sb.insert(0, stack.pollLast());
            count++;
            if (count == k && stack.size() != 0) {
                sb.insert(0, "-");
                count = 0;
            }
        }
        return sb.toString();
    }

    /**
     * #483
     *
     * @param n
     * @return
     */
    public static String smallestGoodBase(String n) {
        long N = Long.parseLong(n);
        for (int m = (int) Math.floor(Math.log(N) / Math.log(2)); m > 1; m--) {
            int b = (int) Math.pow(N, 1. / m);
            if (b >= 2 && baseOnes(m, b) == N)
                return String.valueOf(b);
        }
        return String.valueOf(N - 1);
    }

    private static long baseOnes(int m, int b) {
        long mul = 1, sum = 1;
        for (int i = 0; i < m; i++) {
            mul *= b;
            sum += mul;
        }
        return sum;
    }

    /**
     * #485
     *
     * @param nums
     * @return
     */
    public static int findMaxConsecutiveOnes(int[] nums) {
        int max = 0, count = 0;
        for (int i = 0; i < nums.length; i++) {
            var n = nums[i];
            if (n == 1) {
                if (i - 1 >= 0 && nums[i - 1] == 1)
                    count++;
                else
                    count = 1;
            } else
                max = Math.max(max, count);
        }
        max = Math.max(max, count);
        return max;
    }

    /**
     * #493
     *
     * @param nums
     * @return
     */
    public static int reversePairs(int[] nums) {
        return (new ReversePairsSolver()).solve(nums);
    }

    static class ReversePairsSolver {
        private void merge(int[] array, int start, int[] cache1, int[] cache2) {
            int right_idx = 0;
            int left_idx = 0;
            System.arraycopy(array, start, cache1, 0, cache1.length);
            System.arraycopy(array, start + cache1.length, cache2, 0,
                    cache2.length);
            count(cache1, cache2);
            for (int i = start; (i < start + cache1.length + cache2.length)
                    && (right_idx < cache2.length)
                    && (left_idx < cache1.length); i++) {
                if (cache1[left_idx] <= cache2[right_idx]) {
                    array[i] = cache1[left_idx++];
                } else {
                    array[i] = cache2[right_idx++];
                }
            }
            if (left_idx < cache1.length) {
                System.arraycopy(cache1, left_idx, array,
                        start + left_idx + right_idx, cache1.length - left_idx);
            } else if (right_idx < cache2.length) {
                System.arraycopy(cache2, right_idx, array,
                        start + left_idx + right_idx,
                        cache2.length - right_idx);
            }
        }

        private int count = 0;

        private void count(int[] a, int[] b) {
            int i = 0, j = 0;
            while (i < a.length) {
                while (j < b.length && (long) a[i] > 2 * (long) b[j]) {
                    j++;
                }
                count += j;
                i++;
            }
        }

        public int solve(int[] array) {
            mergeSortReversePairs(array, 0, array.length);
            return count;
        }

        private void mergeSortReversePairs(int[] array, int start, int end) {
            if ((end - start) > 1) {
                int middle = (start + end) / 2;
                mergeSortReversePairs(array, start, middle);
                mergeSortReversePairs(array, middle, end);
                int left_len = middle - start;
                int right_len = end - middle;
                var left_cache = new int[left_len];
                var right_cache = new int[right_len];
                merge(array, start, left_cache, right_cache);
            }
        }
    }

    /**
     * #494
     * 
     * @param nums
     * @param target
     * @return
     */
    public int findTargetSumWays(int[] nums, int target) {
        Arrays.sort(nums);
        Map<Integer, Integer> sumCount = new HashMap<>(8);
        for (var n : nums) {
            if (sumCount.isEmpty()) {
                sumCount.put(n, 1);
                sumCount.put(-n, n == 0 ? 2 : 1);
            } else {
                Map<Integer, Integer> next = new HashMap<>(sumCount.size());
                for (var entry : sumCount.entrySet()) {
                    var v = entry.getKey() + n;
                    next.put(v, entry.getValue() + next.getOrDefault(v, 0));
                    v = entry.getKey() - n;
                    next.put(v, entry.getValue() + next.getOrDefault(v, 0));
                }
                sumCount = next;
            }
        }
        return sumCount.getOrDefault(target, 0);
    }

    /**
     * #498
     *
     * @param mat
     * @return
     */
    public static int[] findDiagonalOrder(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        int lines = m + n - 1;
        int[] ans = new int[m * n];
        int idx = 0;
        for (int sum = 0; sum < lines; sum++) {
            if (sum % 2 == 0) {
                int i = Math.min(sum, m - 1);
                int j = sum - i;
                while (i >= 0 && j < n)
                    ans[idx++] = mat[i--][j++];
            } else {
                int j = Math.min(sum, n - 1);
                int i = sum - j;
                while (i < m && j >= 0)
                    ans[idx++] = mat[i++][j--];
            }
        }
        return ans;
    }
}
