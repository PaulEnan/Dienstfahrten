package winfs.dienstreise.dienstfahrten;

import java.util.LinkedList;

/**
 * @author Paul Enan
 */
public class CentralLogic {
    private ISaveLoadHandler saveLoadHandler;
    private Calculator calculator;
    private SessionData session;

    public CentralLogic(IApiUser apiUser, ISaveLoadHandler saveLoadHandler) {
        this(apiUser, saveLoadHandler, new LinkedList<Location>(), 0, 0, "", "", "");
    }

    public CentralLogic(IApiUser apiUser, ISaveLoadHandler saveLoadHandler,
                        LinkedList<Location> stations, double fixCosts, double variableCosts,
                        String name, String person, String cause) {
        this(apiUser, saveLoadHandler, new SessionData(
                stations, fixCosts, variableCosts, name, person, cause));
    }

    public CentralLogic(IApiUser apiUser, ISaveLoadHandler saveLoadHandler, SessionData session) {
        this.saveLoadHandler = saveLoadHandler;
        this.calculator = new Calculator(apiUser);
        this.session = session;
    }

    public void SaveSession(String path) throws DienstfahrtenException {
        try {
            saveLoadHandler.Save(session);
        } catch (SaveLoadException ex) {
            throw new DienstfahrtenException(ex.getMessage());
        }
    }

    public SessionData LoadSession(int index) throws DienstfahrtenException {
        try {
            this.session = saveLoadHandler.Load(index);
            return session;
        } catch (SaveLoadException ex) {
            throw new DienstfahrtenException(ex.getMessage());
        }
    }

    public String[] calculateCosts() throws DienstfahrtenException {
        String[] stationArr = session.getStations().toArray(new String[session.getStations().size()]);
        String result = calculator.caclculateVariableCostsForMultipleStations(stationArr, session.getVariableCosts());
        if (result.contains("||")) {
            String[] options = result.split("||");
            return options;
        } else {
            if (result.toLowerCase().startsWith("error:")) {
                throw new DienstfahrtenException(result);
            } else {
                this.session.setVariableCosts(Double.parseDouble(result));
                return new String[] { session.getFinalCosts() + "" };
            }
        }
    }

    public String[] useAutoCompleter(String text) throws DienstfahrtenException {
        String result = calculator.autoCompleteAddress(text);
        if (result.contains("||")) {
            String[] options = result.split("||");
            return options;
        } else {
            if (result.toLowerCase().startsWith("error:")) {
                throw new DienstfahrtenException(result);
            }
        }
        return new String[0];
    }

    public String[] getAllSessionNames() throws SaveLoadException {
        SessionData[] sessions = saveLoadHandler.getAllSessions();
        String[] result = new String[sessions.length];
        for (int i = 0; i < sessions.length; i++) {
            result[i] = sessions[i].getTitle();
        }
        return result;
    }

    public SessionData getSession() {
        return session.clone();
    }

    public void addStation(Location station) {
        session.addStation(station);
    }

    public void addStation(Location station, int index) {
        session.addStation(station, index);
    }

    public void removeStation(int index) {
        if (index > 0 && index < session.getStations().size()) {
            session.removeStation(index);
        }
    }

    public void changeFixCosts(String costs) {
        session.setFixCosts(Double.parseDouble(costs));
    }

    public void changeVariableCosts(String costs) {
        session.setVariableCosts(Double.parseDouble(costs));
    }

    public void changePerson(String person) {
        session.setPerson(person);
    }

    public void changeCause(String cause) {
        session.setCause(cause);
    }
}
