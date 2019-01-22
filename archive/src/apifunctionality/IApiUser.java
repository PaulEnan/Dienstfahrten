/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apifunctionality;

import org.json.JSONObject;

/**
 *
 * @author Paul Enan
 */
public interface IApiUser {
    
    
    public JSONObject getDistance(String origin, String destination, Language language);
    
    public JSONObject getExistingAddress(String name, Language language);
    
    public JSONObject getAutoCompleter(String name, Language language);
}
