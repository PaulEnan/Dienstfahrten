package winfs.dienstreise.dienstfahrten;

import java.util.Date;

public class DODestination {

    int id;
    double sleepCosts;
    double foodCosts;
    double tripExtraCosts;
    String location;

    public DODestination(double sleepCosts, double foodCosts, double tripExtraCosts,
                         String location) {
        this.sleepCosts = sleepCosts;
        this.foodCosts = foodCosts;
        this.tripExtraCosts = tripExtraCosts;
        this.location = location;
    }

    public DODestination(int id, double sleepCosts, double foodCosts, double tripExtraCosts,
                         String location) {
        this.id = id;
        this.sleepCosts = sleepCosts;
        this.foodCosts = foodCosts;
        this.tripExtraCosts = tripExtraCosts;
        this.location = location;
    }

    void setId(int id) {
        this.id = id;
    }
}
