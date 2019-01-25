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
}
