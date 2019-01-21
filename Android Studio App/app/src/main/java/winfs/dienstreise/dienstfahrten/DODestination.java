package winfs.dienstreise.dienstfahrten;

import java.util.Date;

public class DODestination {

    int id;
    double sleepCosts;
    double foodCosts;
    double tripExtraCosts;
    Date arrivalDate;
    Date departureDate;
    DOLocation location;

    public DODestination(double sleepCosts, double foodCosts, double tripExtraCosts,
                         DOLocation location, Date arrivalDate, Date departureDate) {
        this.sleepCosts = sleepCosts;
        this.foodCosts = foodCosts;
        this.tripExtraCosts = tripExtraCosts;
        this.location = location;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    public DODestination(int id, double sleepCosts, double foodCosts, double tripExtraCosts,
                         DOLocation location, Date arrivalDate, Date departureDate) {
        this.id = id;
        this.sleepCosts = sleepCosts;
        this.foodCosts = foodCosts;
        this.tripExtraCosts = tripExtraCosts;
        this.location = location;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    void setId(int id) {
        this.id = id;
    }
}
