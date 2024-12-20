package com.example.autoschool.view;

import com.example.autoschool.ApiClient;
import com.example.autoschool.model.ApplicationDTO;
import com.example.autoschool.model.Lesson;
import com.example.autoschool.model.ResultDTO;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Панель обучающегося.
 */
public class StudentView extends VBox {
    private Stage stage;
    private String studentName;

    public StudentView(Stage stage, String studentName) {
        this.stage = stage;
        this.studentName = studentName;
        getStyleClass().add("student-panel");

        Label studentLabel = new Label("Добро пожаловать, " + studentName + "!");
        studentLabel.getStyleClass().add("label");

        Button viewScheduleButton = new Button("Просмотр расписания");
        viewScheduleButton.getStyleClass().add("button");
        viewScheduleButton.setOnAction(event -> openScheduleView());

        Button submitApplicationButton = new Button("Подать заявку на вождение");
        submitApplicationButton.getStyleClass().add("button");
        submitApplicationButton.setOnAction(event -> openApplicationDialog());

        Button viewResultsButton = new Button("Просмотр результатов экзамена");
        viewResultsButton.getStyleClass().add("button");
        viewResultsButton.setOnAction(event -> openResultsView());

        Button backButton = new Button("Выход");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(event -> goBack());

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        buttonBox.getChildren().addAll(viewScheduleButton, submitApplicationButton, viewResultsButton, backButton);

        Button aboutButton = new Button("Об авторе");
        aboutButton.getStyleClass().add("button");
        aboutButton.setOnAction(event -> AboutDialog.showAboutDialog());
        HBox aboutBox = new HBox(aboutButton);
        aboutBox.setAlignment(javafx.geometry.Pos.TOP_RIGHT);

        getChildren().addAll(aboutBox, studentLabel, buttonBox);
        setAlignment(javafx.geometry.Pos.CENTER);
        setSpacing(20);

        Scene scene = stage.getScene();
        if (scene != null) {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        }
    }

    private void openScheduleView() {
        Stage scheduleViewStage = new Stage();
        scheduleViewStage.setTitle("Просмотр расписания");
        try {
            List<Lesson> lessons = ApiClient.getAllLessons();
            ScheduleView scheduleView = new ScheduleView(lessons);
            Scene scene = new Scene(scheduleView, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            scheduleViewStage.setScene(scene);
            scheduleViewStage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Ошибка при получении расписания: " + e.getMessage());
        }
    }

    private void openResultsView() {
        Stage resultsViewStage = new Stage();
        resultsViewStage.setTitle("Просмотр результатов экзамена");
        try {
            List<ResultDTO> results = ApiClient.getAllResults();
            ResultsView resultsView = new ResultsView(results);
            Scene scene = new Scene(resultsView, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            resultsViewStage.setScene(scene);
            resultsViewStage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Ошибка при получении результатов экзамена: " + e.getMessage());
        }
    }

    private void openApplicationDialog() {
        Optional<ApplicationDTO> result = ApplicationDialog.showApplicationDialog();
        result.ifPresent(applicationDto -> {
            try {
                ApplicationDTO createdApp = ApiClient.createApp(applicationDto);
                showAlert("Успех", "Заявка успешно создана!");
            } catch (IOException e) {
                showAlert("Ошибка", "Ошибка при создании заявки");
            }
        });
    }

    private void goBack() {
        stage.close();
    }

    private static void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
