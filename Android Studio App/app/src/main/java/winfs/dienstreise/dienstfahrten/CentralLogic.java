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
        this(apiUser, saveLoadHandler, new LinkedList<String>(), 0, 0);
    }

    public CentralLogic(IApiUser apiUser, ISaveLoadHandler saveLoadHandler,
                        LinkedList<String> stations, double fixCosts, double variableCosts) {
        this(apiUser, saveLoadHandler, new SessionData(stations, fixCosts, variableCosts));
    }

    public CentralLogic(IApiUser apiUser, ISaveLoadHandler saveLoadHandler, SessionData session) {
        this.saveLoadHandler = saveLoadHandler;
        this.calculator = new Calculator(apiUser);
        this.session = session;
    }

    public void SaveSession(String path) throws DienstfahrtenException {
        try {
            saveLoadHandler.Save(path, session);
        } catch (SaveLoadException ex) {
            throw new DienstfahrtenException(ex.getMessage());
        }
    }

    public void LoadSession(String path) throws DienstfahrtenException {
        try {
            this.session = saveLoadHandler.Load(path);
            //gui.displaySession(session);
        } catch (SaveLoadException ex) {
            throw new DienstfahrtenException(ex.getMessage());
        }
    }

    public String calculateCosts() throws DienstfahrtenException {
        String[] stationArr = session.getStations().toArray(new String[session.getStations().size()]);
        String result = calculator.caclculateVariableCostsForMultipleStations(stationArr, session.getVariableCosts());
        if (result.contains("||")) {
            String[] options = result.split("||");
            //gui.showOtherResults(options);
        } else {
            if (result.toLowerCase().startsWith("error:")) {
                throw new DienstfahrtenException(result);
            } else {
                this.session.setVariableCosts(Double.parseDouble(result));
            }
        }
        return result;
    }

    public String useAutoCompleter(String text) throws DienstfahrtenException {
        String result = calculator.autoCompleteAddress(text);
        if (result.contains("||")) {
            String[] options = result.split("||");
            //gui.useAutoCompleter(options);
        } else {
            if (result.toLowerCase().startsWith("error:")) {
                throw new DienstfahrtenException(result);
            }
        }
        return result;
    }

    public void addStation(String station) {
        session.addStation(station);
        //String[] stationArr = session.getStations().toArray(new String[session.getStations().size()]);
        //gui.displayStations(stationArr);
    }

    public void addStation(String station, int index) {
        session.addStation(station, index);
        //String[] stationArr = session.getStations().toArray(new String[session.getStations().size()]);
        //gui.displayStations(stationArr);
    }

    public void removeStation(int index) {
        if (index > 0 && index < session.getStations().size()) {
            session.removeStation(index);
            //String[] stationArr = session.getStations().toArray(new String[session.getStations().size()]);
            //gui.displayStations(stationArr);
        }
    }

    public void changeFixCosts(String costs) {
        session.setFixCosts(Double.parseDouble(costs));
        //gui.displayFixCosts(session.getFixCosts() + "");
    }

    public void changeVariableCosts(String costs) {
        session.setVariableCosts(Double.parseDouble(costs));
        //gui.displayVariableCosts(session.getVariableCosts() + "");
    }
}
