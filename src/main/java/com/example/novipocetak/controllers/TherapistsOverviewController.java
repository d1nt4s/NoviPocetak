package com.example.novipocetak.controllers;

import com.example.novipocetak.model.Psihoterapeut;
import com.example.novipocetak.util.Database;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TherapistsOverviewController {

    @FXML private TableView<Psihoterapeut> table;
    @FXML private TableColumn<Psihoterapeut, String> ime;
    @FXML private TableColumn<Psihoterapeut, String> prezime;
    @FXML private TableColumn<Psihoterapeut, String> jmbg;
    @FXML private TableColumn<Psihoterapeut, String> email;
    @FXML private TableColumn<Psihoterapeut, String> telefon;

    @FXML
    public void initialize() {
        ime.setCellValueFactory(new PropertyValueFactory<>("ime"));
        prezime.setCellValueFactory(new PropertyValueFactory<>("prezime"));
        jmbg.setCellValueFactory(new PropertyValueFactory<>("jmbg"));
        email.setCellValueFactory(new PropertyValueFactory<>("imejl"));
        telefon.setCellValueFactory(new PropertyValueFactory<>("telefon"));

        ObservableList<Psihoterapeut> list = FXCollections.observableArrayList();

        try (Connection conn = Database.connect()) {
            String sql = "SELECT * FROM Psihoterapeut";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new Psihoterapeut(
                        rs.getInt("psihoterapeut_id"),
                        rs.getString("ime"),
                        rs.getString("prezime"),
                        rs.getString("JMBG"),
                        rs.getString("imejl"),
                        rs.getString("telefon")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        table.setItems(list);
    }
}
