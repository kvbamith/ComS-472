/**
 * @author Amith Kopparapu Venkata Boja
 */
package edu.iastate.cs472.proj2;

public class ExpressionTree {
	private String[] proposition;
	private PureStack<Node> tree = new ArrayBasedStack<Node>();
	public ExpressionTree(String[] p) {
		proposition = p;
		initialzeTree();
	}
	public void initialzeTree() {
		for(String str: proposition) {
			if(str.charAt(0) >= 'A'&& str.charAt(0) <= 'Z' ){
				Node n = new Node(str);
				tree.push(n);
			}else if(!tree.isEmpty() && (str.contains("&&") || str.contains("||") || str.contains("=>") || str.contains("<=>"))) {
				Node right = tree.pop();
				Node left = tree.pop();
				Node n = new Node(str,left,right);
				tree.push(n);
			}else if(!tree.isEmpty() && (str.contains("~"))) {
				Node left = tree.pop();
				Node n = new Node(str,left);
				tree.push(n);
			}
		}
	}
	
	public Node returnTree() {
		return tree.peek();
	}
}