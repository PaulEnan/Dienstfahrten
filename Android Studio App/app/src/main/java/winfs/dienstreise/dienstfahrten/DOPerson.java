package winfs.dienstreise.dienstfahrten;

class DOPerson {
    int id;
    String prename;
    String surname;

    public DOPerson(String prename, String surname) {
        this.id = id;
        this.prename = prename;
        this.surname = surname;
    }

    public DOPerson(int id, String prename, String surname) {
        this.id = id;
        this.prename = prename;
        this.surname = surname;
    }

    public void setID(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return prename + " " + surname;
    }

    public int getId() {
        return id;
    }

    public String getPreName() {
        return prename;
    }

    public String getSurName() {
        return surname;
    }

    public void setId(int id) {
        this.id = id;
    }
}
