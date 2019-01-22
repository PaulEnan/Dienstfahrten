/*
 * @
 */
public class Location {
	private Integer id;
	private String street;
	private String postCode;
	private String city;
	private int streetNumber;

    public Location(String street, String postCode, String city, int streetNumber) {
        this.setStreet(street);
        this.setPostCode(postCode);
        this.setCity(city);
        this.setStreetNumber(streetNumber);
    }
    
    public Location(int id, String street, String postCode, String city, int streetNumber) {
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
    
    
}
