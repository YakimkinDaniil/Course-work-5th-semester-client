package com.example.autoschool.view;

import com.example.autoschool.model.ResultDTO;
import javafx.beans.property.SimpleStringProperty;
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
 * Панель для просмотра оценок.
 */
public class ResultsView extends VBox {
    private TableView<ResultDTO> tableView;
    private TextField searchField;
    private ObservableList<ResultDTO> results;

    public ResultsView(List<ResultDTO> results) {
        getStyleClass().add("results-view");
        tableView = new TableView<>();
        tableView.getStyleClass().add("table-view");

        TableColumn<ResultDTO, String> studentNameColumn = new TableColumn<>("Имя Студента");
        studentNameColumn.setCellValueFactory(param -> {
            ResultDTO result = param.getValue();
            if (result.getStudent() != null) {
                return new SimpleStringProperty(result.getStudent().getFirstName() + " " + result.getStudent().getLastName());
            } else {
                return new SimpleStringProperty("Unknown");
            }
        });
        studentNameColumn.getStyleClass().add("column-header");

        TableColumn<ResultDTO, Date> examDateColumn = new TableColumn<>("Дата Экзамена");
        examDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        examDateColumn.setCellFactory(param -> new TableCell<ResultDTO, Date>() {
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
        examDateColumn.getStyleClass().add("column-header");

        TableColumn<ResultDTO, String> scoreColumn = new TableColumn<>("Оценка");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        scoreColumn.getStyleClass().add("column-header");

        tableView.getColumns().addAll(studentNameColumn, examDateColumn, scoreColumn);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.results = FXCollections.observableArrayList(results);
        tableView.setItems(this.results);

        searchField = new TextField();
        searchField.setPromptText("Поиск по имени студента");
        searchField.setOnKeyReleased(event -> filterResults());

        HBox filterBox = new HBox(10, new Label("Поиск по имени студента:"), searchField);
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

    private void filterResults() {
        String query = searchField.getText().toLowerCase();
        ObservableList<ResultDTO> filteredList = FXCollections.observableArrayList();
        for (ResultDTO result : results) {
            if (result.getStudent() != null && (result.getStudent().getFirstName().toLowerCase().contains(query) ||
                    result.getStudent().getLastName().toLowerCase().contains(query))) {
                filteredList.add(result);
            }
        }
        tableView.setItems(filteredList);
    }
}
