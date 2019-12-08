package com.github.gaijinkindred.AppointmentManager.Window;

import com.github.gaijinkindred.AppointmentManager.Backend.ERDController;
import com.github.gaijinkindred.AppointmentManager.Backend.Logger;
import com.github.gaijinkindred.AppointmentManager.ERD.Appointment;
import com.github.gaijinkindred.AppointmentManager.Main;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

public class WeeklyCalendarViewController implements Initializable {
    @FXML private Label titleLabel;
    @FXML private MenuButton weekMenuButton;
    @FXML private TableView tableView;


    //NOTE: The bug with WeeklyCalendarViewController is actually a JFX-level issue that I simplify can't fix. A workaround is to expand the window size or reduce the content. Neither are ideal, but it could work..
    //TODO: Validate and fix the bug with this not opening correctly every time
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.LOGGERINSTANCE.log(Logger.LoggingLevel.LOG, "Loading Weekly Calendar View");
        Calendar.getInstance().setTime(new Date(System.currentTimeMillis()));
        String day = getDayOfWeek(Calendar.getInstance().getTime());
        day = day.substring(0,1).toUpperCase() + day.substring(1);
        titleLabel.setText(day);
        MenuItem miM = new MenuItem();
        miM.setText("Month");
        miM.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                Main.dismissRecentStage();
                Main.newChildStage("CalendarView.fxml", "Calendar View");
            }
        });
        MenuItem miW = new MenuItem();
        miW.setText("Week");
        weekMenuButton.getItems().addAll(miM, miW);
        ArrayList<TableTimeData> ttds = new ArrayList<TableTimeData>();
        ArrayList<Appointment> appointments = new ArrayList<Appointment>();

        Calendar cal = Calendar.getInstance();
        long offset = ((cal.get(Calendar.DAY_OF_WEEK) - 1) * 24 * 3600000) + (cal.get(Calendar.HOUR_OF_DAY) * 3600000) + (cal.get(Calendar.MINUTE) * 60000) + (cal.get(Calendar.SECOND) * 1000);
        long sundayTime = Calendar.getInstance().getTime().getTime() - offset - 1000;
        long saturdayEndTime = sundayTime + 604801000; //Should be about right
        for(Appointment a : ERDController.getInstance().getAppointments()) {
            if(a.getStartDate().getTime() > sundayTime && a.getStartDate().getTime() < saturdayEndTime) {
                appointments.add(a);
            }
        }
        int hour = 6;
        int minute = -1;

        String[] str = { "sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday" };
        ArrayList<Appointment> backlog = new ArrayList<Appointment>();
        long currentTimeCheck = 0;
        for(int i = 0; i < 32; i++) {
            hour += i % 2 == 0 ? 1 : 0;
            minute = (i * 30) % 60;
            TableTimeData ttd = new TableTimeData(hour + ":" + (minute == 0 ? "00" : minute), "", "", "", "", "", "", "");
            ttds.add(ttd);
            for (Appointment a : appointments) {
                currentTimeCheck = (long) ((hour * 3600 * 1000) + (minute * 60 * 1000));
                if (a.getStartDate().getTime() % 86400000 == currentTimeCheck || (backlog.indexOf(a) > -1 && a.getEndDate().getTime() % 86400000 > currentTimeCheck)) {
                    Calendar.getInstance().setTime(a.getStartDate());
                    if(getDayOfWeek(a.getStartDate()).equals("sunday")) {
                        ttd.setSunday(a.getTitle());
                    } else if(getDayOfWeek(a.getStartDate()).equals("monday")) {
                        ttd.setMonday(a.getTitle());
                    } else if(getDayOfWeek(a.getStartDate()).equals("tuesday")) {
                        ttd.setTuesday(a.getTitle());
                    } else if(getDayOfWeek(a.getStartDate()).equals("wednesday")) {
                        ttd.setWednesday(a.getTitle());
                    } else if(getDayOfWeek(a.getStartDate()).equals("thursday")) {
                        ttd.setThursday(a.getTitle());
                    } else if(getDayOfWeek(a.getStartDate()).equals("friday")) {
                        ttd.setFriday(a.getTitle());
                    } else if(getDayOfWeek(a.getStartDate()).equals("saturday")) {
                        ttd.setSaturday(a.getTitle());
                    }
                    if(a.getEndDate().getTime() - a.getStartDate().getTime() > 1) {
                        backlog.add(a);
                    }
                } else if(backlog.indexOf(a) > -1 && a.getEndDate().getTime() % 86400000 < currentTimeCheck) {
                    backlog.remove(a);
                }
            }
        }
        tableView.setItems(FXCollections.observableList(ttds));
        ((TableColumn)(tableView.getColumns().get(0))).setCellValueFactory(new PropertyValueFactory<TableTimeData, String>("time"));
        ((TableColumn)(tableView.getColumns().get(1))).setCellValueFactory(new PropertyValueFactory<TableTimeData, String>("sunday"));
        ((TableColumn)(tableView.getColumns().get(2))).setCellValueFactory(new PropertyValueFactory<TableTimeData, String>("monday"));
        ((TableColumn)(tableView.getColumns().get(3))).setCellValueFactory(new PropertyValueFactory<TableTimeData, String>("tuesday"));
        ((TableColumn)(tableView.getColumns().get(4))).setCellValueFactory(new PropertyValueFactory<TableTimeData, String>("wednesday"));
        ((TableColumn)(tableView.getColumns().get(5))).setCellValueFactory(new PropertyValueFactory<TableTimeData, String>("thursday"));
        ((TableColumn)(tableView.getColumns().get(6))).setCellValueFactory(new PropertyValueFactory<TableTimeData, String>("friday"));
        ((TableColumn)(tableView.getColumns().get(7))).setCellValueFactory(new PropertyValueFactory<TableTimeData, String>("saturday"));
        Main.LOGGERINSTANCE.log(Logger.LoggingLevel.LOG, "Loaded Weekly Calendar View");
    }

    private String getDayOfWeek(Date date) {
        Calendar.getInstance().setTime(date);
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        Calendar.getInstance().setTime(new Date(System.currentTimeMillis()));
        switch(dayOfWeek) {
            case Calendar.SUNDAY:
                return "sunday";
            case Calendar.MONDAY:
                return "monday";
            case Calendar.TUESDAY:
                return "tuesday";
            case Calendar.WEDNESDAY:
                return "wednesday";
            case Calendar.THURSDAY:
                return "thursday";
            case Calendar.FRIDAY:
                return "friday";
            case Calendar.SATURDAY:
                return "saturday";
            default:
                return null;
        }
    }

    public class TableTimeData {
        private String time;
        private String sunday;
        private String monday;
        private String tuesday;
        private String wednesday;
        private String thursday;
        private String friday;
        private String saturday;

        public TableTimeData(String time, String sunday, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday) {
            this.time = time;
            this.sunday = sunday != null ? sunday : "";
            this.monday = monday != null ? monday : "";
            this.tuesday = tuesday != null ? tuesday : "";
            this.wednesday = wednesday != null ? wednesday : "";
            this.thursday = thursday != null ? thursday : "";
            this.friday = friday != null ? friday : "";
            this.saturday = saturday != null ? saturday : "";
        }

        public String getTime() {
            return time;
        }

        public String getSunday() {
            return sunday;
        }

        public String getMonday() {
            return monday;
        }

        public String getTuesday() {
            return tuesday;
        }

        public String getWednesday() {
            return wednesday;
        }

        public String getThursday() {
            return thursday;
        }

        public String getFriday() {
            return friday;
        }

        public String getSaturday() {
            return saturday;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setSunday(String sunday) {
            this.sunday = sunday;
        }

        public void setMonday(String monday) {
            this.monday = monday;
        }

        public void setTuesday(String tuesday) {
            this.tuesday = tuesday;
        }

        public void setWednesday(String wednesday) {
            this.wednesday = wednesday;
        }

        public void setThursday(String thursday) {
            this.thursday = thursday;
        }

        public void setFriday(String friday) {
            this.friday = friday;
        }

        public void setSaturday(String saturday) {
            this.saturday = saturday;
        }

        public String toString() {
            return "time: " + time + "; sunday: " + sunday + "; monday: " + monday + "; tuesday: " + tuesday + "; wednesday: " + wednesday + "; thursday: " + "; friday: " + friday + "; saturday" + saturday;
        }
    }
}
