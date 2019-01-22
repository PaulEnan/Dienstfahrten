import java.util.HashSet;
import java.util.LinkedList;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello World");
		SaveLoadHandler slhandler = new SaveLoadHandler();
		slhandler.ConnectAndCheckTablesExists();
		try {
			SessionData testData = new SessionData(new LinkedList<Location>(), 20, 30,"test", new HashSet<Names>(), "Test", "Test2", "Test3");
			testData.addStation(new Location("teststraﬂe1", "21629", "nw", 22));
			slhandler.Save(testData);
			testData.addName(new Names("Tom", "Mueller"));
			testData.addName(new Names("Mark", "Mueller"));
			slhandler.Save(testData);
			testData.setFixCosts(100);
			slhandler.Save(testData);
			testData.addStation(new Location("teststraﬂe2", "22880", "wedel", 29));
			slhandler.Save(testData);
			testData.getStations().get(0).setStreetNumber(90);
			testData.addName(new Names("Tom", "Knoblauch"));
			slhandler.Save(testData);

			int sessionId = testData.getId();

			SessionData loaded = slhandler.Load(sessionId);
			
			System.out.println("Loadedlocations: " + loaded.getStations().size());
			System.out.println("Loadedlocations: " + slhandler.loadAllLocations().size());
			System.out.println("LoadedNames: " + slhandler.loadAllNames().size());
			
			loaded.setTitle("abcd");
			
			slhandler.Save(loaded);
					
		} catch (SaveLoadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
