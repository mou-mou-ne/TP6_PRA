package kone.bouchra.crossword;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class crossword  extends Grid<CrosswordSquare> {
	
	 private ObservableList<Clue> verticalClues;
	 
	 private ObservableList<Clue> horizontalClues;
	
	 private crossword(int height, int width) {
		 
	        super(height, width);

	 }
	
	 public StringProperty propositionProperty(int row, int column){
		 
	        return getCell(row, column).propositionProperty();

	 }
	
	 public boolean isBlackSquare(int row, int column){
		 
	        return getCell(row, column).isBlack();

	        
	 }
	
	 public void setBlackSquare(int row, int column, boolean black){
		 
	        getCell(row, column).setBlack(black);

	 }
	
	 public char getSolution(int row, int column){
		 
	        return getCell(row, column).getSolution();

	 }
	 
	 public void setSolution(int row, int column, char solution){
		 
	        getCell(row, column).setSolution(solution);

	 }
	 
	  public char getProposition(int row, int column){
		  
	        return getCell(row, column).getProposition();
	  }
	 
	  public void setProposition(int row, int column, char proposition){
		  
	        getCell(row, column).setProposition(proposition);

	  }
	 
	  public String getDefinition(int row, int column, boolean horizontal){
		  
		  if (horizontal) {
	            return horizontalClues.stream()
	                    .filter(clue -> clue.getRow() == row && clue.getColumn() <= column && column < clue.getColumn() + clue.getClue().length())
	                    .findFirst()
	                    .map(Clue::getClue)
	                    .orElse("");
	        } else {
	            return verticalClues.stream()
	                    .filter(clue -> clue.getColumn() == column && clue.getRow() <= row && row < clue.getRow() + clue.getClue().length())
	                    .findFirst()
	                    .map(Clue::getClue)
	                    .orElse("");
	        }
	    
	  }
	 
	  public void setDefinition(int row, int column, boolean horizontal, String definition){
		  
		  if (horizontal) {
	            horizontalClues.add(new Clue(definition, row, column, true));
	        } else {
	            verticalClues.add(new Clue(definition, row, column, false));
	        }
	  }
	 
	  public ObservableList<Clue> getVerticalClues(){
		  
	        return verticalClues;
      
	  }
	 
	  public ObservableList<Clue> getHorizontalClues(){
		  
	        return horizontalClues;

	  }
	 
	  public static crossword createPuzzle(Database database,int puzzleNumber){}
	  
	  public void printProposition(){}
	 
	  public void printSolution(){}


}
