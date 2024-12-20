package com.example.autoschool.view;

import com.example.autoschool.model.Student;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.util.Optional;

/**
 * Диалоговое окно для редактирования студентов.
 */
public class EditStudentDialog {
    public static Optional<Student> showStudentDialog(Student student) {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Редактирование обучающегося");
        dialog.setHeaderText("Пожалуйста, введите данные обучающегося");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

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

        if (student != null) {
            firstNameField.setText(student.getFirstName());
            lastNameField.setText(student.getLastName());
            if (student.getBirthDate() != null) {
                birthDatePicker.setValue(student.getBirthDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
            }
            contactInfoField.setText(student.getContactInfo());
            if (student.getStartDate() != null) {
                startDatePicker.setValue(student.getStartDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
            }
            if (student.getEndDate() != null) {
                endDatePicker.setValue(student.getEndDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
            }
        }

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

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                Student editedStudent = new Student();
                editedStudent.setFirstName(firstNameField.getText());
                editedStudent.setLastName(lastNameField.getText());
                if (birthDatePicker.getValue() != null) {
                    editedStudent.setBirthDate(java.sql.Date.valueOf(birthDatePicker.getValue()));
                }
                editedStudent.setContactInfo(contactInfoField.getText());
                if (startDatePicker.getValue() != null) {
                    editedStudent.setStartDate(java.sql.Date.valueOf(startDatePicker.getValue()));
                }
                if (endDatePicker.getValue() != null) {
                    editedStudent.setEndDate(java.sql.Date.valueOf(endDatePicker.getValue()));
                }
                if (student != null) {
                    editedStudent.setId(student.getId());
                }
                return editedStudent;
            }
            return null;
        });

        dialog.getDialogPane().getStylesheets().add(EditStudentDialog.class.getResource("/styles.css").toExternalForm());

        return dialog.showAndWait();
    }
}
