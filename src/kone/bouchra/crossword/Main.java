package kone.bouchra.crossword;
	
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;



public class Main extends Application {
	 

	@Override
	public void start(Stage stage) throws Exception {
        // Charger le fichier FXML
        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Scene scene = new Scene(root);
        // Définir le titre de la fenêtre
        stage.setTitle("Jeu de mots croisés");
        stage.setResizable(false);
     // Définir la scène sur le stage
        stage.setScene(scene);
        
        KeyCombination closeCombination = new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN);
        scene.setOnKeyPressed(event -> {
            if (closeCombination.match(event)) {
                stage.close();
            }
        });
        // Afficher la fenêtre
        stage.show();
    }
	public static void main(String[] args) {
		launch(args);
	}
	
	 public static int choix;
}
