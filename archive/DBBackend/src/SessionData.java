import java.util.LinkedList;

/**
 *
 * @author Paul Enan
 */
public class SessionData {
	private Integer id;

	private LinkedList<Location> stations;
	private double fixCosts;
	private double variableCosts;
	private String title;
	private String person;
	private String cause;

	public SessionData(LinkedList<Location> stations, double fixCosts, double variableCosts, String title,
			String person, String cause) {
		this.stations = (LinkedList<Location>) stations.clone();
		this.fixCosts = fixCosts;
		this.variableCosts = variableCosts;
		this.title = title;
		this.person = person;
		this.cause = cause;
	}
	
	public SessionData(int id, LinkedList<Location> stations, double fixCosts, double variableCosts, String title,
			String person, String cause) {
		this.id = id;
		this.stations = (LinkedList<Location>) stations.clone();
		this.fixCosts = fixCosts;
		this.variableCosts = variableCosts;
		this.title = title;
		this.person = person;
		this.cause = cause;
	}

	@Override
	public SessionData clone() {
		return new SessionData((LinkedList<Location>) stations.clone(), fixCosts, variableCosts, title, person, cause);
	}

	public LinkedList<Location> getStations() {
		return (LinkedList<Location>) stations.clone();
	}

	public void addStation(Location station, int index) {
		this.stations.add(index, station);
	}

	public void addStation(Location station) {
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

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPerson() {
		return this.person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getCause() {
		return this.cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public Integer getId() {
		return this.id;
	};
	
	public void setId(Integer id) {
		this.id = id;
	};
}
