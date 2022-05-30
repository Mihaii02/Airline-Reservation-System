package logic;

import constants.Mesaje;
import data.Flights;
import data.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
// -password's length is higher than 8 characters
// -password matches confirmation_password
    public void signUp(String[] comenzi) {

        String email = comenzi[1];
        String nume = comenzi[2];
        String parola = comenzi[3];
        String parola_confirmare = comenzi[4];

        for (User user : allUsers) {
            if (user.getEmail().equals(email)) {
                writer.write(Mesaje.getUserAlreadyExist());
                return;
            }
        }
        if (parola.length() < 8) {
            writer.write(Mesaje.getPasswordIsTooShort());
            return;
        }
        if (!parola.equals(parola_confirmare)) {
            writer.write(Mesaje.getPasswordsDontMatch());
            return;
        }
        User user = new User(email, nume, parola);
        allUsers.add(user);
        writer.write(Mesaje.getUserAdded(user));
    }

    public void login(String[] comenzi) {
        String email = comenzi[1];
        String parola = comenzi[2];

        Optional<User> userOptional = allUsers.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
        if (userOptional.isEmpty()) {
            writer.write(Mesaje.getCannotFindUser(email));
            return;
        }
        User user = userOptional.get();
        if (!user.getPassword().equals(parola)) {
            writer.write(Mesaje.getIncorrectPassword(parola));
            return;
        }
        if (currentUser != null) {
            writer.write(Mesaje.getAnotherUserIsConnected());
            return;
        }
        currentUser = user;
        writer.write(Mesaje.getLogInSuccessfully(email, formattedDate));

    }

    public void logOut(String[] comenzi) {
        String email = comenzi[1];
        if (!currentUser.getEmail().equals(email)) {
            writer.write(Mesaje.getUserNotConnected(email));
            return;
        }
        writer.write(Mesaje.getUserDisconnected(currentUser.getEmail(), formattedDate));
        currentUser = null;
    }

    public void addFlightDetails(String[] comenzi) {

        int id = Integer.parseInt(comenzi[1]);
        String from = comenzi[2];
        String to = comenzi[3];
        String dateAsString = comenzi[4];
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateAsString, dateTimeFormatter);
        int duration = Integer.parseInt(comenzi[5]);

        boolean sameFlight = allFlights.stream()
                .anyMatch(flight -> flight.getId() == id);
        if (sameFlight) {
            writer.write(Mesaje.getFlightAlreadyExists(id));
            return;
        }
        /*for (Flights flight : allFlights) {
            if (flight.getId() == id) {
                writer.write(Mesaje.getFlightAlreadyExists(id));
                return;
            }
        }*/
        Flights flight = new Flights(id, from, to, date, duration);
        allFlights.add(flight);
        writer.write(Mesaje.getFlightAdded(from, to, date, duration));
    }

    public void addUserFlight(String[] comenzi) {

        int flightId = Integer.parseInt(comenzi[1]);
        if (currentUser == null) {
            writer.write(Mesaje.getUserNotConnected());
            return;
        }
        Optional<Flights> flightsOptional = allFlights.stream()
                .filter(flight -> flight.getId() == flightId)
                .findFirst();
        if (flightsOptional.isEmpty()) {
            writer.write(Mesaje.getFlightDoesNotExist(flightId));
            return;
        }

        boolean userAreDejaBilet = currentUser.getUserFlights().stream()
                .anyMatch(flights -> flights.getId() == flightId);

        if (userAreDejaBilet) {
            writer.write(Mesaje.getUserAlreadyHaveTicket(flightId, currentUser.getEmail()));
            return;
        }
        currentUser.addFlight(flightsOptional.get());
        writer.write(Mesaje.getTicketPurchased(flightId, currentUser.getEmail()));
    }

    public void displayMyFlights(String[] comenzi) {
        if (currentUser == null) {
            writer.write(Mesaje.getUserNotConnected());
            return;
        }
        Optional<Flights> test = currentUser.getUserFlights().stream()
                .findFirst();
        if (test.isEmpty()) {
            writer.write(Mesaje.getUserHasNoFLights(currentUser.getEmail()));
            return;
        }
        currentUser.getUserFlights().stream()
                .forEach(flight -> writer.write(flight.toString()));
    }

    public void CancelFlight(String[] comenzi) {
        int id = Integer.parseInt(comenzi[1]);
        if (currentUser == null) {
            writer.write(Mesaje.getUserNotConnected());
            return;
        }
        Optional<Flights> findFlight = allFlights.stream()
                .filter(flights -> flights.getId() == id)
                .findFirst();
        if (findFlight.isEmpty()) {
            writer.write(Mesaje.getNoSuchFlights(id));
            return;
        }
        boolean ticketMatch = currentUser.getUserFlights().stream()
                .anyMatch(flight -> flight.getId() == id);

        if (!ticketMatch) {
            writer.write(Mesaje.getDoesntHaveaTicket(currentUser.getEmail(), id));
            return;
        }
        currentUser.deleteFlight(currentUser.getUserFlights());
        writer.write(Mesaje.getFlightCanceled(currentUser.getEmail(), id));
    }

    public void deleteFlight(String[] comenzi) {
        int flightId = Integer.parseInt(comenzi[1]);

        Optional<Flights> verifyFlight = allFlights.stream()
                .filter(flights -> flights.getId() == flightId)
                .findFirst();
        if (verifyFlight.isEmpty()) {
            writer.write(Mesaje.getFlightDoesNotExist(flightId));
            return;
        }
        Iterator<Flights> it = allFlights.iterator();
        while (it.hasNext()) {
            Flights element = it.next();
            if (element.getId() == flightId) {
                it.remove();
                writer.write(Mesaje.getFlightDeleted(flightId));
            }
        }
        for (User user : allUsers) {
            boolean hasATicket = user.getUserFlights().stream()
                    .anyMatch(flights -> flights.getId() == flightId);
            if (hasATicket) {
                writer.write(Mesaje.getFlightCanceledBecauseWhyNot(flightId, user.getEmail()));
            }
            user.deleteUserFligth(flightId);
        }

    }

    public void displayFlights(String[] comenzi) {
        allFlights
                .forEach(flight -> writer.write(flight.toString().replace("{", "").replace("}", "")));
    }

    public void deleteUser(String[] comenzi) {
        String email = comenzi[1];
        boolean userExists = allUsers.stream()
                .anyMatch(user -> user.getEmail().equals(email));
        if(!userExists){
            writer.write(Mesaje.getUserDoesntExist(email));
            return;
        }
        Iterator<User> iterator = allUsers.iterator();
        while(iterator.hasNext()){
            User user = iterator.next();
            if(user.getEmail().equals(email)){
                iterator.remove();
                writer.write(Mesaje.getUserDeleted(email));
            }
        }
    }

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
            writer.write(Mesaje.getUserSaved(formattedDate));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

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
            writer.write(Mesaje.getFlightsSaved(formattedDate));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void defaultCommand(String[] comenzi) {
        writer.write(Mesaje.getWrongCommand());
    }

    public void closeWriterManager() {
        writer.close();
    }

}

