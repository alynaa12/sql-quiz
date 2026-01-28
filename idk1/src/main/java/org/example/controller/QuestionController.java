package org.example.controller;

import org.example.model.Question;
import org.example.persistence.QuestionRepository;

import java.util.List;

public class QuestionController {

    private final QuestionRepository questionRepo = new QuestionRepository();

    public List<Question> getAllQuestions() {
        return questionRepo.loadAllQuestions();
    }
}

