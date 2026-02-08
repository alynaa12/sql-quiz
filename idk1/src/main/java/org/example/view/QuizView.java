package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.controller.QuizController;
import org.example.model.Question;
import org.example.model.User;

/**
 * Quiz view of the application.
 * this view displays questions and answer options and provides immediate feedback (true or false)
 * the user also selects a difficulty rating after each question , which is forwarded to the quiz logic
 * all business logic is delegated to {@link QuizController} to keep the GUI free of core logic
 */
public class QuizView {

    private final VBox root = new VBox(10);

    private final QuizController quizController;
    private Question currentQuestion;

    /** anser buttons for the 4 options */
    private final Label questionLabel = new Label();
    private final Button[] answerButtons = new Button[] {
            new Button(), new Button(), new Button(), new Button()
    };
/** label for correct / incorrect feedback*/
    private final Label feedbackLabel = new Label();
    private final Label pointsLabel = new Label();

/** difficulty selection (leicht,mittel,schwer) */
    private final ComboBox<String> difficultyBox = new ComboBox<>();
    private final Button nextButton = new Button("Nächste Frage");
    private final Button backButton = new Button("Zurück zum Menü");

    private boolean lastAnswerCorrect = false;
    private int lastPointsShown = 0;

    /**
     * creates the quiz view
     * @param stage main application stage
     * @param user currently logged-in user
     */


    public QuizView(Stage stage, User user) {
        this.quizController = new QuizController(user.getUsername());
        lastPointsShown = quizController.getPointsAwardedCount();
        pointsLabel.setText("");

        root.setPadding(new Insets(16));

        Label title = new Label("Quiz");
        Label userLabel = new Label("User: " + user.getUsername());

        // Difficulty
        difficultyBox.getItems().addAll("leicht", "mittel", "schwer");
        difficultyBox.setValue("leicht");

        HBox difficultyRow = new HBox(10, new Label("Schwierigkeit:"), difficultyBox);

        // Answer buttons
        VBox answersBox = new VBox(8);
        for (int i = 0; i < answerButtons.length; i++) {
            int chosenIndex = i;
            answerButtons[i].setMaxWidth(Double.MAX_VALUE);
            answerButtons[i].setOnAction(e -> handleAnswerClick(chosenIndex));
            answersBox.getChildren().add(answerButtons[i]);
        }

        // Buttons bottom
        nextButton.setDisable(true);
        nextButton.setOnAction(e -> {
            quizController.submitDifficulty(lastAnswerCorrect, difficultyBox.getValue());

            int pointsNow = quizController.getPointsAwardedCount();
            if (pointsNow > lastPointsShown) {
                lastPointsShown = pointsNow;
                pointsLabel.setText("🎉 +1 Punkt! Wiederholungsrunde abgeschlossen.");
            }

            loadNextQuestion();
        });


        backButton.setOnAction(e -> {
            MenuView menuView = new MenuView(stage, user);
            stage.getScene().setRoot(menuView.getRoot());
            stage.setTitle("SQL Quiz - Menü");
        });

        HBox bottomButtons = new HBox(10, nextButton, backButton);

        root.getChildren().addAll(
                title,
                userLabel,
                new Separator(),
                questionLabel,
                answersBox,
                feedbackLabel,
                pointsLabel,
                difficultyRow,
                bottomButtons
        );

        // start
        loadNextQuestion();
    }

    /**
     * loads and displays the next questions from the quiz controlle.
     * resets feedback and re-enables answer buttons
     */

    private void loadNextQuestion() {
        feedbackLabel.setText("");



        nextButton.setDisable(true);
        setAnswerButtonsDisabled(false);

        currentQuestion = quizController.nextQuestion();

        questionLabel.setText(currentQuestion.getText());

        // set options on buttons
        for (int i = 0; i < 4; i++) {
            answerButtons[i].setText(currentQuestion.getOptions().get(i));
        }

        // reset difficulty selection to default (optional)
        if (difficultyBox.getValue() == null) {
            difficultyBox.setValue("leicht");
        }
    }

    /**
     * handles a click on an answer button, evaluates the answer, displays feedback and enables the "next" button
     * @param chosenIndex chosen answer index
     */

    private void handleAnswerClick(int chosenIndex) {
        pointsLabel.setText("");
        // evaluate answer through controller
        lastAnswerCorrect = quizController.submitAnswer(chosenIndex);

        if (lastAnswerCorrect) {
            feedbackLabel.setText("✅ Richtig!");
        } else {
            feedbackLabel.setText("❌ Falsch!");
        }

        // after answering: lock answers and allow next
        setAnswerButtonsDisabled(true);
        nextButton.setDisable(false);
    }

    /**
     * enables od disables all answer buttons
     * @param disabled true or disable , false to enable
     */

    private void setAnswerButtonsDisabled(boolean disabled) {
        for (Button b : answerButtons) {
            b.setDisable(disabled);
        }
    }

    /**
     * return the root node of this view
     * @return JavaFX root node
     */

    public Parent getRoot() {
        return root;
    }
}
