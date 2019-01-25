package winfs.dienstreise.dienstfahrten;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * The central logic
 * @author winf101441
 */
public class CentralLogic {
    /**
     * the save and load handler
     */
    private ISaveLoadHandler saveLoadHandler;
    /**
     * the calculator
     */
    private Calculator calculator;
    /**
     * all stored sessions
     */
    List<DOSession> sessions;
    /**
     * the current session
     */
    DOSession curSession;

    /**
     * the cost rate euro per kilometer
     */
    public final static double variableCostRate = 0.30;

    /**
     * creates a central logic with set calculator and saveLoadHandler
     * @param apiUser the apiuser for the calculator
     * @param saveLoadHandler the save and load handler
     */
    public CentralLogic(IApiUser apiUser, ISaveLoadHandler saveLoadHandler) {
        this.saveLoadHandler = saveLoadHandler;
        this.calculator = new Calculator(apiUser);
        try {
            sessions = saveLoadHandler.getAllSessions();
        } catch (Exception ex) {
        }
        if (sessions == null) {
            sessions = new LinkedList<>();
        }
        if (sessions.size() >= 1) {
            curSession = sessions.get(0);
        }
    }

    /**
     * saves a session
     * @param session the session
     * @throws DienstfahrtenException gets thrown if something goes wrong
     */
    public void SaveSession(DOSession session) throws DienstfahrtenException {
        if (!saveLoadHandler.Save(session)) {
            throw new DienstfahrtenException("Speichern nicht möglich");
        }
    }

    /**
     * loades a given session
     * @param id the session's id
     * @return the session
     * @throws DienstfahrtenException gets thrown if something goes wrong
     */
    public DOSession LoadSession(int id) throws DienstfahrtenException {
        if (id != -1) {
            try {
                curSession = saveLoadHandler.Load(id);
            } catch (Exception ex) {
                throw new DienstfahrtenException(ex.getMessage());
            }
        } else {
            curSession = new DOSession();
            curSession.addStation(new DODestination());
        }
        return curSession;
    }

    /**
     * calculates the costs for the current session
     * @return an array. Either only contains one cell with the result, or multiple cells with
     * recommendations if one of the stations of the session was not identifiable
     * @throws DienstfahrtenException gets thrown if one of the stations was not found at all or
     * the connection did not work
     */
    public String[] calculateCosts() throws DienstfahrtenException {
        DODestination[] arr = curSession.getStations().toArray(new DODestination[curSession.getStations().size()]);
        String[] stationArr = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            stationArr[i] = arr[i].location;
        }
        String result = calculator.caclculateVariableCostsForMultipleStations(stationArr);
        if (result.contains("||")) {
            String[] options = result.split("\\|\\|");
            return options;
        } else {
            if (result.toLowerCase().startsWith("error:")) {
                throw new DienstfahrtenException(result);
            } else {
                curSession.setVariableCosts(Double.parseDouble(result));
                return new String[]{curSession.getFinalCosts() + ""};
            }
        }
    }

    /**
     * uses the autocompleter, returning the propositions google maps has to offer for a given string
     * @param text the string
     * @return the array of propositions
     * @throws DienstfahrtenException gets thrown if nothing was found for the string or the connection
     * to google maps didn't work
     */
    public String[] useAutoCompleter(String text) throws DienstfahrtenException {
        String result = calculator.autoCompleteAddress(text);
        if (result.contains("||")) {
            String[] options = result.split("\\|\\|");
            return options;
        } else {
            if (result.toLowerCase().startsWith("error:")) {
                throw new DienstfahrtenException(result);
            }
        }
        return new String[0];
    }

    public void addStation(DODestination station) {
        curSession.addStation(station);
    }

    public void addStation(DODestination station, int index) {
        curSession.addStation(station, index);
    }

    public void removeStation(int index) {
        if (index > 0 && index < curSession.getStations().size()) {
            curSession.removeStation(index);
        }
    }

    public void changeDate(String date) {
        if (curSession != null) {
            String string = "date";
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
            try {
                Date changedDate = format.parse(string);
                curSession.startDate = changedDate;
            } catch (ParseException e) {
            }
        }

    }

    public void changeDuration(String duration) {
        if (curSession != null) {
            curSession.duration = Integer.parseInt(duration);
        }
    }

    public void changePreName(String preName) {
        if (curSession != null) {
            if (curSession.person == null) {
                curSession.person = new DOPerson("", "");
            }
            curSession.person.prename = preName;
        }
    }

    public void changeSurName(String surName) {
        if (curSession != null) {
            if (curSession.person == null) {
                curSession.person = new DOPerson("", "");
            }
        }
        curSession.person.surname = surName;
    }

    public void changeDestination(int index, double sleepCosts, double foodCosts,
                               double tripExtraCosts, String location, String occasion) {
        List<DODestination> dests = curSession.getStations();
        if (dests != null && index >= 0) {
            if (dests.size() == 0 && index == 0) {
                dests.add(new DODestination(0, 0, 0,
                "", ""));
            }
            if (dests.size() > index) {

            }
            dests.get(index).sleepCosts = sleepCosts;
            dests.get(index).foodCosts = foodCosts;
            dests.get(index).tripExtraCosts = tripExtraCosts;
            dests.get(index).location = location;
            dests.get(index).occasion = occasion;
        }
    }

    public void changeLocation(int index, String location) {
        List<DODestination> dests = curSession != null ? curSession.getStations() : null;
        if (dests != null && index >= 0) {
            if (dests.size() == 0 && index == 0) {
                dests.add(new DODestination(0, 0, 0,
                        "", ""));
            }
            if (dests.size() > index) {
                dests.get(index).location = location;
            }
        }
    }

    public void changeTitle(String title) {
        if (curSession != null) {
            curSession.title = title;
        }
    }

    public static void make(Context ctx, String text, Response.Listener<List<String>>
            listener, Response.ErrorListener errorListener) {
        try {
            listener.onResponse(Arrays.asList(Overview.LOGIC.useAutoCompleter(text)));
        } catch (DienstfahrtenException e) {
            List<String> list = new LinkedList<>();
            list.add(e.getMessage());
            listener.onResponse(list);
        }
    }
}
