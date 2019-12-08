package com.github.gaijinkindred.AppointmentManager.Window;

import com.github.gaijinkindred.AppointmentManager.Backend.ERDController;
import com.github.gaijinkindred.AppointmentManager.Backend.LMBIdentifier;
import com.github.gaijinkindred.AppointmentManager.Backend.LanguageIdentifier;
import com.github.gaijinkindred.AppointmentManager.ERD.Address;
import com.github.gaijinkindred.AppointmentManager.ERD.Appointment;
import com.github.gaijinkindred.AppointmentManager.ERD.Customer;
import com.github.gaijinkindred.AppointmentManager.ERD.User;
import com.github.gaijinkindred.AppointmentManager.Main;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

public class AppointmentSpecificsViewController implements Initializable {
    @FXML private TextField typeField;
    @FXML private TextField titleField;
    @FXML private TextField descriptionField;

    @FXML private Label typeLabel;
    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label customerLabel;
    @FXML private Label startTimeLabel;
    @FXML private Label endTimeLabel;

    @FXML private Button selectCustomerButton;
    @FXML private MenuButton selectStartTime;
    @FXML private MenuButton selectEndTime;
    @FXML private DatePicker datePicker;

    @FXML private Button submitButton;

    private ArrayList<MenuItem> timeMenuButtonOptions = new ArrayList<MenuItem>();

