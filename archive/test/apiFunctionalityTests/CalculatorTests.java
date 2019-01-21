package apiFunctionalityTests;

import apifunctionality.ApiUser;
import apifunctionality.Calculator;
import apifunctionality.Language;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Paul Enan
 */
public class CalculatorTests {

    @Test
    public void manualTest() {
        Calculator calculator = new Calculator(new ApiUser());
        String money = calculator.calculateVariableCosts("Berlin", "Reinbeker Redder", 1, 15, Language.German);
        String asd = money;
    }

    @Test
    public void manualTestOfMultipleInstances() {
        Calculator calculator = new Calculator(new ApiUser());
        String money = calculator.caclculateCostsForMultipleStations(new String[]{"Berlin", "Reinbeker Redder", "München", "Rome"}, 1, 15, Language.German);
        String asd = money;
    }
}
