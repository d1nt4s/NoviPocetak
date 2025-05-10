package com.example.novipocetak.controllers;

import com.example.novipocetak.util.Database;
import com.example.novipocetak.util.AppUtils;
import com.example.novipocetak.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TherapistProfileController {

    @FXML private TextField imeField;
    @FXML private TextField prezimeField;
    @FXML private TextField jmbgField;
    @FXML private TextField emailField;
    @FXML private TextField telefonField;

    private int therapistId;

    @FXML
    public void initialize() {
        try (Connection conn = Database.connect()) {
            String sql = "SELECT * FROM Psihoterapeut WHERE imejl = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, Session.getLoggedInEmail());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                therapistId = rs.getInt("psihoterapeut_id");
                imeField.setText(rs.getString("ime"));
                prezimeField.setText(rs.getString("prezime"));
                jmbgField.setText(rs.getString("JMBG"));
                emailField.setText(rs.getString("imejl"));
                telefonField.setText(rs.getString("telefon"));
            }
        } catch (Exception e) {
            AppUtils.showAlert("Greška pri učitavanju profila", e.getMessage());
        }
    }

    public void saveChanges() {
        try (Connection conn = Database.connect()) {
            String update = "UPDATE Psihoterapeut SET ime = ?, prezime = ?, telefon = ? WHERE psihoterapeut_id = ?";
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.setString(1, imeField.getText());
            stmt.setString(2, prezimeField.getText());
            stmt.setString(3, telefonField.getText());
            stmt.setInt(4, therapistId);
            stmt.executeUpdate();
            AppUtils.showSuccess("Uspešno", "Podaci su sačuvani.");
        } catch (Exception e) {
            AppUtils.showAlert("Greška pri čuvanju", e.getMessage());
        }
    }
}
