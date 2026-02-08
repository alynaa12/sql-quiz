package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.User;

/**
 * Main menu view displayed after a succesfull login
 * this view provides navigation options to start the quiz,
 * view the highscore,
 * manage questions and
 * view the credits
 * the available options are shown independently of the user role,while the role itself is displayed for transparency
 */
public class MenuView {

    private final VBox root = new VBox();

    /**
     * creates the main menu view for the given user
     * @param stage the main application stage
     * @param user the currently logged-in user
     */

    public MenuView(Stage stage, User user) {
        root.setPadding(new Insets(16));
        root.setSpacing(10);

        Label hello = new Label("Hallo " + user.getUsername() + " (" + user.getRole() + ")");

        Button startQuizButton = new Button("Quiz starten");
        startQuizButton.setOnAction(e -> {
            QuizView quizView = new QuizView(stage, user);
            stage.getScene().setRoot(quizView.getRoot());
            stage.setTitle("SQL Quiz - Quiz");
        });

        Button showHighscoreButton = new Button("Highscore anzeigen");
        showHighscoreButton.setOnAction(e -> {
            HighscoreView highscoreView = new HighscoreView(stage, user);
            stage.getScene().setRoot(highscoreView.getRoot());
            stage.setTitle("SQL Quiz - Highscore");
        });
        Button creditsButton = new Button("Credits");
        creditsButton.setOnAction(e -> {
            CreditsView creditsView = new CreditsView(stage, user);
            stage.getScene().setRoot(creditsView.getRoot());
            stage.setTitle("SQL Quiz - Credits");
        });




        root.getChildren().addAll(hello, startQuizButton, showHighscoreButton, creditsButton);
        Button manageQuestionsButton = new Button("Fragen verwalten");
        root.getChildren().add(manageQuestionsButton);
        manageQuestionsButton.setOnAction(e -> {
            QuestionManagementView view = new QuestionManagementView(stage, user);
            stage.getScene().setRoot(view.getRoot());
            stage.setTitle("SQL Quiz - Fragen verwalten");
        });



    }

    /**
     * returns the root node of this view
     * @return JavaFX root node
     */



    public Parent getRoot() {

        return root;
    }
}

