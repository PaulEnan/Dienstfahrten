/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apifunctionality;

/**
 *
 * @author Paul Enan
 */
public interface ISaveLoadHandler {
    public void Save(String path, SessionData session) throws SaveLoadException;
    public SessionData Load(String path) throws SaveLoadException;
}
