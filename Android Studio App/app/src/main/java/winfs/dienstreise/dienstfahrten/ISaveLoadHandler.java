package winfs.dienstreise.dienstfahrten;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Paul Enan + Joachim Borgloh
 */
public interface ISaveLoadHandler {
    public void Save(DOSession session) throws SaveLoadException;
    public DOSession Load(int index) throws SaveLoadException;
    public DOSession[] getAllSessions() throws SaveLoadException;
}
