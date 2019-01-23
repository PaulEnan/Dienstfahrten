/*
 * @
 */
public class Location {
	private Integer id;
	private String street;
	private String postCode;
	private String city;
	private int streetNumber;
	private String fullAdress;

	public Location(String street, int streetNumber, String postCode, String city, String fullAdress) {
		this.setStreet(street);
		this.setPostCode(postCode);
		this.setCity(city);
		this.setStreetNumber(streetNumber);
		this.setFullAdress(fullAdress);
	}

	public Location(int id, String street, int streetNumber, String postCode, String city, String fullAdress) {
		this.setStreet(street);
		this.setPostCode(postCode);
		this.setCity(city);
		this.setStreetNumber(streetNumber);
		this.setFullAdress(fullAdress);
		this.setId(id);
	}
	
	public Location(String street, int streetNumber, String postCode, String city) {
		this.setStreet(street);
		this.setPostCode(postCode);
		this.setCity(city);
		this.setStreetNumber(streetNumber);
	}

	public Location(int id, String street, int streetNumber, String postCode, String city) {
		this.setStreet(street);
		this.setPostCode(postCode);
		this.setCity(city);
		this.setStreetNumber(streetNumber);
		this.setId(id);
	}

	public Location(String fullAdress) {
		this.setFullAdress(fullAdress);
	}

	public Location(int id, String fullAdress) {
		this.setId(id);
		this.setFullAdress(fullAdress);
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

	public String getFullAdress() {
		return fullAdress;
	}

	public void setFullAdress(String fullAdress) {
		this.fullAdress = fullAdress;
	}

}
