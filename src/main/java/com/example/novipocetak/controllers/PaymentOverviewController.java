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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

import static com.example.novipocetak.util.AppUtils.showAlert;

public class PaymentOverviewController {

    @FXML private TableView<PaymentInfo> paymentTable;
    @FXML private TableColumn<PaymentInfo, String> clientColumn;
    @FXML private TableColumn<PaymentInfo, String> valutaColumn;
    @FXML private TableColumn<PaymentInfo, Double> amountColumn;
    @FXML private TableColumn<PaymentInfo, LocalDate> dueDateColumn;
    @FXML private TableColumn<PaymentInfo, LocalDate> paidDateColumn;
    @FXML private TableColumn<PaymentInfo, Boolean> overdueColumn;

    private int loggedTherapistId = Session.getLoggedInID();

    @FXML
    public void initialize() {
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        valutaColumn.setCellValueFactory(new PropertyValueFactory<>("valuta"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        paidDateColumn.setCellValueFactory(new PropertyValueFactory<>("paidDate"));
        overdueColumn.setCellValueFactory(new PropertyValueFactory<>("overdue"));

        overdueColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item ? "Da" : "Ne");
                    setStyle(item
                            ? "-fx-background-color: lightcoral; -fx-text-fill: white;"
                            : "-fx-background-color: lightgreen; -fx-text-fill: black;");
                }
            }
        });

        loadPaymentData();
    }

    private void loadPaymentData() {
        ObservableList<PaymentInfo> data = FXCollections.observableArrayList();

        String query = """
            SELECT k.ime, k.prezime, v.skraceni_naziv, r.iznos, r.krajnji_datum, r.datum_uplate
            FROM rata r
            JOIN placenje p ON r.Placenje_placenje_id = p.placenje_id
                AND r.Placenje_Seansa_Psihoterapeut_psihoterapeut_id = ?
            JOIN valuta v ON p.Valuta_valuta_id = v.valuta_id
            JOIN seansa s ON p.Seansa_seansa_id = s.seansa_id
            JOIN klijent k ON s.Klijent_klijent_id = k.klijent_id
        """;

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, loggedTherapistId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String clientName = rs.getString("ime") + " " + rs.getString("prezime");
                String valuta = rs.getString("skraceni_naziv");
                double iznos = rs.getDouble("iznos");
                LocalDate due = rs.getDate("krajnji_datum").toLocalDate();
                LocalDate paid = rs.getDate("datum_uplate").toLocalDate();
                boolean overdue = paid.isAfter(due);

                data.add(new PaymentInfo(clientName, valuta, iznos, due, paid, overdue));
            }

            paymentTable.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class PaymentInfo {
        private final String clientName;
        private final String valuta;
        private final double amount;
        private final LocalDate dueDate;
        private final LocalDate paidDate;
        private final boolean overdue;

        public PaymentInfo(String clientName, String valuta, double amount, LocalDate dueDate, LocalDate paidDate, boolean overdue) {
            this.clientName = clientName;
            this.valuta = valuta;
            this.amount = amount;
            this.dueDate = dueDate;
            this.paidDate = paidDate;
            this.overdue = overdue;
        }

        public String getClientName() { return clientName; }
        public String getValuta() { return valuta; }
        public double getAmount() { return amount; }
        public LocalDate getDueDate() { return dueDate; }
        public LocalDate getPaidDate() { return paidDate; }
        public boolean getOverdue() { return overdue; }
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
