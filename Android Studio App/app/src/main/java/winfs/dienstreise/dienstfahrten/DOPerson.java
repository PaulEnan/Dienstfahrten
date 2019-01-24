/*
 * @author Joachim Borgloh
 */
 package winfs.dienstreise.dienstfahrten;
public class DOPerson {
	private Integer id;
	private String prename;
	private String surname;

	public DOPerson(String prename, String surname) {
	        this.setPreName(prename);
	        this.setSurName(surname);
	  	    }

	public DOPerson(int id, String prename, String surname) {
	        this.setPreName(prename);
	        this.setSurName(surname);
	        this.setId(id);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPreName() {
		return prename;
	}

	public void setPreName(String prename) {
		this.prename = prename;
	}
	
	public String getSurName() {
		return surname;
	}

	public void setSurName(String surname){
		this.surname = surname;
	}
	
}
