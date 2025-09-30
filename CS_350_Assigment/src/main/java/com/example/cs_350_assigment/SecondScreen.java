package com.example.cs_350_assigment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;

public class SecondScreen {

    @FXML private Label questionLabel;
    @FXML private Button optionA;
    @FXML private Button optionB;
    @FXML private Button optionC;
    @FXML private Button optionD;
    @FXML private Button Show_Answer;
    @FXML private Label scoreValue;
    @FXML private Label timerValue;

    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private String correctAnswer;

    private Timer questionTimer;
    private int questionTimeLeft = 30;

    private AudioClip correctSound;
    private AudioClip wrongSound;
    private AudioClip crowdCheerSound;

    @FXML
    public void initialize() {
        loadSounds();
        List<Question> allQuestions = loadQuestions();
        if (allQuestions == null || allQuestions.isEmpty()) {
            showAlert("No questions found!");
        } else {
            questions = getRandomQuestions(allQuestions, 15);
            showQuestion();
        }
    }

    private void loadSounds() {
        try {
            correctSound = new AudioClip(Objects.requireNonNull(
                    getClass().getResource("/com/example/cs_350_assigment/Sounds/correct.wav")
            ).toExternalForm());

            wrongSound = new AudioClip(Objects.requireNonNull(
                    getClass().getResource("/com/example/cs_350_assigment/Sounds/beep.wav")
            ).toExternalForm());

            crowdCheerSound = new AudioClip(Objects.requireNonNull(
                    getClass().getResource("/com/example/cs_350_assigment/Sounds/crowd-cheer.wav")
            ).toExternalForm());
        } catch (Exception e) {
            System.out.println("Error loading sounds: " + e.getMessage());
        }
    }

    private void startQuestionTimer() {
        if (questionTimer != null) {
            questionTimer.cancel();
        }

        questionTimeLeft = 30;
        timerValue.setText("00:30");

        questionTimer = new Timer();
        questionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    questionTimeLeft--;
                    timerValue.setText(String.format("00:%02d", questionTimeLeft));

                    if (questionTimeLeft <= 0) {
                        questionTimer.cancel();
                        endQuizDueToTimeOut();
                    }
                });
            }
        }, 1000, 1000);
    }

    private void endQuizDueToTimeOut() {
        disableOptions();
        highlightCorrectAnswer();
        if (wrongSound != null) wrongSound.play();
        playCrowdCheerAndMoveToThirdScreen();
    }

    private List<Question> loadQuestions() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("bible_questions_200.json")) {
            if (input == null) {
                showAlert("bible_questions_200.json not found!");
                return null;
            }
            try (InputStreamReader reader = new InputStreamReader(input)) {
                Gson gson = new Gson();
                Type questionListType = new TypeToken<List<Question>>() {}.getType();
                return gson.fromJson(reader, questionListType);
            }
        } catch (Exception e) {
            System.out.println("Error loading questions: " + e.getMessage());
            showAlert("Failed to load questions!");
            return null;
        }
    }

    private List<Question> getRandomQuestions(List<Question> all, int count) {
        List<Question> shuffled = new ArrayList<>(all);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, Math.min(count, shuffled.size()));
    }

    private void showQuestion() {
        if (currentIndex < questions.size()) {
            Question q = questions.get(currentIndex);
            questionLabel.setText(q.getQuestion());

            String[] options = q.getOptions();
            optionA.setText(options[0]);
            optionB.setText(options[1]);
            optionC.setText(options[2]);
            optionD.setText(options[3]);

            correctAnswer = q.getAnswer();

            resetButtonStyles();
            enableOptions();
            startQuestionTimer();
        } else {
            endQuizNormally();
        }
    }

    private void checkAnswer(Button selectedButton) {
        if (questionTimer != null) {
            questionTimer.cancel();
        }

        disableOptions();
        String selected = selectedButton.getText();

        if (selected.equals(correctAnswer)) {
            score++;
            scoreValue.setText(String.valueOf(score));
            highlightButton(selectedButton, "green");
            if (correctSound != null) correctSound.play();
        } else {
            highlightButton(selectedButton, "red");
            highlightCorrectAnswer();
            if (wrongSound != null) wrongSound.play();
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    currentIndex++;
                    showQuestion();
                });
            }
        }, 1000);
    }

    private void highlightCorrectAnswer() {
        if (optionA.getText().equals(correctAnswer)) highlightButton(optionA, "green");
        if (optionB.getText().equals(correctAnswer)) highlightButton(optionB, "green");
        if (optionC.getText().equals(correctAnswer)) highlightButton(optionC, "green");
        if (optionD.getText().equals(correctAnswer)) highlightButton(optionD, "green");
    }

    private void highlightButton(Button button, String color) {
        if (color.equals("green")) {
            button.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        } else if (color.equals("red")) {
            button.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
        }
    }

    private void resetButtonStyles() {
        String defaultStyle = "-fx-background-color: #f0f0f0; -fx-text-fill: black;";
        optionA.setStyle(defaultStyle);
        optionB.setStyle(defaultStyle);
        optionC.setStyle(defaultStyle);
        optionD.setStyle(defaultStyle);
    }

    private void disableOptions() {
        optionA.setDisable(false);
        optionB.setDisable(false);
        optionC.setDisable(false);
        optionD.setDisable(false);
        Show_Answer.setDisable(false);
    }

    private void enableOptions() {
        optionA.setDisable(false);
        optionB.setDisable(false);
        optionC.setDisable(false);
        optionD.setDisable(false);
        Show_Answer.setDisable(false);
    }

    private void endQuizNormally() {
        playCrowdCheerAndMoveToThirdScreen();
    }

    private void playCrowdCheerAndMoveToThirdScreen() {
        if (crowdCheerSound != null) {
            crowdCheerSound.play();
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> moveToThirdScreen());
            }
        }, 500);
    }

    private void moveToThirdScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Third_Screen.fxml"));
            Parent root = loader.load();

            ThirdScreen thirdScreenController = loader.getController();
            thirdScreenController.setScores(score, questions.size());

            Stage stage = (Stage) questionLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading third screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML private void handleOptionA() { checkAnswer(optionA); }
    @FXML private void handleOptionB() { checkAnswer(optionB); }
    @FXML private void handleOptionC() { checkAnswer(optionC); }
    @FXML private void handleOptionD() { checkAnswer(optionD); }

    @FXML
    private void handleAnswerButton() {
        if (questionTimer != null) {
            questionTimer.cancel();
        }
        highlightCorrectAnswer();
        disableOptions();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    currentIndex++;
                    showQuestion();
                });
            }
        }, 1000);
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bible Quiz");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}