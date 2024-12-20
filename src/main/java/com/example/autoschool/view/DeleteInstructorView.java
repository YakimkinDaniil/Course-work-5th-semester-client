package com.example.autoschool.view;

import com.example.autoschool.ApiClient;
import com.example.autoschool.model.Instructor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

/**
 * Панель для удаления инструкторов.
 */
public class DeleteInstructorView extends VBox {
    private TableView<Instructor> tableView;
    private ObservableList<Instructor> instructors;
    private TextField searchField;

    public DeleteInstructorView() {
        getStyleClass().add("delete-instructor-view");

        tableView = new TableView<>();
        tableView.getStyleClass().add("table-view");

        TableColumn<Instructor, String> firstNameColumn = new TableColumn<>("Имя");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstNameColumn.getStyleClass().add("column-header");

        TableColumn<Instructor, String> lastNameColumn = new TableColumn<>("Фамилия");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameColumn.getStyleClass().add("column-header");

        TableColumn<Instructor, String> specializationColumn = new TableColumn<>("Специальность");
        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        specializationColumn.getStyleClass().add("column-header");

        TableColumn<Instructor, String> contactInfoColumn = new TableColumn<>("Контактная информация");
        contactInfoColumn.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
        contactInfoColumn.getStyleClass().add("column-header");

        tableView.getColumns().addAll(firstNameColumn, lastNameColumn, specializationColumn, contactInfoColumn);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        try {
            List<Instructor> instructorList = ApiClient.getAllInstructors();
            instructors = FXCollections.observableArrayList(instructorList);
            tableView.setItems(instructors);
        } catch (IOException e) {
            showAlert("Ошибка", "Ошибка при загрузке данных инструкторов: " + e.getMessage());
        }

        searchField = new TextField();
        searchField.setPromptText("Поиск по имени или фамилии");
        searchField.setOnKeyReleased(event -> filterInstructors());

        HBox filterBox = new HBox(10, new Label("Поиск по имени или фамилии:"), searchField);
        filterBox.setAlignment(javafx.geometry.Pos.CENTER);

        Button deleteButton = new Button("Удалить инструктора");
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setOnAction(event -> {
            Instructor selectedInstructor = tableView.getSelectionModel().getSelectedItem();
            if (selectedInstructor != null) {
                try {
                    ApiClient.deleteInstructor(selectedInstructor.getId());
                    instructors.remove(selectedInstructor);
                    showAlert("Успех", "Инструктор успешно удален!");
                } catch (IOException e) {
                    showAlert("Ошибка", "Ошибка при удалении инструктора: " + e.getMessage());
                }
            } else {
                showAlert("Ошибка", "Выберите инструктора для удаления.");
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

    private void filterInstructors() {
        String query = searchField.getText().toLowerCase();
        ObservableList<Instructor> filteredList = FXCollections.observableArrayList();
        for (Instructor instructor : instructors) {
            if (instructor.getFirstName().toLowerCase().contains(query) ||
                    instructor.getLastName().toLowerCase().contains(query)) {
                filteredList.add(instructor);
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
