package com.example.autoschool.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Диалоговое окно для отображения информации об авторе.
 */
public class AboutDialog {
    /**
     * Отображает диалоговое окно с информацией об авторе.
     */
    public static void showAboutDialog() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Об авторе");
        alert.setHeaderText(null);
        alert.setContentText("Автор: Якимкин Д.Г.\nГруппа: ПИ22-1\nПочта: 225011@edu.fa.ru");
        alert.showAndWait();
    }
}
