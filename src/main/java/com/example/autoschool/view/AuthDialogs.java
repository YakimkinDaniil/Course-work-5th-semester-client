package com.example.autoschool.view;

import com.example.autoschool.model.UserDto;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Dialog;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import java.util.Optional;

/**
 * Диалоговое окно для авторизации и регистрации.
 */
public class AuthDialogs {
    public static Optional<UserDto> showLoginDialog() {
        Dialog<UserDto> dialog = new Dialog<>();
        dialog.setTitle("Авторизация");
        dialog.setHeaderText("Введите данные для авторизации");
        ButtonType loginButtonType = new ButtonType("Войти", ButtonData.OK_DONE);
        ButtonType backButtonType = new ButtonType("Назад", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, backButtonType);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField username = new TextField();
        username.setPromptText("Логин");
        PasswordField password = new PasswordField();
        password.setPromptText("Пароль");
        grid.add(new Label("Логин:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Пароль:"), 0, 1);
        grid.add(password, 1, 1);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(new Callback<ButtonType, UserDto>() {
            @Override
            public UserDto call(ButtonType buttonType) {
                if (buttonType == loginButtonType) {
                    return new UserDto(username.getText(), password.getText(), null);
                }
                return null;
            }
        });

        dialog.getDialogPane().getStylesheets().add(AuthDialogs.class.getResource("/styles.css").toExternalForm());
        Button aboutButton = new Button("Об авторе");
        aboutButton.setOnAction(event -> AboutDialog.showAboutDialog());
        HBox aboutBox = new HBox(aboutButton);
        aboutBox.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        dialog.getDialogPane().setGraphic(aboutBox);

        return dialog.showAndWait();
    }

    public static Optional<UserDto> showRegisterDialog() {
        Dialog<UserDto> dialog = new Dialog<>();
        dialog.setTitle("Регистрация");
        dialog.setHeaderText("Введите данные для регистрации");
        ButtonType registerButtonType = new ButtonType("Зарегистрироваться", ButtonData.OK_DONE);
        ButtonType backButtonType = new ButtonType("Назад", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, backButtonType);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField username = new TextField();
        username.setPromptText("Логин");
        PasswordField password = new PasswordField();
        password.setPromptText("Пароль");
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("ROLE_INSTRUCTOR", "ROLE_STUDENT");
        roleComboBox.setPromptText("Выберите роль");
        grid.add(new Label("Логин:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Пароль:"), 0, 1);
        grid.add(password, 1, 1);
        grid.add(new Label("Роль:"), 0, 2);
        grid.add(roleComboBox, 1, 2);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(new Callback<ButtonType, UserDto>() {
            @Override
            public UserDto call(ButtonType buttonType) {
                if (buttonType == registerButtonType) {
                    return new UserDto(username.getText(), password.getText(), roleComboBox.getValue());
                }
                return null;
            }
        });

        dialog.getDialogPane().getStylesheets().add(AuthDialogs.class.getResource("/styles.css").toExternalForm());
        Button aboutButton = new Button("Об авторе");
        aboutButton.setOnAction(event -> AboutDialog.showAboutDialog());
        HBox aboutBox = new HBox(aboutButton);
        aboutBox.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        dialog.getDialogPane().setGraphic(aboutBox);
        return dialog.showAndWait();
    }
}
