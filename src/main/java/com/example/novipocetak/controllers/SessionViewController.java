package com.example.novipocetak.controllers;

import com.example.novipocetak.model.Seansa;
import com.example.novipocetak.util.Database;
import com.example.novipocetak.util.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

import static com.example.novipocetak.util.AppUtils.showAlert;

public class SessionViewController {
    @FXML private TableView<Seansa> sessionsTable;
    @FXML private TableColumn<Seansa, Integer> idColumn;
    @FXML private TableColumn<Seansa, LocalDate> dateColumn;
    @FXML private TableColumn<Seansa, String> timeColumn;
    @FXML private TableColumn<Seansa, Integer> durationColumn;
    @FXML private TableColumn<Seansa, String> notesColumn;
    @FXML private TableColumn<Seansa, String> clientColumn;

    @FXML private RadioButton pastSessionsRadio;
    @FXML private RadioButton futureSessionsRadio;

    private final ObservableList<Seansa> sessions = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        dateColumn.setCellValueFactory(data -> data.getValue().datumProperty());
        timeColumn.setCellValueFactory(data -> data.getValue().vremeProperty());
        durationColumn.setCellValueFactory(data -> data.getValue().trajanjeProperty().asObject());
        notesColumn.setCellValueFactory(data -> data.getValue().beleskeProperty());
        clientColumn.setCellValueFactory(data -> data.getValue().klijentProperty());

        loadSessions(true);

        ToggleGroup group = new ToggleGroup();
        pastSessionsRadio.setToggleGroup(group);
        futureSessionsRadio.setToggleGroup(group);

        group.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            loadSessions(newToggle == pastSessionsRadio);
        });
    }

    private void loadSessions(boolean past) {
        sessions.clear();
        try (Connection conn = Database.connect()) {
            int psih_id = Session.getLoggedInID();
            String sql = """
                SELECT s.seansa_id, s.datum, s.vreme, s.trajanje, s.beleske,
                       k.ime, k.prezime
                FROM seansa s
                JOIN klijent k ON s.Klijent_klijent_id = k.klijent_id
                WHERE s.Psihoterapeut_psihoterapeut_id = ?
                  AND s.datum """ + (past ? "< ?" : ">= ?");

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, psih_id);
            stmt.setDate(2, Date.valueOf(LocalDate.now()));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sessions.add(new Seansa(
                        rs.getInt("seansa_id"),
                        rs.getDate("datum").toLocalDate(),
                        rs.getString("vreme"),
                        rs.getInt("trajanje"),
                        rs.getString("beleske"),
                        rs.getString("ime") + " " + rs.getString("prezime")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Greška", "Došlo je do greške prilikom učitavanja seansi.");
        }
        sessionsTable.setItems(sessions);
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
