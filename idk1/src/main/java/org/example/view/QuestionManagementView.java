package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.controller.QuestionController;
import org.example.model.Question;
import org.example.model.User;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


import java.util.List;

/**
 * View to manage questions (list + add question form).
 * UI only; calls QuestionController for persistence.
 */
public class QuestionManagementView {

    private final VBox root = new VBox();
    private final QuestionController questionController = new QuestionController();

    private final ListView<String> listView = new ListView<>();

    public QuestionManagementView(Stage stage, User user) {
        root.setPadding(new Insets(16));
        root.setSpacing(10);

        Label title = new Label("Fragen verwalten");

        // Top buttons
        Button addButton = new Button("Frage hinzufügen");
        Button backButton = new Button("Zurück zum Menü");
        Button deleteButton = new Button("Ausgewählte Frage löschen");
        deleteButton.setOnAction(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Bitte zuerst eine Frage auswählen.");
                alert.showAndWait();
                return;
            }

            int colonIndex = selected.indexOf(':');
            if (colonIndex <= 0) return;

            int id = Integer.parseInt(selected.substring(0, colonIndex).trim());

            Alert confirm = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Wirklich Frage " + id + " löschen?",
                    ButtonType.YES, ButtonType.NO
            );

            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES) {
                    boolean ok = questionController.deleteQuestion(id);
                    if (ok) {
                        loadQuestionsIntoList();
                    }
                }
            });
        });


        HBox topButtons = new HBox(10, addButton, deleteButton, backButton);

        backButton.setOnAction(e -> {
            MenuView menuView = new MenuView(stage, user);
            stage.getScene().setRoot(menuView.getRoot());
            stage.setTitle("SQL Quiz - Menü");
        });

        // List
        loadQuestionsIntoList();

        // Add form (initially hidden)
        VBox addForm = createAddForm();
        addForm.setVisible(false);
        addForm.setManaged(false);

        addButton.setOnAction(e -> {
            boolean show = !addForm.isVisible();
            addForm.setVisible(show);
            addForm.setManaged(show);
        });

        root.getChildren().addAll(title, topButtons, listView, addForm);
    }

    private void loadQuestionsIntoList() {
        listView.getItems().clear();
        List<Question> questions = questionController.getAllQuestions();
        for (Question q : questions) {
            listView.getItems().add(q.getId() + ": " + q.getText());
        }
    }

    private VBox createAddForm() {
        VBox form = new VBox(8);
        form.setPadding(new Insets(10));
        form.setStyle("-fx-border-color: gray; -fx-border-radius: 6; -fx-background-radius: 6;");

        Label formTitle = new Label("Neue Frage");

        TextField questionTextField = new TextField();
        questionTextField.setPromptText("Fragetext");

        TextField optA = new TextField();
        optA.setPromptText("Option A");

        TextField optB = new TextField();
        optB.setPromptText("Option B");

        TextField optC = new TextField();
        optC.setPromptText("Option C");

        TextField optD = new TextField();
        optD.setPromptText("Option D");

        Spinner<Integer> correctIndexSpinner = new Spinner<>(0, 3, 0);
        correctIndexSpinner.setEditable(true);

        Label correctLabel = new Label("Richtige Option (0=A, 1=B, 2=C, 3=D):");

        Label messageLabel = new Label();

        Button saveButton = new Button("Speichern");

        saveButton.setOnAction(e -> {
            String text = questionTextField.getText().trim();
            String a = optA.getText().trim();
            String b = optB.getText().trim();
            String c = optC.getText().trim();
            String d = optD.getText().trim();
            int correctIndex = correctIndexSpinner.getValue();

            if (text.isEmpty() || a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty()) {
                messageLabel.setText("Bitte alle Felder ausfüllen.");
                return;
            }

            try {
                questionController.addQuestion(text, List.of(a, b, c, d), correctIndex);

                // Clear fields
                questionTextField.clear();
                optA.clear();
                optB.clear();
                optC.clear();
                optD.clear();
                correctIndexSpinner.getValueFactory().setValue(0);

                messageLabel.setText("Gespeichert ✅");
                loadQuestionsIntoList();
            } catch (Exception ex) {
                messageLabel.setText("Fehler beim Speichern: " + ex.getMessage());
            }
        });

        form.getChildren().addAll(
                formTitle,
                questionTextField,
                optA, optB, optC, optD,
                correctLabel, correctIndexSpinner,
                saveButton,
                messageLabel
        );

        return form;
    }



    public Parent getRoot() {
        return root;
    }
}
