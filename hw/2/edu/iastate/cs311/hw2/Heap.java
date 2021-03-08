package edu.iastate.cs311.hw2;

/**
 * @author Haadi Majeed
 *  A simple priority queue interface and a class template implementing
 *  the interface with a heap and a heap sort algorithm. This code template is
 *  written by Xiaoqiu Huang for Com S 311 in Spring 2021.
 */

import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

interface ExtendedPriorityQueue<E>
{
  int size();
  boolean isEmpty();
  void add(E element);

  // Returns a high-priority element without removing it.
  E getMin();

  // Removes a high-priority element.
  E removeMin();

  // Returns an element at the last nonleaf node in the heap
  E getLastInternal();

  // Removes all elements at each leaf node in the heap
  void trimEveryLeaf();

  // Shows the heap as a binary tree in plain text
  void showHeap();
}

public class Heap<E extends Comparable<? super E>>
   implements ExtendedPriorityQueue<E>
{
  private static final int INIT_CAP = 10; // A default size of list
  private ArrayList<E> list; // used as an array to keep the elements in the heap
  // list.size() returns the number of elements in the list,
  // which is also the size of the heap.
  // For 0 <= k < list.size(), list.get(k) returns the element at position k of list
  // list.remove( list.size() - 1 ) removes the last element from the list;
  // note that there is no need to remove any element before the last element.

  public Heap()
  {
    list = new ArrayList<E>(INIT_CAP);
  }

  public Heap(int aSize)
  {
     if ( aSize < 1 )
	throw new IllegalStateException();
     list = new ArrayList<E>(aSize);
  }

  // Builds a heap from a list of elements.
  public Heap(List<E>  aList)
  {
     int j, k;
     int len = aList.size();
     if ( len < 1 )
	throw new IllegalArgumentException();
     list = new ArrayList<E>(len);
     for ( E t : aList )
        list.add(t);
     if ( len < 2 ) return;
     j = (len - 2) / 2; // j is the largest index of an internal node with a child.
     for ( k = j; k >= 0; k-- )
        percolateDown(k);
  }  // O(n) time

  public int size()
  {
    return list.size();
  }

  public boolean isEmpty()
  {
    return list.isEmpty();
  }

  public void add(E element)
  {
    if ( element == null )
      throw new NullPointerException("add");
    list.add(element); // append it to the end of the list
    percolateUp(); // move it up to the proper place
  }

  // TODO: O(log n)
  // Moves the last element up to the proper place so that the heap property holds.
  private void percolateUp() {
    //set index for loop to be last element of the list
    int i = list.size()-1;
    //iterate through checking child with parents
    while(list.get(i).compareTo(list.get((i-1)/2)) < 0){
      //swap the elements
      swap(i, (i-1)/2);
      //change index to parent
      i = (i-1)/2;
    }

  }

  // Swaps the elements at the parent and child indexes.
  private void swap(int parent, int child)
  {
    E tmp = list.get(parent);
    list.set( parent, list.get(child) );
    list.set(child, tmp);
  }

  public E getMin()
  {
    if ( list.isEmpty() )
      throw new NoSuchElementException();
    return list.get(0);
  }

  // TODO: O(1)
  // Returns an element at the last nonleaf node in the heap
  // If the size of the heap is less than 2, it throws new NoSuchElementException().
  public  E getLastInternal()
  {
    if(list.size() < 2){
      throw new NoSuchElementException();
    }
    else{
      return list.get((list.size()-2)/2); //yeet the val of the last parent
    }
  }

  public E removeMin()
  {
    if ( list.isEmpty() )
      throw new NoSuchElementException();
    E minElem = list.get(0); // get the min element at the root
    list.set(0, list.get(list.size() - 1) ); // copy the last element to the root
    list.remove( list.size() - 1 ); // remove the last element from the list
    if ( ! list.isEmpty() )
     percolateDown(0); // move the element at the root down to the proper place
    return minElem;
  }

  // TODO: O(n)
  // If the heap contains internal (nonleaf) nodes, trim every leaf element.
  // If the size of the heap is less than 2, it throws new NoSuchElementException().
  public void trimEveryLeaf()
  {
    if(list.size() < 2){
    throw new NoSuchElementException();
  }
    else{
      int index = list.indexOf(getLastInternal());
      for(int i = list.size()-1; i > index; i--){
        list.remove(i);
      }
    }
  }

  // TODO: O(log n)
  // Move the element at index start down to the proper place so that the heap property holds.
  private void percolateDown(int start)
  {
    if ( start < 0 || start >= list.size() )
      throw new RuntimeException("start < 0 or >= n");
    else if(2*start+2 < list.size() && list.get(start).compareTo(list.get(2*start+1)) > 0){
      swap(start,2*start+1);
      percolateDown(2*start+1);
    }
    else if(2*start+2 < list.size() && list.get(start).compareTo(list.get(2*start+2)) > 0){
      swap(start,2*start+2);
      percolateDown(2*start+2);
    }

  }

  // Shows the tree used to implement the heap with the root element at the leftmost column
  // and with 'null' indicating no left or right child.
  // This method is used to help check if the heap is correctly constructed.
  public void showHeap()
  {
    if ( list.isEmpty() ) return;
    recShowHeap(0, ">");
  }

  public void recShowHeap(Integer r, String level)
  {
    int len = list.size();
    if ( r >= len )
    {
      System.out.println(level + "null");
      return;
    }
    System.out.println(level + list.get(r) ); // get the min element at the root
    recShowHeap(2 * r + 1, " " + level);
    recShowHeap(2 * r + 2, " " + level);
  }

  // This method repeatedly removes the smallest element in the heap and add it to the list.
  public static <E extends Comparable<? super E>> void heapSort(List<E> aList)
  {
    if ( aList.isEmpty() ) return;
    Heap<E> aHeap = new Heap<E>(aList);
    aList.clear();
    while ( ! aHeap.isEmpty() )
      aList.add( aHeap.removeMin() );
  }
} // Heap
