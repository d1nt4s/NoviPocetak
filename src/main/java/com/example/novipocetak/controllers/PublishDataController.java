package com.example.novipocetak.controllers;

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
import java.util.HashMap;
import java.util.Map;

import static com.example.novipocetak.util.AppUtils.showAlert;
import static com.example.novipocetak.util.AppUtils.showSuccess;

public class PublishDataController {

    @FXML private ComboBox<String> seansaCombo;
    @FXML private TextField komeField;
    @FXML private TextField razlogField;
    @FXML private DatePicker datumPicker;

    private final ObservableList<String> seanseList = FXCollections.observableArrayList();
    private final Map<String, int[]> seansaIdMap = new HashMap<>();

    private int currentTherapistId = Session.getLoggedInID();

    @FXML
    public void initialize() {
        try (Connection conn = Database.connect()) {
            String sql = """
                SELECT s.seansa_id, s.Kandidat_kandidat_id, s.Klijent_klijent_id, s.Psihoterapeut_psihoterapeut_id,
                       s.datum, k.ime AS k_ime, k.prezime AS k_prezime
                FROM seansa s
                JOIN klijent k ON s.Klijent_klijent_id = k.klijent_id
                WHERE s.Psihoterapeut_psihoterapeut_id = ?
                """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, currentTherapistId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int seansaId = rs.getInt("seansa_id");
                int kandidatId = rs.getInt("Kandidat_kandidat_id");
                int klijentId = rs.getInt("Klijent_klijent_id");
                int terapeutId = rs.getInt("Psihoterapeut_psihoterapeut_id");
                Date datum = rs.getDate("datum");
                String ime = rs.getString("k_ime");
                String prezime = rs.getString("k_prezime");

                String label = String.format("%s %s – Seansa %d (%s)", ime, prezime, seansaId, datum);
                seanseList.add(label);
                seansaIdMap.put(label, new int[] {seansaId, kandidatId, klijentId, terapeutId});
            }

            seansaCombo.setItems(seanseList);
        } catch (Exception e) {
            showAlert("Greška pri učitavanju seansi", e.getMessage());
        }
    }

    public void saveObjava() {
        String selected = seansaCombo.getValue();
        String kome = komeField.getText().trim();
        String razlog = razlogField.getText().trim();
        LocalDate datum = datumPicker.getValue();

        if (selected == null || kome.isEmpty() || razlog.isEmpty() || datum == null) {
            showAlert("Greška", "Sva polja su obavezna.");
            return;
        }

        int[] ids = seansaIdMap.get(selected);
        if (ids == null) {
            showAlert("Greška", "Nevažeća seansa.");
            return;
        }

        try (Connection conn = Database.connect()) {
            String insertSql = """
                INSERT INTO objavljivanjepodataka
                (datum, kome, razlog, Seansa_seansa_id, Seansa_Kandidat_kandidat_id, Seansa_Psihoterapeut_psihoterapeut_id, Seansa_Klijent_klijent_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
            PreparedStatement stmt = conn.prepareStatement(insertSql);
            stmt.setDate(1, Date.valueOf(datum));
            stmt.setString(2, kome);
            stmt.setString(3, razlog);
            stmt.setInt(4, ids[0]); // seansa_id
            stmt.setInt(5, ids[1]); // kandidat_id
            stmt.setInt(6, ids[3]); // psihoterapeut_id
            stmt.setInt(7, ids[2]); // klijent_id
            stmt.executeUpdate();

            showSuccess("Uspeh", "Objava je uspešno sačuvana!");
            komeField.clear();
            razlogField.clear();
            datumPicker.setValue(null);
            seansaCombo.getSelectionModel().clearSelection();
        } catch (Exception e) {
            showAlert("Greška pri čuvanju", e.getMessage());
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
