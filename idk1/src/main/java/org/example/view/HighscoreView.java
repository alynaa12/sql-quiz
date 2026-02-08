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
 * view displaying the highscore of the currently logged-in user
 * this screen reads the users highscore from the peristence layerand presents it in a simple and readable format
 * it contains no application logic and only handles UI rendering
 */
public class HighscoreView {

    private final VBox root = new VBox(10);

    /**
     * creates the highscore view
     * @param stage the main application stage
     * @param user the currently logged-in user
     */
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

    /**
     * returns the root node of this view
     * @return JavaFX root node
     */

    public Parent getRoot() {
        return root;
    }
}

