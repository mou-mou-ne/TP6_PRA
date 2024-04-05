package kone.bouchra.crossword;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.animation.ScaleTransition;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.*;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import javafx.beans.property.StringProperty;

public class CrosswordController implements Initializable {
    
    @FXML
    private GridPane crosswordGrid;
    @FXML
    private ListView<String> listeIndiceHorizontal;
    @FXML
    private ListView<String> listeIndiceVertical;
    private boolean isHorizontal;
    private Label currentCell;
    
    
    @FXML
    private AnchorPane anchorPane;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Database database = new Database();
            Crossword crossword = Crossword.createPuzzle(database, Main.choix + 1);

            configGrille(crossword);
            configListView(crossword);
            advanceCursor(crossword, null, 1, 1);
            afficherLettre(crossword);
            indiceD(null, crossword);
            selectionnerIndice(crossword);
            majCouleurIndice(listeIndiceHorizontal);
            majCouleurIndice(listeIndiceVertical);
            configCDirection(crossword);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    private void configGrille(Crossword crossword){
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

        AnchorPane.setTopAnchor(crosswordGrid, 30.0);
        AnchorPane.setBottomAnchor(crosswordGrid, 50.0);
        AnchorPane.setLeftAnchor(crosswordGrid, 100.0);
        AnchorPane.setRightAnchor(crosswordGrid, 50.0);

        anchorPane.getChildren().add(crosswordGrid);
    }
    
    private void configListView(Crossword crossword){
        for (Clue element : crossword.getHorizontalClues()) {
            listeIndiceHorizontal.getItems().add(element.getClue() + " ("+ element.getRow() + "," + element.getColumn() + ")");
        }

        for (Clue element : crossword.getVerticalClues()) {
            listeIndiceVertical.getItems().add(element.getClue() + " ("+ element.getRow() + "," + element.getColumn() + ")");
        }
    }
    
    private void advanceCursor(Crossword crossword, KeyCode keyCode, int row, int column) {
        int height = crossword.getHeight();
        int width = crossword.getWidth();

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

        BiConsumer<Integer, Integer> moveVertical = (newRow, newColumn) -> {
            if (newRow == height) {
                newRow = 1;
            } else {
                newRow++;
            }
            crossword.setHorizontalDirection(false);
        };

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

        while (crossword.isBlackSquare(row, column)) {
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
                square.getProp().addListener((observable, oldValue, newValue) -> {
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
                afficherCorrectLettre(crossword);
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

    private void afficherCorrectLettre(Crossword crossword){
        for (int i = 0; i < crossword.getHeight(); i++) {
            for (int j = 0; j < crossword.getWidth(); j++) {
                CrosswordSquare square = crossword.getCell(i+1, j+1);
                if(!crossword.isBlackSquare(i+1, j+1) && square.getSolution() == square.getProp().get().charAt(0)){
                    square.setStyle("-fx-background-color: green; -fx-border-color: black; -fx-border-width: 0.5;");
                }
            }
        }
    }

    private void majCouleurIndice(ListView<String> list) {
        list.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item);

                        setOnMouseClicked(event -> {
                            if (isSelected()) {
                                setStyle("-fx-background-color: red;");
                            } else {
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
                CrosswordSquare crosswordSquare = crossword.getCell(i, j);
                int i1 = i;
                int j2 = j;
                crosswordSquare.focusedProperty().addListener((observable, oldValue, newValue) -> {
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
            if(element.getClue().startsWith(clue) && element.getRow() == 1){
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
