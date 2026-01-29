package org.example.controller;

import org.example.model.Question;
import org.example.persistence.QuestionRepository;

import java.util.ArrayList;
import java.util.List;

public class QuestionController {

    private final QuestionRepository questionRepo = new QuestionRepository();

    public List<Question> getAllQuestions() {
        return questionRepo.loadAllQuestions();
    }

    public void addQuestion(String text, List<String> options, int correctIndex) {
        List<Question> questions = questionRepo.loadAllQuestions();

        int newId = getNextId(questions);
        Question newQuestion = new Question(newId, text, options, correctIndex);

        questions.add(newQuestion);
        questionRepo.saveAllQuestions(questions);
    }

    public boolean updateQuestion(int id, String newText, List<String> newOptions, int newCorrectIndex) {
        List<Question> questions = questionRepo.loadAllQuestions();

        for (Question q : questions) {
            if (q.getId() == id) {
                q.setText(newText);
                q.setOptions(newOptions);
                q.setCorrectIndex(newCorrectIndex);
                questionRepo.saveAllQuestions(questions);
                return true;
            }
        }
        return false;
    }

    public boolean deleteQuestion(int id) {
        List<Question> questions = questionRepo.loadAllQuestions();

        boolean removed = questions.removeIf(q -> q.getId() == id);
        if (removed) {
            questionRepo.saveAllQuestions(questions);
        }
        return removed;
    }

    private int getNextId(List<Question> questions) {
        int max = 0;
        for (Question q : questions) {
            if (q.getId() > max) max = q.getId();
        }
        return max + 1;
    }



}
