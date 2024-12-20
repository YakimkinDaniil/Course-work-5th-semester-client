package com.example.autoschool.view;

import com.example.autoschool.ApiClient;
import com.example.autoschool.model.Result;
import com.example.autoschool.model.ResultDTO;
import com.example.autoschool.model.Student;
import com.example.autoschool.model.StudentDTO;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Диалоговое окно для добавления оценок.
 */
public class GradeDialog extends GridPane {
    private boolean isEdit;
    private ResultDTO selectedResult;
    private boolean saved = false;

    public GradeDialog(boolean isEdit, ResultDTO selectedResult) {
        this.isEdit = isEdit;
        this.selectedResult = selectedResult;
        setPadding(new javafx.geometry.Insets(20));
        setHgap(10);
        setVgap(10);

        TextField typeField = new TextField();
        typeField.setPromptText("Введите тип оценки");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Выберите дату");

        TextField gradeField = new TextField();
        gradeField.setPromptText("Введите оценку");

        TextField studentIdField = new TextField();
        studentIdField.setPromptText("Введите ID студента");

        if (isEdit && selectedResult != null) {
            typeField.setText(selectedResult.getType());
            datePicker.setValue(convertToLocalDate(selectedResult.getDate()));
            gradeField.setText(selectedResult.getGrade());
            studentIdField.setText(String.valueOf(selectedResult.getStudent().getId()));
        }

        Button saveButton = new Button(isEdit ? "Сохранить изменения" : "Добавить оценку");
        saveButton.setOnAction(event -> {
            try {
                ResultDTO result = new ResultDTO();
                result.setType(typeField.getText());
                result.setDate(convertToDate(datePicker.getValue()));
                result.setGrade(gradeField.getText());

                if (result.getType() == null || result.getType().isEmpty()) {
                    showAlert("Ошибка", "Пожалуйста, введите тип оценки.");
                    return;
                }

                StudentDTO studentDTO = new StudentDTO();
                studentDTO.setId(Long.parseLong(studentIdField.getText()));
                result.setStudent(studentDTO);

                if (isEdit && selectedResult != null) {
                    result.setId(selectedResult.getId());
                    ApiClient.updateResult(result.getId(), convertToResult(result));
                    this.saved = true;
                    this.selectedResult = result;
                } else {
                    ApiClient.createResult(convertToResult(result));
                    this.saved = true;
                    this.selectedResult = result;
                }

                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();
            } catch (IOException e) {
                showAlert("Ошибка", "Ошибка при сохранении оценки: " + e.getMessage());
                this.saved = false;
            }
        });

        add(new Label("Тип:"), 0, 0);
        add(typeField, 1, 0);
        add(new Label("Дата:"), 0, 1);
        add(datePicker, 1, 1);
        add(new Label("Оценка:"), 0, 2);
        add(gradeField, 1, 2);
        add(new Label("ID Студента:"), 0, 3);
        add(studentIdField, 1, 3);
        add(saveButton, 1, 4);
    }

    public boolean isSaved() {
        return saved;
    }

    public ResultDTO getResult() {
        return selectedResult;
    }
    private Result convertToResult(ResultDTO resultDto) {
        Result result = new Result();
        result.setType(resultDto.getType());
        result.setDate(resultDto.getDate());
        result.setGrade(resultDto.getGrade());

        if (resultDto.getStudent() != null && resultDto.getStudent().getId() != null) {
            Student student = new Student();
            student.setId(resultDto.getStudent().getId());
            result.setStudent(student);
        } else {
            throw new IllegalArgumentException("ID студента не может быть пустым");
        }

        return result;
    }

    private LocalDate convertToLocalDate(Date utilDate) {
        return utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
