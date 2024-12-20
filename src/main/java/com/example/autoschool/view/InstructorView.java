package com.example.autoschool.view;

import com.example.autoschool.ApiClient;
import com.example.autoschool.model.Instructor;
import com.example.autoschool.model.Lesson;
import com.example.autoschool.model.ResultDTO;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

/**
 * Панель инструктора.
 */
public class InstructorView extends VBox {
    private TableView<Instructor> tableView;
    private ObservableList<Instructor> instructors;
    private Button showTableButton;

    public InstructorView(Stage stage, String instructorName) {
        getStyleClass().add("instructor-panel");

        Label instructorLabel = new Label("Добро пожаловать, " + instructorName + "!");
        instructorLabel.getStyleClass().add("label");

        Button viewScheduleButton = new Button("Просмотр расписания");
        viewScheduleButton.getStyleClass().add("button");
        viewScheduleButton.setOnAction(event -> openScheduleView());

        Button addGradeButton = new Button("Добавление оценки");
        addGradeButton.getStyleClass().add("button");
        addGradeButton.setOnAction(event -> openGradeDialog(false, null));

        Button editGradeButton = new Button("Редактирование оценки");
        editGradeButton.getStyleClass().add("button");
        editGradeButton.setOnAction(event -> openGradeViewForEdit());

        Button deleteGradeButton = new Button("Удаление оценки");
        deleteGradeButton.getStyleClass().add("button");
        deleteGradeButton.setOnAction(event -> openGradeViewForDelete());

        Button backButton = new Button("Выход");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(event -> goBack());

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        buttonBox.getChildren().addAll(viewScheduleButton, addGradeButton, editGradeButton, deleteGradeButton, backButton);

        Button aboutButton = new Button("Об авторе");
        aboutButton.getStyleClass().add("button");
        aboutButton.setOnAction(event -> AboutDialog.showAboutDialog());
        HBox aboutBox = new HBox(aboutButton);
        aboutBox.setAlignment(javafx.geometry.Pos.TOP_RIGHT);

        Button showChartButton = new Button("График статистики оценок");
        showChartButton.setOnAction(event -> openPieChartView());

        getChildren().addAll(instructorLabel, showChartButton, buttonBox, aboutBox);
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

    private void openGradeDialog(boolean isEdit, ResultDTO selectedResult) {
        Stage gradeViewStage = new Stage();
        gradeViewStage.setTitle(isEdit ? "Редактирование оценки" : "Добавление оценки");
        GradeDialog gradeDialog = new GradeDialog(isEdit, selectedResult);
        Scene scene = new Scene(gradeDialog, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        gradeViewStage.setScene(scene);
        gradeViewStage.showAndWait();
    }

    private void openGradeViewForEdit() {
        Stage gradeViewStage = new Stage();
        gradeViewStage.setTitle("Редактирование оценки");
        try {
            List<ResultDTO> results = ApiClient.getAllResults();
            GradeView gradeView = new GradeView(results);
            Scene scene = new Scene(gradeView, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            gradeViewStage.setScene(scene);
            gradeViewStage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Ошибка при получении оценок: " + e.getMessage());
        }
    }

    private void openGradeViewForDelete() {
        Stage gradeViewStage = new Stage();
        gradeViewStage.setTitle("Удаление оценки");
        try {
            List<ResultDTO> results = ApiClient.getAllResults();
            DeleteGradeView deleteGradeView = new DeleteGradeView(results);
            Scene scene = new Scene(deleteGradeView, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            gradeViewStage.setScene(scene);
            gradeViewStage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Ошибка при получении оценок: " + e.getMessage());
        }
    }

    private void openPieChartView() {
        Stage pieChartStage = new Stage();
        pieChartStage.setTitle("График оценок");
        try {
            List<ResultDTO> results = ApiClient.getAllResults();
            PieChartView pieChartView = new PieChartView(results);
            Scene scene = new Scene(pieChartView, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            pieChartStage.setScene(scene);
            pieChartStage.show();
        } catch (IOException e) {
            showAlert("Ошибка", "Ошибка при получении данных для графика: " + e.getMessage());
        }
    }

    private void goBack() {
        Stage stage = (Stage) getScene().getWindow();
        stage.close();
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
