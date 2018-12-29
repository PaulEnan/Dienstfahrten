/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apiFunctionalityTests;

import apifunctionality.ISaveLoadHandler;
import apifunctionality.SaveLoadException;
import apifunctionality.SessionData;

/**
 *
 * @author Paul Enan
 */
public class FakeSaveLoadHandler implements ISaveLoadHandler {

    @Override
    public void Save(String path, SessionData session) throws SaveLoadException {
    }

    @Override
    public SessionData Load(String path) throws SaveLoadException {
        return null;
    }
    
}
