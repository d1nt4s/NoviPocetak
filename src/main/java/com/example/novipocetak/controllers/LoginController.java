package com.example.novipocetak.controllers;

import com.example.novipocetak.util.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.sql.*;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private TextField jmbgField;

    public void login(ActionEvent event) {
        String email = emailField.getText();
        String jmbg = jmbgField.getText();

        try (Connection conn = Database.connect()) {
            String sql = "SELECT * FROM Psihoterapeut WHERE imejl = ? AND JMBG = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, jmbg);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
            } else {
                showAlert("Prijava neuspešna", "Pogrešan JMBG ili email.");
            }
        } catch (Exception e) {
            showAlert("Greška", e.getMessage());
        }
    }

    public void openRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            showAlert("Greška pri prebacivanju scene", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}

