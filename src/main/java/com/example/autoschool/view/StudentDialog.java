package com.example.autoschool.view;

import com.example.autoschool.ApiClient;
import com.example.autoschool.model.Group;
import com.example.autoschool.model.Student;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Диалоговое окно для добавления обучающегося.
 */
public class StudentDialog {
    public static Optional<Student> showStudentDialog() {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Добавление обучающегося");
        dialog.setHeaderText("Пожалуйста, введите данные обучающегося");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Имя");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Фамилия");
        DatePicker birthDatePicker = new DatePicker();
        birthDatePicker.setPromptText("Дата рождения");
        TextField contactInfoField = new TextField();
        contactInfoField.setPromptText("Контактная информация");
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Дата начала");
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("Дата окончания");
        ComboBox<Group> groupComboBox = new ComboBox<>();
        groupComboBox.setPromptText("Выберите группу");

        grid.add(new Label("Имя:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Фамилия:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Дата рождения:"), 0, 2);
        grid.add(birthDatePicker, 1, 2);
        grid.add(new Label("Контактная информация:"), 0, 3);
        grid.add(contactInfoField, 1, 3);
        grid.add(new Label("Дата начала:"), 0, 4);
        grid.add(startDatePicker, 1, 4);
        grid.add(new Label("Дата окончания:"), 0, 5);
        grid.add(endDatePicker, 1, 5);
        grid.add(new Label("Группа:"), 0, 6);
        grid.add(groupComboBox, 1, 6);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                Student student = new Student();
                student.setFirstName(firstNameField.getText());
                student.setLastName(lastNameField.getText());
                student.setBirthDate(java.sql.Date.valueOf(birthDatePicker.getValue()));
                student.setContactInfo(contactInfoField.getText());
                student.setStartDate(java.sql.Date.valueOf(startDatePicker.getValue()));
                student.setEndDate(java.sql.Date.valueOf(endDatePicker.getValue()));

                Group selectedGroup = groupComboBox.getSelectionModel().getSelectedItem();
                if (selectedGroup != null) {
                    student.setGroup(selectedGroup);
                }

                return student;
            }
            return null;
        });

        try {
            List<Group> groups = ApiClient.getAllGroups();
            groupComboBox.getItems().addAll(groups);
            groupComboBox.setConverter(new StringConverter<Group>() {
                @Override
                public String toString(Group group) {
                    return group != null ? group.getName() : "";
                }

                @Override
                public Group fromString(String string) {
                    return null;
                }
            });
        } catch (IOException e) {
            showAlert("Ошибка", "Ошибка при загрузке данных групп: " + e.getMessage());
        }

        dialog.getDialogPane().getStylesheets().add(StudentDialog.class.getResource("/styles.css").toExternalForm());
        Optional<Student> result = dialog.showAndWait();
        return result;
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
