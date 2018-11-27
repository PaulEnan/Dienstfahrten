/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apifunctionality;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Paul Enan
 */
public class Calculator {

    private final IApiUser apiUser;
    private int variable;
    private int fix;

    public Calculator(IApiUser apiUser) {
        this(apiUser, 0, 0);
    }

    public Calculator(IApiUser apiUser, int variable, int fix) {
        this.apiUser = apiUser;
        this.variable = variable;
        this.fix = fix;
    }

    public int getVariable() {
        return variable;
    }

    public int getFix() {
        return fix;
    }

    public void changeVariable(int variable) {
        this.variable = variable;
    }

    public void changeFix(int fix) {
        this.fix = fix;
    }

    public String calculateDistance(String origin, String destination) {
        JSONObject originObject = apiUser.getExistingAddress(origin);
        JSONObject destinationObject = apiUser.getExistingAddress(destination);

        if (!"".equals(originObject.toString())
                && "OK".equals(originObject.getString("status").toUpperCase())) { //TODO differenziertere fehlererkennung und meldung
            if (!"".equals(destinationObject.toString())
                    && "OK".equals(destinationObject.getString("status").toUpperCase())) {
                String originAddress = originObject.getJSONArray("candidates")
                        .getJSONObject(0).getString("formatted_address");
                String destinationAddress = destinationObject.getJSONArray("candidates")
                        .getJSONObject(0).getString("formatted_address");

                JSONObject distanceObject = apiUser.getDistance(originAddress, destinationAddress);

                if (!"".equals(distanceObject)
                        && "OK".equals(distanceObject.getString("status").toUpperCase())) {
                    JSONArray elements = distanceObject.getJSONArray("rows");
                    int distance = 0;
                    
                    for (int i = 0; i < elements.length(); i++) {
                        JSONObject distanceElement = elements.getJSONObject(i);
                        if ("OK".equals(distanceElement.getString("status").toUpperCase())) {
                            String distanceString = distanceElement.getJSONObject("distance").getString("text");
                            Double amount = Double.parseDouble(distanceString.substring(0, distanceString.indexOf(' ')));
                            distance += amount;
                        }
                    }
                    double result = distance * variable + fix;
                    return result + "";
                }

            }
            return autoCompleteAddress(destination);
        }
        return autoCompleteAddress(origin);
    }

    public String autoCompleteAddress(String address) {
        JSONObject addressObject = apiUser.getAutoCompleter(address);

        if (!"".equals(addressObject.toString())
                && "OK".equals(addressObject.getString("status").toUpperCase())) { //TODO differenziertere fehlererkennung und meldung
            JSONArray array = addressObject.getJSONArray("predictions");
            String result = "";
            for (int i = 0; i < array.length(); i++) {
                JSONObject prediction = array.getJSONObject(i);
                result = result + (!"".equals(result) ? "|" : "") + prediction.getString("description");
            }
            return result;
        }
        return "Error: NO FUCKING RESULTS YOU COCK!"; // wird noch geändert. derzeit gilt: Wenn was scheif gelaufen ist, ist das erste Wort Error:
    }
}
