package winfs.dienstreise.dienstfahrten;

import java.util.LinkedList;

/**
 *
 * @author Paul Enan
 */
public class SessionData {

    private LinkedList<String> stations;
    private double fixCosts;    
    private double variableCosts;
    private String name;
    private String person;
    private String cause;
    private String taxAllocation;

    public SessionData(LinkedList<String> stations,
            double fixCosts, double variableCosts, String name, String person, String cause, String taxAllocation) {
        this.stations = (LinkedList<String>) stations.clone();
        this.fixCosts = fixCosts;
        this.variableCosts = variableCosts;
        this.name = name;
        this.person = person;
        this.cause = cause;
        this.taxAllocation = taxAllocation;
    }

    @Override
    public SessionData clone() {
        return new SessionData((LinkedList<String>) stations.clone(), fixCosts, variableCosts, name, person, cause, taxAllocation);
    }

    public LinkedList<String> getStations() {
        return (LinkedList<String>) stations.clone();
    }

    public void addStation(String station, int index) {
        this.stations.add(index, station);
    }
    
    public void addStation(String station) {
        this.stations.add(station);
    }
    
    public void removeStation(int index) {
        this.stations.remove(index);
    }

    public double getFixCosts() {
        return fixCosts;
    }

    public void setFixCosts(double fixCosts) {
        this.fixCosts = fixCosts;
    }
    
    public double getVariableCosts() {
        return variableCosts;
    }

    public void setVariableCosts(double variableCosts) {
        this.variableCosts = variableCosts;
    }

    public double getFinalCosts() {
        return variableCosts + fixCosts;
    }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public String getPerson() { return this.person; }

    public void setPerson(String person) { this.person = person; }

    public String getCause() { return this.cause; }

    public void setCause(String cause) { this.cause = cause; }

    public String getTaxAllocation() { return this.taxAllocation; }

    public void setTaxAllocation(String taxAllocation) { this.taxAllocation = taxAllocation; }
}
