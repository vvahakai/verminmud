package org.vermin.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A ring buffer where new values are added to the end until the
 * maximum capasity is reached and consequent additions remove
 * the first element.
 * 
 * The buffer contents can be iterated over in addition order.
 */
public class RingBuffer<T> implements Iterable<T> {
	
	private Object[] values;
	private int lastIndex;
	
	/**
	 * Create a new ringbuffer of the specified size.
	 */
	public RingBuffer(int size) {
		values = new Object[size];
	}
	
	/**
	 * Add a value to this ringbuffer.
	 * If the buffer is at capacity, the oldest value
	 * is removed from the buffer to make room.
	 */
	public synchronized void add(T value) {
		lastIndex = (lastIndex+1) % values.length;
		values[lastIndex] = value;
	}
	
	/**
	 * Iterate over the contents of this ringbuffer.
	 * The values are returned in the same order they were
	 * added.
	 */
	public Iterator iterator() {
		return new Iterator(){
			private int cur = 0;
			private Object curValue = null;
			
			private Object nextValue() {
				while(values[(lastIndex+cur+1)%values.length] == null && cur < values.length)
					cur++;
				if(cur < values.length)
					return values[(lastIndex+cur+1)%values.length];
				else
					return null;
			}
			
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
			public Object next() {
				if(hasNext()) {
					Object ret = curValue;
					curValue = null;
					cur++;
					return ret;
				} else throw new NoSuchElementException();
			}
			
			public boolean hasNext() {
				if(curValue == null)
					curValue = nextValue();
				return curValue != null;
			}				
		};
	}
}
