/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apiFunctionalityTests;

import apifunctionality.IGui;
import apifunctionality.Language;
import apifunctionality.SessionData;

/**
 *
 * @author Paul Enan
 */
public class FakeGui implements IGui{

    @Override
    public void displaySession(SessionData session) {
    }

    @Override
    public void displayError(String message, Language language) {
    }

    @Override
    public void useAutoCompleter(String[] results, Language language) {
    }

    @Override
    public void showOtherResults(String[] results, Language language) {
    }

    @Override
    public void displayStations(String[] stations, Language language) {
    }

    @Override
    public void displayFinalCosts(String Costs, Language language) {
    }

    @Override
    public void displayFixCosts(String Costs, Language language) {
    }

    @Override
    public void displayVariableCosts(String Costs, Language language) {
    }

    @Override
    public void displayLanguage(Language language) {
    }
    
}
