package constants;

import data.Flights;
import data.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Mesaje {

    public static String getUserAdded(User user){
        return "User with email " + user.getEmail() + " was successfully added!";
    }

    public static String getUserAlreadyExist(){
        return "User already exists!";
    }

    public static String getCannotFindUser(String email) {
        return "Cannot find user with email " + email + ".";
    }

    public static String getIncorrectPassword(String parola) {
        return "Incorrect password!";
    }

    public static String getLogInSuccessfully(String email, String myDateTime) {
        return "User with email " + email + " is the current user started from " + myDateTime + ".";
    }

    public static String getFlightAlreadyExists(int id) {
        return "Cannot add flight! There is already a flight with id = " +id;
    }

    public static String getFlightAdded(String from, String to, LocalDate date, int duration) {
        return "Flight from " + from+ " to " + to + " ,date " + date + " , duration " + duration+ " successfully added!";
    }

    public static String getUserNotConnected() {
        return "There is no connected user!";
    }

    public static String getFlightDoesNotExist(int flightId) {
        return "The flight with id " + flightId + " does not exist!";
    }

    public static String getUserAlreadyHaveTicket(int flightId, String userEmail) {
        return "The user with email" + userEmail + " already have a ticket for flight with id " + flightId + "!";
    }

    public static String getTicketPurchased(int flightId, String email) {

            return "The flight with id " + flightId+ " was successfully added for user with email " + email + "!";
        }

    public static String getAnotherUserIsConnected() {
            return "Another user is already connected!";
    }

    public static String getUserDisconnected(String email, String formattedDate) {
        return "User with email " + email + " successfully disconnected at " + formattedDate + ".";
    }

    public static String getPasswordIsTooShort() {
        return "Cannot add user! Password too short";
    }

    public static String getPasswordsDontMatch() {
        return "Cannot add user! The passwords are different!";
    }

    public static String getAllUsers(List<User> allUsers) {
        return "The users are: " + allUsers;
    }

    public static String getUserNotConnected(String email) {
        return "The user with email " + email + " was not connected!";
    }

    public static String getUsersFlights(String from, String to, LocalDate date, int duration) {
        return "Flight from "+from+" to "+to+" , date "+date+" , duration "+duration+" ." ;
    }

    public static String getZboruri(List<Flights> userFlights) {
        return "Flights: " + userFlights;
    }

    public static String getNoSuchFlights(int id) {
        return "The flight with id "+ id + " doesn’t exist!";
    }

    public static String getDoesntHaveaTicket(String email, int id) {
        return "The user with email " +email+ " doesn't have a ticket for the flight with id " +id+"!";
    }

    public static String getFlightCanceled(String email, int id) {
        return "The user with email "+ email +" has successfully canceled his ticket for flight with id "+id+ "!";
    }

    public static String getUserHasNoFLights(String email) {
        return "User with email " + email + " has no flights!";
    }

    public static String getFlightDeleted(int id) {
        return "Flight with id "+id+" successfully deleted!";
    }

    public static String getFlightCanceledBecauseWhyNot(int flightId, String email) {
        return "The user with email " +email+ " was notified that the flight with id " +flightId+ " was canceled!";
    }

    public static String getWrongCommand() {
        return "You have entered the wrong command!";
    }

    public static String getUserSaved(String myDateTime) {
        return "The users were successfully saved in the database at " +myDateTime+ ".";
    }

    public static String getFlightsSaved(String formattedDate) {
        return "The flights were successfully saved in the database at " + formattedDate +".";
    }

    public static String getUserDoesntExist(String email) {
        return "User with emial " + email+ " does not exist!";
    }

    public static String getUserDeleted(String email) {
        return "User with email " + email + " was successfully deleted!";
    }
}

