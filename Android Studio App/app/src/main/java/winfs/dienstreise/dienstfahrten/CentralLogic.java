package winfs.dienstreise.dienstfahrten;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author winf101441
 */
public class CentralLogic {
    private ISaveLoadHandler saveLoadHandler;
    private Calculator calculator;
    List<DOSession> sessions;
    DOSession curSession;

    public final static double variableCostRate = 0.30;

    public CentralLogic(IApiUser apiUser, ISaveLoadHandler saveLoadHandler) {
        this.saveLoadHandler = saveLoadHandler;
        this.calculator = new Calculator(apiUser);
        try {
            sessions = saveLoadHandler.getAllSessions();
        } catch (Exception ex) {
        }
        if(sessions == null) {
            sessions = new LinkedList<>();
        }
        if (sessions.size() >= 1) {
            curSession = sessions.get(0);
        }
    }

    public void SaveSession(DOSession session) throws DienstfahrtenException {
        if (!saveLoadHandler.Save(session)) {
            throw new DienstfahrtenException("Speichern nicht möglich");
        }
    }

    public DOSession LoadSession(int id) throws DienstfahrtenException {
        if(id != -1) {
            try {
                curSession =  saveLoadHandler.Load(id);
            } catch (Exception ex) {
                throw new DienstfahrtenException(ex.getMessage());
            }
        } else {
            curSession = new DOSession(-1, new LinkedList<DODestination>(), "", new DOPerson("", ""),
                    null, new Date());
        }
        return curSession;
    }

    public String[] calculateCosts() throws DienstfahrtenException {
        DODestination[] arr = curSession.getStations().toArray(new DODestination[curSession.getStations().size()]);
        String[] stationArr = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            stationArr[i] = getLocationString(arr[i].location);
        }
        String result = calculator.caclculateVariableCostsForMultipleStations(stationArr);
        if (result.contains("||")) {
            String[] options = result.split("\\|\\|");
            return options;
        } else {
            if (result.toLowerCase().startsWith("error:")) {
                throw new DienstfahrtenException(result);
            } else {
                curSession.setVariableCosts(Double.parseDouble(result));
                return new String[]{curSession.getFinalCosts() + ""};
            }
        }
    }

    public String[] useAutoCompleter(String text) throws DienstfahrtenException {
        String result = calculator.autoCompleteAddress(text);
        if (result.contains("||")) {
            String[] options = result.split("\\|\\|");
            return options;
        } else {
            if (result.toLowerCase().startsWith("error:")) {
                throw new DienstfahrtenException(result);
            }
        }
        return new String[0];
    }

    public void addStation(DODestination station) {
        curSession.addStation(station);
    }

    public void addStation(DODestination station, int index) {
        curSession.addStation(station, index);
    }

    public void removeStation(int index) {
        if (index > 0 && index < curSession.getStations().size()) {
            curSession.removeStation(index);
        }
    }

    public void changePerson(DOPerson person) {
        curSession.setPerson(person);
    }

    public void changeTitle(String title) {
        curSession.setTitle(title);
    }

    private String getLocationString(DOLocation location) {
        StringBuilder sb = new StringBuilder();
        if (location.getStreet() != null
                && !location.getStreet().equals("")) {
            sb.append(location.getStreet());
            if (location.getStreetNumber() > -1) {
                sb.append(" " + location.getStreetNumber());
            }
        }
        if (location.getCity() != null
                && !location.getCity().equals("")) {
            if (!sb.toString().equals("")) {
                sb.append(", ");
            }
            sb.append(location.getCity());
        }
        return sb.toString();
    }
}
