package kone.bouchra.crossword;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	  public static int choice;

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
        // Afficher la fenêtre
        stage.show();
    }
	public static void main(String[] args) {
		launch(args);
	}
}
