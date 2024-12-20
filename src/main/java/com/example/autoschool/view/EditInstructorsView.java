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
import java.util.Optional;

/**
 * Панель для редактирования инструкторов.
 */
public class EditInstructorsView extends VBox {

    private TableView<Instructor> tableView;
    private ObservableList<Instructor> instructors;
    private TextField searchField;

    public EditInstructorsView() {
        getStyleClass().add("edit-instructors-view");

        tableView = new TableView<>();
        tableView.getStyleClass().add("table-view");

        TableColumn<Instructor, String> firstNameColumn = new TableColumn<>("Имя");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstNameColumn.getStyleClass().add("column-header");

        TableColumn<Instructor, String> lastNameColumn = new TableColumn<>("Фамилия");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameColumn.getStyleClass().add("column-header");

        TableColumn<Instructor, String> specializationColumn = new TableColumn<>("Специализация");
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

        Button editButton = new Button("Редактировать инструктора");
        editButton.getStyleClass().add("edit-button");
        editButton.setOnAction(event -> {
            Instructor selectedInstructor = tableView.getSelectionModel().getSelectedItem();
            if (selectedInstructor != null) {
                openEditInstructorDialog(selectedInstructor);
            } else {
                showAlert("Ошибка", "Выберите инструктора для редактирования.");
            }
        });

        Button backButton = new Button("Назад");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(event -> {
            Stage stage = (Stage) getScene().getWindow();
            stage.close();
        });

        getChildren().addAll(filterBox, tableView, editButton, backButton);
        setAlignment(javafx.geometry.Pos.CENTER);
        setSpacing(20);
    }

    private void openEditInstructorDialog(Instructor instructor) {
        Optional<Instructor> result = EditInstructorDialog.showEditInstructorDialog(instructor);
        result.ifPresent(editedInstructor -> {
            try {
                ApiClient.updateInstructor(editedInstructor);
                instructors.setAll(ApiClient.getAllInstructors());
                showAlert("Успех", "Инструктор успешно отредактирован!");
            } catch (IOException e) {
                showAlert("Ошибка", "Ошибка при редактировании инструктора: " + e.getMessage());
            }
        });
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
