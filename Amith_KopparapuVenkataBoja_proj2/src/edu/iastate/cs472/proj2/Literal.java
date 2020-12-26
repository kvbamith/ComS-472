/**
 * @author Amith Kopparapu Venkata Boja
 */
package edu.iastate.cs472.proj2;

public class Literal {
	private String proposition;
	private boolean isNegated;
	
	public Literal(String p) {
		proposition = p;
		isNegated = false;
	}

	public void setNegation(boolean isN) {
		isNegated = isN;
	}
	
	public void swapNegation() {
		isNegated = !isNegated;
	}
	public boolean getIsNegated() {
		return isNegated;
	}
	
	public String getProposition() {
		return proposition;
	}
	
	public boolean isEqual(Literal l) {
		return (this.isNegated==l.getIsNegated() && this.proposition.equals(l.getProposition()));
	}
}
