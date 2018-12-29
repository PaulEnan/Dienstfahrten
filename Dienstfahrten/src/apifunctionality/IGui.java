/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apifunctionality;

/**
 *
 * @author Paul Enan
 */
public interface IGui {
    public void displaySession(SessionData session);
    public void displayError(String message, Language language);
    public void useAutoCompleter(String[] results, Language language);
    public void showOtherResults(String[] results, Language language);
    public void displayStations(String[] stations, Language language);
    public void displayFinalCosts(String Costs, Language language);
    public void displayFixCosts(String Costs, Language language);
    public void displayVariableCosts(String Costs, Language language);
    public void displayLanguage(Language language);
}
