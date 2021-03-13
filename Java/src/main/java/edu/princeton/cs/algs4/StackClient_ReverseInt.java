package edu.princeton.cs.algs4;

public class StackClient_ReverseInt {
    public static void main(String[] args) {
        Stack<Integer> stack;
        stack = new Stack<Integer>();
        while (!StdIn.isEmpty())
            stack.push(StdIn.readInt());
        for (int i : stack)
            StdOut.println(i);
    }
}
