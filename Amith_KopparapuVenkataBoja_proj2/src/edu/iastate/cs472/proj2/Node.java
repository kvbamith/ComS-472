/**
 * @author Amith Kopparapu Venkata Boja
 */
package edu.iastate.cs472.proj2;

public class Node{
	private String data;
	private Node leftChild;
	private Node rightChild;
	
	public Node(String data) {
		this.data = data;
		this.leftChild = null;
		this.rightChild = null;
	}
	
	public Node(String data, Node n) {
		if(data.contains("~")) {
			this.data = data;
			this.leftChild = n;
			this.rightChild = null;
		}
	}
	
	public Node(String data, Node left, Node right) {
		this.data = data;
		this.leftChild = left;
		this.rightChild = right;
	}
	
	public String getData() {
		return data;
	}
	
	public Node getLeftChild() {
		return leftChild;
	}
	
	public Node getRightChild() {
		return rightChild;
	}
	
	public void setLeftChild(Node n) {
		leftChild = new Node(n.getData());
	}
	
	public void setRightChild(Node n) {
		rightChild = new Node(n.getData());
	}
	public boolean isLeaf() {
		return (leftChild == null && rightChild==null);
	}
}
