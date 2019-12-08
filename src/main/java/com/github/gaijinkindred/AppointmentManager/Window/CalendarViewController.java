package com.github.gaijinkindred.AppointmentManager.Window;

import com.github.gaijinkindred.AppointmentManager.Backend.LanguageIdentifier;
import com.github.gaijinkindred.AppointmentManager.Backend.Logger;
import com.github.gaijinkindred.AppointmentManager.Main;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class CalendarViewController implements Initializable {
    @FXML private Label cellZeroOne;
    @FXML private Label cellZeroTwo;
    @FXML private Label cellZeroThree;
    @FXML private Label cellZeroFour;
    @FXML private Label cellZeroFive;
    @FXML private Label cellZeroSix;
    @FXML private Label cellOneOne;
    @FXML private Label cellOneTwo;
    @FXML private Label cellOneThree;
    @FXML private Label cellOneFour;
    @FXML private Label cellOneFive;
    @FXML private Label cellOneSix;
    @FXML private Label cellTwoOne;
    @FXML private Label cellTwoTwo;
    @FXML private Label cellTwoThree;
    @FXML private Label cellTwoFour;
    @FXML private Label cellTwoFive;
    @FXML private Label cellTwoSix;
    @FXML private Label cellThreeOne;
    @FXML private Label cellThreeTwo;
    @FXML private Label cellThreeThree;
    @FXML private Label cellThreeFour;
    @FXML private Label cellThreeFive;
    @FXML private Label cellThreeSix;
    @FXML private Label cellFourOne;
    @FXML private Label cellFourTwo;
    @FXML private Label cellFourThree;
    @FXML private Label cellFourFour;
    @FXML private Label cellFourFive;
    @FXML private Label cellFourSix;
    @FXML private Label cellFiveOne;
    @FXML private Label cellFiveTwo;
    @FXML private Label cellFiveThree;
    @FXML private Label cellFiveFour;
    @FXML private Label cellFiveFive;
    @FXML private Label cellFiveSix;
    @FXML private Label cellSixOne;
    @FXML private Label cellSixTwo;
    @FXML private Label cellSixThree;
    @FXML private Label cellSixFour;
    @FXML private Label cellSixFive;
    @FXML private Label cellSixSix;
    @FXML private Label monthLabel;
    @FXML private MenuButton monthMenuButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.LOGGERINSTANCE.log(Logger.LoggingLevel.LOG, "Opened CalendarViewController.");

        //Set Month Label:
        monthLabel.setText(getMonthTitle(Calendar.getInstance().get(Calendar.MONTH)));

        //Set Calendar dates:
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        long firstMillis = cal.getTime().getTime() - ((cal.get(Calendar.DATE) - 1) * 24 * 60 * 60 * 1000); //should be millis from 1970 to the first day of the month
        cal.setTime(new Date(firstMillis));
        weekFunnel(cal.get(Calendar.DAY_OF_WEEK), month, year);
        cal.setTime(new Date(System.currentTimeMillis()));
        System.out.println("Month: " + month);

        //Set menuButton stuff:
        MenuItem miM = new MenuItem();
        MenuItem miW = new MenuItem();
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            monthMenuButton.setText("Mois");
            miM.setText("Mois");
            miW.setText("La Semaine");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            monthMenuButton.setText("Mes");
            miM.setText("Mes");
            miW.setText("Semana");
        } else {
            miM.setText("Month");
            miW.setText("Week");
        }
        miW.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                Main.dismissRecentStage();
                if(Main.langIdent == LanguageIdentifier.FRENCH) {
                    Main.newChildStage("WeeklyCalendarView.fxml", "Calendrier - Vue Hebdomadaire");
                } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                    Main.newChildStage("WeeklyCalendarView.fxml", "Calendario - Vista Semanal");
                } else {
                    Main.newChildStage("WeeklyCalendarView.fxml", "Calendar - Weekly View");
                }
            }
        });
        monthMenuButton.getItems().remove(0); //Remove Action 1
        monthMenuButton.getItems().remove(0); //Remove Action 2
        monthMenuButton.getItems().addAll(miW, miM);

        //Done.
        Main.LOGGERINSTANCE.log(Logger.LoggingLevel.LOG, "Loaded CalendarViewController.");
    }

    private void weekFunnel(int startDay, int month, int year) {
        if(startDay == Calendar.SUNDAY) {
            weekOne("1", "2", "3", "4", "5", "6", "7");
            weekTwo("8", "9", "10", "11", "12", "13", "14");
            weekThree("15", "16", "17", "18", "19", "20", "21");
            weekFour("22", "23", "24", "25", "26", "27", "28");
            if(month % 2 == 1) {
                //31 Days
                weekFive("29", "30","31","","","","");
            } else if(month % 2 == 0 && month != Calendar.FEBRUARY) {
                //30 Days
                weekFive("29", "30","","","","","");
            } else {
                //February
                if(year % 4 == 0) {
                    //29 Days, and 28 days is already accounted for here
                    weekFive("29", "","","","","","");
                }
            }
            weekSix("","","","","","","");
        } else if(startDay == Calendar.MONDAY) {
            weekOne("", "1", "2", "3", "4", "5", "6");
            weekTwo("7", "8", "9", "10", "11", "12", "13");
            weekThree("14", "15", "16", "17", "18", "19", "20");
            weekFour("21", "22", "23", "24", "25", "26", "27");
            if(month % 2 == 1) {
                //31 Days
                weekFive("28", "29", "30", "31", "", "", "");
            } else if(month % 2 == 0 && month != Calendar.FEBRUARY) {
                //30 Days
                weekFive("28", "29", "30", "", "", "", "");
            } else {
                //February
                if(year % 4 == 0) {
                    //29 Days
                    weekFive("28", "29", "", "", "", "", "");
                } else {
                    //28 Days
                    weekFive("28", "", "", "", "", "", "");
                }
            }
            weekSix("","","","","","","");
        } else if(startDay == Calendar.TUESDAY) {
            weekOne("", "", "1", "2", "3", "4", "5");
            weekTwo("6", "7", "8", "9", "10", "11", "12");
            weekThree("13", "14", "15", "16", "17", "18", "19");
            weekFour("20", "21", "22", "23", "24", "25", "26");
            if(month % 2 == 1) {
                //31 Days
                weekFive("27", "28", "29", "30", "31", "", "");
            } else if(month % 2 == 0 && month != Calendar.FEBRUARY) {
                //30 Days
                weekFive("27", "28", "29", "30", "", "", "");
            } else {
                //February
                if(year % 4 == 0) {
                    //29 Days
                    weekFive("27", "28", "29", "", "", "", "");
                } else {
                    //28 Days
                    weekFive("27", "28", "", "", "", "", "");
                }
            }
            weekSix("", "", "", "", "", "", "");
        } else if(startDay == Calendar.WEDNESDAY) {
            weekOne("", "", "", "1", "2", "3", "4");
            weekTwo("5", "6", "7", "8", "9", "10", "11");
            weekThree("12", "13", "14", "15", "16", "17", "18");
            weekFour("19", "20", "21", "22", "23", "24", "25");
            if(month % 2 == 1) {
                //31 Days
                weekFive("26", "27", "28", "29", "30", "31", "");
            } else if(month % 2 == 0 && month != Calendar.FEBRUARY) {
                //30 Days
                weekFive("26", "27", "28", "29", "30", "", "");
            } else {
                //February
                if(year % 4 == 0) {
                    //29 Days
                    weekFive("26", "27", "28", "29", "", "", "");
                } else {
                    //28 Days
                    weekFive("26", "27", "28", "", "", "", "");
                }
            }
            weekSix("", "", "", "", "", "", "");
        } else if(startDay == Calendar.THURSDAY) {
            weekOne("", "", "", "", "1", "2", "3");
            weekTwo("4", "5", "6", "7", "8", "9", "10");
            weekThree("11", "12", "13", "14", "15", "16", "17");
            weekFour("18", "19", "20", "21", "22", "23", "24");
            if(month % 2 == 1) {
                //31 Days
                weekFive("25", "26", "27", "28", "29", "30", "31");
            } else if(month % 2 == 0 && month != Calendar.FEBRUARY) {
                //30 Days
                weekFive("25", "26", "27", "28", "29", "30", "");
            } else {
                //February
                if(year % 4 == 0) {
                    //29 Days
                    weekFive("25", "26", "27", "28", "29", "", "");
                } else {
                    //28 Days
                    weekFive("25", "26", "27", "28", "", "", "");
                }
            }
            weekSix("", "" ,"", "", "", "", "");
        } else if(startDay == Calendar.FRIDAY) {
            weekOne("", "", "", "", "", "1", "2");
            weekTwo("3", "4", "5", "6", "7", "8", "9");
            weekThree("10", "11", "12", "13", "14", "15", "16");
            weekFour("17", "18", "19", "20", "21", "22", "23");
            if(month % 2 == 1) {
                //31 Days
                weekFive("24", "25", "26", "27", "28", "29", "30");
                weekSix("31", "", "", "", "", "", "");
            } else if(month % 2 == 0 && month != Calendar.FEBRUARY) {
                //30 Days
                weekFive("24", "25", "26", "27", "28", "29", "30");
                weekSix("", "", "", "", "", "", "");
            } else {
                //February
                if(year % 4 == 0) {
                    //29 Days
                    weekFive("24", "25", "26", "27", "28", "29", "");
                } else {
                    //28 Days
                    weekFive("24", "25", "26", "27", "28", "", "");
                }
                weekSix("", "", "", "", "", "", "");
            }
        } else {
            weekOne("", "", "", "", "", "", "1");
            weekTwo("2", "3", "4", "5", "6", "7", "8");
            weekThree("9", "10", "11", "12", "13", "14", "15");
            weekFour("16", "17", "18", "19", "20", "21", "22");
            if(month % 2 == 1) {
                //31 Days
                weekFive("23", "24", "25", "26", "27", "28", "29");
                weekSix("30", "31", "", "", "", "", "");
            } else if(month % 2 == 0 && month != Calendar.FEBRUARY) {
                //30 Days
                weekFive("23", "24", "25", "26", "27", "28", "29");
                weekSix("30", "", "", "", "", "", "");
            } else {
                //February
                if(year % 4 == 0) {
                    //29 Days
                    weekFive("23", "24", "25", "26", "27", "28", "29");
                } else {
                    //28 Days
                    weekFive("23", "24", "25", "26", "27", "28", "");
                }
                weekSix("", "", "", "", "", "", "");
            }
        }
    }

    private void weekOne(String dayOne, String dayTwo, String dayThree, String dayFour, String dayFive, String daySix, String daySeven) {
        cellZeroOne.setText(dayOne);
        cellOneOne.setText(dayTwo);
        cellTwoOne.setText(dayThree);
        cellThreeOne.setText(dayFour);
        cellFourOne.setText(dayFive);
        cellFiveOne.setText(daySix);
        cellSixOne.setText(daySeven);
    }

    private void weekTwo(String dayOne, String dayTwo, String dayThree, String dayFour, String dayFive, String daySix, String daySeven) {
        cellZeroTwo.setText(dayOne);
        cellOneTwo.setText(dayTwo);
        cellTwoTwo.setText(dayThree);
        cellThreeTwo.setText(dayFour);
        cellFourTwo.setText(dayFive);
        cellFiveTwo.setText(daySix);
        cellSixTwo.setText(daySeven);
    }

    private void weekThree(String dayOne, String dayTwo, String dayThree, String dayFour, String dayFive, String daySix, String daySeven) {
        cellZeroThree.setText(dayOne);
        cellOneThree.setText(dayTwo);
        cellTwoThree.setText(dayThree);
        cellThreeThree.setText(dayFour);
        cellFourThree.setText(dayFive);
        cellFiveThree.setText(daySix);
        cellSixThree.setText(daySeven);
    }

    private void weekFour(String dayOne, String dayTwo, String dayThree, String dayFour, String dayFive, String daySix, String daySeven) {
        cellZeroFour.setText(dayOne);
        cellOneFour.setText(dayTwo);
        cellTwoFour.setText(dayThree);
        cellThreeFour.setText(dayFour);
        cellFourFour.setText(dayFive);
        cellFiveFour.setText(daySix);
        cellSixFour.setText(daySeven);
    }

    private void weekFive(String dayOne, String dayTwo, String dayThree, String dayFour, String dayFive, String daySix, String daySeven) {
        cellZeroFive.setText(dayOne);
        cellOneFive.setText(dayTwo);
        cellTwoFive.setText(dayThree);
        cellThreeFive.setText(dayFour);
        cellFourFive.setText(dayFive);
        cellFiveFive.setText(daySix);
        cellSixFive.setText(daySeven);
    }

    private void weekSix(String dayOne, String dayTwo, String dayThree, String dayFour, String dayFive, String daySix, String daySeven) {
        cellZeroSix.setText(dayOne);
        cellOneSix.setText(dayTwo);
        cellTwoSix.setText(dayThree);
        cellThreeSix.setText(dayFour);
        cellFourSix.setText(dayFive);
        cellFiveSix.setText(daySix);
        cellSixSix.setText(daySeven);
    }

    private String getMonthTitle(int month) {
        switch(month) {
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return "January";
        }
    }
}
