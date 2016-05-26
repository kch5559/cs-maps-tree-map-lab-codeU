/**
 * 
 */
package com.flatironschool.javacs;

import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of a Map using a binary search tree.
 * 
 * @param <K>
 * @param <V>
 *
 */
public class MyTreeMap<K, V> implements Map<K, V> {

	private int size = 0;
	private Node root = null;

	/**
	 * Represents a node in the tree.
	 */
	protected class Node {
		public K key;
		public V value;
		public Node left = null;
		public Node right = null;

		/**
		 * @param key
		 * @param value
		 * @param left
		 * @param right
		 */
		public Node(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}

	@Override
	public void clear() {
		size = 0;
		root = null;
	}

	@Override
	public boolean containsKey(Object target) {
		return findNode(target) != null;
	}

	/**
	 * Returns the entry that contains the target key, or null if there is none.
	 *
	 * @param target
	 */
	private Node findNode(Object target) {
		// some implementations can handle null as a key, but not this one
		if (target == null) {
			throw new NullPointerException();
		}

		// something to make the compiler happy
		@SuppressWarnings("unchecked")
		Comparable<? super K> k = (Comparable<? super K>) target;

		Node travelNode = root;

		while (travelNode != null) {
			int cmp = k.compareTo(travelNode.key);
			if (cmp == 0) {
				return travelNode;
			} else if (cmp < 0) {
				travelNode = travelNode.left;
			} else {
				travelNode = travelNode.right;
			}
		}

		return null;
	}

	/**
	 * Compares two keys or two values, handling null correctly.
	 *
	 * @param target
	 * @param obj
	 * @return
	 */
	private boolean equals(Object target, Object obj) {
		if (target == null) {
			return obj == null;
		}
		return target.equals(obj);
	}

	private boolean containsValueHelper(Object target, Node root) {

		if (root == null) return false;
		if (equals(target, root.value)) return true;
		return containsValueHelper(target, root.left) || containsValueHelper(target, root.right);
	}

	@Override
	public boolean containsValue(Object target) {

		return containsValueHelper(target, root);
	}



	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public V get(Object key) {
		Node node = findNode(key);
		if (node == null) {
			return null;
		}
		return node.value;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}


	public void keySetHelper(Node root, Set<K> set) {

		if(root == null) return;

		keySetHelper(root.left, set);
		set.add(root.key);
		keySetHelper(root.right, set);
	}
	@Override
	public Set<K> keySet() {
		Set<K> set = new LinkedHashSet<K>();
        keySetHelper(root, set);
		return set;
	}

	@Override
	public V put(K key, V value) {
		if (key == null) {
			throw new NullPointerException();
		}
		if (root == null) {
			root = new Node(key, value);
			size++;
			return null;
		}
		return putHelper(root, key, value);
	}

	private V putHelper(Node node, K key, V value) {
        // TODO: Fill this in.

		V result = null;
		Node newNode;

		@SuppressWarnings("unchecked")
		Comparable<? super K> k = (Comparable<? super K>) key;
		int cmp =  k.compareTo(node.key);

		//System.out.println("Node key : " + node.key + " New Key : " + key);
		//System.out.println("current size : " + size);


		//when the key exists already.
		if(cmp == 0) {
			newNode = findNode(key);
			//System.out.println("Current value : " + newNode.value + " Replacing value :" + value);
			result = newNode.value;
			newNode.value = value;
		}else if (cmp < 0){
			if(node.left == null) {
				newNode = new Node(key, value);
				node.left = newNode;
				size++;
			}else {
				putHelper(node.left, key, value);
			}
		}else {
			if(node.right == null) {
				newNode = new Node(key, value);
				node.right = newNode;
				size++;
			}else {
				putHelper(node.right, key, value);
			}
		}


        return result;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		for (Map.Entry<? extends K, ? extends V> entry: map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Collection<V> values() {
		Set<V> set = new HashSet<V>();
		Deque<Node> stack = new LinkedList<Node>();
		stack.push(root);
		while (!stack.isEmpty()) {
			Node node = stack.pop();
			if (node == null) continue;
			set.add(node.value);
			stack.push(node.left);
			stack.push(node.right);
		}
		return set;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, Integer> map = new MyTreeMap<String, Integer>();
		map.put("Word1", 1);
		map.put("Word2", 2);
		Integer value = map.get("Word1");
		System.out.println(value);
		
		for (String key: map.keySet()) {
			System.out.println(key + ", " + map.get(key));
		}
	}

	/**
	 * Makes a node.
	 * 
	 * This is only here for testing purposes.  Should not be used otherwise.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public MyTreeMap<K, V>.Node makeNode(K key, V value) {
		return new Node(key, value);
	}

	/**
	 * Sets the instance variables.
	 * 
	 * This is only here for testing purposes.  Should not be used otherwise.
	 * 
	 * @param node
	 * @param size
	 */
	public void setTree(Node node, int size ) {
		this.root = node;
		this.size = size;
	}

	/**
	 * Returns the height of the tree.
	 * 
	 * This is only here for testing purposes.  Should not be used otherwise.
	 * 
	 * @return
	 */
	public int height() {
		return heightHelper(root);
	}

	private int heightHelper(Node node) {
		if (node == null) {
			return 0;
		}
		int left = heightHelper(node.left);
		int right = heightHelper(node.right);
		return Math.max(left, right) + 1;
	}
}
