package logic;

import constants.Messages;
import data.Flights;
import data.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AirLineManager {
    private static final String JDBC_URLU = "jdbc:mysql://localhost:3306/users";
    private static final String JDBC_URLF = "jdbc:mysql://localhost:3306/flights";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private WriterManager writer = new WriterManager();
    private List<User> allUsers = new ArrayList<>();
    private List<Flights> allFlights = new ArrayList<>();

    private User currentUser = null;
    LocalDateTime myDateObj = LocalDateTime.now();
    DateTimeFormatter myDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDate = myDateObj.format(myDateTime);

    //signup is valid if:
    // -user doesn't have an account already;
    // -password is at least 8 characters long
    // -password matches confirmation_password
    public void signUp(String[] comenzi) {

        String email = comenzi[1];
        String name = comenzi[2];
        String password = comenzi[3];
        String confirmationPassword = comenzi[4];

        //check for user in allUsers
        boolean userAlreadyExists = allUsers.stream()
                .anyMatch(user -> user.getEmail().equals(email));
        if(userAlreadyExists){
            writer.write(Messages.getUserAlreadyExist());
            return;
        }

        if (password.length() < 8) {
            writer.write(Messages.getPasswordIsTooShort());
            return;
        }

        if (!password.equals(confirmationPassword)) {
            writer.write(Messages.getPasswordsDontMatch());
            return;
        }
        //add user to allUsers
        User user = new User(email, name, password);
        allUsers.add(user);
        writer.write(Messages.getUserAdded(user));
    }

    //Login is valid if: -user has signed up
    //- password matches the password sent in parameters
    //- current user is not null
    //if it passes the verification, user will be the current user
    public void login(String[] comenzi) {
        String email = comenzi[1];
        String password = comenzi[2];

        //find user in allUsers with matching email
        Optional<User> userOptional = allUsers.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
        if (userOptional.isEmpty()) {
            writer.write(Messages.getCannotFindUser(email));
            return;
        }
        User user = userOptional.get();
        if (!user.getPassword().equals(password)) {
            writer.write(Messages.getIncorrectPassword(password));
            return;
        }

        if (currentUser != null) {
            writer.write(Messages.getAnotherUserIsConnected());
            return;
        }
        currentUser = user;
        writer.write(Messages.getLogInSuccessfully(email, formattedDate));

    }

    // Logout is valid if email of current user matches the email sent as parameter
    public void logOut(String[] comenzi) {
        String email = comenzi[1];

        if (!currentUser.getEmail().equals(email)) {
            writer.write(Messages.getUserNotConnected(email));
            return;
        }
        writer.write(Messages.getUserDisconnected(currentUser.getEmail(), formattedDate));
        //currentUser will be disconnected
        currentUser = null;
    }

    // Add flight if flight id sent as parameter doesn't match with any flight from allFlights
    //otherwise add new flight
    public void addFlightDetails(String[] comenzi) {

        int id = Integer.parseInt(comenzi[1]);
        String from = comenzi[2];
        String to = comenzi[3];
        String dateAsString = comenzi[4];
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateAsString, dateTimeFormatter);
        int duration = Integer.parseInt(comenzi[5]);

        //check if there is a flight in allFlights matching flight id
        boolean sameFlight = allFlights.stream()
                .anyMatch(flight -> flight.getId() == id);
        if (sameFlight) {
            writer.write(Messages.getFlightAlreadyExists(id));
            return;
        }
        //add new flight to allFlights
        Flights flight = new Flights(id, from, to, date, duration);
        allFlights.add(flight);
        writer.write(Messages.getFlightAdded(from, to, date, duration));
    }

    //Add fights to users
    //Adding is valid if:
    //currentUser is not null
    //flight with flight id exists in allFlights
    //currentUser already have a ticket for the flight sent as parameter
    //otherwise, flight will be added to userFlights
    public void addUserFlight(String[] comenzi) {

        int flightId = Integer.parseInt(comenzi[1]);
        if (currentUser == null) {
            writer.write(Messages.getUserNotConnected());
            return;
        }
        //find the flight from allFlights matching the flightId
        Optional<Flights> flightsOptional = allFlights.stream()
                .filter(flight -> flight.getId() == flightId)
                .findFirst();
        if (flightsOptional.isEmpty()) {
            writer.write(Messages.getFlightDoesNotExist(flightId));
            return;
        }

        //check if user has a ticket for flight with given flightId
        boolean userAlreadyHaveTicket = currentUser.getUserFlights().stream()
                .anyMatch(flights -> flights.getId() == flightId);

        if (userAlreadyHaveTicket) {
            writer.write(Messages.getUserAlreadyHaveTicket(flightId, currentUser.getEmail()));
            return;
        }
        //add flight to userFlights
        currentUser.addFlight(flightsOptional.get());
        writer.write(Messages.getTicketPurchased(flightId, currentUser.getEmail()));
    }

    //Display all currentUser flights
    //Display is valid if :
    //currentUser is not null
    public void displayMyFlights(String[] comenzi) {
        if (currentUser == null) {
            writer.write(Messages.getUserNotConnected());
            return;
        }
        //check if currentUser has any flights
        Optional<Flights> test = currentUser.getUserFlights().stream()
                .findFirst();
        if (test.isEmpty()) {
            writer.write(Messages.getUserHasNoFLights(currentUser.getEmail()));
            return;
        }
        //display currentUser flights
        currentUser.getUserFlights()
                .forEach(flight -> writer.write(flight.toString()));
    }

    //Cancel user flights valid if:
    //currentUser is not null
    //check if there is flights with flight id sent as parameter
    //check if currentUser have a ticket for the given flight
    //otherwise, delete the flight from userFlights
    public void CancelFlight(String[] comenzi) {
        int id = Integer.parseInt(comenzi[1]);

        if (currentUser == null) {
            writer.write(Messages.getUserNotConnected());
            return;
        }
        //check for flights with flightId matching id
        Optional<Flights> findFlight = allFlights.stream()
                .filter(flights -> flights.getId() == id)
                .findFirst();
        if (findFlight.isEmpty()) {
            writer.write(Messages.getNoSuchFlights(id));
            return;
        }

        //check if current user has a ticket for the given flight
        boolean ticketMatch = currentUser.getUserFlights().stream()
                .anyMatch(flight -> flight.getId() == id);

        if (!ticketMatch) {
            writer.write(Messages.getDoesntHaveaTicket(currentUser.getEmail(), id));
            return;
        }

        //delete the flight form current user's flights
        currentUser.deleteUserFlight(findFlight.get());
        writer.write(Messages.getFlightCanceled(currentUser.getEmail(), id));
    }

    //Delete a flight
    //verify if there is a flight in allFlights matching the flightId sent as parameter,
    //if so, delete the flight from allFlights
    //verify if any user has a flight with flightId sent as parameter,
    //if so, delete the flight from userFlights and notify the user
    public void deleteFlight(String[] comenzi) {
        int flightId = Integer.parseInt(comenzi[1]);

        //verify if there is a flight in allFlights matching the flightId sent as parameter
        Optional<Flights> verifyFlight = allFlights.stream()
                .filter(flights -> flights.getId() == flightId)
                .findFirst();

        if (verifyFlight.isEmpty()) {
            writer.write(Messages.getFlightDoesNotExist(flightId));
            return;
        }

        //remove the flight
        allFlights.remove(verifyFlight.get());
        writer.write(Messages.getFlightDeleted(flightId));

        //remove the flight from userFlights and notify the user
        for (User user : allUsers) {
            boolean hasATicket = user.getUserFlights().stream()
                    .anyMatch(flights -> flights.getId() == flightId);
            if (hasATicket) {
                writer.write(Messages.getFlightCanceledBecauseWhyNot(flightId, user.getEmail()));
                user.deleteUserFlight(verifyFlight.get());
            }
        }
    }

    //Display all flights
    public void displayFlights(String[] comenzi) {
        allFlights
                .forEach(flight -> writer.write(flight.toString().replace("{", "").replace("}", "")));
    }

    //Delete a user for allUsers
    //check if user exists in allUsers
    //remove user from allUsers
    public void deleteUser(String[] comenzi) {
        String email = comenzi[1];

        //find the user matching the email
        Optional<User> findUser = allUsers.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();

        if(findUser.isEmpty()){
            writer.write(Messages.getUserDoesntExist(email));
            return;
        }

        //remove the user from allUsers and make current user null
        allUsers.remove(findUser.get());
        writer.write(Messages.getUserRemoved(email));
        currentUser=null;
    }

    //Add users to a database SQL
    public void persistUser(String[] comenzi) {

        try (Connection connection = DriverManager.getConnection(JDBC_URLU, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
                String deleteUser = "DELETE from users WHERE id = 1";
                statement.executeUpdate(deleteUser);
            for(User user :allUsers){
                String insertUser = "INSERT INTO users VALUES(1,'" + user.getEmail() + "' , '" + user.getName() +
                        "' ,'" + user.getPassword() + "')";
                statement.executeUpdate(insertUser);
            }
            writer.write(Messages.getUserSaved(formattedDate));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Add flights to a database SQL
    public void persistFlights(String[] comenzi) {
        try (Connection connection = DriverManager.getConnection(JDBC_URLF, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
                String deleteFlight = "DELETE from flights WHERE no=1";
                statement.executeUpdate(deleteFlight);
            for(Flights flights : allFlights){
                String insertFlight = "INSERT INTO flights VALUES(1,'" + flights.getId() + "' , '" + flights.getFrom() +
                        " ','" + flights.getTo() + " ','" + flights.getDate() + " ','" + flights.getDuration() + "')";
                statement.executeUpdate(insertFlight);
            }
            writer.write(Messages.getFlightsSaved(formattedDate));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void defaultCommand(String[] comenzi) {
        writer.write(Messages.getWrongCommand());
    }

    public void closeWriterManager() {
        writer.close();
    }

}

