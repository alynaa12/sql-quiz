package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.User;

/**
 * view displaying the credits of the application.
 * this screen shows the names of the project contributors and provides a button to return to the main menu
 * it contains no application logic and only handles UI rendering
 */
public class CreditsView {
/** root layout of the credits view */
    private final VBox root = new VBox(16);

    /**
     * creates the credit view
     * @param stage the main application stage
     * @param user the currently logged-in user
     */
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

    /**
     * returns the root node of the view
     * @return JavaFX root node
     */

    public Parent getRoot() {
        return root;
    }
}

