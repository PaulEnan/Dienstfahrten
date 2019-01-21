package winfs.dienstreise.dienstfahrten;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Paul Enan
 */
public class CentralLogic {
    private ISaveLoadHandler saveLoadHandler;
    private Calculator calculator;
    List<DOSession> sessions;
    DOSession curSession;

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
            curSession = new DOSession();
        }

        return curSession;
    }

    public String[] calculateCosts() throws DienstfahrtenException {
        String[] stationArr = curSession.getStations().toArray(new String[curSession.getStations().size()]);
        String result = calculator.caclculateVariableCostsForMultipleStations(stationArr);
        if (result.contains("||")) {
            String[] options = result.split("||");
            return options;
        } else {
            if (result.toLowerCase().startsWith("error:")) {
                throw new DienstfahrtenException(result);
            } else {
                return new String[]{curSession.getFinalCosts() + ""};
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
}
