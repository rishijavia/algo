import java.util.Iterator;

public class Stack<E> implements Iterable<E> {

  private Node first;   // top of the stack
  private int N;    	// # of items

  private class Node {
	E item;
	Node next;
  }

  public boolean isEmpty() { return first == null; }
  public int size() { return N; }

  public void push(E item) {

	Node oldfirst = first;
	first = new Node();
	first.item = item;
	first.next = oldfirst;
	N++;
  }

  public E pop() {
	E item = first.item;
	first = first.next;
	N--;
	return item;
  }

  public Iterator<E> iterator() { return new ListIterator(); }

  private class ListIterator implements Iterator<E> {
	private Node current = first;

    public boolean hasNext() { return current != null; }

    public E next() {
	  E item = current.item;
	  current = current.next;
	  return item;
    }

    public void remove() { /* do nothing */ }

  }
}
