

public class Vector<E> {

	protected final E [] _data;
	
	public Vector(E [] a) {
		_data = (E []) new Object[a.length]; // ugly but gets it done
		for(int i=0;i<a.length;i++) {
			_data[i]=a[i];
		}
	}
	
	public int size() {
		return _data.length;
	}
	
	public E valueAt(int pos) {
		_range_check(pos);
		return _data[pos];
	}

	private void _range_check(int pos) {
		if (pos >= size() || pos < 0) 
			throw new IndexOutOfBoundsException(pos+"");	
	}
	
}
