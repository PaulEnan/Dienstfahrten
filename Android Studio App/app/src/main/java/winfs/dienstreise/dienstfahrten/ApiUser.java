package winfs.dienstreise.dienstfahrten;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Implementation of IApiUser, as used in our app
 * @author winf101441
 */
public class ApiUser implements IApiUser {

    /**
     * key, used to access google map api
     */
    private final String key = "AIzaSyBWJ2xJ8KM-oP4sPigyP6vE2htwpJX39_o";

    @Override
    public JSONObject getDistance(String origin, String destination) {
        String originAsQuery = makeStringQueryCompliant(origin);
        String destinationAsQuery = makeStringQueryCompliant(destination);
        String url = "distancematrix/json";
        HashMap<String, String> map = new HashMap<>();
        map.put("origins", originAsQuery);
        map.put("destinations", destinationAsQuery);
        map.put("language", "de");
        map.put("key", key);
        String queryString = "";
        try {
            queryString = setParameterString(map);
        } catch (UnsupportedEncodingException ex) {
            return null;
        }

        try {
            return new JSONObject(makeQuery(url, queryString));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public JSONObject getExistingAddress(String name) {

        String nameAsQuery = makeStringQueryCompliant(name);
        String url = "place/findplacefromtext/json";
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("input", nameAsQuery);
        map.put("inputtype", "textquery");
        map.put("fields", "formatted_address");
        map.put("language", "de");
        map.put("key", key);
        String queryString = "";
        try {
            queryString = setParameterString(map);
        } catch (UnsupportedEncodingException ex) {
            return null;
        }

        try {
            return new JSONObject(makeQuery(url, queryString));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject getAutoCompleter(String name) {
        String nameAsQuery = makeStringQueryCompliant(name);
        String url = "place/autocomplete/json";
        HashMap<String, String> map = new HashMap<>();
        map.put("input", nameAsQuery);
        map.put("language", "de");
        map.put("key", key);
        String queryString = "";
        try {
            queryString = setParameterString(map);
        } catch (UnsupportedEncodingException ex) {
            return null;
        }

        try {
            return new JSONObject(makeQuery(url, queryString));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * creates a query for the api service
     * @param url the service, as distancematrix or autocompleter
     * @param queryString the queries, as a String
     * @return A String that can be used to create a JSONObject
     */
    private String makeQuery(String url, String queryString) {
        if ("".equals(url)) {
            return "";
        }
        try {
            URL completeUrl;
            /*
      url of google maps api service
     */ /**
             * url of google maps api service
             */String googleUrl = "https://maps.googleapis.com/maps/api/";
            String urlString = googleUrl + url + (!"".equals(queryString) ? ("?" + queryString) : "");
            completeUrl = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) completeUrl.openConnection();
            con.setRequestMethod("GET");
            InputStream response = con.getInputStream();
            Scanner s = new Scanner(response).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } catch (IOException ex) {
            return "";
        }
    }

    /**
     * Makes sure the String actually fits the query by filtering out spaces
     * @param name
     * @return
     */
    private String makeStringQueryCompliant(String name) {
        return name.replace(" ", "+");
    }

    /**
     * creates a query string out of a map of parameternames and values
     * @param parameters the map
     * @return the string
     * @throws UnsupportedEncodingException might get thrown if the encoding doesn't work
     */
    private String setParameterString(Map<String, String> parameters)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;

    }

}
