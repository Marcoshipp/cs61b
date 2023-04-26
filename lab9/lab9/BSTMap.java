package lab9;

import edu.princeton.cs.algs4.BST;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Marco Omagbons
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) return null;
        int cmp = p.key.compareTo(key);
        if (cmp == 0) {
            return p.value;
        }
        // the root's key is larger than the given key, go left
        else if (cmp > 0) {
            return getHelper(key, p.left);
        }
        // the root's key is less than the given key, go right
        return getHelper(key, p.right);
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        Node p = this.root;
        return getHelper(key, p);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) return new Node(key, value);
        int cmp = p.key.compareTo(key);
        if (cmp > 0) {
            p.left = putHelper(key, value, p.left);
        }
        // the root's key is less than the given key, go right
        else if (cmp < 0){
            p.right = putHelper(key, value, p.right);
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        Node p = this.root;
        this.root = putHelper(key, value, p);
        this.size++;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return this.size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        // traverse the tree and add all keys to the set
        Node p = this.root;
        keySetHelper(set, p);
        return set;
    }

    private void keySetHelper(Set<K> keys, Node p) {
        if (p != null) {
            keys.add(p.key);
            keySetHelper(keys, p.left);
            keySetHelper(keys, p.right);
        }
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }
    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    public static void main(String[] args) {
        BSTMap<String, Integer> b = new BSTMap<>();
        b.put("0", 0);
        b.put("1", 1);
        b.put("2", 2);
        b.put("3", 3);
        b.put("4", 4);
        b.put("5", 5);
        b.put("6", 6);
        b.put("7", 7);
        for (String s: b) {
            System.out.println(s);
        }
    }
}
