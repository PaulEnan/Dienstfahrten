/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winfs.dienstreise.dienstfahrten;

/**
 *
 * @author Paul Enan
 */
public interface ISaveLoadHandler {
    public void Save(SessionData session) throws SaveLoadException;
    public SessionData Load(int index) throws SaveLoadException;
    public SessionData[] getAllSessions() throws SaveLoadException;
}
