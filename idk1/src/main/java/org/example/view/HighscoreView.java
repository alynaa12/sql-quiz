package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.User;
import org.example.persistence.UserRepository;

/**
 * Shows the current highscore of the logged-in user.
 */
public class HighscoreView {

    private final VBox root = new VBox(10);

    public HighscoreView(Stage stage, User user) {
        root.setPadding(new Insets(16));

        Label title = new Label("Highscore");
        Label nameLabel = new Label("User: " + user.getUsername());

        // Read highscore from repository (simple and OK, but we can also route via controller later)
        UserRepository userRepository = new UserRepository();
        String highscore = userRepository.getUserHighscore(user.getUsername());

        Label scoreLabel = new Label("Highscore: " + highscore);

        Button backButton = new Button("Zurück zum Menü");
        backButton.setOnAction(e -> {
            MenuView menuView = new MenuView(stage, user);
            stage.getScene().setRoot(menuView.getRoot());
            stage.setTitle("SQL Quiz - Menü");
        });

        root.getChildren().addAll(title, nameLabel, scoreLabel, backButton);
    }

    public Parent getRoot() {
        return root;
    }
}

