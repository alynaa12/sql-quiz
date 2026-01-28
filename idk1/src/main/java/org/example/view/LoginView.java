package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.controller.AuthController;
import org.example.model.User;

/**
 * Login screen (UI only). Calls AuthController for login logic.
 */
public class LoginView {

    private final VBox root = new VBox();
    private final AuthController authController = new AuthController();

    public LoginView(Stage stage) {
        root.setPadding(new Insets(16));
        root.setSpacing(10);

        Label title = new Label("Login");
        Label info = new Label("Bitte Username eingeben :");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        Label messageLabel = new Label();
        Button loginButton = new Button("Login");

        loginButton.setOnAction(e -> {
            User user = authController.login(usernameField.getText());

            if (user == null) {
                messageLabel.setText("User nicht gefunden. Bitte erneut versuchen.");
                return;
            }

            MenuView menuView = new MenuView(stage, user);
            stage.getScene().setRoot(menuView.getRoot());
            stage.setTitle("SQL Quiz - Menü");
        });

        root.getChildren().addAll(title, info, usernameField, loginButton, messageLabel);
    }

    public Parent getRoot() {
        return root;
    }
}

