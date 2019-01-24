/*
 * @author Joachim Borgloh
 */
 package winfs.dienstreise.dienstfahrten;

import java.util.Date;
public class DODestination {

    private int id;
    private double sleepCosts;
    private double foodCosts;
    private double tripExtraCosts;
    private String location;
    private String occasion;

    public DODestination(double sleepCosts, double foodCosts, double tripExtraCosts,
                         String location, String occasion) {
        this.setSleepCosts(sleepCosts);
        this.setFoodCosts(foodCosts);
        this.setTripExtraCosts(tripExtraCosts);
        this.setLocation(location);
        this.setOccasion(occasion);
    }

    public DODestination(int id, double sleepCosts, double foodCosts, double tripExtraCosts,
                         String location, String occasion) {
        this.setId(id);
        this.setSleepCosts(sleepCosts);
        this.setFoodCosts(foodCosts);
        this.setTripExtraCosts(tripExtraCosts);
        this.setLocation(location);
        this.setOccasion(occasion);
    }

	public double getSleepCosts() {
		return sleepCosts;
	}

	public void setSleepCosts(double sleepCosts) {
		this.sleepCosts = sleepCosts;
	}

	public double getFoodCosts() {
		return foodCosts;
	}

	public void setFoodCosts(double foodCosts) {
		this.foodCosts = foodCosts;
	}

	public String getOccasion() {
		return occasion;
	}

	public void setOccasion(String occasion) {
		this.occasion = occasion;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getTripExtraCosts() {
		return tripExtraCosts;
	}

	public void setTripExtraCosts(double tripExtraCosts) {
		this.tripExtraCosts = tripExtraCosts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}