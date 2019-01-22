
/*
 * @author Joachim Borgloh
 */

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

		if (!checkTableExists("Location")) {
			try (Statement stmt = connection.createStatement()) {
				System.out.println("Creating Table Location");
				stmt.execute(
						"CREATE TABLE Location (id integer primary key, street text, streetnumber integer, postcode text, city text)");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		// Names Tabelle
		if (!checkTableExists("Names")) {
			try (Statement stmt = connection.createStatement()) {
				System.out.println("Creating Table Names");
				stmt.execute("CREATE TABLE Names (id integer primary key, name text, lastName text)");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

		if (!checkTableExists("SessionData")) {
			try (Statement stmt = connection.createStatement()) {
				System.out.println("Creating Table SessionData");
				stmt.execute(
						"CREATE TABLE SessionData(id integer primary key, fixcosts double, variablecosts double, title text, ocassion text, date text, duration text)"); 
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

		if (!checkTableExists("SessionLocations")) {
			try (Statement stmt = connection.createStatement()) {
				System.out.println("Creating Table SessionLocations");
				stmt.execute(
						"CREATE TABLE SessionLocations (location_id integer, session_id integer, ind integer, foreign key(location_id) references Location(id), foreign key(session_id) references Session(id))");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		// N:M Tabelle Namen Session
		if (!checkTableExists("SessionNames")) {
			try (Statement stmt = connection.createStatement()) {
				System.out.println("Creating Table SessionNames");
				stmt.execute(
						"CREATE TABLE SessionNames (names_id integer, session_id integer, ind integer, foreign key(names_id) references Names(id), foreign key(session_id) references Session(id))");
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

	private void WriteLocations(SessionData session) throws SQLException {
		String deleteStmt = "delete from SessionLocations where session_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(deleteStmt)) {
			stmt.setInt(1, session.getId());
			stmt.execute();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		int i = 0;
		for (Location loc : session.getStations()) {
			if (loc.getId() != null) {
				String locationUpdateSql = "UPDATE Location set street = ?, streetnumber = ?, postcode = ?, city = ? where id = ?";
				try (PreparedStatement stmt = connection.prepareStatement(locationUpdateSql)) {
					stmt.setString(1, loc.getStreet());
					stmt.setInt(2, loc.getStreetNumber());
					stmt.setString(3, loc.getPostCode());
					stmt.setString(4, loc.getCity());
					stmt.setInt(5, loc.getId());
					stmt.execute();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			} else {
				String locationInsertSql = "insert into Location (street, streetnumber, postcode, city) values (?,?,?,?)";
				try (PreparedStatement stmt = connection.prepareStatement(locationInsertSql)) {
					stmt.setString(1, loc.getStreet());
					stmt.setInt(2, loc.getStreetNumber());
					stmt.setString(3, loc.getPostCode());
					stmt.setString(4, loc.getCity());
					stmt.execute();
					loc.setId(GetLastInsertId());
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}

			String sessionLocationInsertSql = "insert into SessionLocations (location_id, session_id, ind) values (?,?,?)";
			try (PreparedStatement stmt = connection.prepareStatement(sessionLocationInsertSql)) {
				stmt.setInt(1, loc.getId());
				stmt.setInt(2, session.getId());
				stmt.setInt(3, i);
				stmt.execute();
				loc.setId(GetLastInsertId());
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

			i++;
		}
	}

	private void WriteNames(SessionData session) throws SQLException {
		String deleteStmt = "delete from SessionNames where session_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(deleteStmt)) {
			stmt.setInt(1, session.getId());
			stmt.execute();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		int i = 0;
		for (Names name : session.getNames()) {
			if (name.getId() != null) {
				String namesUpdateSql = "UPDATE Names set name = ?, lastName = ? where id = ?";
				try (PreparedStatement stmt = connection.prepareStatement(namesUpdateSql)) {
					stmt.setString(1, name.getName());
					stmt.setString(2, name.getLastName());
					stmt.setInt(3, name.getId());
					stmt.execute();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			} else {
				String namesInsertSql = "insert into Names (name, lastName) values (?,?)";
				try (PreparedStatement stmt = connection.prepareStatement(namesInsertSql)) {
					stmt.setString(1, name.getName());
					stmt.setString(2, name.getLastName());
					stmt.execute();
					name.setId(GetLastInsertId());
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}

			String sessionNamesInsertSql = "insert into SessionNames (names_id, session_id, ind) values (?,?,?)";
			try (PreparedStatement stmt = connection.prepareStatement(sessionNamesInsertSql)) {
				stmt.setInt(1, name.getId());
				stmt.setInt(2, session.getId());
				stmt.setInt(3, i);
				stmt.execute();
				name.setId(GetLastInsertId());
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

			i++;
		}
	}
	
	private void WriteSessionData(SessionData session) throws SQLException {
		if (session.getId() != null) { // existiert auf DB => update
			String sessionUpdateSql = "UPDATE SessionData set fixcosts = ?, variablecosts = ?, title = ?, ocassion = ?, date = ?, duration = ? where id = ?";
			try (PreparedStatement stmt = connection.prepareStatement(sessionUpdateSql)) {
				stmt.setDouble(1, session.getFixCosts());
				stmt.setDouble(2, session.getVariableCosts());
				stmt.setString(3, session.getTitle());
				stmt.setString(4, session.getOcassion());
				stmt.setString(5, session.getDate());
				stmt.setString(6, session.getDuration());
				stmt.setInt(7, session.getId());
				stmt.execute();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		} else { // insert
			String sessionInsertSql = "insert into SessionData (fixcosts, variablecosts, title, ocassion, date, duration) values (?,?,?,?,?,?)";
			try (PreparedStatement stmt = connection.prepareStatement(sessionInsertSql)) {
				stmt.setDouble(1, session.getFixCosts());
				stmt.setDouble(2, session.getVariableCosts());
				stmt.setString(3, session.getTitle());
				stmt.setString(4, session.getOcassion());
				stmt.setString(5, session.getDate());
				stmt.setString(6, session.getDuration());
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
	public void Save(SessionData session) throws SaveLoadException {
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

	private Location loadLocationFromResultSet(ResultSet rs) throws SQLException {
		return new Location(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
	}
	//Load Names
	private Names loadNamesFromResultSet (ResultSet rs) throws SQLException {
		return new Names(rs.getInt(1), rs.getString(2), rs.getString(3));
	}

	private LinkedList<Location> loadLocationsForSession(int sessionId) {
		String loadLocationsSql = "SELECT id, street, streetnumber, postcode, city FROM Location A join SessionLocations B on A.id = B.location_id WHERE B.session_id=?;";

		LinkedList<Location> result = new LinkedList<Location>();

		try (PreparedStatement stmt = connection.prepareStatement(loadLocationsSql)) {
			stmt.setInt(1, sessionId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				result.add(loadLocationFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return result;
	}
	
	//Load Names
	private Set<Names> loadNamesForSession(int sessionId) {
		String loadNamesSql = "SELECT id, name, lastName FROM Names A join SessionNames B on A.id = B.names_id WHERE B.session_id=?;";

		Set<Names> result = new HashSet<Names>();

		try (PreparedStatement stmt = connection.prepareStatement(loadNamesSql)) {
			stmt.setInt(1, sessionId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				result.add(loadNamesFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return result;
	}
	
	private SessionData loadSessionFromResultSet(ResultSet rs, LinkedList<Location> lcs, Set<Names> name) throws SQLException {
		return new SessionData(rs.getInt(1), lcs, rs.getDouble(2), rs.getDouble(3), rs.getString(4), name, rs.getString(5),
				rs.getString(6), rs.getString(7));
	}

	private SessionData[] GetSessionsInternal(Integer sessionId) {
		LinkedList<SessionData> result = new LinkedList<SessionData>();

		if (connection == null)
			connect();

		try {
			String getSessionSql = "select id, fixcosts, variablecosts, title, ocassion, date, duration from SessionData";
			if (sessionId != null)
				getSessionSql = getSessionSql + " where id = ?;";

			try (PreparedStatement stmt = connection.prepareStatement(getSessionSql)) {
				if (sessionId != null)
					stmt.setInt(1, sessionId);
				ResultSet rs = stmt.executeQuery();

				while (rs.next()) {
					LinkedList<Location> sessionLocations = loadLocationsForSession(rs.getInt(1));
					Set<Names> sessionNames = loadNamesForSession(rs.getInt(1));
					result.add(loadSessionFromResultSet(rs, sessionLocations, sessionNames));
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		} finally {
			disconnect();
		}
		return (SessionData[]) result.toArray(new SessionData[result.size()]);
	}

	@Override
	public SessionData Load(int index) throws SaveLoadException {
		SessionData[] result = GetSessionsInternal(index);
		if (result.length == 1)
			return result[0];
		return null;
	}

	@Override
	public SessionData[] getAllSessions() throws SaveLoadException {
		return GetSessionsInternal(null);
	}

	@Override
	public LinkedList<Location> loadAllLocations() throws SaveLoadException {
		LinkedList<Location> result = new LinkedList<Location>();
		
		if (connection == null)
			connect();
		String loadLocationsSql = "SELECT id, street, streetnumber, postcode, city FROM Location";

		try (PreparedStatement stmt = connection.prepareStatement(loadLocationsSql)) {
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				result.add(loadLocationFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return result;
	}

	@Override
	public Set<Names> loadAllNames() throws SaveLoadException {
		Set<Names> result = new HashSet<Names>();
		
		if (connection == null)
			connect();
		String loadNameSql = "SELECT id, name, lastName FROM Names";

		try (PreparedStatement stmt = connection.prepareStatement(loadNameSql)) {
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				result.add(loadNamesFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return result;
	}
}
