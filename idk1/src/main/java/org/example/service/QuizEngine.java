package org.example.service;

import org.example.model.Question;
import org.example.model.RepetitionItem;
import org.example.persistence.QuestionRepository;
import org.example.persistence.RepetitionRepository;
import org.example.persistence.UserRepository;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class QuizEngine {

    private final String username;

    private final QuestionRepository questionRepo = new QuestionRepository();
    private final RepetitionRepository repetitionRepo = new RepetitionRepository();
    private final UserRepository userRepo = new UserRepository();

    private final List<Question> allQuestions;
    private int normalIndex = 0;

    private final Deque<Integer> repetitionQueue = new ArrayDeque<>();
    private int normalCounterSinceRepetition = 0;

    private boolean lastWasRepetition = false;
    private int repetitionRoundPointsGiven = 0;

    private Question currentQuestion;

    public QuizEngine(String username) {
        this.username = username;
        this.allQuestions = questionRepo.loadAllQuestions();

        // lade bestehende Wiederholungen aus Datei (für diesen User)
        for (RepetitionItem item : repetitionRepo.loadAll()) {
            if (item.getUsername().equals(username)) {
                repetitionQueue.addLast(item.getQuestionId());
            }
        }
    }

    public Question nextQuestion() {
        // alle 3 normalen Fragen: wenn Wiederholung da ist, stelle Wiederholung
        if (normalCounterSinceRepetition >= 3 && !repetitionQueue.isEmpty()) {
            int qId = repetitionQueue.removeFirst();
            currentQuestion = findById(qId);
            lastWasRepetition = true;
            normalCounterSinceRepetition = 0;
            return currentQuestion;
        }

        // normale Frage
        if (normalIndex >= allQuestions.size()) {
            // wieder von vorne (einfachste Variante)
            normalIndex = 0;
        }

        currentQuestion = allQuestions.get(normalIndex);
        normalIndex++;
        lastWasRepetition = false;
        normalCounterSinceRepetition++;
        return currentQuestion;
    }

    public boolean submitAnswer(int chosenIndex) {
        return chosenIndex == currentQuestion.getCorrectIndex();
    }

    public void submitDifficulty(boolean wasCorrect, String difficulty) {
        // Regel: falsch ODER schwer => in Wiederholung
        if (!wasCorrect || "schwer".equalsIgnoreCase(difficulty)) {
            addToRepetitionIfMissing(currentQuestion.getId());
            saveRepetitionToFile();
        }

        // Punkte: nur für abgeschlossene Wiederholungsrunde
        // Einfach: Wenn wir gerade eine Wiederholungsfrage gemacht haben UND Queue danach leer ist => +1 Punkt
        if (lastWasRepetition && repetitionQueue.isEmpty()) {
            increaseUserHighscoreBy1();
            repetitionRoundPointsGiven++;
        }
    }

    private void addToRepetitionIfMissing(int questionId) {
        if (!repetitionQueue.contains(questionId)) {
            repetitionQueue.addLast(questionId);
        }
    }

    private void saveRepetitionToFile() {
        // speichere nur die Queue für diesen User (einfach, aber ok für Abgabe)
        List<RepetitionItem> all = repetitionRepo.loadAll();

        // entferne alte Einträge dieses Users
        all.removeIf(item -> item.getUsername().equals(username));

        // füge aktuelle Queue ein
        for (Integer qId : repetitionQueue) {
            all.add(new RepetitionItem(username, qId));
        }

        repetitionRepo.saveAll(all);
    }

    private void increaseUserHighscoreBy1() {
        userRepo.increaseHighscore(username, 1);
    }

    private Question findById(int id) {
        for (Question q : allQuestions) {
            if (q.getId() == id) return q;
        }
        // Fallback: wenn nicht gefunden, nimm erste
        return allQuestions.get(0);
    }

    public int getRepetitionRoundPointsGiven() {
        return repetitionRoundPointsGiven;
    }
}

