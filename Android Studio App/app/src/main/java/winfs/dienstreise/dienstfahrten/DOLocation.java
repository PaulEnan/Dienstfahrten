package winfs.dienstreise.dienstfahrten;

public class DOLocation {
    Integer id;
    String street;
    String postCode;
    String city;
    int streetNumber;

    public DOLocation(String street, String postCode, String city, int streetNumber) {
        this.setStreet(street);
        this.setPostCode(postCode);
        this.setCity(city);
        this.setStreetNumber(streetNumber);
    }

    public DOLocation(int id, String street, String postCode, String city, int streetNumber) {
        this.setStreet(street);
        this.setPostCode(postCode);
        this.setCity(city);
        this.setStreetNumber(streetNumber);
        this.setId(id);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    void setId(int id) {
        this.id = id;
    }
}
