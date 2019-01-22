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

    public Calculator(IApiUser apiUser) {
        this.apiUser = apiUser;
    }
    public String caclculateCostsForMultipleStations(String[] stations, double fix, double var, Language language) {
        String result = "0";
        for (int i = 0; i < stations.length - 1; i++) {
            double currentResult = Double.parseDouble(result);
            String nextStationResult = calculateVariableCosts(stations[i], stations[i + 1], fix, var, language);
            if (!nextStationResult.matches("[0-9]+(\\.[0-9]+)?")) {
                return nextStationResult;
            }
            result = (currentResult + Double.parseDouble(nextStationResult)) + "";
        }
        return (Double.parseDouble(result) + fix) + "";
    }

    public String calculateVariableCosts(String origin, String destination, double fix, double var, Language language) {
        //only works for one origin to one destination. 
        if (!origin.contains("|") && !destination.contains("|")) {
            JSONObject originObject = apiUser.getExistingAddress(origin, language);
            JSONObject destinationObject = apiUser.getExistingAddress(destination, language);

            if (!"".equals(originObject.toString())
                    && "OK".equals(originObject.getString("status").toUpperCase())) { //TODO differenziertere fehlererkennung und meldung
                if (!"".equals(destinationObject.toString())
                        && "OK".equals(destinationObject.getString("status").toUpperCase())) {
                    String originAddress = originObject.getJSONArray("candidates")
                            .getJSONObject(0).getString("formatted_address");
                    String destinationAddress = destinationObject.getJSONArray("candidates")
                            .getJSONObject(0).getString("formatted_address");

                    JSONObject distanceObject = apiUser.getDistance(originAddress, destinationAddress, language);

                    int distance = 0;
                    if (!distanceObject.isEmpty()
                            && "OK".equals(distanceObject.getString("status").toUpperCase())) {
                        JSONArray rows = distanceObject.getJSONArray("rows");
                        JSONObject distanceElement = rows.getJSONObject(0).getJSONArray("elements").getJSONObject(0);
                        if ("OK".equals(distanceElement.getString("status").toUpperCase())) {
                            String distanceString = distanceElement.getJSONObject("distance").getString("text");
                            Double amount = Double.parseDouble(distanceString.substring(0, distanceString.indexOf(' ')));
                            distance += amount;
                        }
                    }

                    double result = distance * var;
                    return result + "";
                }
                return autoCompleteAddress(destination, language);
            }
            return autoCompleteAddress(origin, language);
        }
        return Messages.NoAdressesWith('|', language);
    }

    public String autoCompleteAddress(String address, Language language) {
        JSONObject addressObject = apiUser.getAutoCompleter(address, language);

        if (!"".equals(addressObject.toString())
                && "OK".equals(addressObject.getString("status").toUpperCase())) { //TODO differenziertere fehlererkennung und meldung
            JSONArray array = addressObject.getJSONArray("predictions");
            String result = "";
            for (int i = 0; i < array.length(); i++) {
                JSONObject prediction = array.getJSONObject(i);
                result = result + (!"".equals(result) ? "||" : "") + prediction.getString("description");
            }
            return result;
        }
        return Messages.NotFound(address, Language.English);
    }
}
