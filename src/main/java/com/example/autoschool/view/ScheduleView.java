package com.example.autoschool.view;

import com.example.autoschool.model.Lesson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Панель для просмотра расписания.
 */
public class ScheduleView extends VBox {
    private TableView<Lesson> tableView;
    private TextField searchField;
    private ObservableList<Lesson> lessons;

    public ScheduleView(List<Lesson> lessons) {
        getStyleClass().add("schedule-view");

        tableView = new TableView<>();
        tableView.getStyleClass().add("table-view");

        TableColumn<Lesson, String> typeColumn = new TableColumn<>("Тип");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.getStyleClass().add("column-header");

        TableColumn<Lesson, Date> dateTimeColumn = new TableColumn<>("Дата занятия");
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        dateTimeColumn.setCellFactory(param -> new TableCell<Lesson, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    setText(dateFormat.format(item));
                }
            }
        });
        dateTimeColumn.getStyleClass().add("column-header");

        TableColumn<Lesson, String> locationColumn = new TableColumn<>("Место");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationColumn.getStyleClass().add("column-header");

        tableView.getColumns().addAll(typeColumn, dateTimeColumn, locationColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.lessons = FXCollections.observableArrayList(lessons);
        tableView.setItems(this.lessons);

        searchField = new TextField();
        searchField.setPromptText("Поиск по типу или месту");
        searchField.setOnKeyReleased(event -> filterLessons());

        HBox filterBox = new HBox(10, new Label("Поиск по типу или месту:"), searchField);
        filterBox.setAlignment(javafx.geometry.Pos.CENTER);

        Button backButton = new Button("Назад");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(event -> {
            Stage stage = (Stage) getScene().getWindow();
            stage.close();
        });

        getChildren().addAll(filterBox, tableView, backButton);
        setAlignment(javafx.geometry.Pos.CENTER);
        setSpacing(20);
    }

    private void filterLessons() {
        String query = searchField.getText().toLowerCase();
        ObservableList<Lesson> filteredList = FXCollections.observableArrayList();
        for (Lesson lesson : lessons) {
            if (lesson.getType().toLowerCase().contains(query) ||
                    lesson.getLocation().toLowerCase().contains(query)) {
                filteredList.add(lesson);
            }
        }
        tableView.setItems(filteredList);
    }
}
