package winfs.dienstreise.dienstfahrten;

import java.util.Date;

public class DODestination {

    int id;
    int sleepCosts;
    int foodCosts;
    int tripExtraCosts;
    Date arrivalDate;
    Date departureDate;
    DOLocation location;

    public DODestination(int sleepCosts, int foodCosts, int tripExtraCosts,
                         DOLocation location, Date arrivalDate, Date departureDate) {
        this.sleepCosts = sleepCosts;
        this.foodCosts = foodCosts;
        this.tripExtraCosts = tripExtraCosts;
        this.location = location;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    public DODestination(int id, int sleepCosts, int foodCosts, int tripExtraCosts,
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
