package com.github.gaijinkindred.AppointmentManager.Backend;

import java.sql.*;

import com.github.gaijinkindred.AppointmentManager.Main;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class DatabaseConnector {

    private static DatabaseConnector INSTANCE;
    public static DatabaseConnector getInstance() {
        return INSTANCE == null ? INSTANCE = new DatabaseConnector() : INSTANCE;
    }

    private Connection connection;

    public DatabaseConnector() {
        try {
            //Note: apparently we're not trying to protect against anybody, so here's this stuff in plain text..
            String url = "3.227.166.251";
            String username = "U06YWn";
            String tableName = "U06YWn";
            String password = "53688905125";
            connection = DriverManager.getConnection("jdbc:mysql://" + url + ":3306/" + tableName, username, password);
        } catch(SQLException e) {
            if(Main.langIdent == LanguageIdentifier.FRENCH) {
                Main.newError("Erreur de connexion à la base de données", "En quittant l'application, veuillez rouvrir avec une connexion stable au serveur.");
            } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                Main.newError("Error de conexión a la base de datos","Salida de la aplicación, vuelva a abrir con una conexión estable al servidor.");
            } else {
                Main.newError("Database connection error", "Application Exiting, please reopen with a stable connection to the server.");
            }
        	System.exit(-404);
        }
    }

    public ResultSet request(String reqStr) {
        if(connection != null) {
            try {
                Statement statement = connection.createStatement();
                if(reqStr.contains(";")) { //Over-glorified input validation
                   throw new SQLException("Unexpected character found.");
                }
                if(statement.execute(reqStr)) {
                    return statement.getResultSet();
                }
            } catch(SQLException ex) {
                if(Main.langIdent == LanguageIdentifier.FRENCH) {
                    Main.newError("Erreur de Demande", "Erreur de demande d'application, réessayez plus tard.");
                } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                    Main.newError("Solicitud de error", "Error de solicitud de aplicación, intente nuevamente más tarde.");
                } else {
                    Main.newError("Request Error", "Application request error, try request again later.");
                }
                ex.printStackTrace();
            }
        }
        return null;
    }
}
