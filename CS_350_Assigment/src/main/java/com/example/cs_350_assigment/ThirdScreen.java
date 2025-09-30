package com.example.cs_350_assigment;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ThirdScreen {

    @FXML private Label userScoreLabel;
    @FXML private Label totalScoreLabel;
    @FXML private Button playAgainButton;
    @FXML private Button exitButton;

    public void setScores(int userScore, int totalScore) {
        userScoreLabel.setText(String.valueOf(userScore));
        totalScoreLabel.setText(String.valueOf(totalScore));
    }

    @FXML
    private void handlePlay() {
        try {
            // Load the second screen again
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Second_Screen.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) playAgainButton.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}