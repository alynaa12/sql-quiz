package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Login screen (UI only). Later this will call AuthController.
 */
public class LoginView {

    private final VBox root = new VBox();

    public LoginView(Stage stage) {
        root.setPadding(new Insets(16));
        root.setSpacing(10);

        Label title = new Label("Login");
        Label info = new Label("Bitte Username eingeben (z.B. Zena oder Alina):");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        Label messageLabel = new Label();
        Button loginButton = new Button("Login");

        // Platzhalter: erst mal nur anzeigen, dass Button klickbar ist
        loginButton.setOnAction(e -> messageLabel.setText("Login geklickt: " + usernameField.getText()));

        root.getChildren().addAll(title, info, usernameField, loginButton, messageLabel);
    }

    public Parent getRoot() {
        return root;
    }
}

