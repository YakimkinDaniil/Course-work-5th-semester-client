package com.example.autoschool.view;

import com.example.autoschool.ApiClient;
import com.example.autoschool.model.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Панель для удаления заявок на вождение.
 */
public class DeleteApplicationsView extends VBox {
    private TableView<App> tableView;
    private ObservableList<App> applications;
    private TextField searchField;

    public DeleteApplicationsView() {
        getStyleClass().add("delete-applications-view");

        tableView = new TableView<>();
        tableView.getStyleClass().add("table-view");

        TableColumn<App, String> studentNameColumn = new TableColumn<>("Имя студента");
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        studentNameColumn.getStyleClass().add("column-header");

        TableColumn<App, String> contactInfoColumn = new TableColumn<>("Контактная информация");
        contactInfoColumn.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
        contactInfoColumn.getStyleClass().add("column-header");

        TableColumn<App, Date> applicationDateColumn = new TableColumn<>("Дата заявки");
        applicationDateColumn.setCellValueFactory(new PropertyValueFactory<>("applicationDate"));
        applicationDateColumn.setCellFactory(param -> new TableCell<App, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    setText(dateFormat.format(item));
                }
            }
        });
        applicationDateColumn.getStyleClass().add("column-header");

        tableView.getColumns().addAll(studentNameColumn, contactInfoColumn, applicationDateColumn);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        try {
            List<App> appList = ApiClient.getAllApplications();
            applications = FXCollections.observableArrayList(appList);
            tableView.setItems(applications);
        } catch (IOException e) {
            showAlert("Ошибка", "Ошибка при загрузке данных заявок: " + e.getMessage());
        }

        searchField = new TextField();
        searchField.setPromptText("Поиск по имени студента");
        searchField.setOnKeyReleased(event -> filterApplications());

        HBox filterBox = new HBox(10, new Label("Поиск по имени студента:"), searchField);
        filterBox.setAlignment(javafx.geometry.Pos.CENTER);

        Button deleteButton = new Button("Удалить заявку");
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setOnAction(event -> {
            App selectedApp = tableView.getSelectionModel().getSelectedItem();
            if (selectedApp != null) {
                try {
                    ApiClient.deleteApp(selectedApp.getId());
                    applications.remove(selectedApp);
                    showAlert("Успех", "Заявка успешно удалена!");
                } catch (IOException e) {
                    showAlert("Ошибка", "Ошибка при удалении заявки: " + e.getMessage());
                }
            } else {
                showAlert("Ошибка", "Выберите заявку для удаления.");
            }
        });

        Button backButton = new Button("Назад");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(event -> {
            Stage stage = (Stage) getScene().getWindow();
            stage.close();
        });

        getChildren().addAll(filterBox, tableView, deleteButton, backButton);
        setAlignment(javafx.geometry.Pos.CENTER);
        setSpacing(20);
    }

    private void filterApplications() {
        String query = searchField.getText().toLowerCase();
        ObservableList<App> filteredList = FXCollections.observableArrayList();
        for (App app : applications) {
            if (app.getStudentName().toLowerCase().contains(query)) {
                filteredList.add(app);
            }
        }
        tableView.setItems(filteredList);
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
