package winfs.dienstreise.dienstfahrten;

import org.json.JSONException;
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
    public JSONObject getDistance(String origin, String destination) {
        try {
            return new JSONObject(resultDistance);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject getExistingAddress(String name) {
        try {
            return new JSONObject(resultAdress);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject getAutoCompleter(String name) {
        try {
            return new JSONObject(resultAutoCompleter);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
