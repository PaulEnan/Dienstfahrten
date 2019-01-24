/*
 * @author Joachim Borgloh + Paul Enan
 */
 package winfs.dienstreise.dienstfahrten;

import java.util.Date;
import java.util.LinkedList;
import java.util.Set;


public class DOSession {
	
	private Integer id;
	private LinkedList<DODestination> stations;
	private Integer duration;
	private double variableCosts;
	private String title;
	private Set<DOPerson> doPerson; 
	private String startLocation;
	private String startDate;

	public DOSession(LinkedList<DODestination> stations, int duration, double variableCosts, String title,
			Set<DOPerson> doPerson, String startLocation, String startDate) {
		this.stations = (LinkedList<DODestination>) stations.clone();
		this.duration = duration;
		this.variableCosts = variableCosts;
		this.title = title;
		this.doPerson = doPerson;
		this.setStartLocation(startLocation);
		this.startDate = startDate;
	}
	
	public DOSession(int id, LinkedList<DODestination> stations, int duration, double variableCosts, String title,
			Set<DOPerson> doPerson, String startLocation, String startDate) {
		this.id = id;
		this.stations = (LinkedList<DODestination>) stations.clone();
		this.duration = duration;
		this.variableCosts = variableCosts;
		this.title = title;
		this.doPerson = doPerson;
		this.setStartLocation(startLocation);
		this.startDate = startDate;
	}

	@Override
	public DOSession clone() {
		return new DOSession((LinkedList<DODestination>) stations.clone(), duration, variableCosts, title, doPerson, startLocation, startDate);
	}

	public LinkedList<DODestination> getStations() {
		return (LinkedList<DODestination>) stations.clone();
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

	public double getVariableCosts() {
		return variableCosts;
	}

	public int getDuration() {
		return duration;
	}
	
	public void setVariableCosts(double variableCosts) {
		this.variableCosts = variableCosts;
	}

	public double getFinalCosts() {
		return getVariableCosts() + getFixedCosts();
	}
	
	double getFixedCosts() {
		double costs = 0;
		for (DODestination dest : stations) {
			costs += dest.getTripExtraCosts() + dest.getSleepCosts() + dest.getFoodCosts();
		}

		return costs;
	}


	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<DOPerson> getNames() {
		return this.doPerson;
	}

	public void addName(DOPerson name) {
		this.doPerson.add(name);
	}
	
	public void removeName(DOPerson name) {
		this.doPerson.remove(name);
	}

	public Integer getId() {
		return this.id;
	};
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getDate() {
		return startDate;
	}

	public void setDate(String date) {
		this.startDate = date;
	}

	public String getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	};
}
