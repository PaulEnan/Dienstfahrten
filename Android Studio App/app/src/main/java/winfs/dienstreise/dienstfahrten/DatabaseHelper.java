package winfs.dienstreise.dienstfahrten;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper implements ISaveLoadHandler {

    static SimpleDateFormat GERMANDATEFORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
    private static final String DBNAME = "voyage_calculation";

    private static final String PK = "_id";

    private static final String PERSONSTABLE = "persons";
    private static final String PRENAME = "prename";
    private static final String SURNAME = "surname";

    private static final String DESTINATIONTABLE = "destination";
    private static final String OCCASION = "occasion";
    private static final String SLEEPCOSTS = "sleep_costs";
    private static final String FOODCOSTS = "food_costs";
    private static final String TRIPEXTRACOSTS = "trip_extra_sosts";
    private static final String DESTLOCATION = "dest_location";

    private static final String SESSIONDESTTABLE = "session_dest";
    private static final String SESSIONID = "session_id";
    private static final String DESTINATIONID = "destination_id";

    private static final String SESSIONTABLE = "session";
    private static final String PERSONID = "person_id";
    private static final String STARTLOCATION = "start_location";
    private static final String STARTDATE = "start_date";
    private static final String TITLE = "title";
    private static final String DURATION = "duration";
    private static final String VARCOSTS = "var_costs";


    private static String queryForSession = String.format("SELECT s%1$s, %2$s, %3$s, %4$s, %5$s, " +
                    "%6$s, %7$s, %8$s, %9$s FROM %10$s " +
                    " INNER JOIN %11$s ON %12$s = p%1$s", PK, PRENAME, SURNAME, TITLE,
            STARTLOCATION, STARTDATE, DURATION, PERSONID, VARCOSTS, SESSIONTABLE,
            PERSONSTABLE, PERSONID);

    private static String queryForDestination = String.format("SELECT d%1$s, %2$s, %3$s, %4$s, " +
                    "%5$s, %6$s FROM %7$s " +
                    " INNER JOIN %8$s ON %9$s = d%1$s", PK, FOODCOSTS, SLEEPCOSTS,
            TRIPEXTRACOSTS, DESTLOCATION, OCCASION, SESSIONDESTTABLE,
            DESTINATIONTABLE, DESTINATIONID);
    SQLiteDatabase db = this.getReadableDatabase();

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTablePersons = "CREATE TABLE " + PERSONSTABLE
                + " ( p" + PK + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PRENAME + " TEXT, " + SURNAME + " TEXT)";

        String createTableDestination = "CREATE TABLE " + DESTINATIONTABLE + " ( d" + PK
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SLEEPCOSTS
                + " INTEGER, " + FOODCOSTS + " INTEGER, " + TRIPEXTRACOSTS
                + " INTEGER, " + DESTLOCATION + " TEXT, " + OCCASION + " TEXT)";

        String createTableSessionDest = "CREATE TABLE " + SESSIONDESTTABLE + " (" + SESSIONID
                + " INTEGER, " + DESTINATIONID + " INTEGER, FOREIGN KEY ("
                + DESTINATIONID + ") REFERENCES " + DESTINATIONTABLE
                + "(d" + PK + "), FOREIGN KEY (" + SESSIONID + ") REFERENCES " + SESSIONTABLE
                + "(s" + PK + "))";

        String createTableSession = "CREATE TABLE " + SESSIONTABLE + " (s" + PK
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PERSONID + " INTEGER, "
                + STARTLOCATION + " INTEGER, " + STARTDATE + " DATETIME, " + TITLE + " TEXT, "
                + DURATION + " INTEGER, " + VARCOSTS + " INTEGER, FOREIGN KEY (" + PERSONID + ") REFERENCES "
                + PERSONSTABLE + "(p" + PK + "))";

        db.execSQL(createTableDestination);
        db.execSQL(createTablePersons);
        db.execSQL(createTableSessionDest);
        db.execSQL(createTableSession);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + PERSONSTABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DESTINATIONTABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SESSIONDESTTABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SESSIONTABLE);

        onCreate(db);
    }

    @Override
    public void Save(DOSession session) throws SaveLoadException {
        SQLiteDatabase db = this.getWritableDatabase();
        int id;
        try {
            ContentValues cv = new ContentValues();
            cv.put(PRENAME, session.person.prename);
            cv.put(SURNAME, session.person.surname);
            id = (int) db.insert(PERSONSTABLE, null, cv);
            session.person.setID(id);
            cv.clear();

            cv.put(STARTLOCATION, session.startLocation);
            cv.put(PERSONID, session.person.id);
            cv.put(STARTDATE, session.startDate.toString());
            cv.put(TITLE, session.title);
            cv.put(DURATION, session.duration);
            id = (int) db.insert(SESSIONTABLE, null, cv);
            session.setId(id);
            cv.clear();

            for (DODestination dest : session.getStations()) {

                cv.put(FOODCOSTS, dest.foodCosts);
                cv.put(SLEEPCOSTS, dest.sleepCosts);
                cv.put(TRIPEXTRACOSTS, dest.tripExtraCosts);
                cv.put(DESTLOCATION, dest.location);
                cv.put(OCCASION, dest.occasion);
                id = (int) db.insert(DESTINATIONTABLE, null, cv);
                dest.setId(id);
                cv.clear();

                cv.put(SESSIONID, session.id);
                cv.put(DESTINATIONID, dest.id);
                db.insert(SESSIONDESTTABLE, null, cv);
            }


        } catch (Exception ex) {
            throw new SaveLoadException(ex.getMessage());

        }
    }

    @Override
    public DOSession Load(int id) throws SaveLoadException {
        try {
            String query = String.format("%1$s WHERE s%2$s = %3$s", queryForSession, PK, id);

            String destQuery = String.format("%1$s WHERE %2$s = %3$s", queryForDestination,
                    SESSIONID, id);
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(destQuery, null);

            List<DODestination> destinations = getDestiationsFromCursor(cursor);
            cursor.close();

            Cursor cursorForSession = db.rawQuery(query, null);
            cursorForSession.moveToFirst();
            DOSession session = getSession(id, destinations, cursorForSession);
            cursor.close();
            return session;
        } catch (Exception ex) {
            throw new SaveLoadException(ex.getMessage());
        }

    }

    public List<DOSession> getAllSessions() throws SaveLoadException {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            List<DOSession> listSessions = new ArrayList<>();
            List<DODestination> destinations;
            Cursor cursorForDestinations;
            int sessionId;
            String destQuery = String.format("%1$s WHERE %2$s = ?", queryForDestination,
                    SESSIONID);
            Cursor cursor = db.rawQuery(queryForSession, null);
            while (cursor.moveToNext()) {
                sessionId = cursor.getInt(cursor.getColumnIndex("s" + PK));
                cursorForDestinations = db.rawQuery(destQuery,
                        new String[]{Integer.toString(sessionId)});

                destinations = getDestiationsFromCursor(cursorForDestinations);
                cursorForDestinations.close();
                listSessions.add(getSession(sessionId, destinations, cursor));
            }
            cursor.close();
            return listSessions;
        } catch (Exception ex) {
            throw new SaveLoadException(ex.getMessage());
        } finally {
            db.close();
        }
    }

    List<DODestination> getDestiationsFromCursor(Cursor cursor) {
        List<DODestination> destinations = new LinkedList<>();
        while (cursor.moveToNext()) {
            destinations.add(
                    new DODestination(
                            cursor.getInt(cursor.getColumnIndex("d" + PK)),
                            cursor.getInt(cursor.getColumnIndex(SLEEPCOSTS)),
                            cursor.getInt(cursor.getColumnIndex(FOODCOSTS)),
                            cursor.getInt(cursor.getColumnIndex(TRIPEXTRACOSTS)),
                            cursor.getString(cursor.getColumnIndex(DESTLOCATION)),
                            cursor.getString(cursor.getColumnIndex(OCCASION))
                    )
            );
        }
        return destinations;
    }

    DOSession getSession(int id, List<DODestination> destinations, Cursor cursor) {
        Date start = null;
        try {
            start = dateFormat.parse(cursor.getString(cursor.getColumnIndex(STARTDATE)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new DOSession(id, destinations, cursor.getString(cursor.getColumnIndex(TITLE)),
                new DOPerson(cursor.getInt(cursor.getColumnIndex(PERSONID)),
                        cursor.getString(cursor.getColumnIndex(PRENAME)),
                        cursor.getString(cursor.getColumnIndex(SURNAME))),
                cursor.getString(cursor.getColumnIndex(STARTLOCATION)),
                start, cursor.getInt(cursor.getColumnIndex(DURATION)),
                cursor.getInt(cursor.getColumnIndex(VARCOSTS)));

    }
}


