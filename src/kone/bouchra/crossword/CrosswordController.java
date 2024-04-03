package kone.bouchra.crossword;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;



public class CrosswordController implements Initializable  {
    @FXML
    private GridPane crosswordGrid;
    @FXML
    private ListView<String> horizontalClues;
    @FXML
    private ListView<String> verticalClues;

    private Label currentCell;
    private boolean isHorizontal;
    private Map<String, List<Integer>> horizontalCluesMap = new HashMap<>();
    private Map<String, List<Integer>> verticalCluesMap = new HashMap<>();

    
    

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Database database = new Database();
            Crossword  crossword = Crossword.createPuzzle(database, Main.choix + 1);

//            // Configure grids
//            this.configureGrids(crossword);
//
//            // Configure listView
//            configureListView(crossword);
//
//            crossword.getCell(1, 1).requestFocus();
//
//            // Current direction configuration
//            configureCurrentDirection(crossword);

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
            horizontalClues.getSelectionModel().clearSelection();
            for (Map.Entry<String, List<Integer>> entry : horizontalCluesMap.entrySet()) {
                if (entry.getValue().contains(col)) {
                    horizontalClues.getSelectionModel().select(entry.getKey());
                }
            }
        } else {
            // Mettre à jour les indices verticaux
            verticalClues.getSelectionModel().clearSelection();
            for (Map.Entry<String, List<Integer>> entry : verticalCluesMap.entrySet()) {
                if (entry.getValue().contains(row)) {
                    verticalClues.getSelectionModel().select(entry.getKey());
                }
            }
        }
    }

    @FXML
    private void selectHorizontal() {
        isHorizontal = true;
    }

    @FXML
    private void selectVertical() {
        isHorizontal = false;
    }
}
