package kone.bouchra.crossword;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;

public class CrosswordSquare extends Label {
	
	private char solution;
//    private char proposition;

    private StringProperty proposition;
    private String horizontal;
    private String vertical;
    private boolean statut;
    
    
    public CrosswordSquare(char solution, String proposition, String horizontal, String vertical, boolean statut) {
        this.solution = solution;
//        this.proposition = proposition;
        this.proposition = new SimpleStringProperty(proposition);
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.statut = statut;
    }
   // TODO Auto-generated constructor stub
	
	public char getSolution(){

        return solution;
    }

    public void setSolution(char solution){

        this.solution=solution;
    }

    
//    public char getProposition() {
//
//        return proposition;
//    }
//
//    public void setProposition(char proposition) {
//
//        this.proposition=proposition;
//    }
    
    public StringProperty getProposition() {
        return proposition;
    }



    public void setProposition(String proposition) {
        this.proposition.set(proposition);
    }
    

    public String getHorizontal() {

        return horizontal;
    }
    
    public void setHorizontal(String horizontal) {
        this.horizontal=horizontal;
    }
    
    public String getVertical() {
    	return vertical;
    }
    
    public void setVertical(String vertical) {
    	this.vertical=vertical;
    }
    
    public boolean getStatut() {
    	return statut;
    }
    
    public void setStatut(boolean statut) {
    	this.statut=statut;
    }

	public void setStyle(String string) {
		// TODO Auto-generated method stub
		
	}

	public void requestFocus() {
		// TODO Auto-generated method stub
		
	}

	

}
