/**
 *
 * @author Paul Enan
 */
public interface ISaveLoadHandler {
    public void Save(SessionData session) throws SaveLoadException;
    public SessionData Load(int index) throws SaveLoadException;
    public SessionData[] getAllSessions() throws SaveLoadException;
}
