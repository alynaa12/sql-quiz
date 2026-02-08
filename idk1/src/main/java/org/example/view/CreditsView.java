package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.User;

public class CreditsView {

    private final VBox root = new VBox(16);

    public CreditsView(Stage stage, User user) {
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Credits");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label names = new Label("Zena Edres\nAlina Ali");
        names.setStyle("-fx-font-size: 16px;");

        Button backButton = new Button("Zurück zum Menü");
        backButton.setOnAction(e -> {
            MenuView menuView = new MenuView(stage, user);
            stage.getScene().setRoot(menuView.getRoot());
            stage.setTitle("SQL Quiz - Menü");
        });

        root.getChildren().addAll(title, names, backButton);
    }

    public Parent getRoot() {
        return root;
    }
}

