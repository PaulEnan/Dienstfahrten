package winfs.dienstreise.dienstfahrten;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Paul Enan + Joachim Borgloh
 */
public interface ISaveLoadHandler {
/*
*    boolean Save(DOSession session);
*    DOSession Load(int index) throws SaveLoadException;
*    List<DOSession> getAllSessions() throws SaveLoadException;
*/
    public void Save(SessionData session) throws SaveLoadException;
    public SessionData Load(int index) throws SaveLoadException;
    public SessionData[] getAllSessions() throws SaveLoadException;
    public LinkedList<Location> loadAllLocations() throws SaveLoadException;
    public Set<Names> loadAllNames() throws SaveLoadException;
}
