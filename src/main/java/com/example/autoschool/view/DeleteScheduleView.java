package com.example.autoschool.view;

import com.example.autoschool.ApiClient;
import com.example.autoschool.model.Lesson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Панель для удаления занятий.
 */
public class DeleteScheduleView extends VBox {
    private TableView<Lesson> tableView;
    private ObservableList<Lesson> lessons;
    private TextField searchField;

    public DeleteScheduleView() {
        getStyleClass().add("delete-lesson-view");

        tableView = new TableView<>();
        tableView.getStyleClass().add("table-view");

        TableColumn<Lesson, String> typeColumn = new TableColumn<>("Тип");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.getStyleClass().add("column-header");

        TableColumn<Lesson, Date> dateTimeColumn = new TableColumn<>("Дата и время");
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        dateTimeColumn.setCellFactory(param -> new TableCell<Lesson, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    setText(dateFormat.format(item));
                }
            }
        });
        dateTimeColumn.getStyleClass().add("column-header");

        TableColumn<Lesson, String> locationColumn = new TableColumn<>("Место");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationColumn.getStyleClass().add("column-header");

        tableView.getColumns().addAll(typeColumn, dateTimeColumn, locationColumn);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        try {
            List<Lesson> lessonList = ApiClient.getAllLessons();
            lessons = FXCollections.observableArrayList(lessonList);
            tableView.setItems(lessons);
        } catch (IOException e) {
            showAlert("Ошибка", "Ошибка при загрузке данных занятий: " + e.getMessage());
        }

        searchField = new TextField();
        searchField.setPromptText("Поиск по типу или месту");
        searchField.setOnKeyReleased(event -> filterLessons());

        HBox filterBox = new HBox(10, new Label("Поиск по типу или месту:"), searchField);
        filterBox.setAlignment(javafx.geometry.Pos.CENTER);

        Button deleteButton = new Button("Удалить занятие");
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setOnAction(event -> {
            Lesson selectedLesson = tableView.getSelectionModel().getSelectedItem();
            if (selectedLesson != null) {
                try {
                    ApiClient.deleteLesson(selectedLesson.getId());
                    lessons.remove(selectedLesson);
                    showAlert("Успех", "Занятие успешно удалено!");
                } catch (IOException e) {
                    showAlert("Ошибка", "Ошибка при удалении занятия: " + e.getMessage());
                }
            } else {
                showAlert("Ошибка", "Выберите занятие для удаления.");
            }
        });

        Button backButton = new Button("Назад");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(event -> {
            Stage stage = (Stage) getScene().getWindow();
            stage.close();
        });

        getChildren().addAll(filterBox, tableView, deleteButton, backButton);
        setAlignment(javafx.geometry.Pos.CENTER);
        setSpacing(20);
    }

    private void filterLessons() {
        String query = searchField.getText().toLowerCase();
        ObservableList<Lesson> filteredList = FXCollections.observableArrayList();
        for (Lesson lesson : lessons) {
            if (lesson.getType().toLowerCase().contains(query) ||
                    lesson.getLocation().toLowerCase().contains(query)) {
                filteredList.add(lesson);
            }
        }
        tableView.setItems(filteredList);
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
