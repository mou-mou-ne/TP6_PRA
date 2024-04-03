module TP6_KONE_BOUCHRA {
	requires javafx.controls;
	requires javafx.base;
	requires java.sql;
	requires java.desktop;
	requires javafx.fxml;
	requires javafx.graphics;
	
	opens application to javafx.graphics, javafx.fxml;
}
