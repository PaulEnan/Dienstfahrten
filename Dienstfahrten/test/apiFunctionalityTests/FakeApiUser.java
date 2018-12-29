/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apiFunctionalityTests;

import apifunctionality.IApiUser;
import apifunctionality.Language;
import org.json.JSONObject;

/**
 *
 * @author Paul Enan
 */
public class FakeApiUser implements IApiUser {

    private final String resultDistance;
    private final String resultAdress;
    private final String resultAutoCompleter;
    
    public FakeApiUser(String resultDistance, String resultAdress, String resultAutoCompleter) {
        this.resultAdress = resultAdress;
        this.resultAutoCompleter = resultAutoCompleter;
        this.resultDistance = resultDistance;
    }
    
    @Override
    public JSONObject getDistance(String origin, String destination, Language language) {
        return new JSONObject(resultDistance);
    }

    @Override
    public JSONObject getExistingAddress(String name, Language language) {
        return new JSONObject(resultAdress);
    }

    @Override
    public JSONObject getAutoCompleter(String name, Language language) {
        return new JSONObject(resultAutoCompleter);
    }
    
}
