package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.User;

/**
 * Main menu after login. Shows different options based on user role.
 */
public class MenuView {

    private final VBox root = new VBox();

    public MenuView(Stage stage, User user) {
        root.setPadding(new Insets(16));
        root.setSpacing(10);

        Label hello = new Label("Hallo " + user.getUsername() + " (" + user.getRole() + ")");

        Button startQuizButton = new Button("Quiz starten");
        Button showHighscoreButton = new Button("Highscore anzeigen");

        root.getChildren().addAll(hello, startQuizButton, showHighscoreButton);
        Button manageQuestionsButton = new Button("Fragen verwalten");
        root.getChildren().add(manageQuestionsButton);
        manageQuestionsButton.setOnAction(e -> {
            QuestionManagementView view = new QuestionManagementView(stage, user);
            stage.getScene().setRoot(view.getRoot());
            stage.setTitle("SQL Quiz - Fragen verwalten");
        });



    }



    public Parent getRoot() {
        return root;
    }
}

