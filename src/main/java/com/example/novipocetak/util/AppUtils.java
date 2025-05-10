package com.example.novipocetak.util;

import javafx.scene.control.Alert;

public class AppUtils {

    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