    @FXML
    private void submitButtonAction(ActionEvent event) {
        if(CustomerSelectionView.customer != null) {
            int cusId = CustomerSelectionView.customer.getCustomerId();
            int usrId = Main.user.userId;
            String title = titleField.getText();
            String desc = descriptionField.getText();
            String loc = Main.lmbIdent == LMBIdentifier.PHOENIX ? "Phoenix, Arizona" : (Main.lmbIdent == LMBIdentifier.LONDON ? "London, England" : "New York, New York");
            Address add = ERDController.getInstance().getAddress(CustomerSelectionView.customer.getAddressId());
            String cont = add.getPhoneNumber();
            String type = typeField.getText();
            String url = "";
            Timestamp start = null;
            Timestamp end = null;
            if(selectStartTime.getText().indexOf(":") > -1 && selectEndTime.getText().indexOf(":") > -1) {
                Calendar cal = Calendar.getInstance();
                java.util.Date date = Date.from(Instant.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
                cal.setTime(date);
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), Integer.parseInt(selectStartTime.getText().split(":")[0]), Integer.parseInt(selectStartTime.getText().split(":")[1]));
                start = new Timestamp(cal.getTime().getTime());
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), Integer.parseInt(selectEndTime.getText().split(":")[0]), Integer.parseInt(selectEndTime.getText().split(":")[1]));
                end = new Timestamp(cal.getTime().getTime());
            } else {
                //error out
                if(Main.langIdent == LanguageIdentifier.FRENCH) {
                    Main.newError("Erreur de Soumission", "Les heures de début ou de fin ne sont pas sélectionnées");
                } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                    Main.newError("Error de Envío", "Las horas de inicio o finalización no están seleccionadas");
                } else {
                    Main.newError("Submission Error", "Start or End times are not selected");
                }
                return;
            }
            for(Appointment a : ERDController.getInstance().getAppointments()) {
                if(a.getStartDate().getTime() == start.getTime() && a.getLocation() == Main.lmbIdent.toString()) {
                    if(Main.langIdent == LanguageIdentifier.FRENCH) {
                        Main.newError("Rendez-vous Préexistant", "Rendez-vous préablable trouvé, impossible d'en ajouter un autre!");
                    } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                        Main.newError("Cita Previa!", "Cita previa encontrada, no se puede agregar otra!");
                    } else {
                        Main.newError("Pre-Existing appointment", "Prior appointment found, cannot add another!");
                    }
                    return;
                }
            }
            Date create = new Date(System.currentTimeMillis());
            String createdBy;
            if(Main.user != null)
                createdBy = Main.user.userName;
            else {
                //error out
                if(Main.langIdent == LanguageIdentifier.FRENCH) {
                    Main.newError("Erreur de Soumission", "Impossible de trouver l'utilisateur connecté.");
                } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                    Main.newError("Error de Envío", "No se pudo encontrar el usuario conectado.");
                } else {
                    Main.newError("Submission Error", "Could not find logged in user in Main.java.");
                }
                return;
            }
            Timestamp last = new Timestamp(System.currentTimeMillis());
            Appointment appointment = ERDController.getInstance().newAppointment(cusId, usrId, title, desc, loc, cont, type, url,
                    start, end, create, createdBy, last, createdBy);
        } else {
            //error
            if(Main.langIdent == LanguageIdentifier.FRENCH) {
                Main.newError("Erreur de Soumission", "Aucun client sélectionné.");
            } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                Main.newError("Error de Envío", "Ningún cliente seleccionado.");
            } else {
                Main.newError("Submission Error", "No customer selected.");
            }
            return;
        }
        Main.dismissRecentStage();
    }

    @FXML
    private void selectCustomerWindow(ActionEvent event) {
        Main.newChildStage("CustomerSelectionView.fxml","Customer Selection");
        if(CustomerSelectionView.customer != null && CustomerSelectionView.customer.getCustomerName() != null)
            selectCustomerButton.setText(CustomerSelectionView.customer.getCustomerName());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            typeLabel.setText("Catégorie:");
            customerLabel.setText("Client:");
            titleLabel.setText("Titre:");
            descriptionLabel.setText("La Description:");
            startTimeLabel.setText("Heure de début:");
            endTimeLabel.setText("Heure de fin:");

            selectCustomerButton.setText("Sélectionner un Client");
            selectStartTime.setText("Sélectionner l'heure de début");
            selectEndTime.setText("Sélectionner l'heure de fin");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            typeLabel.setText("Tipos:");
            customerLabel.setText("Cliente:");
            titleLabel.setText("Título:");
            descriptionLabel.setText("Descripción:");
            startTimeLabel.setText("Hora de inicio:");
            endTimeLabel.setText("Hora de finalización:");

            selectCustomerButton.setText("Seleccione Cliente");
            selectStartTime.setText("Seleccione hora de inicio");
            selectEndTime.setText("Seleccione hora de finalización");
        }
        if(AppointmentsViewController.selectedAppointment != null) {
            Appointment appo = ERDController.getInstance().getAppointment(AppointmentsViewController.selectedAppointment.getAppointmentId());
            typeField.setText(appo.getType());
            titleField.setText(appo.getTitle());
            descriptionField.setText(appo.getDescription());
            selectCustomerButton.setText(ERDController.getInstance().getCustomer(appo.getCustomerId()).getCustomerName());
            selectStartTime.setText(appo.getStartDate().toString());
            selectEndTime.setText(appo.getEndDate().toString());
        }

        int hours = 6;
        for(int i = 0; i < 32; i++) {
            if(i % 2 == 0 && i > 0)
                hours++;
            int minutes = (30 * i) % 60;
            String temp = hours + ":" + (minutes == 0 ? "00" : minutes);
            MenuItem mi = new MenuItem();
            mi.setText(temp);
            mi.setOnAction(new EventHandler() {
                @Override
                public void handle(Event event) {
                    String button = mi.getText();
                    selectStartTime.setText(button);
                    int hours = Integer.parseInt(button.split(":")[0]);
                    int minutes = Integer.parseInt(button.split(":")[1]);
                    ArrayList<MenuItem> endTimeMenuOptions = new ArrayList<MenuItem>();
                    for(int j = ((hours - 7)*2) + (minutes == 30 ? 0 : 1); j < 32; j++) {
                        //Add some times to the endTimeMenuButton
                        if(j % 2 == 0) {
                            hours += 1;
                        }
                        minutes = (30 * j) % 60;
                        MenuItem mis = new MenuItem();
                        mis.setText(hours + ":" + (minutes == 0 ? "00" : minutes));
                        mis.setOnAction(new EventHandler() {
                            @Override
                            public void handle(Event event) {
                                selectEndTime.setText(mis.getText());
                            }
                        });
                        endTimeMenuOptions.add(mis);
                    }
                    selectEndTime.getItems().setAll(endTimeMenuOptions);
                }
            });
            timeMenuButtonOptions.add(mi);
        }
        selectStartTime.getItems().setAll(timeMenuButtonOptions);
    }
}
