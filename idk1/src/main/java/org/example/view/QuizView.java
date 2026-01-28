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
 * Quiz screen (UI only). Uses QuizController to run the quiz logic.
 */
public class QuizView {

    private final VBox root = new VBox(10);

    private final QuizController quizController;
    private Question currentQuestion;

    private final Label questionLabel = new Label();
    private final Button[] answerButtons = new Button[] {
            new Button(), new Button(), new Button(), new Button()
    };

    private final Label feedbackLabel = new Label();

    private final ComboBox<String> difficultyBox = new ComboBox<>();
    private final Button nextButton = new Button("Nächste Frage");
    private final Button backButton = new Button("Zurück zum Menü");

    private boolean lastAnswerCorrect = false;

    public QuizView(Stage stage, User user) {
        this.quizController = new QuizController(user.getUsername());

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
            // difficulty submit AFTER answering (requirements)
            quizController.submitDifficulty(lastAnswerCorrect, difficultyBox.getValue());
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
                difficultyRow,
                bottomButtons
        );

        // start
        loadNextQuestion();
    }

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

    private void handleAnswerClick(int chosenIndex) {
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

    private void setAnswerButtonsDisabled(boolean disabled) {
        for (Button b : answerButtons) {
            b.setDisable(disabled);
        }
    }

    public Parent getRoot() {
        return root;
    }
}
