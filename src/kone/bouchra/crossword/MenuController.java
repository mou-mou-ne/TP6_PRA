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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuController implements Initializable {

    @FXML
    private ChoiceBox<String> listeGrille;

    @FXML
    private Button play;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAvailableGrids();
    }

    private void loadAvailableGrids() {
        Database db = new Database();
        Map<Integer, String> grids = db.availableGrids();
        ObservableList<String> availableGrids = FXCollections.observableArrayList(grids.values());
        listeGrille.getItems().addAll(availableGrids);
    }

    @FXML
    public void play(ActionEvent e) {
        Main.choix = listeGrille.getSelectionModel().getSelectedIndex();
        int[] dimension = getDimension(listeGrille.getSelectionModel().getSelectedItem());
        int height = dimension[0];
        int width = dimension[1];
        playGame(e, "crossword.fxml", height, width);
    }

    private void playGame (ActionEvent e, String urlFXML, int height, int width) {
        try {
            FXMLLoader loader = new FXMLLoader(MenuController.class.getResource(urlFXML));
            Parent root = loader.load();
            Scene scene = creerScene(root, height, width);
            configureScene(scene, e);
            showScene(scene, e);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Scene creerScene(Parent root, int height, int width) {
        if (height == 15 && width == 15) {
            return new Scene(root, 750, 500);
        } else {
            return new Scene(root);
        }
    }

    private void configureScene(Scene scene, ActionEvent e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        KeyCombination closeCombination = new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN);
        scene.setOnKeyPressed(event -> {
            if (closeCombination.match(event)) {
                stage.close();
            }
        });
    }

    private void showScene(Scene scene, ActionEvent e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private int[] getDimension(String texte) {
        Pattern pattern = Pattern.compile("(\\d+)x(\\d+)");
        Matcher matcher = pattern.matcher(texte);
        if (!matcher.find()) {
            System.out.println("Le format du texte est incorrect.");
            return new int[]{};
        }
        return new int[]{Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))};
    }
}
