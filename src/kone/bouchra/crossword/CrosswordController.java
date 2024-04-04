package kone.bouchra.crossword;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.scene.layout.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.animation.ScaleTransition;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;




public class CrosswordController implements Initializable  {
	
    @FXML
    private GridPane crosswordGrid;
    @FXML
    private ListView<String> listeIndiceHorizontal;
    @FXML
    private ListView<String> listeIndiceVertical;
    private int row;
    private int column;
    @FXML
    private AnchorPane anchorPane;
    

    

    @FXML
    private void selectHorizontal() {
        isHorizontal = true;
    }

    @FXML
    private void selectVertical() {
        isHorizontal = false;
    }
    

    private Label currentCell;
    private boolean isHorizontal;
   private Map<String, List<Integer>> horizontalCluesMap = new HashMap<>();
   private Map<String, List<Integer>> verticalCluesMap = new HashMap<>();


   

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Database database = new Database();
            Crossword  crossword = Crossword.createPuzzle(database, Main.choix + 1);

           this.configGrille(crossword);
//
//            // Configuration de la liste View
           this.configListView(crossword);
           
           advanceCursor(crossword, null, column, column);
           afficherLettre(crossword);
           indiceD(null, crossword);
           selectionnerIndice(crossword);
//            crossword.getCell(1, 1).requestFocus();
//
//            // Current direction configuration
//            configureCurrentDirection(crossword);
           majCouleurIndice(listeIndiceHorizontal);
           majCouleurIndice(listeIndiceVertical);
           configCDirection(crossword);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
//    public void initialize() {
//        // Exemple de remplissage de la grille avec des Labels
//        for (int row = 0; row < 5; row++) {
//            for (int col = 0; col < 5; col++) {
//                Label label = new Label(" ");
//                label.setMinSize(50, 50);
//                label.setMaxSize(50, 50);
//                label.setPrefSize(50, 50);
//                label.setOnMouseClicked(this::handleCellClick);
//                crosswordGrid.add(label, col, row);
//            }
//        }
//
//        // Exemple d'initialisation des indices
//        horizontalCluesMap.put("Indice 1", Arrays.asList(0, 1, 2));
//        horizontalCluesMap.put("Indice 2", Arrays.asList(3, 4));
//        verticalCluesMap.put("Indice 3", Arrays.asList(0, 1, 2));
//        verticalCluesMap.put("Indice 4", Arrays.asList(3, 4));
//
//        // Remplissage des listes d'indices
//        horizontalClues.setItems(FXCollections.observableArrayList(horizontalCluesMap.keySet()));
//        verticalClues.setItems(FXCollections.observableArrayList(verticalCluesMap.keySet()));
//    }
    
    
    
    private void configGrille(Crossword crossword){
    	crosswordGrid = new GridPane();
    	crosswordGrid.setPrefHeight(crossword.getHeight());
    	crosswordGrid.setPrefWidth(crossword.getWidth());

        for (int i = 0; i < crossword.getHeight(); i++) {
        	crosswordGrid.getRowConstraints().add(new RowConstraints(30));
        }

        for (int j = 0; j < crossword.getWidth(); j++) {
        	crosswordGrid.getColumnConstraints().add(new ColumnConstraints(30));
        }

        crosswordGrid.setGridLinesVisible(true);

        for (int i = 0; i < crossword.getHeight(); i++) {
            for (int j = 0; j < crossword.getWidth(); j++) {
                CrosswordSquare square = crossword.getCell(i+1, j+1);
                if(crossword.isBlackSquare(i+1, j+1)){
                    square.setStyle("-fx-background-color: black");
                }

                crosswordGrid.add(square, j, i);
            }
        }

        // Set the anchor constraints to center the content
        AnchorPane.setTopAnchor(crosswordGrid, 30.0);
        AnchorPane.setBottomAnchor(crosswordGrid, 50.0);
        AnchorPane.setLeftAnchor(crosswordGrid, 100.0);
        AnchorPane.setRightAnchor(crosswordGrid, 50.0);

        anchorPane.getChildren().add(crosswordGrid);
    }
    
    
    

    private void configListView(Crossword crossword){
        // Ajouter les indices horizontaux
        for (Clue element : crossword.getHorizontalClues()) {
        	listeIndiceHorizontal.getItems().add(element.getClue() + " ("+ element.getRow() + "," + element.getColumn() + ")");
        }

        // Ajouter les indices verticaux
        for (Clue element : crossword.getVerticalClues()) {
        	listeIndiceVertical.getItems().add(element.getClue() + " ("+ element.getRow() + "," + element.getColumn() + ")");
        }
    }
    
