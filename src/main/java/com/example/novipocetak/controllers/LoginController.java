package com.example.novipocetak.controllers;

import com.example.novipocetak.util.Database;
import com.example.novipocetak.util.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.sql.*;
import com.example.novipocetak.util.AppUtils;

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
                Session.setLoggedInEmail(email);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/novipocetak/therapist-menu.fxml"));
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
            } else {
                AppUtils.showAlert("Prijava neuspešna", "Pogrešan JMBG ili email.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppUtils.showAlert("Greška", e.getMessage());
        }
    }

    public void openRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/novipocetak/register.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            AppUtils.showAlert("Greška pri prebacivanju scene", e.getMessage());
        }
    }

}

