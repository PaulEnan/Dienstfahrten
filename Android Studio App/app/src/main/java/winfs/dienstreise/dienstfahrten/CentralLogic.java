package winfs.dienstreise.dienstfahrten;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author winf101441
 */
public class CentralLogic {
    private ISaveLoadHandler saveLoadHandler;
    private Calculator calculator;
    List<DOSession> sessions;
    DOSession curSession;

    public final static double variableCostRate = 0.30;

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

    public void SaveSession(DOSession session) throws DienstfahrtenException {
        if (!saveLoadHandler.Save(session)) {
            throw new DienstfahrtenException("Speichern nicht möglich");
        }
    }

    public DOSession LoadSession(int id) throws DienstfahrtenException {
        if (id != -1) {
            try {
                curSession = saveLoadHandler.Load(id);
            } catch (Exception ex) {
                throw new DienstfahrtenException(ex.getMessage());
            }
        } else {
            curSession = new DOSession();
        }
        return curSession;
    }

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

    public void changePerson(DOPerson person) {
        curSession.setPerson(person);
    }

    public void changeTitle(String title) {
        curSession.setTitle(title);
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
