package kone.bouchra.crossword;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class crossword  extends Grid<CrosswordSquare> {
	
	 private ObservableList<Clue> verticalClues;
	 
	 private ObservableList<Clue> horizontalClues;
	
	 private Crossword(int height, int width) {}
	
	 public StringProperty propositionProperty(int row, int column){}
	
	 public boolean isBlackSquare(int row, int column){}
	
	 public void setBlackSquare(int row, int column, boolean black){}
	
	 public char getSolution(int row, int column){}
	 
	 public void setSolution(int row, int column, char solution){}
	 
	  public char getProposition(int row, int column){}
	 
	  public void setProposition(int row, int column, char proposition){}
	 
	  public String getDefinition(int row, int column, boolean horizontal){}
	 
	  public void setDefinition(int row, int column, boolean horizontal, String definition){}
	 
	  public ObservableList<Clue> getVerticalClues(){}
	 
	  public ObservableList<Clue> getHorizontalClues(){}
	 
	  public static Crossword createPuzzle(Database database,int puzzleNumber){}
	  
	  public void printProposition(){}
	 
	  public void printSolution(){}


}
