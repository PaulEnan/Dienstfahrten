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
public enum Language {
    German("de"),
    English("en");
    
    String asString;
    
    Language(String asString) {
        this.asString = asString;
    }
}
