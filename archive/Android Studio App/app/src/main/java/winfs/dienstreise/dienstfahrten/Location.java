package winfs.dienstreise.dienstfahrten;

public class Location {
    String street;
    String postCode;
    String city;
    int streetNumber;


    public Location(String street, String postCode, String city, int streetNumber) {
        this.street = street;
        this.postCode = postCode;
        this.city = city;
        this.streetNumber = streetNumber;
    }
}
