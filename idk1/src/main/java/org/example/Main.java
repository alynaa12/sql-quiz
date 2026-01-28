package org.example;

import org.example.controller.QuizController;
import org.example.model.Question;

public class Main {
    public static void main(String[] args) {

        QuizController controller = new QuizController("Zena");

        Question q1 = controller.nextQuestion();
        System.out.println("Frage: " + q1.getText());
        System.out.println("Optionen: " + q1.getOptions());

        int wrongIndex = (q1.getCorrectIndex() == 0) ? 1 : 0;
        boolean correct = controller.submitAnswer(wrongIndex);
        System.out.println("Antwort richtig? " + correct);

        controller.submitDifficulty(correct, "leicht");

        System.out.println("Test über Controller fertig");
    }
}

