package com.example.autoschool.view;

import com.example.autoschool.model.Instructor;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.util.Optional;

/**
 * Диалоговое окно для добавления инструкторов.
 */
public class InstructorDialog {
    public static Optional<Instructor> showInstructorDialog() {
        Dialog<Instructor> dialog = new Dialog<>();
        dialog.setTitle("Добавление инструктора");
        dialog.setHeaderText("Пожалуйста, введите данные инструктора");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Имя");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Фамилия");
        TextField specializationField = new TextField();
        specializationField.setPromptText("Специальность");
        TextField contactInfoField = new TextField();
        contactInfoField.setPromptText("Контактная информация");

        grid.add(new Label("Имя:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Фамилия:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Специальность:"), 0, 2);
        grid.add(specializationField, 1, 2);
        grid.add(new Label("Контактная информация:"), 0, 3);
        grid.add(contactInfoField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                Instructor instructor = new Instructor();
                instructor.setFirstName(firstNameField.getText());
                instructor.setLastName(lastNameField.getText());
                instructor.setSpecialization(specializationField.getText());
                instructor.setContactInfo(contactInfoField.getText());
                return instructor;
            }
            return null;
        });

        dialog.getDialogPane().getStylesheets().add(InstructorDialog.class.getResource("/styles.css").toExternalForm());
        Optional<Instructor> result = dialog.showAndWait();
        return result;
    }

    public static Optional<Instructor> showDeleteInstructorDialog(Instructor instructor) {
        Dialog<Instructor> dialog = new Dialog<>();
        dialog.setTitle("Удаление инструктора");
        dialog.setHeaderText("Подтвердите удаление инструктора");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Имя");
        firstNameField.setText(instructor.getFirstName());
        firstNameField.setEditable(false);
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Фамилия");
        lastNameField.setText(instructor.getLastName());
        lastNameField.setEditable(false);
        TextField specializationField = new TextField();
        specializationField.setPromptText("Специальность");
        specializationField.setText(instructor.getSpecialization());
        specializationField.setEditable(false);
        TextField contactInfoField = new TextField();
        contactInfoField.setPromptText("Контактная информация");
        contactInfoField.setText(instructor.getContactInfo());
        contactInfoField.setEditable(false);

        grid.add(new Label("Имя:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Фамилия:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Специальность:"), 0, 2);
        grid.add(specializationField, 1, 2);
        grid.add(new Label("Контактная информация:"), 0, 3);
        grid.add(contactInfoField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return instructor;
            }
            return null;
        });

        dialog.getDialogPane().getStylesheets().add(InstructorDialog.class.getResource("/styles.css").toExternalForm());
        Optional<Instructor> result = dialog.showAndWait();
        return result;
    }
}
