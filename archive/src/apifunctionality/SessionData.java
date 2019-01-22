/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apifunctionality;

import java.util.LinkedList;

/**
 *
 * @author Paul Enan
 */
public class SessionData {

    private Language language;
    private LinkedList<String> stations;
    private double fixCosts;    
    private double variableCosts;
    private double finalCosts;

    public SessionData(Language language, LinkedList<String> stations,
            double fixCosts, double variableCosts, double finalCosts) {
        this.language = language;
        this.stations = (LinkedList<String>) stations.clone();
        this.fixCosts = fixCosts;
        this.variableCosts = variableCosts;
        this.finalCosts = finalCosts;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
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
        return finalCosts;
    }

    public void setFinalCosts(double finalCosts) {
        this.finalCosts = finalCosts;
    }
}
