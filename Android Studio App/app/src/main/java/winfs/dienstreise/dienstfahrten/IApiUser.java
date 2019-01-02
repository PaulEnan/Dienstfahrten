package winfs.dienstreise.dienstfahrten;

import org.json.JSONObject;

/**
 * @author Paul Enan
 */
public interface IApiUser {


    JSONObject getDistance(String origin, String destination);

    JSONObject getExistingAddress(String name);

    JSONObject getAutoCompleter(String name);
}
