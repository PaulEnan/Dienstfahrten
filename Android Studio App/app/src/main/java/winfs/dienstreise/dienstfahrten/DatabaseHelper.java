/*
 * @author Joachim Borgloh
 */
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class SaveLoadHandler implements ISaveLoadHandler {

	private Connection connection;

	private void connect() {
		if (connection != null)
			disconnect();
		try {
			// db parameters
			String url = "jdbc:sqlite:./project.db";
			// create a connection to the database
			connection = DriverManager.getConnection(url);

			System.out.println("Connection to SQLite has been established.");

			createTablesIfNeeded();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			if (connection != null)
				disconnect();
		}
	}

	private void disconnect() {
		try {
			if (connection != null)
				connection.close();
			connection = null;

			System.out.println("Connection to SQLite has been disconnected.");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			connection = null;
		}
	}

	private Boolean checkTableExists(String tableName) {
		if (connection == null)
			connect();

		String tableExistsSql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?;";

		try (PreparedStatement stmt = connection.prepareStatement(tableExistsSql)) {
			stmt.setString(1, tableName);
			ResultSet lcl_Result = stmt.executeQuery();

			return lcl_Result.next();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	private void createTablesIfNeeded() {
		if (connection == null)
			connect();

		if (!checkTableExists("Destination")) {
			try (Statement stmt = connection.createStatement()) {
				System.out.println("Creating Table Destination");
				stmt.execute(
						"CREATE TABLE Destination (id integer primary key, sleepCosts double, foodCosts double, tripExtraCosts double, location text, occasion text)");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		// Names Tabelle
		if (!checkTableExists("Persons")) {
			try (Statement stmt = connection.createStatement()) {
				System.out.println("Creating Table Persons");
				stmt.execute("CREATE TABLE Persons (id integer primary key, prename text, surname text)");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

		if (!checkTableExists("SessionData")) {
			try (Statement stmt = connection.createStatement()) {
				System.out.println("Creating Table SessionData");
				stmt.execute(
						"CREATE TABLE SessionData(id integer primary key, duration integer, variablecosts double, title text, startLocation text, startDate text)"); 
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

		if (!checkTableExists("SessionDestination")) {
			try (Statement stmt = connection.createStatement()) {
				System.out.println("Creating Table SessionLocations");
				stmt.execute(
						"CREATE TABLE SessionDestination (dest_id integer, session_id integer, ind integer, foreign key(dest_id) references Destination(id), foreign key(session_id) references Session(id))");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		// N:M Tabelle Namen Session
		if (!checkTableExists("SessionPerson")) {
			try (Statement stmt = connection.createStatement()) {
				System.out.println("Creating Table SessionPerson");
				stmt.execute(
						"CREATE TABLE SessionPerson (person_id integer, session_id integer, ind integer, foreign key(person_id) references Persons(id), foreign key(session_id) references Session(id))");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

	}

	public void ConnectAndCheckTablesExists() {
		connect();
		disconnect();
	}

	private int GetLastInsertId() throws SQLException {
		String lcl_GetLastIdSql = "SELECT last_insert_rowid()";
		try (Statement stmt = connection.createStatement()) {
			ResultSet result = stmt.executeQuery(lcl_GetLastIdSql);
			result.next();
			return result.getInt(1);
		}
	}

	private void WriteLocations(DOSession session) throws SQLException {
		String deleteStmt = "delete from SessionDestination where session_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(deleteStmt)) {
			stmt.setInt(1, session.getId());
			stmt.execute();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		int i = 0;
		for (DODestination dest : session.getStations()) {
			if (dest.getId() != 0) { 
				String destinationUpdateSQL = "UPDATE Destination set sleepCosts = ?, foodCosts = ?, tripExtraCosts = ?, location = ?, occasion = ? where id = ?";
				try (PreparedStatement stmt = connection.prepareStatement(destinationUpdateSQL)) {
					stmt.setDouble(1, dest.getSleepCosts());
					stmt.setDouble(2, dest.getFoodCosts());
					stmt.setDouble(3, dest.getTripExtraCosts());
					stmt.setString(4, dest.getLocation());
					stmt.setString(5, dest.getOccasion());
					stmt.setInt(6, dest.getId());
					stmt.execute();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			} else {
				String destinationInsertSQL = "insert into Destination (sleepCosts, foodCosts, tripExtraCosts, location, occasion) values (?,?,?,?,?)";
				try (PreparedStatement stmt = connection.prepareStatement(destinationInsertSQL)) {
					stmt.setDouble(1, dest.getSleepCosts());
					stmt.setDouble(2, dest.getFoodCosts());
					stmt.setDouble(3, dest.getTripExtraCosts());
					stmt.setString(4, dest.getLocation());
					stmt.setString(5, dest.getOccasion());
					stmt.execute();
					dest.setId(GetLastInsertId());
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}

			String sessionDestinationInsertSql = "insert into SessionDestination (dest_id, session_id, ind) values (?,?,?)";
			try (PreparedStatement stmt = connection.prepareStatement(sessionDestinationInsertSql)) {
				stmt.setInt(1, dest.getId());
				stmt.setInt(2, session.getId());
				stmt.setInt(3, i);
				stmt.execute();
				dest.setId(GetLastInsertId());
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

			i++;
		}
	}

	private void WriteNames(DOSession session) throws SQLException {
		String deleteStmt = "delete from SessionPerson where session_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(deleteStmt)) {
			stmt.setInt(1, session.getId());
			stmt.execute();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		int i = 0;
		for (DOPerson person : session.getNames()) {
			if (person.getId() != null) {
				String personsUpdateSql = "UPDATE Persons set prename = ?, surname = ? where id = ?";
				try (PreparedStatement stmt = connection.prepareStatement(personsUpdateSql)) {
					stmt.setString(1, person.getPreName());
					stmt.setString(2, person.getSurName());
					stmt.setInt(3, person.getId());
					stmt.execute();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			} else {
				String personsInsertSql = "insert into Persons (prename, surname) values (?,?)";
				try (PreparedStatement stmt = connection.prepareStatement(personsInsertSql)) {
					stmt.setString(1, person.getPreName());
					stmt.setString(2, person.getSurName());
					stmt.execute();
					person.setId(GetLastInsertId());
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}

			String sessionNamesInsertSql = "insert into SessionPerson (person_id, session_id, ind) values (?,?,?)";
			try (PreparedStatement stmt = connection.prepareStatement(sessionNamesInsertSql)) {
				stmt.setInt(1, person.getId());
				stmt.setInt(2, session.getId());
				stmt.setInt(3, i);
				stmt.execute();
				person.setId(GetLastInsertId());
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

			i++;
		}
	}
	
	private void WriteSessionData(DOSession session) throws SQLException {
		if (session.getId() != null) { // existiert auf DB => update
			String sessionUpdateSql = "UPDATE SessionData set duration = ?, variablecosts = ?, title = ?, startLocation = ?, startDate = ? where id = ?";
			try (PreparedStatement stmt = connection.prepareStatement(sessionUpdateSql)) {
				stmt.setInt(1, session.getDuration());
				stmt.setDouble(2, session.getVariableCosts());
				stmt.setString(3, session.getTitle());
				stmt.setString(4, session.getStartLocation());
				stmt.setString(5, session.getDate());
				stmt.setInt(6, session.getId());
				stmt.execute();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		} else { // insert
			String sessionInsertSql = "insert into SessionData (duration, variablecosts, title, startLocation, startDate) values (?,?,?,?,?)";
			try (PreparedStatement stmt = connection.prepareStatement(sessionInsertSql)) {
				stmt.setInt(1, session.getDuration());
				stmt.setDouble(2, session.getVariableCosts());
				stmt.setString(3, session.getTitle());
				stmt.setString(4, session.getStartLocation());
				stmt.setString(5, session.getDate());
				stmt.execute();
				session.setId(GetLastInsertId());
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

		WriteLocations(session);
		WriteNames(session);
	}

	@Override
	public void Save(DOSession session) throws SaveLoadException {
		if (connection == null)
			connect();

		try {
			connection.setAutoCommit(false);
			WriteSessionData(session);
			connection.commit();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			disconnect();
		}
	}

	private DODestination loadDestinationFromResultSet(ResultSet rs) throws SQLException {
		return new DODestination(rs.getInt(1), rs.getDouble(2), rs.getDouble(3), rs.getDouble(4), rs.getString(5), rs.getString(6));
	}

	//Load Names
	private DOPerson loadPersonFromResultSet (ResultSet rs) throws SQLException {
		return new DOPerson(rs.getInt(1), rs.getString(2), rs.getString(3));
	}

	private LinkedList<DODestination> loadDestinationsForSession(int sessionId) {

		String loadDestinationSql = "SELECT id, sleepCosts, foodCosts, tripExtraCosts, location, occasion FROM Destination A join SessionDestination B on A.id = B.dest_id WHERE B.session_id=?;";

		LinkedList<DODestination> result = new LinkedList<DODestination>();

		try (PreparedStatement stmt = connection.prepareStatement(loadDestinationSql)) {
			stmt.setInt(1, sessionId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				result.add(loadDestinationFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return result;
	}
	
	//Load Names
	private Set<DOPerson> loadPersonsForSession(int sessionId) {
		String loadNamesSql = "SELECT id, prename, surname FROM Persons A join SessionPerson B on A.id = B.person_id WHERE B.session_id=?;";

		Set<DOPerson> result = new HashSet<DOPerson>();

		try (PreparedStatement stmt = connection.prepareStatement(loadNamesSql)) {
			stmt.setInt(1, sessionId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				result.add(loadPersonFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return result;
	}
	
	private DOSession loadSessionFromResultSet(ResultSet rs, LinkedList<DODestination> lcs, Set<DOPerson> persons) throws SQLException {
		return new DOSession(rs.getInt(1), lcs, rs.getInt(2), rs.getDouble(3), rs.getString(4), persons, rs.getString(5),
				rs.getString(6));
	}

	private DOSession[] GetSessionsInternal(Integer sessionId) {
		LinkedList<DOSession> result = new LinkedList<DOSession>();

		if (connection == null)
			connect();
		try {
			String getSessionSql = "select id, duration, variablecosts, title, startLocation, startDate from SessionData";
			if (sessionId != null)
				getSessionSql = getSessionSql + " where id = ?;";

			try (PreparedStatement stmt = connection.prepareStatement(getSessionSql)) {
				if (sessionId != null)
					stmt.setInt(1, sessionId);
				ResultSet rs = stmt.executeQuery();

				while (rs.next()) {
					LinkedList<DODestination> sessionDestinations = loadDestinationsForSession(rs.getInt(1));
					Set<DOPerson> sessionPersons = loadPersonsForSession(rs.getInt(1));
					result.add(loadSessionFromResultSet(rs, sessionDestinations, sessionPersons));
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		} finally {
			disconnect();
		}
		return (DOSession[]) result.toArray(new DOSession[result.size()]);
	}

	@Override
	public DOSession Load(int index) throws SaveLoadException {
		DOSession[] result = GetSessionsInternal(index);
		if (result.length == 1)
			return result[0];
		return null;
	}

	@Override
	public DOSession[] getAllSessions() throws SaveLoadException {
		return GetSessionsInternal(null);
	}

//	@Override
//	public LinkedList<DoDestination> loadAllLocations() throws SaveLoadException {
//		LinkedList<DoDestination> result = new LinkedList<DoDestination>();
//		
//		if (connection == null)
//			connect();
//		String loadLocationsSql = "SELECT id, street, streetnumber, postcode, city, fullAdress FROM Location";
//
//		try (PreparedStatement stmt = connection.prepareStatement(loadLocationsSql)) {
//			ResultSet rs = stmt.executeQuery();
//
//			while (rs.next()) {
//				result.add(loadLocationFromResultSet(rs));
//			}
//		} catch (SQLException e) {
//			System.out.println(e.getMessage());
//		}
//
//		return result;
//	}

//	@Override
//	public Set<DOPerson> loadAllNames() throws SaveLoadException {
//		Set<DOPerson> result = new HashSet<DOPerson>();
//		
//		if (connection == null)
//			connect();
//		String loadNameSql = "SELECT id, name, lastName FROM Names";
//
//		try (PreparedStatement stmt = connection.prepareStatement(loadNameSql)) {
//			ResultSet rs = stmt.executeQuery();
//
//			while (rs.next()) {
//				result.add(loadNamesFromResultSet(rs));
//			}
//		} catch (SQLException e) {
//			System.out.println(e.getMessage());
//		}
//
//		return result;
//	}
}
