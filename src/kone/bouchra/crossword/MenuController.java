package kone.bouchra.crossword;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private ChoiceBox<String> listeGrille;

    @FXML
    private Button play;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Database db = new Database();
        Map<Integer, String> grids = db.availableGrids();
        ObservableList<String> availableGrids = FXCollections.observableArrayList(grids.values());
		listeGrille.getItems().addAll(availableGrids);
    }

    public void play(ActionEvent e) {
    	Main.choix = listeGrille.getSelectionModel().getSelectedIndex();
        System.out.println("Selected grid index: " + listeGrille.getSelectionModel().getSelectedIndex());
        playGame(e, "grid.fxml");
    }

    public void playGame(ActionEvent e, String urlFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(urlFXML));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            
            System.err.println("Error loading scene: " + ex.getMessage());
        }
    }
}
