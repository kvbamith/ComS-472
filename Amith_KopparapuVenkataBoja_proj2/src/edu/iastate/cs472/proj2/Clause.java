/**
 * @author Amith Kopparapu Venkata Boja
 */
package edu.iastate.cs472.proj2;

import java.util.LinkedList;

public class Clause implements Comparable<Clause>{
	private LinkedList<Literal> list;
	
	
	public Clause() {
		list = new LinkedList<Literal>();
	}
	
	public void add(Literal l) {
		list.add(l);
	}
	
	public void remove(Literal l) {
		list.remove(l);
	}
	
	public Literal get(int i) {
		return list.get(i);
	}
	
	public int getSize() {
		return list.size();	
	}
	
	public Clause copy() {
		Clause temp = new Clause();
		for(Literal index:list) {
			Literal l = new Literal(index.getProposition());
			l.setNegation(index.getIsNegated());
			temp.add(l);
		}
		return temp;
	}

	@Override
	public int compareTo(Clause o) {
		if(this.getSize()>o.getSize()) {
			return 1;
		}else if(o.getSize()>this.getSize()) {
			return -1;
		}
		return 0;
	}
	
	public String toString() {
		String temp = "";
		for(int j = 0; j<list.size()-1; j++) {
			if(list.get(j).getIsNegated()) {
				temp+="~";
			}
			temp+=(list.get(j).getProposition()+ " V ");
		}
		if(list.get(list.size()-1).getIsNegated()) {
			temp+="~";
		}
		temp+=(list.get(list.size()-1).getProposition());
		temp+="\n\n";
		return temp;
	}
	
	public boolean isEqual(Clause c) {
		if(c.getSize() != this.getSize()) {
			return false;
		}
		for(int i =0; i<list.size();i++) {
			if(!c.get(i).isEqual(this.get(i))) {
				return false;
			}
		}
		return true;
	}
}
