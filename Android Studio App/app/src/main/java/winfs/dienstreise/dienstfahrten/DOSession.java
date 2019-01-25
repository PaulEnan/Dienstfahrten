package winfs.dienstreise.dienstfahrten;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author winf101441
 */
public class DOSession {

    int duration;
    boolean isDummy;
    int id;
    List<DODestination> stations;
    String title;
    DOPerson person;
    String startLocation;
    Date startDate;
    private double variableCosts;

    public DOSession() {
        isDummy = true;
        stations = new LinkedList<>();
    }

    public DOSession(int id, List<DODestination> stations, String title, DOPerson person,
                     String startLocation, Date startDate, int duration, double variableCosts) {
        this.id = id;
        this.stations = stations;
        this.title = title;
        this.person = person;
        this.startLocation = startLocation;
        this.startDate = startDate;
        isDummy = false;
        this.duration = duration;
        this.variableCosts = variableCosts;
    }

    List<DODestination> getStations() {
        return stations;
    }

    void addStation(DODestination station, int index) {
        this.stations.add(index, station);
    }

    void addStation(DODestination station) {
        this.stations.add(station);
    }

    void removeStation(int index) {
        this.stations.remove(index);
    }

    public void setTitle(String title) { this.title = title; }

    void setPerson(DOPerson person) {
        this.person = person;
    }

    void setId(int id) {
        this.id = id;
    }

    double getFinalCosts() {
        return getVariableCosts() + getFixedCosts();
    }

    void setVariableCosts(double costs) {
        this.variableCosts = costs;
    }

    double getFixedCosts() {
        double costs = 0;
        for (DODestination dest : stations) {
            costs += dest.tripExtraCosts + dest.sleepCosts + dest.foodCosts;
        }

        return costs;
    }

    double getVariableCosts() { return variableCosts; }

    String getLastLocation() {
        return stations.get(stations.size() - 1).location;
    }

    boolean onlyOneDestination() {
        return stations.size() == 1;
    }

    DODestination getStationAt(int position) {
        return stations.get(position);
    }
}
