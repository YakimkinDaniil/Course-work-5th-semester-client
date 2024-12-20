package com.example.autoschool.view;

import com.example.autoschool.model.ApplicationDTO;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Диалоговое окно для подачи заявки на вождение.
 */
public class ApplicationDialog {

    public static Optional<ApplicationDTO> showApplicationDialog() {
        Dialog<ApplicationDTO> dialog = new Dialog<>();
        dialog.setTitle("Подать заявку на вождение");
        dialog.setHeaderText("Пожалуйста, введите данные заявки");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField studentNameField = new TextField();
        studentNameField.setPromptText("Имя студента");
        TextField contactInfoField = new TextField();
        contactInfoField.setPromptText("Контактная информация");
        DatePicker applicationDatePicker = new DatePicker();
        applicationDatePicker.setPromptText("Дата заявки");

        grid.add(new javafx.scene.control.Label("Имя студента:"), 0, 0);
        grid.add(studentNameField, 1, 0);
        grid.add(new javafx.scene.control.Label("Контактная информация:"), 0, 1);
        grid.add(contactInfoField, 1, 1);
        grid.add(new javafx.scene.control.Label("Дата заявки:"), 0, 2);
        grid.add(applicationDatePicker, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                ApplicationDTO applicationDTO = new ApplicationDTO();
                applicationDTO.setStudentName(studentNameField.getText());
                applicationDTO.setContactInfo(contactInfoField.getText());
                LocalDate localDate = applicationDatePicker.getValue();
                if (localDate != null) {
                    applicationDTO.setApplicationDate(localDate);
                }
                return applicationDTO;
            }
            return null;
        });

        dialog.getDialogPane().getStylesheets().add(ApplicationDialog.class.getResource("/styles.css").toExternalForm());
        Optional<ApplicationDTO> result = dialog.showAndWait();
        return result;
    }
}
