package data;

import constants.Mesaje;
import logic.AirLineManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class User {

    private int id;
    private String email;
    private String name;
    private String password;
    private List<Flights> userFlights = new ArrayList<>();


    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }


    public List<Flights> getUserFlights() {
        return userFlights;
    }


    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


    public void addFlight(Flights flights) {
        userFlights.add(flights);
    }

    public void deleteFlight(List<Flights> userFlights) {
        userFlights.remove(getUserFlights().get(id));
    }

    public void deleteUserFligth(int flightId) {
        userFlights.remove(flightId);
        Iterator<Flights> iterator = userFlights.iterator();
        while (iterator.hasNext()) {
            Flights flights = iterator.next();
                if (flights.getId() == flightId) {
                    iterator.remove();

        }
    }
}



    }
