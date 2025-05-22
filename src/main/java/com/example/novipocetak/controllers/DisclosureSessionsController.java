package com.example.novipocetak.controllers;

import com.example.novipocetak.model.SeansaDisclosure;
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

public class DisclosureSessionsController {

    @FXML private TableView<SeansaDisclosure> table;
    @FXML private TableColumn<SeansaDisclosure, LocalDate> datumCol;
    @FXML private TableColumn<SeansaDisclosure, String> vremeCol;
    @FXML private TableColumn<SeansaDisclosure, Number> trajanjeCol;
    @FXML private TableColumn<SeansaDisclosure, String> beleskeCol;
    @FXML private TableColumn<SeansaDisclosure, LocalDate> objavaDatumCol;
    @FXML private TableColumn<SeansaDisclosure, String> komeCol;
    @FXML private TableColumn<SeansaDisclosure, String> razlogCol;

    @FXML private DatePicker disclosureDateField;
    @FXML private TextField disclosedToField;
    @FXML private TextField reasonField;
    @FXML private Button saveDisclosureButton;

    private final ObservableList<SeansaDisclosure> sessionList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        datumCol.setCellValueFactory(data -> data.getValue().datumProperty());
        vremeCol.setCellValueFactory(data -> data.getValue().vremeProperty());
        trajanjeCol.setCellValueFactory(data -> data.getValue().trajanjeProperty());
        beleskeCol.setCellValueFactory(data -> data.getValue().beleskeProperty());
        objavaDatumCol.setCellValueFactory(data -> data.getValue().datumObjaveProperty());
        komeCol.setCellValueFactory(data -> data.getValue().komeProperty());
        razlogCol.setCellValueFactory(data -> data.getValue().razlogProperty());

        loadData();

        saveDisclosureButton.setOnAction(e -> saveDisclosure());
    }

    private void loadData() {
        sessionList.clear();
        int psihoterapeutId = Session.getLoggedInID();

        String sql = """
            SELECT s.seansa_id, s.datum, s.vreme, s.trajanje, s.beleske,
                   s.Klijent_klijent_id, 
                   o.datum AS objava_datum, o.kome, o.razlog
            FROM seansa s
            LEFT JOIN ObjavljivanjePodataka o ON s.seansa_id = o.Seansa_seansa_id
            WHERE s.Psihoterapeut_psihoterapeut_id = ?
        """;

        try (Connection conn = Database.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, psihoterapeutId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sessionList.add(new SeansaDisclosure(
                        rs.getInt("seansa_id"),
                        rs.getDate("datum").toLocalDate(),
                        rs.getString("vreme"),
                        rs.getInt("trajanje"),
                        rs.getString("beleske"),
                        rs.getDate("objava_datum") != null ? rs.getDate("objava_datum").toLocalDate() : null,
                        rs.getString("kome"),
                        rs.getString("razlog"),
                        rs.getInt("Klijent_klijent_id")
                ));
            }

            table.setItems(sessionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveDisclosure() {
        SeansaDisclosure selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Session Selected", "Please select a session first.");
            return;
        }

        LocalDate disclosureDate = disclosureDateField.getValue();
        String disclosedTo = disclosedToField.getText();
        String reason = reasonField.getText();

        if (disclosureDate == null || disclosedTo.isEmpty() || reason.isEmpty()) {
            showAlert("Missing Fields", "Please fill out all disclosure fields.");
            return;
        }

        String sql = "INSERT INTO ObjavljivanjePodataka (datum, kome, razlog, Seansa_seansa_id, Seansa_Kandidat_kandidat_id, Seansa_Psihoterapeut_psihoterapeut_id, Seansa_Klijent_klijent_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(disclosureDate));
            stmt.setString(2, disclosedTo);
            stmt.setString(3, reason);
            stmt.setInt(4, selected.seansaIdProperty().get());
            stmt.setNull(5, Types.INTEGER);
//            stmt.setInt(5, selected.getKandidatId());
            stmt.setInt(6, Session.getLoggedInID());
            stmt.setInt(7, selected.getKlijentId());
            stmt.executeUpdate();
            showAlert("Success", "Disclosure saved successfully.");
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
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