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
 * login view of the application
 * This view allows a user to enter a username and start a sessions
 * authentication logic is delegated to the {@link AuthController}, keeping this class free of business logic
 */
public class LoginView {
    /** root layout of the login view */

    private final VBox root = new VBox();
    /** controller responsible for authentication logic */
    private final AuthController authController = new AuthController();

    /**
     * creates the login view
     * @param stage the main application stage
     */
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

    /**
     * returns the root node of this view
     * @return JavaFX root node
     */

    public Parent getRoot() {
        return root;
    }
}

