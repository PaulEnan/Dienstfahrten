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

public class DatabaseHelper extends SQLiteOpenHelper implements ISaveLoadHandler {

    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
    private static final String DBNAME = "voyage_calculation";

    private static final String PK = "_id";

    private static final String PERSONSTABLE = "persons"; //
    private static final String PRENAME = "prename"; //
    private static final String SURNAME = "surname"; //


    private static final String LOCATIONTABLE = "location"; //
    private static final String STREET = "street";
    private static final String HOUSENO = "house_no";
    private static final String PLACE = "place";
    private static final String POSTALCODE = "postalcode";

    private static final String DESTINATIONTABLE = "destination"; //
    private static final String SLEEPCOSTS = "sleep_costs"; //
    private static final String FOODCOSTS = "food_costs"; //
    private static final String TRIPEXTRACOSTS = "trip_extra_sosts"; //
    private static final String DESTLOCATIONID = "location_id"; //
    private static final String ARRIVALDATE = "arrival_date"; //
    private static final String DEPARTUREDATE = "departure_date"; //

    private static final String SESSIONDESTTABLE = "session_dest";
    private static final String SESSIONID = "session_id";
    private static final String DESTINATIONID = "destination_id";

    private static final String SESSIONTABLE = "session"; //
    private static final String PERSONID = "person_id"; //
    private static final String STARTLOCATION = "start_location_id"; //
    private static final String STARTDATE = "start_date"; //
    private static final String TITLE = "title"; //


    private static String queryForSession = String.format("SELECT s%1$s, %2$s, %3$s, %4$s, %5$s, %6$s, %7$s, " +
                    "%8$s, %9$s, %10$s, p%1$s, l%1$s" +
                    " FROM %11$s " +
                    " INNER JOIN %12$s ON %13$s = p%1$s" +
                    " INNER JOIN %14$s ON %15$s = l%1$s", PK, PRENAME, SURNAME, TITLE,
            STARTLOCATION, POSTALCODE, PLACE, STREET, HOUSENO, STARTDATE, SESSIONTABLE,
            PERSONSTABLE, PERSONID, LOCATIONTABLE, STARTLOCATION);

    private static String queryForDestination = String.format("SELECT d%1$s, %2$s, %3$s, %4$s, %5$s, %6$s, " +
                    "%7$s, %8$s, %9$s, %10$s, %11$s" +
                    " FROM %12$s " +
                    " INNER JOIN %13$s ON %14$s = d%1$s" +
                    " INNER JOIN %15$s ON %16$s = l%1$s", PK, ARRIVALDATE, DEPARTUREDATE, FOODCOSTS, SLEEPCOSTS,
            TRIPEXTRACOSTS, POSTALCODE, PLACE, STREET, HOUSENO, DESTLOCATIONID, SESSIONDESTTABLE,
            DESTINATIONTABLE, DESTINATIONID, LOCATIONTABLE, DESTLOCATIONID);
    SQLiteDatabase db = this.getReadableDatabase();

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTablePersons = "CREATE TABLE " + PERSONSTABLE + " ( p" + PK + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PRENAME + " TEXT, " + SURNAME + " TEXT)";

        String createTableDestination = "CREATE TABLE " + DESTINATIONTABLE + " ( d" + PK + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SLEEPCOSTS
                + " INTEGER, " + FOODCOSTS + " INTEGER, " + TRIPEXTRACOSTS + " INTEGER, " + DESTLOCATIONID + " INTEGER," + ARRIVALDATE + " datetime, "
                + DEPARTUREDATE + " datetime, FOREIGN KEY (" + DESTLOCATIONID + ") REFERENCES " + LOCATIONTABLE + "(a" + PK + "))";

        String createTableAdresses = "CREATE TABLE " + LOCATIONTABLE + " ( l" + PK + " INTEGER PRIMARY KEY AUTOINCREMENT, " + POSTALCODE + " TEXT, "
                + PLACE + " TEXT, " + STREET + " TEXT, " + HOUSENO + " INTEGER)";

        String createTableSessionDest = "CREATE TABLE " + SESSIONDESTTABLE + " (" + SESSIONID + " INTEGER, " + DESTINATIONID + " INTEGER, FOREIGN KEY ("
                + DESTINATIONID + ") REFERENCES " + DESTINATIONTABLE + "(d" + PK + "), FOREIGN KEY (" + SESSIONID + ") REFERENCES " + SESSIONTABLE
                + "(s" + PK + "))";

        String createTableSession = "CREATE TABLE " + SESSIONTABLE + " (s" + PK + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PERSONID + " INTEGER, "
                + STARTLOCATION + " INTEGER, " + STARTDATE + " DATETIME, " + TITLE + " TEXT, FOREIGN KEY ("
                + PERSONID + ") REFERENCES " + PERSONSTABLE + "(p" + PK + "), FOREIGN KEY (" + STARTLOCATION + ") REFERENCES "
                + LOCATIONTABLE + "(a" + PK + "))";


