package com.example.autoschool.view;

import com.example.autoschool.ApiClient;
import com.example.autoschool.model.Group;
import com.example.autoschool.model.Instructor;
import com.example.autoschool.model.Lesson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.time.ZoneId;

/**
 * Диалоговое окно для редактирования занятий.
 */
public class EditScheduleDialog {

    public static Optional<Lesson> showLessonDialog(Lesson lesson) {
        Dialog<Lesson> dialog = new Dialog<>();
        dialog.setTitle("Редактирование занятия");
        dialog.setHeaderText("Пожалуйста, введите данные занятия");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField typeField = new TextField();
        typeField.setPromptText("Тип");
        DatePicker dateTimePicker = new DatePicker();
        dateTimePicker.setPromptText("Дата и время");
        TextField locationField = new TextField();
        locationField.setPromptText("Место");
        ComboBox<Group> groupComboBox = new ComboBox<>();
        groupComboBox.setPromptText("Группа");
        ComboBox<Instructor> instructorComboBox = new ComboBox<>();
        instructorComboBox.setPromptText("Инструктор");

        if (lesson != null) {
            typeField.setText(lesson.getType());
            if (lesson.getDateTime() != null) {
                dateTimePicker.setValue(lesson.getDateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
            locationField.setText(lesson.getLocation());
            groupComboBox.setValue(lesson.getGroup());
            instructorComboBox.setValue(lesson.getInstructor());
        }

        grid.add(new Label("Тип:"), 0, 0);
        grid.add(typeField, 1, 0);
        grid.add(new Label("Дата и время:"), 0, 1);
        grid.add(dateTimePicker, 1, 1);
        grid.add(new Label("Место:"), 0, 2);
        grid.add(locationField, 1, 2);
        grid.add(new Label("Группа:"), 0, 3);
        grid.add(groupComboBox, 1, 3);
        grid.add(new Label("Инструктор:"), 0, 4);
        grid.add(instructorComboBox, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                Lesson editedLesson = new Lesson();
                editedLesson.setType(typeField.getText());
                if (dateTimePicker.getValue() != null) {
                    editedLesson.setDateTime(java.sql.Date.valueOf(dateTimePicker.getValue()));
                }
                editedLesson.setLocation(locationField.getText());
                editedLesson.setGroup(groupComboBox.getValue());
                editedLesson.setInstructor(instructorComboBox.getValue());
                if (lesson != null) {
                    editedLesson.setId(lesson.getId());
                }
                return editedLesson;
            }
            return null;
        });

        dialog.getDialogPane().getStylesheets().add(EditScheduleDialog.class.getResource("/styles.css").toExternalForm());

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

        groupComboBox.setEditable(true);
        groupComboBox.getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if (!groupComboBox.getSelectionModel().isEmpty()) {
                    groupComboBox.getEditor().setText(groupComboBox.getSelectionModel().getSelectedItem().toString());
                }
            }
        });

        groupComboBox.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent e) -> {
            groupComboBox.hide();
            if (e.getCode() == KeyCode.UP) {
                groupComboBox.show();
                return;
            } else if (e.getCode() == KeyCode.DOWN) {
                if (!groupComboBox.isShowing()) {
                    groupComboBox.show();
                }
                return;
            } else if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.ESCAPE) {
                groupComboBox.hide();
            } else {
                if (groupComboBox.getEditor().getText().length() == 0) {
                    try {
                        groupComboBox.getItems().clear();
                        groupComboBox.getItems().addAll(ApiClient.getAllGroups());
                    } catch (IOException ex) {
                        showAlert("Ошибка", "Ошибка при загрузке данных групп: " + ex.getMessage());
                    }
                    groupComboBox.show();
                } else {
                    ObservableList<Group> filteredGroups = FXCollections.observableArrayList();
                    try {
                        for (Group group : ApiClient.getAllGroups()) {
                            if (group.getName().toLowerCase().contains(groupComboBox.getEditor().getText().toLowerCase())) {
                                filteredGroups.add(group);
                            }
                        }
                    } catch (IOException ex) {
                        showAlert("Ошибка", "Ошибка при загрузке данных групп: " + ex.getMessage());
                    }
                    groupComboBox.getItems().clear();
                    groupComboBox.getItems().addAll(filteredGroups);
                    groupComboBox.show();
                }
            }
        });

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
