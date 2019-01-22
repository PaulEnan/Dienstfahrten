package winfs.dienstreise.dienstfahrten;

import org.json.JSONObject;

/**
 * An interface for communication with the api
 * @author winf101441
 */
public interface IApiUser {
    /**
     * gets the distance between two places
     * @param origin place one
     * @param destination place two
     * @return the distance in an jsonObject
     */
    JSONObject getDistance(String origin, String destination);

    /**
     * gets the address of a given name
     * @param name the name
     * @return address
     */
    JSONObject getExistingAddress(String name);


    JSONObject getAutoCompleter(String name);
}
