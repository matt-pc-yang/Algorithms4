/******************************************************************************
 *  Compilation:  javac Ex1D3D31DoubleLinkedList.java
 *  Execution:    java Ex1D3D31DoubleLinkedList < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/13stacks/tobe.txt
 *
 * Exercise 1.3.31
 * Implement a nested class DoubleNode for building doubly-linked lists, where 
 * each node contains a reference to the item preceding it and the item 
 * following it in the list (null if there is no such item). Then implement 
 * static methods for the following tasks: insert at the beginning, insert at 
 * the end, remove from the beginning, remove from the end, insert before a 
 * given node, insert after a given node, and remove a given node. 
 * 
 * A generic double linked list. Each element is of type Item.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  ToDo:
 *  The {@code Ex1D3D31DoubleLinkedList} class represents a double linked list 
 *  of generic items.
 *  This implementation uses a double linked list with a static nested class for
 *  linked-list nodes.
 *  <p>
 *
 *  @author Matt Yang
 *
 *  @param <Item> the generic type of an item in this list
 */
public class Ex1D3D31DoubleLinkedList<Item> implements Iterable<Item> {

    private final DoubleNode<Item> HEAD = new DoubleNode<>(); // link head
    private final DoubleNode<Item> TAIL = new DoubleNode<>(); // link tail
    private int n;                // size of the list

    // helper double linked list class
    private static class DoubleNode<Item> {
        private Item item = null;
        private DoubleNode<Item> prev = null;
        private DoubleNode<Item> next = null;
    }

    /**
     * Constructor: Initializes an empty list.
     */
    public Ex1D3D31DoubleLinkedList() {
        HEAD.next = TAIL;
        TAIL.prev = HEAD;
        n = 0;
    }

    /**
     * Returns true if this list is empty.
     *
     * @return true if this list is empty; false otherwise
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * Returns the number of items in this list.
     *
     * @return the number of items in this list
     */
    public int size() {
        return n;
    }

    public static <Item> void pushHead(Ex1D3D31DoubleLinkedList<Item> list, Item item) {
        list.insertAt(0, item);
    }

    public static <Item> void pushTail(Ex1D3D31DoubleLinkedList<Item> list, Item item) {
        list.insertAt(list.size(), item);
    }

    public static <Item> void push(Ex1D3D31DoubleLinkedList<Item> list, 
                                    Item item, boolean aheadOf, Item refItem) throws NoSuchElementException {
        int index = list.findIndex(refItem);
        list.insertAt(aheadOf ? index : index+1, item);
    }

    public static <Item> void push(Ex1D3D31DoubleLinkedList<Item> list, 
                                    Item item, boolean aheadOf, int index) throws NoSuchElementException {
        DoubleNode<Item> newNode = new DoubleNode<>();
        newNode.item = item;

        index = aheadOf ? index : index+1;
        list.insertAt(index, item);
    }

    public static <Item> Item retrieveHead(Ex1D3D31DoubleLinkedList<Item> list) throws NoSuchElementException {
        return list.retrieveAt(0);
    }

    public static <Item> Item retrieveTail(Ex1D3D31DoubleLinkedList<Item> list) throws NoSuchElementException {
        return list.retrieveAt(list.size()-1);
    }

    public static <Item> Item retrieve(Ex1D3D31DoubleLinkedList<Item> list, int index) throws NoSuchElementException {
        return list.retrieveAt(index);
    }

    public int findIndex(Item item) {
        if (isEmpty()) throw new NoSuchElementException();

        int index = 0;
        DoubleNode<Item> currentNode = HEAD.next;
        while (currentNode != TAIL) {
            if (currentNode.item == item)
                return index;
            currentNode = currentNode.next;
            index++;
        }
        
        throw new NoSuchElementException();
    }

    private DoubleNode<Item> findAt(int index) {
        if (index < 0 || index > size()) throw new NoSuchElementException();

        DoubleNode<Item> current;
        if (index < size()/2) {
            current = HEAD.next;
            for (int i=0; i<index; i++)
                current = current.next;
        }
        else {
            current = TAIL;
            for (int i=size(); i>index; i--)
                current = current.prev;
        }
        
        return current;
    }

    public void insertAt(int index, Item item) throws NoSuchElementException {
        if (index < 0 || index > size()) throw new NoSuchElementException();

        DoubleNode<Item> newNode = new DoubleNode<>();
        newNode.item = item;

        DoubleNode<Item> nextNode = findAt(index);
        DoubleNode<Item> prevNode = nextNode.prev;

        prevNode.next = newNode;
        newNode.prev = prevNode;
        newNode.next = nextNode;
        nextNode.prev = newNode;

        n++;
    }

