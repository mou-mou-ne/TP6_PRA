package kone.bouchra.crossword;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Crossword extends Grid<CrosswordSquare> {
	private Grid<CrosswordSquare> newGrid;

    private ObservableList<Clue> verticalClues;
    private ObservableList<Clue> horizontalClues;

    Crossword(int height, int width) {
        super(height, width);
        initialiseGrid();
        verticalClues = FXCollections.observableArrayList();
        horizontalClues = FXCollections.observableArrayList();
    }

    private void initialiseGrid() {
        for (int row = 1; row <= getHeight(); row++) {
            for (int col = 1; col <= getWidth(); col++) {
                CrosswordSquare square = new CrosswordSquare(' ', ' ', " ", " ", false);
                setCell(row, col, square);
                setBlackSquare(row, col, true);
            }
        }
    }

//    public String getDefinition(int row, int column, boolean horizontal) {
//        if (horizontal) {
//            return horizontalClues.stream()
//                    .filter(clue -> clue.getRow() == row && clue.getColumn() <= column
//                            && column < clue.getColumn() + clue.getClue().length())
//                    .findFirst()
//                    .map(Clue::getClue)
//                    .orElse("");
//        } else {
//            return verticalClues.stream()
//                    .filter(clue -> clue.getColumn() == column && clue.getRow() <= row
//                            && row < clue.getRow() + clue.getClue().length())
//                    .findFirst()
//                    .map(Clue::getClue)
//                    .orElse("");
//        }
//    }
//
//    public void setDefinition(int row, int column, boolean horizontal, String definition) {
//        if (horizontal) {
//            horizontalClues.add(new Clue(definition, row, column, true));
//        } else {
//            verticalClues.add(new Clue(definition, row, column, false));
//        }
//    }

    public ObservableList<Clue> getVerticalClues() {
        return verticalClues;
    }

    public ObservableList<Clue> getHorizontalClues() {
        return horizontalClues;
    }

    public static Crossword createPuzzle(Database database, int puzzleNumber) throws SQLException {
        Crossword crossword = null;
        String query = "SELECT * FROM grid WHERE numero_grille = " + puzzleNumber;
        try (Statement stmt = database.connecterBD().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                int height = rs.getInt("hauteur");
                int width = rs.getInt("largeur");
                crossword = new Crossword(height, width);

                try (ResultSet rs2 = stmt.executeQuery("SELECT * FROM crossword WHERE numero_grille = " + puzzleNumber)) {
                    while (rs2.next()) {
                        String definition = rs2.getString("definition");
                        boolean horizontal = rs2.getBoolean("horizontal");
                        int row = rs2.getInt("ligne");
                        int col = rs2.getInt("colonne");
                        String solution = rs2.getString("solution");
                        crossword.setDefinition(row, col, horizontal, definition);
                        for (int i = 0; i < solution.length(); i++) {
                            if (horizontal) {
                                crossword.setSolution(row, col + i, solution.toUpperCase().charAt(i));
                            } else {
                                crossword.setSolution(row + i, col, solution.toUpperCase().charAt(i));
                            }
                        }
                    }
                }
            }
        }
        return crossword;
    }

   

    public void printProposition() {
        for (int row = 0; row < getHeight(); row++) {
            for (int column = 0; column < getWidth(); column++) {
                char proposition = getProposition(row, column);
                System.out.print(proposition + " ");
            }
            System.out.println();
        }
    }


    public void printSolution() {
        for (int row = 0; row < getHeight(); row++) {
            for (int column = 0; column < getWidth(); column++) {
                char solution = getSolution(row, column);
                System.out.print(solution + " ");
            }
            System.out.println();
        }
    }

  
 
    public boolean correctCoords(int row, int column) {
        return newGrid.correctCoords(row, column);
    }
 
    public boolean isBlackSquare(int row, int column) {
        return newGrid.getCell(row, column) == null;
    }
 
    public void setBlackSquare(int row, int column, boolean black) {
        if (black) {
            newGrid.setCell(row, column, null);
        } else {
            newGrid.setCell(row, column, new CrosswordSquare());
        }
    }
 
    public char getSolution(int row, int column) {
        if (!correctCoords(row, column) || isBlackSquare(row, column)) {
            throw new IllegalArgumentException("Les coordonnées ne sont pas valides");
        }
        return newGrid.getCell(row, column).getSolution();
    }
 
    public void setSolution(int row, int col, char sol) {
        if (!correctCoords(row, col) || isBlackSquare(row, col)) {
            throw new IllegalArgumentException("Les coordonnées ne sont pas valides");
        }
        newGrid.getCell(row, col).setSolution(sol);
    }
 
    public char getProposition(int row, int column) {
        if (!correctCoords(row, column) || isBlackSquare(row, column)) {
            throw new IllegalArgumentException("Les coordonnées ne sont pas valides");
        }
        return newGrid.getCell(row, column).getProposition();
    }
 
    public void setProposition(int row, int column, char prop) {
        if (!correctCoords(row, column) || isBlackSquare(row, column)) {
            throw new IllegalArgumentException("Les coordonnées ne sont pas valides");
        }
        newGrid.getCell(row, column).setProposition(prop);
    }
 
    public String getDefinition(int row, int column, boolean horiz) {
        if (!correctCoords(row, column) || isBlackSquare(row, column)) {
            throw new IllegalArgumentException("Les coordonnées ne sont pas valides");
        }
        return horiz ? newGrid.getCell(row, column).getHorizontal()
                     : newGrid.getCell(row, column).getVertical();
    }
 
    public void setDefinition(int row, int column, boolean horiz, String def) {
        if (!correctCoords(row, column) || isBlackSquare(row, column)) {
            throw new IllegalArgumentException("Les coordonnées ne sont pas valides");
        }
        if (horiz) {
            newGrid.getCell(row, column).setHorizontal(def);
        } else {
            newGrid.getCell(row, column).setVertical(def);
        }
    }
}
