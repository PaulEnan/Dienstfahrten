/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apifunctionality;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.json.JSONObject;

/**
 *
 * @author Paul Enan
 */
public class ApiUser implements IApiUser {

    private final String key = "AIzaSyBWJ2xJ8KM-oP4sPigyP6vE2htwpJX39_o";
    private final String googleUrl = "https://maps.googleapis.com/maps/api/";

    @Override
    public JSONObject getDistance(String origin, String destination, Language language) {
        String originAsQuery = makeStringQueryCompliant(origin);
        String destinationAsQuery = makeStringQueryCompliant(destination);
        String url = "distancematrix/json";
        HashMap map = new HashMap();
        map.put("origins", originAsQuery);
        map.put("destinations", destinationAsQuery);
        map.put("language", language.asString);
        map.put("key", key);
        String queryString = "";
        try {
            queryString = setParameterString(map);
        } catch (UnsupportedEncodingException ex) {
            return new JSONObject("");
        }

        return new JSONObject(makeQuery(url, queryString));
    }

    @Override
    public JSONObject getExistingAddress(String name, Language language) {

        String nameAsQuery = makeStringQueryCompliant(name);
        String url = "place/findplacefromtext/json";
        HashMap map = new HashMap();
        map.put("input", nameAsQuery);
        map.put("inputtype", "textquery");
        map.put("fields", "formatted_address");
        map.put("language", language.asString);
        map.put("key", key);
        String queryString = "";
        try {
            queryString = setParameterString(map);
        } catch (UnsupportedEncodingException ex) {
            return new JSONObject("");
        }

        return new JSONObject(makeQuery(url, queryString));
    }

    @Override
    public JSONObject getAutoCompleter(String name, Language language) {
        String nameAsQuery = makeStringQueryCompliant(name);
        String url = "place/autocomplete/json";
        HashMap map = new HashMap();
        map.put("input", nameAsQuery);
        map.put("language", language.asString);
        map.put("key", key);
        String queryString = "";
        try {
            queryString = setParameterString(map);
        } catch (UnsupportedEncodingException ex) {
            return new JSONObject("");
        }

        return new JSONObject(makeQuery(url, queryString));
    }

    private String makeQuery(String url, String queryString) {
        if("".equals(url)) {
            return "";
        }
        try {
            URL completeUrl;
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

    private String makeStringQueryCompliant(String name) {
        return name.replace(" ", "+"); //muss evtl noch ausgebaut werden. SOnderzeichen?
    }

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
