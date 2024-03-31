package kone.bouchra.crossword;

public class CrosswordV2 extends Grid<CrosswordSquare> {
	
	private Grid<CrosswordSquare> newGrid;
    private int height;
    private int width;
    private String nom;
 
    public CrosswordV2(int height, int width) {
    	   super(height, width);
        this.height = height;
        this.width = width;
        newGrid = new Grid<>(height, width);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                newGrid.setCell(i, j, new CrosswordSquare());
            }
        }
    }
 
    public int getHeight() {
        return height;
    }
 
    public void setHeight(int height) {
        this.height = height;
    }
 
    public int getWidth() {
        return width;
    }
 
    public void setWidth(int width) {
        this.width = width;
    }
 
    public String getNom() {
        return nom;
    }
 
    public void setNom(String nom) {
        this.nom = nom;
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
