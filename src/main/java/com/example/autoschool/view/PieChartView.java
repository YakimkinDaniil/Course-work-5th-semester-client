package com.example.autoschool.view;

import com.example.autoschool.model.ResultDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Панель для просмотра графика статистики оценок.
 */
public class PieChartView extends VBox {
    private PieChart pieChart;
    private ObservableList<ResultDTO> results;

    public PieChartView(List<ResultDTO> results) {
        getStyleClass().add("pie-chart-view");

        this.results = FXCollections.observableArrayList(results);

        pieChart = new PieChart();
        pieChart.setTitle("Статистика оценок");

        Map<String, Long> data = results.stream()
                .collect(Collectors.groupingBy(ResultDTO::getGrade, Collectors.counting()));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Long> entry : data.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue().doubleValue()));
        }

        pieChart.setData(pieChartData);

        getChildren().add(pieChart);

        Button backButton = new Button("Назад");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(event -> {
            Stage stage = (Stage) getScene().getWindow();
            stage.close();
        });

        getChildren().add(backButton);
        setAlignment(javafx.geometry.Pos.CENTER);
        setSpacing(20);
    }
}
