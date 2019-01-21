package winfs.dienstreise.dienstfahrten;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Paul Enan
 */
public class DOSession {

    int id;
    List<DODestination> stations;
    String title;
    DOPerson person;
    DOLocation startLocation;
    Date startDate;

    public DOSession(int id, List<DODestination> stations, String title, DOPerson person,
                     DOLocation startLocation, Date startDate) {
        this.id = id;
        this.stations = stations;
        this.title = title;
        this.person = person;
        this.startLocation = startLocation;
        this.startDate = startDate;
    }

    public List<DODestination> getStations() {
        return stations;
    }

    public void addStation(DODestination station, int index) {
        this.stations.add(index, station);
    }
    
    public void addStation(DODestination station) {
        this.stations.add(station);
    }
    
    public void removeStation(int index) {
        this.stations.remove(index);
    }

    public String getTitle() { return this.title; }

    public void setTitle(String title) { this.title = title; }

    public DOPerson getPerson() { return this.person; }

    public void setPerson(DOPerson person) { this.person = person; }

    public void setId(int id) {
        this.id = id;
    }

    public double getFinalCosts() {
        return getVariableCosts() + getTripExtraCosts() + getSleepCosts() + getFoodCosts();
    }

    private double getFoodCosts() {
        return 0;
    }

    private double getSleepCosts() {
        return 0;
    }

    private double getTripExtraCosts() {
        return 0;
    }

    private double getVariableCosts() {
        return 0;
    }
}
