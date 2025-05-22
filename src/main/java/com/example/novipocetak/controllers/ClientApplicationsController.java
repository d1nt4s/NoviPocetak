package com.example.novipocetak.controllers;

import com.example.novipocetak.model.Klijent;
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

public class ClientApplicationsController {
    @FXML private TableView<Klijent> clientsTable;
    @FXML private TableColumn<Klijent, String> imeColumn;
    @FXML private TableColumn<Klijent, String> prezimeColumn;
    @FXML private TableColumn<Klijent, String> polColumn;
    @FXML private TableColumn<Klijent, String> telefonColumn;

    @FXML private TextField imeField, prezimeField, polField, imejlField, telefonField;
    @FXML private DatePicker dobPicker;
    @FXML private TextArea problemField;

    private ObservableList<Klijent> clients = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        imeColumn.setCellValueFactory(data -> data.getValue().imeProperty());
        prezimeColumn.setCellValueFactory(data -> data.getValue().prezimeProperty());
        polColumn.setCellValueFactory(data -> data.getValue().polProperty());
        telefonColumn.setCellValueFactory(data -> data.getValue().telefonProperty());
        dobPicker.setValue(LocalDate.of(2025, 5, 1));
        loadClients();
    }

    private void loadClients() {
        clients.clear();
        try (Connection conn = Database.connect()) {
            int psih_id = Session.getLoggedInID();
            System.out.println(psih_id);
            ResultSet rs = conn.createStatement().executeQuery("SELECT \n" +
                    "    p.psihoterapeut_id,\n" +
                    "    CONCAT(p.ime, ' ', p.prezime) AS therapist_name,\n" +
                    "    k.*,\n" +
                    "    COUNT(*) AS session_count\n" +
                    "FROM seansa s\n" +
                    "JOIN Psihoterapeut p ON s.Psihoterapeut_psihoterapeut_id = p.psihoterapeut_id\n" +
                    "JOIN Klijent k ON s.Klijent_klijent_id = k.klijent_id\n" +
                    "WHERE p.psihoterapeut_id = " + psih_id + "\n" +
                    "GROUP BY p.psihoterapeut_id, k.klijent_id;\n");
            while (rs.next()) {
                clients.add(new Klijent(
                        rs.getInt("klijent_id"),
                        rs.getString("ime"),
                        rs.getString("prezime"),
                        rs.getDate("dob").toLocalDate(),
                        rs.getString("pol"),
                        rs.getString("imejl"),
                        rs.getString("telefon"),
                        rs.getBoolean("proslo_iskustvo"),
                        rs.getString("problem")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        clientsTable.setItems(clients);
    }

    public void addClient() {
        try (Connection conn = Database.connect()) {
            if (imeField.getText().isEmpty() || prezimeField.getText().isEmpty() || polField.getText().isEmpty()
                    || imejlField.getText().isEmpty() || telefonField.getText().isEmpty() || dobPicker.getValue() == null) {
                showAlert("Greška", "Molimo vas da popunite sva polja.");
                return;
            }


            String sql = "INSERT INTO Klijent (ime, prezime, dob, pol, imejl, telefon, proslo_iskustvo, problem) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, imeField.getText());
            stmt.setString(2, prezimeField.getText());
            stmt.setDate(3, Date.valueOf(dobPicker.getValue()));
            stmt.setString(4, polField.getText());
            stmt.setString(5, imejlField.getText());
            stmt.setString(6, telefonField.getText());
            stmt.setBoolean(7, false);
            stmt.setString(8, problemField.getText());
            stmt.executeUpdate();
            loadClients();
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