    private void advanceCursor(Crossword crossword, KeyCode keyCode, int row, int column) {
        int width = crossword.getWidth();
        int height = crossword.getHeight();

        // Méthode pour gérer le mouvement horizontal
        BiConsumer<Integer, Integer> moveHorizontal = (newRow, newColumn) -> {
            if (newColumn == 1) {
                if (newRow == 1) {
                    newColumn = width;
                    newRow = height;
                } else {
                    newRow--;
                    newColumn = width;
                }
            } else if (newColumn == width) {
                newRow++;
                newColumn = 1;
            } else {
                newColumn++;
            }
            crossword.setHorizontalDirection(true);
        };

        // Méthode pour gérer le mouvement vertical
        BiConsumer<Integer, Integer> moveVertical = (newRow, newColumn) -> {
            if (newRow == height) {
                newRow = 1;
            } else {
                newRow++;
            }
            crossword.setHorizontalDirection(false);
        };

        // Appliquer le mouvement en fonction du KeyCode
        switch (keyCode) {
            case LEFT:
                moveHorizontal.accept(row - 1, column);
                break;
            case RIGHT:
                moveHorizontal.accept(row, column + 1);
                break;
            case DOWN:
                moveVertical.accept(row + 1, column);
                break;
            case UP:
                moveVertical.accept(row - 1, column);
                break;
            default:
                return;
        }

        // Ajuster pour les cases noires
        while (crossword.isBlackSquare(row, column)) {
            // Logique similaire pour ajuster les cases noires
        	 switch (keyCode) {
             case LEFT:
                 if (column == 1 && row == 1) {
                	 column = width;
                     row = height;
                 } else if (column == 0) {
                     row--;
                     column = width - 1;
                 } else {
                	 column--;
                 }
                 break;

             case RIGHT:
                 if (column == width && row == height) {
                	 column = 1;
                     row = 1;
                 } else if (column == width) {
                     row++;
                     column = 1;
                 } else {
                	 column++;
                 }
                 break;

             case DOWN:
                 if (row == height) {
                     row = 1;
                 } else {
                     row++;
                 }
                 break;

             case UP:
                 if (row == 1) {
                     row = height;
                 } else {
                     row--;
                 }
                 break;
         }
        }

        // Mettre à jour le style et le focus du TextField
        CrosswordSquare label = crossword.getCell(row, column);
        label.requestFocus();
    }
    
