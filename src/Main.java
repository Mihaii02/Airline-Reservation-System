import constants.Commands;
import logic.AirLineManager;
import logic.AirLineStatistics;
import logic.ReaderManager;
import logic.WriterManager;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) throws IOException {

        AirLineManager manager = new AirLineManager();

        BufferedReader reader = ReaderManager.createReader();

        String line = reader.readLine();

        while (line != null) {

            String[] comenzi = line.split(" ");
            Commands command;
            try {
                command = Commands.valueOf(comenzi[0]);
            } catch (IllegalArgumentException e) {
                command = Commands.DEFAULT_COMMAND;
            }

            switch (command) {
                case SIGNUP: {
                    manager.signUp(comenzi);
                    break;
                }
                case LOGIN: {
                    manager.login(comenzi);
                    break;
                }
                case LOGOUT: {
                    manager.logOut(comenzi);
                    break;
                }
                case DISPLAY_MY_FLIGHTS: {
                    manager.displayMyFlights(comenzi);
                    break;
                }
                case ADD_FLIGHT: {
                    manager.addUserFlight(comenzi);
                    break;
                }
                case CANCEL_FLIGHT: {
                    manager.CancelFlight(comenzi);
                    break;
                }
                case ADD_FLIGHT_DETAILS: {
                    manager.addFlightDetails(comenzi);
                    break;
                }
                case DELETE_FLIGHT: {
                    manager.deleteFlight(comenzi);
                    break;
                }
                case DISPLAY_FLIGHTS: {
                    manager.displayFlights(comenzi);
                    break;
                }
                case PERSIST_FLIGHTS: {
                    manager.persistFlights(comenzi);
                    break;
                }
                case PERSIST_USER: {
                    manager.persistUser(comenzi);
                    break;
                }
                case DELETE_USER:{
                    manager.deleteUser(comenzi);
                    break;
                }
                case DEFAULT_COMMAND: {
                    manager.defaultCommand(comenzi);
                    break;
                }
            }
            line = reader.readLine();
        }
        manager.closeWriterManager();


    }
}