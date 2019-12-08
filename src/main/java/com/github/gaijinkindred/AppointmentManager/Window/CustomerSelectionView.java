package com.github.gaijinkindred.AppointmentManager.Window;

import com.github.gaijinkindred.AppointmentManager.Backend.ERDController;
import com.github.gaijinkindred.AppointmentManager.Backend.FormattedCustomer;
import com.github.gaijinkindred.AppointmentManager.Backend.LanguageIdentifier;
import com.github.gaijinkindred.AppointmentManager.ERD.Address;
import com.github.gaijinkindred.AppointmentManager.ERD.Customer;
import com.github.gaijinkindred.AppointmentManager.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomerSelectionView implements Initializable {
    @FXML private Button okButton;
    @FXML private Button cancelButton;
    @FXML private TableView tableView;
    @FXML private Button addCustomerButton;
    @FXML private Button modifyCustomerButton;
    @FXML private Button deleteCustomerButton;

    public static Customer customer = null;

    @FXML
    private void okButtonAction(ActionEvent event) {
        if(tableView.getSelectionModel().getSelectedItem() != null)
            customer = ERDController.getInstance().getCustomers().get(tableView.getSelectionModel().getSelectedIndex());
        Main.dismissRecentStage();
    }

    @FXML
    private void cancelButtonAction(ActionEvent event) {
        Main.dismissRecentStage();
    }

    @FXML private void addCustomer(ActionEvent event) {
        customer = null;
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            Main.newChildStage("CustomerSpecificsView.fxml","Ajouter des données Client");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            Main.newChildStage("CustomerSpecificsView.fxml", "Agregar datos del Cliente");
        } else {
            Main.newChildStage("CustomerSpecificsView.fxml", "Add Customer Data");
        }
        tableView.refresh();
    }

    @FXML
    private void deleteCustomer(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to delete this customer?");
        alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> { //Lambda #2, does something other than what Main.newError does..
            Customer customer = ERDController.getInstance().getCustomers().get(tableView.getSelectionModel().getSelectedIndex());
            ERDController.getInstance().deleteCustomer(customer.getCustomerId());
        });
        tableView.refresh();
    }

    @FXML
    private void modifyCustomer(ActionEvent event) {
        FormattedCustomer fc = (FormattedCustomer)(tableView.getSelectionModel().getSelectedItem());
        customer = ERDController.getInstance().getCustomer(fc.customerId);
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            Main.newChildStage("CustomerSpecificsView.fxml", "Modifier les données client");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            Main.newChildStage("CustomerSpecificsView.fxml", "Modificar datos del Cliente");
        } else {
            Main.newChildStage("CustomerSpecificsView.fxml", "Modify Customer Data");
        }
        tableView.refresh();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Customer> customers = ERDController.getInstance().getCustomers();
        ArrayList<FormattedCustomer> formattedCustomers = new ArrayList<FormattedCustomer>();
        for(Customer c : customers) {
            if(c.getAddressId() != -1) {
                Address addr = ERDController.getInstance().getAddress(c.getAddressId());
                formattedCustomers.add(new FormattedCustomer(c.getCustomerName(), addr.getAddress(), addr.getPhoneNumber(), c.getCustomerId(), c.getAddressId()));
            }
        }
        tableView.setItems(FXCollections.observableList(formattedCustomers));
        ((TableColumn)(tableView.getColumns().get(0))).setCellValueFactory(new PropertyValueFactory<FormattedCustomer, String>("customerName")); //name
        ((TableColumn)(tableView.getColumns().get(1))).setCellValueFactory(new PropertyValueFactory<FormattedCustomer, String>("address")); //location
        ((TableColumn)(tableView.getColumns().get(2))).setCellValueFactory(new PropertyValueFactory<FormattedCustomer, String>("phoneNumber")); //contact

        if(Main.langIdent == LanguageIdentifier.SPANISH) {
            addCustomerButton.setText("Agregar Cliente");
            modifyCustomerButton.setText("Modificar Cliente");
            deleteCustomerButton.setText("Borrar");
        } else if(Main.langIdent == LanguageIdentifier.FRENCH) {
            addCustomerButton.setText("Ajouter un Client");
            modifyCustomerButton.setText("Modifier le Client");
            deleteCustomerButton.setText("Supprimer");
        } else {
            addCustomerButton.setText("Add Customer");
            modifyCustomerButton.setText("Modify Customer");
            deleteCustomerButton.setText("Delete");
        }
    }
}
