/**
 * @author Amith Kopparapu Venkata Boja
 */
package edu.iastate.cs472.proj2;
import java.io.*;
import java.util.*;
public class TheoremProver {
	static boolean isKnowledgeBase = false;
	
	static ArrayList<CNF> kb = new ArrayList<CNF>();
	static ArrayList<CNF> provingPropositions = new ArrayList<CNF>();
	public static void main(String[] args){
		File f = new File("kb.txt");
		try {
			System.out.println("Knowledge base in clauses:");
			Scanner s = new Scanner(f);
			String proposition = "";
			while(s.hasNextLine()) {
				String temp = s.nextLine();
				if(temp.length() > 0) {
					proposition+=temp;
				}
				if(temp.length() == 0 || !s.hasNextLine()) {
					//process the proposition
					processPropostion(proposition);
					proposition = "";
				}
			}
			
			for(int i =0;i <provingPropositions.size(); i++) {
				CNF original = provingPropositions.get(i).copy();
				System.out.println("****************\nGoal sentence "+ i +":");
				String currentPropositon  = original.toString();
				System.out.println(currentPropositon);
				System.out.println("****************");
				CNF refuted = notOperation(original);
				System.out.println("Negated goal in clauses: ");
				System.out.println(refuted.toString());
				System.out.println("Proof by refutation:");
				boolean isProven = PL_resolution(refuted);
				
				if(isProven) {
					System.out.println("The knowledge base entails " + currentPropositon);
					System.out.println();
				}else {
					System.out.println("The knowledge base does not entail " + currentPropositon);
					System.out.println();
				}
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	public static void processPropostion(String proposition) {
		String[] components = proposition.split(" ");
		if(components[0].contains("Knowledge") || components[components.length-1].contains("Base:")) {
			isKnowledgeBase = true;
		}
		else if(proposition.toLowerCase().contains("prove")) {
			isKnowledgeBase = false;
		}else {
			String[] postfix = infixToPostfixConversion(components);
			System.out.println();
			ExpressionTree e = new ExpressionTree(postfix);
			CNF cnf = makeCNF(e.returnTree());
			if(isKnowledgeBase) {
				System.out.println(cnf.toString());
				kb.add(cnf);
			}
			else {
				provingPropositions.add(cnf);
			}
		}
	}
	
	public static String[] infixToPostfixConversion(String[] infix) {
		ArrayList<String> post = new ArrayList<String>();
		PureStack<String> operators = new ArrayBasedStack<String>();
		for(String str: infix) {
			String finalStr = str;
			if(str.contains("~")) {
				operators.push(str.charAt(0)+"");
				finalStr = str.substring(1);
			}
			if(finalStr.charAt(0) >= 'A'&& finalStr.charAt(0) <= 'Z' ){ //isPropostion
				post.add(finalStr);
			}else if(finalStr.contains("(")) {
				operators.push("(");
			}else if(finalStr.contains(")")){
				while(!operators.isEmpty() && !operators.peek().equals("(")) {
					post.add(operators.pop());
				}
				operators.pop();
			}else {
				if(!operators.isEmpty() && importance(finalStr)>= importance(operators.peek())) {
					operators.push(finalStr);
				}else {
					while(!operators.isEmpty() && importance(finalStr) <= importance(operators.peek()) && !operators.peek().equals("(")) {
						post.add(operators.pop());
					}
					operators.push(finalStr);
				}
			}
		}
		while(!operators.isEmpty()) {
			post.add(operators.pop());
		}
		String[] temp = new String[post.size()];
		for(int i =0;i<temp.length;i++) {
			temp[i]=post.get(i);
		}
		return temp;
	}
	
	public static int importance(String s) {
		if(s.contains("(")) {
			return 6;
		}else if(s.contains("~")) {
			return 5;
		}else if(s.contains("&&")) {
			return 4;
		}else if(s.contains("||")) {
			return 3;
		}else if(s.contains("=>")) {
			return 2;
		}else if(s.contains("<=>")) {
			return 1;
		}else if(s.contains(")")) {
			return 0;
		}
		return Integer.MIN_VALUE;
	}
	public static CNF makeCNF(Node root) {
		CNF current = new CNF();
		if(root==null)
			return null;
		if(root.isLeaf()) {
			Literal l = new Literal(root.getData());
			Clause c = new Clause();
			c.add(l);
			current.add(c);
			return current;
		}
		CNF left = makeCNF(root.getLeftChild());
		CNF right = makeCNF(root.getRightChild());
		
		if(root.getData().equals("&&")) {
			current = andOperation(left.copy(), right.copy());
		}else if(root.getData().equals("||")) {
			current = orOperation(left.copy(), right.copy());
		}else if(root.getData().equals("~")) {
			current = notOperation(left.copy());
		}else if(root.getData().equals("=>")) {
			CNF temp = notOperation(left.copy());
			current = orOperation(temp, right.copy());
		}else if(root.getData().equals("<=>")) {
			CNF temp = notOperation(left.copy());
			CNF implies = orOperation(temp, right.copy());
			CNF temp2 = notOperation(right.copy());
			CNF implies2 = orOperation(temp2, left.copy());
			current = andOperation(implies, implies2);
			
		}
		
		return current;
	}
	
	public static CNF andOperation(CNF left, CNF right) {
		CNF current = new CNF();
		for(int i=0;i<left.getSize();i++) {
			current.add(left.get(i));
		}
		for(int i=0;i<right.getSize();i++) {
			current.add(right.get(i));
		}
		return current;
	}
	
	public static CNF orOperation(CNF left, CNF right) {
		CNF current = new CNF();
		if(right.getSize()==0) {
			return left;
		}
		if(left.getSize()==0) {
			return right;
		}
		
		for(int i =0;i < left.getSize(); i++) {//clause in left
			
			for(int j=0;j<right.getSize();j++) {// clause in right
				Clause c = left.get(i).copy(); 
				Clause l = right.get(j).copy(); 
				for(int k=0; k<l.getSize(); k++) {//literal in right
					c.add(l.get(k));
				}
				current.add(c);
			}
			
			
		}
		return current;
	}
	

	public static CNF notOperation(CNF left) {
		CNF negated = new CNF();
		if(left.getSize()>=1) {
			CNF current = new CNF();
			Clause c = left.get(0).copy();
			for(int i = 0; i<c.getSize(); i++) {//every literal
				c.get(i).swapNegation(); 
				Clause temp = new Clause();
				temp.add(c.get(i));
				current.add(temp);
			}
			left.remove(0);
			CNF restClauses = notOperation(left);
			negated = orOperation(current, restClauses);
		}
		return negated;
	}
	
	public static boolean PL_resolution(CNF alpha) {
		ArrayList<Clause> clauses = new ArrayList<Clause>();
		ArrayList<Clause> newConclusions = new ArrayList<Clause>();
		for(int i = 0; i<alpha.getSize(); i++) {
			clauses.add(alpha.get(i));
		}
		for(int i = 0; i<kb.size(); i++) {
			CNF cnf = kb.get(i).copy();
			for(int j = 0; j<cnf.getSize(); j++) {
				clauses.add(cnf.get(j));
			}
		}
		while(true) {
			Collections.sort(clauses);
			
			for(int i =0;i <clauses.size()-1;i++) {
				Clause a = clauses.get(i).copy();
				for(int j =i+1;j <clauses.size();j++) {
					
					Clause b = clauses.get(j).copy();
					Clause resolvent = resolve(a.copy(),b.copy());
					if(resolvent !=null) {
						System.out.println("Combining clauses: ");
						System.out.println(a.toString());
						System.out.println(b.toString());
						System.out.println("--------------------");
						if(resolvent.getSize()==0) {
							System.out.println("Empty clause");
							return true;
						}
						System.out.println(resolvent.toString());
						boolean alreadyExists = false;
						for(int k =0; k<clauses.size();k++) {
							if(resolvent.isEqual(clauses.get(k))) {
								alreadyExists = true;
							}
						}
						if(!alreadyExists)
							newConclusions.add(resolvent);
					}
				}
			}
			if(!isImprovement(clauses,newConclusions)) {	
				System.out.println("No new clauses are found. ");
				return false;
			}
			for(int i =0; i<newConclusions.size();i++) {
				Clause c = newConclusions.get(i);
				if(!clauses.contains(c)) {
					clauses.add(c);
				}
			}
		}
	}
	
	public static boolean isImprovement(ArrayList<Clause> clauses,ArrayList<Clause> newConclusions) {
		if(newConclusions.size() == 0) {
			return true;
		}
		for(Clause c:newConclusions) {
			if(!clauses.contains(c)) {
				return true;
			}
		}
		return false;
	}
	
	public static Clause resolve(Clause a, Clause b) {
		Clause resolvent = new Clause();
		boolean hasResolvent = false;
		for(int i=0; i<a.getSize();i++) {
			Literal aL = a.get(i);
			for(int j=0;j<b.getSize();j++) {
				Literal bL = b.get(j);
				if(aL.getProposition().equals(bL.getProposition()) && aL.getIsNegated()!=bL.getIsNegated()) {
					a.remove(aL);
					b.remove(bL);
					hasResolvent = true;
					break;
				}
				
			}
		}
		if(hasResolvent) {
			for(int i=0; i<a.getSize();i++) {
				resolvent.add(a.get(i));
			}
			for(int i=0; i<b.getSize();i++) {
				resolvent.add(b.get(i));
			}
			return resolvent;
		}else {
			return null;
		}
		
		
	}
	

}