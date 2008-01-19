package org.vermin.util;

public class Pair<K,E> {

	public K first;
	public E second;
	
	public Pair(K first, E second) {
		this.first = first;
		this.second = second;
	}
	
	public Pair() {} // 0-arg for serialization load
}
