/**
 * @author Amith Kopparapu Venkata Boja
 */
package edu.iastate.cs472.proj2;

import java.util.LinkedList;

public class CNF {
	private LinkedList<Clause> list;
	
	
	public CNF() {
		list = new LinkedList<Clause>();
	}
	
	public void add(Clause l) {
		list.add(l);
	}
	
	public void remove(Clause l) {
		list.remove(l);
	}
	
	public void remove(int i) {
		list.remove(i);
	}
	
	public Clause get(int i) {
		return list.get(i);
	}
	
	public int getSize() {
		return list.size();	
	}
	
	public CNF copy() {
		CNF temp = new CNF();
		for(Clause c:list) {
			Clause t = c.copy();
			temp.add(t);
		}
		return temp;
	}
	
	public String toString() {
		String temp = "";
		for(int i = 0; i<list.size(); i++) {
			Clause c = list.get(i);
			for(int j = 0; j<c.getSize()-1; j++) {
				if(c.get(j).getIsNegated()) {
					temp+="~";
				}
				temp+=(c.get(j).getProposition()+ " V ");
			}
			if(c.get(c.getSize()-1).getIsNegated()) {
				temp+="~";
			}
			temp+=(c.get(c.getSize()-1).getProposition());
			temp+="\n\n";
		}
		return temp;
	}
}
