package data;

import java.time.LocalDate;

public class Flights {

    private int id;
    private String from;
    private String to;
    private LocalDate date;
    private int duration;

    public Flights(int id, String from, String to, LocalDate date, int duration) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.date = date;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "{Flight from " + from+" to " + to + " , date " +date+" , duration " + duration+ ".}";
    }

}
