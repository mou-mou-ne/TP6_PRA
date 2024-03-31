package kone.bouchra.crossword;

public class CrosswordSquare {
	
	private char solution;
    private char proposition;
    private String horizontal;
    private String vertical;
    private boolean statut;
    
    
    public char getSolution(){

        return solution;
    }

    public void setSolution(char solution){

        this.solution=solution;
    }

    
    public char getProposition() {

        return proposition;
    }

    public void setProposition(char proposition) {

        this.proposition=proposition;
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

}
