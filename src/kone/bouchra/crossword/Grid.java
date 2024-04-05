package kone.bouchra.crossword;

public class Grid<T> {
	/**
	 * Constructeur permettant d'obtenir une grille dotée d'un tableau
	 * de dimensions conformes aux valeurs respectives de height et
	 * de width, dont tous les elements contiennent la valeur null.
	 *  @pre height >= 0 et width >= 0
	 */
	
	private int height;
	private int width;
	private T[][] array ;
	
	
	public Grid (int height, int width){
		if (height <0 || width <0 ) {
			 throw new IllegalArgumentException("Height et widht sont inferieur à 0");    
		}
		
		else {
			this.height= height;
			this.width=width;
//			this.array = new String [height][width];
			this.array = (T[][]) new Object[this.height][this.width];
		}
		
		
		
		
	}
 
	//Accesseurs
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
 
	/**
	 * Vérifier la validité des coordonnées
	 * @return true si et seulement si (row, column)
	 * 			désignent une cellule existante de la grille
	 */
	
	public boolean correctCoords(int row, int column) {
		return row >=0 && row < height && column >=0 &&  column < width;
	}
	
	
	/**
	 *
	 * @param row
	 * @param column
	 * @return valeur de la cellule ayant pour coordonnées
	 * (row,column), où (row,column) est compris entre 0 et
	 * getHeight()-1 (resp.getWidth()-1)
	 * @pre correctCoords(row,colum)
	 * @throws IOException
	 */
	public T getCell(int row, int column) {
		
		if (!correctCoords(row, column)) {
			 throw new IllegalArgumentException("Les coordonées ne sont pas valides");    
		}
		
		return this.array[row][column];
	}
	
	
	
	public void setCell (int row, int column, T ch){
		
		if (!correctCoords(row, column)) {
			 throw new IllegalArgumentException("Les coordonées ne sont pas valide");    
		}
	else {
		
		array[row][column] = ch;
		}
	}
	
	
	@Override
	public String toString(){
		
		String str = "";
 
		for (int row = 0; row< getHeight();row++) {
			for (int column = 0 ; column < getWidth(); column++) {
				
		
				str += "" + (row+1) + "," + (column+1);
				if(column!=getWidth()-1) str+="|";
				
				
			}
			str += "\n";
			
		}
		return str;
	}
	
	
	public static void main(String[] args) {
		 Grid myGrid = new Grid(3,5) ;
	     for (int l=1; l<=myGrid.getHeight(); l++)
	     {
	    	 String lineNumber = Integer.toString(l);
		       for (int c=0; c<=myGrid.getWidth(); c++)
		       {
		    	   myGrid.setCell
		    	   (l, c, lineNumber + ',' + Integer.toString(c));
		       }
		}
	
		System.out.println(myGrid) ;
 
	}

}
