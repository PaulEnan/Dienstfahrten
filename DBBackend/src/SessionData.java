import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Joachim Borgloh
 */
public class SessionData {
	private Integer id;

	private LinkedList<Location> stations;
	private double fixCosts;
	private double variableCosts;
	private String title;
	private Set<Names> names; //vorher String und person
	private String ocassion; //refactor cause
	private String date; //neu
	private String duration; //neu

	public SessionData(LinkedList<Location> stations, double fixCosts, double variableCosts, String title,
			Set<Names> names, String ocassion, String date, String duration) {
		this.stations = (LinkedList<Location>) stations.clone();
		this.fixCosts = fixCosts;
		this.variableCosts = variableCosts;
		this.title = title;
		this.names = names;
		this.ocassion = ocassion;
		this.date = date;
		this.duration = duration;
	}
	
	public SessionData(int id, LinkedList<Location> stations, double fixCosts, double variableCosts, String title,
			Set<Names> names, String ocassion, String date, String duration) {
		this.id = id;
		this.stations = (LinkedList<Location>) stations.clone();
		this.fixCosts = fixCosts;
		this.variableCosts = variableCosts;
		this.title = title;
		this.names = names;
		this.ocassion = ocassion;
		this.date = date;
		this.duration = duration;
	}

	@Override
	public SessionData clone() {
		return new SessionData((LinkedList<Location>) stations.clone(), fixCosts, variableCosts, title, names, ocassion, date, duration);
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

	public Set<Names> getNames() {
		return this.names;
	}

	public void addName(Names name) {
		this.names.add(name);
	}
	
	public void removeName(Names name) {
		this.names.remove(name);
	}

	public String getOcassion() {
		return this.ocassion;
	}

	public void setOcassion(String cause) {
		this.ocassion = cause;
	}

	public Integer getId() {
		return this.id;
	};
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	};
}
