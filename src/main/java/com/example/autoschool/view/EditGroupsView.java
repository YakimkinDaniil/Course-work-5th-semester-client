package com.example.autoschool.view;

import com.example.autoschool.ApiClient;
import com.example.autoschool.model.Group;
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
import java.util.Optional;

/**
 * Панель для редактирования групп.
 */
public class EditGroupsView extends VBox {
    private TableView<Group> tableView;
    private ObservableList<Group> groups;
    private TextField searchField;

    public EditGroupsView() {
        getStyleClass().add("edit-groups-view");

        tableView = new TableView<>();
        tableView.getStyleClass().add("table-view");

        TableColumn<Group, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.getStyleClass().add("column-header");

        TableColumn<Group, Date> startDateColumn = new TableColumn<>("Дата начала");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startDateColumn.setCellFactory(param -> new TableCell<Group, Date>() {
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
        startDateColumn.getStyleClass().add("column-header");

        TableColumn<Group, Date> endDateColumn = new TableColumn<>("Дата окончания");
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endDateColumn.setCellFactory(param -> new TableCell<Group, Date>() {
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
        endDateColumn.getStyleClass().add("column-header");

        tableView.getColumns().addAll(nameColumn, startDateColumn, endDateColumn);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        try {
            List<Group> groupList = ApiClient.getAllGroups();
            groups = FXCollections.observableArrayList(groupList);
            tableView.setItems(groups);
        } catch (IOException e) {
            showAlert("Ошибка", "Ошибка при загрузке данных групп: " + e.getMessage());
        }

        searchField = new TextField();
        searchField.setPromptText("Поиск по названию");
        searchField.setOnKeyReleased(event -> filterGroups());

        HBox filterBox = new HBox(10, new Label("Поиск по названию:"), searchField);
        filterBox.setAlignment(javafx.geometry.Pos.CENTER);

        Button editButton = new Button("Редактировать группу");
        editButton.getStyleClass().add("edit-button");
        editButton.setOnAction(event -> {
            Group selectedGroup = tableView.getSelectionModel().getSelectedItem();
            if (selectedGroup != null) {
                openEditGroupDialog(selectedGroup);
            } else {
                showAlert("Ошибка", "Выберите группу для редактирования.");
            }
        });

        Button backButton = new Button("Назад");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(event -> {
            Stage stage = (Stage) getScene().getWindow();
            stage.close();
        });

        getChildren().addAll(filterBox, tableView, editButton, backButton);
        setAlignment(javafx.geometry.Pos.CENTER);
        setSpacing(20);
    }

    private void openEditGroupDialog(Group group) {
        Optional<Group> result = EditGroupDialog.showGroupDialog(group);
        result.ifPresent(editedGroup -> {
            try {
                ApiClient.updateGroup(editedGroup.getId(), editedGroup);
                groups.setAll(ApiClient.getAllGroups());
                showAlert("Успех", "Группа успешно отредактирована!");
            } catch (IOException e) {
                showAlert("Ошибка", "Ошибка при редактировании группы: " + e.getMessage());
            }
        });
    }

    private void filterGroups() {
        String query = searchField.getText().toLowerCase();
        ObservableList<Group> filteredList = FXCollections.observableArrayList();
        for (Group group : groups) {
            if (group.getName().toLowerCase().contains(query)) {
                filteredList.add(group);
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
