package com.example.autoschool.view;

import com.example.autoschool.model.Group;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.util.Optional;
import java.time.ZoneId;

/**
 * Диалоговое окно для редактирования групп.
 */
public class EditGroupDialog {

    public static Optional<Group> showGroupDialog(Group group) {
        Dialog<Group> dialog = new Dialog<>();
        dialog.setTitle("Редактирование группы");
        dialog.setHeaderText("Пожалуйста, введите данные группы");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Название");
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Дата начала");
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("Дата окончания");

        if (group != null) {
            nameField.setText(group.getName());
            if (group.getStartDate() != null) {
                startDatePicker.setValue(group.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
            if (group.getEndDate() != null) {
                endDatePicker.setValue(group.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
        }

        grid.add(new Label("Название:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Дата начала:"), 0, 1);
        grid.add(startDatePicker, 1, 1);
        grid.add(new Label("Дата окончания:"), 0, 2);
        grid.add(endDatePicker, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                Group editedGroup = new Group();
                editedGroup.setName(nameField.getText());
                if (startDatePicker.getValue() != null) {
                    editedGroup.setStartDate(java.sql.Date.valueOf(startDatePicker.getValue()));
                }
                if (endDatePicker.getValue() != null) {
                    editedGroup.setEndDate(java.sql.Date.valueOf(endDatePicker.getValue()));
                }
                if (group != null) {
                    editedGroup.setId(group.getId());
                }
                return editedGroup;
            }
            return null;
        });

        dialog.getDialogPane().getStylesheets().add(GroupDialog.class.getResource("/styles.css").toExternalForm());

        Optional<Group> result = dialog.showAndWait();

        return result;
    }
}
