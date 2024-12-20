package com.example.autoschool;

import com.example.autoschool.model.UserDto;
import com.example.autoschool.view.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.OkHttpClient;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Основное приложение автошколы.
 */
public class MainApp extends Application {
    private static final String BASE_URL = "http://localhost:8080";
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .build();
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private static String token;
    private static String adminName;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Автошкола");
        BorderPane root = new BorderPane();
        root.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);

        VBox authLayout = new VBox(10);
        authLayout.getStyleClass().add("auth-container");
        Label welcomeLabel = new Label("Добро пожаловать в Автошколу!");
        Button loginButton = new Button("Авторизация");
        Button registerButton = new Button("Регистрация");
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        buttonBox.getChildren().addAll(loginButton, registerButton);
        authLayout.getChildren().addAll(welcomeLabel, buttonBox);
        authLayout.setAlignment(javafx.geometry.Pos.CENTER);
        root.setCenter(authLayout);

        Button aboutButton = new Button("Об авторе");
        aboutButton.setOnAction(event -> AboutDialog.showAboutDialog());
        HBox aboutBox = new HBox(aboutButton);
        aboutBox.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        root.setTop(aboutBox);

        loginButton.setOnAction(event -> {
            Optional<UserDto> result = AuthDialogs.showLoginDialog();
            result.ifPresent(userDto -> {
                try {
                    String response = ApiClient.loginUser(userDto.getUsername(), userDto.getPassword());
                    if (response != null && !response.isEmpty()) {
                        String role = getUserRole(userDto.getUsername());
                        if (role != null) {
                            openMainWindow(role, primaryStage, userDto.getUsername());
                        } else {
                            showAlert("Ошибка", "Не удалось определить роль пользователя.");
                        }
                    } else {
                        showAlert("Ошибка", "Неверно введенные данные!");
                    }
                } catch (IOException e) {
                    showAlert("Ошибка", "Ошибка при авторизации");
                }
            });
        });

        registerButton.setOnAction(event -> {
            Optional<UserDto> result = AuthDialogs.showRegisterDialog();
            result.ifPresent(userDto -> {
                try {
                    String response = ApiClient.registerUser(userDto.getUsername(), userDto.getPassword(), userDto.getRole());
                    showAlert("Регистрация", response);
                } catch (IOException e) {
                    showAlert("Ошибка", "Ошибка при регистрации");
                }
            });
        });

        primaryStage.show();
    }

    /**
     * Открывает главное окно приложения в зависимости от роли пользователя.
     *
     * @param role Роль пользователя.
     * @param primaryStage Основная сцена.
     * @param username Имя пользователя.
     */
    private void openMainWindow(String role, Stage primaryStage, String username) {
        BorderPane root = new BorderPane();
        root.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        Scene scene;
        switch (role) {
            case "ROLE_ADMIN":
                adminName = username;
                root.setCenter(new AdminView(primaryStage, adminName));
                break;
            case "ROLE_STUDENT":
                root.setCenter(new StudentView(primaryStage, username));
                break;
            case "ROLE_INSTRUCTOR":
                root.setCenter(new InstructorView(primaryStage, username));
                break;
            default:
                root.setCenter(new Label("Неизвестная роль"));
                break;
        }
        scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Получает роль пользователя по имени пользователя.
     *
     * @param username Имя пользователя.
     * @return Роль пользователя.
     * @throws IOException если произошла ошибка при выполнении запроса.
     */
    private String getUserRole(String username) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/auth/userrole?username=" + username)
                .addHeader("Authorization", "Bearer " + MainApp.getToken())
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String role = response.body().string();
            if (role == null || role.isEmpty()) {
                throw new IOException("Роль не найдена");
            }
            return role;
        } catch (IOException e) {
            throw e;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Устанавливает токен авторизации.
     *
     * @param token Токен авторизации.
     */
    public static void setToken(String token) {
        MainApp.token = token;
    }

    /**
     * Возвращает токен авторизации.
     *
     * @return Токен авторизации.
     */
    public static String getToken() {
        return token;
    }

    /**
     * Запускает приложение.
     *
     * @param args Аргументы командной строки.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
