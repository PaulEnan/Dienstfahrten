package winfs.dienstreise.dienstfahrten;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Paul Enan
 */
public class FakeApiUser implements IApiUser {

    private final String[] resultDistance;
    private final String[] resultAdress;
    private final String[] resultAutoCompleter;
    int indexD = 0;
    int indexA = 0;
    int indexAC = 0;
    
    public FakeApiUser(String[] resultDistance, String[] resultAdress, String[] resultAutoCompleter) {
        this.resultAdress = resultAdress;
        this.resultAutoCompleter = resultAutoCompleter;
        this.resultDistance = resultDistance;
    }
    
    @Override
    public JSONObject getDistance(String origin, String destination) {
        try {
            JSONObject result =  new JSONObject(resultDistance[indexD]);
            indexD++;
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject getExistingAddress(String name) {
        try {
            JSONObject result =  new JSONObject(resultAdress[indexA]);
            indexA++;
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject getAutoCompleter(String name) {
        try {
            JSONObject result =  new JSONObject(resultAutoCompleter[indexAC]);
            indexAC++;
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
