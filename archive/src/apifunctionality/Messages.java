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
public class Messages {
    public static String NotFound(String place, Language language) {
        if (language == Language.German) {
            return "ERROR: " + place + " konnte nicht gefunden werden.";
        }
        if (language == Language.English) {
            return "ERROR: " + place + " not found.";
        }
        return "";
    }
    
    public static String DidYouMean(Language language) {
        if (language == Language.German) {
            return "Meinten Sie";
        }
        if (language == Language.English) {
            return "Did you mean";
        }
        return "";
    }
    
    public static String NoAdressesWith(Character symbol, Language language) {
        if (language == Language.German) {
            return "ERROR: Adressen mit '" + symbol + "' werden nicht akzeptiert.";
        }
        if (language == Language.English) {
            return "ERROR: Addresses containing '" + symbol + "' are not accepted.";
        }
        return "";
    }
}
