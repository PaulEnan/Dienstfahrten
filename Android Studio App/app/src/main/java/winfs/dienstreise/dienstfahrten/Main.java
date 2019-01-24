/*
 * @author Joachim Borgloh
 */
import java.util.HashSet;
import java.util.LinkedList;
//Meine Tests. Zur Visualisierung der DB Inhalte habe ich DB Browser for SQLight genutzt
public class Main {

	public static void main(String[] args) {
		// Tests
		System.out.println("Hello World");
		SaveLoadHandler slhandler = new SaveLoadHandler();
		slhandler.ConnectAndCheckTablesExists();
		try {
			DOSession testData = new DOSession(new LinkedList<DODestination>(), 20, 0.30, "test", new HashSet<DOPerson>(), "TestStart", "TestDate");
			testData.addStation(new DODestination(12.5, 22.60, 50, "ZielString", "Testen"));
			slhandler.Save(testData);
			testData.addName(new DOPerson("Tom", "Mueller"));
			testData.addName(new DOPerson("Mark", "Mueller"));
			slhandler.Save(testData);
			testData.setVariableCosts(0.5);
			slhandler.Save(testData);
			testData.addStation(new DODestination(12.5, 22.60, 50, "ZielString2", "Testen2"));
			slhandler.Save(testData);
			testData.getStations().get(0).setLocation("ModdedZiel");
			testData.addName(new DOPerson("Tom", "Knoblauch"));
			slhandler.Save(testData);


			int sessionId = testData.getId();

			DOSession loaded = slhandler.Load(sessionId);
			
			System.out.println("Loadedlocations: " + loaded.getStations().size());
//			System.out.println("Loadedlocations: " + slhandler.loadAllLocations().size());
//			System.out.println("LoadedNames: " + slhandler.loadAllNames().size());
			
			loaded.setTitle("abcd");
			
			slhandler.Save(loaded);
					
		} catch (SaveLoadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
