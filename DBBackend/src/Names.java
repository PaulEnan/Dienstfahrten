
public class Names {
	private Integer id;
	private String name;
	private String lastName;

	public Names(String name, String lastName) {
	        this.setName(name);
	        this.setLastName(lastName);
	  	    }

	public Names(int id, String name, String lastName) {
	        this.setName(name);
	        this.setLastName(lastName);
	        this.setId(id);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	
}
