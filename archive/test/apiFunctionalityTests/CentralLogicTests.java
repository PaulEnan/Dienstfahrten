/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apiFunctionalityTests;
import apifunctionality.CentralLogic;
import apifunctionality.Language;
import java.util.Arrays;
import java.util.LinkedList;
import org.junit.Assert;
import org.junit.Test;

public class CentralLogicTests {
    
    final double delta = 0.01;
    
    @Test
    public void testCalculation() {
        
        String[] stations = new String[] {"Berlin", "Hamburg", "Warschau" };
        String resultText = "{\n\"destination_addresses\":[\"Deutschland\"],\n\"origin_addresses\":[\"Deutschland\"],\n\"rows\":[\n{\n\"elements\":[\n{\n\"distance\":{\n\"text\":\"100 km\",\n\"value\":283311\n},\n\"duration\":{\n\"text\":\"3 Stunden, 18 Minuten\",\n\"value\":11909\n},\n\"status\":\"OK\"\n}\n]\n}\n],\n\"status\":\"OK\"\n}";
        resultText = resultText.replaceAll("\n", "\\n");
        FakeApiUser apiUser = new FakeApiUser(resultText, "", "");
        CentralLogic logic = new CentralLogic(new FakeGui(), Language.German, apiUser, new FakeSaveLoadHandler(), new LinkedList(Arrays.asList(stations)), 29, 1.5, 0);
        String result = logic.calculateCosts();
        double costs = Double.parseDouble(result);
        Assert.assertEquals(costs, 329.0, delta);
    }
}
