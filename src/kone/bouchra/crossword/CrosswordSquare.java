package kone.bouchra.crossword;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.Cursor;


public class CrosswordSquare extends Label {
    
    private Character solution; // Caractère de la solution pour ce carré
    private StringProperty proposition; // Proposition de caractère pour ce carré
    private String horizontal; // Orientation horizontale du mot
    private String vertical; // Orientation verticale du mot
    private boolean statut; // Statut du carré (peut-être utilisé pour indiquer si le carré est actif ou non)
    
    // Constructeur pour initialiser les propriétés du carré
    public CrosswordSquare(char solution, String proposition, String horizontal, String vertical, boolean statut) {
        this.solution = solution;
        this.proposition = new SimpleStringProperty(proposition);
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.statut = statut;
        initializeProperties(); // Appel de la méthode pour définir les propriétés par défaut
        setupEventHandlers(); // Appel de la méthode pour gérer les événements
    }

    // Getters et setters pour chaque propriété
    public char getSolution(){
        return solution;
    }

    public void setSolution(char solution){
        this.solution=solution;
    }

    public StringProperty getProposition() {
        return proposition;
    }

    public void setProposition(String proposition) {
        this.proposition.set(proposition);
    }
    
    public String getHorizontal() {
        return horizontal;
    }
    
    public void setHorizontal(String horizontal) {
        this.horizontal=horizontal;
    }
    
    public String getVertical() {
        return vertical;
    }
    
    public void setVertical(String vertical) {
        this.vertical=vertical;
    }
    
    public boolean getStatut() {
        return statut;
    }
    
    public void setStatut(boolean statut) {
        this.statut=statut;
    }
    
//    public StringProperty textProperty() {
//        return text;
//    }
//
//    public String getText() {
//        return text.get();
//    }
//
//    public void setText(String text) {
//        this.text.set(text);
//    }
//    
    
    // Méthode pour définir les propriétés par défaut du carré
    private void initializeProperties(){
        setPrefSize(30, 30); // Définit la taille préférée du carré
        setAlignment(Pos.CENTER); // Centre le texte dans le carré
        setPadding(new Insets(-10, 0, -10, 0)); // Ajoute un padding autour du texte
    }
    
    // Méthode pour gérer les événements sur le carré
    private void setupEventHandlers(){
        // Gère l'événement de clic de souris
        setOnMouseClicked(event -> {
            if(this.solution != '\0') { // Vérifie si le carré a une solution
                requestFocus(); // Demande le focus sur le carré
            }
        });
        
        // Change le curseur en main lorsqu'il entre dans le carré
        setOnMouseEntered(event -> setCursor(Cursor.HAND));

        // Gère le changement de focus
        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && this.solution != '\0' ) { // Si le carré obtient le focus et a une solution
                setStyle("-fx-border-color: blue"); // Change la couleur de la bordure en bleu
            } else if(oldValue && this.solution != '\0') { // Si le carré perd le focus et a une solution
                setStyle("-fx-border-color: NONE;"); // Supprime la couleur de la bordure
            }
        });

        // Gère l'événement de pression de touche
        this.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if(this.solution != '\0'){ // Vérifie si le carré a une solution
                String text = event.getText();
                if (text.matches("[A-Za-z]")) { // Vérifie si la touche pressée est une lettre
                    setText(text.toUpperCase()); // Met le texte en majuscule
                    this.setProposition(String.valueOf(text.toUpperCase().charAt(0))); // Met à jour la proposition
                } else {
                    event.consume(); // Consomme l'événement si la touche n'est pas une lettre
                }
            }
        });
    }
    
    // Méthode pour demander le focus sur le carré
    public void requestFocus() {
        // Implémentez la logique pour demander le focus ici
    }


}
