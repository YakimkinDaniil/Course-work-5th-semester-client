package com.example.autoschool.view;

import com.example.autoschool.ApiClient;
import com.example.autoschool.model.Student;
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
 * Панель для удаления обучающегося.
 */
public class DeleteStudentView extends VBox {
    private TableView<Student> tableView;
    private ObservableList<Student> students;
    private TextField searchField;

    public DeleteStudentView() {
        getStyleClass().add("delete-student-view");

        tableView = new TableView<>();
        tableView.getStyleClass().add("table-view");

        TableColumn<Student, String> firstNameColumn = new TableColumn<>("Имя");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstNameColumn.getStyleClass().add("column-header");

        TableColumn<Student, String> lastNameColumn = new TableColumn<>("Фамилия");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameColumn.getStyleClass().add("column-header");

        TableColumn<Student, Date> birthDateColumn = new TableColumn<>("Дата рождения");
        birthDateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        birthDateColumn.setCellFactory(param -> new TableCell<Student, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    setText(dateFormat.format(item));
                }
            }
        });
        birthDateColumn.getStyleClass().add("column-header");

        TableColumn<Student, String> contactInfoColumn = new TableColumn<>("Контактная информация");
        contactInfoColumn.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
        contactInfoColumn.getStyleClass().add("column-header");

        TableColumn<Student, Date> startDateColumn = new TableColumn<>("Дата начала");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startDateColumn.setCellFactory(param -> new TableCell<Student, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    setText(dateFormat.format(item));
                }
            }
        });
        startDateColumn.getStyleClass().add("column-header");

        TableColumn<Student, Date> endDateColumn = new TableColumn<>("Дата окончания");
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endDateColumn.setCellFactory(param -> new TableCell<Student, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    setText(dateFormat.format(item));
                }
            }
        });
        endDateColumn.getStyleClass().add("column-header");

        tableView.getColumns().addAll(firstNameColumn, lastNameColumn, birthDateColumn, contactInfoColumn, startDateColumn, endDateColumn);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        try {
            List<Student> studentList = ApiClient.getAllStudents();
            students = FXCollections.observableArrayList(studentList);
            tableView.setItems(students);
        } catch (IOException e) {
            showAlert("Ошибка", "Ошибка при загрузке данных обучающихся: " + e.getMessage());
        }

        searchField = new TextField();
        searchField.setPromptText("Поиск по имени или фамилии");
        searchField.setOnKeyReleased(event -> filterStudents());

        HBox filterBox = new HBox(10, new Label("Поиск по имени или фамилии:"), searchField);
        filterBox.setAlignment(javafx.geometry.Pos.CENTER);

        Button deleteButton = new Button("Удалить обучающегося");
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setOnAction(event -> {
            Student selectedStudent = tableView.getSelectionModel().getSelectedItem();
            if (selectedStudent != null) {
                try {
                    ApiClient.deleteStudent(selectedStudent.getId());
                    students.remove(selectedStudent);
                    showAlert("Успех", "Обучающийся успешно удален!");
                } catch (IOException e) {
                    showAlert("Ошибка", "Ошибка при удалении обучающегося: " + e.getMessage());
                }
            } else {
                showAlert("Ошибка", "Выберите обучающегося для удаления.");
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

    private void filterStudents() {
        String query = searchField.getText().toLowerCase();
        ObservableList<Student> filteredList = FXCollections.observableArrayList();
        for (Student student : students) {
            if (student.getFirstName().toLowerCase().contains(query) ||
                    student.getLastName().toLowerCase().contains(query)) {
                filteredList.add(student);
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
