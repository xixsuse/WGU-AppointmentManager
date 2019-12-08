package com.github.gaijinkindred.AppointmentManager.Window;

import com.github.gaijinkindred.AppointmentManager.Backend.ERDController;
import com.github.gaijinkindred.AppointmentManager.Backend.LanguageIdentifier;
import com.github.gaijinkindred.AppointmentManager.ERD.Address;
import com.github.gaijinkindred.AppointmentManager.ERD.City;
import com.github.gaijinkindred.AppointmentManager.ERD.Country;
import com.github.gaijinkindred.AppointmentManager.ERD.Customer;
import com.github.gaijinkindred.AppointmentManager.Main;
import com.sun.tools.internal.xjc.Language;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class CustomerSpecificsViewController implements Initializable {
    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField cityField;
    @FXML private TextField countryField;
    @FXML private TextField postalField;
    @FXML private TextField phoneNumberField;

    @FXML private Label nameLabel;
    @FXML private Label addressLabel;
    @FXML private Label cityLabel;
    @FXML private Label countryLabel;
    @FXML private Label postalLabel;
    @FXML private Label phoneNumberLabel;
    @FXML private Button submitButton;

    @FXML private TableView tableView;

    @FXML
    private void submitButtonAction(ActionEvent ae) {
        java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
        Timestamp ts = new Timestamp(date.getTime());
        if(CustomerSelectionView.customer == null) {
            Address addr = ERDController.getInstance().newAddress(addressField.getText(), "", cityField.getText(), countryField.getText(), postalField.getText(), phoneNumberField.getText(), date, Main.user.userName, ts, Main.user.userName);
            ERDController.getInstance().newCustomer(nameField.getText(), addr.getAddressId(), 1, date, Main.user.userName, ts, Main.user.userName);
        }
        Main.dismissRecentStage();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            nameLabel.setText("Nom:");
            addressLabel.setText("Addresse:");
            cityLabel.setText("Ville:");
            countryLabel.setText("Pays:");
            postalLabel.setText("Code Postal:");
            phoneNumberLabel.setText("Numéro de Téléphone:");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            nameLabel.setText("Nombre:");
            addressLabel.setText("Dirección:");
            cityLabel.setText("Ciudad:");
            countryLabel.setText("País:");
            postalLabel.setText("Postal:");
            phoneNumberLabel.setText("Número de Teléfono:");
        }

        if(CustomerSelectionView.customer != null) {
            nameField.setText(CustomerSelectionView.customer.getCustomerName());
            Address addr = ERDController.getInstance().getAddress(CustomerSelectionView.customer.getAddressId());
            if(addr != null)
                addressField.setText(addr.getAddress());
            City c = ERDController.getInstance().getCity(addr.getCityId());
            if(c != null) {
                cityField.setText(c.getCity());
                Country co = ERDController.getInstance().getCountry(c.getCountryId());
                if (co != null) {
                    countryField.setText(co.getCountry() + "");
                }
            }
            if(addr.getPostalCode() != null)
                postalField.setText(addr.getPostalCode());
            phoneNumberField.setText(addr.getPhoneNumber());
        }
    }
}
