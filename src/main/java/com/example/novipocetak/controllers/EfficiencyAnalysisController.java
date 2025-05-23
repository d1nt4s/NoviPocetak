package com.example.novipocetak.controllers;

import com.example.novipocetak.util.Database;
import com.example.novipocetak.util.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.example.novipocetak.util.AppUtils.showAlert;

public class EfficiencyAnalysisController {
    @FXML private Label sessionCountLabel;
    @FXML private Label avgDurationLabel;
    @FXML private Label uniqueClientsLabel;
    @FXML private Label publishedPercentLabel;

    @FXML
    public void initialize() {
        int id = Session.getLoggedInID();
        try (Connection conn = Database.connect()) {
            PreparedStatement stmt = conn.prepareStatement("""
                SELECT COUNT(*) AS total_sessions,
                       AVG(trajanje) AS avg_duration,
                       COUNT(DISTINCT Klijent_klijent_id) AS unique_clients
                FROM seansa
                WHERE Psihoterapeut_psihoterapeut_id = ?
            """);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                sessionCountLabel.setText("Broj seansi: " + rs.getInt("total_sessions"));
                avgDurationLabel.setText("Prosečno trajanje: " + String.format("%.2f", rs.getDouble("avg_duration")) + " min");
                uniqueClientsLabel.setText("Broj klijenata: " + rs.getInt("unique_clients"));
            }

            PreparedStatement pubStmt = conn.prepareStatement("""
                SELECT COUNT(*) AS ukupno,
                       SUM(CASE WHEN o.objavljivanje_id IS NOT NULL THEN 1 ELSE 0 END) AS objavljeno
                FROM seansa s
                LEFT JOIN objavljivanjepodataka o ON o.Seansa_seansa_id = s.seansa_id
                WHERE s.Psihoterapeut_psihoterapeut_id = ?
            """);
            pubStmt.setInt(1, id);
            ResultSet rs2 = pubStmt.executeQuery();
            if (rs2.next()) {
                int total = rs2.getInt("ukupno");
                int pub = rs2.getInt("objavljeno");
                double percent = total == 0 ? 0 : (pub * 100.0) / total;
                publishedPercentLabel.setText("Objavljeni podaci: " + String.format("%.1f", percent) + "%");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openTherapeutMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/novipocetak/therapist-menu.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            showAlert("Greška pri prebacivanju scene", e.getMessage());
        }
    }
}
