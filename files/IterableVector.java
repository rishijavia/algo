
import java.util.Iterator;
import java.util.NoSuchElementException;

public class IterableVector<E> extends Vector<E> implements Iterable<E> {

	public IterableVector(E[] a) {
		super(a);
	}

	@Override
	public Iterator<E> iterator() {
		return new VectorIterator();
	}
	
	private class VectorIterator implements Iterator<E> {

		private int position=0;
		
		public boolean hasNext() {
			return (position < size());
		}

		public E next() {
		
			if (position >= size()) 
				throw new NoSuchElementException("end of interation");
			  
			return _data[position++];
			
		}
		
		public void remove () { 
			throw new UnsupportedOperationException("remove operation not allowed");
		}
	}
	
	

}
