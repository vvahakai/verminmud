/**
 * A sad hack for some FP style programming.
 */
package org.vermin.util;

import java.util.*;

public class Functional {

	public static class Lambda<T,A> {
		public T call(A arg1) {
			throw new IllegalArgumentException("Wrong argument count.");
		}
		public T call(A arg1, A arg2) {
			throw new IllegalArgumentException("Wrong argument count.");
		}
		public T call(A arg1, A arg2, A arg3) {
			throw new IllegalArgumentException("Wrong argument count.");
		}
		public T call(A arg1, A arg2, A arg3, A arg4) {
			throw new IllegalArgumentException("Wrong argument count.");
		}			
	};

	/* Predicate class */
	public static class Predicate<K> {
		public boolean call(K arg) {
			throw new UnsupportedOperationException("Method not implemented");
		}
	}
	
	/* Interface for single mapping: F -> T (from, to) */
	public interface MAP<F,T> {
		public T call(F arg);
	};
	
	/* Interface for key,value mapping: K,V -> T */
	public interface MH<K,V, T> {
		public T call(K key, V value);
	}

	public interface MOST<T> {
		public int call(T arg);
	}

	public static List map(MAP proc, Iterable lst) {
		LinkedList result = new LinkedList();
		for(Object o : lst) result.add(proc.call(o));
		return result;
	}

	public static List filter(Predicate filter, Iterable lst) {
		LinkedList result = new LinkedList();
		for(Object o : lst)
			if(filter.call(o))
				result.add(o);
		return result;
	}

	public static Object most(MOST proc, Iterable lst) {
		Object winner = null;
		int points=0;
		for(Object o : lst) {
			if(winner == null) {
				winner = o;
				points = proc.call(o);
			} else {
				int p1 = proc.call(o);
				winner = p1 > points ? o : winner;
			}
		}
		return winner;
	}
}
