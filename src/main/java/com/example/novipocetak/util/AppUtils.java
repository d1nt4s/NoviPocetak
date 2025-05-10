package com.example.novipocetak.util;

import javafx.scene.control.Alert;

public class AppUtils {

    public static void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null); // можно убрать или задать
        alert.setContentText(message);
        alert.show();
    }


    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
