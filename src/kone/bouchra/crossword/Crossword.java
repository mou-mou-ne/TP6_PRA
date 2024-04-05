package kone.bouchra.crossword;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Crossword extends Grid<CrosswordSquare> {
    private boolean isHorizontalDirection;
    private ObservableList<Clue> verticalClues;
    private ObservableList<Clue> horizontalClues;

    Crossword(int height, int width) {
        super(height, width);
      
        verticalClues = FXCollections.observableArrayList();
        horizontalClues = FXCollections.observableArrayList();
        initialiseGrid();
    }

    private void initialiseGrid() {
        for (int row = 1; row <= getHeight(); row++) {
            for (int col = 1; col <= getWidth(); col++) {
                CrosswordSquare square = CrosswordSquare.createDefault();
                setCell(row, col, square);
                setBlackSquare(row, col, true);
            }
        }
    }

    public ObservableList<Clue> getVerticalClues() {
        return verticalClues;
    }

    public ObservableList<Clue> getHorizontalClues() {
        return horizontalClues;
    }

    public static Crossword createPuzzle(Database database, int puzzleNumber) {
        Crossword crossword = null;
        String queryGrid = "SELECT hauteur, largeur FROM grid WHERE numero_grille = ?";
        String queryCrossword = "SELECT definition, horizontal, ligne, colonne, solution FROM crossword WHERE numero_grille = ?";

        try (Connection conn = database.connecterBD();
             PreparedStatement stmtGrid = conn.prepareStatement(queryGrid);
             PreparedStatement stmtCrossword = conn.prepareStatement(queryCrossword)) {

            stmtGrid.setInt(1, puzzleNumber);
            try (ResultSet rsGrid = stmtGrid.executeQuery()) {
                if (rsGrid.next()) {
                    int height = rsGrid.getInt("hauteur");
                    int width = rsGrid.getInt("largeur");
                    crossword = new Crossword(height, width);

                    stmtCrossword.setInt(1, puzzleNumber);
                    try (ResultSet rsCrossword = stmtCrossword.executeQuery()) {
                        while (rsCrossword.next()) {
                            String definition = rsCrossword.getString("definition");
                            boolean horizontal = rsCrossword.getBoolean("horizontal");
                            int row = rsCrossword.getInt("ligne");
                            int col = rsCrossword.getInt("colonne");
                            String solution = rsCrossword.getString("solution");
                            crossword.setDefinition(row, col, horizontal, definition);
                            for (int i = 0; i < solution.length(); i++) {
                                if (horizontal) {
                                    crossword.setSolution(row, col + i, solution.toUpperCase().charAt(i));
                                } else {
                                    crossword.setSolution(row + i, col, solution.toUpperCase().charAt(i));
                                }
                            }
                        }
                        for (int i = 1; i <= crossword.getHeight(); i++) {
                            for (int j = 1; j <= crossword.getWidth(); j++) {
                                if(crossword.getCell(i, j).getSolution() == ' '){
                                    crossword.setBlackSquare(i, j, true);
                                    System.out.println(crossword.getCell(i, j).getSolution());
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to create crossword puzzle: " + e.getMessage());
            e.printStackTrace();
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
        return row >= 1 && row <= getHeight() && column >= 1 && column <= getWidth();
    }

    public boolean isBlackSquare(int row, int column) {
        return !correctCoords(row, column) || getCell(row, column).getSolution() == ' ';
    }

    public void setBlackSquare(int row, int column, boolean black) {
        if (correctCoords(row, column)) {
            if(black){
                getCell(row, column).setSolution(' ');
            } else {
                getCell(row, column).setSolution(null);
            }
        } else {
            throw new IllegalArgumentException("Invalid coordinates: row=" + row + ", column=" + column);
        }
    }

    public char getSolution(int row, int column) {
        if (!isBlackSquare(row, column)) {
            return getCell(row, column).getSolution();
        } else {
            throw new IllegalArgumentException("Invalid coordinates for solution: row=" + row + ", column=" + column);
        }
    }

    public void setSolution(int row, int column, char solution) {
        if (!isBlackSquare(row, column)) {
            getCell(row, column).setSolution(solution);
        } else {
            throw new IllegalArgumentException("Invalid coordinates for solution: row=" + row + ", column=" + column);
        }
    }

    public char getProposition(int row, int column) {
        if (!isBlackSquare(row, column)) {
            Character cellValue = getCell(row, column).getProp().getValue().charAt(0);
            if (cellValue != null) {
                return cellValue;
            } else {
                throw new IllegalArgumentException("Cell content is not a character");
            }
        } else {
            throw new IllegalArgumentException("Invalid coordinates for proposition: row=" + row + ", column=" + column);
        }
    }

    public void setProposition(int row, int column, String proposition) {
        if (!isBlackSquare(row, column)) {
            getCell(row, column).setProp(proposition);
        } else {
            throw new IllegalArgumentException("Invalid coordinates for proposition: row=" + row + ", column=" + column);
        }
    }

    public String getDefinition(int row, int column, boolean horiz) {
        if (!isBlackSquare(row, column)) {
            return horiz ? getCell(row, column).getHorizontal() : getCell(row, column).getVertical();
        } else {
            throw new IllegalArgumentException("Invalid coordinates for definition: row=" + row + ", column=" + column);
        }
    }

    public void setDefinition(int row, int column, boolean horizontal, String def) {
        if (!isBlackSquare(row, column)) {
            if (horizontal) {
                getCell(row, column).setHorizontal(def);
            } else {
                getCell(row, column).setVertical(def);
            }
        } else {
            throw new IllegalArgumentException("Invalid coordinates for definition: row=" + row + ", column=" + column);
        }
    }

    public boolean isHorizontalDirection() {
        return this.isHorizontalDirection;
    }

    public void setHorizontalDirection(boolean isHorizontalDirection) {
        this.isHorizontalDirection = isHorizontalDirection;
    }
}
