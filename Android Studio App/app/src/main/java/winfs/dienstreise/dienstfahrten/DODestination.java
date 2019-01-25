package winfs.dienstreise.dienstfahrten;


public class DODestination {

    boolean isDummy;
    int id;
    double sleepCosts;
    double foodCosts;
    double tripExtraCosts;
    String location;
    String occasion;

    DODestination() {
        isDummy = true;
    }

    DODestination(double sleepCosts, double foodCosts, double tripExtraCosts,
                         String location, String occasion) {
        this.sleepCosts = sleepCosts;
        this.foodCosts = foodCosts;
        this.tripExtraCosts = tripExtraCosts;
        this.location = location;
        this.occasion = occasion;
    }

    DODestination(int id, double sleepCosts, double foodCosts, double tripExtraCosts,
                  String location, String occasion) {
        this.id = id;
        this.sleepCosts = sleepCosts;
        this.foodCosts = foodCosts;
        this.tripExtraCosts = tripExtraCosts;
        this.location = location;
        this.occasion = occasion;
    }

    void setId(int id) {
        this.id = id;
    }

    public boolean getIsDummy() {
        return isDummy;
    }
    public int getId() {
        return id;
    }

    public double getSleepCosts() {
        return sleepCosts;
    }
    public double getFoodCosts() {
        return foodCosts;
    }
    public double getTripExtraCosts() {
        return tripExtraCosts;
    }
    public String getLocation() {
        return location;
    }
    public String getOccasion() {
        return occasion;
    }
}
