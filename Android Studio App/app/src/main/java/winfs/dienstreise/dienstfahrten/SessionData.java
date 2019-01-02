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

    public SessionData(LinkedList<String> stations,
            double fixCosts, double variableCosts) {
        this.stations = (LinkedList<String>) stations.clone();
        this.fixCosts = fixCosts;
        this.variableCosts = variableCosts;
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
}
