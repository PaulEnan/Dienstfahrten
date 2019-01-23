package winfs.dienstreise.dienstfahrten;

import java.util.Date;

public class DODestination {

    int id;
    double sleepCosts;
    double foodCosts;
    double tripExtraCosts;
    String location;
    String occasion;

    public DODestination(double sleepCosts, double foodCosts, double tripExtraCosts,
                         String location, String occasion) {
        this.sleepCosts = sleepCosts;
        this.foodCosts = foodCosts;
        this.tripExtraCosts = tripExtraCosts;
        this.location = location;
        this.occasion = occasion;
    }

    public DODestination(int id, double sleepCosts, double foodCosts, double tripExtraCosts,
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
}