        db.execSQL(createTableAdresses);
        db.execSQL(createTableDestination);
        db.execSQL(createTablePersons);
        db.execSQL(createTableSessionDest);
        db.execSQL(createTableSession);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + PERSONSTABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DESTINATIONTABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LOCATIONTABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SESSIONDESTTABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SESSIONTABLE);

        onCreate(db);
    }

    @Override
    public boolean Save(DOSession session) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id;
        try {
            ContentValues cv = new ContentValues();
            cv.put(PRENAME, session.person.prename);
            cv.put(SURNAME, session.person.surname);
            id = (int) db.insert(PERSONSTABLE, null, cv);
            session.person.setID(id);
            cv.clear();

            cv.put(PLACE, session.startLocation.city);
            cv.put(POSTALCODE, session.startLocation.postCode);
            cv.put(STREET, session.startLocation.street);
            cv.put(HOUSENO, session.startLocation.streetNumber);
            id = (int) db.insert(LOCATIONTABLE, null, cv);
            session.startLocation.setId(id);
            cv.clear();

            cv.put(STARTLOCATION, session.startLocation.id);
            cv.put(PERSONID, session.person.id);
            cv.put(STARTDATE, session.startDate.toString());
            cv.put(TITLE, session.title);
            id = (int) db.insert(SESSIONTABLE, null, cv);
            session.setId(id);
            cv.clear();

            for (DODestination dest : session.getStations()) {
                cv.put(PLACE, dest.location.city);
                cv.put(POSTALCODE, dest.location.postCode);
                cv.put(STREET, dest.location.street);
                cv.put(HOUSENO, dest.location.streetNumber);
                id = (int) db.insert(LOCATIONTABLE, null, cv);
                dest.location.setId(id);
                cv.clear();

                cv.put(FOODCOSTS, dest.foodCosts);
                cv.put(SLEEPCOSTS, dest.sleepCosts);
                cv.put(TRIPEXTRACOSTS, dest.tripExtraCosts);
                cv.put(ARRIVALDATE, dest.arrivalDate.toString());
                cv.put(DEPARTUREDATE, dest.departureDate.toString());
                cv.put(DESTLOCATIONID, dest.location.id);
                id = (int) db.insert(DESTINATIONTABLE, null, cv);
                dest.setId(id);
                cv.clear();

                cv.put(SESSIONID, session.id);
                cv.put(DESTINATIONID, dest.id);
                db.insert(SESSIONDESTTABLE, null, cv);
            }


        } catch (Exception ex) {
            return false;

        }
        return true;
    }

    @Override
    public DOSession Load(int id) throws SaveLoadException {
        try {
            String query = String.format("%2$s WHERE s%1$s = ?", PK, queryForSession);

            String destQuery = String.format("%1$s WHERE %2$s = ?", queryForDestination, SESSIONID);
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(destQuery, new String[]{Integer.toString(id)});

            List<DODestination> destinations = getDestiationsFromCursor(cursor);
            cursor.close();

            cursor = db.rawQuery(query, new String[]{Integer.toString(id)});
            DOSession session = getSession(cursor.getInt(cursor.getColumnIndex("s" + PK)),
                    destinations, cursor);
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

            Cursor cursor = db.rawQuery(queryForSession, null);
            while (cursor.moveToNext()) {
                sessionId = cursor.getInt(cursor.getColumnIndex("s" + PK));
                cursorForDestinations = db.rawQuery(queryForDestination, null);

                destinations = getDestiationsFromCursor(cursorForDestinations);
                cursorForDestinations.close();
                listSessions.add(getSession(sessionId, destinations, cursor));
            }
            cursor.close();
            return listSessions;
        } catch (Exception ex) {
            throw new SaveLoadException(ex.getMessage());
        }
    }

    List<DODestination> getDestiationsFromCursor(Cursor cursor) {
        List<DODestination> destinations = new LinkedList<>();
        while (cursor.moveToNext()) {
            Date arrival = null;
            Date departure = null;
            try {
                arrival = dateFormat.parse(cursor.getString(cursor.getColumnIndex(ARRIVALDATE)));
                departure = dateFormat.parse(cursor.getString(cursor.getColumnIndex(DEPARTUREDATE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            destinations.add(
                    new DODestination(
                            cursor.getInt(cursor.getColumnIndex("d" + PK)),
                            cursor.getInt(cursor.getColumnIndex(SLEEPCOSTS)),
                            cursor.getInt(cursor.getColumnIndex(FOODCOSTS)),
                            cursor.getInt(cursor.getColumnIndex(TRIPEXTRACOSTS)),
                            new DOLocation(
                                    cursor.getInt(cursor.getColumnIndex(DESTLOCATIONID)),
                                    cursor.getString(cursor.getColumnIndex(STREET)),
                                    cursor.getString(cursor.getColumnIndex(POSTALCODE)),
                                    cursor.getString(cursor.getColumnIndex(PLACE)),
                                    cursor.getInt(cursor.getColumnIndex(HOUSENO))),
                            arrival,
                            departure
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
                new DOPerson(cursor.getInt(cursor.getColumnIndex("p" + PK)),
                        cursor.getString(cursor.getColumnIndex(PRENAME)), cursor.getString(cursor.getColumnIndex(SURNAME))),
                new DOLocation(
                        cursor.getInt(cursor.getColumnIndex("l" + PK)),
                        cursor.getString(cursor.getColumnIndex(STREET)),
                        cursor.getString(cursor.getColumnIndex(POSTALCODE)),
                        cursor.getString(cursor.getColumnIndex(PLACE)),
                        cursor.getInt(cursor.getColumnIndex(HOUSENO))
                ),
                start
        );

    }
}


