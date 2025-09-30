package com.example.cs_350_assigment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class QuestionLoader {
    public static List<Question> loadQuestions() {
        try {
            // Load from resources using the class loader
            InputStream inputStream = QuestionLoader.class.getClassLoader().getResourceAsStream("/bible_questions_200.json");

            if (inputStream == null) {
                System.out.println("JSON file not found in resources.");
                return null;
            }

            InputStreamReader reader = new InputStreamReader(inputStream);
            Gson gson = new Gson();
            Type questionListType = new TypeToken<List<Question>>() {}.getType();
            return gson.fromJson(reader, questionListType);

        } catch (Exception e) {
            System.out.println("Error loading questions: " + e.getMessage());
            return null;
        }
    }
}