    public Item retrieveAt(int index) throws NoSuchElementException {
        if (isEmpty() || index >= size()) throw new NoSuchElementException();

        DoubleNode<Item> node = findAt(index);
        node.prev.next = node.next;
        node.next.prev = node.prev;
        n--;
        
        return node.item;
    }

    /**
     * Returns a string representation of this list.
     *
     * @return the sequence of items in this list from head to tail, separated by spaces
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this) {
            s.append(item);
            s.append(' ');
        }
        return s.toString();
    }

    /**
     * Returns an iterator to this list that iterates through the items from head to tail.
     *
     * @return an iterator to this stack that iterates through the items from head to tail.
     */
    @Override
    public Iterator<Item> iterator() {
        return new LinkedIterator(HEAD);
    }

    // an iterator, doesn't implement remove() since it's optional
    private class LinkedIterator implements Iterator<Item> {
        private DoubleNode<Item> currentNode;

        public LinkedIterator(DoubleNode<Item> node) {
            currentNode = node;
        }

        @Override
        public boolean hasNext() {
            return currentNode.next != TAIL;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            currentNode = currentNode.next;
            return currentNode.item;
        }
    }


    static boolean error = true;
    static int total = 0;
    /**
     * Unit tests the {@code Ex1D3D31DoubleLinkedList} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        Ex1D3D31DoubleLinkedList<String> list = 
                new Ex1D3D31DoubleLinkedList<>();

        int pass = 0;
        total = 0;

        // Test#1
        resetTest();
        try {
            error = true;
            retrieveHead(list);
        } catch (Exception e) {
            error = false;
        }
        if (error) StdOut.print("ERROR#1: retrieve head from an empty list shall return error!");
        else pass++;

        // Test#2
        resetTest();
        try {
            error = true;
            retrieveTail(list);
        } catch (Exception e) {
            error = false;
        }
        if (error) StdOut.print("ERROR#2: retrieve tail from an empty list shall return error!");
        else pass++;

        // Test#3
        resetTest();
        try {
            error = true;
            retrieve(list, 0);
        } catch (Exception e) {
            error = false;
        }
        if (error) StdOut.print("ERROR#3: retrieve from an empty list shall return error!");
        else pass++;

        // Test#4
        resetTest();
        if (list.size() == 0 && list.isEmpty()) pass++;
        else StdOut.print("ERROR#4: Uninitialized list is not empty!");

        // Test#5
        final String H0 = "h0";
        final String H1 = "h1";
        final String M0 = "m0";
        final String M1 = "m1";
        final String T0 = "t0";

        resetTest();
        pushHead(list, H0); // HEAD-h0-TAIL
        pushTail(list, T0); // HEAD-h0-t0-TAIL
        push(list, M0, true, 1); // HEAD-h0-m0-t0-TAIL
        push(list, M1, false, 1); // HEAD-h0-m0-m1-t0-TAIL
        push(list, H1, true, 1); // HEAD-h0-h1-m0-m1-t0-TAIL

        if (list.size() != 5) {
            error = true;
            StdOut.println("ERROR#5-3: List size not expected: " + list.size() + " instead of 5");
        }

        if (list.findIndex(H0) == 0 
            && list.findIndex(H1) == 1 
            && list.findIndex(M0) == 2 
            && list.findIndex(M1) == 3 
            && list.findIndex(T0) == 4) {
            if (!"h0 h1 m0 m1 t0".equals(list.toString().trim())) {
                error = true;
                StdOut.println("ERROR#5-1: list.toString() not expected: " + list.toString());
            }
        }
        else {

            StdOut.println("ERROR#5-2: list not expected: " + list.toString());

        if (!error) pass++;

        // Test#6
        resetTest();
        String h1 = retrieve(list, 1);
        if (h1 == H1) error = false;
        else StdOut.println("ERROR#6-1: try to retrieve h1 but get: " + h1 + " instead.");

        String t0 = retrieveTail(list);
        if (t0 == T0) error = StdOut.println("ERROR#6-2: try to retrieve t0 but get: " + t0 + " instead.");
        else {
            String h0 = retrieveHead(list);
            if (h0 != H0) StdOut.println("ERROR#6-3: try to retrieve h0 but get: " + h0 + " instead.");
            else if (list.size() != 2)
                StdOut.println("ERROR#6-4: List size not expected: " + list.size() + " instead of 2");
            else
                pass++;
        }

        // Test#7
        resetTest();
        StringBuilder builder = new StringBuilder();
        for (String item : list) {
            builder.append(item);
            builder.append(' ');
        }
        if ("m0 m1".equals(builder.toString().trim())) pass++;
        else StdOut.println("ERROR#7: List content not expected: " + builder.toString());

        StdOut.println("Passed: " + pass + "/" + total);
        StdOut.println("Failed: " + (total-pass) + "/" + total);
    }

    private static void resetTest() {
        total++;
        error = false;
    }

}