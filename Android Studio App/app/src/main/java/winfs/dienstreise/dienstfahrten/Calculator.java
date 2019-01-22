package winfs.dienstreise.dienstfahrten;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The calculator class, that offers two services:
 * Calculating how much distance a journey between multiple stations takes and how much this costs
 * and autocompleting a place
 * @author winf101441
 */
public class Calculator {

    /**
     * the API interface that does all the communication with google maps
     */
    private final IApiUser apiUser;

    /**
     * creates a calculator with set apiUser
     * @param apiUser the apiUser
     */
    public Calculator(IApiUser apiUser) {
        this.apiUser = apiUser;
    }

    /**
     * calculates what a journey between multiple stations would cost by multiplying variable costs
     * with the total distance. Might return a String containing just one multiple addresses if one
     * of the stations was not identifyable.
     * @param stations the stations
     * @return the total amount, or proposed addresses
     * @throws DienstfahrtenException might get thrown if there was a problem communicating with
     * google or a stationname was faulty
     */
    public String caclculateVariableCostsForMultipleStations(String[] stations)
            throws DienstfahrtenException {
        String result = "0";
        for (int i = 0; i < stations.length - 1; i++) {
            double currentResult = Double.parseDouble(result);
            String nextStationResult = calculateVariableCosts(stations[i], stations[i + 1]);
            if (!nextStationResult.matches("[0-9]+(\\.[0-9]+)?")) {
                return nextStationResult;
            }
            result = (currentResult + Double.parseDouble(nextStationResult)) + "";
        }
        return Double.parseDouble(result) + "";
    }

    /**
     * auto completes an address by searching for all possible predictions google maps would get for
     * a given string. returns those predictions
     * @param address the string
     * @return the predictions
     * @throws DienstfahrtenException gets thrown if the string was not found or there was an error
     * communicating with google maps
     */
    public String autoCompleteAddress(String address) throws DienstfahrtenException {
        JSONObject addressObject = apiUser.getAutoCompleter(address);
        if (addressObject == null) {
            throw new DienstfahrtenException(Messages.JSONError());
        }
        try {
            if ("OK".equals(addressObject.getString("status").toUpperCase())) {
                JSONArray array = null;

                array = addressObject.getJSONArray("predictions");
                if (array.length() == 0) {
                    throw new DienstfahrtenException(Messages.NotFound(address));
                }
                String result = "";
                for (int i = 0; i < array.length(); i++) {
                    JSONObject prediction = array.getJSONObject(i);
                    result = result + (!result.isEmpty() ? "||" : "") + prediction.getString("description");
                }
                return result;
            }
        } catch (JSONException e) {
            throw new DienstfahrtenException(Messages.JSONError());
        }
        throw new DienstfahrtenException(Messages.NotFound(address));
    }

    /**
     * calculates the cost of a journey between two places.
     * @param origin the origin place
     * @param destination the destination place
     * @return the total costs
     * @throws DienstfahrtenException might get thrown if there was a problem communicating with
     * google or a stationname was faulty
     */
    private String calculateVariableCosts(String origin, String destination) throws DienstfahrtenException {
        //only works for one origin to one destination.
        if (!origin.contains("|") && !destination.contains("|")) {
            JSONObject originObject = apiUser.getExistingAddress(origin);
            JSONObject destinationObject = apiUser.getExistingAddress(destination);

            if (originObject == null ||destinationObject == null) {
                throw new DienstfahrtenException(Messages.JSONError());
            }
            try {
                if (!originObject.toString().isEmpty()
                        && "OK".equals(originObject.getString("status").toUpperCase())) {
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

                        double result = distance * CentralLogic.variableCostRate;
                        return result + "";
                    }
                    return autoCompleteAddress(destination);
                }
            } catch (JSONException e) {
                throw new DienstfahrtenException(Messages.JSONError());
            }
            return autoCompleteAddress(origin);
        }
        throw new DienstfahrtenException(Messages.NoAdressesWith('|'));
    }
}
