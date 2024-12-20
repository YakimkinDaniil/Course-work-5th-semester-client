package com.example.autoschool.view;

import com.example.autoschool.ApiClient;
import com.example.autoschool.model.Group;
import com.example.autoschool.model.Instructor;
import com.example.autoschool.model.Lesson;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Диалоговое окно для добавления занятия.
 */
public class ScheduleDialog {
    public static Optional<Lesson> showScheduleDialog() {
        Dialog<Lesson> dialog = new Dialog<>();
        dialog.setTitle("Добавление занятия");
        dialog.setHeaderText("Пожалуйста, введите данные занятия");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField typeField = new TextField();
        typeField.setPromptText("Тип");
        DatePicker dateTimePicker = new DatePicker();
        dateTimePicker.setPromptText("Дата и время");
        TextField locationField = new TextField();
        locationField.setPromptText("Место");
        ComboBox<Instructor> instructorComboBox = new ComboBox<>();
        instructorComboBox.setPromptText("Выберите инструктора");
        ComboBox<Group> groupComboBox = new ComboBox<>();
        groupComboBox.setPromptText("Выберите группу");

        grid.add(new Label("Тип:"), 0, 0);
        grid.add(typeField, 1, 0);
        grid.add(new Label("Дата и время:"), 0, 1);
        grid.add(dateTimePicker, 1, 1);
        grid.add(new Label("Место:"), 0, 2);
        grid.add(locationField, 1, 2);
        grid.add(new Label("Инструктор:"), 0, 3);
        grid.add(instructorComboBox, 1, 3);
        grid.add(new Label("Группа:"), 0, 4);
        grid.add(groupComboBox, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                Lesson lesson = new Lesson();
                lesson.setType(typeField.getText());
                lesson.setDateTime(java.sql.Date.valueOf(dateTimePicker.getValue()));
                lesson.setLocation(locationField.getText());

                Instructor selectedInstructor = instructorComboBox.getSelectionModel().getSelectedItem();
                if (selectedInstructor != null) {
                    lesson.setInstructor(selectedInstructor);
                }

                Group selectedGroup = groupComboBox.getSelectionModel().getSelectedItem();
                if (selectedGroup != null) {
                    lesson.setGroup(selectedGroup);
                }

                return lesson;
            }
            return null;
        });

        try {
            List<Instructor> instructors = ApiClient.getAllInstructors();
            instructorComboBox.getItems().addAll(instructors);
            instructorComboBox.setConverter(new StringConverter<Instructor>() {
                @Override
                public String toString(Instructor instructor) {
                    return instructor != null ? instructor.getFirstName() + " " + instructor.getLastName() : "";
                }

                @Override
                public Instructor fromString(String string) {
                    return null;
                }
            });
        } catch (IOException e) {
            showAlert("Ошибка", "Ошибка при загрузке данных инструкторов: " + e.getMessage());
        }

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

        dialog.getDialogPane().getStylesheets().add(ScheduleDialog.class.getResource("/styles.css").toExternalForm());
        Optional<Lesson> result = dialog.showAndWait();
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
