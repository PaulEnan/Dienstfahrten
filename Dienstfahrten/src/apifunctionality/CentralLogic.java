/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apifunctionality;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Paul Enan
 */
public class CentralLogic {

    private IGui gui;
    private ISaveLoadHandler saveLoadHandler;
    private Calculator calculator;
    private SessionData session;

    public CentralLogic(IGui gui, IApiUser apiUser, ISaveLoadHandler saveLoadHandler) {
        this(gui, Language.English, apiUser, saveLoadHandler, new LinkedList<String>(), 0, 0, 0);
    }

    public CentralLogic(IGui gui, Language language, IApiUser apiUser, ISaveLoadHandler saveLoadHandler,
            LinkedList<String> stations, double fixCosts, double variableCosts, double finalCosts) {
        this(gui, apiUser, saveLoadHandler, new SessionData(language, stations, fixCosts, variableCosts, finalCosts));
    }

    public CentralLogic(IGui gui, IApiUser apiUser, ISaveLoadHandler saveLoadHandler, SessionData session) {
        this.gui = gui;
        this.saveLoadHandler = saveLoadHandler;
        this.calculator = new Calculator(apiUser);
        this.session = session;
    }
    
    public void SaveSession(String path) {
        try {
            saveLoadHandler.Save(path, session);
        } catch (SaveLoadException ex) {
            gui.displayError(ex.getMessage(), session.getLanguage());
        }
    }
    
    public void LoadSession(String path) {
        try {
            this.session = saveLoadHandler.Load(path);
            gui.displaySession(session);
        } catch (SaveLoadException ex) {
            gui.displayError(ex.getMessage(), session.getLanguage());
        }
    }

    public String calculateCosts() {
        String[] stationArr = session.getStations().toArray(new String[session.getStations().size()]);
        String result = calculator.caclculateCostsForMultipleStations(stationArr, session.getFixCosts(), session.getVariableCosts(), session.getLanguage());
        if (result.contains("||")) {
            String[] options = result.split("||");
            gui.showOtherResults(options, session.getLanguage());
        } else {
            if (result.toLowerCase().startsWith("error:")) {
                gui.displayError(result, session.getLanguage());
            } else {
                this.session.setFinalCosts(Double.parseDouble(result));
                gui.displayFinalCosts(result, Language.English);
            }
        }
        return result;
    }

    public String useAutoCompleter(String text) {
        String result = calculator.autoCompleteAddress(text, session.getLanguage());
        if (result.contains("||")) {
            String[] options = result.split("||");
            gui.useAutoCompleter(options, session.getLanguage());
        } else {
            if (result.toLowerCase().startsWith("error:")) {
                gui.displayError(result, session.getLanguage());
            }
        }
        return result;
    }

    public void addStation(String station) {
        session.addStation(station);
        String[] stationArr = session.getStations().toArray(new String[session.getStations().size()]);
        gui.displayStations(stationArr, session.getLanguage());
    }

    public void addStation(String station, int index) {
        session.addStation(station, index);
        String[] stationArr = session.getStations().toArray(new String[session.getStations().size()]);
        gui.displayStations(stationArr, session.getLanguage());
    }

    public void removeStation(int index) {
        if (index > 0 && index < session.getStations().size()) {
            session.removeStation(index);
            String[] stationArr = session.getStations().toArray(new String[session.getStations().size()]);
            gui.displayStations(stationArr, session.getLanguage());
        }
    }

    public void changeLanguage(Language language) {
        session.setLanguage(language);
        gui.displayLanguage(session.getLanguage());
    }

    public void changeFixCosts(String costs) {
        session.setFixCosts(Double.parseDouble(costs));
        gui.displayFixCosts(session.getFixCosts() + "", Language.English);
    }

    public void changeVariableCosts(String costs) {
        session.setVariableCosts(Double.parseDouble(costs));
        gui.displayVariableCosts(session.getVariableCosts() + "", Language.English);
    }
}
