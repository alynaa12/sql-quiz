package org.example.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.model.Question;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepository {

    private static final String QUESTION_FILE = "data/questions.json";

    public List<Question> loadAllQuestions() {
        try {
            Gson gson = new Gson();
            Type questionListType = new TypeToken<List<Question>>() {}.getType();
            List<Question> questions = gson.fromJson(new FileReader(QUESTION_FILE), questionListType);
            return questions != null ? questions : new ArrayList<>();
        } catch (Exception e) {
            throw new RuntimeException("Could not load questions", e);
        }
    }

    public void saveAllQuestions(List<Question> questions) {
        try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(QUESTION_FILE);
            gson.toJson(questions, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("Could not save questions", e);
        }
    }
}


