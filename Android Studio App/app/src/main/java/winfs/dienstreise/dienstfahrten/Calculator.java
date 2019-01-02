package winfs.dienstreise.dienstfahrten;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Paul Enan
 */
public class Calculator {

    private final IApiUser apiUser;


    public Calculator(IApiUser apiUser) {
        this.apiUser = apiUser;
    }

    public String caclculateVariableCostsForMultipleStations(String[] stations, double var)
            throws DienstfahrtenException {
        String result = "0";
        for (int i = 0; i < stations.length - 1; i++) {
            double currentResult = Double.parseDouble(result);
            String nextStationResult = calculateVariableCosts(stations[i], stations[i + 1], var);
            if (!nextStationResult.matches("[0-9]+(\\.[0-9]+)?")) {
                return nextStationResult;
            }
            result = (currentResult + Double.parseDouble(nextStationResult)) + "";
        }
        return Double.parseDouble(result) + "";
    }

    public String calculateVariableCosts(String origin, String destination, double var) throws DienstfahrtenException {
        //only works for one origin to one destination. 
        if (!origin.contains("|") && !destination.contains("|")) {
            JSONObject originObject = apiUser.getExistingAddress(origin);
            JSONObject destinationObject = apiUser.getExistingAddress(destination);

            try {
                if (originObject != null &&  !originObject.toString().isEmpty()
                        && "OK".equals(originObject.getString("status").toUpperCase())) { //TODO differenziertere fehlererkennung und meldung
                    if (!"".equals(destinationObject.toString())
                            && "OK".equals(destinationObject.getString("status").toUpperCase())) {
                        String originAddress = originObject.getJSONArray("candidates")
                                .getJSONObject(0).getString("formatted_address");
                        String destinationAddress = destinationObject.getJSONArray("candidates")
                                .getJSONObject(0).getString("formatted_address");

                        JSONObject distanceObject = apiUser.getDistance(originAddress, destinationAddress);

                        int distance = 0;
                        if (distanceObject != null
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
                    return autoCompleteAddress(destination);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return autoCompleteAddress(origin);
        }
        throw new DienstfahrtenException(Messages.NoAdressesWith('|'));
    }

    public String autoCompleteAddress(String address) throws DienstfahrtenException {
        JSONObject addressObject = apiUser.getAutoCompleter(address);
        try {
            if (!"".equals(addressObject.toString())
                    && "OK".equals(addressObject.getString("status").toUpperCase())) { //TODO differenziertere fehlererkennung und meldung
                JSONArray array = null;

                array = addressObject.getJSONArray("predictions");

                String result = "";
                for (int i = 0; i < array.length(); i++) {
                    JSONObject prediction = array.getJSONObject(i);
                    result = result + (!result.isEmpty() ? "||" : "") + prediction.getString("description");
                }
                return result;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        throw new DienstfahrtenException(Messages.NotFound(address));
    }
}
