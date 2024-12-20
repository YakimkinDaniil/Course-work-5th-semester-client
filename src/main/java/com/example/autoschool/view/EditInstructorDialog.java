package com.example.autoschool.view;

import com.example.autoschool.model.Instructor;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.util.Optional;

/**
 * Диалоговое окно для редактирования инструкторов.
 */
public class EditInstructorDialog {

    public static Optional<Instructor> showEditInstructorDialog(Instructor instructor) {
        Dialog<Instructor> dialog = new Dialog<>();
        dialog.setTitle("Редактирование инструктора");
        dialog.setHeaderText("Пожалуйста, введите данные инструктора");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField firstNameField = new TextField(instructor.getFirstName());
        firstNameField.setPromptText("Имя");
        TextField lastNameField = new TextField(instructor.getLastName());
        lastNameField.setPromptText("Фамилия");
        TextField specializationField = new TextField(instructor.getSpecialization());
        specializationField.setPromptText("Специализация");
        TextField contactInfoField = new TextField(instructor.getContactInfo());
        contactInfoField.setPromptText("Контактная информация");

        grid.add(new Label("Имя:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Фамилия:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Специализация:"), 0, 2);
        grid.add(specializationField, 1, 2);
        grid.add(new Label("Контактная информация:"), 0, 3);
        grid.add(contactInfoField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                instructor.setFirstName(firstNameField.getText());
                instructor.setLastName(lastNameField.getText());
                instructor.setSpecialization(specializationField.getText());
                instructor.setContactInfo(contactInfoField.getText());
                return instructor;
            }
            return null;
        });

        dialog.getDialogPane().getStylesheets().add(EditInstructorDialog.class.getResource("/styles.css").toExternalForm());

        Optional<Instructor> result = dialog.showAndWait();

        return result;
    }
}

