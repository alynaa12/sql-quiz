package org.example;

import org.example.controller.QuestionController;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        QuestionController qc = new QuestionController();

        qc.addQuestion(
                "Was macht COUNT(*)?",
                List.of("Zählt Zeilen", "Löscht Zeilen", "Erstellt Tabelle", "Ändert Spalten"),
                0
        );

        System.out.println("Fragenanzahl: " + qc.getAllQuestions().size());
        System.out.println("Letzte Frage in Datei gespeichert ✅");
    }
}


