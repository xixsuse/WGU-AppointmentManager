package com.github.gaijinkindred.AppointmentManager.Window;

import com.github.gaijinkindred.AppointmentManager.Backend.ERDController;
import com.github.gaijinkindred.AppointmentManager.Backend.FormattedAppointment;
import com.github.gaijinkindred.AppointmentManager.Backend.LanguageIdentifier;
import com.github.gaijinkindred.AppointmentManager.Backend.Logger;
import com.github.gaijinkindred.AppointmentManager.ERD.Appointment;
import com.github.gaijinkindred.AppointmentManager.ERD.Customer;
import com.github.gaijinkindred.AppointmentManager.Main;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AppointmentsViewController implements Initializable {
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private TableView tableView;

    public static Appointment selectedAppointment;

    @FXML
    private void addAppointment(ActionEvent event) {
        selectedAppointment = null;
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            Main.newChildStage("AppointmentSpecificsView.fxml", "Ajouter un rendez-vous");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            Main.newChildStage("AppointmentSpecificsView.fxml", "Añadir Cita");
        } else {
            Main.newChildStage("AppointmentSpecificsView.fxml", "Add Appointment");
        }
    }

    @FXML
    private void updateAppointment(ActionEvent event) {
        selectedAppointment = ERDController.getInstance().getAppointments().get(tableView.getSelectionModel().getSelectedIndex());
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            Main.newChildStage("AppointmentSpecificsView.fxml", "Modifier un rendez-vous");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            Main.newChildStage("AppointmentSpecificsView.fxml", "Modificar Cita");
        } else {
            Main.newChildStage("AppointmentSpecificsView.fxml", "Modify Appointment");
        }
    }

    @FXML
    private void deleteAppointment(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            alert.setContentText("Êtes-vous sûr de vouloir supprimer ce rendez-vous");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            alert.setContentText("Está seguro de que desea eliminiar esta cita?");
        } else {
            alert.setContentText("Are you sure you would like to delete this appointment?");
        }
        alert.showAndWait().filter(response -> response == ButtonType.YES).ifPresent(response -> { //Lambda here
            Appointment app = ERDController.getInstance().getAppointments().get(tableView.getSelectionModel().getSelectedIndex());
            ERDController.getInstance().deleteAppointment(app.getAppointmentId());
            tableView.refresh();
        });
    }

    public void initialize(URL location, ResourceBundle resources) {
        Main.LOGGERINSTANCE.log(Logger.LoggingLevel.LOG, "Loading Appointments View");
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            addButton.setText("Ajouter");
            updateButton.setText("Modifier");
            deleteButton.setText("Supprimer");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            addButton.setText("Añadir");
            updateButton.setText("Actualizer");
            deleteButton.setText("Borrar");
        }

        ArrayList<Appointment> appointments = ERDController.getInstance().getAppointments();
        ArrayList<FormattedAppointment> fa = new ArrayList<FormattedAppointment>();
        for(Appointment a : appointments) {
            System.out.println(a.getUserId());
            Customer c = ERDController.getInstance().getCustomer(a.getCustomerId());
            System.out.println(c == null);
            String customerName = c.getCustomerName();
            long length = ((a.getEndDate().getTime() - a.getStartDate().getTime())/1000)/60;
            length = length - (length % 1);
            FormattedAppointment formattedAppointment = new FormattedAppointment(a.getUserId(), a.getAppointmentId(), a.getType(), customerName, length + " minutes", a.getStartDate().toString());
            fa.add(formattedAppointment);
        }

        tableView.setItems(FXCollections.observableList(fa));
        ((TableColumn)(tableView.getColumns().get(0))).setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        ((TableColumn)(tableView.getColumns().get(1))).setCellValueFactory(new PropertyValueFactory<Appointment, String>("customerName"));
        ((TableColumn)(tableView.getColumns().get(2))).setCellValueFactory(new PropertyValueFactory<Appointment, String>("length"));
        ((TableColumn)(tableView.getColumns().get(3))).setCellValueFactory(new PropertyValueFactory<Appointment, String>("time"));
        Main.LOGGERINSTANCE.log(Logger.LoggingLevel.LOG, "Loaded Appointments View");
    }
}