    private void configCDirection(Crossword crossword){
        for (int i = 1; i <= crossword.getHeight(); i++) {
            for (int j = 1; j <= crossword.getWidth(); j++) {
                CrosswordSquare square = crossword.getCell(i, j);
                square.setOnKeyReleased((e) -> {
                    releaseKey(e, crossword);
                });
                int finalI = i;
                int finalJ = j;
                square.getProposition().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        if(crossword.isHorizontalDirection() && finalJ<= crossword.getWidth() && finalJ + 1 <= crossword.getWidth() && !crossword.isBlackSquare(finalI , finalJ+1)){
                            crossword.getCell(finalI, finalJ +1).requestFocus();
                        } else if(!crossword.isHorizontalDirection() && finalI<= crossword.getHeight() && finalI +1 <= crossword.getHeight() && !crossword.isBlackSquare(finalI+1, finalJ)) {
                            crossword.getCell(finalI + 1, finalJ).requestFocus();
                        }
                    }
                });

            }
        }
    }

    @FXML
    public void releaseKey(KeyEvent event, Crossword crossword) {
        Label label = (Label) event.getSource();
        int row = ((int) label.getProperties().get("gridpane-row")) + 1;
        int col = ((int) label.getProperties().get("gridpane-column")) + 1;

        KeyCode eventKC = event.getCode();
        switch (eventKC) {
            case ENTER:
                displayCorrectLetters(crossword);
                System.out.println("Case correct");
                break;
            case DOWN:
                advanceCursor(crossword, KeyCode.DOWN, row, col);
                break;
            case UP:
                advanceCursor(crossword, KeyCode.UP, row, col);
                break;
            case LEFT:
                advanceCursor(crossword, KeyCode.LEFT, row, col);
                break;
            case BACK_SPACE:
                label.setText("");
                advanceCursor(crossword, KeyCode.LEFT, row, col);
                break;
            case RIGHT:
                advanceCursor(crossword, KeyCode.RIGHT, row, col);
                break;
            case TAB:
                break;
            default:
                break;
        }
    }

    private void displayCorrectLetters(Crossword crossword) {
        for (int i = 0; i < crossword.getHeight(); i++) {
            for (int j = 0; j < crossword.getWidth(); j++) {
                CrosswordSquare square = crossword.getCell(i + 1, j + 1);
                if (!crossword.isBlackSquare(i + 1, j + 1)) {
                    Optional<String> proposition = square.getProposition();
                    if (proposition.isPresent() && !proposition.get().isEmpty()) {
                        char firstChar = proposition.get().charAt(0);
                        if (square.getSolution() == firstChar) {
                            square.setStyle("-fx-background-color: green; -fx-border-color: black; -fx-border-width: 0.5;");
                        }
                    }
                }
            }
        }
    }

   


    private void handleCellClick(MouseEvent event) {
        Label clickedCell = (Label) event.getSource();
        if (currentCell != null) {
            currentCell.setStyle("-fx-border-color: transparent;");
        }
        currentCell = clickedCell;
        currentCell.setStyle("-fx-border-color: blue; -fx-border-width: 2;");

        // Mise à jour des indices sélectionnés
        int row = GridPane.getRowIndex(clickedCell);
        int col = GridPane.getColumnIndex(clickedCell);

        if (isHorizontal) {
            // Mettre à jour les indices horizontaux
        	listeIndiceHorizontal.getSelectionModel().clearSelection();
            for (Map.Entry<String, List<Integer>> entry : horizontalCluesMap.entrySet()) {
                if (entry.getValue().contains(col)) {
                	listeIndiceHorizontal.getSelectionModel().select(entry.getKey());
                }
            }
        } else {
            // Mettre à jour les indices verticaux
        	listeIndiceVertical.getSelectionModel().clearSelection();
            for (Map.Entry<String, List<Integer>> entry : verticalCluesMap.entrySet()) {
                if (entry.getValue().contains(row)) {
                	listeIndiceVertical.getSelectionModel().select(entry.getKey());
                }
            }
        }
    }

    
//modifier la couleur d'arrière-plan des éléments sélectionnés
    private void majCouleurIndice(ListView<String> list) {
        // Utilise un Callback pour personnaliser l'apparence des cellules
        list.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        // Met à jour le texte de la cellule
                        setText(item);

                        // Gestionnaire d'événements pour changer la couleur d'arrière-plan lors du clic
                        setOnMouseClicked(event -> {
                            if (isSelected()) {
                                // Change la couleur d'arrière-plan en rouge lorsqu'un élément est sélectionné
                                setStyle("-fx-background-color: red;");
                            } else {
                                // Réinitialise la couleur d'arrière-plan
                                setStyle("");
                            }
                        });
                    }
                };
            }
        });
    }
    
    private void selectionnerIndice(Crossword crossword){
        for (int i = 1; i <= crossword.getHeight(); i++) {
            for (int j = 1; j <= crossword.getWidth(); j++) {
                CrosswordSquare square = crossword.getCell(i, j);
                int i1 = i;
                int j2 = j;
                square.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if(newValue) {
                        String liste = listeIndiceHorizontal.getItems().get(i1-1);
                        listeIndiceHorizontal.scrollTo(i1-1);
                        listeIndiceHorizontal.getSelectionModel().select(i1-1);
                        listeIndiceVertical.scrollTo(j2-1);
                        listeIndiceVertical.getSelectionModel().select(j2-1);
                        this.indiceD(liste, crossword);
                    }
                });
            }
        }
    }
    
    private void indiceD(String clue, Crossword crossword){
        for (Clue element : crossword.getHorizontalClues()) {
            if(!clue.startsWith(clue) && element.getRow() == 1){
                System.out.println("lig: " + 1 + " "+ element.getClue());
            }
        }
    }
    
    private void afficherLettre(Crossword crossword){
        for (int i = 0; i < crossword.getHeight(); i++) {
            for (int j = 0; j < crossword.getWidth(); j++) {
                CrosswordSquare crosswordSquare = crossword.getCell(i+1, j+1);
                crosswordSquare.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        ScaleTransition st = new ScaleTransition(Duration.millis(500));
                        st.setFromX(0);
                        st.setToX(1);
                        st.setFromY(0);
                        st.setToY(1);
                        st.setNode(crosswordSquare);
                        st.play();
                    }
                });
            }
        }
    }

}
