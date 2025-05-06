package com.example.novipocetak.controller;

import com.example.novipocetak.util.Database;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import java.sql.*;

public class RegisterController {
    @FXML private TextField imeField;
    @FXML private TextField prezimeField;
    @FXML private TextField jmbgField;
    @FXML private TextField emailField;
    @FXML private TextField telefonField;

    public void register() {
        try (Connection conn = Database.connect()) {
            String sql = "INSERT INTO Psihoterapeut (datum_specifikacije, ime, prezime, JMBG, imejl, telefon, OblastPsihoterapije_oblastPsih_id) VALUES (CURDATE(), ?, ?, ?, ?, ?, 1)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, imeField.getText());
            stmt.setString(2, prezimeField.getText());
            stmt.setString(3, jmbgField.getText());
            stmt.setString(4, emailField.getText());
            stmt.setString(5, telefonField.getText());
            stmt.executeUpdate();
            showAlert("Uspešno", "Registracija uspešna!");
        } catch (Exception e) {
            showAlert("Greška", e.getMessage());
        }
    }

    public void openLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            showAlert("Greška pri prebacivanju scene", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}