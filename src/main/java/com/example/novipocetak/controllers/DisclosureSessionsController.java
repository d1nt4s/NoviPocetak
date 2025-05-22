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

import static com.example.novipocetak.util.AppUtils.showAlert;

public class DisclosureSessionsController {

    @FXML private TableView<SeansaDisclosure> seansaTable;
    @FXML private TableColumn<SeansaDisclosure, Integer> seansaIdCol;
    @FXML private TableColumn<SeansaDisclosure, String> datumCol;
    @FXML private TableColumn<SeansaDisclosure, String> vremeCol;
    @FXML private TableColumn<SeansaDisclosure, Integer> trajanjeCol;
    @FXML private TableColumn<SeansaDisclosure, String> beleskeCol;
    @FXML private TableColumn<SeansaDisclosure, String> testCol;
    @FXML private TableColumn<SeansaDisclosure, String> rezultatCol;
    @FXML private TableColumn<SeansaDisclosure, String> objavaCol;
    @FXML private TableColumn<SeansaDisclosure, String> klijentCol;

    private final ObservableList<SeansaDisclosure> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        seansaIdCol.setCellValueFactory(cellData -> cellData.getValue().seansaIdProperty().asObject());
        datumCol.setCellValueFactory(cellData -> cellData.getValue().datumProperty());
        vremeCol.setCellValueFactory(cellData -> cellData.getValue().vremeProperty());
        trajanjeCol.setCellValueFactory(cellData -> cellData.getValue().trajanjeProperty().asObject());
        beleskeCol.setCellValueFactory(cellData -> cellData.getValue().beleskeProperty());
        testCol.setCellValueFactory(cellData -> cellData.getValue().testNazivProperty());
        rezultatCol.setCellValueFactory(cellData -> cellData.getValue().rezultatProperty());
        objavaCol.setCellValueFactory(cellData -> cellData.getValue().objavaProperty());
        klijentCol.setCellValueFactory(cellData -> cellData.getValue().klijentProperty());

        loadData();
    }

    private void loadData() {
        try (Connection conn = Database.connect()) {
            String sql = """
                SELECT s.seansa_id, s.datum, s.vreme, s.trajanje, s.beleske,
                       pt.naziv AS test_naziv, t.rezultat,
                       op.kome, op.razlog,
                       k.ime AS klijent_ime, k.prezime AS klijent_prezime
                FROM seansa s
                LEFT JOIN testiranje t ON s.Testiranje_testiranje_id = t.testiranje_id
                                        AND s.Testiranje_PsiholoskiTest_test_id = t.PsiholoskiTest_test_id
                LEFT JOIN psiholoskitest pt ON t.PsiholoskiTest_test_id = pt.test_id
                LEFT JOIN klijent k ON s.Klijent_klijent_id = k.klijent_id
                LEFT JOIN objavljivanjepodataka op ON s.seansa_id = op.Seansa_seansa_id
                                                   AND s.Kandidat_kandidat_id = op.Seansa_Kandidat_kandidat_id
                                                   AND s.Psihoterapeut_psihoterapeut_id = op.Seansa_Psihoterapeut_psihoterapeut_id
                                                   AND s.Klijent_klijent_id = op.Seansa_Klijent_klijent_id
                WHERE s.Psihoterapeut_psihoterapeut_id = ?
            """;

            int prijavljeniId = Session.getLoggedInID();

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, prijavljeniId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int seansaId = rs.getInt("seansa_id");
                String datum = rs.getString("datum");
                String vreme = rs.getString("vreme");
                int trajanje = rs.getInt("trajanje");
                String beleske = rs.getString("beleske");
                String test = rs.getString("test_naziv");
                String rezultat = rs.getString("rezultat");
                String objava = rs.getString("kome") != null
                        ? rs.getString("kome") + " — " + rs.getString("razlog")
                        : "Nije objavljeno";
                String klijent = rs.getString("klijent_ime") + " " + rs.getString("klijent_prezime");

                data.add(new SeansaDisclosure(seansaId, datum, vreme, trajanje, beleske, test, rezultat, objava, klijent));
            }

            seansaTable.setItems(data);

        } catch (SQLException e) {
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
