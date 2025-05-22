package com.example.novipocetak.controllers;

import com.example.novipocetak.util.Database;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.example.novipocetak.util.AppUtils.showAlert;
import static com.example.novipocetak.util.AppUtils.showSuccess;

public class RegisterController {
    @FXML private TextField imeField;
    @FXML private TextField prezimeField;
    @FXML private TextField jmbgField;
    @FXML private TextField emailField;
    @FXML private TextField telefonField;

    public void register() {
        try (Connection conn = Database.connect()) {
            String sql = "INSERT INTO psihoterapeut " +
                    "(datum_sertifikacije, ime, prezime, JMBG, imejl, telefon, OblastPsihoterapije_oblastPsih_id) " +
                    "VALUES (CURDATE(), ?, ?, ?, ?, ?, 1)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, imeField.getText().trim());
            stmt.setString(2, prezimeField.getText().trim());
            stmt.setString(3, jmbgField.getText().trim());
            stmt.setString(4, emailField.getText().trim());
            stmt.setString(5, telefonField.getText().trim());
            stmt.executeUpdate();
            showSuccess("Uspešno", "Registracija uspešna!");
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate")) {
                showAlert("Greška", "Uneti podaci već postoje u sistemu.");
            } else {
                showAlert("Greška", e.getMessage());
            }
        }
    }

    public void openLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/novipocetak/login.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            showAlert("Greška pri prebacivanju scene", e.getMessage());
        }
    }
}
